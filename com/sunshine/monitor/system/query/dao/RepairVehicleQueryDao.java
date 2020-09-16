package com.sunshine.monitor.system.query.dao;

import java.util.List;
import java.util.Map;

/**
 * 机修车查询dao
 * @author admin
 *
 */
public interface RepairVehicleQueryDao {

	/**
	 * 机修车查询列表
	 * @param params
	 * @return
	 */
	public Map<String, Object> getList(Map<String,Object> params);
	
	/**
	 * 获取机修车详情
	 * @param params
	 * @return
	 */
	public Map<String,Object> getDetail(Map<String,Object> params);
	/**
	 * 获取机动车详情
	 * @param params
	 * @return
	 */
	public Map<String,Object> getJdcDetail(Map<String,Object> params);
}
