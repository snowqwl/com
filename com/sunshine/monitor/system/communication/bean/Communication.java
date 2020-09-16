package com.sunshine.monitor.system.communication.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class Communication extends Entity {
	private static final long serialVersionUID = 1L;
	private String id;
	private String xh;//
	private String ywlx;
	private String dxjsr;
	private String dxjshm;
	private String dxnr;
	private String dxfssj;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	public String getDxjsr() {
		return dxjsr;
	}
	public void setDxjsr(String dxjsr) {
		this.dxjsr = dxjsr;
	}
	public String getDxjshm() {
		return dxjshm;
	}
	public void setDxjshm(String dxjshm) {
		this.dxjshm = dxjshm;
	}
	public String getDxnr() {
		return dxnr;
	}
	public void setDxnr(String dxnr) {
		this.dxnr = dxnr;
	}
	public String getDxfssj() {
		return dxfssj;
	}
	public void setDxfssj(String dxfssj) {
		this.dxfssj = dxfssj;
	}

}