package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.analysis.bean.DayNightAnalysis;
import com.sunshine.monitor.system.analysis.bean.DayNigntNewAnaiysis;
import com.sunshine.monitor.system.notice.bean.SysInformation;

public interface DaysNightAnalysisManager {
	public int saveDayNightGz(DayNightAnalysis info)throws Exception;
	public List<DayNightAnalysis>  getDqValid() throws Exception ;
	public Map<String, Object> queryGZList(Map<String, Object> conditions) throws Exception;
	public int delDayNightAna(String gzxh) throws Exception;
	public DayNightAnalysis getEditDaynightInfo(String gzxh)throws Exception;
	public int editDayNightGz(DayNightAnalysis info) throws Exception;
	public int editZt(DayNightAnalysis info,String zt) throws Exception;
	public int editOtherZt() throws Exception;
	public DayNightAnalysis getZyxx() throws Exception;
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
