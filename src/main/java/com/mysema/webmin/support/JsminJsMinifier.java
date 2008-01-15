/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mysema.webmin.Configuration;
import com.mysema.webmin.jsmin.JSMin;

/**
 * JsminJsMinifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class JsminJsMinifier implements Minifier {
    
    public void minify(InputStream in, OutputStream out,
            Configuration configuration) throws IOException {
        new JSMin(in, out).jsmin();
        out.flush();
    }

}
