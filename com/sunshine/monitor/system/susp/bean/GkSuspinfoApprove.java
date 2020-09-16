package com.sunshine.monitor.system.susp.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;

@Entity(name="VEH_GK_SUSPINFO_APPROVE")
public class GkSuspinfoApprove {
	@Id
	private String  bjxh ;
	private String  bkxh ;
	private String  hcr  ;
	private String  hcsj ;
	private String  hcjg ;
	private String  hcnr ;
	private String  hcdw ;
	private String  by1  ;
	private String  by2  ;
	
	public String hcdwmc ;
	public String hcrmc ;
	
	@XmlElement(name="报警序号")
	public String getBjxh() {
		return bjxh;
	}
	public void setBjxh(String bjxh) {
		this.bjxh = bjxh;
	}
	@XmlElement(name="布控序号")
	public String getBkxh() {
		return bkxh;
	}
	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}
	public String getHcr() {
		return hcr;
	}
	public void setHcr(String hcr) {
		this.hcr = hcr;
	}
	public String getHcsj() {
		return hcsj;
	}
	public void setHcsj(String hcsj) {
		this.hcsj = hcsj;
	}
	@XmlElement(name="核查结果")
	public String getHcjg() {
		return hcjg;
	}
	public void setHcjg(String hcjg) {
		this.hcjg = hcjg;
	}
	public String getHcnr() {
		return hcnr;
	}
	public void setHcnr(String hcnr) {
		this.hcnr = hcnr;
	}
	public String getHcdw() {
		return hcdw;
	}
	public void setHcdw(String hcdw) {
		this.hcdw = hcdw;
	}
	public String getBy1() {
		return by1;
	}
	public void setBy1(String by1) {
		this.by1 = by1;
	}
	public String getBy2() {
		return by2;
	}
	public void setBy2(String by2) {
		this.by2 = by2;
	}
	public String getHcdwmc() {
		return hcdwmc;
	}
	public void setHcdwmc(String hcdwmc) {
		this.hcdwmc = hcdwmc;
	}
	public String getHcrmc() {
		return hcrmc;
	}
	public void setHcrmc(String hcrmc) {
		this.hcrmc = hcrmc;
	}
}
