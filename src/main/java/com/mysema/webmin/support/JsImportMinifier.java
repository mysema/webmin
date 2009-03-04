/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.mysema.webmin.Configuration;
import com.mysema.webmin.Configuration.Bundle;

/**
 * JsImportMinifier provides JavaScript import statements for JavaScript debug purposes
 *
 * @author tiwe
 * @version $Id$
 */
public class JsImportMinifier implements Minifier{
    
    public void minify(HttpServletRequest req, InputStream input, OutputStream output,
            Bundle bundle, Configuration configuration) throws IOException {
        if (bundle.getResources().size() > 1){
            Writer writer = new OutputStreamWriter(output, configuration.getTargetEncoding());
            String base = req.getContextPath() + bundle.getPath();
            for (Configuration.Resource resource : bundle.getResources()){
                String path = base + "?path=" + resource.getPath();
                writer.write("document.write(\"<script src='" + path + "' type='text/javascript'></script>\");\n");
            }
            writer.flush();
        }else{
            NullMinifier.DEFAULT.minify(req, input, output, bundle, configuration);
        }               
    }

}
