package com.sunshine.monitor.system.analysis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.util.picws.PicWSInvokeTool;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.LikeVehiclesDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.LikeVehiclesManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Service("likeManager")
public class LikeVehiclesManagerImpl implements LikeVehiclesManager {
	
	@Autowired
	@Qualifier("likeDao")
	private LikeVehiclesDao likeDao;
	
	@Autowired
	@Qualifier("likeSCSDao")
	private LikeVehiclesDao likeSCSDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	private SystemManager systemManager;
	@Autowired
	private DirectDao directDao;
	
	@Autowired
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;
	
	protected Logger debugLogger = LoggerFactory.getLogger(LikeVehiclesManagerImpl.class);

	public List<Map<String,Object>> queryForLikeExt(VehPassrec veh, Map<String, Object> filter) throws Exception{
		veh.setKdbh("'"+veh.getKdbh().replace(",", "','")+"'");
		//List list = this.likeSCSDao.queryForLikeExt(veh, filter);
		List list = this.clickHouseRepos.queryForLikeExt(veh, filter);
		List<CarKey> cars = new ArrayList<CarKey>();
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			CarKey car = new CarKey();	
			car.setCarNo(map.get("HPHM").toString());
        	car.setCarType(map.get("HPZL").toString());
        	cars.add(car);
			map.put("HPZLMC",this.systemDao.getCodeValue("030107",map.get("HPZL")==null?"":map.get("HPZL").toString()));
			map.put("HPYSMC",this.systemDao.getCodeValue("031001",map.get("HPYS")==null?"":map.get("HPYS").toString()));
		}
		 //获取违法关联数      
		 try {
			//Map<CarKey,Integer> wfxx = new HashMap<>();
			Map<CarKey,Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
			//翻译字段
		    for(int i=0;i<list.size();i++){
		        Map<String,Object> map = (Map<String, Object>) list.get(i);
				map.put("wfxx", wfxx.get(
						 new CarKey(map.get("HPHM").toString(), map.get("HPZL").toString())
			    ));
		    }
		} catch (Exception e) {
			debugLogger.warn("违法库查询",e);
		}
		return list;
	}
	
	@Override
	public Integer getForLikeTotal(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		Integer count = 0;
		veh.setKdbh("'"+veh.getKdbh().replace(",", "','")+"'");
		//count = this.likeSCSDao.getForLikeTotal(veh, filter);
		count = this.clickHouseRepos.getForLikeTotal(veh, filter);
		return count;
	}
	
	//按条件获取号牌号码和过车数
	@SuppressWarnings("unchecked")
	public Map<String,Object> queryForLike(VehPassrec veh,Map<String,Object> filter) throws Exception {
	//	return this.likeDao.queryForLike(veh, filter);
		veh.setKdbh("'"+veh.getKdbh().replace(",", "','")+"'");
		Map result=this.likeSCSDao.queryForLike(veh, filter);
		List list=(List) result.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			CarKey car = new CarKey();	
			car.setCarNo(map.get("hphm").toString());
        	car.setCarType(map.get("hpzl").toString());
        	cars.add(car);
			map.put("HPZLMC",this.systemDao.getCodeValue("030107",map.get("HPZL")==null?"":map.get("HPZL").toString()));
			map.put("HPYSMC",this.systemDao.getCodeValue("031001",map.get("HPYS")==null?"":map.get("HPYS").toString()));
		}
		 //获取违法关联数      
		 try {
			//Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
			Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
			    //翻译字段
			    for(int i=0;i<list.size();i++){
			        Map<String,Object> map = (Map<String, Object>) list.get(i);
					map.put("wfxx", wfxx.get(
							 new CarKey(map.get("hphm").toString(), map.get("hpzl").toString())
				    ));
			    }
		} catch (Exception e) {
			debugLogger.warn("违法库查询",e);
		}
       		
		
		return result;
	}
	
	//根据号牌号码和部分条件获取过车的轨迹列表
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForLikeList(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		veh.setKdbh("'"+veh.getKdbh().replace(",", "','")+"'");
		long start = System.currentTimeMillis();
		//Map map =this.likeSCSDao.queryForLikePic(veh, filter);
		Map map =this.clickHouseRepos.queryForLikePic(veh, filter);
		long end = System.currentTimeMillis();
		debugLogger.info("其他地市图片请求总耗时-(" + (end-start) + "毫秒)");
		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
		
		// 长沙图片处理
		List<Map<String, Object>> piclist = new ArrayList();
		String changshaKd = "4301";
		
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
			result.put("HPYS", systemManager.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("XZQH", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			result.put("KDBHMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			result.put("FXBH", this.directDao.getOldDirectName(result.get("FXBH").toString()));
			
//			// 长沙图片处理
//			String kdbh = result.get("KDBH")==null?"":result.get("KDBH").toString();
//			if(changshaKd.equals(kdbh.substring(0, 4)) && result.get("tp1")!=null&&!"".equals(result.get("tp1"))){
//				Map<String,Object> temp = new HashMap<String, Object>();
//				temp.put("tp1", result.get("tp1").toString());
//				piclist.add(temp);
//			}
		}
		
//		if(piclist.size()>0){
//			long startTime = System.currentTimeMillis();
//			List tplist = new PicWSInvokeTool().getPicMil(piclist);
//			long endTime = System.currentTimeMillis();
//			debugLogger.info("长沙市图片请求总耗时-(" + (endTime-startTime) + "毫秒)");
//			for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
//				Map<String,Object> result=i.next();
//				// 长沙图片处理
//				for(Iterator<Map<String,Object>> picit=tplist.iterator();picit.hasNext();){
//					Map<String,Object> picmap=picit.next();
//					if(picmap.containsKey(result.get("tp1"))){
//						result.put("tp1",picmap.get(result.get("tp1")));
//					}
//				}
//				
//			}
//		}
		
		return map;
	}
	
	//根据号牌号码和部分条件获取过车的轨迹视图
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForLikePic(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		veh.setKdbh("'"+veh.getKdbh().replace(",", "','")+"'");
		//return this.likeDao.queryForLikePic(veh, filter);
		//Map result = this.likeSCSDao.queryForLikePic(veh, filter);
		Map result = this.clickHouseRepos.queryForLikePic(veh, filter);
		List list =(List)result.get("rows");
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			map.put("NUM", (Integer.parseInt(filter.get("page").toString())-1)*Integer.parseInt(filter.get("rows").toString())+(i+1));
			map.put("HPYS", systemManager.getCodeValue("031001", map.get("HPYS").toString()));
			map.put("XZQH", this.systemDao.getDistrictNameByXzqh(map.get("KDBH").toString().substring(0, 6)));
			map.put("KDMC", this.gateDao.getOldGateName(map.get("KDBH").toString()));
			map.put("FXBH", this.directDao.getOldDirectName(map.get("FXBH").toString()));
	
		}
		
		return result;
	}
	
	//获取行政区划
	@SuppressWarnings("unchecked")
	public List getXzqh() {
		return this.likeDao.getXzqh();
	}
	
	//获取卡点名称
	@SuppressWarnings("unchecked")
	public List getKdmc() {
		return this.likeDao.getKdmc();
	}
	
	//获取最后一次过车记录
	@SuppressWarnings("unchecked")
	public Map queryForLikeLast(VehPassrec veh) throws Exception {
		Map map=null;
		try{
			veh.setKdbh("'"+veh.getKdbh().replace(",", "','")+"'");
			//map= (Map) this.likeSCSDao.queryForLikeLast(veh).get(0);
			map= (Map) this.clickHouseRepos.queryForLikeLast(veh).get(0);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
		//return this.likeSCSDao.queryForLikeLast(veh);
	}
	
	//获取卡点名称
	public String getKdmcByKdbh(String kdbh) throws Exception {
		return this.likeDao.getKdmcByKdbh(kdbh);
	}
	//获取行政区划前6位
	public String getXzqhByKdbh6(String kdbh6) throws Exception {
		return this.likeDao.getXzqhByKdbh6(kdbh6);
	}
	
	//分页查询
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForLikePage(Map<String, Object> filter)
			throws Exception {
		Map result=this.likeSCSDao.queryForLikePage(filter);
		List list=(List) result.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			CarKey car = new CarKey();	
			car.setCarNo(map.get("hphm").toString());
        	car.setCarType(map.get("hpzl").toString());
        	cars.add(car);
			map.put("HPZLMC",this.systemDao.getCodeValue("030107",map.get("HPZL")==null?"":map.get("HPZL").toString()));
			map.put("HPYSMC",this.systemDao.getCodeValue("031001",map.get("HPYS")==null?"":map.get("HPYS").toString()));
		}
		 //获取违法关联数      
		 //Map<CarKey,Integer> wfxx=this.queryListDao.getViolationCount(cars);
		 Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
	        //翻译字段
	        for(int i=0;i<list.size();i++){
	            Map<String,Object> map = (Map<String, Object>) list.get(i);
				map.put("wfxx", wfxx.get(
						 new CarKey(map.get("hphm").toString(), map.get("hpzl").toString())
			    ));
	        }
		return result;
	}
	
	//获取方向名称
	@SuppressWarnings("unchecked")
	public List getFxmc() {
		return this.likeDao.getFxmc();
	}
	
	//车辆轨迹查询
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryCarTrack(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
	//	return this.likeDao.queryForLike(veh, filter);
		veh.setKdbh("'"+veh.getKdbh().replace(",", "','")+"'");
		Map result=this.likeSCSDao.queryForCarTrack(veh, filter);
		List list=(List) result.get("rows");
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			map.put("HPZLMC",this.systemDao.getCodeValue("030107",map.get("HPZL")==null?"":map.get("HPZL").toString()));
			map.put("HPYSMC",this.systemDao.getCodeValue("031001",map.get("HPYS")==null?"":map.get("HPYS").toString()));
			map.put("XZQH", this.systemDao.getDistrictNameByXzqh(map.get("KDBH").toString().substring(0, 6)));
			map.put("KDMC", this.gateDao.getOldGateName(map.get("KDBH").toString()));
			map.put("FXMC", this.directDao.getOldDirectName(map.get("FXBH").toString()));
		}
		return result;
	}
	
	//车辆轨迹查询 分页查询
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryCarTrackPage(Map<String, Object> filter)
			throws Exception {
		Map result = this.likeSCSDao.queryCarTrackPage(filter);
		List list = (List) result.get("rows");
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			map.put("HPZLMC", this.systemDao.getCodeValue("030107", map
					.get("HPZL") == null ? "" : map.get("HPZL").toString()));
			map.put("HPYSMC", this.systemDao.getCodeValue("031001", map
					.get("HPYS") == null ? "" : map.get("HPYS").toString()));
			map.put("XZQH", this.systemDao.getDistrictNameByXzqh(map
					.get("KDBH").toString().substring(0, 6)));
			map.put("KDMC", this.gateDao.getOldGateName(map.get("KDBH")
					.toString()));
			map.put("FXMC", this.directDao.getOldDirectName(map.get("FXBH")
					.toString()));
		}
		return result;
	}

}
