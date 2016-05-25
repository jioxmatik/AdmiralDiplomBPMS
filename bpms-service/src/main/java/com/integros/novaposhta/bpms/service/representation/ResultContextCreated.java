package com.integros.novaposhta.bpms.service.representation;



public final class ResultContextCreated extends ResultOK {
	private String context_id;
	
	public ResultContextCreated(String context_id)
	{
		super();
		this.setContext_id(context_id);
	}

	public String getContext_id() {
		return context_id;
	}

	public void setContext_id(String context_id) {
		this.context_id = context_id;
	}
}
