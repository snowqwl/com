package com.sunshine.monitor.system.susp.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface SuspinfoEditDao extends BaseDao{

	public Map findSuspinfoForMap(Map map ,VehSuspinfo info)throws Exception;

	public VehSuspinfo getSuspinfoDetailForBkxh(String bkxh)throws Exception;
	
	public VehSuspinfo getCitySuspinfoDetailForBkxh(String bkxh,String cityname) throws Exception;

	public List getSuspListForHphm(String hphm,String hpzl)throws Exception;
	
	public int updateSuspinfo(VehSuspinfo suspInfo)throws Exception;
	
	public int updateSuspInfoForDel(VehSuspinfo info)throws Exception;
	
	public int insertBusinessLogForDel(VehSuspinfo info,String ssjz)throws Exception;

	public List getBkfwListTree()throws Exception;
	
	public int getSuspinfoEditCount(String begin, String end, String yhdh, String bkfwlx) throws Exception;
	
	public AuditApprove getAuditApprove(String bkxh)throws Exception;
}
