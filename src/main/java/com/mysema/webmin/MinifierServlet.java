package com.mysema.webmin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.ErrorReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.webmin.Configuration.Bundle;
import com.mysema.webmin.jsmin.JSMin;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * MinifierServlet provides minification and gzip compression of JavaScript and CSS content
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public class MinifierServlet extends HttpServlet {

    private static final long serialVersionUID = 4375394993659397518L;

    private static Map<String, CacheEntry> cache = new ConcurrentHashMap <String, CacheEntry>();
    
    private class CacheEntry{byte[] content; long lastModified;};
    
    private static final Logger logger = LoggerFactory.getLogger(MinifierServlet.class);

    private Configuration configuration = null;   

    private ErrorReporter errorReporter = new MinifierErrorReporter();
    
    private URL confResource;
    
    // TODO : make configurable
    private String resourceLocation = "/WEB-INF/minifier.xml";
    
    // TODO : make configurable
    private boolean reloadOnChange = true;
    
    /**
     * 
     */
    public void init() throws ServletException { 
        load();
    }
    
    /**
     * 
     */
    private void load(){       
        logger.debug("loading");
        try {
            if (confResource == null){
                confResource = getServletContext().getResource(resourceLocation);    
            }            
            configuration = ConfigurationFactory.readFrom(confResource.openStream());
            configuration.setLastModified(lastModified(confResource));
            getServletContext().setAttribute(Configuration.class.getName(), configuration);
            cache.clear();
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }
    }
    
    /**
     * 
     */
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length()); 
        logger.debug("path = {}", path);
        
        if (reloadOnChange && lastModified(confResource) > configuration.getLastModified()){
            // TODO : synchronize ?            
            load();
        }
        
        Configuration.Bundle bundle = configuration.getBundleByPath(path);        
        
        // no bundle found
        if (bundle == null){
            String msg = "No bundle found for path "+path;
            response.getOutputStream().print(msg);            
            
        // boundle found
        }else{
            response.setContentType("text/"+bundle.getType());
            String encoding = configuration.getTargetEncoding();
            CacheEntry cacheEntry = cache.get(path);
            boolean create = false;
            if (cacheEntry == null){
                cacheEntry = new CacheEntry();
                create = true;
            }
            long start = System.currentTimeMillis();
            
            // determine last modified date for bundle
            long lastModified = lastModified(bundle);
           
            // check if-modified-since header
            long dateHeader = request.getDateHeader("If-Modified-Since");
            if (create || dateHeader == -1 || lastModified > dateHeader){
            
                // if the cache entry's lastModified is older than the content, recreate it
                if (create || cacheEntry.lastModified < lastModified){
                    logger.debug("(re)creating content for {}", path);
                    cacheEntry.lastModified = lastModified;
                    if (bundle.getType().equals("javascript")){          
                        // NOTE : using JSMin, since YUIC is not yet stable
                        cacheEntry.content = getCompressedJavaScriptJSMin(bundle, encoding, request, response);
                    } else {                
                        cacheEntry.content = getCompressedCss(bundle, encoding, request, response);
                    }    
                    cache.put(path, cacheEntry);
                }else{
                    logger.debug("taking content for {} from cache", path);
                }
            
                logger.debug("(re)created content in {} ms", System.currentTimeMillis()-start);
                
                response.setHeader("Content-Encoding", "gzip");
                response.setDateHeader("Last-Modified", cacheEntry.lastModified);                
                if (bundle.getMaxage() != 0l){
                    logger.debug("setting expires header");
                    response.setDateHeader("Expires", cacheEntry.lastModified + bundle.getMaxage() * 1000);
                }
                response.setCharacterEncoding(encoding);
                response.getOutputStream().write(cacheEntry.content);  
            
            // not modified
            }else{
                logger.debug("{} not modified", path);
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
        }           
    }
    


    /**
     * Note that the inputStream is closed!
     *
     * @param inputStream
     * @throws IOException
     * @throws ServletException 
     */
    private byte[] getCompressedCss(Configuration.Bundle bundle, String encoding,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter out = new OutputStreamWriter(new GZIPOutputStream(baos), encoding);
        
        // iterate over resource and write the content
        for (String resource : bundle.getResources()){
            Reader reader = getReaderForResource(resource, request, response);
            CssCompressor compressor = new CssCompressor(reader);
            reader.close();
            
            // write the compressed content to out
            compressor.compress(out, configuration.getLineBreakPos());            
        }        
        
        out.flush();
        out.close();        
        return baos.toByteArray();
    }
    
    /**
     * Note that the inputStream is closed!
     *
     * @param inputStream
     * @throws IOException
     * @throws ServletException 
     */
    private byte[] getCompressedJavaScriptYUI(Configuration.Bundle bundle, String encoding,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter out = new OutputStreamWriter(new GZIPOutputStream(baos), encoding);
        
        // iterate over resource and write the content
        for (String resource : bundle.getResources()){
            Reader reader = getReaderForResource(resource,request,response);           
            JavaScriptCompressor compressor = new JavaScriptCompressor(reader, errorReporter);
            reader.close();       
            
            // write the compressed content to out
            compressor.compress(out, configuration.getLineBreakPos(), 
                    configuration.isMunge(), configuration.isWarn(), 
                    configuration.isPreserveAllSemiColons(),
                    configuration.isPreserveStringLiterals());                          
        }        
        
        out.flush();
        out.close();
        return baos.toByteArray();
    }
    
    /**
     * 
     * @param bundle
     * @param encoding
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private byte[] getCompressedJavaScriptJSMin(Configuration.Bundle bundle, String encoding,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream out = new GZIPOutputStream(baos);
        
        // iterate over resource and write the content
        for (String resource : bundle.getResources()){
            InputStream in = getStreamForResource(resource,request,response);
            JSMin jsmin = new JSMin(in, out);
            try {
                jsmin.jsmin();
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                logger.error(error, e);
                throw new RuntimeException(error, e);
            }    
        }
        
        out.flush();
        out.close();
        return baos.toByteArray();
    }
    
    
    /**
     * 
     * @param path
     * @return
     * @throws IOException 
     * @throws ServletException 
     */
     private Reader getReaderForResource(String path,HttpServletRequest req,
             HttpServletResponse res) throws IOException, ServletException {
         InputStream is = getStreamForResource(path,req,res);
         return new InputStreamReader(is,"ISO-8859-1");
         
     }
     
     /**
      * 
      * @param path
      * @param req
      * @param res
      * @return
      * @throws IOException
      * @throws ServletException
      */
     private InputStream getStreamForResource(String path,HttpServletRequest req,
             HttpServletResponse res) throws IOException, ServletException {
         InputStream is = getServletContext().getResourceAsStream(path);
         // use RequestDispatcher if path is unavailable as resource
         if (is == null){
             RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(path);
             MinifierResponseWrapper mres = new MinifierResponseWrapper(res);
             dispatcher.forward(req, mres);
             is = new ByteArrayInputStream(mres.getBytes());
         }
         return is;
         
     }
     
     /**
      * 
      * @param bundle
      * @return
      * @throws MalformedURLException 
      */
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
}
