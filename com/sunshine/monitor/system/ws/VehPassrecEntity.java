package com.sunshine.monitor.system.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
/**
 * JAXB: Java class transform to XML is Marshal
 * on contrary XML transform to Java is unMarshal
 * @author OUYANG 2013/8/6
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="VehPassrec")
@XmlRootElement(name="vehPassrec")
public class VehPassrecEntity implements Serializable{
	/**
	 * Ignore field
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	private String gcxh;
	
	private String sbbh;
	
	private String kdbh;
	
	private String fxbh;
	
	private String hpzl;
	
	private String hphm;
	
	private String gcsj;
	
	private Long clsd;
	
	private String hpys;
	
	private String cllx;
	
	private String tp1;
	
	private String tp2;
	
	private String tp3;
	
	private String wfbj;
	
	private String by1;
	
	private String rksj;
	
	private Long clxs;
	
	private String cwhphm;
	
	private String cwhpys;
	
	private String hpyz;
	
	private String xszt;
	
	private String byzd;
	
	private String clpp;
	
	private String clwx;
	
	private String csys;
	
	private String cdbh;
	
	public String kssj;
	
	public String jssj;
	
	public String kssd;
	
	public String jssd;
	
	public String glbm;
	
	public String hpzlmc;
	
	public String sbmc;
	
	public String kdmc;
	
	public String fxmc;
	
	public String hpysmc;
	
	public String cllxmc;
	
	public String wfbjmc;
	
	public String cwhpysmc;
	
	public String hpyzmc;
	
	public String csysmc;
	
	public String xsztmc;
		
	public String wpc;
	
	public String city;
	
	public String datasource;
	
	public String ll;
	
	public String getGcxh() {
		return this.gcxh;
	}

	public void setGcxh(String gcxh) {
		this.gcxh = gcxh;
	}

	public String getSbbh() {
		return this.sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getKdbh() {
		return this.kdbh;
	}

	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}

	public String getFxbh() {
		return this.fxbh;
	}

	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}

	public String getHpzl() {
		return this.hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getHphm() {
		return this.hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getGcsj() {
		return this.gcsj;
	}

	public void setGcsj(String gcsj) {
		this.gcsj = gcsj;
	}

	public Long getClsd() {
		return this.clsd;
	}

	public void setClsd(Long clsd) {
		this.clsd = clsd;
	}

	public String getHpys() {
		return this.hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getCllx() {
		return this.cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getTp1() {
		return this.tp1;
	}

	public void setTp1(String tp1) {
		this.tp1 = tp1;
	}

	public String getTp2() {
		return this.tp2;
	}

	public void setTp2(String tp2) {
		this.tp2 = tp2;
	}

	public String getTp3() {
		return this.tp3;
	}

	public void setTp3(String tp3) {
		this.tp3 = tp3;
	}

	public String getWfbj() {
		return this.wfbj;
	}

	public void setWfbj(String wfbj) {
		this.wfbj = wfbj;
	}

	public String getBy1() {
		return this.by1;
	}

	public void setBy1(String by1) {
		this.by1 = by1;
	}

	public String getRksj() {
		return this.rksj;
	}

	public void setRksj(String rksj) {
		this.rksj = rksj;
	}

	public Long getClxs() {
		return this.clxs;
	}

	public void setClxs(Long clxs) {
		this.clxs = clxs;
	}

	public String getCwhphm() {
		return this.cwhphm;
	}

	public void setCwhphm(String cwhphm) {
		this.cwhphm = cwhphm;
	}

	public String getCwhpys() {
		return this.cwhpys;
	}

	public void setCwhpys(String cwhpys) {
		this.cwhpys = cwhpys;
	}

	public String getHpyz() {
		return this.hpyz;
	}

	public void setHpyz(String hpyz) {
		this.hpyz = hpyz;
	}

	public String getXszt() {
		return this.xszt;
	}

	public void setXszt(String xszt) {
		this.xszt = xszt;
	}

	public String getByzd() {
		return this.byzd;
	}

	public void setByzd(String byzd) {
		this.byzd = byzd;
	}

	public String getClpp() {
		return this.clpp;
	}

	public void setClpp(String clpp) {
		this.clpp = clpp;
	}

	public String getClwx() {
		return this.clwx;
	}

	public void setClwx(String clwx) {
		this.clwx = clwx;
	}

	public String getCsys() {
		return this.csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getCdbh() {
		return this.cdbh;
	}

	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
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

	public String getKssd() {
		return kssd;
	}

	public void setKssd(String kssd) {
		this.kssd = kssd;
	}

	public String getJssd() {
		return jssd;
	}

	public void setJssd(String jssd) {
		this.jssd = jssd;
	}

	public String getGlbm() {
		return glbm;
	}

	public void setGlbm(String glbm) {
		this.glbm = glbm;
	}

	public String getHpzlmc() {
		return hpzlmc;
	}

	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getKdmc() {
		return kdmc;
	}

	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}

	public String getFxmc() {
		return fxmc;
	}

	public void setFxmc(String fxmc) {
		this.fxmc = fxmc;
	}

	public String getHpysmc() {
		return hpysmc;
	}

	public void setHpysmc(String hpysmc) {
		this.hpysmc = hpysmc;
	}

	public String getCllxmc() {
		return cllxmc;
	}

	public void setCllxmc(String cllxmc) {
		this.cllxmc = cllxmc;
	}

	public String getWfbjmc() {
		return wfbjmc;
	}

	public void setWfbjmc(String wfbjmc) {
		this.wfbjmc = wfbjmc;
	}

	public String getCwhpysmc() {
		return cwhpysmc;
	}

	public void setCwhpysmc(String cwhpysmc) {
		this.cwhpysmc = cwhpysmc;
	}

	public String getHpyzmc() {
		return hpyzmc;
	}

	public void setHpyzmc(String hpyzmc) {
		this.hpyzmc = hpyzmc;
	}

	public String getCsysmc() {
		return csysmc;
	}

	public void setCsysmc(String csysmc) {
		this.csysmc = csysmc;
	}

	public String getXsztmc() {
		return xsztmc;
	}

	public void setXsztmc(String xsztmc) {
		this.xsztmc = xsztmc;
	}

	public String getWpc() {
		return wpc;
	}

	public void setWpc(String wpc) {
		this.wpc = wpc;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getLl() {
		return ll;
	}

	public void setLl(String ll) {
		this.ll = ll;
	}
}