package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface WarningModelManager {
	
	/**分析区域首次出现*/
	public Map queryWarningList(Map filter,VehPassrec veh) throws Exception ;
	/**查询分析出来的过车信息*/
	public Map queryPass(Map filter) throws Exception ;
	
	public Map queryOneIllegal(Map filter) throws Exception ;
	
	public List<Map<String,Object>> queryWarningListExt(Map filter,VehPassrec veh) throws Exception ;
	
	public Integer queryWarningListTotal(Map filter,VehPassrec veh) throws Exception ;
}
