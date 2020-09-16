package com.sunshine.monitor.system.gate.event;

import com.sunshine.monitor.comm.context.AbstractApplicationEvent;
/**
 * 
 * @author OUYANG 2014/4/17
 *
 */
public class CodeGateAddorUpdateEvent extends AbstractApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	/** 操作人 */
	private String userName ;

	public CodeGateAddorUpdateEvent(Object obj, String userName){
		super(obj);
		this.userName = userName ;
	}

	public String getUserName() {
		return userName;
	}

}
