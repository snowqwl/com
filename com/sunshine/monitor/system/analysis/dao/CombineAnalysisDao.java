package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.analysis.bean.CombineCondition;

public interface CombineAnalysisDao extends ScsBaseDao {

	/**
	 * 轨迹信息（ 大数据）
	 * 
	 * @param map
	 * @param consql
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> combineAnalysisByScsDb(Map map, String consql,String[] conditionArray) throws Exception;


	/**
	 * 频繁出入分析（ 大数据）
	 * 
	 * @param map
	 * @param consql
	 * @return
	 */
	public Map<String, Object> offenInOutByScsDb(Map map, String consql,String pl,String tablename,String cssql);
	/**
	 * 频繁出入（GP）--改造 liumeng 2016-9-28 
	 * @param map
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> offenInOutByScsDbExt(Map<String, Object> filter, String sql)throws Exception;
	/**
	 * 频繁出入（GP）-总记录统计--改造 liumeng 2016-9-28 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int getTotals(String sql) throws Exception;
	/**
	 * 区域碰撞分析（大数据）
	 * @param filter
	 * @param consql
	 * @param conditionArray
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> districtbyScsDb(Map filter, String consql, String[] conditionArray,String tablename) throws Exception;

	/**
	 * 区域碰撞分析改造（大数据）统计列表：去掉原有区域数、卡口数、天数、次数，改为统计各个域出现次数
	 * @param filter
	 * @param consql
	 * @param conditionArray
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> district2byScsDb(Map filter, String consql, String[] conditionArray,String tablename) throws Exception;
	public List<Map<String, Object>> district2byScsDbExt(Map filter, String consql, String[] conditionArray,String tablename, Object[] params) throws Exception;
	public int getTotal2(Map filter, String consql, String[] conditionArray,String tablename) throws Exception;
	
	
	/**
	 * 时间组合，分页处理，直接查询临时表
	 * @param filter
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> pageByScsDb(Map filter, String tablename) throws Exception;
	
	/**
	 * 查询轨迹列表
	 * @param filter
	 * @param consql
	 * @return
	 */
	public Map<String, Object> queryGjList(Map filter,String consql);	
	public List<Map<String, Object>> queryGjListExt(Map filter,String consql)  throws Exception;	
	public int getTotal3(Map filter,String consql) throws Exception;
	
	/**
	 * 查询轨迹列表（临时表方式）
	 * @param filter
	 * @param consql
	 * @return
	 */
	public Map<String, Object> supposeGjList(Map filter, String[] conditionArray);
	
	/**
	 * 预警规则
	 * @param condition
	 * @return
	 */
	public int alarmRule(String condition);

}
