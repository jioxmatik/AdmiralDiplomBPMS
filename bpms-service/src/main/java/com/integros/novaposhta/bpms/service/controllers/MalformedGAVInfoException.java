package com.integros.novaposhta.bpms.service.controllers;

class MalformedGAVInfoException extends ControllerException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "\"%s\" is not a valid GAV-info string. Correct format: \"com.example.group.id:ArtifactId:VERSION-ID\"";
    
    public MalformedGAVInfoException(String gav) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, gav));
    }
    
    public MalformedGAVInfoException(String gav, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, gav), cause);
    }
}