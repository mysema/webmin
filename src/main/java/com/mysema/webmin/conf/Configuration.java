/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.conf;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.servlet.ServletContext;

import com.mysema.commons.lang.Assert;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Configuration of webmin
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class Configuration {
    
    @XStreamAsAttribute
    private String basePath;

    private Set<Bundle> bundles = new HashSet<Bundle>();
    
    private transient Map<String, Bundle> bundlesByName = new HashMap<String, Bundle>();

    private transient Map<String, Bundle> bundlesByPath = new HashMap<String, Bundle>();

//    private transient boolean debug;
    private transient Mode mode;

    private transient long lastModified;

    private transient int lineBreakPos = -1; 

    private transient boolean munge = false; 

    private transient boolean preserveAllSemiColons = true;

    private transient boolean preserveStringLiterals = true;

    private transient String targetEncoding = "UTF-8";

    private transient boolean useGzip;

    private transient boolean warn = true; 

    public String getBasePath() {
        return basePath;
    }
    
    @Nullable
    public Bundle getBundleByName(String name) {
        return bundlesByName.get(Assert.notNull(name,"name"));
    }

    @Nullable
    public Bundle getBundleByPath(String path) {
        return bundlesByPath.get(Assert.notNull(path,"path"));
    }

    public Collection<Bundle> getBundles() {
        return bundles;
    }

    public long getLastModified() {
        return lastModified;
    }

    public int getLineBreakPos() {
        return lineBreakPos;
    }

    public String getTargetEncoding() {
        return targetEncoding;
    }

    public void initialize(ServletContext context) {
        if (basePath != null && !basePath.endsWith("/")) {
            basePath += "/";
        }        
        for (Bundle bundle : bundles) {            
            if (bundle.getPath() != null){
                bundlesByPath.put(bundle.getPath(), bundle);
            }
            if (bundle.getName() != null){
                bundlesByName.put(bundle.getName(), bundle);
            }
        }
        for (Bundle bundle : bundles){
            bundle.initialize(this, context);
        }
    }

    public boolean isMunge() {
        return munge;
    }

    public boolean isPreserveAllSemiColons() {
        return preserveAllSemiColons;
    }

    public boolean isPreserveStringLiterals() {
        return preserveStringLiterals;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public boolean isWarn() {
        return warn;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setLineBreakPos(int lineBreakPos) {
        this.lineBreakPos = lineBreakPos;
    }

    public void setMunge(boolean munge) {
        this.munge = munge;
    }

    public void setPreserveAllSemiColons(boolean preserveAllSemiColons) {
        this.preserveAllSemiColons = preserveAllSemiColons;
    }

    public void setPreserveStringLiterals(boolean preserveStringLiterals) {
        this.preserveStringLiterals = preserveStringLiterals;
    }

    public void setTargetEncoding(String encoding) {
        this.targetEncoding = encoding;
    }

    public void setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
    }

    public void setWarn(boolean warn) {
        this.warn = warn;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
}
