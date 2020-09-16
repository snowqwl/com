package com.sunshine.monitor.system.susp.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface SuspinfoAuditApproveManager {
	
	/**
	 * 
	 * 函数功能说明 : 查询需布控审批的记录集合
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getSuspinfoApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	public int getSuspinfoApproveOverTimeCount(String begin, String end, String glbm)
	throws Exception;
	
	/**
	 * 
	 * （根据参数）查询需布控审批的记录集合
	 * @param bkxh
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public Object getApproveDetailForBkxh(String bkxh, String glbm) throws Exception;
	/**
	 * 
	 * 得到车身颜色
	 * @param csys
	 * @throws Exception    
	 * @return 
	 */
	public String getCsys(String csys) throws Exception;
	/**
	 * 
	 * 得到布控范围
	 * @param bkfw
	 * @throws Exception    
	 * @return 
	 */
	public String getBkfw(String bkfw) throws Exception;

	/**
	 * 
	 * 	查询code_url表的信息
	 * @param 
	 * @throws Exception    
	 * @return 
	 */
	public List getBkfwListTree() throws Exception; 
	/**
	 * 
	 * 得到报警方式
	 * @param bjfs
	 * @throws Exception    
	 * @return 
	 */
	public String getBjfs(String bjfs) throws Exception;
	/**
	 * 
	 * 转义特殊符号
	 * @param s
	 * @throws Exception    
	 * @return 
	 */
	public String TextToHtml(String s) throws Exception;
	
	/**
	 * 
	 * 	查询veh_alarmrec表的信息
	 * @param bkxh
	 * @throws Exception    
	 * @return 
	 */
	public List getAlarmList(String bkxh)throws Exception;
	/**
	 * 
	 * 	(根据参数)查询veh_alarmrec表的信息
	 * @param bkxh
	 * @throws Exception    
	 * @return 
	 */
	public List getSuspinfoAlarm(String bkxh) throws Exception;
	/**
	 * 
	 * 函数功能说明:获取审核审批表记录集合
	 * 修改日期 	2013-6-19
	 * @param aa
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	public List getAuditApproves(AuditApprove aa) throws Exception;
	
	/**
	 * 
	 * 函数功能说明 : 布控审核调用方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map   
	 * @throws
	 */
	public int saveAuditApprove(AuditApprove info) throws Exception;
	
	/**
	 * 
	 * 函数功能说明:保存审核审批表数据
	 * @param info(TransAuditApprove)
	 * @throws Exception    
	 * @return 
	 */
	public int saveAuditApprove(TransAuditApprove info) throws Exception;
	
	/**
	 * 
	 * 函数功能说明:查询已知布控序号布控记录的布控审核审批记录集合
	 * 修改日期 	2013-6-19
	 * @param bkxh
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	public List getSuspinfoAuditHistory(String bkxh) throws Exception;
	
	/**
	 * 
	 * 函数功能说明 : 查询需要审核的布控数据
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map findSuspinfoAuditForMap(Map filter, VehSuspinfo info,String glbm)throws Exception;
	
	/**
	 * 
	 * 函数功能说明 : 查询需要审核的超时布控数据
	 * 修改日期 	2014-10-15
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map findSuspinfoAuditOverTimeForMap(Map filter, VehSuspinfo info,String glbm)throws Exception;

	/**
	 * 
	 * 函数功能说明 :布控审批调用方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map saveApprove(AuditApprove info) throws Exception;
	
	
	/**
	 * 
	 * 函数功能说明 : 布控审核调用方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map   
	 * @throws
	 */
	public Map<String,String> saveAudit(AuditApprove info)throws Exception;
	
	
	/**
	 * 
	 * 函数功能说明 : 查询撤控申请的列表方法
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @param modul
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map findSuspinfoCancelForMap(Map filter, VehSuspinfo info, String glbm, String modul) throws Exception;
	/**
	 * 
	 * 函数功能说明 : 查询撤控申请的列表方法
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @param modul
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map findSuspinfoCancelOverTimeForMap(Map filter, VehSuspinfo info, String glbm, String modul) throws Exception;
	
	/**
	 * 
	 * 函数功能说明 : 撤控申请保存方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @param jz
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public boolean saveCancelSuspInfo(VehSuspinfo info, String jz) throws Exception;
	
	/**
	 * 
	 * 函数功能说明 : 撤控审核调用方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map<String,String>
	 */
	public Map<String,String> saveCAudit(AuditApprove info)throws Exception;

	/**
	 * 
	 * 函数功能说明:查询需撤控审批的列表记录(包括涉案和交通违法)
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getSuspinfoCancelApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	/**
	 * 	
	 * 函数功能说明:查询需撤控审批的列表记录(涉案)
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getSuspinfoCancelClassApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	/**
	 * 
	 * 函数功能说明:查询需撤控审批的列表记录(交通违法)
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getSuspinfoCancleTrafficApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	/**
	 * 
	 * 函数功能说明:撤控审核审批历史记录(针对已知布控序号的布控记录)
	 * 修改日期 	2013-6-19
	 * @param bkxh
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	public List getSuspinfoCAuditHistory(String bkxh) throws Exception;
	
	/**
	 * 
	 * 函数功能说明 :保存撤控审批记录方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map saveCApprove(AuditApprove info) throws Exception;
	
	
	/**
	 * 
	 * (根据参数)查询veh_suspinfo表的数据数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoAuditCount(String begin, String end, String glbm) throws Exception;
	
	/**
	 * 
	 * (根据参数)查询veh_suspinfo表的数据数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoAuditOverTimeCount(String begin, String end, String glbm) throws Exception;
	
	
	/**
	 * 
	 * (根据参数)得到布控审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoApproveCount(String begin, String end, String glbm) throws Exception;
	/**
	 * 
	 * (根据参数)得到撤控审核数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoCancelAuditCount(String begin, String end, String glbm) throws Exception;
	/**
	 * 
	 * (根据参数)得到撤控审核数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoCancelAuditOverTimeCount(String begin, String end, String glbm) throws Exception;
	/**
	 * 
	 * (根据参数)得到撤控审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoCancelApproveCount(String begin, String end, String glbm) throws Exception;
	/**
	 * 
	 * (根据参数)得到撤控审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoCancelApproveOverTimeCount(String begin, String end, String glbm) throws Exception;
	/**
	 * 
	 * (根据参数)得到超期撤控审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @param yhmc
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getExpireCancelSuspinfoCount(String begin, String end, String glbm,String yhmc) throws Exception;
	
	/**
	 * （根据参数）得到超时未撤控数目（已拦截确认）
	 * @param begin
	 * @param end
	 * @param glbm
	 * @param yhmc
	 * @return
	 * @throws Exception
	 */
	public int getSuspinfoOuttimeCountCount(String begin, String end, String glbm,String yhmc) throws Exception;
	/**
	 * 
	 * (根据参数)得到用户布控信息
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public List<Code> getUserSuspinfo(String begin, String end, String yhdh) throws Exception;
	/**
	 * 
	 * (根据参数)得到联动布控数目
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoBkfwlxCount(String begin, String end, String yhdh) throws Exception;
	/**
	 * 
	 * (根据参数)得到联动布控数目
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoNoBkfwlxCount(String begin, String end, String yhdh) throws Exception;
	/**
	 * 
	 * 超时撤控审批列表
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public Map findSuspinfoCancelTimeoutForMap(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	/**
	 * @author huanghaip
	 * 批量审核-查询跨省布控待审核的布控数据
	 * @Date 2017-07-11
	 * @param bkr
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getVehSuspinfoList() throws Exception;
	
	
	/**
	 * yaowang
	 * 函数功能说明 : 撤控申请保存方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @param jz
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@OperationAnnotation(type = OperationType.CSUSP_APPLY_ADD, description = "撤控申请新增")
	public boolean saveCancelSuspInfocksq(final VehSuspinfo info, String jz)
			throws Exception;
}
