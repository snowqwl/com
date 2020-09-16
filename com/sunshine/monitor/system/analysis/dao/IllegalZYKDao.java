package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface IllegalZYKDao {
	
	public Map queryOneIllegal(Map filter) throws Exception;
	
	/**
	 * 获取违法次数
	 * @param cars
	 * @return
	 * @throws Exception
	 */
	public Map<CarKey, Integer> getViolationCount(List<CarKey> cars) throws Exception;

//	/**
//	 * NEW
//	 * 获取当前页的违法次数 2016-10-10 liumeng  
//	 * @param cars
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<CarKey, Integer> getViolationCountWF(List<CarKey> cars) throws Exception;
	/**
	 * 根据号牌号码，号牌种类查询查询车辆违法历史记录
	 * @param car
	 * @param filter
	 * @return
	 */
	public Map<String,Object> getViolationDetail(CarKey car,Map<String,Object> filter)throws Exception;
	
	/**
	 * 根据号牌号码，号牌种类查询查询车辆违法历史记录
	 * @param car
	 * @param filter
	 * @return
	 */
	public Map<String,Object> getViolationDetail2(CarKey car,Map<String,Object> filter)throws Exception;
	
	/**
	 * 根据号牌号码集合查询车辆违法次数
	 * 缺少号牌种类
	 * 请使用getViolationCount方法
	 * @param hphms
	 * @return
	 * @throws Exception
	 */
    public Map<String,Object> getTrafficCount(List<String> hphms)throws Exception;	
	
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
	public Map<String,Object> getMapForIntegrateTraffic(Map<String,Object> filter, VehPassrec info,String citys, String wflxTab)throws Exception;
	
	/**
	 * 根据违法记录查询交通违法详细信息
	 * @param wfbh 违法记录
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getViolationForWfbh(String wfbh) throws Exception;
	
	/**
	 * 获取机动车信息
	 */
	public VehicleEntity getVehicleInfo(VehicleEntity veh) throws Exception; 
	
}
