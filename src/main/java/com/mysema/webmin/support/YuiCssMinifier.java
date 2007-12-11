/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * YUICSSMinifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class YuiCssMinifier extends Minifier {

    @Override
    protected void minify(InputStream in, String encoding,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream out) throws IOException {
        
        InputStreamReader reader = new InputStreamReader(in, "ISO-8859-1");        
        CssCompressor compressor = new CssCompressor(reader);
        reader.close();

        OutputStreamWriter ow = new OutputStreamWriter(out);
        compressor.compress(ow, getConfiguration().getLineBreakPos());
        ow.flush();
    }

}
