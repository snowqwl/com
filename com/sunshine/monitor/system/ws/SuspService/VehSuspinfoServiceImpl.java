package com.sunshine.monitor.system.ws.SuspService;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.jws.WebService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;
import com.sunshine.monitor.system.ws.SuspService.bean.VehSuspinfoEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.SuspService.VehSuspinfoService", serviceName = "VehSuspinfoService")
@Component("vehSuspinfoServiceImpl")
public class VehSuspinfoServiceImpl implements VehSuspinfoService {

	@Autowired
	private SuspinfoDao suspinfoDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	public String queryVehSuspinfo(String conditions) {
		String result ="";
		Map<String, Object> map = new HashMap<String,Object>();
		
		if(conditions!=null&&conditions.length()>0){
    		map = JSONObject.fromObject(conditions);	
		}
		
		map.put("sort", "bkxh");
		map.put("order", "asc");
		try {
			Map<String, Object> temp = this.suspinfoDao.querySuspinfoByPage(map, VehSuspinfoEntity.class);
		    List<VehSuspinfoEntity> list = (List<VehSuspinfoEntity>) temp.get("rows");
		    for(int i = 0;i<list.size();i++){
		    	VehSuspinfoEntity susp = list.get(i);

		    	susp.setHpzlmc(this.systemDao.getCodeValue("030107",susp.getHpzl()));
		    	susp.setBkdlmc(this.systemDao.getCodeValue("120019",susp.getBkdl()));
		    	susp.setBklbmc(this.systemDao.getCodeValue("120005",susp.getBklb()));

		    	susp.setBkfwlxmc(this.systemDao.getCodeValue("120004",susp.getBkfwlx()));
		    	susp.setBkjbmc(this.systemDao.getCodeValue("120020",susp.getBkjb()));
		    	susp.setBkxzmc(this.systemDao.getCodeValue("120021",susp.getBkxz()));
		    	susp.setSqsbmc(this.systemDao.getCodeValue("190001",susp.getSqsb()));
		    	susp.setBjyamc(this.systemDao.getCodeValue("130022",susp.getBjya()));
		    	susp.setBjfsmc(this.systemDao.getCodeValue("130000",susp.getBjfs()));  	
		    	
		    	susp.setHpysmc(this.systemDao.getCodeValue("031001",susp.getHpys()));
		    	susp.setCllxmc(this.systemDao.getCodeValue("030104",susp.getCllx()));
		    	susp.setCsysmc(this.systemDao.getCodeValue("030108",susp.getCsys()));
		    	
		    	susp.setCkyydmmc(this.systemDao.getCodeValue("120007",susp.getCkyydm()));
		    	susp.setYwztmc(this.systemDao.getCodeValue("120008",susp.getYwzt()));
		    	susp.setJlztmc(this.systemDao.getCodeValue("120002",susp.getJlzt()));
		    	susp.setXxlymc(this.systemDao.getCodeValue("120012",susp.getXxly()));
		    	susp.setBjztmc(this.systemDao.getCodeValue("130009",susp.getBjzt()));
		    	
		    	String[] bkfw=susp.getBkfw().split(",");
		    	StringBuffer sb =new StringBuffer();
                List<String> l = this.systemDao.getCityNames(bkfw);
                for(String s:l){
                	sb.append(s+",");
                }
		    	susp.setBkfwmc(sb.substring(0,(sb.length()-1)));
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
