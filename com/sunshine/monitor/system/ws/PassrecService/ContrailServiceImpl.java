package com.sunshine.monitor.system.ws.PassrecService;

import java.util.Map;
import javax.jws.WebService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.veh.dao.PassrecDao;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.PassrecService.ContrailService", serviceName = "ContrailService")
@Component("contrailServiceImpl")
public class ContrailServiceImpl implements ContrailService {

	@Autowired
	@Qualifier("passrecDao")
	private PassrecDao passrecDao;
	
	public String getHphm(String conditions) {
		Map<String, Object> map = null;
		String result = "";
		if (conditions != null && conditions.length() > 0) {
			map = JSONObject.fromObject(conditions);
		}
		try {
			Map<String, Object> temp = this.passrecDao.getHphm(map);
			JSONObject json = JSONObject.fromObject(temp);
			result=json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String queryContrail(String conditions) {
		Map<String, Object> map = null;
		String result = "";
		if (conditions != null && conditions.length() > 0) {
			map = JSONObject.fromObject(conditions);
		}
		try {
			Map<String, Object> temp = this.passrecDao.queryContrailForMap(map);
			JSONObject json = JSONObject.fromObject(temp);
			result=json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
