package com.integros.novaposhta.bpms.service.contexts.persistence;

import com.integros.novaposhta.bpms.service.contexts.persistence.ServiceContextPersistenceException;

public class InvalidPersistencePathException extends ServiceContextPersistenceException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Unable to use %s as persistent storage location";
    
    InvalidPersistencePathException(String persistencePath) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, persistencePath));
    }
    
    InvalidPersistencePathException(String persistencePath, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, persistencePath), cause);
    }
    
    InvalidPersistencePathException(String persistencePath, String message, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, persistencePath) + ": " + message, cause);
    }
    
    InvalidPersistencePathException(String persistencePath, String message) {
        this(persistencePath, message, null);
    }
}