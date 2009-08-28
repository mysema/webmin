/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.util;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * SafeResponseWrapper provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public abstract class SafeResponseWrapper implements HttpServletResponse {

    private HttpServletResponse response;

    public SafeResponseWrapper(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void addCookie(Cookie cookie) {
                
    }

    @Override
    public void addDateHeader(String name, long date) {
                
    }

    @Override
    public void addHeader(String name, String value) {
                
    }

    @Override
    public void addIntHeader(String name, int value) {
                
    }

    @Override
    public boolean containsHeader(String name) {
        return response.containsHeader(name);
    }

    @Override
    public String encodeRedirectURL(String url) {
        return response.encodeRedirectURL(url);
    }

    @Override
    @SuppressWarnings("deprecation")
    public String encodeRedirectUrl(String url) {
        return response.encodeRedirectUrl(url);
    }

    @Override
    public String encodeURL(String url) {
        return response.encodeURL(url);
    }

    @Override
    @SuppressWarnings("deprecation")
    public String encodeUrl(String url) {
        return response.encodeUrl(url);
    }

    @Override
    public void sendError(int sc) throws IOException {
                
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
                
    }

    @Override
    public void sendRedirect(String location) throws IOException {
                
    }

    @Override
    public void setDateHeader(String name, long date) {
                
    }

    @Override
    public void setHeader(String name, String value) {
                
    }

    @Override
    public void setIntHeader(String name, int value) {
                
    }

    @Override
    public void setStatus(int sc) {
                
    }

    @Override
    public void setStatus(int sc, String sm) {
                
    }

    @Override
    public void flushBuffer() throws IOException {
                
    }

    @Override
    public int getBufferSize() {        
        return 0;
    }

    @Override
    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
        return response.getContentType();
    }

    @Override
    public Locale getLocale() {
        return response.getLocale();
    }

    @Override
    public boolean isCommitted() {        
        return false;
    }

    @Override
    public void reset() {        
        
    }

    @Override
    public void resetBuffer() {        
        
    }

    @Override
    public void setBufferSize(int size) {        
        
    }

    @Override
    public void setCharacterEncoding(String charset) {        
        
    }

    @Override
    public void setContentLength(int len) {        
        
    }

    @Override
    public void setContentType(String type) {        
        
    }

    @Override
    public void setLocale(Locale loc) {        
        
    }

}
