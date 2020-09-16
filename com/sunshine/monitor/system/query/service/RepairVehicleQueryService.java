package com.sunshine.monitor.system.query.service;

import java.util.Map;

/**
 * 机修车查询service
 * @author admin
 *
 */
public interface RepairVehicleQueryService {

	/**
	 * 获取机修车列表
	 * @param params
	 * @return
	 */
	public Map<String, Object> list(Map<String,Object> params);
	
	/**
	 * 获取详情
	 * @param params
	 * @return
	 */
	public Map<String,Object> detail(Map<String,Object> params);
	
	/**
	 * 获取机修车列表（导出功能使用）
	 * @param params
	 * @return
	 */
	public Map<String, Object> getListXls(Map<String,Object> params);
}
