package com.integros.novaposhta.bpms.service.utils;

public interface IExecutionTimer {
    boolean resume();
    boolean pause();
    long getTime(ExecutionTimerPrecision precision);
    String getTimeAsString();
    String getTimeAsString(ExecutionTimerPrecision precision);
    boolean isPaused();
}
