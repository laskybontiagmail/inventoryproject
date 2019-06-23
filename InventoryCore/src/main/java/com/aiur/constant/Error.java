package com.aiur.constant;

import java.io.Serializable;

/**
 * Error.
 *
 */
public enum Error implements Serializable{
	ErrorDateTimeOutSync(1),
	ErrorNonceDuplicated(2),
	ErrorRecordNotFound(3),
	ErrorRecordFound(4);
	
	private Integer value;
	/**
	 * Constructor
	 */
	private Error(Integer value) {
		this.value = value;
	}
	public Integer getValue() {
		return value;
	}
	
}

