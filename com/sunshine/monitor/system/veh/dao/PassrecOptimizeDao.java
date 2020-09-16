package com.sunshine.monitor.system.veh.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface PassrecOptimizeDao extends BaseDao{
	
	/**
	 * 查询过车信息列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecList(Map<String, Object> conditions, String tableName) throws Exception;
	
	/**
	 * 查询过车信息列表（含交警卡口）
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecAndJjList(Map<String, Object> conditions, String tableName) throws Exception;
	
	
	/**
	 * 统计查询总数
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public int getAllCount(Map<String,Object> conditions) throws Exception;
	
	/**
	 * 统计查询总数(含交警卡口)
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public int getPassAndJjAllCount(Map<String,Object> conditions) throws Exception;
	
	
	
	/**
	 * 用户查询提示
	 * @param condition
	 * @return
	 */
	public int queryTips(Map condition) ;
	
	/**
	 * 用户查询提示（含交警过车查询）
	 * @param condition
	 * @return
	 */
	public int queryTipsAndJj(Map condition) ;
	
	
	/**
	 * 查询过车总数
	 * @param sql
	 * @param params
	 * @return
	 */
	public int queryPassCount(String sql, Object... params) throws Exception;
	
	/**
	 * 查询过车分页数据
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List queryPassPage(String sql, Object[] params, Class<?> clazz) throws Exception;
}
