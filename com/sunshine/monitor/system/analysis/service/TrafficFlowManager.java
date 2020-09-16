package com.sunshine.monitor.system.analysis.service;

import java.util.List;

import com.sunshine.monitor.system.analysis.bean.TrafficFlow;

public interface TrafficFlowManager {

	
	/**
	 * 按天统计流量,环比分两次查询
	 * 
	 * @param <T>
	 * @param Conddition
	 * @return
	 * @throws Exception
	 */
	public <T> List<List<? extends Object>> queryTotalFlowPeerDay(
			TrafficFlow trafficFlow) throws Exception;

	

	/**
	 * 复制数据到临时表
	 * @param condition
	 * @param tableName
	 * @param args
	 * @return
	 */
	public int copyDatasTotempTable(String condition, String tableName, Object[] args);
	

	/**
	 * 删除表
	 * 
	 * @param tableName
	 */
	public void deleteTable(String tableName);


	/**
	 * 按小时统计流量,环比一次查询
	 * 
	 * @param <T>
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public <T> List<? extends Object> queryFlowPeerHour(TrafficFlow trafficFlow)
			throws Exception;
	
	/**
	 * 表名是否存在
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public boolean isExstisTable(String name) throws Exception;
	
	/**
	 * 创建索引
	 * @param tableName
	 * @param fields
	 * @throws Exception
	 */
	public void createTableIndex(String tableName,String ...fields) throws Exception;
}
