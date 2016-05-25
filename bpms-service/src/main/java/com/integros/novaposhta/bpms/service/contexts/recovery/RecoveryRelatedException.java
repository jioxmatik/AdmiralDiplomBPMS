package com.integros.novaposhta.bpms.service.contexts.recovery;

public abstract class RecoveryRelatedException extends Exception {
    private static final String DEFAULT_MESSAGE = "Unknown error, related to context manager state recovery";
    
    public RecoveryRelatedException() {
        super(DEFAULT_MESSAGE);
    }
    public RecoveryRelatedException(String message) {
        super(message);
    }
    
    public RecoveryRelatedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RecoveryRelatedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
