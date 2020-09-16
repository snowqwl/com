package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.analysis.bean.CaseTouch;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface CaseTouchSCSDao extends ScsBaseDao {

	public Map<String,Object> getCaseTouchAnaiysisDatagrid(Map<String,Object> filter,List<CaseTouch> list)throws Exception;
	
	public Map<String,Object>  getCaseTouchVehList(VehPassrec veh,Map<String,Object> filter) throws Exception;
	
	public List<Map<String,Object>> getCaseTouchList(String fxtj)throws Exception;
	
	public Map<String,Object> getCaseForList(Map<String,Object> conditions)throws Exception;
	
	public List getCaseDm(String ywxtmc,String dmlb,String dmmc) throws Exception;
}
