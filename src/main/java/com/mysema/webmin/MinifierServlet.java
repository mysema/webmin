/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import java.io.IOException;
import java.net.URL;

import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.mysema.webmin.conf.Configuration;
import com.mysema.webmin.conf.ConfigurationFactory;
import com.mysema.webmin.conf.Handler;
import com.mysema.webmin.conf.MinifierException;
import com.mysema.webmin.conf.Mode;
import com.mysema.webmin.impl.MinifierHandler;
import com.mysema.webmin.util.ResourceUtil;

/**
 * MinifierServlet provides minification and gzip compression of JavaScript and CSS content
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class MinifierServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MinifierServlet.class);
    
    private static final long serialVersionUID = 4375394993659397518L;
    
    @Nullable
    private URL confResource = null;
    
    private transient final ConfigurationFactory confFactory = new ConfigurationFactory();
    
    @Nullable
    private transient Handler handler = null;
    
    /**
     * get the servlet config parameter with the given key or return defaultValue
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    private String getParameter(String key, String defaultValue){
        String value = getServletConfig().getInitParameter(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void init() throws ServletException {        
        try {
            String configResourceLocation = getParameter("configResourceLocation", "/WEB-INF/minifier.xml");
            confResource = getServletContext().getResource(configResourceLocation);
            
            // TODO : maybe wrap this with secure reloading ?
            logger.debug("loading");                 
            Configuration configuration = confFactory.create(getServletContext(), confResource.openStream());
            configuration.setLastModified(ResourceUtil.lastModified(confResource));            
            configuration.setUseGzip(Boolean.valueOf(getParameter("useGzip","true")));
            configuration.setJavascriptCompressor(getParameter("javascriptCompressor", "jsmin"));            
            configuration.setMode(Mode.valueOf(System.getProperty("com.mysema.webmin.mode", "PRODUCTION")));            
            getServletContext().setAttribute(Configuration.class.getName(), configuration);
                        
            handler = new MinifierHandler(configuration, getServletContext());
                        
        } catch (IOException e) {
            throw new MinifierException(e.getMessage(), e);
        } catch (SAXException e) {
            throw new MinifierException(e.getMessage(), e);
        }
    }
    
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        handler.handle(request, response);        
    }
       
     
}
