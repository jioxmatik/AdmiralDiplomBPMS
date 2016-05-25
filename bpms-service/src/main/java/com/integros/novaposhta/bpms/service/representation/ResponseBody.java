package com.integros.novaposhta.bpms.service.representation;

import java.util.Date;

public abstract class ResponseBody extends AbstractSerializableObject {
	private String result;
	private String date;

	public ResponseBody(String result) {
		this.result = result;
		this.date = (new Date()).toString();
	}
	
	public final String getResult() {
		return this.result;
	}
	
	public final void setResult(String result) {
		this.result = result;
	}
	
	public final String getDate() {
		return this.date;
	}
	
	public final void setDate(String date) {
		this.date = date;
	}
} 
