package com.sunshine.monitor.comm.util.orm.exception;

/**
 * 初始化表格错误
 * @author lifenghu
 *
 */
public class InitTableInfoException extends ORMException {
	private static final long serialVersionUID = -713866270086074231L;

	public InitTableInfoException(){
		super();
	}
	
	public InitTableInfoException(String msg){
		super(msg);
	}
	
	public InitTableInfoException(Exception e){
		super(e);
	}
}
