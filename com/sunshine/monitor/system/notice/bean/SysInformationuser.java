package com.sunshine.monitor.system.notice.bean;

import com.sunshine.monitor.comm.bean.Entity;


public class SysInformationuser extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String yhdh;
	private String hyyh;
	private String hylb;
	private String bz;
	private String hymc;

	public String getYhdh() {
		return this.yhdh;
	}

	public void setYhdh(String yhdh) {
		this.yhdh = yhdh;
	}

	public String getHyyh() {
		return this.hyyh;
	}

	public void setHyyh(String hyyh) {
		this.hyyh = hyyh;
	}

	public String getHylb() {
		return this.hylb;
	}

	public void setHylb(String hylb) {
		this.hylb = hylb;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getHymc() {
		return this.hymc;
	}

	public void setHymc(String hymc) {
		this.hymc = hymc;
	}
}