package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.analysis.dao.AcrossRegionsSCSDao;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.AcrossRegionsManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Service("acrossRegionsManager")
public class AcrossRegionsManagerImpl implements AcrossRegionsManager {

	@Autowired
	private AcrossRegionsSCSDao acrossRegionsSCSDao;
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	private DirectDao directDao;
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Override
	public Map<String, Object> getList(Map<String, Object> filter)
			throws Exception {
		if(filter.get("rows")==null){
			filter.put("rows", 2);
		}
		if(filter.get("page")==null){
			filter.put("page", 1);
		}
		// 根据条件查询本地跨区域车辆分析
		Map<String,Object> resultmap =  this.acrossRegionsSCSDao.getList(filter);		
		return resultmap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryDetilList(Map<String, Object> filter, String sign) throws Exception {
		Map<String, Object> map = this.acrossRegionsSCSDao.queryDetilList(filter, sign);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for (Iterator<Map<String, Object>> i = list.iterator(); i.hasNext();) {
			Map<String, Object> result = i.next();
			CarKey car = new CarKey();
			car.setCarNo(result.get("hphm").toString());
			car.setCarType(result.get("hpzl").toString());
			cars.add(car);
			result.put("hpysmc", systemManager.getCodeValue("031001", result.get("hpys").toString()));
		}
		// 获取违法关联数
		//Map<CarKey, Integer> wfxx = this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey, Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> wfmap = (Map<String, Object>) list.get(i);
			wfmap.put("wfxx", wfxx.get(new CarKey(wfmap.get("hphm").toString(),wfmap.get("hpzl").toString())));
		}
		map.put("rows", list);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryForDetilList(Map<String, Object> filter) throws Exception {
		Map<String, Object> map =this.acrossRegionsSCSDao.queryForDetilList(filter);
		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
		
			//result.put("HPYS", systemManager.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("HPZLMC", systemManager.getCodeValue("030107", result.get("hpzl").toString()));
			result.put("XZQH", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			result.put("KDBHMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			result.put("FXBH", this.directDao.getOldDirectName(result.get("FXBH").toString()));
			result.put("GCSJ", sdf.format(result.get("GCSJ")));
			result.put("HPYSMC",this.systemDao.getCodeValue("031001",result.get("HPYS")==null?"":result.get("HPYS").toString()));
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getListExt(Map<String, Object> filter)
			throws Exception {
		if(filter.get("rows")==null){
			filter.put("rows", 2);
		}
		if(filter.get("page")==null){
			filter.put("page", 1);
		}
		// 根据条件查询本地跨区域车辆分析
		List<Map<String,Object>> resultmap =  this.acrossRegionsSCSDao.getListExt(filter);		
		return resultmap;
	}

	@Override
	public int getListTotal(Map<String, Object> filter) throws Exception {
		try{
			return acrossRegionsSCSDao.getListTotal(filter);
		} catch(Exception e){
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> queryDetilListExt(
			Map<String, Object> filter, String sign) throws Exception {
		List<Map<String, Object>> list = this.acrossRegionsSCSDao.queryDetilListExt(filter, sign);
		List<CarKey> cars = new ArrayList<CarKey>();
		for (Iterator<Map<String, Object>> i = list.iterator(); i.hasNext();) {
			Map<String, Object> result = i.next();
			CarKey car = new CarKey();
			car.setCarNo(result.get("hphm").toString());
			car.setCarType(result.get("hpzl").toString());
			cars.add(car);
			//result.put("hpysmc", systemManager.getCodeValue("031001", result.get("hpys").toString()));
			result.put("HPZLMC",this.systemDao.getCodeValue("030107",result.get("HPZL").toString()));
		}
		// 获取违法关联数
		//Map<CarKey, Integer> wfxx = this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey, Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> wfmap = (Map<String, Object>) list.get(i);
			wfmap.put("wfxx", wfxx.get(new CarKey(wfmap.get("hphm").toString(),wfmap.get("hpzl").toString())));
		}
		return Common.orderListByWfcs(list,"wfxx");
	}

	@Override
	public int queryDetilListTotal(Map<String, Object> filter, String sign)
			throws Exception {
		try{
			return this.acrossRegionsSCSDao.queryDetilListTotal(filter, sign);
		} catch(Exception e){
			return 0;
		}
	}
}
