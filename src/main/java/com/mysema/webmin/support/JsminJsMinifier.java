package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
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
public class JsminJsMinifier extends Minifier {

    public void minify(Configuration.Bundle bundle, String encoding,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream out) throws IOException, ServletException {

        // iterate over resource and write the content
        for (String resource : bundle.getResources()) {
            InputStream in = getStreamForResource(resource, request, response);
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

}
