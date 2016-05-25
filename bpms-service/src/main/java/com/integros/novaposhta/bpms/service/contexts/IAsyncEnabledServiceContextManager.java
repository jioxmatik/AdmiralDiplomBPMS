package com.integros.novaposhta.bpms.service.contexts;

public interface IAsyncEnabledServiceContextManager extends IServiceContextManager {
    void executeXmlCommandsAsync(String contextId, String commands) throws ContextNotFoundException;
    
    void executeJsonCommandsAsync(String contextId, String commands) throws ContextNotFoundException;
}
