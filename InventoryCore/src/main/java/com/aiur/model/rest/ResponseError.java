package com.aiur.model.rest;

import java.io.Serializable;

/**
 * Response error.
 */
@SuppressWarnings("serial")
public class ResponseError implements Serializable {
	private String message;
	private int errorCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
