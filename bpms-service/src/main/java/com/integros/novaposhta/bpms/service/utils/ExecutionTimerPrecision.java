package com.integros.novaposhta.bpms.service.utils;

public enum ExecutionTimerPrecision {
    SECONDS (0, "s"),
    MILLISECONDS (3, "ms"),
    MICROSECONDS (6, "us"),
    NANOSECONDS (9, "ns");
    
    private final int powerOf10;
    private final String abbreviation;
    
    private final long multiplier;
    
    private ExecutionTimerPrecision(int powerOf10, String abbreviation) {
        this.powerOf10 = powerOf10;
        this.abbreviation = abbreviation;
        
        this.multiplier = (long) Math.pow(10, powerOf10);
    }
    
    public int getPowerOf10() {
        return this.powerOf10;
    }
    
    public String getAbbreviation() {
        return this.abbreviation;
    }
    
    public long getMultiplier() {
        return this.multiplier;
    }
}
