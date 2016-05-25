package com.integros.novaposhta.bpms.service.contexts.recovery;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import java.util.Map;
import java.util.Set;

public class ImmutableRecoveryResult implements IRecoveryResult {
    private final Map<String, IServiceContext> contexts;
    private final Map<String, Throwable> errors;
    private final Map<String, Set<String>> aliases;

    
    public ImmutableRecoveryResult(Map<String, IServiceContext> contexts, Map<String, Throwable> errors, Map<String, Set<String>> aliases) {
        this.contexts = contexts;
        this.errors = errors;
        this.aliases = aliases;
    }
    
    @Override
    public Map<String, IServiceContext> getRecoveredContexts() {
        return this.contexts;
    }

    @Override
    public Map<String, Throwable> getRecoveryErrors() {
        return this.errors;
    }

    @Override
    public Map<String, Set<String>> getRecoveredAliases() {
        return this.aliases;
    }
    
}
