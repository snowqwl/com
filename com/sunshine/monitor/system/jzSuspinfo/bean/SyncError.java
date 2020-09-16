package com.sunshine.monitor.system.jzSuspinfo.bean;

import java.util.Date;

import com.sunshine.monitor.comm.bean.Entity;

public class SyncError extends Entity  {
	private static final long serialVersionUID = 2828882811688061916L;
	
	private String ysbh,hphm,bz;
	private Date tbsj;
	
	public String getYsbh() {
		return ysbh;
	}
	public void setYsbh(String ysbh) {
		this.ysbh = ysbh;
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
	public Date getTbsj() {
		return tbsj;
	}
	public void setTbsj(Date tbsj) {
		this.tbsj = tbsj;
	}
	
}
