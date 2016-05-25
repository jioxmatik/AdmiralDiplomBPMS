package com.integros.novaposhta.bpms.service.contexts;

import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.runtime.helper.BatchExecutionHelper;

import com.thoughtworks.xstream.XStream;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;

public class ServiceContext implements IServiceContext
{
        protected static final KieServices ks = KieServices.Factory.get();
	protected static final XStream xmlMarshaller = BatchExecutionHelper.newXStreamMarshaller();
	protected static final XStream jsonMarshaller = BatchExecutionHelper.newJSonMarshaller();
        
	private final KieContainer kContainer;
	private KieSession kSession;
	private String sessionName;
	
	public ServiceContext(String groupId, String artifactId, String versionId, String sessionName)
	{
		// Create container
		this.kContainer = KieServices.Factory.get().newKieContainer(ks.newReleaseId(groupId, artifactId, versionId));

		// Create session
		this.kSession = newSession(this.kContainer, sessionName);
		this.sessionName = sessionName;
	}
	
	protected static KieSession newSession(KieContainer kContainer, String sessionName)
	{
		KieSession kSession = sessionName.isEmpty() ? kContainer.newKieSession() : kContainer.newKieSession(sessionName);
		return registerWorkItemHandlers(kSession);
	}

        protected static KieSession registerWorkItemHandlers(KieSession kSession)
        {
            return kSession;
        }
        
	public void dispose() 
	{
		kSession.dispose();
	}

	public ExecutionResults execute(BatchExecutionCommand command)
	{
		ExecutionResults executionResults = kSession.execute(command);
		return executionResults;
	}

	public String executeXml(String xmlCommand) {
		return xmlMarshaller.toXML(this.execute((BatchExecutionCommand) xmlMarshaller.fromXML(xmlCommand)));
	}

	public String executeJson(String jsonCommand) {
		return jsonMarshaller.toXML(this.execute((BatchExecutionCommand) jsonMarshaller.fromXML(jsonCommand)));
	}
	
	@Override
	public String toString()
	{
		return "ServiceContext for KieContainer " 
				+ kContainer.getReleaseId().toString()
				+ " with KieSession "
				+ sessionName;
	}

	public void reset()
	{	
		this.reset(this.sessionName);
	}

	public void reset(String sessionName)
	{
		this.dispose();
		kSession = newSession(kContainer, sessionName);
	}

        @Override
        public KieSession getKieSession() {
            return this.kSession;
        }

        @Override
        public KieContainer getKieContainer() {
            return this.kContainer;
        }      

        @Override
        public void reset(KieSession kSession) {
            this.dispose();
            this.kSession = registerWorkItemHandlers(kSession);
        }

        @Override
        public void updateContext(String versionId) {
            ReleaseId releaseId = kContainer.getReleaseId();
            this.updateContext(releaseId.getGroupId(), releaseId.getArtifactId(), versionId);
        }

        @Override
        public void updateContext(String groupId, String artifactId, String versionId) {
            this.updateContext(ks.newReleaseId(groupId, artifactId, versionId));
        }
        
        @Override
        public void updateContext(ReleaseId newReleaseId) {
            this.kContainer.updateToVersion(newReleaseId);
        }

        @Override
        public ReleaseId getReleaseId() {
                return this.kContainer.getReleaseId();
        }

        @Override
        public String getSessionName() {
                return this.sessionName;
        }
}
