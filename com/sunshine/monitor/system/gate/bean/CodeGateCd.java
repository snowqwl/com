package com.sunshine.monitor.system.gate.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class CodeGateCd extends Entity {

	private static final long serialVersionUID = -6190066857420258925L;
	private String kdbh;
	private String fxlx;
	private String fxbh;
	private String cdbh;
	private String cdlx;
	private Long xcxs;
	private Long dcxs;
	private Long zdxs;
	private String xxpz;
	private String ip;
	private String bz;

	public String getKdbh() {
		return kdbh;
	}
	

	public String getFxbh() {
		return fxbh;
	}


	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}


	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}

	public String getCdbh() {
		return cdbh;
	}

	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
	}

	public String getCdlx() {
		return cdlx;
	}

	public void setCdlx(String cdlx) {
		this.cdlx = cdlx;
	}

	public Long getZdxs() {
		return zdxs;
	}

	public void setZdxs(Long zdxs) {
		this.zdxs = zdxs;
	}

	public Long getXcxs() {
		return xcxs;
	}

	public void setXcxs(Long xcxs) {
		this.xcxs = xcxs;
	}

	public Long getDcxs() {
		return dcxs;
	}

	public void setDcxs(Long dcxs) {
		this.dcxs = dcxs;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getFxlx() {
		return fxlx;
	}

	public void setFxlx(String fxlx) {
		this.fxlx = fxlx;
	}

	public String getXxpz() {
		return xxpz;
	}

	public void setXxpz(String xxpz) {
		this.xxpz = xxpz;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
	
}
