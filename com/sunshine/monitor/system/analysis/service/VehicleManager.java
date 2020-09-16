package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;

public interface VehicleManager {


	/**
	 * 查询机动车信息明细
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public VehicleEntity getVehicleInfo(VehicleEntity veh)throws Exception;
	
	/**
	 * 在Greenplum环境中查询机动车信息明细
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public VehicleEntity getVehicleInfoGreenplum(VehicleEntity veh)throws Exception;
	
	/**
	 * 查询机动车信息集合
	 * @param cars
	 * @return
	 * @throws Exception
	 */
	public Map<CarKey, VehicleEntity> getVehicleList(List<CarKey> cars) throws Exception;
	
}
