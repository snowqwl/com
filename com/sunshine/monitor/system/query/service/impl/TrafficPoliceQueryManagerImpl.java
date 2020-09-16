package com.sunshine.monitor.system.query.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.bean.TrafficPolicePassrec;
import com.sunshine.monitor.system.query.dao.TrafficPoliceQueryDao;
import com.sunshine.monitor.system.query.service.TrafficPoliceQueryManager;

@Service("trafficPoliceQueryManager")
public class TrafficPoliceQueryManagerImpl implements TrafficPoliceQueryManager{
	
	@Autowired
	@Qualifier("trafficPoliceQueryDao")
	private TrafficPoliceQueryDao trafficPoliceQueryDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	public Map<String, Object> getPassrecList(TrafficPolicePassrec veh, Page page)
			throws Exception {
		Map<String,Object> result = this.trafficPoliceQueryDao.getPassrecList(veh,page);
		List<TrafficPolicePassrec> list = (List<TrafficPolicePassrec>) result.get("rows");
		for(TrafficPolicePassrec v:list){
			CodeGate gate = this.systemDao.queryDetail("V_DEV_TOLLGATE","kdbh",v.getKdbh(),CodeGate.class);
			v.setKdmc(gate!=null?gate.getKdmc():v.getKdbh());
			v.setHpzlmc(this.systemDao.getCodeValue("030107", v.getHpzl()));
			v.setHpysmc(this.systemDao.getCodeValue("031001", v.getHpys()));
			v.setCllxmc(this.systemDao.getCodeValue("030104", v.getCllx()));
			v.setCsysmc(this.systemDao.getCsys(v.getCsys()));
		}
		result.put("rows",list);
		return result;
	}

}
