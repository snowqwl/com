package com.sunshine.monitor.system.analysis.dao;

import java.util.List;

public interface TrafficFlowDao {

	/**
	 * 表名是否存在
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public boolean isExstisTable(String name) throws Exception;

	/**
	 * 按小时统计流量
	 * @param <T>
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public <T> List<? extends Object> queryTotalFlowPeerHour(String tableName, String condition)
			throws Exception;

	/**
	 * 按天统计流量
	 * @param <T>
	 * @param Conddition
	 * @return
	 * @throws Exception
	 */
	public <T> List<? extends Object> queryTotalFlowPeerDay(String tableName, String condition)
			throws Exception;

	/**
	 * 按小时统计流量
	 * @param <T>
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public <T> List<? extends Object> queryTotalFlowPeerHour(String tableName, String condition, Object ...args)
			throws Exception;

	/**
	 * 按天统计流量
	 * @param <T>
	 * @param Conddition
	 * @return
	 * @throws Exception
	 */
	public <T> List<? extends Object> queryTotalFlowPeerDay(String tableName, String condition, Object ...args)
			throws Exception;
	
	/**
	 * 统计流量
	 * @param <T>
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public <T> List<? extends Object> queryTotalFlow(String sql, Object[] args) throws Exception;
	
	/**
	 * 复制数据到临时表
	 * @param condition
	 * @param tableName
	 * @return
	 */
	public int copyDatasTotempTable(String condition, String tableName);
	
	/**
	 * 复制数据到临时表
	 * @param condition
	 * @param tableName
	 * @param args
	 * @return
	 */
	public int copyDatasTotempTable(String condition, String tableName, Object[] args);
	
	/**
	 * 删除临时表
	 * @param tableName
	 * @return
	 */
	public int deleteTempTable(String tableName);
	
	
	/**创建索引
	 * @param tableName
	 * @param field
	 * @return
	 */
	public int createTableIdx(String tableName, String idxName, String field);
	
}
