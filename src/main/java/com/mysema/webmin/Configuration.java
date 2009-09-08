/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import com.mysema.commons.lang.Assert;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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
    
    private Map<String, Bundle> bundlesByName;

    private Map<String, Bundle> bundlesByPath;

    @XStreamOmitField
    private boolean debug;

    @XStreamOmitField
    private String javascriptCompressor;

    @XStreamOmitField
    private long lastModified;

    @XStreamOmitField
    private int lineBreakPos = -1; 

    @XStreamOmitField
    private boolean munge = false; 

    @XStreamOmitField
    private boolean preserveAllSemiColons = true;

    @XStreamOmitField
    private boolean preserveStringLiterals = true;

    @XStreamOmitField
    private String targetEncoding = "UTF-8";

    @XStreamOmitField
    private boolean useGzip;

    @XStreamOmitField
    private boolean warn = true; 

    public String getBasePath() {
        return basePath;
    }
    
    public Bundle getBundleByName(String name) {
        return bundlesByName.get(Assert.notNull(name));
    }

    public Bundle getBundleByPath(String path) {
        return bundlesByPath.get(Assert.notNull(path));
    }

    public Collection<Bundle> getBundles() {
        return bundles;
    }

    public String getJavascriptCompressor() {
        return javascriptCompressor;
    }

    public long getLastModified() {
        return lastModified;
    }

    public int getLineBreakPos() {
        return lineBreakPos;
    }

    public String getTargetEncoding() {
        return targetEncoding != null ? targetEncoding : "UTF-8";
    }

    public void initialize(ServletContext context) {
        if (basePath != null && !basePath.endsWith("/")) {
            basePath += "/";
        }        
        bundlesByName = new HashMap<String, Bundle>();
        bundlesByPath = new HashMap<String, Bundle>();
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

    public boolean isDebug() {
        return debug;
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

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setJavascriptCompressor(String javascriptCompressor) {
        this.javascriptCompressor = javascriptCompressor;
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

}
