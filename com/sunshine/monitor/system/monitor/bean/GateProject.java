package com.sunshine.monitor.system.monitor.bean;

import com.sunshine.monitor.comm.bean.Entity;


public class GateProject extends Entity {
	private static final long serialVersionUID = 1L;
	private String rq;
	private String dwdm;
	private Long ghjrs;
	private Long sjjrs;
	private String inputtime;
	private String dwdmmc;
	public String getInputtime() {
		return inputtime;
	}
	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}
	public String getRq() {
		return rq;
	}
	public void setRq(String rq) {
		this.rq = rq;
	}
	public String getDwdm() {
		return dwdm;
	}
	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}
	public Long getGhjrs() {
		return ghjrs;
	}
	public void setGhjrs(Long ghjrs) {
		this.ghjrs = ghjrs;
	}
	public Long getSjjrs() {
		return sjjrs;
	}
	public void setSjjrs(Long sjjrs) {
		this.sjjrs = sjjrs;
	}
	public void setDwdmmc(String dwdmmc) {
		this.dwdmmc = dwdmmc;
	}
	public String getDwdmmc() {
		return dwdmmc;
	}
	
}
