package com.sunshine.monitor.system.analysis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.analysis.dao.ViolationDao;
import com.sunshine.monitor.system.analysis.service.ViolationManager;

@Service("violationManager")
@Transactional
public class ViolationManagerImpl implements ViolationManager {

	@Autowired
	@Qualifier("violationDao")
	private ViolationDao violationDao;
	
	public List getViolationList(String wflx, String hphm, String hpzl)
			throws Exception {		
		return this.violationDao.getViolationList(wflx, hphm, hpzl);
	}

}
