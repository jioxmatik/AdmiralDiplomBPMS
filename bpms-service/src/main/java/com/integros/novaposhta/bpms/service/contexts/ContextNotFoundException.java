package com.integros.novaposhta.bpms.service.contexts;

public class ContextNotFoundException extends ServiceContextManagementException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "No context with ID %s";
    
    public ContextNotFoundException(String context_id)
    {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, context_id));
    }
    
    public ContextNotFoundException(String context_id, Throwable cause)
    {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, cause));
    }
}