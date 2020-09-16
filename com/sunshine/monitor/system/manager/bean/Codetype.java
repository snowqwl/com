package com.sunshine.monitor.system.manager.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class Codetype extends Entity{

	private static final long serialVersionUID = 5643935792600225191L;
	
	
	private String dmlb;
	private String lbsm;
	private Long dmcd;
	private String lbsx;
	private String lbbz;
	private String bz;
	
	
	public String getDmlb() {
		return dmlb;
	}
	public void setDmlb(String dmlb) {
		this.dmlb = dmlb;
	}
	public String getLbsm() {
		return lbsm;
	}
	public void setLbsm(String lbsm) {
		this.lbsm = lbsm;
	}
	public String getLbsx() {
		return lbsx;
	}
	public void setLbsx(String lbsx) {
		this.lbsx = lbsx;
	}
	public String getLbbz() {
		return lbbz;
	}
	public void setLbbz(String lbbz) {
		this.lbbz = lbbz;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public Long getDmcd() {
		return dmcd;
	}
	public void setDmcd(Long dmcd) {
		this.dmcd = dmcd;
	}
}
