package com.mysema.webmin;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.xml.sax.SAXException;

/**
 * EmptyConfigurationTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class EmptyConfigurationTest {
    
    @Test
    public void testEmpty() throws IOException, SAXException{
        ServletContext sc = new MockServletContext(new FileSystemResourceLoader());
        ConfigurationFactory confFactory = new ConfigurationFactory();
        Configuration conf = confFactory.create(sc, getClass().getResourceAsStream("/minifier2.xml"));
        
        assertEquals("UTF-8", conf.getTargetEncoding());
        assertEquals("/WEB-INF/", conf.getBasePath());
    }

}