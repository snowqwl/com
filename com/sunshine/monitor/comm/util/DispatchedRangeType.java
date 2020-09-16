package com.sunshine.monitor.comm.util;

/**
 * 布控范围类型
 * @author OUYANG
 *
 */
public enum DispatchedRangeType {
	
	LOCAL_DISPATCHED("1","本地布控"),
	
	LINKAGE_DISPATCHED("2","联动布控"),
	
	KLINKAGE_DISPATCHED("3","跨省布控");
	
	private String code ;
	
	private String desc ;
	
	private DispatchedRangeType(String code, String desc){
		this.code = code ;
		this.desc = desc ;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public DispatchedRangeType[] getDispatchedRangeTypes(){
		return DispatchedRangeType.values();
	}
	
	/**
	 * 根据植找到描述
	 * @param code
	 * @return
	 */
	public String getDesc(String code){
		DispatchedRangeType[] drs = DispatchedRangeType.values();
		for(DispatchedRangeType b : drs){
			if(code.equals(b.getCode())){
				return b.getDesc();
			}
		}
		return null;
	}
	
	/**
	 * 根据描述找到值
	 * @param desc
	 * @return
	 */
	public String getCode(String desc){
		DispatchedRangeType[] drs = DispatchedRangeType.values();
		for(DispatchedRangeType b : drs){
			if(desc.equals(b.getDesc())){
				return b.getCode();
			}
		}
		return null;
	}
	
}
