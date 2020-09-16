package com.sunshine.monitor.system.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
/**
 * JAXB: Java class transform to XML is Marshal
 * on contrary XML transform to Java is unMarshal
 * Query conditions packaged
 * @author OUYANG 2013/8/6
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="QueryVehPassrecEntity")
@XmlRootElement(name="queryVehPassrecEntity")
public class QueryVehPassrecConditions implements Serializable{
	/**
	 * Ignore field
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;
	
	private String kdbh;
	
	private String fxbh;
	
	private String hpzl;
	
	private String hphm;
	
	//@XmlElement(required = true)
	private String kssj;
	
	//@XmlElement(required = true)
	private String jssj;

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
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
}