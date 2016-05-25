package com.integros.novaposhta.bpms.service.contexts.persistence;

public class ServiceContextPersistenceFailedException extends ServiceContextPersistenceException {
    private static final String DEFAULT_MESSAGE = "Failed to persist service context";
    
    public ServiceContextPersistenceFailedException(Throwable cause)
    {
        super(DEFAULT_MESSAGE, cause);
    }
    
    public ServiceContextPersistenceFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}