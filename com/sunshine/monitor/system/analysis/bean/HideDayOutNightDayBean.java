package com.sunshine.monitor.system.analysis.bean;

public class HideDayOutNightDayBean {
	private String hphm;
	private String hpzl;
	private String hpys;
	private String kssj;
	private String jssj;
	private String lfz;//累计次数
	private String cs;
	private String comple; // 次数比较运算符
	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public String getLfz() {
		return lfz;
	}
	public void setLfz(String lfz) {
		this.lfz = lfz;
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
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	public String getJssj() {
		return jssj;
	}
	public void setJssj(String jssj) {
		this.jssj = jssj;
	}
	public String getComple() {
		return comple;
	}
	public void setComple(String comple) {
		this.comple = comple;
	}
}
