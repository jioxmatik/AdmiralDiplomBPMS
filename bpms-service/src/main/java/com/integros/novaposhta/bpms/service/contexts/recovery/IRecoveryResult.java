package com.integros.novaposhta.bpms.service.contexts.recovery;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import java.util.Map;
import java.util.Set;

public interface IRecoveryResult {
    Map<String, IServiceContext> getRecoveredContexts();
    Map<String, Throwable> getRecoveryErrors();
    Map<String, Set<String>> getRecoveredAliases();
}
