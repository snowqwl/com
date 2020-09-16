package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.dao.AnotherPlaceDao;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.AnotherPlaceManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Service("anotherPlaceManager")
public class AnotherPlaceManagerImpl implements AnotherPlaceManager {
	@Autowired
	@Qualifier("anotherPlaceDao")
	private AnotherPlaceDao AnotherPlaceDao;
	
	@Autowired
	@Qualifier("anotherSCSPlaceDao")
	private AnotherPlaceDao AnotherPlaceSCSDao;
	

	@Autowired
	private DirectDao directDao;
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;
	
	public Map<String, Object> queryForAnotherPlaceList(VehPassrec veh,
			Map<String, Object> filter, String local) throws Exception {
		
		
		
		Map<String,Object> map=this.AnotherPlaceDao.queryForAnotherPlaceList(veh, filter, local);
		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
			result.put("HPYS", systemManager.getCodeValue("031001", result.get("HPYS").toString()));
		}
		return map;
	}
	public Map<String, Object> getList(Map<String, Object> filter)
			throws Exception {
		
		if(filter.get("rows")==null){
			filter.put("rows", 2);
		}if(filter.get("page")==null){
			filter.put("page", 1);
		}
		Map dangerousMap= this.AnotherPlaceDao.queryDangerous(filter);
		List dangerousList=(List)dangerousMap.get("rows");
		if(dangerousList.size()>0){
		Map dangerousCityMap=(Map)dangerousList.get(0);
			filter.put("dangerousCity", dangerousCityMap.get("CITY").toString().split(","));
		}		
		Map<String,Object> resultmap =  this.AnotherPlaceSCSDao.getList(filter);		
		return resultmap;
	
	}
	public String saveZcd(String taskname,Map<String, Object> filter) throws Exception {
		//return this.AnotherPlaceSCSDao.saveZcd(taskname,filter);
		return this.clickHouseRepos.saveZcd(taskname,filter);
	}
	
	public Map findtask(String xh, Map<String, Object> filter, String taskname)
			throws Exception {
		
		return this.AnotherPlaceDao.findtask(xh, filter, taskname);
	}
	
	public Map findZcdList(String xh, Map<String, Object> filter)
			throws Exception {
		return this.AnotherPlaceDao.findZcdList(xh, filter);
	}
	public Map queryDetilList(String xh, Map<String, Object> filter, String sign)
			throws Exception {
		
		//查询高危地区
		String dangerousCity[]=null;
		/*if(filter.get("dangerousId")!=null){
			if(filter.get("dangerousId").toString().length()>0&&!filter.get("dangerousId").toString().equals("1")){
				filter.put("arrayid", filter.get("dangerousId").toString());
				Map dangerousMap= this.AnotherPlaceDao.queryDangerous(filter);
				List dangerousList=(List)dangerousMap.get("rows");
				if(dangerousList.size()>0){
				 Map dangerousCityMap=(Map)dangerousList.get(0);
				 dangerousCity= dangerousCityMap.get("CITY").toString().split(",");
				}
			}
		}*/
		if(filter.get("dangerousMatchCity")!=null){
			if(filter.get("dangerousMatchCity").toString().length()>0){
				dangerousCity=filter.get("dangerousMatchCity").toString().split(",");
			}
			
		}
		
		Map map =this.AnotherPlaceSCSDao.queryDetilList(xh, filter, sign);
		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
			CarKey car = new CarKey();	
			car.setCarNo(result.get("hphm").toString());
        	car.setCarType(result.get("hpzl").toString());
        	cars.add(car);
			result.put("hpysmc", systemManager.getCodeValue("031001", result.get("hpys").toString()));
			//匹配高危地区
			/*if(dangerousCity!=null){
				for(int j=0;j<dangerousCity.length;j++){
					if(result.get("hphm").toString().length()>1){
						if(result.get("hphm").toString().substring(0, 2).equals(dangerousCity[j])){
							//result.put("hphm","<font color='red'>"+result.get("hphm").toString()+"</font>");
							result.put("yjflag","1");
						}
					}
				}
			}*/
		}
		 //获取违法关联数      
		 //Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
		 Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		 for(int i=0;i<list.size();i++){
	            Map<String,Object> wfmap = (Map<String, Object>) list.get(i);
	            wfmap.put("wfxx", wfxx.get(
						 new CarKey(wfmap.get("hphm").toString(), wfmap.get("hpzl").toString())
			    ));
	        }
        map.put("rows", list);
		return map;
	}
	public Map getListForpage(Map<Object, String> filter) throws Exception {

		return this.AnotherPlaceSCSDao.getListForpage(filter);
	}
	public Map queryDetilList(String xh,Map filter) throws Exception {
		//查询高危地区
		String dangerousCity[]=null;
		/*if(filter.get("dangerousId")!=null){
			if(filter.get("dangerousId").toString().length()>0&&!filter.get("dangerousId").toString().equals("1")){
				filter.put("arrayid", filter.get("dangerousId").toString());
				Map dangerousMap= this.AnotherPlaceDao.queryDangerous(filter);
				List dangerousList=(List)dangerousMap.get("rows");
				if(dangerousList.size()>0){
				 Map dangerousCityMap=(Map)dangerousList.get(0);
				 dangerousCity= dangerousCityMap.get("CITY").toString().split(",");
				}
			}
		}*/
		if(filter.get("dangerousMatchCity")!=null){
			if(filter.get("dangerousMatchCity").toString().length()>0){
				dangerousCity=filter.get("dangerousMatchCity").toString().split(",");
			}
			
		}
		
		Map map =this.AnotherPlaceSCSDao.queryDetilList(filter);
		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
			CarKey car = new CarKey();	
			car.setCarNo(result.get("hphm").toString());
        	car.setCarType(result.get("hpzl").toString());
        	cars.add(car);
			result.put("hpysmc", systemManager.getCodeValue("031001", result.get("hpys").toString()));
			//匹配高危地区
			if(dangerousCity!=null){
				for(int j=0;j<dangerousCity.length;j++){
					if(result.get("hphm").toString().length()>1){
						if(result.get("hphm").toString().substring(0, 2).equals(dangerousCity[j])){
							result.put("yjflag","1");
						}
					}
				}
			}
		}
		 //获取违法关联数      
		 //Map<CarKey,Integer> wfxx=this.queryListDao.getViolationCount(cars);
		Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		 for(int i=0;i<list.size();i++){
	            Map<String,Object> wfmap = (Map<String, Object>) list.get(i);
	            wfmap.put("wfxx", wfxx.get(
						 new CarKey(wfmap.get("hphm").toString(), wfmap.get("hpzl").toString())
			    ));
	        }
		return map;
	}
	
	public Map queryDangerousDetailList(String xh, Map<String, Object> filter,
			String sign) throws Exception {
		//查询高危地区
		String dangerousCity=null;
		String dangerousId=null;
		String dangerousName=null;
		if(filter.get("dangerousId")!=null){
			if(filter.get("dangerousId").toString().length()>0&&!filter.get("dangerousId").toString().equals("1")){
				filter.put("arrayid", filter.get("dangerousId").toString());
				Map dangerousMap= this.AnotherPlaceDao.queryDangerous(filter);
				List dangerousList=(List)dangerousMap.get("rows");
				if(dangerousList.size()>0){
				 Map dangerousCityMap=(Map)dangerousList.get(0);
				 dangerousCity= dangerousCityMap.get("CITY").toString();
				 dangerousId=dangerousCityMap.get("ID").toString();
				 dangerousName=dangerousCityMap.get("ARRAYNAME").toString();
				 if(filter.get("dangerousMatchCity")!=null){
						if(filter.get("dangerousMatchCity").toString().length()>0){
							 filter.put("dangerousCity", filter.get("dangerousMatchCity").toString().replace(",", "','"));
						}
					}
				// filter.put("dangerousCity", dangerousCityMap.get("CITY").toString().replace(",", "','"));
				}
			}
		}
		
		
		
		Map map = this.AnotherPlaceSCSDao.queryDangerousDetailList(xh, filter, sign);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for (Iterator<Map<String, Object>> i = list.iterator(); i.hasNext();) {
			Map<String, Object> result = i.next();
			CarKey car = new CarKey();
			car.setCarNo(result.get("hphm").toString());
			car.setCarType(result.get("hpzl").toString());
			cars.add(car);
			result.put("hpysmc", systemManager.getCodeValue("031001", result.get("hpys").toString()));
			result.put("hpzlmc", systemManager.getCodeValue("030107", result.get("hpzl").toString()));
			//放进高危地区条件
			result.put("dangerousName", dangerousName);
			result.put("dangerousId", dangerousId);
			result.put("dangerousCity", dangerousCity);
		}
		// 获取违法关联数
		//Map<CarKey, Integer> wfxx = this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey, Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> wfmap = (Map<String, Object>) list.get(i);
			wfmap.put("wfxx", wfxx.get(new CarKey(wfmap.get("hphm").toString(),wfmap.get("hpzl").toString())));
		}
		// map.put("rows", list);
		return map;
	}
	
	public Map queryDangerousDetailList(Map filter) throws Exception {
		//查询高危地区
		String dangerousCity=null;
		String dangerousId=null;
		String dangerousName=null;
		if(filter.get("dangerousId")!=null){
			if(filter.get("dangerousId").toString().length()>0&&!filter.get("dangerousId").toString().equals("1")){
				filter.put("arrayid", filter.get("dangerousId").toString());
				Map dangerousMap= this.AnotherPlaceDao.queryDangerous(filter);
				List dangerousList=(List)dangerousMap.get("rows");
				if(dangerousList.size()>0){
				 Map dangerousCityMap=(Map)dangerousList.get(0);
				 dangerousCity= dangerousCityMap.get("CITY").toString();
				 dangerousId=dangerousCityMap.get("ID").toString();
				 dangerousName=dangerousCityMap.get("ARRAYNAME").toString();
				 if(filter.get("dangerousMatchCity")!=null){
						if(filter.get("dangerousMatchCity").toString().length()>0){
							 filter.put("dangerousCity", filter.get("dangerousMatchCity").toString().replace(",", "','"));
						}
						
					}
				// filter.put("dangerousCity", dangerousCityMap.get("CITY").toString().replace(",", "','"));
				}
			}
		}
		Map map =this.AnotherPlaceSCSDao.queryDangerousDetailList(filter);
		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
			CarKey car = new CarKey();	
			car.setCarNo(result.get("hphm").toString());
        	car.setCarType(result.get("hpzl").toString());
        	cars.add(car);
			result.put("hpysmc", systemManager.getCodeValue("031001", result.get("hpys").toString()));
			result.put("hpzlmc", systemManager.getCodeValue("030107", result.get("hpzl").toString()));
			//放进高危地区条件
			result.put("dangerousName", dangerousName);
			result.put("dangerousId", dangerousId);
			result.put("dangerousCity", dangerousCity);
		}
		 //获取违法关联数      
		 //Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
		 Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		 for(int i=0;i<list.size();i++){
	            Map<String,Object> wfmap = (Map<String, Object>) list.get(i);
	            wfmap.put("wfxx", wfxx.get(
						 new CarKey(wfmap.get("hphm").toString(), wfmap.get("hpzl").toString())
			    ));
	        }
		return map;
	}
	
	public Map queryForDetilList(Map filter) throws Exception {
		//Map map = this.AnotherPlaceSCSDao.queryForDetilList(filter);
		Map map = this.clickHouseRepos.queryAnotherPlaceDetil(filter);
		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
		
			//result.put("HPYS", systemManager.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("XZQH", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			result.put("KDBHMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			result.put("FXBH", this.directDao.getOldDirectName(result.get("FXBH").toString()));
			//result.put("GCSJ", sdf.format(result.get("GCSJ")));
			result.put("HPZLMC", systemManager.getCodeValue("030107", result.get("HPZL")==null?"":result.get("hpzl").toString()));
		}
		return map;
	}
	public String sureDeleteTask(Map filter) throws Exception {
		return this.AnotherPlaceDao.sureDeleteTask(filter);
	}
	public List cityTree() throws Exception {
		return this.AnotherPlaceDao.cityTree();
	}
	public int insertDangerousArray(Map filter) throws Exception {
		return this.AnotherPlaceDao.insertDangerousArray(filter);
	}
	
	public Map queryDangerous(Map filter) throws Exception {
		//限定高危组组数
		if(filter.get("rows")==null){
			filter.put("rows", 30);
		}if(filter.get("page")==null){
			filter.put("page", 1);
		}
		return this.AnotherPlaceDao.queryDangerous(filter);
	}
	
	public int deleteById(Map filter) throws Exception {
		return this.AnotherPlaceDao.deleteById(filter);
	}
	@Override
	public int getForDetilTotal(String xh, Map<String, Object> filter,
			String sign) throws Exception {
		try{
			//return AnotherPlaceSCSDao.getForDetilTotal(xh, filter, sign);
			return clickHouseRepos.getAnotherPlaceDetilTotal(xh, filter, sign);
		} catch(Exception e){
			return 0;
		}
	}
	
	@Override
	public List<Map<String, Object>> queryDetilListExt(String xh,
			Map<String, Object> filter, String sign) throws Exception {
		//查询高危地区
		String dangerousCity[]=null;
		if(filter.get("dangerousMatchCity")!=null){
			if(filter.get("dangerousMatchCity").toString().length()>0){
				dangerousCity=filter.get("dangerousMatchCity").toString().split(",");
			}
			
		}
		
		//List<Map<String,Object>> list = this.AnotherPlaceSCSDao.queryDetilListExt(xh, filter, sign);
		List<Map<String,Object>> list = this.clickHouseRepos.queryAnotherPlaceDetilList(xh, filter, sign);
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
			CarKey car = new CarKey();	
			car.setCarNo(result.get("hphm").toString());
        	car.setCarType(result.get("hpzl").toString());
        	cars.add(car);
			//result.put("hpysmc", systemManager.getCodeValue("031001", result.get("hpys").toString()));
        	result.put("hpzlmc", systemManager.getCodeValue("030107", result.get("hpzl").toString()));
		}
		
		 //获取违法关联数      
		 //Map<CarKey,Integer> wfxx = new HashMap<>();
		 Map<CarKey,Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
		 for(int i=0;i<list.size();i++){
	            Map<String,Object> wfmap = (Map<String, Object>) list.get(i);
	            wfmap.put("wfxx", wfxx.get(
						 new CarKey(wfmap.get("hphm").toString(), wfmap.get("hpzl").toString())
			    ));
	        }
		return Common.orderListByWfcs(list, "wfxx");
	}
	@Override
	public int getForDetilDangerousTotal(String xh ,Map<String ,Object> filter,String sign)
			throws Exception {
		try{
			return AnotherPlaceSCSDao.getForDetilDangerousTotal(xh, filter, sign);
		} catch(Exception e){
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryDangerousDetailListExt(String xh ,Map<String ,Object> filter,String sign)throws Exception {
		//查询高危地区
		String dangerousCity=null;
		String dangerousId=null;
		String dangerousName=null;
		if(filter.get("dangerousId")!=null){
			if(filter.get("dangerousId").toString().length()>0&&!filter.get("dangerousId").toString().equals("1")){
				filter.put("arrayid", filter.get("dangerousId").toString());
				filter.put("stat", "1");
				Map<String, Object> dangerousMap= this.AnotherPlaceDao.queryDangerous(filter);
				List<Map<String, Object>> dangerousList=(List<Map<String, Object>>)dangerousMap.get("rows");
				if(dangerousList.size()>0){
				 Map<String, Object> dangerousCityMap=(Map<String, Object>)dangerousList.get(0);
				 dangerousCity= dangerousCityMap.get("CITY").toString();
				 dangerousId=dangerousCityMap.get("ID").toString();
				 dangerousName=dangerousCityMap.get("ARRAYNAME").toString();
				 if(filter.get("dangerousMatchCity")!=null){
						if(filter.get("dangerousMatchCity").toString().length()>0){
							 filter.put("dangerousCity", filter.get("dangerousMatchCity").toString().replace(",", "','"));
						}
					}
				}
			}
		}
		
		
		
		List<Map<String, Object>> list = this.AnotherPlaceSCSDao.queryDangerousDetailListExt(xh, filter, sign);
		List<CarKey> cars = new ArrayList<CarKey>();
		for (Iterator<Map<String, Object>> i = list.iterator(); i.hasNext();) {
			Map<String, Object> result = i.next();
			CarKey car = new CarKey();
			car.setCarNo(result.get("hphm").toString());
			car.setCarType(result.get("hpzl").toString());
			cars.add(car);
			result.put("hpysmc", systemManager.getCodeValue("031001", result.get("hpys").toString()));
			result.put("hpzlmc", systemManager.getCodeValue("030107", result.get("hpzl").toString()));
			//放进高危地区条件
			result.put("dangerousName", dangerousName);
			result.put("dangerousId", dangerousId);
			result.put("dangerousCity", dangerousCity);
		}
		// 获取违法关联数
		//Map<CarKey, Integer> wfxx = this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey, Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> wfmap = (Map<String, Object>) list.get(i);
			wfmap.put("wfxx", wfxx.get(new CarKey(wfmap.get("hphm").toString(),wfmap.get("hpzl").toString())));
		}
		return Common.orderListByWfcs(list, "wfxx");
	}
	@Override
	public Map<String, Object> getListExt(Map<String, Object> filter)
			throws Exception {
		if(filter.get("rows")==null){
			filter.put("rows", 2);
		}if(filter.get("page")==null){
			filter.put("page", 1);
		}
		Map dangerousMap= this.AnotherPlaceDao.queryDangerous(filter);
		List dangerousList=(List)dangerousMap.get("rows");
		if(dangerousList.size()>0){
		Map dangerousCityMap=(Map)dangerousList.get(0);
			filter.put("dangerousCity", dangerousCityMap.get("CITY").toString().split(","));
		}		
		
		//Map<String,Object> resultmap = this.AnotherPlaceSCSDao.getListExt(filter);		
		Map<String,Object> resultmap = this.clickHouseRepos.getAnotherPlaceListExt(filter);
		return resultmap;
	}
	
	@Override
	public int getListTotal(Map<String, Object> filter) throws Exception {
		try{
			//return AnotherPlaceSCSDao.getListTotal(filter);
			return clickHouseRepos.getAnotherPlaceListTotal(filter);
		} catch(Exception e){
			return 0;
		}
	}
	
}
