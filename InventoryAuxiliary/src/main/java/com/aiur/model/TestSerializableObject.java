package com.aiur.model;

import java.io.Serializable;

public class TestSerializableObject implements Serializable {
	private static final long serialVersionUID = 1234567890L;
	
	private int number;
	private String string;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
}