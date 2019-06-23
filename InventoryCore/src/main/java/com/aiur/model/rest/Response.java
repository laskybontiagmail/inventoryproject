package com.aiur.model.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
	
	private int responseCode;
	private Object data;
	private ElapseExecutionTime elapsedExecutionTime;
	private boolean hasError;

	public Response(Object data, int responseCode) {
		this.data = data;
		this.responseCode = responseCode;
	}

	public Response() {
	}

//	public int getResponseCode() {
//		return responseCode;
//	}
//
//	public void setResponseCode(int responseCode) {
//		this.responseCode = responseCode;
//	}
//
//	public Object getData() {
//		return data;
//	}
//
//	public void setData(Object data) {
//		this.data = data;
//	}
//
//	public ElapseExecutionTime getElapsedExecutionTime() {
//		return elapsedExecutionTime;
//	}
//
//	public void setElapsedExecutionTime(ElapseExecutionTime elapsedExecutionTime) {
//		this.elapsedExecutionTime = elapsedExecutionTime;
//	}
}
