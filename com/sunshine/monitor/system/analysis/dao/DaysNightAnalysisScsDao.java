package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.analysis.bean.DayNigntNewAnaiysis;

public interface DaysNightAnalysisScsDao extends ScsBaseDao{
	public Map<String,Object> queryList(DayNigntNewAnaiysis dn,Map<String, Object> filter)throws Exception;
	public Map<String,Object> queryDayNightPageList(DayNigntNewAnaiysis dn,Map<String, Object> filter)throws Exception;
	
	/**
	 * 改造 2016-9-8 liumeng
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryForDayNightListExt(DayNigntNewAnaiysis dn,Map<String,Object> filter) throws Exception;
	/**
	 * 改造 2016-9-8 liumeng
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int getForDayNightTotal(DayNigntNewAnaiysis dn, Map<String,Object> filter) throws Exception;
}
