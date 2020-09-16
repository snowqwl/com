package com.sunshine.monitor.system.manager.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class TrafficDepartment extends Entity {
  
	private static final long serialVersionUID = 1L;
    private String glbm;
    private String bmqc;
    private String sjbm;
	public String getGlbm() {
		return glbm;
	}
	public void setGlbm(String glbm) {
		this.glbm = glbm;
	}
	public String getBmqc() {
		return bmqc;
	}
	public void setBmqc(String bmqc) {
		this.bmqc = bmqc;
	}
	public String getSjbm() {
		return sjbm;
	}
	public void setSjbm(String sjbm) {
		this.sjbm = sjbm;
	}
	
	
    
}
