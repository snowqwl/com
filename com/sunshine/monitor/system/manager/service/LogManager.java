package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.manager.bean.BusinessLog;
import com.sunshine.monitor.system.manager.bean.Log;

public interface LogManager {

public Map<String,Object> findLogForMap(Map filter);
	
	public Map<String,Object> findLogForMap(Map filter, Log log);
	
	/**
	 * 日志行为分析
	 * 按用户统计
	 * @param log
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> findLogCountForMapByYH(Map filter, Log log) throws Exception;
	
	/**
	 * 日志行为分析
	 * 按部门统计
	 * @param log
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> findLogCountForMapByBM(Map filter, Log log) throws Exception;
	
	/**
	 * 日志行为分析
	 * 按区划统计
	 * @param log
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> findLogCountForMapByQH(Map filter, Log log) throws Exception;
	
	public List<Log> findLogList(Log log);
	
	public int saveLogs(String paramString1, String paramString2,
			String paramString3, String paramString4, String paramString5);
	
	public int saveBusinessLog(BusinessLog log) throws Exception;
	
	/**
	 * 添加日志
	 * @param log
	 * @return
	 */
	public int saveLog(Log log) throws Exception;
	
	/**
	 * 获取日志类别树
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getLogCategoryTree(String filter) throws Exception;
	
	/**
	 * 查询是否有下级
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public boolean countLogCategoryParent(String filter) throws Exception;
	
	/**
	 * 获取用户授权日志、车辆查询日志列表
	 * @param params
	 * @param log 
	 * @param type 类型：yhsq/clcx
	 * @return
	 */
	public Map<String,Object> findLogForMap2(Map<String,String> params, Log log,String type);
}
