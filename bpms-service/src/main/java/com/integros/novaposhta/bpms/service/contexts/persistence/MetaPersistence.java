package com.integros.novaposhta.bpms.service.contexts.persistence;

import org.kie.api.builder.ReleaseId;

public class MetaPersistence {
	private String sessionName;
	
	private String groupId;
	
	private String artifactId;
	
	private String version;

	public MetaPersistence(){
		
	}
	
	public MetaPersistence(String sessionName, String groupId,
			String artifactId, String version) {
		super();
		this.sessionName = sessionName;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}
	
	public MetaPersistence(String sessionName, ReleaseId releaseId) {
		super();
		this.sessionName = sessionName;
		this.groupId = releaseId.getGroupId();
		this.artifactId = releaseId.getArtifactId();
		this.version = releaseId.getVersion();
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String versionId) {
		this.version = versionId;
	}
}
