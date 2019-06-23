package com.aiur.service;

public class Test implements TestMBean {
	public String Message;

	public Test() {
		System.out.println(" TestMBean is activated...inside Test() constructor, Message: " + Message);
	}
	
	@Override
	public void setMessage(String message) {
		System.out.println(" Server Watch Message is set to : " + Message);
		this.Message = Message;
	}

	@Override
	public String getMessage() {
		System.out.println(" Server Watch Message is set to : " + Message);
		return this.Message;
	}
	
	public void start() throws Exception {
		System.out.println(" Starting start() Test invoked");
	}

	public void stop() throws Exception {
		System.out.println(" Stopping stop() Test  invoked");
	}
}
