/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.mysema.webmin.conf.Bundle;
import com.mysema.webmin.conf.Configuration;

/**
 * YUICSSMinifier provides CSS minification based on YUI CSS minification
 * 
 * @author tiwe
 */
public class CSSMinifier implements Minifier {

    public void minify(HttpServletRequest request, InputStream in, OutputStream out,
            Bundle bundle, Configuration configuration) throws IOException {        
        InputStreamReader reader = new InputStreamReader(in, "ISO-8859-1");        
        CSSCompressor compressor = new CSSCompressor(reader);
        reader.close();

        Writer ow = new OutputStreamWriter(out);
        compressor.compress(ow, configuration.getLineBreakPos());
        ow.flush();
    }

}
