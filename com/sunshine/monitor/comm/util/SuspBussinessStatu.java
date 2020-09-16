package com.sunshine.monitor.comm.util;

/**
 * 布、撤控业务装态
 * @author OUYANG
 *
 */
public enum SuspBussinessStatu {

	DISPATCHED_WAIT_CHECK("120008","11","布控待审核"),
	
	DISPATCHED_WAIT_APPROVAL("120008","12","布控待审批"),
	
	DISPATCHED_WAIT_MODIFICATION("120008","13","布控待修改"),
	
	DISPATCHED_WAIT_WITHDRAW("120008","14","布控待撤销"),
	
	WITHDRAW_WAIT_CHECK("120008","41","撤控待审核"),
	
	WITHDRAW_WAIT_APPROVAL("120008","42","撤控待审批"),
	
	DISPATCHED_WITHDRAWED("120008","99","布控已撤销");
	
	private String dmlb;
	
	private String dmz ;
	
	private String dmsm;
	
	private SuspBussinessStatu(String dmlb, String dmz, String dmsm){
		this.dmlb = dmlb ;
		this.dmz = dmz ;
		this.dmsm = dmsm ;
	}

	public String getDmlb() {
		return dmlb;
	}

	public String getDmz() {
		return dmz;
	}

	public String getDmsm() {
		return dmsm;
	}
}
