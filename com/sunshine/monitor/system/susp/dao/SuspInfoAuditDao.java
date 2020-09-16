package com.sunshine.monitor.system.susp.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface SuspInfoAuditDao extends BaseDao{

	public Map findSuspinfoAuditForMap(Map filter, VehSuspinfo info,String glbm)throws Exception;
	
	public Map findSuspinfoAuditOverTimeForMap(Map filter, VehSuspinfo info,String glbm)throws Exception;

	public int insertAuditApprove(AuditApprove info)throws Exception;
	
	public int updateSuspInfo(String T_YWZT,String T_JLZT,String bkxh)throws Exception;
	
	//ckyydm撤控原因代码
	public int updateSuspInfo(String T_YWZT,String T_JLZT,String bkxh,String ckyydm)throws Exception;
	
	public int insertTransSusp(String xzqh,String jsdw,String bkxh,String type)throws Exception;
	
	public int insertTransSusp_test(String xzqh,String jsdw,String bkxh,String type)throws Exception;
	
	public int insertBusinessLog(VehSuspinfo suspInfo,AuditApprove info,String ssjz,String ywlb)throws Exception;
	
	public int getSupinfoAuditCont(String begin, String end, String glbm) throws Exception;
	
	public int getSuspinfoAuditOverTimeCount(String begin, String end, String glbm) throws Exception;

	public int getSuspinfoCancelAuditCount(String begin, String end, String glbm) throws Exception;
	
	public int getSuspinfoCancelAuditOverTimeCount(String begin, String end, String glbm) throws Exception;
	

	/**
	 * @author huanghaip
	 * 批量审核-查询跨省布控待审核的布控数据
	 * @Date 2017-07-11
	 * @param bkr
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getVehSuspinfoList() throws Exception;
}
