package com.sunshine.monitor.system.ws.PassrecService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ContrailEntity")
@XmlRootElement(name="contrailEntity")
public class ContrailEntity implements Serializable {

	@XmlTransient
	private static final long serialVersionUID = 1L;
	
	private String gcsj;
	private String kdmc;
	private String 	kkjd;
	private String 	kkwd;
	private String gcxh;
	private String kdbh;
	
	public String getGcsj() {
		return gcsj;
	}
	public void setGcsj(String gcsj) {
		this.gcsj = gcsj;
	}
	public String getKdmc() {
		return kdmc;
	}
	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}
	public String getKkjd() {
		return kkjd;
	}
	public void setKkjd(String kkjd) {
		this.kkjd = kkjd;
	}
	public String getKkwd() {
		return kkwd;
	}
	public void setKkwd(String kkwd) {
		this.kkwd = kkwd;
	}
	public String getGcxh() {
		return gcxh;
	}
	public void setGcxh(String gcxh) {
		this.gcxh = gcxh;
	}
	public String getKdbh() {
		return kdbh;
	}
	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}
}
