package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.bean.CaseTouch;
import com.sunshine.monitor.system.analysis.bean.CaseTouchAnalysis;

public interface CaseTouchDao extends BaseDao{
	
	public String saveCaseTouchGz(CaseTouchAnalysis command)throws Exception;
	
	public Map<String,Object> queryCaseTouchRuleList(Map<String,Object> conditions)throws Exception;
	
	public CaseTouchAnalysis getEditInformation(String gzbh)throws Exception;
	
	public int updateCaseTouchGz(CaseTouchAnalysis command)throws Exception;
	
	public List getCaseTouchRule() throws Exception;
	
	public Map<String,Object> getCaseForList(Map<String,Object> conditions)throws Exception;
	
	public String getXzqhName(String xzqhdm)throws Exception;

	public Map<String, Object> getCaseForListNew(Map<String, Object> conditions) throws Exception;
	
	
}
