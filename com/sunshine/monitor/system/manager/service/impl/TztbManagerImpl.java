package com.sunshine.monitor.system.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.bean.TztbBean;
import com.sunshine.monitor.system.manager.dao.TztbDao;
import com.sunshine.monitor.system.manager.service.TztbManager;

@Service("tztbManager")
@Transactional()
public class TztbManagerImpl implements TztbManager {

	@Autowired
	@Qualifier("tztbDao")
	private TztbDao tztbDao;

	@Override
	public Map<String, Object> queryList(Map<String, Object> filter,
			TztbBean bean) throws Exception {
		return tztbDao.queryList(filter, bean);
	}

	@Override
	public int save(TztbBean bean) throws Exception {
		return tztbDao.save(bean);
	}

	@Override
	public int delete(String zgId) throws Exception {
		return tztbDao.delete(zgId);
	}

	@Override
	public int update(TztbBean bean) throws Exception {
		return tztbDao.update(bean);
	}

	@Override
	public TztbBean queryDetail(String Id) throws Exception {
		return tztbDao.getById(Id); 
	}

	@Override
	public Map<String, Object> queryTztbForMainPage(Map<String, Object> filter,String title) throws Exception {
		return tztbDao.queryForMainPage(filter, title);
	}
	
	
}
