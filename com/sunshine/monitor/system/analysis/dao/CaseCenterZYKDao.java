package com.sunshine.monitor.system.analysis.dao;

import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.CaseEntity;

public interface CaseCenterZYKDao {
	/**
	 * 分页查询：案件信息
	 * @param entity
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findCaseByPage(CaseEntity entity,Map<String,Object> filter)throws Exception; 
	
	/**
	 * 查询案件信息明细
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCaseDetail(Map<String, Object> entity);
}
