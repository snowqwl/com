package com.sunshine.monitor.system.query.dao;

import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.query.bean.ScrapAnnual;


public interface ScrapAnnualDao extends BaseDao {

	/**
	 * 
	 * 函数功能说明:报废未年检查询过车数据
	 * @param map
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForScrapAnnual(Map map,ScrapAnnual info)throws Exception;
	
}
