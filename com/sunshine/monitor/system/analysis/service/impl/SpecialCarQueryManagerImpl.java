package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.analysis.bean.SpecialCarQueryGz;
import com.sunshine.monitor.system.analysis.dao.SpecialCarQueryDao;
import com.sunshine.monitor.system.analysis.service.SpecialCarQueryManager;
import com.sunshine.monitor.system.gate.bean.CodeDirect;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Transactional
@Service("SpecialCarQueryManager")
public class SpecialCarQueryManagerImpl implements SpecialCarQueryManager {
		
	@Autowired
	@Qualifier("SpecialCarQueryDao")
	private SpecialCarQueryDao specialCarQueryDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	public String saveBdgz(SpecialCarQueryGz information)throws Exception{
			return this.specialCarQueryDao.saveBdgz(information);
	}


	public Map<String, Object> querySpecialCarRuleList(Map<String, Object> conditions) throws Exception {		
		Map<String, Object> map = this.specialCarQueryDao.querySpecialCarRuleList(conditions);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List queryList = (List) map.get("rows");
		for (int i=0;i<queryList.size();i++) {
			Map  carmap = (Map) queryList.get(i);
			carmap.put("GZKSSJ", sdf.format(carmap.get("gzkssj")));
			carmap.put("GZJSSJ", sdf.format(carmap.get("gzjssj")));
		}
		map.put("rows", queryList);
		return map;
	}
	
	public SpecialCarQueryGz getSpecialCarRule(String gzbh)throws Exception{
		return this.specialCarQueryDao.getSpecialCarRule(gzbh);
	}
	
	
}
