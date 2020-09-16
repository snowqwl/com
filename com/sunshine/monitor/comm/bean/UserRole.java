package com.sunshine.monitor.comm.bean;


/**
 * 用户角色表
 * @author JCBK-OUYANG 2012/12/26
 *
 */
public class UserRole extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户编号
	 */
	private String yhdh;
	/**
	 * 角色编 号
	 */
	private String jsdh;

	public String getYhdh() {
		return this.yhdh;
	}

	public void setYhdh(String yhdh) {
		this.yhdh = yhdh;
	}

	public String getJsdh() {
		return this.jsdh;
	}

	public void setJsdh(String jsdh) {
		this.jsdh = jsdh;
	}
}
