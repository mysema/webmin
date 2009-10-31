package com.mysema.webmin;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.Before;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

/**
 * AbstractWebTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractWebTest {
    
    private MinifierServlet servlet;
    
    protected MockHttpServletResponse res;
    
    @Before
    public void setUp() throws ServletException{
        String path = "src/test/webapp";
        ServletContext context = new MockServletContext(path, new FileSystemResourceLoader());
        ServletConfig config = new MockServletConfig(context);
        servlet = new MinifierServlet();
        servlet.init(config);
    }
    
    protected void goTo(String url) throws IOException, ServletException{
        MockHttpServletRequest request = new MockHttpServletRequest("GET",url);
        res = new MockHttpServletResponse();
        servlet.service(request, res);
    }
}
