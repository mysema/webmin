/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.html;

import static com.mysema.webmin.html.HTMLMinifier.minify;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * MinfierWriterAdapterTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HTMLMinifierTest {

    @Test
    public void testMinify() {
        assertEquals("<b> </b>", minify("<b>   \n\t  </b>"));
        assertEquals("</a> <a>", minify("</a> <a>"));
        assertEquals("/<a> abc", minify("/<a> \n abc"));
        
        assertEquals("<a>", minify("<a >"));
        assertEquals("ill</a> by <a href=\".\">sasa</a> 3 days ago",
            minify("ill</a> by <a href=\".\" >sasa</a>\n 3 days ago"));
        
        String markup = "von <a href=\"/bookmarks/users/admin\">admin</a>";
        assertEquals(markup, minify(markup));
        
        // block elements
        assertEquals("</ul></div></body></html>", minify("</ul> </div> </body> </html>"));
        assertEquals("</script></head><body><div>", minify("</script> </head> <body> <div>"));
        assertEquals("</title><script>", minify("</title> <script>"));
    }

}
