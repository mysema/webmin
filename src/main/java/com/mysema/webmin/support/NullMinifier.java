/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.mysema.webmin.Bundle;
import com.mysema.webmin.Configuration;

/**
 * NullMinifier streams 
 *
 * @author Timo Westkamper
 * @version $Id$
 */
public final class NullMinifier implements Minifier{

    public static final NullMinifier DEFAULT = new NullMinifier();
    
    private NullMinifier(){}
    
    public void minify(HttpServletRequest request, InputStream input, OutputStream output,
            Bundle bundle, Configuration configuration) throws IOException {
        IOUtils.copy(input, output);        
    }

}
