package com.sunshine.monitor.system.ws.util;

public class NotDefPaserException extends Exception {
	
	private static final long serialVersionUID = 8029239054864666215L;

	public NotDefPaserException(){
		super();
	}
	
	public NotDefPaserException(String message){
		super(message);
	}
	
	public NotDefPaserException(String message,Throwable e){
		super(message,e);
	}
	
	public NotDefPaserException(Throwable cause) {
        super(cause);
    }
}
