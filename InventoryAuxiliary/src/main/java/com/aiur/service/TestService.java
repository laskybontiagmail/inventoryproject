package com.aiur.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class TestService {
	private static Logger logger = LogManager.getLogger(TestService.class);
	
	public TestService() {
		TestService.logger.error("logger: TestService is constructed!");
		System.out.println("sysout: TestService is constructed!");
	}
	
	@PostConstruct
	public void doThisPostConstruct() {
		TestService.logger.error("logger: TestService is postconstruct called!");
		System.out.println("sysout: TestService is postconstruct called!");
	}
}
