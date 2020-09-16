package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;
import com.sunshine.monitor.system.analysis.bean.CaseInfo;
import com.sunshine.monitor.system.analysis.bean.CaseSpecial;
import com.sunshine.monitor.system.analysis.bean.PoliceInfor;

public interface CaseSpecialManager {
	
	public Map<String, Object> findCaseByPage(Map<String, Object> filter) throws Exception;
	
	
	public Map<String, Object> findPoliceByPage(Map<String, Object> filter) throws Exception;
	
	public Map<String, Object> findCaseInfoByPage(Map<String, Object> filter) throws Exception;
	
	
	public Map<String, Object> findExemplaryCaseByPage(Map<String, Object> filter) throws Exception;
	
	/**
	 * 查询警情名称信息
	 * @return
	 * @throws Exception
	 */
	public List<PoliceInfor> getPoliceList() throws Exception;
	
	
	/**
	 * 查询案件名称信息
	 * @return
	 * @throws Exception
	 */
	public List<CaseInfo> getCaseInfoList() throws Exception;
	
	
	public int saveCaseDoubtfulExceplary(String[] zazyxh, String[] ajzyxh,
			String[] jqh, String[] kyzyxh) throws Exception;
	
	public int deleteCaseInfo(String[] kyzyxhArr) throws Exception;
	
	/**
	 * 查询专案名称信息
	 * @return
	 * @throws Exception
	 */
	public List<CaseGroupEntity> getExemplaryCaseList() throws Exception;


	public int saveCaseInfo(CaseSpecial caseSpecial) throws Exception;
	
	/**
	 * 保存到分析图片到专案信息库
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int saveCasePic(Map<String,Object> map) throws Exception;
	/**
	 * 快速查询   
     * @param ci 条件参数
	 * @param filter 分页参数
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> quicksearchList(Map<String, Object> filter) throws Exception;
	

}
