package com.sunshine.monitor.system.manager.bean;

import com.sunshine.monitor.comm.bean.Entity;
/**
 * 
 * @author OUYANG
 *
 */
public class AdministrativeDivision extends Entity{

	private static final long serialVersionUID = 1L;
	/**
	 * 行政区划
	 */
	private String xzqh;
	
	/**
	 * 行政区划代码前四位
	 */
	private String xzdm;
	
	/**
	 * 行政区划代码后二位（区代码）
	 */
	private String qhdm;
	
	/**
	 * 区名称
	 */
	private String qhmc;
	
	/**
	 * 状态(1=有效0=撤消)
	 */
	private String zt;
	
	/**
	 * 显示标志
	 */
	private String sfxs;
	
	/**
	 * 位置
	 */
	private String wz;
	
	private String bz1;
	
	private String bz2;
	
	//行政区划序号，主要用于行政区划管理
	private String xzqhxh;
	
	private String xzqhqc;
	
	private String pid;
	
	public String getXzqh() {
		return xzqh;
	}

	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}

	public String getXzdm() {
		return xzdm;
	}

	public void setXzdm(String xzdm) {
		this.xzdm = xzdm;
	}

	public String getQhdm() {
		return qhdm;
	}

	public void setQhdm(String qhdm) {
		this.qhdm = qhdm;
	}

	public String getQhmc() {
		return qhmc;
	}

	public void setQhmc(String qhmc) {
		this.qhmc = qhmc;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getSfxs() {
		return sfxs;
	}

	public void setSfxs(String sfxs) {
		this.sfxs = sfxs;
	}

	public String getWz() {
		return wz;
	}

	public void setWz(String wz) {
		this.wz = wz;
	}

	public String getBz1() {
		return bz1;
	}

	public void setBz1(String bz1) {
		this.bz1 = bz1;
	}

	public String getBz2() {
		return bz2;
	}

	public void setBz2(String bz2) {
		this.bz2 = bz2;
	}

	public String getXzqhxh() {
		return xzqhxh;
	}

	public void setXzqhxh(String xzqhxh) {
		this.xzqhxh = xzqhxh;
	}

	public String getXzqhqc() {
		return xzqhqc;
	}

	public void setXzqhqc(String xzqhqc) {
		this.xzqhqc = xzqhqc;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
}
