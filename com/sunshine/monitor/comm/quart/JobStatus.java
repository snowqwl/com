package com.sunshine.monitor.comm.quart;
/**
 * 后台定时任备状态
 * @author OUYANG
 *
 */
public enum JobStatus {
	
	STOP_STATU("0","停止"),
	
	NORMAL_STATU("1","正常"),
	
	RUNING_STATU("2","正在执行");
	
	private String code ;
	
	private String desc ;
	
	private JobStatus(String code, String desc){
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
