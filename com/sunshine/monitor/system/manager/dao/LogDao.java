package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.BusinessLog;
import com.sunshine.monitor.system.manager.bean.Log;

public interface LogDao extends BaseDao {
	
	public Map<String,Object> findLogForMap(Map filter);
	
	public Map<String,Object> findLogForMap(Map filter, Log log);
	
	/**
	 * 日志行为分析
	 * 按用户统计
	 * @param log
	 * @return
	 */
	public Map<String,Object> findLogCountForMapByYH(Map filter, Log log) throws Exception;
	
	/**
	 * 日志行为分析
	 * 按部门统计
	 * @param log
	 * @return
	 */
	public Map<String,Object> findLogCountForMapByBM(Map filter, Log log) throws Exception;
	
	/**
	 * 日志行为分析
	 * 按区划统计
	 * @param log
	 * @return
	 */
	public Map<String,Object> findLogCountForMapByQH(Map filter, Log log) throws Exception;
	
	public List<Log> findLogList(Log log);
	
	public int saveLogs(String paramString1, String paramString2,
			String paramString3, String paramString4, String paramString5);
	
	/**
	 * 保存业务日志
	 * @return
	 * @throws Exception
	 */
	public int saveBusinessLog(BusinessLog log) throws Exception;
	
	/**
	 * 添加日志
	 * @param log
	 * @return
	 */
	public int saveLog(Log log) throws Exception;
	
	/**
	 * 最近一个月与上个月环比(用户数、查询数、布控数)----按公安月环比
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map getPoliceRatio(Map condition) throws Exception;
	
	/**
	 * 获取日志类型
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryLogCategoryTree(String filter) throws Exception;
	
	/**
	 * 查询是否有下级
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public boolean countLogCategoryParent(String filter) throws Exception;
	
	/**
	 * 保存同步日志结果
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveSynchLog(Map<String,Object> map) throws Exception;
	
	/**
	 * 用户授权日志审计分页查询
	 */
	public Map<String,Object> getLogForMap(Map<String,String> params, Log log,String type);
}
