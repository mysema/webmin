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
import com.mysema.webmin.jsmin.JSMin;

/**
 * JsminJsMinifier provides JavaScript minification based on the JSMin library
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class JsminJsMinifier implements Minifier {
    
    public void minify(HttpServletRequest request, InputStream in, OutputStream out,
            Bundle bundle, Configuration configuration) throws IOException {
        new JSMin(in, out).jsmin();
        out.flush();
    }

}
