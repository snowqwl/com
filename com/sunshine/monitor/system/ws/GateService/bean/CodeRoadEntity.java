package com.sunshine.monitor.system.ws.GateService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodeRoadEntity")
@XmlRootElement(name = "codeRoadEntity")
public class CodeRoadEntity implements Serializable {
	
	@XmlTransient
	private static final long serialVersionUID = 1L;
	
	private String kdbh;
	private String fxbh;
	private String cdbh;
	private String cdlx;
	private String cdlxmc;
	private Double xcxs;
	private Double dcxs;
	private Double zdxs;
	private String bzsm;	
	private String ip;
	public String getKdbh() {
		return kdbh;
	}
	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}
	public String getFxbh() {
		return fxbh;
	}
	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}
	public String getCdbh() {
		return cdbh;
	}
	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
	}
	public String getCdlx() {
		return cdlx;
	}
	public void setCdlx(String cdlx) {
		this.cdlx = cdlx;
	}
	public Double getXcxs() {
		return xcxs;
	}
	public void setXcxs(Double xcxs) {
		this.xcxs = xcxs;
	}
	public Double getDcxs() {
		return dcxs;
	}
	public void setDcxs(Double dcxs) {
		this.dcxs = dcxs;
	}
	public Double getZdxs() {
		return zdxs;
	}
	public void setZdxs(Double zdxs) {
		this.zdxs = zdxs;
	}
	public String getBzsm() {
		return bzsm;
	}
	public void setBzsm(String bzsm) {
		this.bzsm = bzsm;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCdlxmc() {
		return cdlxmc;
	}
	public void setCdlxmc(String cdlxmc) {
		this.cdlxmc = cdlxmc;
	}

}
