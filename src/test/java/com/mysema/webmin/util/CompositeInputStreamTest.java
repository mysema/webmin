package com.mysema.webmin.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Test;


/**
 * CompositeInputStreamTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class CompositeInputStreamTest {

    @Test
    public void test() throws IOException{
        InputStream input = new CompositeInputStream(Arrays.<InputStream>asList(
            new ByteArrayInputStream("Hello".getBytes()),
            new ByteArrayInputStream(" World".getBytes())
        ));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
        assertEquals("Hello World", new String(output.toByteArray()));
    }
}
