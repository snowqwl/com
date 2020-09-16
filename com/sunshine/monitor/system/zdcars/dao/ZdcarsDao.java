package com.sunshine.monitor.system.zdcars.dao;

import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.zdcars.bean.CarS;

public interface ZdcarsDao extends BaseDao{
	public Map<String, Object> saveZdCars(CarS car) throws Exception ; 
	public Map<String, Object> selectCarsResult(String key,String value) throws Exception ;
}
