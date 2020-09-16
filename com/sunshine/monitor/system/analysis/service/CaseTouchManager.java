package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.CaseTouch;
import com.sunshine.monitor.system.analysis.bean.CaseTouchAnalysis;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface CaseTouchManager {
	public String saveCaseTouchGz(CaseTouchAnalysis command)throws Exception;
	
	public Map<String,Object> queryCaseTouchRuleList(Map<String,Object> conditions)throws Exception;
	
	public CaseTouchAnalysis getEditInformation(String gzbh)throws Exception;
	
	public int updateCaseTouchGz(CaseTouchAnalysis command)throws Exception;
	
	public List getCaseTouchRule() throws Exception;
	
	public List getCaseDm(String ywxtmc,String dmlb,String dmmc) throws Exception;
		
	public Map<String,Object> getCaseForList(Map<String,Object> conditions)throws Exception;
	
	public Map<String,Object> getCaseTouchAnaiysisDatagrid(Map<String,Object> filter,List<CaseTouch> list)throws Exception;
	
	public Map<String,Object> getCaseTouchVehList(VehPassrec veh,Map<String,Object> filter) throws Exception;
	
}
