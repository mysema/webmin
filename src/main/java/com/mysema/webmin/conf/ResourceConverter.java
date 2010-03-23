/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.conf;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * ResourceConverter provides
 *
 * @author tiwe
 * @version $Id$
 */
public final class ResourceConverter implements Converter {
    
    public void marshal(Object source, HierarchicalStreamWriter writer,MarshallingContext context) {
        // TODO Auto-generated method stub                
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {            
        String l10n = reader.getAttribute("l10n");
        String forward = reader.getAttribute("forward");
        String path = reader.getValue();
        return new Resource(path, 
                forward != null ? Boolean.valueOf(forward) : false,
                l10n != null ? Boolean.valueOf(l10n) : false);
    }

    @SuppressWarnings("unchecked")
    public boolean canConvert(Class type) {
        return type.equals(Resource.class);
    }
}