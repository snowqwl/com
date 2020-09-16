package com.sunshine.monitor.system.monitor.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.monitor.bean.KhzbProject;

public interface KhzbProjectManager {
	/**
	 * 考核指标信息查询
	 * @param paramString1
	 * @param paramString2
	 * @param city
	 * @return
	 */
	public KhzbProject getKhzbProjectInfo(String paramString1, String paramsString2, String city) throws Exception;
	/**
	 * 考核指标信息保存
	 * @param khzbProject
	 * @return
	 */
	public int saveKhzb(KhzbProject khzbProject) throws Exception;

	/**
	 * 统计指标信息记录
	 * @param khzbProject
	 * @return
	 */
	public Map getKhzbProjectList(Map filter,KhzbProject khzbProject) throws Exception;
	
	public KhzbProject getKhzbProjectForObject(KhzbProject khzbProject) throws Exception;

	public List<Map<String, Object>> queryKhzbStatics(String kssj, String jssj)throws Exception;
	
}
