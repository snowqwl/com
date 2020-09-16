package com.sunshine.monitor.system.ws.AlarmService;

import java.util.List;
import java.util.Map;
import javax.jws.WebService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.alarm.dao.VehAlarmDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.ws.AlarmService.bean.VehAlarmrecEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.AlarmService.VehAlarmService", serviceName = "VehAlarmService")
@Component("vehAlarmServiceImpl")
public class VehAlarmServiceImpl implements VehAlarmService {

	@Autowired
	@Qualifier("vehAlarmDao")
	private VehAlarmDao vehAlarmDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	public String queryVehAlarm(String conditions) {
		String result = "";
		Map<String, Object> map = null;
		if (conditions != null && conditions.length() > 0) {
			map = JSONObject.fromObject(conditions);
		}
		try {
			Map<String, Object> temp = this.vehAlarmDao.findPageVehAlarmForMap(map, VehAlarmrecEntity.class);
		    List<VehAlarmrecEntity> list = (List<VehAlarmrecEntity>) temp.get("rows");
		    for(int i = 0;i<list.size();i++){
		    	VehAlarmrecEntity alarm = list.get(i);	
		    	alarm.setBjdlmc(this.systemDao.getCodeValue("130033",alarm.getBjdl()));
		    	alarm.setBjlxmc(this.systemDao.getCodeValue("130034",alarm.getBjlx()));
		    	alarm.setHpzlmc(this.systemDao.getCodeValue("030107",alarm.getHpzl()));
		    	alarm.setCllxmc(this.systemDao.getCodeValue("030104",alarm.getCllx()));
		    	alarm.setHpysmc(this.systemDao.getCodeValue("031001",alarm.getHpys()));
		    	alarm.setCwhpysmc(this.systemDao.getCodeValue("031001",alarm.getCwhpys()));
		    	alarm.setHpyzmc(this.systemDao.getCodeValue("031003",alarm.getHpyz()));
		    	alarm.setCsysmc(this.systemDao.getCodeValue("030108",alarm.getCsys()));
		    	alarm.setQrztmc(this.systemDao.getCodeValue("120014",alarm.getQrzt()));
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
