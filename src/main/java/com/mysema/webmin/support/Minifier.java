/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mysema.webmin.Configuration;

/**
 * Minifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public interface Minifier {
    
    /**
     * 
     * @param input
     * @param output
     * @param configuration
     * @throws IOException
     */
    public void minify(InputStream input, OutputStream output,
            Configuration configuration) throws IOException;
}
