package com.sunshine.monitor.system.query.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.dao.RepairVehicleQueryDao;
import com.sunshine.monitor.system.query.service.RepairVehicleQueryService;

@Service("repairVehicleQueryService")
public class RepairVehicleQueryServiceImpl implements RepairVehicleQueryService {
	
	@Autowired
	private RepairVehicleQueryDao repairVehicleQueryDao;
	
	@Autowired
	private SystemManager systemManager;

	@Override
	public Map<String, Object> list(Map<String, Object> params) {
		int curPage=1;
		int pageSize=10;
		if(StringUtils.isNotBlank((String)params.get("curPage"))) {
			curPage=Integer.parseInt((String)params.get("curPage"));
			if(curPage<0)
				curPage=1;
		}
		if(StringUtils.isNotBlank((String)params.get("pageSize"))) {
			pageSize=Integer.parseInt((String)params.get("pageSize"));
			if(pageSize<10)
				pageSize=10;
		}
		params.put("curPage", curPage);
		params.put("pageSize", pageSize);
		Map<String, Object> map = repairVehicleQueryDao.getList(params);
		//计算总页数
		int total=(int) map.get("total");
		int yeshu=0;
		if(total % pageSize ==0) {
			yeshu=total/pageSize;
		}else {
			yeshu=(total/pageSize)+1;
		}
		map.put("yeshu", yeshu);
		return map;
	}

	@Override
	public Map<String, Object> detail(Map<String, Object> params) {
		Map<String,Object> resultMap=new HashMap<>();
		//获取机修车详情
		Map<String, Object> repairMap = repairVehicleQueryDao.getDetail(params);
		String hpzl=(String) repairMap.get("hpzl");
		String hphm=(String) repairMap.get("clph");
		params.put("hpzl", hpzl);
		params.put("hphm", hphm);
		//获取机动车详情
		Map<String, Object> jdcMap = repairVehicleQueryDao.getJdcDetail(params);
		
		//翻译字典项
		try {
			String hpzlmc2 = this.systemManager.getCodeValue("030107", (String)repairMap.get("hpzl"));
			repairMap.put("hpzlmc", hpzlmc2);
			String hpzlmc = this.systemManager.getCodeValue("030107", hpzl);
			String cllxmc = this.systemManager.getCodeValue("030104", (String)jdcMap.get("cllx"));//车辆类型
			String zjlxmc = this.systemManager.getCodeValue("040019", (String)jdcMap.get("sfzmmc"));//证件类型
			String csysmc = this.systemManager.getCodeValue("030108", (String)jdcMap.get("csys"));//车身颜色
			jdcMap.put("hpzlmc", hpzlmc);
			jdcMap.put("cllxmc", cllxmc);
			jdcMap.put("zjlxmc", zjlxmc);
			jdcMap.put("csysmc", csysmc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("repairMap", repairMap);
		resultMap.put("jdcMap", jdcMap);
		return resultMap;
	}

	@Override
	public Map<String, Object> getListXls(Map<String, Object> params) {
		int curPage=1;
		int pageSize=10;
		if(StringUtils.isNotBlank((String)params.get("num"))) {
			pageSize=Integer.parseInt((String)params.get("num"));
		}
		params.put("curPage", curPage);
		params.put("pageSize", pageSize);
		Map<String, Object> map = repairVehicleQueryDao.getList(params);
		return map;
	}

}
