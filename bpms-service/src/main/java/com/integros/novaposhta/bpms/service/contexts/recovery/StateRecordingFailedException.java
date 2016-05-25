package com.integros.novaposhta.bpms.service.contexts.recovery;

public class StateRecordingFailedException extends RecoveryRelatedException {
    private static final String DEFAULT_MESSAGE = "Failed to record current state to disk";
    
    public StateRecordingFailedException() {
        super(DEFAULT_MESSAGE);
    }
    public StateRecordingFailedException(String message) {
        super(message);
    }
    
    public StateRecordingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public StateRecordingFailedException(Throwable cause) {
        super(DEFAULT_MESSAGE + ": " + cause.getMessage(), cause);
    }
}
