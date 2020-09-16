package com.sunshine.monitor.system.ws.ManagerService;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.manager.dao.CodeDao;
import com.sunshine.monitor.system.ws.ManagerService.bean.CodeEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.ManagerService.CodeService", serviceName = "CodeService")
@Component("codeServiceImpl")
public class CodeServiceImpl implements CodeService {


	@Autowired
	@Qualifier("codeDao")
	private CodeDao codeDao;

	public String getCode(String conditions) {
		Map<String,Object> map=null;
		String result = "";
		if(conditions!=null&&conditions.length()>0){
			map=JSONObject.fromObject(conditions);
		}
		try {
			List<CodeEntity> list = this.codeDao.queryForListByMap(map,"frm_code",CodeEntity.class);
			JSONArray json = JSONArray.fromObject(list);
			result = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
