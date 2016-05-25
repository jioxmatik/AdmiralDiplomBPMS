package com.integros.novaposhta.bpms.service.controllers;

import com.integros.novaposhta.bpms.service.representation.ResultError;
import com.integros.novaposhta.bpms.service.representation.ResultNOOP;
import com.integros.novaposhta.bpms.service.representation.ResultOK;
import com.integros.novaposhta.bpms.service.representation.ResultContextCreated;
import com.integros.novaposhta.bpms.service.contexts.ContextNotFoundException;
import com.integros.novaposhta.bpms.service.contexts.IAsyncEnabledServiceContextManager;
import com.integros.novaposhta.bpms.service.contexts.IServiceContextManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("/bpms-service")
public class RPCController implements IBPMSServiceController {
        private final IServiceContextManager contextManager;

        public RPCController(IServiceContextManager contextManager)
        {
            this.contextManager = contextManager;
        }
        
	@POST
	@Path("/create-context/{groupId}/{artifactId}/{versionId}/{sessionName}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String createContext(@PathParam("groupId") String groupId, @PathParam("artifactId") String artifactId, @PathParam("versionId") String versionId, @PathParam("sessionName") String sessionName) {
		try {
			return new ResultContextCreated(
                                this.contextManager.createContext(
                                        groupId, 
                                        artifactId, 
                                        versionId, 
                                        sessionName
                                )
                        ).asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
	
	@POST
	@Path("/update-context/{contextId}/to/{groupId}/{artifactId}/{versionId}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String updateContext(@PathParam("contextId") String contextId, @PathParam("groupId") String groupId, @PathParam("artifactId") String artifactId, @PathParam("versionId") String versionId){
		try {
			this.contextManager.updateContext(contextId, groupId, artifactId, versionId);
			return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
	
	@POST
	@Path("/update-context/{contextId}/to/{versionId}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String updateContext(@PathParam("contextId") String contextId, @PathParam("versionId") String versionId){
		try {
			this.contextManager.updateContext(contextId, versionId);
			return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
	
	@POST
	@Path("/delete-context/{contextId}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String deleteContext(@PathParam("contextId") String contextId) {
		try {
			this.contextManager.deleteContext(contextId);
                        return new ResultOK().asJsonString();
		} catch (ContextNotFoundException e) {
                        return new ResultNOOP().asJsonString();
                } catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
	
	@POST
	@Path("/reset-context/{contextId}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String resetContext(@PathParam("contextId") String contextId) {
		try {
			this.contextManager.resetContext(contextId);
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
	
	@POST
	@Path("/execute/{contextId}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String executeCommands(@Context HttpHeaders headers, @PathParam("contextId") String contextId, String data) {
		try {
                    if(data.trim().startsWith("{")) {
                            // Attempt JSON
                            return this.contextManager.executeJsonCommands(contextId, data);
                    } else {
                            // Attempt XML
                            return this.contextManager.executeXmlCommands(contextId, data);
                    }
                } catch(Exception e) {
                    return new ResultError(e).asJsonString();
                }
	}
	
	@POST
	@Path("/execute/{contextId}.xml")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String executeXmlCommands(@PathParam("contextId") String contextId, String data) {
		try {
			return this.contextManager.executeXmlCommands(contextId, data);
		} catch (Exception e) {
			return new ResultError(e).asXmlString();
		}
	}
	
	@POST
	@Path("/execute/{contextId}.json")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String executeJsonCommands(@PathParam("contextId") String contextId, String data) {
		try {
			return this.contextManager.executeJsonCommands(contextId, data);
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @POST
	@Path("/execute-async/{contextId}.xml")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String executeXmlCommandsAsync(@PathParam("contextId") String contextId, String data) {
            try {
                ((IAsyncEnabledServiceContextManager)this.contextManager).executeXmlCommandsAsync(contextId, data);
                return new ResultOK().asXmlString();
            } catch(ClassCastException e) {
                return new ResultError(
                    String.format(
                        "%s does not support asynchronous commands execution", 
                        this.contextManager.getClass().getName()
                    )
                ).asXmlString();
            } catch (Exception e) {
                return new ResultError(e).asXmlString();
            }
	}
	
	@POST
	@Path("/execute-async/{contextId}.json")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String executeJsonCommandsAsync(@PathParam("contextId") String contextId, String data) {
            try {
                ((IAsyncEnabledServiceContextManager)this.contextManager).executeJsonCommandsAsync(contextId, data);
                return new ResultOK().asJsonString();
            } catch(ClassCastException e) {
                return new ResultError(
                    String.format(
                        "%s does not support asynchronous commands execution", 
                        this.contextManager.getClass().getName()
                    )
                ).asJsonString();
            } catch (Exception e) {
                return new ResultError(e).asJsonString();
            }
	}
        
        @POST
	@Path("/persist-context/{contextId}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String persistContext(@PathParam("contextId") String contextId, String data) {
		try {
                        this.contextManager.persistContext(contextId);
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @POST
	@Path("/restore-context/{contextId}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String restoreContext(@PathParam("contextId") String contextId, String data) {
		try {
                        this.contextManager.restoreContext(contextId);
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @POST
	@Path("/restore-context/{contextId}/to/{targetId}")
	@Consumes("text/plain")
	@Produces("text/plain")
	public String restoreContextToContext(@PathParam("contextId") String contextId, @PathParam("targetId") String targetId, String data) {
		try {
                        this.contextManager.restoreContextToContext(contextId, targetId);
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
}
