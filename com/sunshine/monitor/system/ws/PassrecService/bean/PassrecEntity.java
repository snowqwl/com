package com.sunshine.monitor.system.ws.PassrecService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PassEntity")
@XmlRootElement(name = "PassEntity")
public class PassrecEntity implements Serializable {

	/**
	 * Ignore field
	 */
	@XmlTransient
	private static final long serialVersionUID = -7708365686436407744L;
	
	private String gcxh;
	private String kdbh;
	private String kdmc;
	private String fxbh;
	private String fxmc;
	private String cdbh;
	private String cdlx;
	private String cdlxmc;
	private String hpzl;
	private String hpzlmc;
	private String gcsj;
	private String kssj;
	private String jssj;
	private Long clsd;
	private String hpys;
	private String hpysmc;
	private String cllx;
	private String cllxmc;
	private String hphm;
	private String cwhphm;
	private String cwhpys;
	private String cwhpysmc;
	private String hpyz;
	private String hpyzmc;
	private String csys;
	private String csysmc;
	private String clxs;
	private String clpp;
	private String clwx;
	private String xszt;
	private String xsztmc;
	private String wfbj;
	private String tp1;
	private String tp2;
	private String tp3;
	private String tztp;
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
	public String getKdmc() {
		return kdmc;
	}
	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}
	public String getFxbh() {
		return fxbh;
	}
	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}
	public String getFxmc() {
		return fxmc;
	}
	public void setFxmc(String fxmc) {
		this.fxmc = fxmc;
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
	public String getCdlxmc() {
		return cdlxmc;
	}
	public void setCdlxmc(String cdlxmc) {
		this.cdlxmc = cdlxmc;
	}
	public String getHpzl() {
		return hpzl;
	}
	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}
	public String getHpzlmc() {
		return hpzlmc;
	}
	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}
	public String getGcsj() {
		return gcsj;
	}
	public void setGcsj(String gcsj) {
		this.gcsj = gcsj;
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
	public Long getClsd() {
		return clsd;
	}
	public void setClsd(Long clsd) {
		this.clsd = clsd;
	}
	public String getHpys() {
		return hpys;
	}
	public void setHpys(String hpys) {
		this.hpys = hpys;
	}
	public String getHpysmc() {
		return hpysmc;
	}
	public void setHpysmc(String hpysmc) {
		this.hpysmc = hpysmc;
	}
	public String getCllx() {
		return cllx;
	}
	public void setCllx(String cllx) {
		this.cllx = cllx;
	}
	public String getCllxmc() {
		return cllxmc;
	}
	public void setCllxmc(String cllxmc) {
		this.cllxmc = cllxmc;
	}
	public String getHphm() {
		return hphm;
	}
	public void setHphm(String hphm) {
		this.hphm = hphm;
	}
	public String getCwhphm() {
		return cwhphm;
	}
	public void setCwhphm(String cwhphm) {
		this.cwhphm = cwhphm;
	}
	public String getCwhpys() {
		return cwhpys;
	}
	public void setCwhpys(String cwhpys) {
		this.cwhpys = cwhpys;
	}
	public String getCwhpysmc() {
		return cwhpysmc;
	}
	public void setCwhpysmc(String cwhpysmc) {
		this.cwhpysmc = cwhpysmc;
	}
	public String getHpyz() {
		return hpyz;
	}
	public void setHpyz(String hpyz) {
		this.hpyz = hpyz;
	}
	public String getHpyzmc() {
		return hpyzmc;
	}
	public void setHpyzmc(String hpyzmc) {
		this.hpyzmc = hpyzmc;
	}
	public String getCsys() {
		return csys;
	}
	public void setCsys(String csys) {
		this.csys = csys;
	}
	public String getCsysmc() {
		return csysmc;
	}
	public void setCsysmc(String csysmc) {
		this.csysmc = csysmc;
	}
	public String getClxs() {
		return clxs;
	}
	public void setClxs(String clxs) {
		this.clxs = clxs;
	}
	public String getClpp() {
		return clpp;
	}
	public void setClpp(String clpp) {
		this.clpp = clpp;
	}
	public String getClwx() {
		return clwx;
	}
	public void setClwx(String clwx) {
		this.clwx = clwx;
	}
	public String getXszt() {
		return xszt;
	}
	public void setXszt(String xszt) {
		this.xszt = xszt;
	}
	public String getXsztmc() {
		return xsztmc;
	}
	public void setXsztmc(String xsztmc) {
		this.xsztmc = xsztmc;
	}
	public String getWfbj() {
		return wfbj;
	}
	public void setWfbj(String wfbj) {
		this.wfbj = wfbj;
	}
	public String getTp1() {
		return tp1;
	}
	public void setTp1(String tp1) {
		this.tp1 = tp1;
	}
	public String getTp2() {
		return tp2;
	}
	public void setTp2(String tp2) {
		this.tp2 = tp2;
	}
	public String getTp3() {
		return tp3;
	}
	public void setTp3(String tp3) {
		this.tp3 = tp3;
	}
	public String getTztp() {
		return tztp;
	}
	public void setTztp(String tztp) {
		this.tztp = tztp;
	}
	
}
