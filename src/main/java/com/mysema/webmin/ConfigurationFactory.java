/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * ConfigurationFactory is a Factory class for Configuration instances
 *
 * @author Timo Westkamper
 * @version $Id$
 */
class ConfigurationFactory {
    
    /**
     * @param is
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public static Configuration readFrom(InputStream is) throws IOException, SAXException{
        Digester digester = new Digester();
        digester.addObjectCreate("minifier", Configuration.class);        
        
        digester.addObjectCreate("minifier/bundle", Configuration.Bundle.class);        
        digester.addSetProperties("minifier/bundle");
        digester.addSetNext("minifier/bundle", "addBundle", Configuration.Bundle.class.getName());
                  
        digester.addCallMethod("minifier/bundle/max-age", "setMaxage", 1, 
                new String[]{"java.lang.Long"});
        digester.addCallParam("minifier/bundle/max-age", 0);
        digester.addCallMethod("minifier/bundle/resources/resource", "addResource", 
                2, new Class[]{String.class, Boolean.class});
        digester.addCallParam("minifier/bundle/resources/resource",0);
        digester.addCallParam("minifier/bundle/resources/resource", 1, "forward");
        
        Configuration c = (Configuration)digester.parse(is);
        c.initialize();
        
        return c;
    }

}
