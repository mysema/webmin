/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysema.webmin.Configuration;
import com.mysema.webmin.jsmin.JSMin;

/**
 * JsminJsMinifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class JsminJsMinifier implements Minifier {
    
    public void minify(InputStream in, Configuration configuration,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream out) throws Exception {
        new JSMin(in, out).jsmin();
    }

}
