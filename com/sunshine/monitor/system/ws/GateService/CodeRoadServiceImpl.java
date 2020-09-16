package com.sunshine.monitor.system.ws.GateService;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.ws.GateService.bean.CodeRoadEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.GateService.CodeRoadService", serviceName = "CodeRoadService")
@Component("codeRoadServiceImpl")
public class CodeRoadServiceImpl implements CodeRoadService {

	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	public String getRoad(String conditions) {
		Map<String, Object> map = null;
		String result = "";
		if (conditions != null && conditions.length() > 0) {
			map = JSONObject.fromObject(conditions);
		}
		try {
			List<CodeRoadEntity> list = this.gateDao.queryForListByMap(map,"Code_Gate_Cd", CodeRoadEntity.class);
			for (int i = 0; i < list.size(); i++) {
				CodeRoadEntity road = list.get(i);
				road.setCdlxmc(this.systemDao.getCodeValue("", road.getCdlxmc()));
			}
			JSONArray json = JSONArray.fromObject(list);
			result = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
