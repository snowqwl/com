package com.sunshine.monitor.comm.util;

/**
 * 布控审核，审批流程标志位
 * 撤控审核、审批流程标志位
 * @author OUYANG
 *
 */
public enum SuspApprovalFlag {
	
	DISPATCHED_CHECK("1","布控审核"),
	
	DISPATCHED_APPROVAL("2","布控审批"),
	
	WITHDRAW_CHECK("3","撤控审核"),
	
	WITHDRAW_APPROVAL("4","撤控审批");
	
	private String code ;
	
	private String desc ;
	
	private SuspApprovalFlag(String code, String desc){
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
