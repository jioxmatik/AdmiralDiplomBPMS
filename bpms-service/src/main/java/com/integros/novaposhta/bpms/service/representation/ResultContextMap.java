package com.integros.novaposhta.bpms.service.representation;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.kie.api.builder.ReleaseId;

public class ResultContextMap extends ResultOK {
	
        protected class ServiceContextRepresentation {
            private final String groupId;
            private final String artifactId;
            private final String versionId;
            private final String sessionName;
            
            public ServiceContextRepresentation(IServiceContext represented) {
                ReleaseId releaseId = represented.getReleaseId();
                this.groupId = releaseId.getGroupId();
                this.artifactId = releaseId.getArtifactId();
                this.versionId = releaseId.getVersion();
                this.sessionName = represented.getSessionName();
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
    
        private final Map<String,ServiceContextRepresentation> contexts = new HashMap<String,ServiceContextRepresentation>();

        public Map<String,ServiceContextRepresentation> getContexts() {
            return contexts;
        }
        
        public ResultContextMap(Map<String,IServiceContext> contexts) {
		super();
                for(Entry<String,IServiceContext> entry : contexts.entrySet()) {
                    this.contexts.put(entry.getKey(), new ServiceContextRepresentation(entry.getValue()));
                }
	}
}