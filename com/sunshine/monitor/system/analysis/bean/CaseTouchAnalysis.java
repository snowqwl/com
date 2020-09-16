package com.sunshine.monitor.system.analysis.bean;

public class CaseTouchAnalysis {
	private String gzbh;	///规则编号
	private String gzmc;	///规则名称（翻译所得）
	private String dpgz;	///(0:区域;1:案件)
	private String dpgzmc;	///(对应翻译如：同案件;同区域;同区域,同案件)
	private String pl ; 	///频率
	private String gxsj ; 	///规则更新时间
	public String getGzbh() {
		return gzbh;
	}
	public void setGzbh(String gzbh) {
		this.gzbh = gzbh;
	}
	public String getGzmc() {
		return gzmc;
	}
	public void setGzmc(String gzmc) {
		this.gzmc = gzmc;
	}
	public String getDpgz() {
		return dpgz;
	}
	public void setDpgz(String dpgz) {
		this.dpgz = dpgz;
	}
	public String getDpgzmc() {
		return dpgzmc;
	}
	public void setDpgzmc(String dpgzmc) {
		this.dpgzmc = dpgzmc;
	}
	public String getPl() {
		return pl;
	}
	public void setPl(String pl) {
		this.pl = pl;
	}
	public String getGxsj() {
		return gxsj;
	}
	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}
	
	
}
