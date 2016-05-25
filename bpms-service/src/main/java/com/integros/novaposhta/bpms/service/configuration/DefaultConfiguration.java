package com.integros.novaposhta.bpms.service.configuration;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

final class DefaultConfiguration {
    private static final Map<String, Object> DEFAULT_VALUES = new HashMap<String, Object>(){{
        //put("persistence_path", "/opt/jbossbpms/persist");
        put("log_level", "INFO");
        //put("log_level_console", "INFO");
        put("log_aliases", new LinkedList<String>());
        put("log_tag_filters", new LinkedList<String>(){{
            add(".*");
        }});
    }};

    public static<T> T getDefaultValue(String key) throws ConfigurationParameterMissingException {
        if(!DEFAULT_VALUES.containsKey(key)) {
            throw new ConfigurationParameterMissingException(key);
        }
        try {
            return (T)DEFAULT_VALUES.get(key);
        } catch(ClassCastException e) {
            /*assert false : String.format(
                "Default value \"%s\" specified for config parameter %s is not convertible to requested type",
                DEFAULT_VALUES.get(key).toString(),
                key
            );*/
            throw new AssertionError(
                String.format(
                    "Default value \"%s\" specified for config parameter %s is not convertible to requested type",
                    DEFAULT_VALUES.get(key).toString(),
                    key
                )
            );
        }
    }
}
