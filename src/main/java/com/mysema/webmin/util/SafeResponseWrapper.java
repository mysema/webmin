package com.mysema.webmin.util;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * SafeResponseWrapper provides
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class SafeResponseWrapper extends HttpServletResponseWrapper {

    /**
     * Constructs a response adaptor wrapping the given response.
     * 
     * @throws java.lang.IllegalArgumentException
     *             if the response is null
     */
    public SafeResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public void addCookie(Cookie cookie) {

    }

    public void addDateHeader(String name, long date) {

    }

    public void addHeader(String name, String value) {

    }

    public void addIntHeader(String name, int value) {

    }

    public void sendError(int sc) throws IOException {

    }

    public void sendError(int sc, String msg) throws IOException {

    }

    public void sendRedirect(String location) throws IOException {

    }

    public void setBufferSize(int size) {

    }

    public void setCharacterEncoding(String charset) {

    }

    public void setContentLength(int len) {

    }

    public void setContentType(String type) {

    }

    public void setDateHeader(String name, long date) {

    }

    public void setHeader(String name, String value) {

    }

    public void setIntHeader(String name, int value) {

    }

    public void setLocale(Locale loc) {

    }

    public void setStatus(int sc) {

    }

    public void setStatus(int sc, String sm) {

    }

}
