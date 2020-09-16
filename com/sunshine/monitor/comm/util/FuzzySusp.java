package com.sunshine.monitor.comm.util;

/**
 * 是否模糊布控
 * @author OUYANG
 *
 */
public enum FuzzySusp {
	
	FUZZY_TRUE("1","是模糊布控"),
	
	FUZZY_FALSE("0","不是模糊布控");
	
	private String code ;
	
	private String desc ;
	
	private FuzzySusp(String code, String desc){
		this.code = code ;
		this.desc = desc ;
	}
	
	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}