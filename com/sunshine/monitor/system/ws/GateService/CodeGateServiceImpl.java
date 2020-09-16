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
import com.sunshine.monitor.system.ws.GateService.bean.CodeGateEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.GateService.CodeGateService", serviceName = "CodeGateService")
@Component("codeGateServiceImpl")
public class CodeGateServiceImpl implements CodeGateService {

	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	public String getGate(String conditions) {
		Map<String, Object> map = null;
		String result = "";
		if (conditions != null && conditions.length() > 0) {
			map = JSONObject.fromObject(conditions);
		}
		try {
			List<CodeGateEntity> list = this.gateDao.queryForListByMap(map,"Code_Gate", CodeGateEntity.class);
			JSONArray json = JSONArray.fromObject(list);
			result = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String queryGate(String conditions) {
		Map<String, Object> map = null;
		String result = "";
		if (conditions != null && conditions.length() > 0) {
			map = JSONObject.fromObject(conditions);
		}
		try {
			Map<String, Object> temp = this.gateDao.findPageGateForMap(map,CodeGateEntity.class);
			List<CodeGateEntity> list = (List<CodeGateEntity>) temp.get("rows");
			for (int i = 0; i < list.size(); i++) {
				CodeGateEntity gate = list.get(i);
				gate.setDwdmmc(this.systemDao.getDepartmentName(gate.getDwdm()));
				gate.setKklxmc(this.systemDao.getCodeValue("150000", gate.getKklx()));
				gate.setKklx2mc(this.systemDao.getCodeValue("156002", gate.getKklx2()));
				gate.setKkxzmc(this.systemDao.getCodeValue("156001", gate.getKkxz()));
				gate.setKkztmc(this.systemDao.getCodeValue("156003", gate.getKkzt()));
				gate.setDllxmc(this.systemDao.getCodeValue("156010", gate.getDllx()));
				if (gate.getBjcs() != null && gate.getBjcs().length() > 0) {
					StringBuffer bjcsmc = new StringBuffer();
					String[] bjcs = gate.getBjcs().split(",");
					List<String> l = this.systemDao.getCityNames(bjcs);
					for (String s : l) {
						bjcsmc.append(s + ",");
					}
					gate.setBjcsmc(bjcsmc.substring(0, (bjcsmc.length() - 1)));
				}
			}
			temp.put("rows", list);
			JSONObject json = JSONObject.fromObject(temp);
			result = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
