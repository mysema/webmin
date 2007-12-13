/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration of webmin
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class Configuration {
    private List<Bundle> bundles = new ArrayList<Bundle>();
    private final Map<String, Bundle> bundlesByPath = new HashMap<String, Bundle>();
    private final Map<String, Bundle> bundlesByName = new HashMap<String, Bundle>();
    
    private int lineBreakPos = -1;   //Insert a line break after the specified column number    
    private boolean warn = true; //Display possible errors in the code    
    private boolean munge = false; //Minify only, do not obfuscate    
    private boolean preserveAllSemiColons = true; //Preserve unnecessary semicolons    
    private boolean preserveStringLiterals = true;
    
    private long lastModified;
    
    private String targetEncoding = "UTF-8";
    
    private boolean useGzip;
    
    private String javascriptCompressor;
    
    public void initialize(){
        for (Bundle b : bundles){
            b.initialize(this);
        }
    }
    
    public void addBundle(Bundle b) {
        bundles.add(b);
        if (b.path != null){
            bundlesByPath.put(b.path, b);    
        }
        if (b.name != null){
            bundlesByName.put(b.name, b);
        }        
    }
    
    public Collection<Bundle> getBundles(){
        return bundlesByPath.values();
    }

    public Bundle getBundleByPath(String path) {
        if (path == null) throw new IllegalArgumentException("path was null");
        return bundlesByPath.get(path);
    }
    
    Bundle getBundleByName(String name) {
        if (name == null) throw new IllegalArgumentException("name was null");
        return bundlesByName.get(name);
    }

    public int getLineBreakPos() {
        return lineBreakPos;
    }

    public void setLineBreakPos(int lineBreakPos) {
        this.lineBreakPos = lineBreakPos;
    }

    public boolean isWarn() {
        return warn;
    }

    public void setWarn(boolean warn) {
        this.warn = warn;
    }

    public boolean isMunge() {
        return munge;
    }

    public void setMunge(boolean munge) {
        this.munge = munge;
    }

    public boolean isPreserveAllSemiColons() {
        return preserveAllSemiColons;
    }

    public void setPreserveAllSemiColons(boolean preserveAllSemiColons) {
        this.preserveAllSemiColons = preserveAllSemiColons;
    }

    public boolean isPreserveStringLiterals() {
        return preserveStringLiterals;
    }

    public void setPreserveStringLiterals(boolean preserveStringLiterals) {
        this.preserveStringLiterals = preserveStringLiterals;
    }    

    public String getTargetEncoding() {
        return targetEncoding;
    }
    
    public void setTargetEncoding(String encoding){
        this.targetEncoding = encoding;
    }
    
    public long getLastModified(){
        return lastModified;
    }
    
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    
    public void setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;        
    }
    
    public boolean isUseGzip() {
        return useGzip;
    }

    public String getJavascriptCompressor() {
        return javascriptCompressor;
    }

    public void setJavascriptCompressor(String javascriptCompressor) {
        this.javascriptCompressor = javascriptCompressor;        
    }
    
     
    public static class Bundle {
        private final List<String> resources = new ArrayList<String>();

        private long maxage;
        
        private String type = "javascript";
        
        private String[] _extends;
        
        private String name;
        
        private String path;

        public void addResource(String resource) {
            if (resource == null) throw new IllegalArgumentException("resource was null");
            resources.add(resource);
        }
        void initialize(Configuration c) {
            if (_extends == null) return;
            List<String> res = new ArrayList<String>();
            for (String name : _extends){
                Bundle parent = c.getBundleByName(name);
                parent.initialize(c);
                res.addAll(parent.getResources());
            }
            resources.addAll(0,res);
            _extends = null;
        }
        public List<String> getResources() {
            return resources;
        }   
        public String getPath(){
            return path;
        }
        public void setType(String type) {
            if (type == null) throw new IllegalArgumentException("type was null");
            this.type = type;
        }
        public void setPath(String path) {
            if (path == null) throw new IllegalArgumentException("path was null");
            this.path = path;
        }
        public String getType(){
            return type; 
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            if (name == null) throw new IllegalArgumentException("name was null");
            this.name = name;
        }
        public void setExtends(String _extends) {
            if (_extends == null) throw new IllegalArgumentException("_extends was null");
            this._extends = _extends.split(",");
        }
        public long getMaxage() {
            return maxage;
        }
        public void setMaxage(long maxage) {
            this.maxage = maxage;
        }        
    }



        
}
