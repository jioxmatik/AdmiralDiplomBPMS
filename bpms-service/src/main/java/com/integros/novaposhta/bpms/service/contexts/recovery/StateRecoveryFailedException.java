package com.integros.novaposhta.bpms.service.contexts.recovery;

public class StateRecoveryFailedException extends RecoveryRelatedException {
    private static final String DEFAULT_MESSAGE = "Failed to recover previous state";
    
    public StateRecoveryFailedException() {
        super(DEFAULT_MESSAGE);
    }
    public StateRecoveryFailedException(String message) {
        super(message);
    }
    
    public StateRecoveryFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public StateRecoveryFailedException(Throwable cause) {
        super(DEFAULT_MESSAGE + ": " + cause.getMessage(), cause);
    }
}
