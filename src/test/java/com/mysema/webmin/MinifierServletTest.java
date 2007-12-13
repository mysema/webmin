/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.mysema.test.httpunit.WebTestCase;

/**
 * MinifierServletTest provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class MinifierServletTest extends WebTestCase {
    
    @Test
    public void testCss() throws MalformedURLException, IOException, SAXException{
        goTo("/res/bookmarks.min.css");
        assertTrue(res.getText().contains("body"));
        assertNotNull(res.getHeaderField("Content-Encoding"));
        assertNotNull(res.getHeaderField("Expires"));
        assertNotNull(res.getHeaderField("Last-Modified"));        
    }
    
    @Test
    public void testJavascript() throws MalformedURLException, IOException, SAXException{
        goTo("/res/deletetag.min.js");
        assertTrue(res.getText().contains("jQuery"));
        
        goTo("/res/edit.min.js");
        goTo("/res/renametag.min.js");
        goTo("/res/subscribe.min.js");
        goTo("/res/toggle.min.js");
    }
    
    protected String baseUrl(){
        return "http://localhost:8080/test";
    }

    @Override
    protected ServletUnitClient createClient() throws IOException, SAXException {
        ServletRunner sr = new ServletRunner(new File("src/test/webapp/WEB-INF/web.xml"), "/test");
        return sr.newClient();
    }


}
