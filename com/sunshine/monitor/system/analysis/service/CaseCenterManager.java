package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.CaseEntity;
import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;

public interface CaseCenterManager {
	public List<Map<String, Object>> queryAnalysisProjectList(CaseGroupEntity caseEntity) throws Exception;
	
	
	/**
	 * 分页查询：案件信息
	 * @param entity
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findCaseByPage(CaseEntity entity,Map<String,Object> filter)throws Exception;
    
	/**
	 * 查询案件详细信息
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public CaseEntity getCaseDetail(Map<String,Object> entity) throws Exception;
	
	/**
	 * 分页查询：专案信息
	 * @param entity
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findCaseGroupByPage(CaseGroupEntity entity,Map<String,Object> filter)throws Exception;
	
	/**
	 * 保存专案信息
	 * @param entity
	 * @throws Exception
	 */
	public void saveCaseGroup(CaseGroupEntity entity)throws Exception;

}
