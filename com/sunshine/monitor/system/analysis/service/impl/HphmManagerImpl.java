package com.sunshine.monitor.system.analysis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.system.analysis.bean.HphmEntity;
import com.sunshine.monitor.system.analysis.dao.HphmDao;
import com.sunshine.monitor.system.analysis.service.HphmManager;

/**
 * 注册地维护信息业务逻辑层操作实现
 * @author licheng
 *
 */
@Service("hphmManager")
public class HphmManagerImpl implements HphmManager {

	@Autowired
	@Qualifier("hphmDao")
	private HphmDao hphmDao;
	
	@Override
	public List<HphmEntity> getHphmList(Map<String, Object> filter) {
		List<HphmEntity> hphmList = hphmDao.getHphmList(filter);
		return hphmList;
	}

}
