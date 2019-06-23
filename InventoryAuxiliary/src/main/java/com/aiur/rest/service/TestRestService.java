package com.aiur.rest.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aiur.controller.TestRestController;
import com.aiur.model.rest.BadRequestException;
import com.aiur.model.rest.Response;
import com.aiur.utilities.rest.HTTPUtils;

@RestController
public class TestRestService {
	private static Logger logger = LogManager.getLogger(TestRestService.class);
	
	@RequestMapping(
			value = "/test/service",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
		)
	public ResponseEntity<Response> testGet() throws BadRequestException {
		Response response = new Response();
		String methodName = "testGet()";
		
		TestRestService.logger.info(TestRestService.class.getName() + methodName + " called!");
		
		//Using EJB
		response = (new TestRestController()).testGet();
		
		return new ResponseEntity<Response>(response, HTTPUtils.getHttpStatus(response.getResponseCode()));
	}
}
