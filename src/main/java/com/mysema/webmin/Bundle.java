/**
 * 
 */
package com.mysema.webmin;

import java.util.*;

import javax.servlet.ServletContext;

import com.mysema.commons.lang.Assert;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Bundle provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class Bundle {
    
    @XStreamAsAttribute
    @XStreamAlias("extends")
    private String _extends;

    private long maxage;

    @XStreamAsAttribute
    private String name;

    private transient String localName;

    @XStreamAsAttribute
    private String path;

    private List<Resource> resources = new ArrayList<Resource>();
    
    private transient Map<String,Resource> resourceByPath;

    @XStreamAsAttribute
    private String type; // default is "javascript"
    
    public String getLocalName() {
        return localName;
    }

    public long getMaxage() {
        return maxage;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Resource getResourceForPath(String path) {
        return resourceByPath.get(path);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public String getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    void initialize(Configuration c, ServletContext context) {
        if (resources == null){
            resources = new ArrayList<Resource>();
        }        
        if (type == null){
            type = "javascript";
        }        
        if (path != null){
            localName = path.substring(path.lastIndexOf('/') + 1);
        }
        // handle wildcards
        List<Resource> wildcards = new ArrayList<Resource>(resources.size());
        List<Resource> additions = new ArrayList<Resource>();
        for (Resource resource : resources){
            // add base path
            if (c.getBasePath() != null && !resource.getPath().startsWith("/")) {
                resource.addPathPrefix(c.getBasePath());
            }
            if (resource.getPath().contains("*")){
                int index = resource.getPath().indexOf('*');                
                String prefix = resource.getPath().substring(0, index);
                String suffix = null;
                if (index != resource.getPath().length() -1){
                    suffix = resource.getPath().substring(index +1);
                }
                Set<String> paths = context.getResourcePaths(prefix);
                if (paths != null){
                    for (String path : paths){
                        if (path.contains(".svn")) continue;                        
                        if (suffix == null || path.endsWith(suffix)){
                            additions.add(new Resource(path,false,false));    
                        }                        
                    }    
                }                
                wildcards.add(resource);
            }
        }
        resources.removeAll(wildcards);
        resources.addAll(additions);
    
        if (_extends != null) {            
            // process extends
            Set<Resource> res = new LinkedHashSet<Resource>();
            for (String name : _extends.split(",")) {
                Bundle parent = c.getBundleByName(name);
                parent.initialize(c, context);
                res.addAll(parent.getResources());
            }
            resources.addAll(0, res);
            _extends = null;
        }
        
        resourceByPath = new HashMap<String,Resource>();
        for (Resource resource : resources){            
            resourceByPath.put(resource.getPath(), resource);
        }
        
    }

    public void setExtends(String _extends) {
        this._extends = _extends;
    }

    public void setMaxage(long maxage) {
        this.maxage = maxage;
    }

    public void setName(String name) {
        this.name = Assert.notNull(name);
    }

    public void setPath(String path) {
        this.path = Assert.notNull(path);        
    }

    public void setType(String type) {
        this.type = Assert.notNull(type);
    }
    
    @Override
    public String toString(){
        return resources.toString();
    }
}