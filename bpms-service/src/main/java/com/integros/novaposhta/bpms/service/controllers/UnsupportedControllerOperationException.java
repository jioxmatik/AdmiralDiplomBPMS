package com.integros.novaposhta.bpms.service.controllers;

class UnsupportedControllerOperationException extends ControllerException {
    private static final String DEFAULT_MESSAGE = "Requested operation is not supported";
    
    public UnsupportedControllerOperationException() {
        super(DEFAULT_MESSAGE);
    }
    public UnsupportedControllerOperationException(String message) {
        super(message);
    }
    
    public UnsupportedControllerOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UnsupportedControllerOperationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}