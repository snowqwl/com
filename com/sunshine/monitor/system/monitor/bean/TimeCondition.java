package com.sunshine.monitor.system.monitor.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class TimeCondition extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String kssj ;
	
	private String jssj;
	
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
}
