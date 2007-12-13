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
import javax.servlet.http.HttpServletResponse;

import com.mysema.webmin.Configuration;

/**
 * Minifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public interface Minifier {
    
    /**
     * 
     * @param input
     * @param configuration
     * @param request
     * @param response
     * @param output
     * @throws IOException
     */
    public void minify(InputStream input, Configuration configuration,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream output) throws Exception;
}
