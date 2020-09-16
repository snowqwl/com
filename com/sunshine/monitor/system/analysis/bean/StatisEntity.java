package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 统计信息实体类
 * 
 */
public class StatisEntity extends Entity {

	private static final long serialVersionUID = 1L;

	// 主题编号
	private String ztbh;

	// 号牌号码
	private String hphm;

	// 号牌种类
	private String hpzl;

	// 号牌颜色
	private String hpys;

	// 卡口数
	private String kks;

	// 行政区数
	private String xzqs;

	// 天数
	private String ts;

	// 次数
	private String cs;

	// 是否预警
	private String sfyj;

	// 备注
	private String bz;

	public String getZtbh() {
		return ztbh;
	}

	public void setZtbh(String ztbh) {
		this.ztbh = ztbh;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
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

	public String getKks() {
		return kks;
	}

	public void setKks(String kks) {
		this.kks = kks;
	}

	public String getXzqs() {
		return xzqs;
	}

	public void setXzqs(String xzqs) {
		this.xzqs = xzqs;
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

	public String getSfyj() {
		return sfyj;
	}

	public void setSfyj(String sfyj) {
		this.sfyj = sfyj;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

}
