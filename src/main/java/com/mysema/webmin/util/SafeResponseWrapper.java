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
        
        
    }

    public void addDateHeader(String name, long date) {
        
        
    }

    public void addHeader(String name, String value) {
        
        
    }

    public void addIntHeader(String name, int value) {
        
        
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
        
        
    }

    public void sendError(int sc, String msg) throws IOException {
        
        
    }

    public void sendRedirect(String location) throws IOException {
        
        
    }

    public void setDateHeader(String name, long date) {
        
        
    }

    public void setHeader(String name, String value) {
        
        
    }

    public void setIntHeader(String name, int value) {
        
        
    }

    public void setStatus(int sc) {
        
        
    }

    public void setStatus(int sc, String sm) {
        
        
    }

    public void flushBuffer() throws IOException {
        
        
    }

    public int getBufferSize() {
        
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
        
        return false;
    }

    public void reset() {
        
        
    }

    public void resetBuffer() {
        
        
    }

    public void setBufferSize(int size) {
        
        
    }

    public void setCharacterEncoding(String charset) {
        
        
    }

    public void setContentLength(int len) {
        
        
    }

    public void setContentType(String type) {
        
        
    }

    public void setLocale(Locale loc) {
        
        
    }

}
