package com.sunshine.monitor.system.query.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;

public interface ReQueryListDao extends BaseDao {
	public int updates(List<Map<String, Object>> listrows);
	public int insert(List<Map<String, Object>> listrows);
	public List<Map<String, Object>> query(String bkxh);
	
	/**
	 * 函数功能说明：查询布控表
	 * @param map
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map getMapForSuspinfoFilter(Map map,StringBuffer condition)throws Exception;
	
	public boolean saveSuspinfopictrue(VehSuspinfopic vspic)throws Exception;
	
}
