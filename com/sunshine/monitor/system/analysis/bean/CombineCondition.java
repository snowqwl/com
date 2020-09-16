package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class CombineCondition extends Entity {

	private static final long serialVersionUID = -2529428641419302619L;
	
	/**
	 * 号牌号码
	 */
	private String hphm;

	/**
	 *  车身颜色
	 */
	private String csys;

	/**
	 * 号牌种类
	 */
	private String hpzl;

	/**
	 * 号牌颜色
	 */
	private String hpys;

	/**
	 * 车辆品牌
	 */
	private String clpp;
	
	/**
	 * 车辆类型
	 */
	private String cllx;

	/**
	 * 频率
	 */
	private String pl;
	
	/**
	 * 频次规则
	 */
	private String pcgz;
	
	/**
	 * 统计类型(1为查询轨迹统计，否则查询轨迹信息)
	 */
	private String tjlx;
	
	/**
	 * 分析类型
	 * 1为假设轨迹分析，2为频繁出入分析，
	 * 3为区域碰撞分析，4为时空碰撞分析
	 */
	private String fxlx;

	/**
	 *  组合条件
	 */
	private String condition;
	
	/**
	 * 卡口数
	 */
	private String kks;
	/**
	 * 天数
	 */
	private String ts;
	/**
	 * 次数
	 */
	private String cs;
	

	//分页标志位，分页为1
	private String pageSign;
	
	public String getKks() {
		return kks;
	}

	public void setKks(String kks) {
		this.kks = kks;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	
	public String getPageSign() {
		return pageSign;
	}

	public void setPageSign(String pageSign) {
		this.pageSign = pageSign;
	}

	public String getCsys() {
		return csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getHpys() {
		return hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getClpp() {
		return clpp;
	}

	public void setClpp(String clpp) {
		this.clpp = clpp;
	}

	public String getPl() {
		return pl;
	}

	public void setPl(String pl) {
		this.pl = pl;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getTjlx() {
		return tjlx;
	}

	public void setTjlx(String tjlx) {
		this.tjlx = tjlx;
	}

	public String getFxlx() {
		return fxlx;
	}

	public void setFxlx(String fxlx) {
		this.fxlx = fxlx;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getPcgz() {
		return pcgz;
	}

	public void setPcgz(String pcgz) {
		this.pcgz = pcgz;
	}

}
