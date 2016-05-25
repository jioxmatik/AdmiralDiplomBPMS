package com.integros.novaposhta.bpms.service.configuration;

import com.integros.novaposhta.bpms.service.logging.CustomLogger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvironmentBasedConfiguration implements IBPMSServiceConfiguration {
    private static final Logger LOG = CustomLogger.getLogger(EnvironmentBasedConfiguration.class.getName());

    protected static final String SYSVAR_PREFIX = "BPMS_SERVICE_";

    protected static final String PERSISTENCE_PATH_VARIABLE = "PERSISTENCE_PATH";
    private final String persistencePath;
    
    private static final List<String> DEFAULT_LOG_TAG_FILTER_SET = new LinkedList<String>(){{
        add(".*");
    }};
    
    protected EnvironmentBasedConfiguration(Map<String, String> environment)
    {
        String persistencePathSysvarName = getSysvarName(PERSISTENCE_PATH_VARIABLE);
        if(environment.containsKey(persistencePathSysvarName)) {
            this.persistencePath = environment.get(persistencePathSysvarName);
        } else {
            try {
                LOG.warning(
                        String.format(
                                "WARNING: Environment variable %s is not set; falling back to default value %s",
                                persistencePathSysvarName,
                                DefaultConfiguration.getDefaultValue("persistence_path")
                        )
                );
            } catch (ConfigurationParameterMissingException e) {
                LOG.warning(
                        String.format(
                                "WARNING: Environment variable %s is not set; no default value is available",
                                persistencePathSysvarName
                        )
                );
            }
            this.persistencePath = null;
        }
    }
    
    @Override
    public final String getPersistencePath() throws ConfigurationParameterMissingException {
        return this.persistencePath != null ? this.persistencePath : DefaultConfiguration.<String>getDefaultValue("persistence_path");
    }
    
    public static final EnvironmentBasedConfiguration fromSystemEnvironment()
    {
        return new EnvironmentBasedConfiguration(System.getenv());
    }
    
    protected static final String getSysvarName(String variable)
    {
        return SYSVAR_PREFIX + variable;
    }

    protected static final<T> T defaultValueOf(String parameter) {
        try {
            return DefaultConfiguration.<T>getDefaultValue(parameter);
        } catch (ConfigurationParameterMissingException e) {
            throw new DefaultValueUndefinedAssertionError(parameter, e);
        }
    }
    
    @Override
    public boolean needsLogging(String alias, String tag) {
        return false;
    }

    @Override
    public List<String> getLoggedAliases() {
        return EnvironmentBasedConfiguration.<List<String>>defaultValueOf("log_aliases");
    }

    @Override
    public List<String> getLogTagFilters() {
        return EnvironmentBasedConfiguration.<List<String>>defaultValueOf("log_tag_filters");
    }

    @Override
    public Level getLogLevel() {
        return Level.parse(EnvironmentBasedConfiguration.<String>defaultValueOf("log_level"));
    }
}
