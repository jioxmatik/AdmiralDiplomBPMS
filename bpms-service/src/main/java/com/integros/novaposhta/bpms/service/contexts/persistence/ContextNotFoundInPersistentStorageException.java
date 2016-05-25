package com.integros.novaposhta.bpms.service.contexts.persistence;

public class ContextNotFoundInPersistentStorageException extends ServiceContextRestorationFailedException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Context %s not found in persistent storage";
    
    public ContextNotFoundInPersistentStorageException(String context_id)
    {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, context_id));
    }
    
    public ContextNotFoundInPersistentStorageException(String context_id, Throwable cause)
    {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, context_id), cause);
    }
}