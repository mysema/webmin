/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * ConfigurationTest provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class ConfigurationTest {

    @Test
    public void testLoad() throws IOException, SAXException {
        Configuration c = ConfigurationFactory.readFrom(this.getClass()
                .getResourceAsStream("/minifier.xml"));
        assertEquals("javascript",c.getBundleByPath("/res/deletetag.min.js").getType());
        List<String> resources = c.getBundleByPath("/res/deletetag.min.js").getResources();
        
        assertEquals(4,resources.size());
        Iterator<String> it = resources.iterator();
        assertEquals("/dwr/interface/ServiceFacade.js", it.next());
        assertEquals("/dwr/engine.js", it.next());
        assertEquals("/WEB-INF/scripts/jquery/jquery-1.2.1.js", it.next());
        assertEquals("/WEB-INF/scripts/deletetag.js", it.next());
    }

}
