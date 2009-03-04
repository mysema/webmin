/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.webmin.Configuration;
import com.mysema.webmin.Handler;
import com.mysema.webmin.MinifierServlet;
import com.mysema.webmin.Configuration.Bundle;
import com.mysema.webmin.support.*;
import com.mysema.webmin.util.CompositeInputStream;
import com.mysema.webmin.util.ResourceUtil;

/**
 * MinifierHandler provides CSS and JS minification services via the Handler interface 
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class MinifierHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(MinifierServlet.class);

    private static final Minifier nullMinifier = new NullMinifier();
    
    private final Configuration configuration;

    private final Map<String, Minifier> minifiers = new HashMap<String, Minifier>();

    private final Map<String, Minifier> debugMinifiers = new HashMap<String,Minifier>();
    
    private final ServletContext servletContext;
    
    public MinifierHandler(Configuration configuration,
            ServletContext servletContext) {
        this.servletContext = servletContext;
        this.configuration = configuration;
        if (configuration.getJavascriptCompressor().equals("jsmin")) {
            minifiers.put("javascript", new JsminJsMinifier());
        } else {
            minifiers.put("javascript", new YuiJsMinifier());
        }
        if (configuration.isDebug()){
            logger.warn("Using debug mode. Do not use this in production.");
        }
        minifiers.put("css", new YuiCssMinifier());        
        debugMinifiers.put("javascript", new JsImportMinifier());
        debugMinifiers.put("css", new CssImportMinifier());
    }

    private Minifier getMinifier(Configuration.Bundle bundle) {
        if (configuration.isDebug()){
            return debugMinifiers.get(bundle.getType());    
        }else{
            return minifiers.get(bundle.getType());
        }        
    }

    /**
     * 
     * @param path
     * @param req
     * @param res
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private InputStream getStreamForResource(Configuration.Resource resource,
            HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        if (resource.isForward()){
            if (resource.isL10n()){
                throw new IllegalArgumentException("Localization is not supported for forwarded resources");
            }
            RequestDispatcher dispatcher = servletContext.getRequestDispatcher(resource.getPath());
            MinifierResponseWrapper mres = new MinifierResponseWrapper(res);
            dispatcher.forward(req, mres);
            return new ByteArrayInputStream(mres.getBytes());
            
        }else if (resource.isL10n()){    
            String path = resource.getPath();
            path = path.substring(0, path.lastIndexOf('.')) + "_" 
                + req.getParameter("locale")
                + path.substring(path.lastIndexOf('.'));
            if (servletContext.getResourceAsStream(path) != null){
                return servletContext.getResourceAsStream(path);              
            }else{
                logger.error("Got no resource for path " + path);
                return servletContext.getResourceAsStream(resource.getPath());
            }
            
        }else{
            return servletContext.getResourceAsStream(resource.getPath());
        }
    }

    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        int i = path.indexOf(';');
        if (i > -1){
            // strip jsessionid parameters etc off
            path = path.substring(0, i);
        }
        logger.debug("path = {}", path);

        Configuration.Bundle bundle = configuration.getBundleByPath(path);

        if (bundle != null) {
            // content type
            response.setContentType("text/" + bundle.getType());
            // characeter encoding
            String charsetEncoding = configuration.getTargetEncoding();
            response.setCharacterEncoding(charsetEncoding);

            // last modified header
            long lastModified = lastModified(bundle);
            response.setDateHeader("Last-Modified", lastModified);

            // expires header
            if (bundle.getMaxage() != 0l) {
                logger.debug("setting expires header");
                response.setDateHeader("Expires", System.currentTimeMillis()+ bundle.getMaxage() * 1000);
            }

            // check if-modified-since header
            long ifModifiedSince = request.getDateHeader("If-Modified-Since");
            if (configuration.isDebug() || ifModifiedSince == -1 || lastModified > ifModifiedSince) {
                OutputStream os;
                String acceptEncoding = request.getHeader("Accept-Encoding");
                if (configuration.isUseGzip() && acceptEncoding != null && acceptEncoding.contains("gzip")) {
                    response.setHeader("Content-Encoding", "gzip");
                    os = new GZIPOutputStream(response.getOutputStream());
                } else {
                    os = response.getOutputStream();
                }
       
                long start = System.currentTimeMillis();
                streamBundle(bundle, os, charsetEncoding, request, response);
                logger.debug("created content in {} ms", System.currentTimeMillis()- start);
            } else {
                logger.debug("{} not modified", path);
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }

        } else {
            String msg = "No bundle found for path " + path;
            response.getOutputStream().print(msg);
            return;
        }

    }

    /**
     * Returns the last modified timestamp of the given Bundle
     * 
     * @param bundle
     * @return
     * @throws MalformedURLException
     */
    private long lastModified(Bundle bundle) throws MalformedURLException {
        long lastModified = 0l;
        for (Configuration.Resource resource : bundle.getResources()) {
            if (!resource.isForward()){
                URL url = servletContext.getResource(resource.getPath());
                lastModified = Math.max(lastModified, ResourceUtil.lastModified(url));
            }
        }
        // round down to the nearest second since client headers are in seconds
        return lastModified / 1000 * 1000;
    }

    /**
     * 
     * @param bundle
     * @param os
     * @param encoding
     * @param request
     * @param response
     * @throws Exception
     */
    private void streamBundle(Configuration.Bundle bundle, OutputStream os,
            String encoding, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        String path = request.getParameter("path");

        InputStream in;
        Minifier minifier;
        // partial bundle streaming is only supported in debug mode
        if (path != null && configuration.isDebug()){
            Configuration.Resource res = bundle.getResourceForPath(path);
            if (path != null){
                in = getStreamForResource(res, request, response);
            }else{
                in = new ByteArrayInputStream(("No resource for path " + path).getBytes());
            }
            minifier = nullMinifier;
        }else{
            // unite contents
            List<InputStream> streams = new LinkedList<InputStream>();
            for (Configuration.Resource res : bundle.getResources()) {
                streams.add(getStreamForResource(res, request, response));
            }
            in = new CompositeInputStream(streams);
            minifier = getMinifier(bundle);
        }
        
        try {
            // uses intermediate form, to avoid HTTP 1.1 chunking
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            // minify contents
            minifier.minify(request, in, out, bundle, configuration);
            os.write(out.toByteArray());
        } finally {
            in.close();
            os.close();
        }

        

    }

}
