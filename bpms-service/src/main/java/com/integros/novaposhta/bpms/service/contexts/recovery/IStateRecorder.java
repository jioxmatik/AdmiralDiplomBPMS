package com.integros.novaposhta.bpms.service.contexts.recovery;

interface IStateRecorder {
    /**
     * Get last recorded state from the previous session of bpms-service.
     * @return Last recorder state from the previous session.
     */
    IServiceContextManagerState getPreviousSessionState() throws StateRecoveryFailedException;
    IServiceContextManagerState getLastRecordedState() throws StateRecoveryFailedException;

    void recordState(IServiceContextManagerState state) throws StateRecordingFailedException;
}
