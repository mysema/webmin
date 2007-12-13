/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysema.webmin.Configuration;
import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * YUICSSMinifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class YuiCssMinifier implements Minifier {

    public void minify(InputStream in, Configuration configuration,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream out) throws Exception {
        
        InputStreamReader reader = new InputStreamReader(in, "ISO-8859-1");        
        CssCompressor compressor = new CssCompressor(reader);
        reader.close();

        OutputStreamWriter ow = new OutputStreamWriter(out);
        compressor.compress(ow, configuration.getLineBreakPos());
        ow.flush();
    }

}
