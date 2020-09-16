package com.sunshine.monitor.system.susp.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;


public interface SuspinfoCancelApproveManager {
	/**
	 * 撤控指挥中心审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	public Map<String, Object> getSuspinfoCancelApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	/**
	 * 撤控指挥中心超时审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	public Map<String, Object> getSuspinfoCancelApprovesOverTime(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	/**
	 * (根据参数)得到审批详细信息
	 * @param bkxh
	 * @param glbm
	 * @return
	 */
	public Object getApproveDetailForBkxh(String bkxh, String glbm) throws Exception;
	
	/**
	 * 撤控指挥中心涉案类审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	public Map<String, Object> getSuspinfoCancelClassApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	/**
	 * 撤控指挥中心交通类审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	public Map<String, Object> getSuspinfoCancleTrafficApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	
	/**
	 *得到车身颜色
	 * @param csys
	 * @return
	 */
	public String getCsys(String csys) throws Exception;

	/**
	 *得到布控范围
	 * @param bkfw
	 * @return
	 */
	public String getBkfw(String bkfw) throws Exception;
	/**
	 *查询code_url表数据
	 * @param 
	 * @return
	 */
	public List getBkfwListTree() throws Exception; 
	/**
	 *得到报警方式
	 * @param bjfs
	 * @return
	 */
	public String getBjfs(String bjfs) throws Exception;
	/**
	 *特殊字符转义
	 * @param s
	 * @return
	 */
	public String TextToHtml(String s) throws Exception;
	/**
	 *得到撤控审核/审批信息
	 * @param bkxh
	 * @return List
	 */
	public List getSuspinfoCAuditHistory(String bkxh) throws Exception;
	
	/**
	 *撤控指挥中心审批新增
	 * @param info
	 * @return Map
	 */
	public Map saveApprove(AuditApprove info) throws Exception;

	/**
	 *得到veh_alarmrec表的信息
	 * @param info
	 * @return Map
	 */
	public List getSuspinfoAlarm(String bkxh) throws Exception;
	
}
