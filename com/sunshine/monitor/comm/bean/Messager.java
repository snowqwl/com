package com.sunshine.monitor.comm.bean;

import java.util.Date;

public class Messager extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2470674498715324513L;
	
	/**
	 * 卡点编号
	 */
	private String kdbh;
	
	/**
	 * 卡点名称
	 */
	private String kdmc;
	
	/**
	 * 设备名称
	 */
	private String sbbh;
	
	/**
	 * 单位代码
	 */
	private String dwdm;
	
	
	/**
	 * 过车时间
	 */
	private Date gcsj;
	
	
	/**
	 * 中断时间段
	 */
	private String dzsj;

	private String fxbh;
	
	
	
	public String getFxbh() {
		return fxbh;
	}


	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}


	public String getKdbh() {
		return kdbh;
	}


	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}


	public String getKdmc() {
		return kdmc;
	}


	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}


	public String getSbbh() {
		return sbbh;
	}


	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}


	public String getDwdm() {
		return dwdm;
	}


	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}


	public Date getGcsj() {
		return gcsj;
	}


	public void setGcsj(Date gcsj) {
		this.gcsj = gcsj;
	}


	public String getDzsj() {
		return dzsj;
	}


	public void setDzsj(String dzsj) {
		this.dzsj = dzsj;
	}
	
}
