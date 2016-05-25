package com.integros.novaposhta.bpms.service.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.integros.novaposhta.bpms.service.logging.CustomLogger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public final class Serialization {
    private static final Logger LOG = CustomLogger.getLogger(Serialization.class.getName());
    
    public static String asJsonString(Object o)
    {
            try {
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    return ow.writeValueAsString(o);
            } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Failed to serialize object to JSON", e);
                    return null;
            }
    }

    public static String asXmlString(Object o)
    {
            try {
                    XmlMapper xmlMapper = new XmlMapper();
                    return xmlMapper.writeValueAsString(o);
            } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Failed to serialize object to XML", e);
                    return null;
            }
    }
}
