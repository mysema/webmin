package com.mysema.webmin.html;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * HTMLMinifierFilterTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HTMLMinifierFilterTest {

    @Test
    public void testDoFilter() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/index.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HTMLMinifierFilter filter = new HTMLMinifierFilter();
        filter.doFilter(request, response, new MockFilterChain(){
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) {
                try {
                    response.setContentType("text/html");
                    response.getWriter().append("<html>\n<body>\n</body>\n</html>");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        assertEquals("<html><body></body></html>", response.getContentAsString());
    }

}
