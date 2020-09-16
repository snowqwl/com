package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;

public interface IllegalSCSDao extends ScsBaseDao {

	/**
	 * 查询获取违法关联数-greenplum      
	 * @param cars
	 * @return
	 */
	public Map<CarKey, Integer> getViolationCount(List<CarKey> cars) throws Exception;

	

}
