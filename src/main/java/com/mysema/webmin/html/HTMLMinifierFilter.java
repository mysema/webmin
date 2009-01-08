/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * JspMinifierFilter provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HTMLMinifierFilter implements Filter {
    
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String url = ((HttpServletRequest)request).getRequestURI();
        // TODO : improve this
        if (url.endsWith(".js") || url.endsWith(".css")){
            chain.doFilter(request, response);
            return;
        }
        
        ServletResponse original = response;
        StringWriter targetWriter = new StringWriter(20 * 1024);
        final PrintWriter writer = new PrintWriter(targetWriter);
        response = new HttpServletResponseWrapper((HttpServletResponse) response){
            public PrintWriter getWriter() throws IOException {
                return writer;
            }
        };
        
        chain.doFilter(request, response);
                
        if (targetWriter.getBuffer().length() > 0){
            original.getWriter().write(HTMLMinifier.minify(targetWriter.toString()));    
        }        
    }

    public void init(FilterConfig filterConfig) throws ServletException {        
    }

}
