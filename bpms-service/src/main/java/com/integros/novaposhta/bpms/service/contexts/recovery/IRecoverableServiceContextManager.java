package com.integros.novaposhta.bpms.service.contexts.recovery;

import com.integros.novaposhta.bpms.service.contexts.IServiceContextManager;

public interface IRecoverableServiceContextManager extends IServiceContextManager {
    IRecoveryResult recoverFromCrash() throws StateRecoveryFailedException;
}
