package com.integros.novaposhta.bpms.service.contexts.persistence;

public class ServiceContextRestorationFailedException extends ServiceContextPersistenceException {
    private static final String DEFAULT_MESSAGE = "Failed to restore service context";
    
    public ServiceContextRestorationFailedException(Throwable cause)
    {
        super(DEFAULT_MESSAGE, cause);
    }
    
    public ServiceContextRestorationFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public ServiceContextRestorationFailedException(String message)
    {
        super(message);
    }
}