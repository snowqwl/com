package com.sunshine.monitor.system.ws.ManagerService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DepartmentEntity")
@XmlRootElement(name = "departmentEntity")
public class DepartmentEntity implements Serializable {
    
	@XmlTransient
	private static final long serialVersionUID = 1L;
	
	private String glbm;
	private String bmmc;
	private String jb;
	private String sjbm;
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
	private String sfzs;
	public String getGlbm() {
		return glbm;
	}
	public void setGlbm(String glbm) {
		this.glbm = glbm;
	}
	public String getBmmc() {
		return bmmc;
	}
	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}
	public String getJb() {
		return jb;
	}
	public void setJb(String jb) {
		this.jb = jb;
	}
	public String getSjbm() {
		return sjbm;
	}
	public void setSjbm(String sjbm) {
		this.sjbm = sjbm;
	}
	public String getLxdz() {
		return lxdz;
	}
	public void setLxdz(String lxdz) {
		this.lxdz = lxdz;
	}
	public String getLxdh1() {
		return lxdh1;
	}
	public void setLxdh1(String lxdh1) {
		this.lxdh1 = lxdh1;
	}
	public String getLxdh2() {
		return lxdh2;
	}
	public void setLxdh2(String lxdh2) {
		this.lxdh2 = lxdh2;
	}
	public String getLxdh3() {
		return lxdh3;
	}
	public void setLxdh3(String lxdh3) {
		this.lxdh3 = lxdh3;
	}
	public String getLxdh4() {
		return lxdh4;
	}
	public void setLxdh4(String lxdh4) {
		this.lxdh4 = lxdh4;
	}
	public String getLxdh5() {
		return lxdh5;
	}
	public void setLxdh5(String lxdh5) {
		this.lxdh5 = lxdh5;
	}
	public String getLxcz() {
		return lxcz;
	}
	public void setLxcz(String lxcz) {
		this.lxcz = lxcz;
	}
	public String getYzbm() {
		return yzbm;
	}
	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}
	public String getLxr() {
		return lxr;
	}
	public void setLxr(String lxr) {
		this.lxr = lxr;
	}
	public String getJyrs() {
		return jyrs;
	}
	public void setJyrs(String jyrs) {
		this.jyrs = jyrs;
	}
	public String getXjrs() {
		return xjrs;
	}
	public void setXjrs(String xjrs) {
		this.xjrs = xjrs;
	}
	public String getSyjgdm() {
		return syjgdm;
	}
	public void setSyjgdm(String syjgdm) {
		this.syjgdm = syjgdm;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	public String getBmlx() {
		return bmlx;
	}
	public void setBmlx(String bmlx) {
		this.bmlx = bmlx;
	}
	public String getSflsjg() {
		return sflsjg;
	}
	public void setSflsjg(String sflsjg) {
		this.sflsjg = sflsjg;
	}
	public String getSsjz() {
		return ssjz;
	}
	public void setSsjz(String ssjz) {
		this.ssjz = ssjz;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getSfzs() {
		return sfzs;
	}
	public void setSfzs(String sfzs) {
		this.sfzs = sfzs;
	}
}
