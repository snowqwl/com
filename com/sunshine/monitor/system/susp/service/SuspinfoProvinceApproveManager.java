package com.sunshine.monitor.system.susp.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface SuspinfoProvinceApproveManager {

	/**
	 * 布控指挥中心审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	public Map<String, Object> getSuspinfoApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	/**
	 * 布控指挥中心审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	public Map<String, Object> getSuspinfoApprovesOverTime(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
}
