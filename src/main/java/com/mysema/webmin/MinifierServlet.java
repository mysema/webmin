package com.mysema.webmin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.webmin.Configuration.Bundle;
import com.mysema.webmin.support.JsminJsMinifier;
import com.mysema.webmin.support.Minifier;
import com.mysema.webmin.support.YuiCssMinifier;
import com.mysema.webmin.support.YuiJsMinifier;

/**
 * MinifierServlet provides minification and gzip compression of JavaScript and CSS content
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class MinifierServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MinifierServlet.class);
    
    private static final long serialVersionUID = 4375394993659397518L;

    /**
     * location of the configuration file
     */
    private String configResourceLocation;   
    
    private Configuration configuration = null;
    
    private URL confResource;
    
    /**
     * "yuic" for YUI Compressor and "jsmin" for JSMIn
     */
    private String javascriptCompressor;
    
    private Minifier jsminMinifier = new JsminJsMinifier();
    
    /**
     * true, if reloaded on configuration file change, and false if not
     */
    private boolean reloadOnChange;
    
    /**
     * use gzip encoding for responses
     */
    private boolean useGzip;
    
    private Minifier yuiCssMinifier = new YuiCssMinifier();
    
    private Minifier yuiJsMinifier = new YuiJsMinifier();
    
    private String getParameter(String key, String defaultValue){
        String value = getServletConfig().getInitParameter(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 
     */
    public void init() throws ServletException {        
        try {
            configResourceLocation = getParameter("configResourceLocation", 
                    "/WEB-INF/minifier.xml");
            confResource = getServletContext().getResource(configResourceLocation);
            reloadOnChange = Boolean.valueOf(getParameter("reloadOnChange", 
                    "false"));
            useGzip = Boolean.valueOf(getParameter("useGzip","true"));
            javascriptCompressor = getParameter("javascriptCompressor", 
                    "jsmin");
            
            jsminMinifier.init(getServletContext(), configuration);
            yuiJsMinifier.init(getServletContext(), configuration);
            yuiCssMinifier.init(getServletContext(), configuration);
            
            // load the congiuration
            load();
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }
    }
    
    private long lastModified(Bundle bundle) throws MalformedURLException {
         long lastModified = 0l;
         for (String path : bundle.getResources()){
             URL resource = getServletContext().getResource(path);
             if (resource != null){
                 lastModified = Math.max(lastModified, lastModified(resource));    
             }             
         }
         // round down to the nearest second since client headers are in seconds
         return lastModified / 1000 * 1000;
     }

    private long lastModified(URL resource){
         return new File(resource.getFile()).lastModified();
     }   
         
     private void load(){              
        try {
            logger.debug("loading");     
            synchronized(configuration){
                Configuration newConfig = ConfigurationFactory.readFrom(confResource.openStream());
                newConfig.setLastModified(lastModified(confResource));
                getServletContext().setAttribute(Configuration.class.getName(), newConfig);
                configuration = newConfig;
            }     
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }
    }
     
     protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length()); 
        logger.debug("path = {}", path);
        
        // reload if reloadChange and configuration needs to be updated
        if (reloadOnChange && lastModified(confResource) > configuration.getLastModified()){      
            load();
        }
        
        Configuration.Bundle bundle = configuration.getBundleByPath(path);        
        
        // no bundle found
        if (bundle == null){
            String msg = "No bundle found for path "+path;
            response.getOutputStream().print(msg);    
            return;
        }            
        response.setContentType("text/"+bundle.getType());
        // TODO : different encodings for different types ?
        String encoding = configuration.getTargetEncoding();                
        long lastModified = lastModified(bundle);
       
        // check if-modified-since header
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        
        if (ifModifiedSince == -1 || lastModified > ifModifiedSince){            
            logger.debug("(creating content for {}", path);
            long start = System.currentTimeMillis();
            
            Minifier minifier = null;
            if (bundle.getType().equals("javascript")){          
                if ("jsmin".equals(javascriptCompressor)){
                    minifier = jsminMinifier;    
                }else if ("yuic".equals(javascriptCompressor)){
                    minifier = yuiJsMinifier;
                }
            } else {                
                minifier = yuiCssMinifier;
            }    
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            minifier.minify(bundle, encoding, request, response, out);                   
            byte[] content = out.toByteArray();
        
            logger.debug("created content in {} ms", System.currentTimeMillis()-start);
            
            // set Expires header
            response.setDateHeader("Last-Modified", lastModified);                
            if (bundle.getMaxage() != 0l){
                logger.debug("setting expires header");
                response.setDateHeader("Expires", lastModified + bundle.getMaxage() * 1000);
            }
            response.setCharacterEncoding(encoding);
            
            // write to servletoutputstream            
            OutputStream os = response.getOutputStream();
            String acceptEncoding = request.getHeader("Accept-Encoding");
            if (useGzip && acceptEncoding != null && acceptEncoding.contains("gzip")){
                response.setHeader("Content-Encoding", "gzip");
                os = new GZIPOutputStream(os);
            }   
            os.write(content);
        
        // not modified
        }else{
            logger.debug("{} not modified", path);
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
                 
    }
}
