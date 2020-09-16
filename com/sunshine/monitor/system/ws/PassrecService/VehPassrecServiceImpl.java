package com.sunshine.monitor.system.ws.PassrecService;

import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.veh.dao.PassrecDao;
import com.sunshine.monitor.system.ws.PassrecService.bean.PassrecEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.PassrecService.VehPassrecService", serviceName = "VehPassrecService")
@Component("vehPassrecServiceImpl")
public class VehPassrecServiceImpl implements VehPassrecService {

	@Autowired
	@Qualifier("passrecDao")
	private PassrecDao passrecDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;

	public String queryVehPassrec(String conditions) {
		String result="";
		Map<String,Object> map=null;
		if(conditions!=null&&conditions.length()>0){
		   map=JSONObject.fromObject(conditions);
		}
		try {
			Map<String, Object>  temp = this.passrecDao.findPagePassrecForMap(map,PassrecEntity.class);	
		    List<PassrecEntity> list = (List<PassrecEntity>) temp.get("rows");
		    for(int i = 0;i<list.size();i++){
		    	PassrecEntity veh = list.get(i);
		    	veh.setKdmc(this.gateManager.getGateName(veh.getKdbh()));
		    	veh.setFxmc(this.gateManager.getDirectName(veh.getFxbh()));
		    	veh.setHpzlmc(this.systemDao.getCodeValue("030107",veh.getHpzl()));
		    	veh.setCwhpysmc(this.systemDao.getCodeValue("031001",veh.getCwhpys()));
                veh.setHpyzmc(this.systemDao.getCodeValue("031003",veh.getHpyz()));
		    	veh.setHpysmc(this.systemDao.getCodeValue("031001",veh.getHpys()));
		    	veh.setCllxmc(this.systemDao.getCodeValue("030104",veh.getCllx()));
		    	veh.setXsztmc(this.systemDao.getCodeValue("110005",veh.getXszt()));
		    	veh.setCsysmc(this.systemDao.getCodeValue("030108",veh.getCsys()));

		    }
		    temp.put("rows",list);
			JSONObject json = JSONObject.fromObject(temp);
			result=json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return result;
	}

}
