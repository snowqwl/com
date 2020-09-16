package com.sunshine.monitor.system.analysis.zyk.dao;

import java.util.List;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.zyk.bean.CaseInfo;

public interface CaseInfoDao extends BaseDao{
	
	/**
	 * 保存案件信息
	 * @param caseInfo
	 */
	public void saveCaseInfo(CaseInfo ci);
	
	/**
	 * 根据ID获取案件信息单条记录
	 * @param caseInfoById
	 * @return
	 * @throws Exception
	 */
	public CaseInfo getCaseInfoById(String id) throws Exception;
	

	/**
	 * 更新案件信息记录
	 * @param caseInfo
	 * @throws Exception
	 */
	public void updateCaseInfo(CaseInfo ci) throws Exception;
	
	/**
	 * 根据ID删除案件信息记录
	 * @param id
	 * @throws Exception
	 */
	public void deleteCaseInfo(String id) throws Exception;
	
	/**
	 * 获取所有案件信息
	 * @throws Exception
	 */
	public List<CaseInfo> getAllCaseInfo();
}
