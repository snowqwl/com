package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;

public interface VehicleSCSDao extends ScsBaseDao {

	/**
	 * 获取机动车信息-greenplum
	 * @param cars
	 * @return
	 * @throws Exception 
	 */
	public Map<CarKey, VehicleEntity> getVehicleList(List<CarKey> cars) throws Exception;


}
