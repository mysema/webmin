package com.mysema.webmin.impl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.mysema.webmin.conf.Bundle;
import com.mysema.webmin.conf.Configuration;
import com.mysema.webmin.conf.Resource;

public class CSSImportMinifierTest {

    @Test
    public void Minify() throws IOException {
        Minifier minifier = new CSSImportMinifier();
        Configuration configuration = new Configuration();
        Bundle bundle = new Bundle();
        bundle.getResources().add(new Resource("/path1",true,false));
        bundle.getResources().add(new Resource("/path2",false,true));
        MockHttpServletRequest request = new MockHttpServletRequest("GET","/index.html");
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes("UTF-8"));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        minifier.minify(request, input, output, bundle, configuration);
        assertEquals("@import url(null?path=/path1);\n@import url(null?path=/path2);\n", new String(output.toByteArray(),"UTF-8"));
    }

}
