/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * HTMLMinifierFilter provides minification functionality as a Servlet filter 
 *
 * @author tiwe
 * @version $Id$
 */
public class HTMLMinifierFilter implements Filter {
    
    private static final Set<String> skipList = new HashSet<String>(Arrays.asList("js","css","gif","png","jpg","rss"));
    
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest
            && response instanceof HttpServletResponse){
            String url = ((HttpServletRequest)request).getRequestURI();
            String suffix = url.substring(url.lastIndexOf('.')+1);
            if (skipList.contains(suffix)){
                chain.doFilter(request, response);
                return;
            }
            
            final HttpServletResponse original = (HttpServletResponse)response;
            StringWriter targetWriter = new StringWriter(20 * 1024);
            final PrintWriter writer = new PrintWriter(targetWriter);
            response = new HttpServletResponseWrapper((HttpServletResponse) response){
                public PrintWriter getWriter() throws IOException {
                    String ct = original.getContentType();
                    if (ct.startsWith("text/html") || ct.startsWith("application/xhtml+xml")){
                        return writer;    
                    }else{
                        return original.getWriter();
                    }                
                }
            };
            
            chain.doFilter(request, response);
                    
            if (targetWriter.getBuffer().length() > 0){
                original.getWriter().write(HTMLMinifier.minify(targetWriter.toString()));    
            }
            
        }else{
            // TODO : error logging
            chain.doFilter(request, response);
        }
        
        
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {        
    }

}
