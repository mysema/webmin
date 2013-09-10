/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

/**
 * CompositeInputStream bundles multiple InputStreams into one
 *
 * @author tiwe
 */
public class CompositeInputStream extends InputStream{
    
    @Nullable
    private InputStream current;

    private final Iterator<InputStream> inputStreams;
    
    public CompositeInputStream(List<InputStream> streams){
        inputStreams = streams.iterator();
        current = inputStreams.next();
    }
    
    @Override
    public void close() throws IOException {
        while (inputStreams.hasNext()){
            IOUtils.closeQuietly(inputStreams.next());
        }
    }

    @Override
    public int read() throws IOException {
        if (current == null) {
            return -1;
        }            
        int result = current.read();
        if (result == -1) {
            result = readFromNext();
        }            
        return result;
    }
    
    private int readFromNext() throws IOException {
        current.close();
        current = null;
        if (!inputStreams.hasNext()) {
            return -1;
        }            
        current = inputStreams.next();
        return current.read();
    }

}
