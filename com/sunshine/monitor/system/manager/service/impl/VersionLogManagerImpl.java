package com.sunshine.monitor.system.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.bean.VersionLogBean;
import com.sunshine.monitor.system.manager.dao.VersionLogDao;
import com.sunshine.monitor.system.manager.service.VersionLogManager;

@Service("versionLogManager")
@Transactional()
public class VersionLogManagerImpl implements VersionLogManager {

	@Autowired
	@Qualifier("versionLogDao")
	private VersionLogDao versionLogDao;

	@Override
	public Map<String, Object> queryList(Map<String, Object> filter,
			VersionLogBean bean) throws Exception {
		return versionLogDao.queryList(filter, bean);
	}

	@Override
	public int save(VersionLogBean bean) throws Exception {
		return versionLogDao.save(bean);
	}

	@Override
	public int delete(String zgId) throws Exception {
		return versionLogDao.delete(zgId);
	}

	@Override
	public int update(VersionLogBean bean) throws Exception {
		return versionLogDao.update(bean);
	}

	@Override
	public VersionLogBean queryDetail(String versionId) throws Exception {
		return versionLogDao.getVersionById(versionId); 
	}

	@Override
	public Map<String, Object> queryVersionForMainPage(Map<String, Object> filter,String name) throws Exception {
		return versionLogDao.queryForShowPage(filter, name);
	}
	
	
}
