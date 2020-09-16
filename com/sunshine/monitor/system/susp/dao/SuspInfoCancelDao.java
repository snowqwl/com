package com.sunshine.monitor.system.susp.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface SuspInfoCancelDao  extends BaseDao{

	public Map findSuspinfoCancelForMap(Map filter, VehSuspinfo info, String glbm, String modul) throws Exception;
		
	public Map findSuspinfoCancelOverTimeForMap(Map filter, VehSuspinfo info, String glbm, String modul) throws Exception;
	
	public List getAlarmList(String bkxh)throws Exception;
	
	public Map findSuspinfoCancelTimeoutForMap(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	public List<VehSuspinfo> getAutoCsuspinfoList() throws Exception;
	
	public List getAuditApproves(AuditApprove aApprove) throws Exception;

	public int updateSuspInfoForCancel(VehSuspinfo info)throws Exception;
	
	public int insertBusinessLog(VehSuspinfo info,String jz)throws Exception;
	
	public void synchronizationJz() throws Exception;
	
	/**
	 * 根据对象中非空的字段更新
	 * @param info
	 * @throws Exception
	 */
	public void updateSuspInfo(VehSuspinfo info) throws Exception;
	
	/*
	 * yaowang
	 * 
	 */
	//更新布控表(针对撤控申请)
		public int updateSuspInfoForCancelcksq(VehSuspinfo info)throws Exception;
}
