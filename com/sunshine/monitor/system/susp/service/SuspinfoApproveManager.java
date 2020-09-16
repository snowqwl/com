package com.sunshine.monitor.system.susp.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;


public interface SuspinfoApproveManager {
	
	public Object getApproveDetailForBkxh111(String bkxh)throws Exception;
	
	public List getBkfwListTreenew() throws Exception;
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
	/**
	 * 
	 * @param bkxh
	 * @param glbm
	 * @return
	 */
	public Object getApproveDetailForBkxh(String bkxh, String glbm) throws Exception;
	/**
	 * 布控指挥中心涉案类审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	public Map<String, Object> getSuspinfoClassApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	/**
	 * 布控指挥中心交通类审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	public Map<String, Object> getSuspinfoTrafficApproves(Map filter, VehSuspinfo info, String glbm) throws Exception;
	/**
	 * 获得车身颜色
	 * @param csys
	 * @return
	 */
	public String getCsys(String csys) throws Exception;
	/**
	 * 获得布控范围
	 * @param bkfw
	 * @return
	 */
	public String getBkfw(String bkfw) throws Exception;
	/**
	 * 查询code_url表信息
	 * @return
	 * @throws Exception
	 */
	public List getBkfwListTree() throws Exception; 
	/**
	 * 得到报警方式
	 * @param bjfs
	 * @return
	 * @throws Exception
	 */
	public String getBjfs(String bjfs) throws Exception;
	/**
	 * 转义字符
	 * @return
	 * @throws Exception
	 */
	public String TextToHtml(String s) throws Exception;
	/**
	 * (根据参数)查询布控信息查询审批/核
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public List getSuspinfoAuditHistory(String bkxh) throws Exception;
	/**
	 * 布控指挥中心审批新增
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> saveApprove(AuditApprove info) throws Exception;
	
	 /**
     * 批量审批-查询需要批量审批的跨省的布控数据
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getBatchApproveList() throws Exception;
	
}
