package com.sunshine.monitor.system.analysis.service;

import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.SpecialCarQueryGz;

public interface SpecialCarQueryManager {
	
	public String saveBdgz(SpecialCarQueryGz information)throws Exception;
	
	public Map<String, Object>  querySpecialCarRuleList(Map<String, Object>  conditions) throws Exception;
	
	public SpecialCarQueryGz getSpecialCarRule(String gzbh)throws Exception;
}
