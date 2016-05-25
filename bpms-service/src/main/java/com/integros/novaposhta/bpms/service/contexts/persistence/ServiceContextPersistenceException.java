package com.integros.novaposhta.bpms.service.contexts.persistence;

public abstract class ServiceContextPersistenceException extends Exception {
    private static final String DEFAULT_MESSAGE = "Unknown persistence-related error";
    
    public ServiceContextPersistenceException() {
        super(DEFAULT_MESSAGE);
    }
    public ServiceContextPersistenceException(String message) {
        super(message);
    }
    
    public ServiceContextPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceContextPersistenceException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}