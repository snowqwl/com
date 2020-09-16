package com.sunshine.monitor.system.analysis.zyk.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.zyk.bean.CaseInfo;

public interface CaseInfoManager {
	/**
	 * 根据条件查询案件信息库
	 * @param caseInfo
	 * @param filter
	 * @return
	 */
	public Map<String, Object> queryList(CaseInfo ci, Map<String, Object> filter);
	
	/**
	 * 新增案件信息
	 * @param caseInfo
	 */
	public void saveCaseInfo(CaseInfo ci);
	
	/**
	 * 根据ID获取单条案件信息
	 * @param id
	 */
	public CaseInfo getCaseInfoById(String id);
	
	/**
	 * 更新案件信息
	 * @param caseInfo
	 */
	public void updateCaseInfo(CaseInfo ci);
	
	/**
	 * 根据ID删除案件信息
	 * @param id
	 */
	public void deleteCaseInfo(String id);
	
	/**
	 * 获取所有案件信息
	 */
	public List<CaseInfo> getAllCaseInfo();
	
}
