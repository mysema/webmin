/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.html;

import java.util.regex.Pattern;

/**
 * HTMLMinifier provides HTML minification services
 *
 * @author tiwe
 * @version $Id$
 */
public final class HTMLMinifier {
    
    private static final String BLOCK_ELEMENTS = 
        "(address|blockquote|center|div|dir|frameset|" +
        "h1|h2|h3|h4|h5|h6|hr|isindex|noframes|noscript|" +
        "p|block|pre|head|body|html|script|link|title)";
    
    private static final Pattern BLOCK_END = Pattern.compile("\\s?</" + BLOCK_ELEMENTS + ">\\s?");
        
    private static final Pattern BLOCK_START = Pattern.compile("\\s?<" + BLOCK_ELEMENTS);
        
    private static final Pattern START_TAG_WS = Pattern.compile("\\s>");
        
    private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
        
    private HTMLMinifier(){}
    
    public static String minify(String s) {
        String rv = WHITE_SPACE.matcher(s).replaceAll(" ");
        
        rv = START_TAG_WS.matcher(rv).replaceAll(">");
        rv = BLOCK_START.matcher(rv).replaceAll("<$1");
        rv = BLOCK_END.matcher(rv).replaceAll("</$1>");       
        
        return rv;
    }
    
}
