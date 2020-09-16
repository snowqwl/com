package com.sunshine.monitor.system.query.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;


public interface ReSuspinfoQueryManager {
	public int updates(List<Map<String, Object>> listrows);
	public int insert(List<Map<String, Object>> listrows);
	public List<Map<String, Object>> query(String bkxh);
	
	/**
	 * 查询布/管控信息
	 * @param filter
	 * @param info
	 * @param conSql
	 * @return
	 */
	public Map getSuspinfoMapForFilter(Map filter, VehSuspinfo info,String conSql)throws Exception ;
	
	/**
	 * [新增续控联动布控]
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveLinkVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception; 
	

}
