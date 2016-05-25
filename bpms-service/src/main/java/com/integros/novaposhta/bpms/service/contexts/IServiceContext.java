package com.integros.novaposhta.bpms.service.contexts;

import org.kie.api.builder.ReleaseId;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public interface IServiceContext {
	void dispose();
	void reset();
	void reset(String sessionName);
        void reset(KieSession kSession);
	ExecutionResults execute(BatchExecutionCommand command);
	String executeXml(String xmlCommand);
	String executeJson(String jsonCommand);
        KieSession getKieSession();
        KieContainer getKieContainer();
        void updateContext(String versionId);
        void updateContext(String groupId, String artifactId, String versionId);
        void updateContext(ReleaseId releaseId);
        ReleaseId getReleaseId();
        String getSessionName();
}
