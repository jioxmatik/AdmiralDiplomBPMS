package com.integros.novaposhta.bpms.service.configuration;

public class ConfigurationParameterMalformedException extends ConfigurationParameterValueException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Invalid value specified for configuration parameter %s: %s";
    private static final String DEFAULT_MESSAGE_TEMPLATE_WITH_VALUE = "Invalid value \"%s\" was specified for configuration parameter %s: %s";
    
    public ConfigurationParameterMalformedException(String parameter, String explanation, Object value) {
        super(parameter, String.format(DEFAULT_MESSAGE_TEMPLATE_WITH_VALUE, value.toString(), parameter, explanation));
    }
    
    public ConfigurationParameterMalformedException(String parameter, String explanation) {
        super(parameter, String.format(DEFAULT_MESSAGE_TEMPLATE, parameter, explanation));
    }
    
    public ConfigurationParameterMalformedException(String parameter, Throwable cause) {
        super(parameter, String.format(DEFAULT_MESSAGE_TEMPLATE, parameter, cause.getMessage()), cause);
    }
    
    public ConfigurationParameterMalformedException(String parameter, Throwable cause, Object value) {
        super(parameter, String.format(DEFAULT_MESSAGE_TEMPLATE_WITH_VALUE, value.toString(), parameter, cause.getMessage()), cause);
    }
}
