package com.sunshine.monitor.system.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.manager.service.SysparaManager;

@Service("sysparaManager")
@Transactional()
public class SysparaManagerImpl implements SysparaManager {

	
	@Autowired
	@Qualifier("sysparaDao")
	private SysparaDao sysparaDao;
	
	public Syspara getSyspara(String paramString1, String paramString2,
			String paramString3) throws Exception {
		return this.sysparaDao.getSyspara(paramString1, paramString2, paramString3);
	}
	
	public List getSysparas(String paramString) throws Exception {
		return this.sysparaDao.getSysparas(paramString);
	}



	public Map<String,Object> getSysparas(Map filter) throws Exception {
		return this.sysparaDao.getSysparas(filter);
	}

	public SysparaDao getSysparaDao() {
		return sysparaDao;
	}

	public void setSysparaDao(SysparaDao sysparaDao) {
		this.sysparaDao = sysparaDao;
	}
	

}
