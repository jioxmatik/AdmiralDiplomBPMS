package com.integros.novaposhta.bpms.service.contexts;

import com.integros.novaposhta.bpms.service.configuration.IBPMSServiceConfiguration;
import com.integros.novaposhta.bpms.service.contexts.persistence.ContextNotFoundInPersistentStorageException;
import com.integros.novaposhta.bpms.service.contexts.persistence.IServiceContextPersistenceEngine;
import com.integros.novaposhta.bpms.service.contexts.persistence.PersistenceNotAvailableException;
import com.integros.novaposhta.bpms.service.contexts.persistence.RotatingFileBasedPersistenceEngine;
import com.integros.novaposhta.bpms.service.contexts.persistence.ServiceContextPersistenceException;
import com.integros.novaposhta.bpms.service.contexts.persistence.ThrowingDummyPersistenceEngine;
import com.integros.novaposhta.bpms.service.logging.CustomLogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kie.api.builder.ReleaseId;

public class ServiceContextManager implements IServiceContextManager {
    private static final Logger LOG = CustomLogger.getLogger(ServiceContextManager.class.getName());
    
    protected final class AliasAwareContextMap extends HashMap<String, IServiceContext> implements IAliasMap {
        private final Map<String, String> aliases = new HashMap<String, String>();
        
        public AliasAwareContextMap() {
            
        }

        @Override
        public IServiceContext get(Object key) {
            return super.get(this.translate((String)key));
        }

        @Override
        public boolean containsKey(Object key) {
            return super.containsKey(this.translate((String)key));
        }        
        
        @Override
        public void setAlias(String original, String alias) {
            this.aliases.put(alias, original);
            
            LOG.log(
                Level.INFO, 
                "Added alias \"{0}\" for context \"{1}\"",
                new Object[] {alias, original}
            );
        }

        @Override
        public void unsetAlias(String alias) {
            String original = this.aliases.remove(alias);
            
            LOG.log(
                Level.INFO, 
                "Removed alias \"{0}\" (previously pointed to context {1})",
                new Object[] {alias, original}
            );
        }

        @Override
        public Iterable<String> getAliases(String original) {
            List<String> result = new LinkedList<String>();
            for(String alias : this.aliases.keySet()) {
                if(this.aliases.get(alias).equals(original)) {
                    result.add(alias);
                }
            }
            return result;
        }

        @Override
        public String getOriginal(String alias) throws UnknownAliasException {
            String result = this.aliases.get(alias);
            if(result == null) {
                throw new UnknownAliasException(alias);
            }
            return result;
        }

        @Override
        public String translate(String string) {
            try {
                return this.getOriginal(string);
            } catch (UnknownAliasException e) {
                return string;
            }
        }
    }

    protected AliasAwareContextMap contexts = new AliasAwareContextMap();
    
    protected IServiceContextPersistenceEngine persistenceEngine;

    protected String generateContextId()
    {
            return UUID.randomUUID().toString();
    }
    
