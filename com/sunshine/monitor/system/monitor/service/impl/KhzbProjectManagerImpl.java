package com.sunshine.monitor.system.monitor.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.system.monitor.bean.KhzbProject;
import com.sunshine.monitor.system.monitor.dao.KhzbProjectDao;
import com.sunshine.monitor.system.monitor.service.KhzbProjectManager;

@Service("khzbProjectManager")
public class KhzbProjectManagerImpl implements KhzbProjectManager {

    @Autowired
    @Qualifier("khzbProjectDao")
	private KhzbProjectDao khzbProjectDao;
	
	public KhzbProject getKhzbProjectInfo(String paramString1,
			String paramsString2, String city) throws Exception {
		return khzbProjectDao.getKhzbProjectInfo(paramString1, paramsString2, city);
	}

	public int saveKhzb(KhzbProject khzbProject) throws Exception {
		return khzbProjectDao.saveKhzb(khzbProject);
	}

	public KhzbProjectDao getKhzbProjectDao() {
		return khzbProjectDao;
	}

	public void setKhzbProjectDao(KhzbProjectDao khzbProjectDao) {
		this.khzbProjectDao = khzbProjectDao;
	}

	public Map getKhzbProjectList(Map filter, KhzbProject khzbProject)
			throws Exception {
		return this.khzbProjectDao.getKhzbProjectList(filter, khzbProject);
	}

	public KhzbProject getKhzbProjectForObject(KhzbProject khzbProject)
			throws Exception {
		return this.khzbProjectDao.getKhzbProjectforObject(khzbProject);
	}

	public List<Map<String, Object>> queryKhzbStatics(String kssj, String jssj)
			throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("NDGHL", String.valueOf(this.khzbProjectDao.queryNdghRate(kssj, jssj) + "%"));
		map.put("LHS", String.valueOf(this.khzbProjectDao.queryLHS(kssj, jssj)));
		map.put("CSCKS", String.valueOf(this.khzbProjectDao.queryCSCKS(kssj, jssj)));
		map.put("KKZXL", String.valueOf(this.khzbProjectDao.queryKkzxRate(kssj, jssj)) + "%");
		map.put("GZJSL", String.valueOf(this.khzbProjectDao.queryGzjsRate(kssj, jssj)) + "%");
		list.add(map);
		return list;
	}
}
