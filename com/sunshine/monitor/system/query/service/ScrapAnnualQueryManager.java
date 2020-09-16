package com.sunshine.monitor.system.query.service;

import java.util.Map;
import com.sunshine.monitor.system.query.bean.ScrapAnnual;
public interface ScrapAnnualQueryManager {
	/**
	 * 
	 * 函数功能说明:报废未年检查询数据
	 * @param map
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForScrapAnnual(Map filter,ScrapAnnual info)throws Exception;
}
