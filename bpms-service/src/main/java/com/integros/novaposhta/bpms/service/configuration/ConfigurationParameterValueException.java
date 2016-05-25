package com.integros.novaposhta.bpms.service.configuration;

public abstract class ConfigurationParameterValueException extends ConfigurationException{
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Value of configuration parameter %s is missing or invalid";
    
    public ConfigurationParameterValueException(String parameter) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, parameter));
    }
    
    public ConfigurationParameterValueException(String parameter, String message) {
        super(message);
    }
    
    public ConfigurationParameterValueException(String parameter, String message, Throwable cause) {
        super(message, cause);
    }
}
