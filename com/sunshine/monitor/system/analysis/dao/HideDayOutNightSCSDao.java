package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.HideDayOutNightDayBean;

public interface HideDayOutNightSCSDao {
	
	/**
	 * 查询昼伏夜出统计列表-按天数统计
	 * @param bean
	 * @param filter
	 * @return list
	 * @throws Exception
	 */
	public List<Map<String,Object>> getDayNightListExt(Map<String,Object> filter,HideDayOutNightDayBean bean) throws Exception;
	
	/**
	 * 统计昼伏夜出列表总记录数-按天数统计 
	 * @param bean
	 * @param filter
	 * @return list
	 * @throws Exception
	 */
	public int getDayNightTotal(Map<String,Object> filter,HideDayOutNightDayBean bean) throws Exception;

}
