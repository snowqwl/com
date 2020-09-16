package com.sunshine.monitor.comm.util.orm.exception;

/**
 * 方法映射错误
 * @author lifenghu
 *
 */
public class ReflectMethodException extends ORMException {
	private static final long serialVersionUID = 7423858196384316144L;

	public ReflectMethodException(){}
	
	public ReflectMethodException(String msg){
		super(msg);
	}
	
	public ReflectMethodException(Exception e){
		super(e);
	}
}
