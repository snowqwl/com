package com.sunshine.monitor.comm.util;

public enum AlarmSignStau {
	
	NO_SIGN("0","未确认"),
	
	SIGN_VALID("1","确认有效"),
	
	SIGN_INVALID("2","确认无效"),
	
	RECEIVED_ALARM("3","已接报"),
	
	NO_MAKESURE("9","无需确认");
	
	private String code ;
	
	private String desc ;
	
	private AlarmSignStau(String code, String desc){
		this.desc = desc ;
		this.code = code ;
	}
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
}
