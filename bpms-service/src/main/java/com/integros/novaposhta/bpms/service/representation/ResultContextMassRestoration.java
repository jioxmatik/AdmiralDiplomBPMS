package com.integros.novaposhta.bpms.service.representation;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import com.integros.novaposhta.bpms.service.contexts.recovery.IRecoveryResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ResultContextMassRestoration extends ResultContextMap {
    private final Map<String, ExceptionRepresentation> errors;
    
    public Map<String, ExceptionRepresentation> getErrors() {
        return errors;
    }
    
    public ResultContextMassRestoration(
        Map<String, IServiceContext> contexts,
        Map<String, Throwable> errors
    ) {
        super(contexts);
        this.errors = new HashMap<String, ExceptionRepresentation>();
        for(Entry<String, Throwable> entry : errors.entrySet()) {
            this.errors.put(entry.getKey(), new ExceptionRepresentation(entry.getValue()));
        }
    }
    
    public ResultContextMassRestoration(IRecoveryResult recoveryResult) {
        this(recoveryResult.getRecoveredContexts(), recoveryResult.getRecoveryErrors());
    }
}
