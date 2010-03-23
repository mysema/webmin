/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.conf;

/**
 * Mode provides
 *
 * @author tiwe
 * @version $Id$
 */
public enum Mode {
    /**
     * no cache & no minification
     */
    DEBUG(false,false),
    /**
     * cache & no minification
     */
    TEST(true,false),
    /**
     * cache & minification
     */
    PRODUCTION(true,true);
    
    private final boolean cached, minified;
    
    Mode(boolean cached, boolean minified){
        this.cached = cached;
        this.minified = minified;
    }

    public boolean isCached() {
        return cached;
    }

    public boolean isMinified() {
        return minified;
    }
    
}
