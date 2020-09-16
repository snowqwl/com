package com.sunshine.monitor.system.monitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.system.monitor.dao.KkjrjcProjectDao;
import com.sunshine.monitor.system.monitor.service.KkjrjcProjectManager;

@Repository("kkjrjcProjectManager")
public class KkjrjcProjectManagerImpl implements KkjrjcProjectManager {

	@Autowired
	@Qualifier("kkjrjcProjectDao")
	private KkjrjcProjectDao kkjrjcProjectDao;
	
	
	public int getKkjrjcQueryCount() throws Exception {
		return this.kkjrjcProjectDao.getKkjrjcQueryCount();
	}


	public KkjrjcProjectDao getKkjrjcProjectDao() {
		return kkjrjcProjectDao;
	}


	public void setKkjrjcProjectDao(KkjrjcProjectDao kkjrjcProjectDao) {
		this.kkjrjcProjectDao = kkjrjcProjectDao;
	}


	public int getKkzxQueryCount() throws Exception {
		return this.kkjrjcProjectDao.getKkzxQueryCount();
	}
	
}
