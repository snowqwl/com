package com.sunshine.monitor.system.manager.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class CodeUrl extends Entity {

	
	private static final long serialVersionUID = 8793032803127962131L;
	private String dwdm;
	private String url;
	private String port;
	private String context;
	private String jb;
	private String jdmc;
	private String sn;
	private String sjjd;
	private String bz;

	public String getDwdm() {
		return this.dwdm;
	}

	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getJb() {
		return this.jb;
	}

	public void setJb(String jb) {
		this.jb = jb;
	}

	public String getJdmc() {
		return this.jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}

	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSjjd() {
		return this.sjjd;
	}

	public void setSjjd(String sjjd) {
		this.sjjd = sjjd;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

}
