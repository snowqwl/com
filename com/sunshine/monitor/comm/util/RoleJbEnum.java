package com.sunshine.monitor.comm.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色级别
 * @author YANGYANG
 *
 */
public enum RoleJbEnum {
	
	JS_SYSTEM("0","系统管理员"),
	
	JS_PROVINCE("1","省级"),
	
	JS_CITY("2","市级"),
	
	JS_COUNTY("3","区县级"),
	
	JS_OTHER("4","其它");
	
	/**
	 * 级别编号
	 */
	private String jbbh ;
	
	/**
	 * 级别名称
	 */
	private String jbmc ;
	
	
	private RoleJbEnum(String jbbh, String jbmc) {
		this.jbbh = jbbh ;
		this.jbmc = jbmc ;
	}
	
	public static Map<String,String> getRoleJbMap(){
		Map<String,String> map = new HashMap<String, String>();
		RoleJbEnum[] rjbs = RoleJbEnum.values() ;
		for(RoleJbEnum rolejb:rjbs){
			map.put(rolejb.getJbbh(), rolejb.getJbmc());
		}
		return map ;
	}
	
	public String getJbbh() {
		return jbbh;
	}

	public void setJbbh(String jbbh) {
		this.jbbh = jbbh;
	}

	public String getJbmc() {
		return jbmc;
	}

	public void setJbmc(String jbmc) {
		this.jbmc = jbmc;
	}
}
