package com.aiur.model.rest;

/**
 * Bad request exception.
 *
 */
@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {
	
	/**
	 * Constructor.
	 */
	public BadRequestException() {
		super("Invalid request");
	}
	
}
