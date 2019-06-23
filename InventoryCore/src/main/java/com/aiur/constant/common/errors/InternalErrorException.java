package com.aiur.constant.common.errors;

/**
 * Internal server error exception.
 *
 */
@SuppressWarnings("serial")
public class InternalErrorException extends Exception {

	/**
	 * Constructor
	 */
	public InternalErrorException() {
		super(new NullPointerException());
	}
	
	/**
	 * Constructor
	 * @param cause Throwable
	 */
	public InternalErrorException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor
	 * @param message String
	 */
	public InternalErrorException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param message String
	 * @param cause Throwable
	 */
	public InternalErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}



