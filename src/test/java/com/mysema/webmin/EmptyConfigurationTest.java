/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.xml.sax.SAXException;

import com.mysema.webmin.conf.Configuration;
import com.mysema.webmin.conf.ConfigurationFactory;

public class EmptyConfigurationTest {
    
    @Test
    public void Empty() throws IOException, SAXException{
        ServletContext sc = new MockServletContext(new FileSystemResourceLoader());
        ConfigurationFactory confFactory = new ConfigurationFactory();
        Configuration conf = confFactory.create(sc, getClass().getResourceAsStream("/minifier2.xml"));
        
        assertEquals("UTF-8", conf.getTargetEncoding());
        assertEquals("/WEB-INF/", conf.getBasePath());
    }

}
