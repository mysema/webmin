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
 * CssImportMinifier provides
 *
 * @author tiwe
 * @version $Id$
 */
public class CssImportMinifier implements Minifier {

    private static final NullMinifier nullMinifier = new NullMinifier();
    
    public void minify(HttpServletRequest req, InputStream input, OutputStream output,
            Bundle bundle, Configuration configuration) throws IOException {
        if (bundle.getResources().size() > 1){
            Writer writer = new OutputStreamWriter(output, configuration.getTargetEncoding());
            String base = "@import url(" + bundle.getLocalName();
            for (Configuration.Resource resource : bundle.getResources()){
                writer.write(base + "?path="+resource.getPath()+");\n");
            }
            writer.flush();
        }else{            
            nullMinifier.minify(req, input, output, bundle, configuration);
        }
        
    }

}
