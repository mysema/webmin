package com.mysema.webmin;

import com.mysema.commons.jetty.JettyHelper;

/**
 * WebminStart provides
 *
 * @author tiwe
 * @version $Id$
 */
public class WebminStart {
    
    public static void main(String[] args) throws Exception{
        System.setProperty("com.mysema.webmin.mode","DEBUG");
        JettyHelper.startJetty("src/test/webapp", "/webmin-test", 8080, 8443);
    }

}
