package com.sunshine.monitor.system.susp.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoApproveDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoProvinceApproveDao;
import com.sunshine.monitor.system.susp.service.SuspinfoProvinceApproveManager;

@Service("suspinfoProvinceApproveManager")
public class SuspinfoProvinceApproveManagerImpl implements SuspinfoProvinceApproveManager {
	
	@Autowired
	@Qualifier("suspinfoProvinceApproveDao")
	private SuspinfoProvinceApproveDao suspinfoProvinceApproveDao;
	
	
	
	/**
	 * 布控指挥中心审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	private Logger logger = LoggerFactory.getLogger(SuspinfoApproveManagerImpl.class);

	@OperationAnnotation(type=OperationType.SUSP_APPROVAL_QUERY,description="布控指挥中心审批查询")
	public Map<String, Object> getSuspinfoApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoProvinceApproveDao.getSusinfoApproves(filter, info, glbm);
		List queryList = (List) map.get("rows");
		for (int i=0;i<queryList.size();i++) {
			Map  susp = (Map) queryList.get(i);
			susp.put("SHRMC",this.suspinfoProvinceApproveDao.getBkshrmc(susp.get("bkxh").toString()));
		}
		map.put("rows", queryList);
		return map;
	}

	
	/**
	 * 布控指挥中心审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	@OperationAnnotation(type=OperationType.SUSP_APPROVAL_QUERY,description="布控指挥中心审批查询")
	public Map<String, Object> getSuspinfoApprovesOverTime(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoProvinceApproveDao.getSusinfoApprovesOverTime(filter, info, glbm);
		return map;
	}

}
