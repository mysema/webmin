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
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.webmin.Configuration;
import com.mysema.webmin.Handler;
import com.mysema.webmin.MinifierServlet;
import com.mysema.webmin.Configuration.Bundle;
import com.mysema.webmin.support.JsminJsMinifier;
import com.mysema.webmin.support.Minifier;
import com.mysema.webmin.support.YuiCssMinifier;
import com.mysema.webmin.util.ResourceUtil;

/**
 * MinifierHandler provides
 *
 * @author Timo Westkamper
 * @version $Id$
 */
public class MinifierHandler implements Handler{
    
    private static final Logger logger = LoggerFactory.getLogger(MinifierServlet.class);
    
    private final Map<String,Minifier> minifiers = new HashMap<String,Minifier>();
        
    private final ServletContext servletContext;
    
    private final Configuration configuration;
    
    public MinifierHandler(Configuration configuration, ServletContext servletContext){
        this.servletContext = servletContext;
        this.configuration = configuration;     
        minifiers.put("javascript", new JsminJsMinifier());
        minifiers.put("css", new YuiCssMinifier());
    }

    public void handle(HttpServletRequest request,
           HttpServletResponse response) throws IOException, ServletException {
       String path = request.getRequestURI().substring(request.getContextPath().length()); 
       logger.debug("path = {}", path);
                  
       Configuration.Bundle bundle = configuration.getBundleByPath(path);
       
       if (bundle != null){
           // content type
           response.setContentType("text/"+bundle.getType());
           // characeter encoding
           String charsetEncoding = configuration.getTargetEncoding();     
           response.setCharacterEncoding(charsetEncoding);
           
           // last modified header
           long lastModified = lastModified(bundle);           
           response.setDateHeader("Last-Modified", lastModified);
           
           // expires header
           if (bundle.getMaxage() != 0l){
               logger.debug("setting expires header");
               response.setDateHeader("Expires", lastModified + bundle.getMaxage() * 1000);
           }
           
           // check if-modified-since header
           long ifModifiedSince = request.getDateHeader("If-Modified-Since");            
           if (ifModifiedSince == -1 || lastModified > ifModifiedSince){           
               OutputStream os = response.getOutputStream();
               String acceptEncoding = request.getHeader("Accept-Encoding");
               if (configuration.isUseGzip() && acceptEncoding != null && acceptEncoding.contains("gzip")){
                   response.setHeader("Content-Encoding", "gzip");
                   os = new GZIPOutputStream(os);
               }
               
               long start = System.currentTimeMillis();                
               streamBundle(bundle, os, charsetEncoding, request, response);                
               logger.debug("created content in {} ms", System.currentTimeMillis()-start);            
           }else{
               logger.debug("{} not modified", path);
               response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
           }
           
       }else{
           String msg = "No bundle found for path "+path;
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
         for (String path : bundle.getResources()){
             URL resource = servletContext.getResource(path);
             if (resource != null){
                 lastModified = Math.max(lastModified, ResourceUtil.lastModified(resource));    
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
           HttpServletResponse response) throws IOException,ServletException{          
       
       // unite contents
       ByteArrayOutputStream out = new ByteArrayOutputStream();   
       for (String resource : bundle.getResources()) {
           InputStream in = getStreamForResource(resource,request,response);
           try{
               IOUtils.copy(in, out);    
           }finally{
               in.close();
           }           
       }
        
       // minfiy contents
       try{
           Minifier minifier = minifiers.get(bundle.getType());
           minifier.minify(new ByteArrayInputStream(out.toByteArray()), 
                   configuration, request, response, os);    
       } catch (Exception e) {
           if (e instanceof IOException){
               throw (IOException)e;
           }else if (e instanceof ServletException){
               throw (ServletException)e;
           }else{
               String error = "Caught " + e.getClass().getName();
               logger.error(error, e);
               throw new RuntimeException(error, e);
           }
       }finally{
           if (os != null){
               os.flush();
               os.close();
           }
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
   private InputStream getStreamForResource(String path,
           HttpServletRequest req, HttpServletResponse res)
           throws IOException, ServletException {
       InputStream is = servletContext.getResourceAsStream(path);
       // use RequestDispatcher if path is unavailable as resource
       if (is == null) {
           RequestDispatcher dispatcher = servletContext.getRequestDispatcher(path);
           MinifierResponseWrapper mres = new MinifierResponseWrapper(res);
           dispatcher.forward(req, mres);
           is = new ByteArrayInputStream(mres.getBytes());
       }
       return is;
   }
    


}
