package com.sunshine.monitor.system.query.service.impl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.query.bean.ScrapAnnual;
import com.sunshine.monitor.system.query.dao.ScrapAnnualDao;
import com.sunshine.monitor.system.query.service.ScrapAnnualQueryManager;
@Transactional
@Service("ScrapAnnualQueryManager")
public class ScrapAnnualQueryManagerImpl implements ScrapAnnualQueryManager {
	
	@Autowired
	private ScrapAnnualDao scrapAnnualDao;
	/**
	 * 
	 * 函数功能说明:报废未年检车辆查询数据
	 * @param map
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@OperationAnnotation(type=OperationType.INTEGRATION_PASSRC_QUERY,description="报废未年检车辆查询")
	public Map getMapForScrapAnnual(Map filter, ScrapAnnual info)
			throws Exception {

		return scrapAnnualDao.getMapForScrapAnnual(filter, info);
	}
	
}
