package com.integros.novaposhta.bpms.service.contexts.recovery;

import com.integros.novaposhta.bpms.service.configuration.IBPMSServiceConfiguration;
import com.integros.novaposhta.bpms.service.contexts.ContextNotFoundException;
import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import com.integros.novaposhta.bpms.service.contexts.ServiceContextManager;
import com.integros.novaposhta.bpms.service.logging.CustomLogger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecoverableServiceContextManager extends ServiceContextManager implements IRecoverableServiceContextManager {
    private static final Logger LOG = CustomLogger.getLogger(RecoverableServiceContextManager.class.getName());
    
    private final IStateRecorder stateRecorder;
    
    public RecoverableServiceContextManager(IBPMSServiceConfiguration configuration) {
        super(configuration);
        IStateRecorder recorder;
        try {
            recorder = new SimpleYAMLStateRecorder();
        } catch (InvalidStateFilesDirectoryException e) {
            LOG.severe("This session will not be recoverable after restart! Reason:");
            LOG.log(
                Level.SEVERE, 
                String.format(
                    "ERROR: %s. State recording and recovery will be unavailable!", 
                    e.getMessage()
                ), 
                e
            );
            recorder = new ThrowingDummyStateRecorder(e);
        }
        this.stateRecorder = recorder;
    }

    @Override
    public IRecoveryResult recoverFromCrash() throws StateRecoveryFailedException {
        return this.applyState(this.stateRecorder.getPreviousSessionState());
    }
    
    private IRecoveryResult applyState(IServiceContextManagerState state) {
        Map<String, IServiceContext> recoveredContexts = new HashMap<String, IServiceContext>();
        Map<String, Throwable> errors = new HashMap<String, Throwable>();    
        for(String contextId : state.getContextIds()) {
            try {
                this.restoreContext(contextId);
                recoveredContexts.put(contextId, this.getContext(contextId));
            } catch(Exception e) {
                errors.put(contextId, e);
            }
        }
        
        Map<String, Set<String>> aliases = new HashMap<String, Set<String>>();
        for(String contextId : recoveredContexts.keySet()) {
            aliases.put(contextId, new HashSet<String>());
            for(String alias : state.getAliases(contextId)) {
                this.contexts.setAlias(contextId, alias);
                aliases.get(contextId).add(alias);
            }
        }

        return new ImmutableRecoveryResult(recoveredContexts, errors, aliases);
    }

    private void recordCurrentState() throws StateRecordingFailedException {
        this.stateRecorder.recordState(ImmutableManagerState.fromServiceContextManager(this));
    }
    
    private void tryToRecordCurrentState() {
        try {
            this.recordCurrentState();
        } catch(StateRecordingFailedException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    @Override
    public String createContext(String groupId, String artifactId, String versionId, String sessionName) {
        String contextId = super.createContext(groupId, artifactId, versionId, sessionName);
        tryToRecordCurrentState();
        return contextId;
    }

    @Override
    public void deleteContext(String contextId) throws ContextNotFoundException {
        super.deleteContext(contextId);
        tryToRecordCurrentState();
    }
    
    @Override
    public void addAlias(String contextId, String alias) throws ContextNotFoundException {
        super.addAlias(contextId, alias);
        tryToRecordCurrentState();
    }

    @Override
    public void removeAlias(String alias) {
        super.removeAlias(alias);
        tryToRecordCurrentState();
    }
}
