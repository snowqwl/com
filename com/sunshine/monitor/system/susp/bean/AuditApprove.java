package com.sunshine.monitor.system.susp.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.sunshine.monitor.comm.bean.Entity;

public class AuditApprove extends Entity {
	
	private static final long serialVersionUID = 1L;
	private String xh;
	private String bkxh;
	private String czr;
	private String czrmc;
	private String czrjh;
	private String czrdw;
	private String czrdwmc;
	private String czsj;
	private String czjg;
	private String ms;
	private String bzw;
	private String by1;
	private String by2;
	private String czjgmc;
	private String bzwmc;
	private String user;
	private String lxdh;
	
	public String getBzwmc() {
		return bzwmc;
	}

	public void setBzwmc(String bzwmc) {
		this.bzwmc = bzwmc;
	}

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}
	
	@XmlElement(name="布控序号")
	public String getBkxh() {
		return this.bkxh;
	}

	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}

	public String getCzr() {
		return this.czr;
	}

	public void setCzr(String czr) {
		this.czr = czr;
	}

	public String getCzrdw() {
		return this.czrdw;
	}

	public void setCzrdw(String czrdw) {
		this.czrdw = czrdw;
	}

	public String getCzrdwmc() {
		return this.czrdwmc;
	}

	public void setCzrdwmc(String czrdwmc) {
		this.czrdwmc = czrdwmc;
	}

	public String getCzsj() {
		return this.czsj;
	}

	public void setCzsj(String czsj) {
		this.czsj = czsj;
	}

	public String getCzjg() {
		return this.czjg;
	}

	public void setCzjg(String czjg) {
		this.czjg = czjg;
	}

	public String getMs() {
		return this.ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

	public String getBzw() {
		return this.bzw;
	}

	public void setBzw(String bzw) {
		this.bzw = bzw;
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

	public String getCzrjh() {
		return this.czrjh;
	}

	public void setCzrjh(String czrjh) {
		this.czrjh = czrjh;
	}

	public String getCzrmc() {
		return this.czrmc;
	}

	public void setCzrmc(String czrmc) {
		this.czrmc = czrmc;
	}

	public String getCzjgmc() {
		return this.czjgmc;
	}

	public void setCzjgmc(String czjgmc) {
		this.czjgmc = czjgmc;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	
}