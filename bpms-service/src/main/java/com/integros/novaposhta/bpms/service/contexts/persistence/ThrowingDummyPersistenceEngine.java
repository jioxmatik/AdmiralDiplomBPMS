package com.integros.novaposhta.bpms.service.contexts.persistence;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import java.util.Set;

public final class ThrowingDummyPersistenceEngine implements IServiceContextPersistenceEngine {
    private final Throwable e;
    
    public ThrowingDummyPersistenceEngine(Throwable e) {
        this.e = e;
    }
    
    public ThrowingDummyPersistenceEngine(String message) {
        this(new Exception(message));
    }

    @Override
    public void persist(String key, IServiceContext value) throws PersistenceNotAvailableException {
        throw new PersistenceNotAvailableException(this.e);
    }

    @Override
    public IServiceContext restore(String key) throws PersistenceNotAvailableException {
        throw new PersistenceNotAvailableException(this.e);
    }

    @Override
    public IServiceContext restoreToContext(String key, IServiceContext target) throws PersistenceNotAvailableException {
        throw new PersistenceNotAvailableException(this.e);
    }

    @Override
    public boolean containsKey(String key) throws PersistenceNotAvailableException {
        throw new PersistenceNotAvailableException(this.e);
    }

    @Override
    public Set<String> keySet() throws PersistenceNotAvailableException {
        throw new PersistenceNotAvailableException(this.e);
    }
}
