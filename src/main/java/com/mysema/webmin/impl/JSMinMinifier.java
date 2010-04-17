/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import com.mysema.webmin.conf.Bundle;
import com.mysema.webmin.conf.Configuration;

/**
 * JsminJsMinifier provides JavaScript minification based on the JSMin library
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class JSMinMinifier implements Minifier {
    
    public void minify(HttpServletRequest request, InputStream in, OutputStream out,
            Bundle bundle, Configuration configuration) throws IOException {
        new JSMin(in, out).jsmin();
        out.flush();
    }

}
