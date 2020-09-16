package com.sunshine.monitor.system.query.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.system.query.dao.SpecialCarDao;
import com.sunshine.monitor.system.query.service.SpecialCarManager;

@Service
public class SpecialCarManagerImpl implements SpecialCarManager {

	@Autowired
	private SpecialCarDao specialCarDao;
	
	public Map querySpecialList(Map conditions) throws Exception {
		return this.specialCarDao.querySpecialList(conditions);
	}

}
