package com.mysema.webmin;

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
        assertAvailable("/test/res/bookmarks.min.css");
    }
    
    @Test
    public void testJavascript() throws MalformedURLException, IOException, SAXException{
        assertAvailable("/test/res/deletetag.min.js");
        assertAvailable("/test/res/edit.min.js");
        assertAvailable("/test/res/renametag.min.js");
        assertAvailable("/test/res/subscribe.min.js");
        assertAvailable("/test/res/toggle.min.js");
    }
    
    protected String baseUrl(){
        return "http://localhost:8080";
    }

    @Override
    protected ServletUnitClient createClient() throws IOException, SAXException {
        ServletRunner sr = new ServletRunner(new File("src/test/webapp/WEB-INF/web.xml"), "/test");
        return sr.newClient();
    }


}
