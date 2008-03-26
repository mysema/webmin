package com.mysema.webmin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * CompositeInputStream provides
 *
 * @author Timo Westkamper
 * @version $Id$
 */
public class CompositeInputStream extends InputStream{
    
    private InputStream current;

    private List<InputStream> inputStreams;
    
    public CompositeInputStream(List<InputStream> streams){
        inputStreams = streams;
        current = inputStreams.remove(0);
    }
    
    @Override
    public void close() throws IOException {
        while (!inputStreams.isEmpty()){
            IOUtils.closeQuietly(inputStreams.remove(0));
        }
    }

    @Override
    public int read() throws IOException {
        if (current == null) 
            return -1;
        int result = current.read();
        if (result == -1) 
            result = readFromNext();
        return result;
    }
    
//    public int read(byte b[], int off, int len) throws IOException {
//        if (current == null)
//            return -1;
//        int read = current.read(b,off,len);
//        if (read < len && !inputStreams.isEmpty()){
//            current.close();
//            current = inputStreams.remove(0);
//        }
//        return read;
//    }
    
    private int readFromNext() throws IOException {
        current.close();
        current = null;
        if (inputStreams.isEmpty()) 
            return -1;
        current = inputStreams.remove(0);
        return current.read();
    }

}
