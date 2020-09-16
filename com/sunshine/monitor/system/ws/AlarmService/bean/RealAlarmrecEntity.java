package com.sunshine.monitor.system.ws.AlarmService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RealAlarmrecEntity")
@XmlRootElement(name = "realAlarmrecEntity")
public class RealAlarmrecEntity implements Serializable {

	@XmlTransient
	private static final long serialVersionUID = 7626721340459398794L;


	private String sbbh;
	
	private String bjxh;
	
	private String kdbh;
	
	private String kdmc;
	
    private String fxbh;
	
    private String fxmc;
	
    private String cllx;
    
	private String cllxmc;
	
	private Long clsd;

	private String hpys;
	
	private String hpysmc;
	
	private String tp1;
	
	private String tp2;
	
	private String tp3;
	
	private String bjlx;
	
	private String bjlxmc;
	
	private String bjsj;
	
	private String hphm;
	
	private String hpzl;
	
	private String hpzlmc;
	
	private String csys;
	
	private String csysmc;
	
	private String bkxh;
	
	private String bkjb;
	
	private String bkjg;
	
	private String cdbh;
	
	private String bjdwdm;
	
	private String bjdwdmmc;

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getBjxh() {
		return bjxh;
	}

	public void setBjxh(String bjxh) {
		this.bjxh = bjxh;
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

	public String getBjlx() {
		return bjlx;
	}

	public void setBjlx(String bjlx) {
		this.bjlx = bjlx;
	}

	public String getBjlxmc() {
		return bjlxmc;
	}

	public void setBjlxmc(String bjlxmc) {
		this.bjlxmc = bjlxmc;
	}

	public String getBjsj() {
		return bjsj;
	}

	public void setBjsj(String bjsj) {
		this.bjsj = bjsj;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
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

	public String getBkxh() {
		return bkxh;
	}

	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}

	public String getBkjb() {
		return bkjb;
	}

	public void setBkjb(String bkjb) {
		this.bkjb = bkjb;
	}

	public String getBkjg() {
		return bkjg;
	}

	public void setBkjg(String bkjg) {
		this.bkjg = bkjg;
	}

	public String getCdbh() {
		return cdbh;
	}

	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
	}

	public String getBjdwdm() {
		return bjdwdm;
	}

	public void setBjdwdm(String bjdwdm) {
		this.bjdwdm = bjdwdm;
	}

	public String getBjdwdmmc() {
		return bjdwdmmc;
	}

	public void setBjdwdmmc(String bjdwdmmc) {
		this.bjdwdmmc = bjdwdmmc;
	}

}
