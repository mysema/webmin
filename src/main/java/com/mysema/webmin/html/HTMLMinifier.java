/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.html;

import java.util.regex.Pattern;

/**
 * MinfierWriterAdapter provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HTMLMinifier {
    
    private static final Pattern ws = Pattern.compile("\\s+");
    
    private static final Pattern startTagWs = Pattern.compile("\\s>");
        
    private static String blockElements = 
    "(address|blockquote|center|div|dir|frameset|" +
     "h1|h2|h3|h4|h5|h6|hr|isindex|noframes|noscript|" +
     "p|block|pre|head|body|html|script|link|title)";
        
    private static final Pattern blockStart = Pattern.compile("\\s?<" + blockElements);
    
    private static final Pattern blockEnd = Pattern.compile("\\s?</" + blockElements + ">\\s?");
    
    public static String minify(String s) {
        s = ws.matcher(s).replaceAll(" ");
        
        s = startTagWs.matcher(s).replaceAll(">");
        s = blockStart.matcher(s).replaceAll("<$1");
        s = blockEnd.matcher(s).replaceAll("</$1>");       
        
        return s;
    }
    
}
