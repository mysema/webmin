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
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.mysema.webmin.Configuration;
import com.mysema.webmin.Configuration.Bundle;
import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * YUICSSMinifier provides CSS minification based on YUI CSS minification
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class YuiCssMinifier implements Minifier {

    public void minify(HttpServletRequest request, InputStream in, OutputStream out,
            Bundle bundle, Configuration configuration) throws IOException {        
        InputStreamReader reader = new InputStreamReader(in, "ISO-8859-1");        
        CssCompressor compressor = new CssCompressor(reader);
        reader.close();

        Writer ow = new OutputStreamWriter(out);
        compressor.compress(ow, configuration.getLineBreakPos());
        ow.flush();
    }

}
