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
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.dao.WarningModelSCSDao;
import com.sunshine.monitor.system.analysis.service.WarningModelManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Service("warningManager")
@Transactional
public class WarningModelManagerImpl implements WarningModelManager {
	
	@Autowired
	@Qualifier("warningSCSDao")
	private WarningModelSCSDao warningSCSDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	private GateDao gateDao;
	
	@Autowired
	private DirectDao directDao;
	
	@Autowired
	@Qualifier("scsVehPassrecDao")
	private ScsVehPassrecDao scsVehPassrecDao;
	
	public Map queryWarningList(Map filter, VehPassrec veh) throws Exception {
		Map map = this.warningSCSDao.queryWarningList(filter, veh);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> queryList = (List)map.get("rows");
		if(queryList.size() == 0){
			return map;
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Map<String, Object> m : queryList){
			CarKey car = new CarKey();	
        	car.setCarNo(m.get("HPHM").toString());
        	car.setCarType(m.get("HPZL").toString());
        	cars.add(car);
		}
		//Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPZLMC",this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			String kkmc = result.get("kkbh").toString();
			result.put("KDMC", this.gateDao.getOldGateName(result.get("kkbh").toString()));
			//result.put("CLLXMC",this.systemDao.getCodeValue("030104",result.get("cllx")==null?"":result.get("cllx").toString()));
			//result.put("CSYSMC",this.systemDao.getCodeValue("030108",result.get("csys")==null?"":result.get("csys").toString()));
			//result.put("FXMC", this.directDao.getOldDirectName(veh.getFxbh()));
			//result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(veh.getKdbh().substring(0, 6)));
			result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM").toString(), result.get("HPZL").toString())));
			//result.put("WFXX", "2");
			result.put("GCSJ",sdf.format(result.get("gcsj")));
			list.add(result);
		}
		map.put("rows",list);
		return map;
	}
	
	public List<Map<String, Object>> queryWarningListExt(Map filter, VehPassrec veh) throws Exception {
		List<Map<String, Object>> queryList = this.warningSCSDao.queryWarningListExt(filter, veh);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(queryList.size() == 0){
			return queryList;
		}
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Map<String, Object> m : queryList){
			CarKey car = new CarKey();	
        	car.setCarNo(m.get("HPHM").toString());
        	car.setCarType(m.get("HPZL").toString());
        	cars.add(car);
		}
		//Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPZLMC",this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			//String kkmc = result.get("kdbh").toString();
			result.put("KDMC", this.gateDao.getOldGateName(result.get("kdbh").toString()));
			//result.put("CLLXMC",this.systemDao.getCodeValue("030104",result.get("cllx")==null?"":result.get("cllx").toString()));
			//result.put("CSYSMC",this.systemDao.getCodeValue("030108",result.get("csys")==null?"":result.get("csys").toString()));
			//result.put("FXMC", this.directDao.getOldDirectName(veh.getFxbh()));
			//result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(veh.getKdbh().substring(0, 6)));
			result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM").toString(), result.get("HPZL").toString())));
			//result.put("WFXX", "2");
			result.put("GCSJ",sdf.format(result.get("gcsj")));
		}
		return queryList;
	}
	
	public Integer queryWarningListTotal(Map filter, VehPassrec veh) throws Exception {
		int total = 0;
		total = this.warningSCSDao.queryWarningListTotal(filter, veh);
		return total;
	}

	
	public Map queryPass(Map filter) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map map = this.warningSCSDao.queryPass(filter);
		
		//map.putAll(wfmap);
		List<Map<String, Object>> queryList = (List)map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPZLMC",this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("hpys")==null?"":result.get("hpys").toString()));
			result.put("CLLXMC",this.systemDao.getCodeValue("030104",result.get("cllx")==null?"":result.get("cllx").toString()));
			result.put("CSYSMC",this.systemDao.getCodeValue("030108",result.get("csys")==null?"":result.get("csys").toString()));
			result.put("KDMC", this.gateDao.getOldGateName(result.get("kdbh").toString()));
			result.put("FXMC", this.directDao.getOldDirectName(result.get("fxbh").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("kdbh").toString().substring(0, 6)));
			//result.put("FXMC",this.systemDao.getField("code_gate_extend","fxbh","fxmc",result.get("fxbh").toString())==null?"":result.get("fxbh").toString());
			result.put("GCSJ",sdf.format(result.get("gcsj")));
			
			Map<String, Object> warningfilter = new HashMap<String, Object>();
			filter.put("hphm", result.get("hphm"));
			filter.put("hpzl", result.get("hpzl"));
			//Map mapwarning = this.warningSCSDao.queryOneIllegal(filter);
			Map mapwarning = this.illegalZYKDao.queryOneIllegal(filter);
			List<Map<String, Object>> queryList2 = (List)mapwarning.get("rows");
			List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>();
			for (Iterator i2 = queryList2.iterator(); i2.hasNext();) {
				Map<String, Object> result2 = (Map<String, Object>) i2.next();
				result.putAll(result2);
			}
			list.add(result);
		}
		map.put("rows",list);
		return map;
	}
	
	public Map queryOneIllegal(Map filter) throws Exception {
		//Map mapwarning = this.warningSCSDao.queryOneIllegal(filter);
		Map mapwarning = this.illegalZYKDao.queryOneIllegal(filter);
		List<Map<String, Object>> queryList = (List)mapwarning.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> map = (Map<String, Object>) i.next();
			String wfsj = map.get("WFSJ").toString();
			String wfsj_format = wfsj.substring(0, 4)+"-"+wfsj.substring(4, 6)+"-"+
								 wfsj.substring(6, 8)+" "+wfsj.substring(8, 10)+":"+wfsj.substring(10,12)+":"+wfsj.substring(12);
			map.put("wfsj",wfsj_format);
			
			String lrsj = map.get("LRSJ").toString();
			String lrsj_format = lrsj.substring(0, 4)+"-"+lrsj.substring(4, 6)+"-"+
								 lrsj.substring(6, 8)+" "+lrsj.substring(8, 10)+":"+lrsj.substring(10,12)+":"+lrsj.substring(12);
			map.put("lrsj",lrsj_format);
			
			String clsj = map.get("CLSJ")!=null?map.get("CLSJ").toString():"";
			if(clsj!=null && !"".equals(clsj)){
				String clsj_format = clsj.substring(0, 4)+"-"+clsj.substring(4, 6)+"-"+
									 clsj.substring(6, 8)+" "+clsj.substring(8, 10)+":"+clsj.substring(10,12)+":"+clsj.substring(12);
				map.put("clsj",clsj_format);
			}
			map.put("HPZLMC",this.systemDao.getCodeValue("030107",map.get("HPZL")==null?"":map.get("HPZL").toString()));
			map.put("dzwzid", map.get("DZWZID"));
        	map.put("cjjg",  map.get("CJJG"));
        	map.put("cjjgmc",  map.get("CJJGMC"));
        	map.put("clfl",  map.get("CLFL"));
        	map.put("hphm",  map.get("HPHM"));
        	map.put("fzjg",  map.get("FZJG"));
        	map.put("wfdd",  map.get("WFDD"));
        	map.put("wfdz",  map.get("WFDZ"));
        	map.put("wfxw",  map.get("WFXW"));
        	map.put("fkje",  map.get("FKJE"));
        	map.put("cljgmc",  map.get("CLJGMC"));
        	map.put("cljg",  map.get("CLJG"));
        	map.put("wfbh",  map.get("WFBH"));
        	map.put("dsr",  map.get("DSR"));
        	map.put("zsxxdz",  map.get("ZSXXDZ"));
        	map.put("jdsbh",  map.get("JDSBH"));
        	map.put("jdcsyr",  map.get("JDCSYR"));
        	map.put("lrr",  map.get("LRR"));
        	map.put("zqmj",  map.get("ZQMJ"));
			list.add(map);
		}
		mapwarning.put("rows",list);
		return mapwarning;
	}
}
