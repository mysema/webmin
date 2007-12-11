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

import com.mysema.webmin.jsmin.JSMin;

/**
 * JsminJsMinifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class JsminJsMinifier extends Minifier {

    @Override
    protected void minify(InputStream in, String encoding,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream out) throws IOException {

        JSMin jsmin = new JSMin(in, out);
        try {
            jsmin.jsmin();
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }

    }

}
