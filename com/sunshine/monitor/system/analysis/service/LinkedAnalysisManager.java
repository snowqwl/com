package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;

public interface LinkedAnalysisManager {

	/**
	 * 关联查询
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findLinkForPage(ScsVehPassrec veh,Map<String,Object> filter) throws Exception;
	
	/**
	 * 改造 liumeng 2016-9-8
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findLinkForPageExt(ScsVehPassrec veh,Map<String,Object> filter) throws Exception;
	
	/**
	 * 改造 liumeng 2016-9-8
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int getForLinkTotal(ScsVehPassrec veh, Map<String,Object> filter) throws Exception;
	
	/**
	 * 关联结果过车轨迹列表查询
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findLinkDetailForPage(ScsVehPassrec veh,Map<String,Object> filter)throws Exception;
	
	/**
	 * 获取关联分析结果
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getLinkList(String sessionId)throws Exception;

}
