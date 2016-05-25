package com.integros.novaposhta.bpms.service.controllers;

abstract class ControllerException extends Exception {
    private static final String DEFAULT_MESSAGE = "Unknown controller-related error";
    
    public ControllerException() {
        super(DEFAULT_MESSAGE);
    }
    public ControllerException(String message) {
        super(message);
    }
    
    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ControllerException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}