package com.integros.novaposhta.bpms.service.representation;

import com.integros.novaposhta.bpms.service.contexts.recovery.IRecoveryResult;
import java.util.Map;
import java.util.Set;

public class ResultCrashRecovery extends ResultContextMassRestoration {
    private final Map<String, Set<String>> aliases;
    
    public ResultCrashRecovery(IRecoveryResult recoveryResult) {
        super(recoveryResult);
        this.aliases = recoveryResult.getRecoveredAliases();
    }
    
    public Map<String, Set<String>> getAliases() {
        return aliases;
    }
}
