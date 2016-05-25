package com.integros.novaposhta.bpms.service.representation;

import com.integros.novaposhta.bpms.service.utils.Serialization;

abstract public class AbstractSerializableObject {
    public String asJsonString()
    {
        return Serialization.asJsonString(this);
    }

    public String asXmlString()
    {
        return Serialization.asXmlString(this);
    }
}
