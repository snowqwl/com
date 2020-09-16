package com.sunshine.monitor.system.monitor.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.monitor.dao.IdentProjectDao;
import com.sunshine.monitor.system.monitor.service.IdentProjectManager;

@Service("identProjectManager")
public class IdentProjectManagerImpl implements IdentProjectManager {

	@Autowired
	@Qualifier("identProjectDao")
	private IdentProjectDao identProjectDao;
	
	public List<List<String>> getIdentProjectInfo(String kssj, String jssj,
			String kdbhs) {
		List<Map<String,Object>> list = this.identProjectDao.getIdentProjectInfo(kssj, jssj, kdbhs);
		String []titleName = {"白天识别","夜晚识别"};
		String []keys = {"BTSL","YWSL"};
		String columnKey = "FXMC";
		return FusionChartsXMLGenerator.getInstance().getMultiDSXMLData(list, titleName, keys,columnKey);
	}

	public IdentProjectDao getIdentProjectDao() {
		return identProjectDao;
	}

	public void setIdentProjectDao(IdentProjectDao identProjectDao) {
		this.identProjectDao = identProjectDao;
	}

	public List<List<String>> getIdentcdProjectInfo(String kssj, String jssj,
			String kdbh, String fxbh) {
		List<Map<String,Object>> list = this.identProjectDao.getIndetcdProjectInfo(kssj, jssj, kdbh, fxbh);
		String []titleName = {"白天识别","夜晚识别"};
		String []keys = {"BTSL","YWSL"};
		String columnKey = "cdbh";
		return FusionChartsXMLGenerator.getInstance().getMultiDSXMLData(list, titleName, keys, columnKey);
	}
	
}
