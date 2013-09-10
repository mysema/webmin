/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.webmin.util.SafeResponseWrapper;

/**
 * MinifierResponse provides is a wrapped response for the minification
 *
 * @author tiwe
 */
public class MinifierResponseWrapper extends SafeResponseWrapper {
    
    private static final Logger logger = LoggerFactory.getLogger(MinifierResponseWrapper.class);
    
    @Nullable
    private MinifierResponseStream stream = null;

    @Nullable
    private PrintWriter writer = null;

    private int threshold = 0;

    public MinifierResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public void setCompressionThreshold(int threshold) {
        this.threshold = threshold;
    }

    public MinifierResponseStream createOutputStream() throws IOException {
        return new MinifierResponseStream();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called for this response");
        }
        if (stream == null) {
            stream = createOutputStream();
        }
        stream.setBuffer(threshold);
        return stream;
    }
    
    @Nullable
    public byte[] getBytes(){
        if (stream != null){
            return stream.getBytes();
        }else{
            logger.error("stream is null");
        }
        return null;
    }

    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return writer;
        }
        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called for this response");
        }
        stream = createOutputStream();
        stream.setBuffer(threshold);
        // TODO : check encoding
        writer = new PrintWriter(stream);
        return writer;
    }

}
