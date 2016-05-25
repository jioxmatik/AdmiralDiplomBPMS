package com.integros.novaposhta.bpms.service.configuration;

public class ConfigurationFormatException extends ConfigurationLoadingFailedException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Failed to parse configuration file %s as %s";

    public ConfigurationFormatException(String filename, String format) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, filename, format));
    }
    
    public ConfigurationFormatException(String filename, String format, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, filename, format), cause);
    }
    
    protected ConfigurationFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
