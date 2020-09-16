package com.sunshine.monitor.system.ws.outAccess.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class WsManager extends Entity {
	private static final long serialVersionUID = 212206816516869690L;
	
	private String id;
	private String systemtype;
	private String businesstype;
	private String businessname;
	private String classname;
	private String method;
	private String zt;
	private String sn;
	private String ip;
	private String gxsj;
	private String bz;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystemtype() {
		return this.systemtype;
	}

	public void setSystemtype(String systemtype) {
		this.systemtype = systemtype;
	}

	public String getBusinesstype() {
		return this.businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getBusinessname() {
		return this.businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}

	public String getClassname() {
		return this.classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getZt() {
		return this.zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getGxsj() {
		return this.gxsj;
	}

	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
