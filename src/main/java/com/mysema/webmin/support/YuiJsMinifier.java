/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.*;

import javax.servlet.http.HttpServletRequest;

import org.mozilla.javascript.ErrorReporter;

import com.mysema.webmin.Configuration;
import com.mysema.webmin.Configuration.Bundle;
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
