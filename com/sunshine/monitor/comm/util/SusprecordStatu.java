package com.sunshine.monitor.comm.util;

/**
 * 布控记录状态
 * @author OUYANG
 *
 */
public enum SusprecordStatu {
	
	SUSP_INIT("0","初始"),
	
	SUSP_DISPATCHED("1","布控"),
	
	SUSP_WITHDRAW("2","撤控");
	
	private String code ;
	
	private String desc ;
	
	private SusprecordStatu(String code,String desc){
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
