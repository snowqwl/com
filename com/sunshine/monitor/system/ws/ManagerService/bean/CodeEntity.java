package com.sunshine.monitor.system.ws.ManagerService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodeEntity")
@XmlRootElement(name = "codeEntity")
public class CodeEntity implements Serializable {
    
    @XmlTransient
	private static final long serialVersionUID = 1L;
	
	private String dmlb;
	private String dmz;
	private String dmsm1;
	private String dmsm2;
	private String dmsm3;
	private String dmsm4;
	private String dmsx;
	private String zt;

	public String getDmlb() {
		return dmlb;
	}

	public void setDmlb(String dmlb) {
		this.dmlb = dmlb;
	}

	public String getDmz() {
		return dmz;
	}

	public void setDmz(String dmz) {
		this.dmz = dmz;
	}

	public String getDmsm1() {
		return dmsm1;
	}

	public void setDmsm1(String dmsm1) {
		this.dmsm1 = dmsm1;
	}

	public String getDmsm2() {
		return dmsm2;
	}

	public void setDmsm2(String dmsm2) {
		this.dmsm2 = dmsm2;
	}

	public String getDmsm3() {
		return dmsm3;
	}

	public void setDmsm3(String dmsm3) {
		this.dmsm3 = dmsm3;
	}

	public String getDmsm4() {
		return dmsm4;
	}

	public void setDmsm4(String dmsm4) {
		this.dmsm4 = dmsm4;
	}

	public String getDmsx() {
		return dmsx;
	}

	public void setDmsx(String dmsx) {
		this.dmsx = dmsx;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

}
