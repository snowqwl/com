package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.bean.CombineCondition;
import com.sunshine.monitor.system.analysis.bean.FalseLicense;

public interface TimeSpaceCombineDao extends BaseDao{
	
	/**
	 * 轨迹信息
	 * @param map
	 * @param consql
	 * @return
	 */
	public Map<String, Object> combineAnalysis(Map map, String consql);
	
	/**
	 * 轨迹统计
	 * @param map
	 * @param consql
	 * @return
	 */
	public Map<String, Object> trackCount(Map map, String consql);
	
	/**
	 * 频繁出入分析
	 * @param map
	 * @param consql
	 * @return
	 */
	public Map<String, Object> offenInOut(Map map, String consql);
	
	/**
	 * 假设轨迹分析(Oracle)
	 * @param filter
	 * @param consql
	 * @param condition
	 * @return
	 */
	public Map<String, Object> supposeAnalysis(Map filter,String consql,String[] condition);
	

	/**
	 * 假设轨迹分析统计（Oracle）
	 * @param filter
	 * @param consql
	 * @param condition
	 * @return
	 */
	public Map<String,Object> supposeAnalysisTrack(Map filter,String consql,String[] condition);
	

}
