package com.mysema.webmin.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.mysema.webmin.Configuration;

/**
 * NullMinifier provides
 *
 * @author Timo Westkamper
 * @version $Id$
 */
public class NullMinifier implements Minifier{

    public void minify(InputStream input, OutputStream output,
            Configuration configuration) throws IOException {
        IOUtils.copy(input, output);        
    }

}