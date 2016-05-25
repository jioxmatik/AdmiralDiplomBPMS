package com.integros.novaposhta.bpms.service.controllers;

import com.integros.novaposhta.bpms.service.contexts.IServiceContextManager;
import com.integros.novaposhta.bpms.service.logging.CustomLogger;
import com.integros.novaposhta.bpms.service.representation.ExecutionRequestMetadata;
import com.integros.novaposhta.bpms.service.representation.ResultError;
import com.integros.novaposhta.bpms.service.utils.ExecutionTimer;
import com.integros.novaposhta.bpms.service.utils.ExecutionTimerPrecision;
import com.integros.novaposhta.bpms.service.utils.IExecutionTimer;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

public class VerboseRESTfulController extends RESTfulController {
    private static final Logger LOG = CustomLogger.getLogger(VerboseRESTfulController.class.getName());
    
    private static final String LOG_TAG             = "[COMMANDS]";
    private static final String LOG_PREFIX_START    = "START";
    private static final String LOG_PREFIX_END      = "END";
    
    private static final int xmlBytesPerEntryEstimate = 3809;

    protected final List<String> loggedAliases;
    
    public VerboseRESTfulController(IServiceContextManager contextManager, List<String> loggedAliases) {
        super(contextManager);
        this.loggedAliases = loggedAliases;
    }
    
    @POST
    @Path("/contexts/{contextId}/commands/{tag}.json")
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public String executeJsonCommands(
            @PathParam("contextId") String contextId,
            @PathParam("tag") String tag,
            String data
    ) {
        try {
            if(this.isLoggable(contextId)) {
                IExecutionTimer timer = new ExecutionTimer();
                ExecutionRequestMetadata metadata = new ExecutionRequestMetadata(
                    contextId,
                    tag,
                    "JSON",
                    this.contextManager.translateAlias(contextId),
                    this.contextManager.getContext(contextId),
                    data.length(),
                    -1
                );
                logRequest(metadata, false);
                
                String result;
                try {
                    result = this.contextManager.executeJsonCommands(contextId, data);
                } catch(Exception e) {
                    result = new ResultError(e).asJsonString();
                    metadata.setError(e);
                }
                
                metadata.setResponseMetadata(timer.getTime(ExecutionTimerPrecision.MILLISECONDS), result.length());
                logRequest(metadata, true);
                
                return result;
            } else {
                return super.executeJsonCommands(contextId, tag, data);
            }
        } catch (Exception e) {
            return new ResultError(e).asJsonString();
        }
    }

    @POST
    @Path("/contexts/{contextId}/commands/{tag}.xml")
    @Consumes("application/xml")
    @Produces("application/xml")
    @Override
    public String executeXmlCommands(
            @PathParam("contextId") String contextId,
            @PathParam("tag") String tag,
            String data
    ) {
        try {
            if(this.isLoggable(contextId)) {
                IExecutionTimer timer = new ExecutionTimer();
                ExecutionRequestMetadata metadata = new ExecutionRequestMetadata(
                    contextId,
                    tag,
                    "XML",
                    this.contextManager.translateAlias(contextId),
                    this.contextManager.getContext(contextId),
                    data.length(),
                    data.length() / xmlBytesPerEntryEstimate
                );
                logRequest(metadata, false);
                
                String result;
                try {
                    result = this.contextManager.executeXmlCommands(contextId, data);
                } catch(Exception e) {
                    result = new ResultError(e).asXmlString();
                    metadata.setError(e);
                }
                
                metadata.setResponseMetadata(timer.getTime(ExecutionTimerPrecision.MILLISECONDS), result.length());
                logRequest(metadata, true);
                
                return result;
            } else {
                return super.executeXmlCommands(contextId, tag, data);
            }
        } catch (Exception e) {
            return new ResultError(e).asXmlString();
        }
    }
    
    protected static void logRequest(ExecutionRequestMetadata metadata, boolean end) {
        LOG.info(
            String.format(
                "%s %s %s", 
                LOG_TAG, 
                end ? LOG_PREFIX_END : LOG_PREFIX_START,
                metadata.asJsonString()
            )
        );
    }
    
    protected boolean isLoggable(String contextId) {
        return this.loggedAliases.contains(contextId);
    }
}
