package com.mocha.entity;

public class MochaResponse {

	private boolean result;
	
	public MochaResponse(boolean result) {
		this.result = result;
	}
	
	public boolean isResult() {
		return this.result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
