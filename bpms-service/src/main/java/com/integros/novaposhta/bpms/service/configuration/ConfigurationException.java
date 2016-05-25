package com.integros.novaposhta.bpms.service.configuration;

public abstract class ConfigurationException extends Exception {
    private static final String DEFAULT_MESSAGE = "Unknown configuration-related error";
    
    public ConfigurationException() {
        super(DEFAULT_MESSAGE);
    }
    public ConfigurationException(String message) {
        super(message);
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ConfigurationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
