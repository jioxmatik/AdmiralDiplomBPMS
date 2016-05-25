package com.integros.novaposhta.bpms.service.contexts;

import com.integros.novaposhta.bpms.service.configuration.IBPMSServiceConfiguration;
import com.integros.novaposhta.bpms.service.contexts.recovery.RecoverableServiceContextManager;
import com.integros.novaposhta.bpms.service.logging.CustomLogger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadpoolBasedRecoverableContextManager extends RecoverableServiceContextManager implements IAsyncEnabledServiceContextManager {
    private static final Logger LOG = CustomLogger.getLogger(ThreadpoolBasedRecoverableContextManager.class.getName());

    private static final int THREADPOOL_SIZE = 4;

    protected enum CommandsFormat {
        JSON, XML;
    }
    
    protected static class CommandsExecutionThread implements Runnable {
        private static final Logger LOG = CustomLogger.getLogger(CommandsExecutionThread.class.getName());
        
        private final IServiceContext targetContext;
        private final String commands;
        private final CommandsFormat format;
        
        public CommandsExecutionThread(IServiceContext targetContext, String commands, CommandsFormat format) {
            this.targetContext = targetContext;
            this.commands = commands;
            this.format = format;
        }
        
        @Override
        public void run() {
            if(this.format.equals(CommandsFormat.JSON)) {
                this.targetContext.executeJson(this.commands);
            } else {
                this.targetContext.executeXml(this.commands);
            }
        }
    }
    
    protected final ExecutorService executor;
    
    public ThreadpoolBasedRecoverableContextManager(IBPMSServiceConfiguration configuration) {
        super(configuration);
        this.executor = Executors.newFixedThreadPool(THREADPOOL_SIZE);
    }

    @Override
    public void executeXmlCommandsAsync(String contextId, String commands) throws ContextNotFoundException {
        this.executor.execute(new CommandsExecutionThread(this.getContext(contextId), commands, CommandsFormat.XML));
        
        LOG.log(
            Level.FINE, 
            "Executing commands asynchronously in context {0}; command format: XML, command string length: {1}",
            new Object[]{this.getContextIdForLogging(contextId), String.valueOf(commands.length())}
        );
    }

    @Override
    public void executeJsonCommandsAsync(String contextId, String commands) throws ContextNotFoundException {
        this.executor.execute(new CommandsExecutionThread(this.getContext(contextId), commands, CommandsFormat.JSON));
        
        LOG.log(
            Level.FINE, 
            "Executing commands asynchronously in context {0}; command format: XML, command string length: {1}",
            new Object[]{this.getContextIdForLogging(contextId), String.valueOf(commands.length())}
        );
    }
    
}
