package com.integros.novaposhta.bpms.service.contexts.recovery;

public class InvalidStateFilesDirectoryException extends RecoveryRelatedException {
    public InvalidStateFilesDirectoryException(String message) {
        super(message);
    }
    
    public InvalidStateFilesDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
