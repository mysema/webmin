/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.util;

import java.io.File;
import java.net.URL;

import com.mysema.commons.lang.Assert;

/**
 * ResourceUtil provides basic utilities for URL resources
 *
 * @author Timo Westkamper
 * @version $Id$
 */
public class ResourceUtil {
    
    /**
     * Returns the last modified timestamp of the given URL
     * 
     * @param resource
     * @return
     */
    public static long lastModified(URL resource){
        return new File(Assert.notNull(resource).getFile()).lastModified();
    } 

}
