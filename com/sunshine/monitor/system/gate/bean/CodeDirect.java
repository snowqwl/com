package com.sunshine.monitor.system.gate.bean;

import com.sunshine.monitor.comm.bean.Entity;


public class CodeDirect extends Entity {
	private static final long serialVersionUID = 1L;
	private String fxbh;
	private String fxmc;
	private String kdbh;
	private String fxlx;
	private String xlh;
	private String fxxh;
	private String gxsj;
	private String bz;
	private String fxlxmc;

	public String getFxbh() {
		return this.fxbh;
	}
	
	public String getKdbh() {
		return kdbh;
	}

	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}



	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}

	public String getFxmc() {
		return this.fxmc;
	}

	public void setFxmc(String fxmc) {
		this.fxmc = fxmc;
	}

	public String getFxlx() {
		return this.fxlx;
	}

	public void setFxlx(String fxlx) {
		this.fxlx = fxlx;
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

	public String getFxlxmc() {
		return this.fxlxmc;
	}

	public void setFxlxmc(String fxlxmc) {
		this.fxlxmc = fxlxmc;
	}

	public String getXlh() {
		return xlh;
	}

	public void setXlh(String xlh) {
		this.xlh = xlh;
	}

	public String getFxxh() {
		return fxxh;
	}

	public void setFxxh(String fxxh) {
		this.fxxh = fxxh;
	}

}
