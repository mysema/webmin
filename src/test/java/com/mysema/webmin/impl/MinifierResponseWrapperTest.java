package com.mysema.webmin.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * MinifierResponseWrapperTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MinifierResponseWrapperTest {

    @Test
    public void test() throws IOException{
        MockHttpServletResponse response = new MockHttpServletResponse();
        MinifierResponseWrapper res = new MinifierResponseWrapper(response);
        res.getOutputStream().write(new byte[]{0,1,2});
        res.getOutputStream().close();
        assertEquals(3, res.getBytes().length);
        
        res = new MinifierResponseWrapper(response);
        res.getWriter().append("Hello World");
        res.getWriter().close();
        assertEquals("Hello World".length(), res.getBytes().length);
    }
    

}
