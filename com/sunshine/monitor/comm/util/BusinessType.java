package com.sunshine.monitor.comm.util;

/**
 * 业务种类
 * @author OUYANG 2012/06/10
 *
 */
public enum BusinessType {

	DISPATCHED_APPLY("10","布控申请"),
	
	DISPATCHED_CHECK("11","布控审核"),
	
	DISPATCHED_APPROVAL("12","布控审批"),
	
	DISPATCHED_MODIFICATION("13","布控修改"),
	
	WITHDRAW_APPLY("20","撤控申请"),
	
	WITHDRAW_CHECK("21","撤控审核"),
	
	WITHDRAW_APPROVAL("22","撤控审批"),
	
	WITHDRAW_AUTO("23","自动撤控"),
	
	ALARM_CONFIRM("41","预警确认"),
	
	COMMAND_GIVE("42","下达指令"),
	
	POLICE_REGIST("43","出警登记"),
	
	POLICE_RETURN("44","出警反馈");
	
	private String code ;
	
	private String desc ;
	
	private BusinessType(String code, String desc){
		this.code = code ;
		this.desc = desc ;
	}
	
	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public BusinessType[] getBusinessTypes(){
		return BusinessType.values();
	}
	
	/**
	 * 根据植找到描述
	 * @param code
	 * @return
	 */
	public String getDesc(String code){
		BusinessType[] businesses = BusinessType.values();
		for(BusinessType b : businesses){
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
		BusinessType[] businesses = BusinessType.values();
		for(BusinessType b : businesses){
			if(desc.equals(b.getDesc())){
				return b.getCode();
			}
		}
		return null;
	}	
}
