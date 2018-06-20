package com.xxb.base;

public class ProjectException extends RuntimeException {

	private static final long serialVersionUID = -3915601237140823973L;
	
	public final static String NeedReg = "needReg";
	
	public final static String NeedLogin = "needLogin";
	
	private String errorCode;

	public ProjectException(String msg) {
		super(msg);
	}

	public ProjectException(String msg, String errorCode) {
		super(msg);
		this.setErrorCode(errorCode);
	}

	public ProjectException(String msg, String errorCode, Throwable e) {
		super(msg, e);
		this.setErrorCode(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
