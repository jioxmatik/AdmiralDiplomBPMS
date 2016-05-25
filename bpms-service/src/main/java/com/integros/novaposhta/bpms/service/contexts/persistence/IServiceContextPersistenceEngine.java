package com.integros.novaposhta.bpms.service.contexts.persistence;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import java.util.Set;

public interface IServiceContextPersistenceEngine {
    void persist(String key, IServiceContext value) throws ServiceContextPersistenceFailedException, PersistenceNotAvailableException;
    IServiceContext restore(String key) throws ServiceContextRestorationFailedException, PersistenceNotAvailableException;
    IServiceContext restoreToContext(String key, IServiceContext target) throws ServiceContextRestorationFailedException, PersistenceNotAvailableException;
    boolean containsKey(String key) throws PersistenceNotAvailableException;
    Set<String> keySet() throws PersistenceNotAvailableException;
}
