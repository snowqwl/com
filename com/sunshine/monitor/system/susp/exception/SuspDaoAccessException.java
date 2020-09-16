package com.sunshine.monitor.system.susp.exception;

public class SuspDaoAccessException extends Exception {
	private static final long serialVersionUID = 1994459912090411059L;
	
	public SuspDaoAccessException(){}
	
	public SuspDaoAccessException(String msg){
		super(msg);
	}
	
	public SuspDaoAccessException(Exception e){
		super(e);
	}

}
