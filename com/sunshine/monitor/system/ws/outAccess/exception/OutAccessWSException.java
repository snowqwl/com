package com.sunshine.monitor.system.ws.outAccess.exception;

public class OutAccessWSException extends Exception {
	private static final long serialVersionUID = 3106367324216129270L;
	
	private String errorCode = "999";
	private String exceptionMsg = "";
	private String info = "";
	
	public OutAccessWSException(){ 
		super(); 
	}
	
	public OutAccessWSException(String exceptionMsg){ 
		super(exceptionMsg);
		this.exceptionMsg = exceptionMsg;
	}
	
	public OutAccessWSException(Exception e){
		super(e);
		this.exceptionMsg = e.getMessage();
	}
	
	public OutAccessWSException(String code,String exceptionMsg,String info){
		this(exceptionMsg);
		this.errorCode = code;
		this.info = info;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
