package com.sunshine.monitor.system.ws.ConnectionService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="CitySituationEntity")
@XmlRootElement(name="citySituationEntity")
public class CitySituationEntity implements Serializable {
	
	@XmlTransient
	private static final long serialVersionUID = 7626721340459398794L;
	
	private String dwdm;
	private String dwmc;
	private String jq;
	private String xt;
	private String jr;
	private String jqmc;
	private String xtmc;
	private String jrmc;
	private String gxsj;
	private String bz;
	public String getDwdm() {
		return dwdm;
	}
	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}
	public String getJq() {
		return jq;
	}
	public void setJq(String jq) {
		this.jq = jq;
	}
	public String getXt() {
		return xt;
	}
	public void setXt(String xt) {
		this.xt = xt;
	}
	public String getJr() {
		return jr;
	}
	public void setJr(String jr) {
		this.jr = jr;
	}
	public String getGxsj() {
		return gxsj;
	}
	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getJqmc() {
		return jqmc;
	}
	public void setJqmc(String jqmc) {
		this.jqmc = jqmc;
	}
	public String getXtmc() {
		return xtmc;
	}
	public void setXtmc(String xtmc) {
		this.xtmc = xtmc;
	}
	public String getJrmc() {
		return jrmc;
	}
	public void setJrmc(String jrmc) {
		this.jrmc = jrmc;
	}
	
	
}
