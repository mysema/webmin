/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.xml.sax.SAXException;

import com.mysema.webmin.conf.Bundle;
import com.mysema.webmin.conf.Configuration;
import com.mysema.webmin.conf.ConfigurationFactory;
import com.mysema.webmin.conf.Resource;


/**
 * ConfigurationTest provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class ConfigurationTest {
    
    private final ConfigurationFactory confFactory = new ConfigurationFactory();
    
    private Configuration c;
    
    @Before
    public void setUp() throws IOException, SAXException {
        ServletContext sc = new MockServletContext(new FileSystemResourceLoader());
        InputStream is = new FileInputStream("src/test/webapp/WEB-INF/minifier.xml");
        c = confFactory.create(sc, is);
    }

    @Test
    public void testLoad() throws IOException, SAXException {
        assertEquals("javascript",c.getBundleByPath("/res/deletetag.min.js").getType());
        List<Resource> resources = c.getBundleByPath("/res/deletetag.min.js").getResources();
        
        assertEquals(4,resources.size());
        Iterator<Resource> it = resources.iterator();
        assertEquals("/dwr/interface/ServiceFacade.js", it.next().getPath());
        assertEquals("/dwr/engine.js", it.next().getPath());
        assertEquals("/WEB-INF/scripts/jquery/jquery-1.2.1.js", it.next().getPath());
        
        Resource next = it.next();
        assertEquals("/WEB-INF/scripts/deletetag.js", next.getPath());
        assertEquals(true, next.isL10n());        
    }
    
    @Test
    public void testEncoding(){
        assertEquals("UTF-8", c.getTargetEncoding());
    }
    
    @Test
    public void testForward(){
        Bundle bundle = c.getBundleByName("dwr");
        assertTrue(bundle.getResources().get(0).isForward());
        
        bundle = c.getBundleByName("jquery");
        assertFalse(bundle.getResources().get(0).isForward());
    }
    
    @Test
    public void testWilcard(){
        // scripts/*;
        assertEquals(7, c.getBundleByPath("/res/all-resources.js").getResources().size());        
    }
    
    @Test
    public void testWilcard2(){
        // scripts/*.js
        assertEquals(5, c.getBundleByPath("/res/all-scripts.js").getResources().size());
    }
    
    @Test
    public void testPrint(){
        for (Bundle bundle : c.getBundles()){
            if (bundle.getName() != null) System.out.println("bundle name : " + bundle.getName());
            if (bundle.getPath() != null) System.out.println("bundle path : " + bundle.getPath());
            System.out.println("bundle type : " + bundle.getType());
            
            for (Resource  res : bundle.getResources()){
                System.out.print(" res path : " + res.getPath() );
                if (res.isForward()) System.out.print(" (forward)");
                if (res.isL10n()) System.out.print(" (l10n)");
                System.out.println();
            }
            System.out.println();
        }
    }

}
