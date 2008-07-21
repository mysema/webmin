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
import com.mysema.webmin.support.JsminJsMinifier;
import com.mysema.webmin.support.Minifier;
import com.mysema.webmin.support.YuiCssMinifier;
import com.mysema.webmin.support.YuiJsMinifier;
import com.mysema.webmin.util.CompositeInputStream;
import com.mysema.webmin.util.ResourceUtil;

/**
 * MinifierHandler provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class MinifierHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(MinifierServlet.class);

    private final Configuration configuration;

    private final Map<String, Minifier> minifiers = new HashMap<String, Minifier>();

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
        minifiers.put("css", new YuiCssMinifier());
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
            RequestDispatcher dispatcher = servletContext.getRequestDispatcher(resource.getPath());
            MinifierResponseWrapper mres = new MinifierResponseWrapper(res);
            dispatcher.forward(req, mres);
            return new ByteArrayInputStream(mres.getBytes());            
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
            if (ifModifiedSince == -1 || lastModified > ifModifiedSince) {
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

        // unite contents
        List<InputStream> streams = new LinkedList<InputStream>();
        for (Configuration.Resource res : bundle.getResources()) {
            streams.add(getStreamForResource(res, request, response));
        }
        InputStream in = new CompositeInputStream(streams);

        try {
            // uses intermediate form, to avoid HTTP 1.1 chunking
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Minifier minifier = minifiers.get(bundle.getType());
            // minify contents
            minifier.minify(in, out, configuration);
            os.write(out.toByteArray());
        } finally {
            in.close();
            os.close();
        }

    }

}
