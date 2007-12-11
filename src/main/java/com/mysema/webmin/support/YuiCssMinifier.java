package com.mysema.webmin.support;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysema.webmin.Configuration;
import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * YUICSSMinifier provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class YuiCssMinifier extends Minifier {

    public void minify(Configuration.Bundle bundle, String encoding,
            HttpServletRequest request, HttpServletResponse response,
            OutputStream out) throws IOException, ServletException {
        OutputStreamWriter ow = new OutputStreamWriter(out, encoding);

        // iterate over resource and write the content
        for (String resource : bundle.getResources()) {
            Reader reader = getReaderForResource(resource, request, response);
            CssCompressor compressor = new CssCompressor(reader);
            reader.close();

            // write the compressed content to out
            compressor.compress(ow, getConfiguration().getLineBreakPos());
        }

    }

}
