package com.sunshine.monitor.system.query.service;

import java.util.List;
import java.util.Map;

public interface KsywtjQueryManager {
	
	/**
	 * 跨省业务统计列表
	 * @param kssj
	 * @param jssj
	 * @param sssf
	 * @return
	 */
	public List ksywtjList(String kssj, String jssj, String sssf)throws Exception ;
	
	/**
	 * 获取跨省过车查询日志列表
	 * @param conditions
	 * @return
	 */
	public Map<String,Object> getGccxList(Map<String, Object> conditions)throws Exception;
	
	/**
	 * 获取跨省布控信息列表
	 * @param conditions
	 * @return
	 */
	public Map<String,Object> getBkList(Map<String, Object> conditions)throws Exception;
	
	/**
	 * 获取跨省撤控信息列表
	 * @param conditions
	 * @return
	 */
	public Map<String,Object> getCkList(Map<String, Object> conditions)throws Exception;
	
	/**
	 * 获取跨省预警信息列表
	 * @param conditions
	 * @return
	 */
	public Map<String,Object> getYjList(Map<String, Object> conditions)throws Exception;
	
}
