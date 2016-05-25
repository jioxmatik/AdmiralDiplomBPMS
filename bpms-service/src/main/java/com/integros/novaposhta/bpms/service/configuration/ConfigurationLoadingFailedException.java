package com.integros.novaposhta.bpms.service.configuration;

public class ConfigurationLoadingFailedException extends ConfigurationException {
    private static final String DEFAULT_MESSAGE = "Failed to load configuration";
    
    public ConfigurationLoadingFailedException() {
        super(DEFAULT_MESSAGE);
    }
    public ConfigurationLoadingFailedException(String message) {
        super(message);
    }
    
    public ConfigurationLoadingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ConfigurationLoadingFailedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
