package com.sunshine.monitor.comm.bean;

/**
 * 角色表
 * @author JCBK-OUYANG 2012/12/26
 *
 */
public class Role extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 角色编号
	 */
	private String jsdh;
	/**
	 * 角色名称
	 */
	private String jsmc;
	/**
	 * 角色属性
	 */
	private String jssx;
	
	/**
	 * 备注
	 */
	private String bz;

	/**
	 * 角色级别（省级、市级、区县级）
	 */
	private String jsjb;
	
	/**
	 * 角色类型
	 */
	private String type;
	
	/**
	 * 
	 * 角色排序
	 */
	private String px;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJsjb() {
		return jsjb;
	}

	public void setJsjb(String jsjb) {
		this.jsjb = jsjb;
	}

	public String getJsdh() {
		return this.jsdh;
	}

	public void setJsdh(String jsdh) {
		this.jsdh = jsdh;
	}

	public String getJsmc() {
		return this.jsmc;
	}

	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

	public String getJssx() {
		return this.jssx;
	}

	public void setJssx(String jssx) {
		this.jssx = jssx;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	

	

	
	
}
