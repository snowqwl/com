package com.sunshine.monitor.system.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.bean.ZgfxBean;
import com.sunshine.monitor.system.manager.dao.ZgfxDao;
import com.sunshine.monitor.system.manager.service.ZgfxManager;

@Service("zgfxManager")
@Transactional()
public class ZgfxManagerImpl implements ZgfxManager {

	@Autowired
	@Qualifier("zgfxDao")
	private ZgfxDao zgfxDao;

	@Override
	public Map<String, Object> queryListZgzf(Map<String, Object> filter,
			ZgfxBean bean) throws Exception {
		return zgfxDao.queryList(filter, bean);
	}

	@Override
	public int saveZgzf(ZgfxBean bean) throws Exception {
		return zgfxDao.save(bean);
	}

	@Override
	public int deleteZgzf(String zgId) throws Exception {
		return zgfxDao.delete(zgId);
	}

	@Override
	public int updateZgzf(ZgfxBean bean) throws Exception {
		return zgfxDao.update(bean);
	}

	@Override
	public ZgfxBean queryDetail(String zgId) throws Exception {
		return zgfxDao.getZgfx(zgId); 
	}

	@Override
	public List<ZgfxBean> queryZgfxForMainPage() throws Exception {
		return zgfxDao.queryForMainPage();
	}

	@Override
	public Map<String, Object> queryForShowPage(Map<String, Object> filter,
			String title) throws Exception {
		return zgfxDao.queryForShowPage(filter, title); 
	}
	
	
}
