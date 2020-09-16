package com.sunshine.monitor.comm.dao.exception;

public class DaoAccessException extends Exception {

	private static final long serialVersionUID = -5802171628524886527L;
	
	public DaoAccessException(){}
	
	public DaoAccessException(String msg){
		super(msg);
	}
	
	public DaoAccessException(Exception e){
		super(e);
	}
	
}
