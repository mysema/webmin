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
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.ErrorReporter;

import com.mysema.webmin.Configuration;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * YuiJsMinifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class YuiJsMinifier implements Minifier {

    private static final ErrorReporter errorReporter = new MinifierErrorReporter();

    public void minify(InputStream in, Configuration configuration,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream out) throws Exception {
        
        OutputStreamWriter ow = new OutputStreamWriter(out, configuration.getTargetEncoding());

        Reader reader = new InputStreamReader(in,"ISO-8859-1");
        JavaScriptCompressor compressor = new JavaScriptCompressor(reader,errorReporter);
        reader.close();

        // write the compressed content to out
        compressor.compress(ow, configuration.getLineBreakPos(),
                configuration.isMunge(), configuration.isWarn(),
                configuration.isPreserveAllSemiColons(), 
                configuration.isPreserveStringLiterals());

    }

}
