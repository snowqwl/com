package com.sunshine.monitor.system.manager.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class Code extends Entity {

	private static final long serialVersionUID = 1L;
	private String dmlb = "";
	private String dmz = "";
	private String dmsm1 = "";
	private String dmsm2 = "";
	private String dmsm3 = "";
	private String dmsm4 = "";
	private String dmsx = "";
	private String zt = "";

	public Code() {

	}

	public Code(String dmlb, String dmz) {
		this.dmlb = dmlb;
		this.dmz = dmz;
	}

	public Code(String dmlb, String dmz, String dmsm1) {
		this.dmlb = dmlb;
		this.dmz = dmz;
		this.dmsm1 = dmsm1;
	}

	public String getDmlb() {
		return this.dmlb;
	}

	public String getDmsm1() {
		return this.dmsm1;
	}

	public String getDmsm2() {
		return this.dmsm2;
	}

	public String getDmsm3() {
		return this.dmsm3;
	}

	public String getDmsm4() {
		return this.dmsm4;
	}

	public String getDmz() {
		return this.dmz;
	}

	public String getZt() {
		return this.zt;
	}

	public String getDmsx() {
		return this.dmsx;
	}

	public void setDmlb(String dmlb) {
		this.dmlb = dmlb;
	}

	public void setDmsm1(String dmsm1) {
		this.dmsm1 = dmsm1;
	}

	public void setDmsm2(String dmsm2) {
		this.dmsm2 = dmsm2;
	}

	public void setDmsm3(String dmsm3) {
		this.dmsm3 = dmsm3;
	}

	public void setDmsm4(String dmsm4) {
		this.dmsm4 = dmsm4;
	}

	public void setDmz(String dmz) {
		this.dmz = dmz;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public void setDmsx(String dmsx) {
		this.dmsx = dmsx;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Code code = (Code) obj;
		if (this.dmlb.equals(code.getDmlb()) && this.dmz.equals(code.getDmz()))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return this.dmlb.hashCode() * this.dmz.hashCode()
				* this.dmsm1.hashCode();
	}
}
