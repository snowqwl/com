package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface WarningModelSCSDao {
	
	public Map queryWarningList(Map filter, VehPassrec veh) throws Exception;
	
	public Map queryPass(Map filter) throws Exception;
	
	public Map queryOneIllegal(Map filter) throws Exception;
	
	public Map queryKksbl(Map filter) throws Exception;
	
	public List<Map<String, Object>> queryWarningListExt(Map filter, VehPassrec veh) throws Exception;
	
	public int queryWarningListTotal(Map filter, VehPassrec veh) throws Exception;
}
