package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

public interface CaseTouchZYKDao {
	public List getCaseDm(String ywxtmc,String dmlb,String dmmc) throws Exception; 
	
	public Map<String,Object> getCaseForList(Map<String,Object> conditions)throws Exception;
}
