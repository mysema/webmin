/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;

/**
 * ConfigurationFactory is a Factory class for Configuration instances
 *
 * @author Timo Westkamper
 * @version $Id$
 */
class ConfigurationFactory {
    
    private final XStream xstream;
    
    private final Converter resourceConverter = new ResourceConverter();

    public ConfigurationFactory(){
        xstream = new XStream();
        xstream.autodetectAnnotations(true);
        xstream.alias("minifier", Configuration.class);
        xstream.alias("bundle", Bundle.class);
        xstream.addImplicitCollection(Configuration.class, "bundles");
        xstream.aliasField("max-age", Bundle.class, "maxage");        
        xstream.alias("resource",Resource.class);       
        xstream.registerConverter(resourceConverter);
        
    }
    
    public Configuration create(ServletContext context, InputStream is) throws IOException, SAXException{
        Configuration configuration = (Configuration)xstream.fromXML(is);
        configuration.initialize(context);
        return configuration;
    }

}
