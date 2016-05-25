package com.integros.novaposhta.bpms.service.contexts;

public abstract class ServiceContextManagementException extends Exception {
    private static final String DEFAULT_MESSAGE = "Unknown service context management error";
    
    public ServiceContextManagementException() {
        super(DEFAULT_MESSAGE);
    }
    public ServiceContextManagementException(String message) {
        super(message);
    }
    
    public ServiceContextManagementException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceContextManagementException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}