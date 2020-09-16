package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.CombineCondition;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface TimeSpaceCombineManager {

	/**
	 * 假设轨迹分析
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> supposeAnalysis(Map map, CombineCondition cc) throws Exception;
	
	/**
	 * 假设轨迹分析（oracle）
	 * @param filter
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> supposeAnalysisByOracle(Map filter,CombineCondition cc) throws Exception;
	
	/**
	 * 频繁出入分析
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> offenInOut(Map map,CombineCondition cc) throws Exception;
	
	/**
	 * 区域碰撞分析
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> districtAnalysis(Map map,CombineCondition cc) throws Exception;
	
	/**
	 * 假设轨迹分析、时空碰撞分析（时间任意）（大数据）
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> supposeAnalysisByScsDB(Map map, CombineCondition cc) throws Exception;
	
	/**
	 * 频繁出入分析（大数据）
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> offenInOutByScsDB(Map map,CombineCondition cc,String tablename) throws Exception;
	
	/**
	 * 频繁出入分析GP--改造 liumeng 2016-9-28 
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> offenInOutByScsDBExt(Map<String, Object> map,CombineCondition cc) throws Exception;
	/**
	 * 频繁出入分析GP-总数统计--改造 liumeng 2016-9-28 
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public int queryTotals(CombineCondition cc, Map<String,Object> filter) throws Exception;
	/**
	 * 区域碰撞分析（大数据）
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> districtAnalysisByScsDB(Map map,CombineCondition cc,String tablename) throws Exception;
	
	/**
	 * 区域碰撞分析（大数据） 区域碰撞去掉原有区域数、卡口数、天数、次数，改为统计各个区域出现次数
	 * @param map
	 * @param cc
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> districtAnalysis2ByScsDB(Map map,CombineCondition cc,String tablename) throws Exception;
	public List<Map<String, Object>> districtAnalysis2ByScsDBExt(Map map,CombineCondition cc,String tablename) throws Exception;
	public int getTotal2(Map map,CombineCondition cc,String tablename) throws Exception;
	
	/**
	 * 预警判断
	 * @param param
	 * @return
	 */
	public int alarmRule(Map param);
	
}
