package com.sunshine.monitor.system.susp.exception;

public class SuspinfoValidateException extends Exception {
	
	private static final long serialVersionUID = -2617758986362760732L;
	
	public SuspinfoValidateException(){}
	
	public SuspinfoValidateException(String msg){
		super(msg);
	}
	
	public SuspinfoValidateException(Exception e){
		super(e);
	}

}
