package com.integros.novaposhta.bpms.service.utils;

public class ExecutionTimer implements IExecutionTimer {
    private long startTime;
    private long accumulatedTime;
    private boolean isPaused;
    
    public ExecutionTimer() {
        this(false);
    }
    
    public ExecutionTimer(boolean startPaused) {
        this.startTime = System.nanoTime();
        this.accumulatedTime = 0;
        this.isPaused = startPaused;
    }
    
    @Override
    public boolean resume() {
        if(this.isPaused) {
            this.startTime = System.nanoTime();
            this.isPaused = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean pause() {
        if(!this.isPaused) {
            this.accumulatedTime += System.nanoTime() - this.startTime;
            this.isPaused = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public long getTime(ExecutionTimerPrecision precision) {
        long ns = this.isPaused ? accumulatedTime : accumulatedTime + (System.nanoTime() - this.startTime);
        assert ns >= 0 : String.format("Elapsed time cannot be negative (got %d)", ns);
        return precision == ExecutionTimerPrecision.NANOSECONDS
                ? ns
                : (long) Math.round(
                    (double)ns * precision.getMultiplier()/ExecutionTimerPrecision.NANOSECONDS.getMultiplier()
                );
    }

    @Override
    public String getTimeAsString() {
        return getTimeAsString(ExecutionTimerPrecision.NANOSECONDS);
    }

    @Override
    public String getTimeAsString(ExecutionTimerPrecision precision) {
        return String.valueOf(this.getTime(precision)) + precision.getAbbreviation();
    }

    @Override
    public boolean isPaused() {
        return this.isPaused;
    }
    
}
