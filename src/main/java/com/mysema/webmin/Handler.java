/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler is a minimal Servlet like interface for request handling
 *
 * @author Timo Westkamper
 * @version $Id$
 *
 */
public interface Handler
{
    /**
     * Handle the given request and response
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}