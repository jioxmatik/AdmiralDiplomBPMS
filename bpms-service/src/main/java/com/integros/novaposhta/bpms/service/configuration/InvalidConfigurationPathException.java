package com.integros.novaposhta.bpms.service.configuration;

public class InvalidConfigurationPathException extends ConfigurationLoadingFailedException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "%s must exist and be readable";
    
    public InvalidConfigurationPathException(String path) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, path));
    }

    public InvalidConfigurationPathException(String path, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, path), cause);
    }
}
