package com.aiur.common.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.constant.Error;
import com.aiur.model.rest.Response;
import com.aiur.model.rest.ResponseError;


public class ServiceManager {
	private static Logger logger = LogManager.getLogger(ServiceManager.class);
	
	/**
	 * A generic way of generating a response object based from a
	 * list of object of a certain type <T>
	 * @param response
	 * @param list
	 */
	public  <T> void setResponseDataFromList(Response response, List<T> list) {
		setResponseData(response, list, null);
	}
	
	/**
	 * A generic way of generating a response object based from a
	 * list of object of a certain type <T>
	 * @param response
	 * @param list
	 */
	public  <T> void setResponseDataFromList(Response response, List<T> list, T common) {
		setResponseData(response, list, common);
	}
	
	/**
	 * A generic way of generating a response object based from
	 * an object of a certain type <T>
	 * @param response
	 * @param list
	 */
	public  <T> void setResponseDataFromObject(Response response, T object, T common) {
		setResponseData(response, object, common);
	}
	
	/**
	 * A generic way of generating a response object based from
	 * an object or a list of objects of a certain type <T>
	 * @param response
	 * @param list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private  <T> void setResponseData(Response response, T responseData, T common) {
		response.setResponseCode(HttpStatus.OK.value());
		Map<String, Object> data = new WeakHashMap<String, Object>();
		Map<String, Object> metadata = new WeakHashMap<String, Object>();
		Date date = new java.util.Date();
		long unixTime = (long) date.getTime() / 1000;
		metadata.put("dateLastUpdated", unixTime);
		data.put("metadata", metadata);
		data.put("content", responseData);
		if (common == null) {
			common = (T) new WeakHashMap();
		}
		data.put("common", common);
		response.setData(data);
	}
	
	/**
	 * Service not available.
	 * @return {Response}
	 */
	public Response serviceUnavailable(Throwable throwable) {
		Response response = new Response();
		response.setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		response.setHasError(true);
		if (throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) {
			ServiceManager.logger.error(throwable.getMessage());
		}
		return response;
	}
	
	/**
	 * Service not available.
	 * @return {Response}
	 */
	public Response serviceUnavailable() {
		Exception exception = new Exception();
		exception.setStackTrace(new StackTraceElement[]{});
		return serviceUnavailable(exception);
	}
	
	/**
	 * Internal service error.
	 * @param message - message
	 * @return {Response}
	 */
	public Response internalServiceError(Throwable throwable) {
		Response response = new Response();
		response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setData(throwable);
		response.setHasError(true);
		if (throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) {
			ServiceManager.logger.error(throwable.getMessage());
		}
		return response;
	}
	
	/**
	 * Internal service error.
	 * @param message - message
	 * @return {Response}
	 */
	public Response internalServiceError() {
		Exception exception = new Exception();
		exception.setStackTrace(new StackTraceElement[]{});
		return internalServiceError(exception);
	}
	
	/**
	 * Database error.
	 * @param message - message
	 * @return {Response}
	 */
	public Response serviceDatabaseError(Throwable throwable) {
		Response response = new Response();
		response.setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		response.setData(throwable.getMessage());
		response.setHasError(true);
		if (throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) {
			ServiceManager.logger.error(throwable.getMessage());
		}
		return response;
	}
	
	/**
	 * Database error.
	 * @param message - message
	 * @return {Response}
	 */
	public Response serviceDatabaseError() {
		Exception exception = new Exception();
		exception.setStackTrace(new StackTraceElement[]{});
		return serviceDatabaseError(exception);
	}
	
	/**
	 * Service bad request
	 * @return {Response}
	 */
	public Response serviceAccepted(String message) {
		Response response = new Response();
		response.setResponseCode(HttpStatus.ACCEPTED.value());
		response.setData(message);
		response.setHasError(true);
		return response;
	}
	
	/**
	 * Service bad request
	 * @return {Response}
	 */
	public Response serviceBadRequest(Throwable throwable) {
		Response response = new Response();
		response.setResponseCode(HttpStatus.BAD_REQUEST.value());
		response.setData(throwable.getMessage());
		response.setHasError(true);
		if (throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) {
			ServiceManager.logger.error(throwable.getMessage());
		}
		return response;
	}
	
	/**
	 * Service bad request
	 * @return {Response}
	 */
	public Response serviceBadRequest(String message) {
		Exception exception = new Exception(message);
		exception.setStackTrace(new StackTraceElement[]{});
		return serviceBadRequest(exception);
	}
	
	/**
	 * Service forbidden.
	 * @return {Response}
	 */
	public Response serviceForbidden() {
		Response response = new Response();
		response.setResponseCode(HttpStatus.FORBIDDEN.value());
		response.setHasError(true);
		return response;
	}
	
		
	/**
	 * Service unauthorized.
	 * @return {Response}
	 */
	public Response serviceUnauthorized() {
		Response response = new Response();
		response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
		response.setHasError(true);
		return response;
	}
	
	/**
	 * Service not found.
	 * @return {Response}
	 */
	public Response serviceNotFound() {
		return serviceNotFound("Not found");
	}
	
	/**
	 * Service not found.
	 * @return {Response}
	 */
	public Response serviceNotFound(String message) {
		Response response = new Response();
		response.setResponseCode(HttpStatus.OK.value());
		ResponseError responseError = new ResponseError();
		responseError.setErrorCode(Error.ErrorRecordNotFound.getValue());
		responseError.setMessage(message);
		response.setData(responseError);
		response.setHasError(true);
		return response;
	}
	/**
	 * Precondition Failed
	 * @return {Response}
	 */
	public Response servicePreconditionFailed() {
		Response response = new Response();
		response.setResponseCode(HttpStatus.PRECONDITION_FAILED.value());
		response.setHasError(true);
		return response;
	}
	
	/**
	 * Service found.
	 * @return {Response}
	 */
	public Response serviceFound() {
		Response response = new Response();
		response.setResponseCode(HttpStatus.OK.value());
		ResponseError responseError = new ResponseError();
		responseError.setErrorCode(Error.ErrorRecordFound.getValue());
		responseError.setMessage("Found");
		response.setData(responseError);
		response.setHasError(true);
		return response;
	}
	
	/**
	 * Service created.
	 * @return {Response}
	 */
	public Response serviceCreated() {
		Response response = new Response();
		response.setResponseCode(HttpStatus.CREATED.value());
		return response;
	}
	
	/**
	 * Service accepted.
	 * @return {Response}
	 */
	public Response serviceAccepted(Object data) {
		Response response = new Response();
		response.setResponseCode(HttpStatus.ACCEPTED.value());
		response.setData(data);
		return response;
	}
}
