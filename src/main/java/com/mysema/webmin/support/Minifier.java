/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import com.mysema.webmin.Bundle;
import com.mysema.webmin.Configuration;

/**
 * Minifier defines the interface for minification of JavaScript and CSS resources
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public interface Minifier {
    
    /**
     * minify
     * 
     * @param request TODO
     * @param input
     * @param output 
     * @param bundle
     * @param configuration
     * @param request
     * @throws IOException
     */
    public void minify(HttpServletRequest request, InputStream input, OutputStream output,
            Bundle bundle, Configuration configuration) throws IOException;
}
