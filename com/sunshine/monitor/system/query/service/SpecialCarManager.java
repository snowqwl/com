package com.sunshine.monitor.system.query.service;

import java.util.Map;

public interface SpecialCarManager {
	
	
	
	/**
	 * 查询特殊车辆库列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map querySpecialList(Map conditions) throws Exception;
	

}
