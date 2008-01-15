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

    public void addCookie(Cookie cookie) {
        // TODO Auto-generated method stub
        
    }

    public void addDateHeader(String name, long date) {
        // TODO Auto-generated method stub
        
    }

    public void addHeader(String name, String value) {
        // TODO Auto-generated method stub
        
    }

    public void addIntHeader(String name, int value) {
        // TODO Auto-generated method stub
        
    }

    public boolean containsHeader(String name) {
        return response.containsHeader(name);
    }

    public String encodeRedirectURL(String url) {
        return response.encodeRedirectURL(url);
    }

    public String encodeRedirectUrl(String url) {
        return response.encodeRedirectUrl(url);
    }

    public String encodeURL(String url) {
        return response.encodeURL(url);
    }

    public String encodeUrl(String url) {
        return response.encodeUrl(url);
    }

    public void sendError(int sc) throws IOException {
        // TODO Auto-generated method stub
        
    }

    public void sendError(int sc, String msg) throws IOException {
        // TODO Auto-generated method stub
        
    }

    public void sendRedirect(String location) throws IOException {
        // TODO Auto-generated method stub
        
    }

    public void setDateHeader(String name, long date) {
        // TODO Auto-generated method stub
        
    }

    public void setHeader(String name, String value) {
        // TODO Auto-generated method stub
        
    }

    public void setIntHeader(String name, int value) {
        // TODO Auto-generated method stub
        
    }

    public void setStatus(int sc) {
        // TODO Auto-generated method stub
        
    }

    public void setStatus(int sc, String sm) {
        // TODO Auto-generated method stub
        
    }

    public void flushBuffer() throws IOException {
        // TODO Auto-generated method stub
        
    }

    public int getBufferSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

    public String getContentType() {
        return response.getContentType();
    }

    public Locale getLocale() {
        return response.getLocale();
    }

    public boolean isCommitted() {
        // TODO Auto-generated method stub
        return false;
    }

    public void reset() {
        // TODO Auto-generated method stub
        
    }

    public void resetBuffer() {
        // TODO Auto-generated method stub
        
    }

    public void setBufferSize(int size) {
        // TODO Auto-generated method stub
        
    }

    public void setCharacterEncoding(String charset) {
        // TODO Auto-generated method stub
        
    }

    public void setContentLength(int len) {
        // TODO Auto-generated method stub
        
    }

    public void setContentType(String type) {
        // TODO Auto-generated method stub
        
    }

    public void setLocale(Locale loc) {
        // TODO Auto-generated method stub
        
    }

}
