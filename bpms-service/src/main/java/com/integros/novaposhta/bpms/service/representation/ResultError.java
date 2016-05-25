package com.integros.novaposhta.bpms.service.representation;

public class ResultError extends ResponseBody {
	protected static final String RESULT_VALUE = "error";
	protected static final String DEFAULT_ERROR = "unknown error";
	
	private String error;
	
	public ResultError(Exception e) {
		super(RESULT_VALUE);
		this.error = e.toString();
                
                e.printStackTrace();
	}
	
	public ResultError(String error) {
		super(RESULT_VALUE);
		this.error = error;
	}
	
	public ResultError() {
		this(DEFAULT_ERROR);
	}
	
	public String getError() {
		return this.error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
}