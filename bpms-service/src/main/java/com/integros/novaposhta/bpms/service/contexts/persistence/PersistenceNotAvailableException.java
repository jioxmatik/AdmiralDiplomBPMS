package com.integros.novaposhta.bpms.service.contexts.persistence;

public class PersistenceNotAvailableException extends ServiceContextPersistenceException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Persistence services are not available: %s";
    
    public PersistenceNotAvailableException(Throwable cause) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, cause.toString()), cause);
    }
}