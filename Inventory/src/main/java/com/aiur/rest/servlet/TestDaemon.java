package com.aiur.rest.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestDaemon extends Thread {
	private static Logger logger = LogManager.getLogger(TestDaemon.class);
	
	static {
		(new TestDaemon()).start();
	}
	
	@Override
	public void run() {
		while (true) {
			TestDaemon.logger.info("TestDaemon is running!!!");
			try {
				Thread.sleep(500);
			} catch (Exception exception) {
				
			}
		}
	}
}
