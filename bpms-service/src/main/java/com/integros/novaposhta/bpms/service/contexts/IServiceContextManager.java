package com.integros.novaposhta.bpms.service.contexts;

import com.integros.novaposhta.bpms.service.contexts.persistence.PersistenceNotAvailableException;
import com.integros.novaposhta.bpms.service.contexts.persistence.ServiceContextPersistenceException;
import java.util.Map;
import java.util.Set;
import org.kie.api.builder.ReleaseId;

public interface IServiceContextManager {
    /**
     * 
     * @param groupId
     * @param artifactId
     * @param versionId
     * @param sessionName
     * @return ID of the created context.
     */
    String createContext(String groupId, String artifactId, String versionId, String sessionName);
    
    void deleteContext(String contextId) throws ContextNotFoundException;
    
    void resetContext(String contextId) throws ContextNotFoundException;
    
    void resetContext(String contextId, String sessionName) throws ContextNotFoundException;
    
    //String executeCommands(String contextId, String commands) throws ContextNotFoundException;
    
    String executeXmlCommands(String contextId, String commands) throws ContextNotFoundException;
    
    String executeJsonCommands(String contextId, String commands) throws ContextNotFoundException;
    
    void persistContext(String contextId) throws ContextNotFoundException, ServiceContextPersistenceException;
    
    void restoreContext(String contextId) throws ContextNotFoundException, ServiceContextPersistenceException;
    
    void restoreContextToContext(String sourceId, String targetId) throws ContextNotFoundException, ServiceContextPersistenceException;
	
    void updateContext(String contextId, String versionId) throws ContextNotFoundException;

    void updateContext(String contextId, String groupId, String artifactId, String versionId) throws ContextNotFoundException;

    IServiceContext getContext(String contextId) throws ContextNotFoundException;
    
    Map<String,IServiceContext> getContexts();

    Map<String,IServiceContext> getContexts(ReleaseId releaseId);
    
    Map<String,IServiceContext> getContexts(ReleaseId releaseId, String sessionName);
    
    Set<String> getPersistedContextIds() throws PersistenceNotAvailableException;
    
    void addAlias(String contextId, String alias) throws ContextNotFoundException;
    
    void removeAlias(String alias);
    
    Iterable<String> getAliases(String contextId) throws ContextNotFoundException;
    
    String translateAlias(String alias);    // FIXME: Questionable design, exposing internal logic here...
}

