package com.mysema.webmin.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;


/**
 * MinifierRequestWrapperTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MinifierRequestWrapperTest {
    
    @Test
    public void test(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MinifierRequestWrapper req = new MinifierRequestWrapper(request);
        request.addHeader("If-None-Match", "XXX");
        request.addHeader("X", "Y");
        request.addHeader("Y", "Z");
        assertNull(req.getHeader("If-None-Match"));
        assertEquals("Y",req.getHeader("X"));
        assertEquals("Z",req.getHeader("Y"));
        
    }

}
