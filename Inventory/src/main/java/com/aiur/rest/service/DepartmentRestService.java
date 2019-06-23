package com.aiur.rest.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aiur.controller.DepartmentRestController;
import com.aiur.model.rest.BadRequestException;
import com.aiur.model.rest.Response;
import com.aiur.utilities.rest.HTTPUtils;

@RestController
public class DepartmentRestService {
	private static Logger logger = LogManager.getLogger(DepartmentRestService.class);
	
	@RequestMapping(
			value = "/departments/all",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE/*,
			consumes = MediaType.APPLICATION_JSON_VALUE*/
		)
	public ResponseEntity<Response> getAllDepartments(
			@RequestHeader(value="client")  final String datasource
			) throws BadRequestException {
		Response response = new Response();
		String methodName = "getAllDepartments";
		
		DepartmentRestService.logger.info(DepartmentRestService.class.getName() + methodName + "() called!");
		
		//Using EJB
		response = (new DepartmentRestController()).getDepartments(datasource);
		
		return new ResponseEntity<Response>(response, HTTPUtils.getHttpStatus(response.getResponseCode()));
	}
}
