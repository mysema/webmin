/**
 * 
 */
package com.mysema.webmin;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mysema.webmin.util.Assert;


/**
 * Bundle provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Bundle {
    private String[] _extends;

    private long maxage;
    
    String name;
    
    private String localName;

    String path;
    
    private final List<Resource> resources = new ArrayList<Resource>();
    
    private String type = "javascript";

    public void addResource(String resource, boolean forward, boolean l10n) {       
        resources.add(new Resource(Assert.notNull(resource), forward, l10n));
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
        if (_extends != null){
            Set<Resource> res = new LinkedHashSet<Resource>();        
            // process extends
            for (String name : _extends){
                Bundle parent = c.getBundleByName(name);
                parent.initialize(c);
                res.addAll(parent.getResources());
            }           
            resources.addAll(0,res);    
            _extends = null;
        }        
        
        // add base path
        if (c.getBasePath() != null){
            for (Resource resource : resources){
                if (!resource.getPath().startsWith("/")){
                    resource.addPathPrefix(c.getBasePath());
                }
            }
        }
    }
    public void setExtends(String _extends) {
        this._extends = Assert.notNull(_extends).split(",");
    }
    public void setMaxage(long maxage) {
        this.maxage = maxage;
    }
    public void setName(String name) {
        this.name = Assert.notNull(name);
    }
    public void setPath(String path) {
        this.path = Assert.notNull(path);
        this.localName = path.substring(path.lastIndexOf('/')+1);
    }
    public void setType(String type) {
        this.type = Assert.notNull(type);
    }        
}