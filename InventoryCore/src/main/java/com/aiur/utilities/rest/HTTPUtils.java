package com.aiur.utilities.rest;

import org.springframework.http.HttpStatus;

public class HTTPUtils {
	/**
	 * HttpStatus conversion.
	 * @param code - code
	 * @return {HttpStatus}
	 */
	public static HttpStatus getHttpStatus(int code) {
		if (HttpStatus.OK.value() == code) {
			return HttpStatus.OK;
		} else if (HttpStatus.ACCEPTED.value() == code) {
			return HttpStatus.ACCEPTED;
		} else if (HttpStatus.ALREADY_REPORTED.value() == code) {
			return HttpStatus.ALREADY_REPORTED;
		} else if (HttpStatus.BAD_GATEWAY.value() == code) {
			return HttpStatus.BAD_GATEWAY;
		} else if (HttpStatus.BAD_REQUEST.value() == code) {
			return HttpStatus.BAD_REQUEST;
		} else if (HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value() == code) {
			return HttpStatus.BANDWIDTH_LIMIT_EXCEEDED;
		} else if (HttpStatus.INSUFFICIENT_STORAGE.value() == code) {
			return HttpStatus.INSUFFICIENT_STORAGE;
		} else if (HttpStatus.TOO_MANY_REQUESTS.value() == code) {
			return HttpStatus.TOO_MANY_REQUESTS;
		} else if (HttpStatus.EXPECTATION_FAILED.value() == code) {
			return HttpStatus.EXPECTATION_FAILED;
		} else if (HttpStatus.CREATED.value() == code) {
			return HttpStatus.CREATED;
		} else if (HttpStatus.UNSUPPORTED_MEDIA_TYPE.value() == code) {
			return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		} else if (HttpStatus.FORBIDDEN.value() == code) {
			return HttpStatus.FORBIDDEN;
		} else if (HttpStatus.FOUND.value() == code) {
			return HttpStatus.FOUND;
		} else if (HttpStatus.GATEWAY_TIMEOUT.value() == code) {
			return HttpStatus.GATEWAY_TIMEOUT;
		} else if (HttpStatus.UNAUTHORIZED.value() == code) {
			return HttpStatus.UNAUTHORIZED;
		} else if (HttpStatus.HTTP_VERSION_NOT_SUPPORTED.value() == code) {
			return HttpStatus.HTTP_VERSION_NOT_SUPPORTED;
		} else if (HttpStatus.INTERNAL_SERVER_ERROR.value() == code) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		} else if (HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value() == code) {
			return HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
		} else if (HttpStatus.NOT_FOUND.value() == code) {
			return HttpStatus.NOT_FOUND;
		} /*else if (HttpStatus.PAYLOAD_TOO_LARGE.value() == code) {
			return HttpStatus.PAYLOAD_TOO_LARGE;
		} */else if (HttpStatus.REQUEST_TIMEOUT.value() == code) {
			return HttpStatus.REQUEST_TIMEOUT;
		} else if (HttpStatus.SERVICE_UNAVAILABLE.value() == code) {
			return HttpStatus.SERVICE_UNAVAILABLE;
		}else if (HttpStatus.PROCESSING.value() == code) {
			return HttpStatus.PROCESSING;
		}
		return null;
	}
}
