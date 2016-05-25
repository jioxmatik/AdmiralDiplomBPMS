package com.integros.novaposhta.bpms.service.contexts.recovery;

final class ThrowingDummyStateRecorder implements IStateRecorder {
    private Throwable e;
    
    public ThrowingDummyStateRecorder(Throwable e) {
        this.e = e;
    }

    @Override
    public IServiceContextManagerState getPreviousSessionState() throws StateRecoveryFailedException {
        throw new StateRecoveryFailedException(
            String.format(
                "State recovery is unavailable during this session: %s",
                this.e.getMessage()
            )
        );
    }

    @Override
    public IServiceContextManagerState getLastRecordedState() throws StateRecoveryFailedException {
        throw new StateRecoveryFailedException(
            String.format(
                "State recovery is unavailable during this session: %s",
                this.e.getMessage()
            )
        );
    }

    @Override
    public void recordState(IServiceContextManagerState state) throws StateRecordingFailedException {
        throw new StateRecordingFailedException(
            String.format(
                "State recording is unavailable during this session: %s",
                this.e.getMessage()
            )
        );
    }
}
