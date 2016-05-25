package com.integros.novaposhta.bpms.service.contexts.recovery;

import com.integros.novaposhta.bpms.service.contexts.ContextNotFoundException;
import com.integros.novaposhta.bpms.service.contexts.IServiceContextManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class ImmutableManagerState implements IServiceContextManagerState {
    private final Map<String, Set<String>> contextAliasMap;
    
    public ImmutableManagerState(Map<String, Set<String>> contextAliasMap) {
        this.contextAliasMap = contextAliasMap;
    }
    
    @Override
    public Map<String, Set<String>> getContextAliasMap() {
        return this.contextAliasMap;
    }

    @Override
    public Set<String> getContextIds() {
        return this.contextAliasMap.keySet();
    }

    @Override
    public Set<String> getAliases(String contextId) {
        return this.contextAliasMap.get(contextId);
    }
    
    /**
     * Create an ImmutableManagerState object from current state of an IServiceContextManager.
     * @param contextManager Context manager from which to create a state object.
     * @return Resulting ImmutableManagerState object.
     */
    public static ImmutableManagerState fromServiceContextManager(IServiceContextManager contextManager) {
        Map<String, Set<String>> contextAliasMap = new HashMap<String, Set<String>>();
        for(String contextId : contextManager.getContexts().keySet()) {
            try {
                contextAliasMap.put(contextId, new HashSet((Collection) contextManager.getAliases(contextId)));
            } catch (ContextNotFoundException e) {
                assert false : String.format("impossible exception %s on iterating over context ID set", e.toString());
            }
        }
        return new ImmutableManagerState(contextAliasMap);
    }
}
