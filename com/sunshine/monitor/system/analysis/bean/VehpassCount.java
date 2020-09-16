package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class VehpassCount extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String kdbh;      //卡点编号
	
	private String gcs;       //过车数
	
	private String xcs;       //小车数
	
	private String gcsj;      //过车时间
	
	private String tjrq;      //统计日期

	public String getKdbh() {
		return kdbh;
	}

	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}

	public String getGcs() {
		return gcs;
	}

	public void setGcs(String gcs) {
		this.gcs = gcs;
	}

	public String getXcs() {
		return xcs;
	}

	public void setXcs(String xcs) {
		this.xcs = xcs;
	}

	public String getGcsj() {
		return gcsj;
	}

	public void setGcsj(String gcsj) {
		this.gcsj = gcsj;
	}

	public String getTjrq() {
		return tjrq;
	}

	public void setTjrq(String tjrq) {
		this.tjrq = tjrq;
	}
	
}
