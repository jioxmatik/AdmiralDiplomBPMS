package com.integros.novaposhta.bpms.service.configuration;

/**
 * This non-catchable error is thrown if a parameter, which MUST have a default value,
 * is not defined in DefaultConfiguration.
 */
class DefaultValueUndefinedAssertionError extends AssertionError {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Default value for \"%s\" config parameter MUST be specified in DefaultConfiguration definition";
    
    DefaultValueUndefinedAssertionError(String parameter) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, parameter));
    }
    
    DefaultValueUndefinedAssertionError(String parameter, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, parameter), cause);
    }
}
