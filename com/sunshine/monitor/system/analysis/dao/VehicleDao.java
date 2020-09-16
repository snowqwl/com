package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;

public interface VehicleDao extends BaseDao {

	/**
	 * 查询机动车信息明细
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public VehicleEntity getVehicleInfo(VehicleEntity veh)throws Exception;
	
	/**
	 * 查询机动车信息集合
	 * @param cars
	 * @return
	 * @throws Exception
	 */
	public Map<CarKey, VehicleEntity> getVehicleList(List<CarKey> cars) throws Exception;
	
	/**
	 * 根据序号获取机动车图片
	 * @param xh
	 * @return
	 * @throws Exception
	 */
	public String getVehiclePic(String xh)throws Exception;

}
