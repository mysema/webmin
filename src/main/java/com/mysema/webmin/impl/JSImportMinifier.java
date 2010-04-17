/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.mysema.webmin.conf.Bundle;
import com.mysema.webmin.conf.Configuration;
import com.mysema.webmin.conf.Resource;

/**
 * JsImportMinifier provides JavaScript import statements for JavaScript debug purposes
 *
 * @author tiwe
 * @version $Id$
 */
public class JSImportMinifier implements Minifier{
    
    public void minify(HttpServletRequest req, InputStream input, OutputStream output,
            Bundle bundle, Configuration configuration) throws IOException {
        if (bundle.getResources().size() > 1){
            Writer writer = new OutputStreamWriter(output, configuration.getTargetEncoding());
            String base = req.getContextPath() + bundle.getPath();
            for (Resource resource : bundle.getResources()){
                String resourcePath = resource.getPath();
                if (configuration.getBasePath() != null && resourcePath.startsWith(configuration.getBasePath())){
                    resourcePath = resourcePath.substring(configuration.getBasePath().length());
                }
                StringBuilder path = new StringBuilder(base).append("?path=").append(resourcePath);
                if (resource.isL10n() && req.getParameter("locale") != null){
                    path.append("&locale=").append(req.getParameter("locale"));
                }                
                if (req.getParameter("version") != null){
                    path.append("&version=").append(req.getParameter("version"));
                }
                writer.write("document.write(\"<script src='" + path + "' type='text/javascript'></script>\");\n");
            }
            writer.flush();
        }else{
            NullMinifier.DEFAULT.minify(req, input, output, bundle, configuration);
        }               
    }

}
