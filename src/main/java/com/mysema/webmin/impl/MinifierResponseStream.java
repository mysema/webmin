/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * MinifierResponseStream provides
 *
 * @author Timo Westkamper
 * @version $Id$
 *
 */
class MinifierResponseStream extends ServletOutputStream {
    
    private static final Logger logger = LoggerFactory
            .getLogger(MinifierResponseStream.class);

    /**
     * The buffer through which all of our output bytes are passed.
     */
    private byte[] buffer = null;

    /**
     * The number of data bytes currently in the buffer.
     */
    private int bufferCount = 0;

    /**
     * The underlying gzip output stream to which we should write data.
     */
    private ByteArrayOutputStream baos = null;
    
    /**
     * 
     */
    private byte[] result;

    /**
     * Has this stream been closed?
     */
    private boolean closed = false;

    /**
     * Set the buffer size
     */
    protected void setBuffer(int threshold) {
        buffer = new byte[threshold];
    }

    /**
     * Close this output stream, causing any buffered data to be flushed and
     * any further output data to throw an IOException.
     */
    public void close() throws IOException {
        if (closed) {
            throw new IOException("This output stream has already been closed");
        }
        if (baos != null) {
            flushToBaos();
            baos.close();
            result = baos.toByteArray();
            baos = null;
        } 
        closed = true;
    }


    /**
     * Flush any buffered data for this output stream
     */
    public void flush() throws IOException {
        if (closed) {
            logger.warn("Cannot flush a closed output stream");
        }
        if (baos != null) {
            baos.flush();
        }
    }

    public void flushToBaos() throws IOException {
        if (bufferCount > 0) {
            writeToBaos(buffer, 0, bufferCount);
            bufferCount = 0;
        }
    }
    
    public byte[] getBytes(){
        return result;
    }

    /**
     * Write the specified byte to our output stream.
     *
     * @param b The byte to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {
        if (closed){
            throw new IOException("Cannot write to a closed output stream");
        }            
        if (bufferCount >= buffer.length) {
            flushToBaos();
        }
        buffer[bufferCount++] = (byte) b;
    }


    /**
     * Write <code>b.length bytes from the specified byte array
     * to our output stream.
     *
     * @param b The byte array to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }


    /**
     * Write <code>len bytes from the specified byte array, starting
     * at the specified offset, to our output stream.
     *
     * @param b The byte array containing the bytes to be written
     * @param off Zero-relative starting offset of the bytes to be written
     * @param len The number of bytes to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[], int off, int len) throws IOException {
        if (closed){
            throw new IOException("Cannot write to a closed output stream");
        }         
        logger.debug("writing");
        if (len == 0){
            return;
        }            

        // Can we write into buffer ?
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            return;
        }

        // There is not enough space in buffer. Flush it ...
        flushToBaos();

        // ... and try again. Note, that bufferCount = 0 here !
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            return;
        }

        // write direct to gzip
        writeToBaos(b, off, len);
    }

    public void writeToBaos(byte b[], int off, int len) throws IOException {
        if (baos == null) {
            baos = new ByteArrayOutputStream();
        }
        logger.debug("writing to baos");
        baos.write(b, off, len);

    }

    /**
     * Has this response stream been closed?
     */
    public boolean closed() {
        return closed;
    }

}