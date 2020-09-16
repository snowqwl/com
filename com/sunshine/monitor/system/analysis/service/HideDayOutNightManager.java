package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.HideDayOutNightDayBean;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface HideDayOutNightManager {

	/**
	 * 查询昼伏夜出统计列表-按天数统计
	 * @param bean
	 * @param filter
	 * @return list
	 * @throws Exception
	 */
	public List<Map<String,Object>> findDayNightForPageExt(Map<String,Object> filter,HideDayOutNightDayBean bean) throws Exception;
	
	/**
	 * 统计昼伏夜出列表总记录数-按天数统计 
	 * @param bean
	 * @param filter
	 * @return list
	 * @throws Exception
	 */
	public int getForLinkTotal(Map<String,Object> filter,HideDayOutNightDayBean bean) throws Exception;
	
	/**
	 * 过车轨迹数据列表查询
	 * @param veh
	 * @param filter
	 * @return
	 */
	public List<Map<String, Object>> queryGjPassrecListExt(VehPassrec veh, Map<String, Object> filter);
	
	/**
	 * 过车轨迹数据列表轨迹总数查询
	 * @param veh
	 * @param filter
	 * @return
	 */
	public Integer queryGjPassrecListTotal(VehPassrec veh, Map<String, Object> filter);
	
	public List<Map<String, Object>> queryHideDayHotView(VehPassrec veh) throws Exception;
	
	public List<Map<String, Object>> queryCarCount(VehPassrec veh) throws Exception;
	
}
