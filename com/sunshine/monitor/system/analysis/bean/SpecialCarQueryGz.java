package com.sunshine.monitor.system.analysis.bean;

public class SpecialCarQueryGz {	
	private String gzbh; //规则编号
	private String bdlx; // 比对类型
	private String hphm; // 比对号牌号码
	private String gzkssj ; //比对规则开始时间
	private String gzjssj; //比对规则结束时间
	private String city ; //比对地段所在地市
	private String kdbhstr; //比对地段卡口（字符串）
	private String kdmcstr ; //比对地段卡口名称（字符串）
	private String zt ; //比对规则当前状态（0:启用，1：停用）
	public String getGzbh() {
		return gzbh;
	}
	public void setGzbh(String gzbh) {
		this.gzbh = gzbh;
	}
	public String getBdlx() {
		return bdlx;
	}
	public void setBdlx(String bdlx) {
		this.bdlx = bdlx;
	}
	public String getHphm() {
		return hphm;
	}
	public void setHphm(String hphm) {
		this.hphm = hphm;
	}
	public String getGzkssj() {
		return gzkssj;
	}
	public void setGzkssj(String gzkssj) {
		this.gzkssj = gzkssj;
	}
	public String getGzjssj() {
		return gzjssj;
	}
	public void setGzjssj(String gzjssj) {
		this.gzjssj = gzjssj;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getKdbhstr() {
		return kdbhstr;
	}
	public void setKdbhstr(String kdbhstr) {
		this.kdbhstr = kdbhstr;
	}
	public String getKdmcstr() {
		return kdmcstr;
	}
	public void setKdmcstr(String kdmcstr) {
		this.kdmcstr = kdmcstr;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
}
