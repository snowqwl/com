package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.CaseEntity;
import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;

public interface CaseCenterDao {
	public List<Map<String,Object>> queryAnalysisProjectList(CaseGroupEntity caseEntity) throws Exception ;

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
	public CaseEntity getCaseDetail(CaseEntity entity) throws Exception;
	
	/**
	 * 根据专案编号查询关联案件
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public List<CaseEntity> getCaseList(String zabh) throws Exception;
	
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
	 * @return
	 * @throws Exception
	 */
	public String saveCaseGroup(CaseGroupEntity entity) throws Exception;
	
	/**
	 * 插入专案案件关联表
	 * @param zabh
	 * @param ajbh
	 * @throws Exception
	 */
	public void saveAssociateInfo(String zabh,String ajbh)throws Exception;

}
