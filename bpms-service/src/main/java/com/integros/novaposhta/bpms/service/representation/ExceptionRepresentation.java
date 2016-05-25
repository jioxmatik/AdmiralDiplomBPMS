package com.integros.novaposhta.bpms.service.representation;

public class ExceptionRepresentation {
    private final String name;
    private final String message;

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }
    private final String cause;

    public ExceptionRepresentation(Throwable error) {
        this.name = error.getClass().getName();
        this.message = error.getMessage();
        this.cause = String.valueOf(error.getCause());
    }
}