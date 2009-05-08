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

/**
 * Configuration of webmin
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class Configuration {
    private String basePath;

    private Set<Bundle> bundles = new HashSet<Bundle>();

    private final Map<String, Bundle> bundlesByName = new HashMap<String, Bundle>();

    private final Map<String, Bundle> bundlesByPath = new HashMap<String, Bundle>();

    private boolean debug;

    private String javascriptCompressor;

    private long lastModified;

    private int lineBreakPos = -1; 

    private boolean munge = false; 

    private boolean preserveAllSemiColons = true;

    private boolean preserveStringLiterals = true;

    private String targetEncoding = "UTF-8";

    private boolean useGzip;

    private boolean warn = true; 

    public void addBundle(Bundle b) {
        bundles.add(b);
        if (b.path != null) {
            bundlesByPath.put(b.path, b);
        }
        if (b.name != null) {
            bundlesByName.put(b.name, b);
        }
    }

    public String getBasePath() {
        return basePath;
    }

    Bundle getBundleByName(String name) {
        return bundlesByName.get(Assert.notNull(name));
    }

    public Bundle getBundleByPath(String path) {
        return bundlesByPath.get(Assert.notNull(path));
    }

    public Collection<Bundle> getBundles() {
        return bundlesByPath.values();
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
        return targetEncoding;
    }

    public void initialize(ServletContext context) {
        for (Bundle b : bundles) {
            b.initialize(this, context);
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
        if (basePath != null && !basePath.endsWith("/")) {
            basePath += "/";
        }
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
