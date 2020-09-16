package com.sunshine.monitor.system.alarm.bean;

import java.sql.Blob;

import javax.xml.bind.annotation.XmlElement;

import com.sunshine.monitor.comm.bean.Entity;

public class VehAlarmHandled extends Entity{
	private static final long serialVersionUID = -3576884980177210708L;
	private String fkbh;
	private String bkxh;
	private String bjxh;
	private String zlxh;
	private String gcxh;
	private String lrr;
	private String lrrjh;
	private String lrrmc;
	private String lrdwdm;
	private String lrdwmc;
	private String lrsj;
	private String ddr;
	private String ddrmc;
	private String xbr;
	private String xbrmc;
	private String sflj;
	private String wljdyy;
	private String clgc;
	private String bjsj;
	private String bjdwdm;
	private String bjdwmc;
	private String bjdwlxdh;
	private String xxly;
	private String by1;
	private String by2;
	private String by3;
	private String by4;
	private String by5;
	private String fkqsdwmc;
	private String dxjshm;
	
	// 图片数据
	private byte[] tp;
	
	public String sfljmc;
	public String wljdyymc;
	
	// 是否有效
	public String sfyx ;


	public byte[] getTp() {
		return tp;
	}

	public void setTp(byte[] tp) {
		this.tp = tp;
	}

	public String getSfyx() {
		return sfyx;
	}

	public void setSfyx(String sfyx) {
		this.sfyx = sfyx;
	}

	public String getWljdyymc() {
		return this.wljdyymc;
	}

	public void setWljdyymc(String wljdyymc) {
		this.wljdyymc = wljdyymc;
	}

	public String getSfljmc() {
		return this.sfljmc;
	}

	public void setSfljmc(String sfljmc) {
		this.sfljmc = sfljmc;
	}

	public String getFkbh() {
		return this.fkbh;
	}

	public void setFkbh(String fkbh) {
		this.fkbh = fkbh;
	}
	
	@XmlElement(name="布控序号")
	public String getBkxh() {
		return this.bkxh;
	}

	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}
	
	@XmlElement(name="报警序号")
	public String getBjxh() {
		return this.bjxh;
	}

	public void setBjxh(String bjxh) {
		this.bjxh = bjxh;
	}

	public String getZlxh() {
		return this.zlxh;
	}

	public void setZlxh(String zlxh) {
		this.zlxh = zlxh;
	}

	public String getGcxh() {
		return this.gcxh;
	}

	public void setGcxh(String gcxh) {
		this.gcxh = gcxh;
	}

	public String getLrr() {
		return this.lrr;
	}

	public void setLrr(String lrr) {
		this.lrr = lrr;
	}

	public String getLrrjh() {
		return this.lrrjh;
	}

	public void setLrrjh(String lrrjh) {
		this.lrrjh = lrrjh;
	}

	public String getLrdwdm() {
		return this.lrdwdm;
	}

	public void setLrdwdm(String lrdwdm) {
		this.lrdwdm = lrdwdm;
	}

	public String getLrdwmc() {
		return this.lrdwmc;
	}

	public void setLrdwmc(String lrdwmc) {
		this.lrdwmc = lrdwmc;
	}

	public String getLrsj() {
		return this.lrsj;
	}

	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}

	public String getDdr() {
		return this.ddr;
	}

	public void setDdr(String ddr) {
		this.ddr = ddr;
	}

	public String getDdrmc() {
		return this.ddrmc;
	}

	public void setDdrmc(String ddrmc) {
		this.ddrmc = ddrmc;
	}

	public String getXbr() {
		return this.xbr;
	}

	public void setXbr(String xbr) {
		this.xbr = xbr;
	}

	public String getXbrmc() {
		return this.xbrmc;
	}

	public void setXbrmc(String xbrmc) {
		this.xbrmc = xbrmc;
	}

	public String getSflj() {
		return this.sflj;
	}

	public void setSflj(String sflj) {
		this.sflj = sflj;
	}

	public String getWljdyy() {
		return this.wljdyy;
	}

	public void setWljdyy(String wljdyy) {
		this.wljdyy = wljdyy;
	}

	public String getClgc() {
		return this.clgc;
	}

	public void setClgc(String clgc) {
		this.clgc = clgc;
	}

	public String getBjsj() {
		return this.bjsj;
	}

	public void setBjsj(String bjsj) {
		this.bjsj = bjsj;
	}

	public String getBjdwdm() {
		return this.bjdwdm;
	}

	public void setBjdwdm(String bjdwdm) {
		this.bjdwdm = bjdwdm;
	}

	public String getBjdwmc() {
		return this.bjdwmc;
	}

	public void setBjdwmc(String bjdwmc) {
		this.bjdwmc = bjdwmc;
	}

	public String getBjdwlxdh() {
		return this.bjdwlxdh;
	}

	public void setBjdwlxdh(String bjdwlxdh) {
		this.bjdwlxdh = bjdwlxdh;
	}

	public String getXxly() {
		return this.xxly;
	}

	public void setXxly(String xxly) {
		this.xxly = xxly;
	}

	public String getBy1() {
		return this.by1;
	}

	public void setBy1(String by1) {
		this.by1 = by1;
	}

	public String getBy2() {
		return this.by2;
	}

	public void setBy2(String by2) {
		this.by2 = by2;
	}

	public String getBy3() {
		return this.by3;
	}

	public void setBy3(String by3) {
		this.by3 = by3;
	}

	public String getBy4() {
		return this.by4;
	}

	public void setBy4(String by4) {
		this.by4 = by4;
	}

	public String getBy5() {
		return this.by5;
	}

	public void setBy5(String by5) {
		this.by5 = by5;
	}

	public String getLrrmc() {
		return this.lrrmc;
	}

	public void setLrrmc(String lrrmc) {
		this.lrrmc = lrrmc;
	}
	public String getDxjshm() {
		return this.dxjshm;
	}

	public void setDxjshm(String dxjshm) {
		this.dxjshm = dxjshm;
	}

	public String getFkqsdwmc() {
		return fkqsdwmc;
	}

	public void setFkqsdwmc(String fkqsdwmc) {
		this.fkqsdwmc = fkqsdwmc;
	}
}
