package com.sunshine.monitor.system.notice.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class SysInformationreceive extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String yhdh;
	private String xh;
	private String sfjs;
	private String jssj;
	private String jsnr;
	private String bz;

	public String getYhdh() {
		return this.yhdh;
	}

	public void setYhdh(String yhdh) {
		this.yhdh = yhdh;
	}

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getSfjs() {
		return this.sfjs;
	}

	public void setSfjs(String sfjs) {
		this.sfjs = sfjs;
	}

	public String getJssj() {
		return this.jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getJsnr() {
		return this.jsnr;
	}

	public void setJsnr(String jsnr) {
		this.jsnr = jsnr;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}