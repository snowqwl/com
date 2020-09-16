package com.sunshine.monitor.comm.bean;

/**
 * 角色菜单表
 * @author JCBK-OUYANG 2012/12/26
 *
 */
public class RoleMenu extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 角色编号
	 */
	private String jsdh;
	/**
	 * 菜单编号
	 */
	private String cxdh;
	
	/**
	 * 父菜单编号
	 */
	private String parentid;
	
	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getJsdh() {
		return this.jsdh;
	}

	public void setJsdh(String jsdh) {
		this.jsdh = jsdh;
	}

	public String getCxdh() {
		return this.cxdh;
	}

	public void setCxdh(String cxdh) {
		this.cxdh = cxdh;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