    public ServiceContextManager(IBPMSServiceConfiguration configuration) {
        try {
            this.persistenceEngine = new RotatingFileBasedPersistenceEngine(configuration.getPersistencePath());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, String.format("ERROR: %s. Persistence services will be unavailable!", e.getMessage()), e);
            this.persistenceEngine = new ThrowingDummyPersistenceEngine(e);
        }
    }

    @Override
    public String createContext(String groupId, String artifactId, String versionId, String sessionName) {
        String context_id = this.generateContextId();
        contexts.put(context_id, new ServiceContext(groupId, artifactId, versionId, sessionName));

        LOG.log(Level.INFO, "Created context {0}: {1}", new Object[]{context_id, contexts.get(context_id).toString()});
        
        return context_id;
    }

    @Override
    public void deleteContext(String contextId) throws ContextNotFoundException {
        if(contexts.containsKey(contextId))
        {
            contexts.remove(contextId).dispose();

            LOG.log(Level.INFO, "Deleted context {0}", this.getContextIdForLogging(contextId));
        }
        else
        {
            throw new ContextNotFoundException(contextId);
        }
    }

    @Override
    public void resetContext(String contextId) throws ContextNotFoundException {
        if(contexts.containsKey(contextId))
        {
            contexts.get(contextId).reset();

            LOG.log(Level.INFO, "Reset context {0}", this.getContextIdForLogging(contextId));
        }
        else
        {
            throw new ContextNotFoundException(contextId);
        }
    }
    
    @Override
    public void resetContext(String contextId, String sessionName) throws ContextNotFoundException {
        if(contexts.containsKey(contextId))
        {
            contexts.get(contextId).reset(sessionName);

            LOG.log(Level.INFO, "Reset context {0} with new session {1}", new Object[]{this.getContextIdForLogging(contextId), sessionName});
        }
        else
        {
            throw new ContextNotFoundException(contextId);
        }
    }

    @Override
    public String executeXmlCommands(String contextId, String commands) throws ContextNotFoundException {
        if(!this.contexts.containsKey(contextId)){
            throw new ContextNotFoundException(contextId);
        }
        
        LOG.log(
            Level.FINE, 
            "Executing commands in context {0}; command format: XML, command string length: {1}",
            new Object[]{this.getContextIdForLogging(contextId), String.valueOf(commands.length())}
        );
        
        return contexts.get(contextId).executeXml(commands);
    }

    @Override
    public String executeJsonCommands(String contextId, String commands) throws ContextNotFoundException {
        if(!this.contexts.containsKey(contextId)){
            throw new ContextNotFoundException(contextId);
        }
        
        LOG.log(
            Level.FINE, 
            "Executing commands in context {0}; command format: JSON, command string length: {1}",
            new Object[]{this.getContextIdForLogging(contextId), String.valueOf(commands.length())}
        );
        
        return contexts.get(contextId).executeJson(commands);
    }

    @Override
    public void persistContext(String contextId) throws ContextNotFoundException, ServiceContextPersistenceException {
        if(!this.contexts.containsKey(contextId)){
            throw new ContextNotFoundException(contextId);
        }
        
        this.persistenceEngine.persist(contextId, this.contexts.get(contextId));
        
        LOG.log(Level.INFO, "Saved context {0} to persistent storage", this.getContextIdForLogging(contextId));
    }

    @Override
    public void restoreContext(String contextId) throws ContextNotFoundException, ServiceContextPersistenceException {
        this.contexts.put(contextId, this.persistenceEngine.restore(contextId));
    }

    @Override
    public void restoreContextToContext(String sourceId, String targetId) throws ContextNotFoundException, ServiceContextPersistenceException {
        if(!this.contexts.containsKey(targetId)) {
            throw new ContextNotFoundException(targetId);
        }
        if(!this.persistenceEngine.containsKey(sourceId)) {
            throw new ContextNotFoundInPersistentStorageException(sourceId);
        }
        this.persistenceEngine.restoreToContext(sourceId, this.contexts.get(targetId));
        
        LOG.log(
            Level.INFO, 
            "Restored data from context {0} in persistent storage to context {1}", 
            new Object[]{this.getContextIdForLogging(sourceId), this.getContextIdForLogging(targetId)}
        );
    }

    @Override
    public void updateContext(String contextId, String versionId)
                    throws ContextNotFoundException {
            if(!this.contexts.containsKey(contextId)) {
                    throw new ContextNotFoundException(contextId);
        }
            IServiceContext context = this.contexts.get(contextId);
            context.updateContext(versionId);
            
            LOG.log(
                Level.INFO, 
                "Updated context {0} to version {1} (new release ID: {2})", 
                new Object[] {this.getContextIdForLogging(contextId), versionId, context.getReleaseId().toString()}
            );
    }

    @Override
    public void updateContext(String contextId, String groupId,
                    String artifactId, String versionId)
                    throws ContextNotFoundException {
            if(!this.contexts.containsKey(contextId)) {
                    throw new ContextNotFoundException(contextId);
        }
            IServiceContext context = this.contexts.get(contextId);
            context.updateContext(groupId, artifactId, versionId);
            
            LOG.log(
                Level.INFO, 
                "Updated context {0} (new release ID: {1})", 
                new Object[] {this.getContextIdForLogging(contextId), versionId, context.getReleaseId().toString()}
            );
    }

    @Override
    public IServiceContext getContext(String contextId) throws ContextNotFoundException {
        return this.contexts.get(contextId);
    }

    @Override
    public Map<String,IServiceContext> getContexts() {
        return Collections.unmodifiableMap(this.contexts);
    }

    @Override
    public Map<String,IServiceContext> getContexts(ReleaseId releaseId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String,IServiceContext> getContexts(ReleaseId releaseId, String sessionName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> getPersistedContextIds() throws PersistenceNotAvailableException {
        return this.persistenceEngine.keySet();
    }
    
    @Override
    public void addAlias(String contextId, String alias) throws ContextNotFoundException {
        if(!this.contexts.containsKey(contextId)) {
            throw new ContextNotFoundException(contextId);
        }
        this.contexts.setAlias(contextId, alias);
    }

    @Override
    public void removeAlias(String alias) {
        this.contexts.unsetAlias(alias);
    }

    @Override
    public Iterable<String> getAliases(String contextId) throws ContextNotFoundException {
        if(!this.contexts.containsKey(contextId)) {
            throw new ContextNotFoundException(contextId);
        }
        return this.contexts.getAliases(contextId);
    }
    
    protected String getContextIdForLogging(String contextId) {
        try {
            return String.format("%s (as %s)", this.contexts.getOriginal(contextId), contextId);
        } catch(UnknownAliasException e) {
            return contextId;
        }
    }
    
    @Override
    public String translateAlias(String alias) {
        return this.contexts.translate(alias);
    }
}
