package com.sunshine.monitor.system.ws.ManagerService;

import java.util.Map;
import java.util.List;
import javax.jws.WebService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.manager.dao.DepartmentDao;
import com.sunshine.monitor.system.ws.ManagerService.bean.DepartmentEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.ManagerService.DepartmentService", serviceName = "DepartmentService")
@Component("departmentServiceImpl")
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	@Qualifier("departmentDao")
	private DepartmentDao departmentDao;

	public String getDepartment(String conditions) {
		Map<String,Object> map=null;
		String result = "";
		if(conditions!=null&&conditions.length()>0){
			map=JSONObject.fromObject(conditions);
		}
		try {
			List<DepartmentEntity> list = this.departmentDao.queryForListByMap(map, "frm_department",DepartmentEntity.class);
			JSONArray json = JSONArray.fromObject(list);
			result = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
