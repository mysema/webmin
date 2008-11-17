/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import java.util.*;

/**
 * Configuration of webmin
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class Configuration {
    private List<Bundle> bundles = new ArrayList<Bundle>();
    private final Map<String, Bundle> bundlesByName = new HashMap<String, Bundle>();
    private final Map<String, Bundle> bundlesByPath = new HashMap<String, Bundle>();
    
    private boolean debug;
    private String javascriptCompressor;
    // general configuration
    private long lastModified;
    // YUI JS minifier configuration
    private int lineBreakPos = -1;   //Insert a line break after the specified column number    
    private boolean munge = false; //Minify only, do not obfuscate    
    
    private boolean preserveAllSemiColons = true; //Preserve unnecessary semicolons        
    private boolean preserveStringLiterals = true;    
    private String targetEncoding = "UTF-8";    
    private boolean useGzip;
    
    private boolean warn = true; //Display possible errors in the code    
    
    public void addBundle(Bundle b) {
        bundles.add(b);
        if (b.path != null){
            bundlesByPath.put(b.path, b);    
        }
        if (b.name != null){
            bundlesByName.put(b.name, b);
        }        
    }
    
    Bundle getBundleByName(String name) {
        if (name == null) throw new IllegalArgumentException("name was null");
        return bundlesByName.get(name);
    }
    
    public Bundle getBundleByPath(String path) {
        if (path == null) throw new IllegalArgumentException("path was null");
        return bundlesByPath.get(path);
    }

    public Collection<Bundle> getBundles(){
        return bundlesByPath.values();
    }
    
    public String getJavascriptCompressor() {
        return javascriptCompressor;
    }

    public long getLastModified(){
        return lastModified;
    }

    public int getLineBreakPos() {
        return lineBreakPos;
    }

    public String getTargetEncoding() {
        return targetEncoding;
    }

    public void initialize(){
        for (Bundle b : bundles){
            b.initialize(this);
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

    public void setTargetEncoding(String encoding){
        this.targetEncoding = encoding;
    }
    
    public void setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;        
    }

    public void setWarn(boolean warn) {
        this.warn = warn;
    }
        
    public static class Bundle {
        private String[] _extends;

        private long maxage;
        
        private String name;
        
        private String path, localName;
        
        private final List<Resource> resources = new ArrayList<Resource>();
        
        private String type = "javascript";

        public void addResource(String resource, boolean forward, boolean l10n) {
            if (resource == null) throw new IllegalArgumentException("resource was null");
            resources.add(new Resource(resource, forward, l10n));
        }
        public String getLocalName(){
            return localName;
        }
        
        public long getMaxage() {
            return maxage;
        }
        
        public String getName() {
            return name;
        }   
        public String getPath(){
            return path;
        }
        public Resource getResourceForPath(String path) {
            for (Resource resource : resources){
                if (resource.getPath().equals(path)) return resource;
            }
            return null;
        }
        public List<Resource> getResources() {
            return resources;
        }
        public String getType(){
            return type; 
        }
        void initialize(Configuration c) {
            if (_extends == null) return;
            Set<Resource> res = new LinkedHashSet<Resource>();
            for (String name : _extends){
                Bundle parent = c.getBundleByName(name);
                parent.initialize(c);
                res.addAll(parent.getResources());
            }           
            resources.addAll(0,res);
            _extends = null;
        }
        public void setExtends(String _extends) {
            if (_extends == null) throw new IllegalArgumentException("_extends was null");
            this._extends = _extends.split(",");
        }
        public void setMaxage(long maxage) {
            this.maxage = maxage;
        }
        public void setName(String name) {
            if (name == null) throw new IllegalArgumentException("name was null");
            this.name = name;
        }
        public void setPath(String path) {
            if (path == null) throw new IllegalArgumentException("path was null");
            this.path = path;
            this.localName = path.substring(path.lastIndexOf('/')+1);
        }
        public void setType(String type) {
            if (type == null) throw new IllegalArgumentException("type was null");
            this.type = type;
        }        
    }
     
    public static class Resource{
        private final boolean forward;
        private final String path;
        private boolean l10n; 

        public Resource(String path, boolean forward, boolean l10n) {
            this.path = path;
            this.forward = forward;
            this.l10n = l10n;
        }

        public boolean equals(Object o){
            return o instanceof Resource && ((Resource)o).path.equals(path);
        }

        public String getPath() {
            return path;
        }
        
        public boolean isForward() {
            return forward;
        }

        public boolean isL10n() {
            return l10n;
        }
        
    }
        
}
