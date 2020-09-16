package com.sunshine.monitor.system.susp.dao;

import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface SuspinfoProvinceApproveDao extends BaseDao {

	 public Map<String, Object> getSusinfoApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	 
	 public Map<String, Object> getSusinfoApprovesOverTime(Map filter, VehSuspinfo info, String glbm) throws Exception;
	 
	 public String getBkshrmc(String bkxh)throws Exception;
}
