package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.DayNightAnalysis;
import com.sunshine.monitor.system.analysis.bean.DayNigntNewAnaiysis;

public interface DaysNightAnalysisDao {
	public int saveDayNightGz(DayNightAnalysis info) throws Exception;
	public List<DayNightAnalysis> getDqValid() throws Exception;
	public Map<String, Object> queryGZList(Map<String, Object> conditions) throws Exception;
	public int delDayNightAna(String gzxh) throws Exception;
	public DayNightAnalysis getEditDaynightInfo(String gzxh) throws Exception;
	public int editDayNightGz(DayNightAnalysis info) throws Exception;
	public int editZt(DayNightAnalysis info,String zt) throws Exception;
	public int editOtherZt() throws Exception;
	public DayNightAnalysis getZyxx() throws Exception;
	public Map<String,Object> queryList(DayNigntNewAnaiysis dn,Map<String, Object> filter)throws Exception;
}
