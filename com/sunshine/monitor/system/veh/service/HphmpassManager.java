package com.sunshine.monitor.system.veh.service;

import java.util.Map;

public interface HphmpassManager {
	
	/**
	 * 用户查询提示
	 * @param condition
	 * @return
	 */
	public int queryTips(Map condition);
	
	/**
	 * 查询过车信息列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecList(Map<String, Object> conditions) throws Exception; 
	
}
