package com.integros.novaposhta.bpms.service.contexts.recovery;

import java.util.Map;
import java.util.Set;

interface IServiceContextManagerState {
    /**
     * Get this state represented as a map, where each entry has context ID as its key
     * and a list of aliases, set for this context ID as its value.
     * @return A collection that maps context IDs to lists of aliases for those context IDs.
     */
    Map<String, Set<String>> getContextAliasMap();
    
    Set<String> getContextIds();
    Set<String> getAliases(String contextId);
}
