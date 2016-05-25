package com.integros.novaposhta.bpms.service;

import com.integros.novaposhta.bpms.service.configuration.ConfigurationLoadingFailedException;
import com.integros.novaposhta.bpms.service.configuration.EnvironmentBasedConfiguration;
import com.integros.novaposhta.bpms.service.configuration.IBPMSServiceConfiguration;
import com.integros.novaposhta.bpms.service.configuration.YAMLFileBasedConfiguration;
import com.integros.novaposhta.bpms.service.contexts.IServiceContextManager;
import com.integros.novaposhta.bpms.service.contexts.ThreadpoolBasedRecoverableContextManager;
import com.integros.novaposhta.bpms.service.controllers.*;
import com.integros.novaposhta.bpms.service.logging.CustomLogger;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationPath("/rest")
public class JaxRsActivator extends Application {
    private static final Logger LOG = CustomLogger.getLogger(JaxRsActivator.class.getName());
    
    private final IServiceContextManager contextManager;
    private final Set<Object> controllers = new HashSet<Object>();

    public JaxRsActivator() {
        // Initialize context manager
        IBPMSServiceConfiguration config;
        try {
            config = YAMLFileBasedConfiguration.fromDefaultConfigFile();
        } catch (ConfigurationLoadingFailedException e) {
            LOG.warning(
                String.format(
                    "Failed to load configuration from file: %s. Falling back to environment-based configuration.",
                    e.getMessage()
                )
            );
            config = EnvironmentBasedConfiguration.fromSystemEnvironment();
        }
        LOG.info("==================== STARTING BPMS-SERVICE ====================");
        this.contextManager = new ThreadpoolBasedRecoverableContextManager(config);

        // Create controllers for context manager
        controllers.add(new RPCController(this.contextManager));
        controllers.add(new VerboseRESTfulController(this.contextManager, config.getLoggedAliases()));
    }

    @Override
    public Set<Object> getSingletons() {
        return this.controllers;
    }
}
