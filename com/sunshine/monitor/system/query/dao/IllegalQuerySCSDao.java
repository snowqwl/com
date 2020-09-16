package com.sunshine.monitor.system.query.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface IllegalQuerySCSDao extends ScsBaseDao {

	/**
	 * 
	 * 函数功能说明：查询交通违法记录 greenplum
	 * 修改日期 	2016-3-22 liumeng
	 * @param filter
	 * @param info
	 * @param citys
	 * @param wflxTab 
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForIntegrateTraffic(Map filter, VehPassrec info,String citys, String wflxTab)throws Exception;

	/**
	 * 根据违法记录查询交通违法详细信息
	 * @param wfbh 违法记录
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getViolationForWfbh(String wfbh) throws Exception;

	/**
	 * 根据号牌号码，号牌种类查询查询车辆违法历史记录
	 * @param car
	 * @param filter
	 * @return
	 */
	public Map<String,Object> getViolationDetail(CarKey car,Map<String,Object> filter)throws Exception;
}
