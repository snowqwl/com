package com.sunshine.monitor.system.monitor.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.Code;

public interface StatMonitorDao extends BaseDao {

	/**
	 * 获取时间段内的卡口流量
	 * 
	 * @param kssj
	 * @param jssj
	 * @param jkdss
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getIdentList(String kssj, String jssj,
			String jkdss) throws Exception;

	/**
	 * 获取单个卡口小时流量
	 * 
	 * @param kssj
	 * @param jssj
	 * @param cdbh
	 * @param jkdss
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getHourIdentList(String kssj, String jssj,
			String kdbh, String fxbh, String cdbh) throws Exception;

	/**
	 *过车流量统计
	 */
	public Map<String, Object> getFlow();

	/**
	 * 省厅过车流量统计
	 * 
	 * @param cityList
	 * @return
	 */
	public List<Map<String, Object>> getStFlow(List<Code> cityList);
}
