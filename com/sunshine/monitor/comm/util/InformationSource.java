package com.sunshine.monitor.comm.util;

/**
 * 信息来源
 * @author OUYANG
 *
 */
public enum InformationSource {
	
	LOCAL_WRITER("0","本地录入"),
	
	BATCH_WRITER("1","批量转入"),
	
	LINKAGE_WRITER("2","联动转入"),
	
	INTELLIGENCE_WRITER("5","情报平台转入"),
	
	POLICESYS_WRITER("6","警综系统"),
	
	SISX_INTERGRATE("9","六合一");
	
	private String code ;
	
	private String desc ;
	
	private InformationSource(String code, String desc){
		this.code = code ;
		this.desc = desc ;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	public InformationSource[] getInformationSources(){
		return InformationSource.values();
	}
	
	/**
	 * 根据植找到描述
	 * @param code
	 * @return
	 */
	public String getDesc(String code){
		InformationSource[] isource = InformationSource.values();
		for(InformationSource b : isource){
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
		InformationSource[] isource = InformationSource.values();
		for(InformationSource b : isource){
			if(desc.equals(b.getDesc())){
				return b.getCode();
			}
		}
		return null;
	}	
}
