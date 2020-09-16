package com.sunshine.monitor.system.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.bean.BusinessLog;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.dao.LogDao;
import com.sunshine.monitor.system.manager.service.LogManager;

@Transactional
@Service("logManager")
public class LogManagerImpl implements LogManager {

	@Autowired
	@Qualifier("logDao")
	private LogDao logDao;

	public Map<String, Object> findLogForMap(Map filter) {
		return logDao.findLogForMap(filter);
	}
	
	public Map<String, Object> findLogForMap(Map filter, Log log) {
		return logDao.findLogForMap(filter, log);
	}

	public Map<String, Object> findLogCountForMapByYH(Map filter, Log log) throws Exception  {
		return this.logDao.findLogCountForMapByYH(filter, log);
	}
	
	/**
	 * 日志行为分析
	 * 按部门统计
	 * @param log
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> findLogCountForMapByBM(Map filter, Log log) throws Exception {
		return this.logDao.findLogCountForMapByBM(filter, log);
	}
	
	/**
	 * 日志行为分析
	 * 按区划统计
	 * @param log
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> findLogCountForMapByQH(Map filter, Log log) throws Exception {
		return this.logDao.findLogCountForMapByQH(filter, log);
	}

	public List<Log> findLogList(Log log) {
		return this.logDao.findLogList(log);
	}

	public int saveLogs(String paramString1, String paramString2,
			String paramString3, String paramString4, String paramString5) {

		return this.logDao.saveLogs(paramString1, paramString2, paramString3,
				paramString4, paramString5);
	}

	public int saveBusinessLog(BusinessLog log) throws Exception {
		
		return this.logDao.saveBusinessLog(log);
	}

	public int saveLog(Log log) throws Exception {
		
		return this.logDao.saveLog(log);
	}

	@Override
	public List<Map<String,Object>> getLogCategoryTree(String filter)
			throws Exception {
		return this.logDao.queryLogCategoryTree(filter);
	}

	@Override
	public boolean countLogCategoryParent(String filter) throws Exception{
		return this.logDao.countLogCategoryParent(filter);
	}

	//用户授权日志分页查询
	@Override
	public Map<String, Object> findLogForMap2(Map<String, String> map, Log log,String type) {
		// TODO Auto-generated method stub
		return this.logDao.getLogForMap(map, log,type);
	}
}
