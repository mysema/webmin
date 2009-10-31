/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * MinifierServletTest provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class MinifierServletTest extends AbstractWebTest {
    
    @Test
    public void testCss() throws Exception{
        goTo("/res/bookmarks.min.css");
        assertTrue(res.getContentAsString().contains("body"));
//        assertNotNull(res.getHeader("Content-Encoding"));
        assertNotNull(res.getHeader("Expires"));
        assertNotNull(res.getHeader("Last-Modified"));        
    }
    
    @Test
    public void test_bug289() throws Exception{
        goTo("/res/bookmarks.min.css");
        Object exp1 = res.getHeader("Expires");
        Thread.sleep(1000);
        goTo("/res/bookmarks.min.css");
        Object exp2 = res.getHeader("Expires");
        assertFalse("Expected different Expires for second request", exp1.equals(exp2));
    }
    
    @Test
    public void testJavascript() throws Exception{
          goTo("/res/test1.js");
          goTo("/res/test2.js");
          
//        goTo("/res/deletetag.min.js");
//        assertTrue(res.getContentAsString().contains("jQuery"));
//        
//        goTo("/res/edit.min.js");
//        goTo("/res/renametag.min.js");
//        goTo("/res/subscribe.min.js");
//        goTo("/res/toggle.min.js");
    }


}
