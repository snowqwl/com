package com.sunshine.monitor.system.ws.AlarmService;

import java.util.Map;
import java.util.List;

import javax.jws.WebService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.alarm.dao.RealalarmDao;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.ws.AlarmService.bean.RealAlarmrecEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.AlarmService.RealAlarmService", serviceName = "RealAlarmService")
@Component("realAlarmServiceImpl")
public class RealAlarmServiceImpl implements RealAlarmService {

	@Autowired
	private RealalarmDao realalarmDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;

	public String queryRealAlarm(String conditions) {
		String result = "";
		Map<String, Object> map = null;
		if (conditions != null && conditions.length() > 0) {
			map = JSONObject.fromObject(conditions);
		}
		try {
			Map<String, Object> temp = this.realalarmDao.findPageAlarmForMap(
					map, RealAlarmrecEntity.class);
		    List<RealAlarmrecEntity> list = (List<RealAlarmrecEntity>) temp.get("rows");
		    for(int i = 0;i<list.size();i++){
		    	RealAlarmrecEntity real = list.get(i);
		    	real.setKdmc(this.gateManager.getGateName(real.getKdbh()));
		    	real.setFxmc(this.gateManager.getDirectName(real.getFxbh()));
		    	real.setCllxmc(this.systemDao.getCodeValue("030104",real.getCllx()));
		    	real.setHpysmc(this.systemDao.getCodeValue("031001",real.getHpys()));
		    	real.setBjlxmc(this.systemDao.getCodeValue("130034",real.getBjlx()));
		    	real.setHpzlmc(this.systemDao.getCodeValue("030107",real.getHpzl()));
		    	real.setCsysmc(this.systemDao.getCodeValue("030108",real.getCsys()));
		    	real.setBjdwdmmc(this.systemDao.getDepartmentName(real.getBjdwdm()));
		    }
		    temp.put("rows",list);
			JSONObject json = JSONObject.fromObject(temp);
			result = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
