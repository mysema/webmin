/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.mozilla.javascript.ErrorReporter;

import com.mysema.webmin.conf.Bundle;
import com.mysema.webmin.conf.Configuration;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * YuiJsMinifier provides JavaScript minification based on YUI JavaScript minification
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class YuiJsMinifier implements Minifier {

    private static final ErrorReporter errorReporter = new MinifierErrorReporter();

    public void minify(HttpServletRequest request, InputStream in, OutputStream out,
            Bundle bundle, Configuration configuration) throws IOException {
        
        Writer ow = new OutputStreamWriter(out, configuration.getTargetEncoding());

        Reader reader = new InputStreamReader(in,"ISO-8859-1");
        JavaScriptCompressor compressor = new JavaScriptCompressor(reader,errorReporter);
        reader.close();

        // write the compressed content to out
        compressor.compress(ow, configuration.getLineBreakPos(),
                configuration.isMunge(), configuration.isWarn(),
                configuration.isPreserveAllSemiColons(), 
                configuration.isPreserveStringLiterals());
        ow.flush();

    }

}
