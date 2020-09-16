package com.sunshine.monitor.system.ws.AlarmService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VehAlarmrecEntity")
@XmlRootElement(name = "vehAlarmrecEntity")
public class VehAlarmrecEntity implements Serializable {

	@XmlTransient
	private static final long serialVersionUID = -8231726939499334806L;

	private String bjxh;
	
	private String bjdl;
	
	private String bjdlmc;
	
	private String bjlx;
	
	private String bjlxmc;
	
	private String bkxh;
	
	private String bjsj;
	
	private String bjdwdm;
	
	private String bjdwmc;
	
	private String bjdwlxdh;
	
	private String hphm;
	
	private String hpzl;
	
	private String hpzlmc;
	
	private String gcxh;
	
	private String gcsj;
	
	private String sbbh;
	
	private String sbmc;
	
	private String kdbh;
	
	private String kdmc;
	
	private String fxbh;
	
	private String fxmc;
	
	private String cllx;
	
	private String cllxmc;
	
	private Long clsd;
	
	private String hpys;
	
	private String hpysmc;
	
	private String cwhphm;
	
	private String cwhpys;
	
	private String cwhpysmc;
	
	private String hpyz;
	
	private String hpyzmc;
	
	private String cdbh;
	
	private String clwx;
	
	private String csys;
	
	private String csysmc;
	
	private String tp1;
	
	private String tp2;
	
	private String tp3;
	
	private String qrr;
	
	private String qrrjh;
	
	private String qrrmc;
	
	private String qrdwdm;
	
	private String qrdwdmmc;
	
	private String qrdwlxdh;
	
	private String qrsj;
	
	private String qrzt;
	
	private String qrztmc;
	
	private String qrjg;
	
	private String jyljtj;
	
	private String xxly;
	
	private String sfxdzl;
	
	private String sffk;
	
	private String sflj;

	public String getBjxh() {
		return bjxh;
	}

	public void setBjxh(String bjxh) {
		this.bjxh = bjxh;
	}

	public String getBjdl() {
		return bjdl;
	}

	public void setBjdl(String bjdl) {
		this.bjdl = bjdl;
	}

	public String getBjdlmc() {
		return bjdlmc;
	}

	public void setBjdlmc(String bjdlmc) {
		this.bjdlmc = bjdlmc;
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

	public String getBkxh() {
		return bkxh;
	}

	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}

	public String getBjsj() {
		return bjsj;
	}

	public void setBjsj(String bjsj) {
		this.bjsj = bjsj;
	}

	public String getBjdwdm() {
		return bjdwdm;
	}

	public void setBjdwdm(String bjdwdm) {
		this.bjdwdm = bjdwdm;
	}

	public String getBjdwmc() {
		return bjdwmc;
	}

	public void setBjdwmc(String bjdwmc) {
		this.bjdwmc = bjdwmc;
	}

	public String getBjdwlxdh() {
		return bjdwlxdh;
	}

	public void setBjdwlxdh(String bjdwlxdh) {
		this.bjdwlxdh = bjdwlxdh;
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

	public String getGcxh() {
		return gcxh;
	}

	public void setGcxh(String gcxh) {
		this.gcxh = gcxh;
	}

	public String getGcsj() {
		return gcsj;
	}

	public void setGcsj(String gcsj) {
		this.gcsj = gcsj;
	}

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
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

	public String getCdbh() {
		return cdbh;
	}

	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
	}

	public String getClwx() {
		return clwx;
	}

	public void setClwx(String clwx) {
		this.clwx = clwx;
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

	public String getQrr() {
		return qrr;
	}

	public void setQrr(String qrr) {
		this.qrr = qrr;
	}

	public String getQrrjh() {
		return qrrjh;
	}

	public void setQrrjh(String qrrjh) {
		this.qrrjh = qrrjh;
	}

	public String getQrrmc() {
		return qrrmc;
	}

	public void setQrrmc(String qrrmc) {
		this.qrrmc = qrrmc;
	}

	public String getQrdwdm() {
		return qrdwdm;
	}

	public void setQrdwdm(String qrdwdm) {
		this.qrdwdm = qrdwdm;
	}

	public String getQrdwdmmc() {
		return qrdwdmmc;
	}

	public void setQrdwdmmc(String qrdwdmmc) {
		this.qrdwdmmc = qrdwdmmc;
	}

	public String getQrdwlxdh() {
		return qrdwlxdh;
	}

	public void setQrdwlxdh(String qrdwlxdh) {
		this.qrdwlxdh = qrdwlxdh;
	}

	public String getQrsj() {
		return qrsj;
	}

	public void setQrsj(String qrsj) {
		this.qrsj = qrsj;
	}

	public String getQrzt() {
		return qrzt;
	}

	public void setQrzt(String qrzt) {
		this.qrzt = qrzt;
	}

	public String getQrztmc() {
		return qrztmc;
	}

	public void setQrztmc(String qrztmc) {
		this.qrztmc = qrztmc;
	}

	public String getQrjg() {
		return qrjg;
	}

	public void setQrjg(String qrjg) {
		this.qrjg = qrjg;
	}

	public String getJyljtj() {
		return jyljtj;
	}

	public void setJyljtj(String jyljtj) {
		this.jyljtj = jyljtj;
	}

	public String getXxly() {
		return xxly;
	}

	public void setXxly(String xxly) {
		this.xxly = xxly;
	}

	public String getSfxdzl() {
		return sfxdzl;
	}

	public void setSfxdzl(String sfxdzl) {
		this.sfxdzl = sfxdzl;
	}

	public String getSffk() {
		return sffk;
	}

	public void setSffk(String sffk) {
		this.sffk = sffk;
	}

	public String getSflj() {
		return sflj;
	}

	public void setSflj(String sflj) {
		this.sflj = sflj;
	}
	
}
