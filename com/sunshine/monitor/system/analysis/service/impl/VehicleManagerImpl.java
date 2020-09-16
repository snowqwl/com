package com.sunshine.monitor.system.analysis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.vehicle.tran.CSYSTranslation;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.dao.VehicleDao;
import com.sunshine.monitor.system.analysis.dao.ZykVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.VehicleManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Service("vehicleManager")
public class VehicleManagerImpl implements VehicleManager {
	
	@Autowired
	@Qualifier("vehicleDao")
	private VehicleDao vehicleDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("scsVehPassrecDao")
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("zykVehPassrecDao")
	private ZykVehPassrecDao zykVehPassrecDao;

	public VehicleEntity getVehicleInfo(VehicleEntity veh) throws Exception {
		VehicleEntity entity =  this.vehicleDao.getVehicleInfo(veh);
		if(entity!=null){
			entity.setZp(this.vehicleDao.getVehiclePic(entity.getSystemid()));
			entity.setCsysmc(this.systemDao.getCodeValue("030108",entity.getCsys()));
		}
		return entity;
	}
	
	/**
	 * 在Greenplum环境中查询机动车信息
	 * @param veh
	 * @return
	 * @throws Exception
	 */
	public VehicleEntity getVehicleInfoGreenplum(VehicleEntity veh) throws Exception {
		VehicleEntity entity =  this.zykVehPassrecDao.getVehicleInfo(veh);
		if(entity!=null){
			//entity.setZp(this.vehicleDao.getVehiclePic(entity.getSystemid()));
			CSYSTranslation csysTranslation = new CSYSTranslation("030108");
			entity.setCsysmc(csysTranslation.getMC(entity.getCsys()));
		}
		return entity;
	}

	public Map<CarKey, VehicleEntity> getVehicleList(List<CarKey> cars)
			throws Exception {
		return this.vehicleDao.getVehicleList(cars);
	}

}
