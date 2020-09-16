package com.sunshine.monitor.system.veh.bean;

import javax.xml.bind.annotation.XmlElement;

import com.sunshine.monitor.comm.bean.Entity;

public class VehPassrec extends Entity {
	private static final long serialVersionUID = 1L;
	private String gcxh;
	private String sbbh;
	private String kdbh;
	private String fxbh;
	private String fxlx;
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
	private String gateNameMsg;
	private String directNameMsg;
	private String gateColor;
	private String directColor;
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
	public String licesenHeader;
	public String bz;
	public String isread;

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

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


	public String getFxlx() {
		return fxlx;
	}

	public void setFxlx(String fxlx) {
		this.fxlx = fxlx;
	}
	
	@XmlElement(name="号牌种类")
	public String getHpzl() {
		return this.hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}
	
	@XmlElement(name="号牌号码")
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
	
	@XmlElement(name="违法开始时间")
	public String getKssj() {
		return this.kssj;
	}

	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	
	@XmlElement(name="违法结束时间")
	public String getJssj() {
		return this.jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getKssd() {
		return this.kssd;
	}

	public void setKssd(String kssd) {
		this.kssd = kssd;
	}

	public String getJssd() {
		return this.jssd;
	}

	public void setJssd(String jssd) {
		this.jssd = jssd;
	}

	public String getGlbm() {
		return this.glbm;
	}

	public void setGlbm(String glbm) {
		this.glbm = glbm;
	}

	public String getHpzlmc() {
		return this.hpzlmc;
	}

	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}

	public String getSbmc() {
		return this.sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getKdmc() {
		return this.kdmc;
	}

	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}

	public String getFxmc() {
		return this.fxmc;
	}

	public void setFxmc(String fxmc) {
		this.fxmc = fxmc;
	}

	public String getHpysmc() {
		return this.hpysmc;
	}

	public void setHpysmc(String hpysmc) {
		this.hpysmc = hpysmc;
	}

	public String getCllxmc() {
		return this.cllxmc;
	}

	public void setCllxmc(String cllxmc) {
		this.cllxmc = cllxmc;
	}

	public String getWfbjmc() {
		return this.wfbjmc;
	}

	public void setWfbjmc(String wfbjmc) {
		this.wfbjmc = wfbjmc;
	}

	public String getCwhpysmc() {
		return this.cwhpysmc;
	}

	public void setCwhpysmc(String cwhpysmc) {
		this.cwhpysmc = cwhpysmc;
	}

	public String getHpyzmc() {
		return this.hpyzmc;
	}

	public void setHpyzmc(String hpyzmc) {
		this.hpyzmc = hpyzmc;
	}

	public String getCsysmc() {
		return this.csysmc;
	}

	public void setCsysmc(String csysmc) {
		this.csysmc = csysmc;
	}

	public String getXsztmc() {
		return this.xsztmc;
	}

	public void setXsztmc(String xsztmc) {
		this.xsztmc = xsztmc;
	}

	public String getWpc() {
		return this.wpc;
	}

	public void setWpc(String wpc) {
		this.wpc = wpc;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDatasource() {
		return this.datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getLl() {
		return this.ll;
	}

	public void setLl(String ll) {
		this.ll = ll;
	}
	
	public String getGateNameMsg() {
		return this.gateNameMsg;
	}

	public void setGateNameMsg(String gateNameMsg) {
		this.gateNameMsg = gateNameMsg;
	}
	
	public String getDirectNameMsg() {
		return this.directNameMsg;
	}

	public void setDirectNameMsg(String directNameMsg) {
		this.directNameMsg = directNameMsg;
	}
	
	public String getGateColor() {
		return this.gateColor;
	}

	public void setGateColor(String gateColor) {
		this.gateColor = gateColor;
	}
	
	public String getDirectColor() {
		return this.directColor;
	}

	public void setDirectColor(String directColor) {
		this.directColor = directColor;
	}

	public String getLicesenHeader() {
		return licesenHeader;
	}

	public void setLicesenHeader(String licesenHeader) {
		this.licesenHeader = licesenHeader;
	}

	public String getFxbh() {
		return fxbh;
	}

	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}

	@Override
	public String toString() {
		return "VehPassrec [tp1=" + tp1 + ", tp2=" + tp2 + ", tp3=" + tp3 + ", by1=" + by1 + "]";
	}

	
}