package com.mocha.entity;

public class MochaResponse {

	private boolean result = false;
	private String message;
	private double quantine;
	private double cdf;
	
	public MochaResponse() {
	}
	
	public MochaResponse(boolean result) {
		this.result = result;
	}
	
	public boolean isResult() {
		return this.result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	public double getQuantine() {
		return quantine;
	}
	
	public void setQuantine(double quantine) {
		this.quantine = quantine;
	}
	
	public double getCdf() {
		return cdf;
	}
	
	public void setCdf(double cdf) {
		this.cdf = cdf;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}
}

