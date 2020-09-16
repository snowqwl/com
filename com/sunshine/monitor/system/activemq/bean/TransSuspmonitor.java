 package com.sunshine.monitor.system.activemq.bean;
 
 import java.io.Serializable;
 
 public class TransSuspmonitor implements Serializable {

    private static final long serialVersionUID = 2894803613521935550L;
    private String bkxh;
    private String dwdm;
    private String bkjg;
    private String lrsj;
   
	public String getBkxh() {
		return bkxh;
	}
	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}
	public String getDwdm() {
		return dwdm;
	}
	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}
	public String getBkjg() {
		return bkjg;
	}
	public void setBkjg(String bkjg) {
		this.bkjg = bkjg;
	}
	public String getLrsj() {
		return lrsj;
	}
	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}
   
 }

