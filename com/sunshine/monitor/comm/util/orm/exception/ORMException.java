package com.sunshine.monitor.comm.util.orm.exception;

/**
 * 操作数据库错误
 * @author lifenghu
 *
 */
public class ORMException extends Exception {
	private static final long serialVersionUID = -562949115040398176L;

	public ORMException(){
		super();
	}
	
	public ORMException(String msg){
		super(msg);
	}
	
	public ORMException(Exception e){
		super(e);
	}
}
