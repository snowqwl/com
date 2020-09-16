package com.sunshine.monitor.system.manager.bean;

import java.util.List;

import com.sunshine.monitor.comm.bean.Entity;

public class Department extends Entity {

	private static final long serialVersionUID = 1L;
	private String glbm;
	private String bmmc;
	private String jb;
	private String sjbm;
	private String yzmc;
	private String lxdz;
	private String lxdh1;
	private String lxdh2;
	private String lxdh3;
	private String lxdh4;
	private String lxdh5;
	private String lxcz;
	private String yzbm;
	private String lxr;
	private String jyrs;
	private String xjrs;
	private String syjgdm;
	private String py;
	private String bmlx;
	private String sflsjg;
	private String ssjz;
	private String zt;
	private String gxsj;
	private String bz;
	public String jbmc;
	public String ztmc;
	public String agent;
	public List prefecture;
	public String key;
	public String sfzs;
	public String sjbmmc;

	public String getSfzs() {
		return sfzs;
	}

	public void setSfzs(String sfzs) {
		this.sfzs = sfzs;
	}

	public String getGlbm() {
		return this.glbm;
	}

	public void setGlbm(String glbm) {
		this.glbm = glbm;
	}

	public String getBmmc() {
		return this.bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getJb() {
		return this.jb;
	}

	public void setJb(String jb) {
		this.jb = jb;
	}

	public String getSjbm() {
		return this.sjbm;
	}

	public void setSjbm(String sjbm) {
		this.sjbm = sjbm;
	}

	public String getYzmc() {
		return this.yzmc;
	}

	public void setYzmc(String yzmc) {
		this.yzmc = yzmc;
	}

	public String getLxdz() {
		return this.lxdz;
	}

	public void setLxdz(String lxdz) {
		this.lxdz = lxdz;
	}

	public String getLxdh1() {
		return this.lxdh1;
	}

	public void setLxdh1(String lxdh1) {
		this.lxdh1 = lxdh1;
	}

	public String getLxdh2() {
		return this.lxdh2;
	}

	public void setLxdh2(String lxdh2) {
		this.lxdh2 = lxdh2;
	}

	public String getLxdh3() {
		return this.lxdh3;
	}

	public void setLxdh3(String lxdh3) {
		this.lxdh3 = lxdh3;
	}

	public String getLxdh4() {
		return this.lxdh4;
	}

	public void setLxdh4(String lxdh4) {
		this.lxdh4 = lxdh4;
	}

	public String getLxdh5() {
		return this.lxdh5;
	}

	public void setLxdh5(String lxdh5) {
		this.lxdh5 = lxdh5;
	}

	public String getLxcz() {
		return this.lxcz;
	}

	public void setLxcz(String lxcz) {
		this.lxcz = lxcz;
	}

	public String getYzbm() {
		return this.yzbm;
	}

	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}

	public String getLxr() {
		return this.lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getJyrs() {
		return this.jyrs;
	}

	public void setJyrs(String jyrs) {
		this.jyrs = jyrs;
	}

	public String getXjrs() {
		return this.xjrs;
	}

	public void setXjrs(String xjrs) {
		this.xjrs = xjrs;
	}

	public String getSyjgdm() {
		return this.syjgdm;
	}

	public void setSyjgdm(String syjgdm) {
		this.syjgdm = syjgdm;
	}

	public String getPy() {
		return this.py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getBmlx() {
		return this.bmlx;
	}

	public void setBmlx(String bmlx) {
		this.bmlx = bmlx;
	}

	public String getSflsjg() {
		return this.sflsjg;
	}

	public void setSflsjg(String sflsjg) {
		this.sflsjg = sflsjg;
	}

	public String getSsjz() {
		return this.ssjz;
	}

	public void setSsjz(String ssjz) {
		this.ssjz = ssjz;
	}

	public String getZt() {
		return this.zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getGxsj() {
		return this.gxsj;
	}

	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getJbmc() {
		return this.jbmc;
	}

	public void setJbmc(String jbmc) {
		this.jbmc = jbmc;
	}

	public String getZtmc() {
		return this.ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String glbms, String bmlxs) {
		if (bmlxs.equals("1"))
			this.agent = (glbms.substring(0, 6) + "000000");
		else
			this.agent = glbms;
	}

	public List getPrefecture() {
		return this.prefecture;
	}

	public void setPrefecture(List prefecture) {
		this.prefecture = prefecture;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSjbmmc() {
		return sjbmmc;
	}

	public void setSjbmmc(String sjbmmc) {
		this.sjbmmc = sjbmmc;
	}

	public String getSQL() {
		return "select xjjg from frm_prefecture where dwdm='" + this.agent
				+ "'";
	}

	public String getSQL(boolean all) {
		return "select * from frm_prefecture where dwdm='" + this.agent + "'";
	}

	public String getSQL(String jgqz) {
		return "select xjjg from frm_prefecture where dwdm='" + this.agent
				+ "' and jgqz lke '%" + jgqz + "%'";
	}

	public String getSQL(boolean all, String jgqz) {
		return "select * from frm_prefecture where dwdm='" + this.agent
				+ "' and jgqz lke '%" + jgqz + "%'";
	}

	/**
	public int getDepLevel() {
		char[] dep1 = this.glbm.toCharArray();
		int level = 0;
		for (int i = 0; i < dep1.length; i += 2) {
			if ((dep1[i] != '0') || (dep1[(i + 1)] != '0')) {
				dep1[i] = '1';
				dep1[(i + 1)] = '1';
			}
		}
		for (int i = 0; i < dep1.length; i++) {
			if (dep1[i] == '0') {
				level++;
			}
		}
		return (12 - level) / 2;
	}
	*/

}
