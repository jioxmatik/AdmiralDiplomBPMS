package com.integros.novaposhta.bpms.service.configuration;

public class ConfigurationParameterMissingException extends ConfigurationParameterValueException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Configuration parameter %s is missing";
    
    public ConfigurationParameterMissingException(String parameter) {
        super(parameter, String.format(DEFAULT_MESSAGE_TEMPLATE, parameter));
    }
    
    public ConfigurationParameterMissingException(String parameter, Throwable cause) {
        super(parameter, String.format(DEFAULT_MESSAGE_TEMPLATE, parameter), cause);
    }
}
