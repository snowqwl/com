package com.sunshine.monitor.system.gate.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class CodeGateExtend extends Entity {

	private static final long serialVersionUID = -8687543492708780710L;

	private String kdbh;
	private String fxbh;
	private String fxlx;
	private String fxmc;
	private String lrsj;	
	private String zrdw;
	private String ljdw;	
	private String bz1;	
	private String bz2;
	private String sbzt;	
	private String sblx;
	private String jyh;
	private String sjhm;
    private String ywdw;
	private String ywzrr;
	private String ywsjhm;	
	private String zrdwlxr;
	private String ljdwlxr;
    private String zrdwlxdh;	
	private String ljdwlxdh;
	private Double cdzs;
	private String gxsj;
	private Double csfz;
	//车道信息
	private String delRoads;
	private String roads;
    
	//部门信息(涉案，交通)
	private String zrdw_sa;
	private String zrdw_jt;
	private String ljdw_sa;
	private String ljdw_jt;
	private String ywdwmc;
	private String zrdwmc_sa;
	private String zrdwmc_jt;
	private String ljdwmc_sa;
	private String ljdwmc_jt;
	private String zrdwlxr_sa;
	private String zrdwlxr_jt;
	private String ljdwlxr_sa;
	private String ljdwlxr_jt;
	private String zrdwlxdh_sa;
	private String zrdwlxdh_jt;
	private String ljdwlxdh_sa;
	private String ljdwlxdh_jt;
	
	public String getKdbh() {
		return kdbh;
	}
	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}
	public String getLrsj() {
		return lrsj;
	}
	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}
	public String getZrdw() {
		return zrdw;
	}
	public void setZrdw(String zrdw) {
		this.zrdw = zrdw;
	}
	public String getLjdw() {
		return ljdw;
	}
	public void setLjdw(String ljdw) {
		this.ljdw = ljdw;
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
	public String getSbzt() {
		return sbzt;
	}
	public void setSbzt(String sbzt) {
		this.sbzt = sbzt;
	}
	public String getSblx() {
		return sblx;
	}
	public void setSblx(String sblx) {
		this.sblx = sblx;
	}
	public String getJyh() {
		return jyh;
	}
	public void setJyh(String jyh) {
		this.jyh = jyh;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public String getYwdw() {
		return ywdw;
	}
	public void setYwdw(String ywdw) {
		this.ywdw = ywdw;
	}
	public String getYwzrr() {
		return ywzrr;
	}
	public void setYwzrr(String ywzrr) {
		this.ywzrr = ywzrr;
	}
	public String getYwsjhm() {
		return ywsjhm;
	}
	public void setYwsjhm(String ywsjhm) {
		this.ywsjhm = ywsjhm;
	}
	public String getZrdwlxr() {
		return zrdwlxr;
	}
	public void setZrdwlxr(String zrdwlxr) {
		this.zrdwlxr = zrdwlxr;
	}
	public String getLjdwlxr() {
		return ljdwlxr;
	}
	public void setLjdwlxr(String ljdwlxr) {
		this.ljdwlxr = ljdwlxr;
	}
	public String getZrdwlxdh() {
		return zrdwlxdh;
	}
	public void setZrdwlxdh(String zrdwlxdh) {
		this.zrdwlxdh = zrdwlxdh;
	}
	public String getLjdwlxdh() {
		return ljdwlxdh;
	}
	public void setLjdwlxdh(String ljdwlxdh) {
		this.ljdwlxdh = ljdwlxdh;
	}
	public Double getCdzs() {
		return cdzs;
	}
	public void setCdzs(Double cdzs) {
		this.cdzs = cdzs;
	}
	public String getYwdwmc() {
		return ywdwmc;
	}
	public void setYwdwmc(String ywdwmc) {
		this.ywdwmc = ywdwmc;
	}
	public String getZrdw_sa() {
		return zrdw_sa;
	}
	public void setZrdw_sa(String zrdwSa) {
		zrdw_sa = zrdwSa;
	}
	public String getZrdw_jt() {
		return zrdw_jt;
	}
	public void setZrdw_jt(String zrdwJt) {
		zrdw_jt = zrdwJt;
	}
	public String getLjdw_sa() {
		return ljdw_sa;
	}
	public void setLjdw_sa(String ljdwSa) {
		ljdw_sa = ljdwSa;
	}
	public String getLjdw_jt() {
		return ljdw_jt;
	}
	public void setLjdw_jt(String ljdwJt) {
		ljdw_jt = ljdwJt;
	}
	public String getZrdwmc_sa() {
		return zrdwmc_sa;
	}
	public void setZrdwmc_sa(String zrdwmcSa) {
		zrdwmc_sa = zrdwmcSa;
	}
	public String getZrdwmc_jt() {
		return zrdwmc_jt;
	}
	public void setZrdwmc_jt(String zrdwmcJt) {
		zrdwmc_jt = zrdwmcJt;
	}
	public String getLjdwmc_sa() {
		return ljdwmc_sa;
	}
	public void setLjdwmc_sa(String ljdwmcSa) {
		ljdwmc_sa = ljdwmcSa;
	}
	public String getLjdwmc_jt() {
		return ljdwmc_jt;
	}
	public void setLjdwmc_jt(String ljdwmcJt) {
		ljdwmc_jt = ljdwmcJt;
	}
	public String getZrdwlxr_sa() {
		return zrdwlxr_sa;
	}
	public void setZrdwlxr_sa(String zrdwlxrSa) {
		zrdwlxr_sa = zrdwlxrSa;
	}
	public String getZrdwlxr_jt() {
		return zrdwlxr_jt;
	}
	public void setZrdwlxr_jt(String zrdwlxrJt) {
		zrdwlxr_jt = zrdwlxrJt;
	}
	public String getLjdwlxr_sa() {
		return ljdwlxr_sa;
	}
	public void setLjdwlxr_sa(String ljdwlxrSa) {
		ljdwlxr_sa = ljdwlxrSa;
	}
	public String getLjdwlxr_jt() {
		return ljdwlxr_jt;
	}
	public void setLjdwlxr_jt(String ljdwlxrJt) {
		ljdwlxr_jt = ljdwlxrJt;
	}
	public String getZrdwlxdh_sa() {
		return zrdwlxdh_sa;
	}
	public void setZrdwlxdh_sa(String zrdwlxdhSa) {
		zrdwlxdh_sa = zrdwlxdhSa;
	}
	public String getZrdwlxdh_jt() {
		return zrdwlxdh_jt;
	}
	public void setZrdwlxdh_jt(String zrdwlxdhJt) {
		zrdwlxdh_jt = zrdwlxdhJt;
	}
	public String getLjdwlxdh_sa() {
		return ljdwlxdh_sa;
	}
	public void setLjdwlxdh_sa(String ljdwlxdhSa) {
		ljdwlxdh_sa = ljdwlxdhSa;
	}
	public String getLjdwlxdh_jt() {
		return ljdwlxdh_jt;
	}
	public void setLjdwlxdh_jt(String ljdwlxdhJt) {
		ljdwlxdh_jt = ljdwlxdhJt;
	}
	public String getFxlx() {
		return fxlx;
	}
	public void setFxlx(String fxlx) {
		this.fxlx = fxlx;
	}
	public String getFxmc() {
		return fxmc;
	}
	public void setFxmc(String fxmc) {
		this.fxmc = fxmc;
	}

	public String getDelRoads() {
		return delRoads;
	}
	public void setDelRoads(String delRoads) {
		this.delRoads = delRoads;
	}
	public String getRoads() {
		return roads;
	}
	public void setRoads(String roads) {
		this.roads = roads;
	}
	public Double getCsfz() {
		return csfz;
	}
	public void setCsfz(Double csfz) {
		this.csfz = csfz;
	}
	public String getFxbh() {
		return fxbh;
	}
	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}
	public String getGxsj() {
		return gxsj;
	}
	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}
	
	
}
