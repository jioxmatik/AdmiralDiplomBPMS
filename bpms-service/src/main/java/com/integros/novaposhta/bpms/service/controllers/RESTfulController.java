package com.integros.novaposhta.bpms.service.controllers;

import com.integros.novaposhta.bpms.service.contexts.ContextNotFoundException;
import com.integros.novaposhta.bpms.service.contexts.IAsyncEnabledServiceContextManager;
import com.integros.novaposhta.bpms.service.contexts.IServiceContextManager;
import com.integros.novaposhta.bpms.service.contexts.recovery.IRecoverableServiceContextManager;
import com.integros.novaposhta.bpms.service.representation.*;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/resources")
public class RESTfulController implements IBPMSServiceController {
        private static final int GAV_COMPONENTS_COUNT = 3;

        protected final IServiceContextManager contextManager;

        public RESTfulController(IServiceContextManager contextManager)
        {
            this.contextManager = contextManager;
        }
        
	@GET
	@Path("/contexts")
	@Produces("application/json")
	public String getContexts() {
		try {
                    return new ResultContextMap(this.contextManager.getContexts()).asJsonString();
		} catch (Exception e) {
                    return new ResultError(e).asJsonString();
		}
	}
        
        @GET
	@Path("/containers")
	@Produces("application/json")
	public String getContainers() {
		try {
                    return new ResultContainerMap(this.contextManager.getContexts()).asJsonString();
		} catch (Exception e) {
                    return new ResultError(e).asJsonString();
		}
	}
        
        @POST
	@Path("/contexts")
	@Produces("application/json")
	public String createContext(
                @DefaultValue("") @QueryParam("gav") String gav,
                @DefaultValue("") @QueryParam("session") String sessionName
        ) {
		try {
                        String[] gavComponents = gav.split(":");
                        if(gavComponents.length != GAV_COMPONENTS_COUNT) {
                            throw new MalformedGAVInfoException(gav);
                        }
			return new ResultContextCreated(
                                this.contextManager.createContext(
                                        gavComponents[0], 
                                        gavComponents[1], 
                                        gavComponents[2], 
                                        sessionName
                                )
                        ).asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @DELETE
	@Path("/contexts/{contextId}")
	@Produces("application/json")
	public String deleteContext(
                @PathParam("contextId") String contextId
        ) {
		try {
			this.contextManager.deleteContext(contextId);
                        return new ResultOK().asJsonString();
		} catch (ContextNotFoundException e) {
                        return new ResultNOOP().asJsonString();
                } catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @PUT
	@Path("/contexts/{contextId}")
	@Produces("application/json")
	public String resetContext(
                @PathParam("contextId") String contextId,
                @DefaultValue("") @QueryParam("session") String sessionName,
                @DefaultValue("") @QueryParam("gav") String gav
        ) {
		try {
                        if(gav.length() > 0) {
                            throw new UnsupportedControllerOperationException(
                                    "You supplied gav parameter, but container replacement is not currently supported. This request had no effect."
                            );
                        }
                        if(sessionName.length() > 0) {
                            this.contextManager.resetContext(contextId, sessionName);
                        } else {
                            this.contextManager.resetContext(contextId);
                        }
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @GET
	@Path("/contexts/{contextId}")
	@Produces("application/json")
	public String getContext(@PathParam("contextId") String contextId) {
		try {
                        return new ResultContextInfo(contextId, this.contextManager.getContext(contextId)).asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @POST
	@Path("/contexts/{contextId}/commands/{tag}.json")
        @Consumes("application/json")
	@Produces("application/json")
	public String executeJsonCommands(
                @PathParam("contextId") String contextId,
                @PathParam("tag") String tag,
                String data
        ) {
		try {
                        return this.contextManager.executeJsonCommands(contextId, data);
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @POST
	@Path("/contexts/{contextId}/commands/{tag}.xml")
        @Consumes("application/xml")
	@Produces("application/xml")
	public String executeXmlCommands(
                @PathParam("contextId") String contextId,
                @PathParam("tag") String tag,
                String data
        ) {
		try {
                        return this.contextManager.executeXmlCommands(contextId, data);
		} catch (Exception e) {
			return new ResultError(e).asXmlString();
		}
	}
        
        @POST
	@Path("/contexts/{contextId}/commands-async/{tag}.json")
        @Consumes("application/json")
	@Produces("application/json")
	public String executeJsonCommandsAsync(
                @PathParam("contextId") String contextId,
                @PathParam("tag") String tag,
                String data
        ) {
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
	@Path("/contexts/{contextId}/commands-async/{tag}.xml")
        @Consumes("application/xml")
	@Produces("application/xml")
	public String executeXmlCommandsAsync(
                @PathParam("contextId") String contextId,
                @PathParam("tag") String tag,
                String data
        ) {
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
        
        @PUT
	@Path("/contexts/{contextId}/aliases/{alias}")
	@Produces("application/json")
	public String addAlias(
                @PathParam("contextId") String contextId,
                @PathParam("alias") String alias
        ) {
		try {
                        this.contextManager.addAlias(contextId, alias);
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @DELETE
	@Path("/contexts/{contextId}/aliases/{alias}")
	@Produces("application/json")
	public String removeAlias(
                @PathParam("contextId") String contextId,
                @PathParam("alias") String alias
        ) {
		try {
                        this.contextManager.getContext(contextId);  // To produce ContextNotFoundException
                        this.contextManager.removeAlias(alias);
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @GET
	@Path("/contexts/{contextId}/aliases")
	@Produces("application/json")
	public String getAliasesByContextId(@PathParam("contextId") String contextId) {
		try {
                    return new ResultAliasList(this.contextManager.getAliases(contextId)).asJsonString();
		} catch (Exception e) {
                    return new ResultError(e).asJsonString();
		}
	}
        
        @GET
	@Path("/storage")
	@Produces("application/json")
	public String getStoredContexts() {
		try {
                    return new ResultContextIdList(this.contextManager.getPersistedContextIds()).asJsonString();
		} catch (Exception e) {
                    return new ResultError(e).asJsonString();
		}
	}
        
        @PUT
	@Path("/storage/{contextId}")
	@Produces("application/json")
	public String persistContext(@PathParam("contextId") String contextId) {
		try {
                        this.contextManager.persistContext(contextId);
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @POST
	@Path("/storage/{contextId}/restoration.json")
	@Produces("application/json")
	public String restoreContext(
                @PathParam("contextId") String contextId,
                @DefaultValue("") @QueryParam("target") String targetId
        ) {
		try {
                        if(targetId.length() > 0) {
                            this.contextManager.restoreContextToContext(contextId, targetId);
                        } else {
                            this.contextManager.restoreContext(contextId);
                        }
                        return new ResultOK().asJsonString();
		} catch (Exception e) {
			return new ResultError(e).asJsonString();
		}
	}
        
        @POST
        @Path("/storage/restart-recovery.json")
        @Produces("application/json")
        public String recoverFromRestart() {
            try {
                return new ResultCrashRecovery(
                    ((IRecoverableServiceContextManager)this.contextManager).recoverFromCrash()
                ).asJsonString();
            } catch(ClassCastException e) {
                return new ResultError(
                    String.format(
                        "%s does not support previous state recovery", 
                        this.contextManager.getClass().getName()
                    )
                ).asJsonString();
            } catch (Exception e) {
                return new ResultError(e).asJsonString();
            }
        }
}
