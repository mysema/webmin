/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.webmin.Configuration;
import com.mysema.webmin.MinifierResponseWrapper;

/**
 * Minifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public abstract class Minifier {
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Configuration configuration;

    private ServletContext context;

    protected Configuration getConfiguration(){
        return configuration;
    }

    protected final InputStream getStreamForResource(String path,
            HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        InputStream is = context.getResourceAsStream(path);
        // use RequestDispatcher if path is unavailable as resource
        if (is == null) {
            RequestDispatcher dispatcher = context.getRequestDispatcher(path);
            MinifierResponseWrapper mres = new MinifierResponseWrapper(res);
            dispatcher.forward(req, mres);
            is = new ByteArrayInputStream(mres.getBytes());
        }
        return is;
    }
    
    public void init(ServletContext context, Configuration configuration) {
        this.configuration = configuration;
        this.context = context;
    }
   

    /**
     * 
     * @param bundle
     * @param encoding
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public void minify(Configuration.Bundle bundle, String encoding,
            HttpServletRequest request, HttpServletResponse response, OutputStream os)
            throws IOException, ServletException{
        // unite contents
        ByteArrayOutputStream baos = new ByteArrayOutputStream();   
        for (String resource : bundle.getResources()) {
            IOUtils.copy(getStreamForResource(resource,request,response), baos);
        }
        
        minify(new ByteArrayInputStream(baos.toByteArray()), encoding, request, response, os);
    }
    
    protected void minify(InputStream input, String encoding,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream output) throws IOException {
        IOUtils.copy(input, output);
        
    }
}
