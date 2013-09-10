/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.mysema.webmin.conf.Bundle;
import com.mysema.webmin.conf.Configuration;

/**
 * @author tiwe
 */
public final class NullMinifier implements Minifier{

    public static final NullMinifier DEFAULT = new NullMinifier();
    
    private NullMinifier(){}
    
    public void minify(HttpServletRequest request, InputStream input, OutputStream output,
            Bundle bundle, Configuration configuration) throws IOException {
        IOUtils.copy(input, output);        
    }

}
