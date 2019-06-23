package com.aiur.rest.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aiur.controller.ItemRestController;
import com.aiur.model.rest.BadRequestException;
import com.aiur.model.rest.Response;
import com.aiur.utilities.rest.HTTPUtils;

@RestController
public class ItemRestService {
	private static Logger logger = LogManager.getLogger(ItemRestService.class);
	
	
	@RequestMapping(
			value = "/test/service",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
		)
	public ResponseEntity<Response> testGet() throws BadRequestException {
		Response response = new Response();
		String methodName = "testGet()";
		
		ItemRestService.logger.info(ItemRestService.class.getName() + methodName + " called!");
		
		//Using EJB
		response = (new ItemRestController()).testGet();
		
		return new ResponseEntity<Response>(response, HTTPUtils.getHttpStatus(response.getResponseCode()));
	}
	
	@RequestMapping(
			value = "/items/all",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE/*,
			consumes = MediaType.APPLICATION_JSON_VALUE*/
		)
	public ResponseEntity<Response> getAllItems() throws BadRequestException {
		Response response = new Response();
		String methodName = "get";
		
		ItemRestService.logger.info(ItemRestService.class.getName() + methodName + "() called!");
		
		//Using EJB
		response = (new ItemRestController()).getItems();
		
		return new ResponseEntity<Response>(response, HTTPUtils.getHttpStatus(response.getResponseCode()));
	}
}
