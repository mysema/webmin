package com.mysema.webmin.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.mysema.webmin.conf.Bundle;
import com.mysema.webmin.conf.Configuration;

public class NullMinifierTest {

    @Test
    public void Minify() throws IOException {
        NullMinifier minifier = NullMinifier.DEFAULT;
        Configuration configuration = new Configuration();
        Bundle bundle = new Bundle();
        MockHttpServletRequest request = new MockHttpServletRequest("GET","/index.html");
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes("UTF-8"));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        minifier.minify(request, input, output, bundle, configuration);
        assertEquals("Hello World", new String(output.toByteArray(),"UTF-8"));
    }

}
