package com.sunshine.monitor.comm.quart.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class JobLog extends Entity {
	private static final long serialVersionUID = 1L;
	private String xh;
	private String rwbh;
	private String zxsj;
	private String jssj;
	private String czlx;
	private String czjg;
	private String cwnr;
	private String bz;
	public String rwmc;
	public String czjgmc;

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getRwbh() {
		return this.rwbh;
	}

	public void setRwbh(String rwbh) {
		this.rwbh = rwbh;
	}

	public String getZxsj() {
		return this.zxsj;
	}

	public void setZxsj(String zxsj) {
		this.zxsj = zxsj;
	}

	public String getJssj() {
		return this.jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getCzlx() {
		return this.czlx;
	}

	public void setCzlx(String czlx) {
		this.czlx = czlx;
	}

	public String getCzjg() {
		return this.czjg;
	}

	public void setCzjg(String czjg) {
		this.czjg = czjg;
	}

	public String getCwnr() {
		return this.cwnr;
	}

	public void setCwnr(String cwnr) {
		this.cwnr = cwnr;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getRwmc() {
		return this.rwmc;
	}

	public void setRwmc(String rwmc) {
		this.rwmc = rwmc;
	}

	public String getCzjgmc() {
		return this.czjgmc;
	}

	public void setCzjgmc(String czjgmc) {
		this.czjgmc = czjgmc;
	}
}