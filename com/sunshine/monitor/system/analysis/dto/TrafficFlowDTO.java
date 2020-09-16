package com.sunshine.monitor.system.analysis.dto;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 流通流量分析:交通流量条件传输实体
 * 用于前端页面与后台交互
 * @author OUYANG
 *
 */
public class TrafficFlowDTO extends Entity{
	
	private static final long serialVersionUID = 1L;

	private String kdbh;
	
	private String staticType;
	
	private String analysisType;
	
	private String kssj;
	
	private String jssj;
	
	private String hpys;
	
	private String cllx;
	
	private String csys;
	
	private String displayType;

	public String getKdbh() {
		return kdbh;
	}

	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}

	public String getStaticType() {
		return staticType;
	}

	public void setStaticType(String staticType) {
		this.staticType = staticType;
	}

	public String getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
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

	public String getCsys() {
		return csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getDisplayType() {
		return displayType;
	}
	
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
}
