package com.sunshine.monitor.system.analysis.bean;

/**
 * 号牌号码注册地维护信息实体
 * @author licheng
 *
 */
public class HphmEntity {
	
	/**
	 * 行政区划代码
	 */
	private String dwdm; 

	/**
	 * 所属注册地
	 */
	private String hphm;
	
	/**
	 * 备注(地市名称)
	 */
	private String bz;

	public String getDwdm() {
		return dwdm;
	}

	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
