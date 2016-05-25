package com.integros.novaposhta.bpms.service.configuration;

import java.util.List;
import java.util.logging.Level;

public interface IBPMSServiceConfiguration {
    String getPersistencePath() throws ConfigurationParameterMissingException, ConfigurationParameterMalformedException;
    /**
     * 
     * @return Collection of aliases, requests through which have to be logged. 
     * Empty collection, if parameter is not set in config.
     */
    List<String> getLoggedAliases();
    
    /**
     * 
     * @return Collection of regexes, against which URL tags are checked to determine
     * whether request should be logged or not. Empty collection, if parameter is not set in config.
     */
    List<String> getLogTagFilters();
    
    boolean needsLogging(String alias, String tag);
    
    Level getLogLevel();
}
