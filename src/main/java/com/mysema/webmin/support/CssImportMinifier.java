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

import com.mysema.webmin.Bundle;
import com.mysema.webmin.Configuration;
import com.mysema.webmin.Resource;

/**
 * CssImportMinifier provides a debug minifier which provides import directives 
 * for inner CSS resources
 *
 * @author tiwe
 * @version $Id$
 */
public class CssImportMinifier implements Minifier {
    
    public void minify(HttpServletRequest req, InputStream input, OutputStream output,
            Bundle bundle, Configuration configuration) throws IOException {
        if (bundle.getResources().size() > 1){
            Writer writer = new OutputStreamWriter(output, configuration.getTargetEncoding());
            String base = "@import url(" + bundle.getLocalName();
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
                writer.write(path+");\n");
            }
            writer.flush();
        }else{            
            NullMinifier.DEFAULT.minify(req, input, output, bundle, configuration);
        }
        
    }

}
