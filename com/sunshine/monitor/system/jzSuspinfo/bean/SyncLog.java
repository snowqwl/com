package com.sunshine.monitor.system.jzSuspinfo.bean;

import java.util.Date;

import com.sunshine.monitor.comm.bean.Entity;

public class SyncLog extends Entity  {
	private static final long serialVersionUID = -9041603147420083634L;
	
	private  String rwbh,ywbh,czjg,tbnr;
	private Date tbsj;
	
	public String getRwbh() {
		return rwbh;
	}
	public void setRwbh(String rwbh) {
		this.rwbh = rwbh;
	}
	public String getYwbh() {
		return ywbh;
	}
	public void setYwbh(String ywbh) {
		this.ywbh = ywbh;
	}
	public String getCzjg() {
		return czjg;
	}
	public void setCzjg(String czjg) {
		this.czjg = czjg;
	}
	public String getTbnr() {
		return tbnr;
	}
	public void setTbnr(String tbnr) {
		this.tbnr = tbnr;
	}
	public Date getTbsj() {
		return tbsj;
	}
	public void setTbsj(Date tbsj) {
		this.tbsj = tbsj;
	}
	
}
