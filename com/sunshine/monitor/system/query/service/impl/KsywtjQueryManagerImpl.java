package com.sunshine.monitor.system.query.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.query.dao.KsywtjQueryDao;
import com.sunshine.monitor.system.query.service.KsywtjQueryManager;

@Service("ksywtjQueryManagerImpl")
@Transactional
public class KsywtjQueryManagerImpl implements KsywtjQueryManager {
	
	@Autowired
	@Qualifier("ksywtjQueryDao")
	private KsywtjQueryDao ksywtjQueryDao;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Override
	public List ksywtjList(String kssj, String jssj, String sssf)
			throws Exception {
		return this.ksywtjQueryDao.ksywtjList(kssj, jssj, sssf);
	}
	
	@Override
	public Map<String,Object> getGccxList(Map<String, Object> conditions)
			throws Exception {
		return this.ksywtjQueryDao.getGccxList(conditions);
	}
	
	@Override
	public Map<String,Object> getBkList(Map<String, Object> conditions)
			throws Exception {
		return this.ksywtjQueryDao.getBkList(conditions);
	}
	
	@Override
	public Map<String,Object> getCkList(Map<String, Object> conditions)
			throws Exception {
		return this.ksywtjQueryDao.getCkList(conditions);
	}
	
	@Override
	public Map<String,Object> getYjList(Map<String, Object> conditions)
			throws Exception {
		Map<String,Object> map =  this.ksywtjQueryDao.getYjList(conditions);
		List<Map<String,Object>> list =(List<Map<String,Object>>)map.get("rows");
		if(list == null || list.size() < 1) {
			return map;
		}
        for(int i = 0 ;i<list.size();i++){
        	Map<String,Object> rec = list.get(i);
        	CodeGate gate = this.gateManager.getGate(rec.get("KDBH").toString());
        	if(gate!=null){
            	rec.put("KKZT",gate.getKkzt());
        	}else{
        		rec.put("KKZT","");
        	}
        }
        map.put("rows",list);
		return map;
	}
}
