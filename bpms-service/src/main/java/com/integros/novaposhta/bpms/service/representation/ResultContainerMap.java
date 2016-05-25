package com.integros.novaposhta.bpms.service.representation;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.kie.api.builder.ReleaseId;

public class ResultContainerMap extends ResultOK {
        private static final String GAV_SEPARATOR = ":";
    
        private final Map<String,List<String>> containers = new HashMap<String,List<String>>();

        public Map<String,List<String>> getContainers() {
            return containers;
        }
        
        public ResultContainerMap(Map<String,IServiceContext> contexts) {
		super();
                for(Entry<String,IServiceContext> entry : contexts.entrySet()) {
                    String releaseId = releaseIdToString(entry.getValue().getReleaseId());
                    if(!containers.containsKey(releaseId)) {
                        containers.put(releaseId, new LinkedList<String>());
                    }
                    containers.get(releaseId).add(entry.getKey());
                }
	}
        
        protected static String releaseIdToString(ReleaseId releaseId)
        {
            return releaseId.getGroupId() + GAV_SEPARATOR
                    + releaseId.getArtifactId() + GAV_SEPARATOR
                    + releaseId.getVersion();
        }
}