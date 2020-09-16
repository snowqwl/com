package com.sunshine.monitor.system.ws.ConnectionService;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.ws.ConnectionService.bean.CitySituationEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.ConnectionService.CitySituationService", 
		serviceName = "CitySituationService")
@Component("citySituationServiceImpl")
public  class CitySituationServiceImpl implements CitySituationService {
	
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	public List<CitySituationEntity> getSurvey() {
		String sql = "select * from stat_system order by dwdm";
		List<CitySituationEntity> list = null;
		try {
			list = this.systemDao.queryList(sql, CitySituationEntity.class);
			for (CitySituationEntity cs : list) {			
				cs.setJqmc(this.systemDao
						.getCodeValue("156012", cs.getJq()));		
				cs.setJrmc(this.systemDao
						.getCodeValue("156012", cs.getJr()));
				cs.setXtmc(this.systemDao
						.getCodeValue("156012", cs.getXt()));
			}
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}

	public String cityResponse() {
		return "1";
	}

}
