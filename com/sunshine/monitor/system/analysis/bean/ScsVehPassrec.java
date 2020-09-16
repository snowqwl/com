package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;
import com.sunshine.monitor.comm.dao.annotation.DIC;
import com.sunshine.monitor.comm.dao.annotation.GetName;

public class ScsVehPassrec extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4434413867539085079L;
	
	private String gcxh;//	varchar(22) default null,

	private String sbbh;// 	varchar(20) default null,
	
	@GetName(tableName="code_gate",pk="kdbh",name="kdmc",nameFieldName="kdmc")
	private String kdbh;// 	varchar(18) default null,
	
	private String kdmc;
	
	@GetName(tableName="code_gate_extend",pk="fxbh",name="fxmc",nameFieldName="fxmc")
	private String fxbh;// 	varchar(10) default null,
	
	private String fxmc;
	
	@DIC(dicFieldName="hpzlmc",dicType="030107")
	private String hpzl;// 	varchar(2) default null,
	
	private String hpzlmc;

	private String hphm;// 	varchar(15) default null,

	private String cwhphm;// 	varchar(15) default null,

	private String cwhpys;// 	char(1) default null,
	
	private String cwhpysmc;

	private String hpyz;//	 char(1) default null,
	
	private String hpyzmc;

	private String gcsj;// 	datetime default null,

	private String clsd;// 	int(5) default null,

	private String clxs;// 	int(5) default null,
	@DIC(dicFieldName="hpysmc",dicType="031001")
	private String hpys;// 	char(1) default null,
	
	private String hpysmc;

	private String cllx;// 	varchar(4) default null,
	
	private String cllxmc;

	private String xszt;// 	varchar(5) default null,
	
	private String xsztmc;

	private String byzd;// 	varchar(50) default null,

	private String clpp;// 	varchar(3) default null,

	private String clwx;// 	varchar(3)  default null,
	
	private String csys;// 	varchar(3) default null,

	private String cdbh;// 	varchar(2) default null,

	private String tp1;// 	varchar(1024) default null,

	private String tp2;// 	varchar(1024) default null,

	private String tp3;// 	varchar(1024) default null,

	private String wfbj;// 	varchar(2) default null,

	private String rksj;// 	datetime default null,

	private String by1;// 	varchar(256) default null
	@GetName(tableName="frm_xzqh",pk="xzqhdm",name="xzqhmc",nameFieldName="xzqhmc")
	private String xzqh;//行政区划
	
	private String xzqhmc;//行政区划名称
	
	private String gaptime;//同行时间
	
	private String condition;//车辆信息条件组
	
	private String kssj;//开始时间
	
	private String jssj;//结束时间
	
	private String wfcs;//违法关联次数
	
	private String pageSign;//翻页标识
	
	private String sjc;
	
	private String checked;
	
	private String mbhp;
	
	private String mbxh;
	
	private String kkjd;
	
	private String kkwd;
	
	private String xxly;//信息来源-报废未年检
	private String lb;//布控类别-报废未年检
	

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	public String getXxly() {
		return xxly;
	}

	public void setXxly(String xxly) {
		this.xxly = xxly;
	}

	public String getGcxh() {
		return gcxh;
	}

	public void setGcxh(String gcxh) {
		this.gcxh = gcxh;
	}

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
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

	public String getHpyz() {
		return hpyz;
	}

	public void setHpyz(String hpyz) {
		this.hpyz = hpyz;
	}

	public String getGcsj() {
		return gcsj;
	}

	public void setGcsj(String gcsj) {
		this.gcsj = gcsj;
	}

	public String getHpys() {
		return hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getXszt() {
		return xszt;
	}

	public void setXszt(String xszt) {
		this.xszt = xszt;
	}

	public String getByzd() {
		return byzd;
	}

	public void setByzd(String byzd) {
		this.byzd = byzd;
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

	public String getCsys() {
		return csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getCdbh() {
		return cdbh;
	}

	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
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

	public String getWfbj() {
		return wfbj;
	}

	public void setWfbj(String wfbj) {
		this.wfbj = wfbj;
	}

	public String getRksj() {
		return rksj;
	}

	public void setRksj(String rksj) {
		this.rksj = rksj;
	}

	public String getBy1() {
		return by1;
	}

	public void setBy1(String by1) {
		this.by1 = by1;
	}

	public String getClsd() {
		return clsd;
	}

	public void setClsd(String clsd) {
		this.clsd = clsd;
	}

	public String getClxs() {
		return clxs;
	}

	public void setClxs(String clxs) {
		this.clxs = clxs;
	}

	public String getXzqh() {
		return xzqh;
	}

	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}

	public String getGaptime() {
		return gaptime;
	}

	public void setGaptime(String gaptime) {
		this.gaptime = gaptime;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
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

	public String getHpzlmc() {
		return hpzlmc;
	}

	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
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

	public String getXsztmc() {
		return xsztmc;
	}

	public void setXsztmc(String xsztmc) {
		this.xsztmc = xsztmc;
	}

	public String getXzqhmc() {
		return xzqhmc;
	}

	public void setXzqhmc(String xzqhmc) {
		this.xzqhmc = xzqhmc;
	}

	public String getWfcs() {
		return wfcs;
	}

	public void setWfcs(String wfcs) {
		this.wfcs = wfcs;
	}

	public String getPageSign() {
		return pageSign;
	}

	public void setPageSign(String pageSign) {
		this.pageSign = pageSign;
	}

	public String getSjc() {
		return sjc;
	}

	public void setSjc(String sjc) {
		this.sjc = sjc;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
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

	public String getMbhp() {
		return mbhp;
	}

	public void setMbhp(String mbhp) {
		this.mbhp = mbhp;
	}

	public String getMbxh() {
		return mbxh;
	}

	public void setMbxh(String mbxh) {
		this.mbxh = mbxh;
	}
	
	

}
