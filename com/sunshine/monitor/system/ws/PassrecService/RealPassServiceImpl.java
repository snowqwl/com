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
import com.sunshine.monitor.system.veh.dao.RealDao;
import com.sunshine.monitor.system.ws.PassrecService.bean.RealPassEntity;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.PassrecService.RealPassService", serviceName = "RealPassService")
@Component("realPassServiceImpl")
public class RealPassServiceImpl implements RealPassService {

	@Autowired
	@Qualifier("realDao")
	private RealDao realDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;

	public String queryRealPass(String conditions) {		
		String result="";
		Map<String,Object> map=null;
		if(conditions!=null&&conditions.length()>0){
		   map=JSONObject.fromObject(conditions);
		}
		try {
			Map<String, Object>  temp = this.realDao.findPageRealForMap(map,RealPassEntity.class);
		    List<RealPassEntity> list = (List<RealPassEntity>) temp.get("rows");
		    for(int i = 0;i<list.size();i++){
		    	RealPassEntity real = list.get(i);
		    	real.setKdmc(this.gateManager.getGateName(real.getKdbh()));
		    	real.setFxmc(this.gateManager.getDirectName(real.getFxbh()));
		    	real.setCdlxmc(this.systemDao.getCodeValue("156004", real.getCdlx()));
		    	real.setHpzlmc(this.systemDao.getCodeValue("030107",real.getHpzl()));
		    	real.setHpysmc(this.systemDao.getCodeValue("031001",real.getHpys()));
		    	real.setCllxmc(this.systemDao.getCodeValue("030104",real.getCllx()));
                real.setHpyzmc(this.systemDao.getCodeValue("031003",real.getHpyz()));
		    	real.setCsysmc(this.systemDao.getCodeValue("030108",real.getCsys()));
		    	real.setCwhpysmc(this.systemDao.getCodeValue("031001",real.getCwhpys()));
		    	real.setXsztmc(this.systemDao.getCodeValue("110005",real.getXszt()));
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
