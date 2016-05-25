package com.integros.novaposhta.bpms.service.representation;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import org.kie.api.builder.ReleaseId;



public final class ResultContextInfo extends ResultOK {
	private final String context_id;
        private final String groupId;
        private final String artifactId;
        private final String versionId;
        private final String sessionName;
	
	public ResultContextInfo(String contextId, IServiceContext context)
	{
		super();
		this.context_id = contextId;
                ReleaseId releaseId = context.getReleaseId();
                this.groupId = releaseId.getGroupId();
                this.artifactId = releaseId.getArtifactId();
                this.versionId = releaseId.getVersion();
                this.sessionName = context.getSessionName();
	}

	public String getContext_id() {
		return context_id;
	}

        public String getGroupId() {
            return groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public String getVersionId() {
            return versionId;
        }

        public String getSessionName() {
            return sessionName;
        }
}
