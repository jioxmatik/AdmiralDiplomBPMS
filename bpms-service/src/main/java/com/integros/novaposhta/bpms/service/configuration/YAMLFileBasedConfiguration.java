package com.integros.novaposhta.bpms.service.configuration;

import com.integros.novaposhta.bpms.service.logging.CustomLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

public class YAMLFileBasedConfiguration implements IBPMSServiceConfiguration {
    private static final Logger LOG = CustomLogger.getLogger(YAMLFileBasedConfiguration.class.getName());
    
    protected static final String CONFIGURATION_FORMAT = "YAML";
    /**
     * bpms_service.yml filepath relative to $JBOSS_HOME
     */
    protected static final String DEFAULT_CONFIG_FILEPATH = "bpms_service.yml";
    
    private final Map<String, Object> configuration;

    public YAMLFileBasedConfiguration(File configurationFile) throws InvalidConfigurationPathException, ConfigurationFormatException
    {
        try {
            if(!configurationFile.exists() || configurationFile.isDirectory() || !configurationFile.canRead()) {
                throw new InvalidConfigurationPathException(configurationFile.getAbsolutePath());
            }
            
            Yaml yaml = new Yaml();
            this.configuration = (Map<String, Object>) yaml.load(new FileInputStream(configurationFile));
        } catch (FileNotFoundException e) {
            throw new InvalidConfigurationPathException(configurationFile.getAbsolutePath());
        } catch (ClassCastException e) {
            throw new ConfigurationFormatException(configurationFile.getAbsolutePath(), CONFIGURATION_FORMAT);
        }
    }
    
    public YAMLFileBasedConfiguration(String configurationFilename) throws InvalidConfigurationPathException, ConfigurationFormatException
    {
        this(new File(configurationFilename));
    }
    
    protected final<T> T get(String key) throws ConfigurationParameterMissingException, ConfigurationParameterMalformedException {
        if(!this.configuration.containsKey(key)) {
            return DefaultConfiguration.<T>getDefaultValue(key);
        }
        try {
            return (T)this.configuration.get(key);
        } catch(ClassCastException e) {
            throw new ConfigurationParameterMalformedException(key, e);
        }
    }
    
    protected final<T> T getNoExcept(String key) {
        try {
            return this.<T>get(key);
        } catch (ConfigurationParameterValueException e) {
            try {
                return DefaultConfiguration.<T>getDefaultValue(key);
            } catch (ConfigurationParameterMissingException ex) {
                throw new DefaultValueUndefinedAssertionError(key, ex);
            }
        }
    }
    
    @Override
    public final String getPersistencePath() throws ConfigurationParameterMissingException, ConfigurationParameterMalformedException {
        return this.<String>get("persistence_path");
    }
    
    public static final YAMLFileBasedConfiguration fromDefaultConfigFile() throws ConfigurationLoadingFailedException
    {
        String jboss_home = getJbossHome();
        if(jboss_home == null) {
            throw new ConfigurationLoadingFailedException("JBOSS_HOME environment variable is not set");
        }
        return new YAMLFileBasedConfiguration(getJbossHome() + "/" + DEFAULT_CONFIG_FILEPATH);
    }
    
    protected static final String getJbossHome()
    {
        return System.getenv("JBOSS_HOME");
    }

    @Override
    public List<String> getLoggedAliases() {
        return this.<List<String>>getNoExcept("log_aliases");
    }

    @Override
    public List<String> getLogTagFilters() {
        return this.<List<String>>getNoExcept("log_tag_filters");
    }

    @Override
    public boolean needsLogging(String alias, String tag) {
        return this.getLoggedAliases().contains(alias) && this.passesTagFilters(tag);
    }
    
    /**
     * 
     * @param tag Tag to check against filters
     * @return If tag matches ANY of the filter regexes, returns true. Otherwise returns false.
     */
    protected boolean passesTagFilters(String tag)
    {
        for(String filter : this.getLogTagFilters()) {
            if(tag.matches(filter)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Level getLogLevel() {
        return Level.parse(this.<String>getNoExcept("log_level"));
    }
}
