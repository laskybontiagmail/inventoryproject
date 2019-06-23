package com.aiur.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.ejb.TestRestBean;
import com.aiur.ejb.beans.TestRestBeanImplementation;
import com.aiur.model.rest.Response;
import com.aiur.utilities.ejb.EJB3;

public class TestRestController {
	private static Logger logger = LogManager.getLogger(TestRestController.class);
	private static TestRestBean testRestBean = null;
	
	public TestRestController() {
		String methodName = "TestRestController()";
		TestRestController.logger.info(methodName + " {");
		
		if (TestRestController.testRestBean == null) {
			TestRestController.logger.info("Creating " + TestRestBean.class.getSimpleName() + " (via EJB3)");
			TestRestController.testRestBean = EJB3.getInstance().objectLookUp(TestRestBean.class, TestRestBeanImplementation.class);
		} else {
			TestRestController.logger.info(TestRestController.testRestBean + " has been created already.");
		}
		
		TestRestController.logger.info("} " + methodName);
	}
	
	public Response testGet() {
		Response response = new Response();
		String methodName = "testGet()";
		
		TestRestController.logger.info(methodName + " {");
		
		if (TestRestController.testRestBean != null) {
			response = TestRestController.testRestBean.testGet();
		} else {
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setData(TestRestBean.class.getName() + " was not loaded!");
		}
		
		TestRestController.logger.info("} " + methodName);
		return response;
	}
}
