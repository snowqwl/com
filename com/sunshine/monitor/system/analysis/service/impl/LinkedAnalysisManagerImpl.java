package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.dao.IllegalSCSDao;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.LinkedAnalysisDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.dao.VehicleDao;
import com.sunshine.monitor.system.analysis.dao.ZykVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.LinkedAnalysisManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.dao.QueryListDao;

@Service("linkedManager")
public class LinkedAnalysisManagerImpl implements LinkedAnalysisManager {
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	@Qualifier("linkedDao")
	private LinkedAnalysisDao linkedDao;
	
	@Autowired
	@Qualifier("vehicleDao")
	private VehicleDao vehicleDao;
	
	@Autowired
	@Qualifier("zykVehPassrecDao")
	private ZykVehPassrecDao zykVehPassrecDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("illegalSCSDao")
	private IllegalSCSDao illegalSCSDao;
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;

	public Map<String, Object> findLinkForPage(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		veh.setKdbh("'"+veh.getKdbh().replace(",","','")+"'");
		Map<String,Object> result=this.linkedDao.findLinkForPage(veh, filter);
		List<Map<String,Object>> list=(List<Map<String,Object>>) result.get("rows");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=(Map<String,Object>)list.get(i);
			CarKey car = new CarKey();
			car.setCarNo(map.get("hphm").toString());
        	car.setCarType(map.get("hpzl").toString());
        	cars.add(car);
        	// map.put("zhgcsj",sdf.format(map.get("zhgcsj")));
			map.put("hpzlmc",this.systemDao.getCodeValue("030107",map.get("hpzl")==null?"":map.get("hpzl").toString()));
			map.put("hpysmc",this.systemDao.getCodeValue("031001",map.get("hpys")==null?"":map.get("hpys").toString()));
			//map.put("csysmc",this.systemDao.getCodeValue("030108",map.get("csys")==null?"":map.get("csys").toString()));
		}
		 //获取违法关联数      
		 //Map<CarKey,Integer> wfxx=this.illegalSCSDao.getViolationCount(cars);
		Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		
		 //获取机动车信息
		 Map<CarKey,VehicleEntity> vehInfo = this.zykVehPassrecDao.getVehicleList(cars);
		 
	     for(Map<String,Object> map:list){
	    	 String hphm = map.get("hphm").toString();
	    	 String hpzl = map.get("hpzl").toString();
	    	 VehicleEntity info = vehInfo.get(new CarKey(hphm,hpzl));
	    	 map.put("wfxx", wfxx.get(new CarKey(hphm,hpzl)));
	    	 map.put("clpp1", "");
	    	 map.put("csysmc", "");
	    	 if(info!=null){
	    		 if(info.getFzjg().equals(hphm.substring(0,2))){
	    			 map.put("clpp1", info.getClpp1());
	    			 map.put("csysmc",this.systemDao.getCodeValue("030108",info.getCsys()));
	    		 }
	    	 }
	     }
	     result.put("rows",list);
		return result;
	}

	public Map<String, Object> findLinkDetailForPage(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		Map<String,Object> result = this.clickHouseRepos.findLinkDetailForPage(veh, filter);
		 //翻译字段
		List<ScsVehPassrec> rows = (List<ScsVehPassrec>) result.get("rows");
		for(int i=0;i<rows.size();i++){
			ScsVehPassrec scs = rows.get(i);
			scs.setXzqhmc(this.systemDao.getDistrictNameByXzqh(scs.getKdbh().substring(0,6)));
			scs.setKdmc((String) ((scs.getKdbh()!=null)?this.systemDao.getField("code_Gate","kdbh","kdmc",scs.getKdbh()):""));
			scs.setHpysmc((scs.getHpys()!=null)?this.systemDao.getCodeValue("031001",scs.getHpys()):"");
			scs.setFxmc((String) ((scs.getFxbh()!=null)?this.systemDao.getField("code_gate_extend","fxbh","fxmc",scs.getFxbh()):""));
		}
        result.put("rows", rows);
		return result;
 	}

	public List<Map<String, Object>> getLinkList(String sessionId)
			throws Exception {
		return this.linkedDao.getLinkList(sessionId);
	}

	@Override
	public List<Map<String, Object>> findLinkForPageExt(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		if(veh.getKdbh()!=null){
			veh.setKdbh("'"+veh.getKdbh().replace(",","','")+"'");
		}
		//List<Map<String,Object>> list=this.linkedDao.findLinkForPageExt(veh, filter);
		List<Map<String,Object>> list = this.clickHouseRepos.findLinkForPageExt(veh, filter);
		if(list==null || list.size() == 0) {
			return null;
		}
		
		List<CarKey> cars = new ArrayList<CarKey>();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=(Map<String,Object>)list.get(i);
			CarKey car = new CarKey();
			car.setCarNo(map.get("hphm").toString());
        	car.setCarType(map.get("hpzl").toString());
        	cars.add(car);
			map.put("hpzlmc",this.systemDao.getCodeValue("030107",map.get("hpzl")==null?"":map.get("hpzl").toString()));
			map.put("hpysmc",this.systemDao.getCodeValue("031001",map.get("hpys")==null?"":map.get("hpys").toString()));
//        	map.put("zhgcsj",sdf.format(map.get("zhgcsj")));
//			map.put("csysmc",this.systemDao.getCodeValue("030108",map.get("csys")==null?"":map.get("csys").toString()));
		}
		
		 //获取违法关联数 
		 //Map<CarKey,Integer> wfxx = new HashMap<>();
		 Map<CarKey,Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
		 
		 //获取机动车信息
		 //Map<CarKey,VehicleEntity> vehInfo = new HashMap<>();
		 Map<CarKey,VehicleEntity> vehInfo = this.zykVehPassrecDao.getVehicleList(cars);

	     for(Map<String,Object> map:list){
	    	 String hphm = map.get("hphm").toString();
	    	 String hpzl = map.get("hpzl").toString();
	    	 VehicleEntity info = vehInfo.get(new CarKey(hphm,hpzl));
	    	 map.put("wfxx", wfxx.get(new CarKey(hphm,hpzl)));
	    	 map.put("clpp1", "");
	    	 map.put("csysmc", "");
	    	 if(info!=null){
	    		 if(info.getFzjg().equals(hphm.substring(0,2))){
	    			 map.put("clpp1", info.getClpp1());
	    			 map.put("csysmc",this.systemDao.getCodeValue("030108",info.getCsys()));
	    		 }
	    	 }
	     }
		return list;
	}

	@Override
	public int getForLinkTotal(ScsVehPassrec veh, Map<String, Object> filter)
			throws Exception {
		try{
			veh.setKdbh("'" + veh.getKdbh().replace(",", "','") + "'");
			//return linkedDao.getForLinkTotal(veh, filter);
			return clickHouseRepos.getForLinkTotal(veh, filter);
		} catch(Exception e){
			return 0;
		}
	}

}
