package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;
import com.sunshine.monitor.system.analysis.bean.CaseInfo;
import com.sunshine.monitor.system.analysis.bean.CaseSpecial;
import com.sunshine.monitor.system.analysis.bean.PoliceInfor;


public interface CaseSpecialDao{
	
	/**
	 * 分页查询:可疑信息库
	 * @param entity
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findCaseByPage(Map<String,Object> filter) throws Exception;
	
	
	/**
	 * 分页查询:警情信息库
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findPoliceByPage(Map<String,Object> filter) throws Exception;
	
	
	
	/**
	 * 分页查询:案件信息库
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findCaseInfoByPage(Map<String,Object> filter) throws Exception;
	
	
	
	/**
	 * 分页查询:案件信息库
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findExemplaryCaseByPage(Map<String,Object> filter) throws Exception;
	
	
	
	
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
	
	/**
	 *  保存警情,案件,专案相关信息  
	 * @param zazyxh
	 * @param ajzyxh
	 * @param jqh
	 * @param kyzyxh
	 * @return
	 */
	public int saveCaseDoubtfulExceplary(String []zazyxh,String []ajzyxh,String []jqh,String []kyzyxh) throws Exception;
	
	/**
	 * 删除可疑案件信息
	 * @param kyzyxhArr
	 * @return
	 * @throws Exception
	 */
	public int deleteCaseInfo(String[] kyzyxhArr) throws Exception;
	
	
	/**
	 * 保存相关信息
	 * @param param
	 * @return
	 */
	public int saveCaseInfo (String []paramStr1,String []paramStr2,String paramStr3) throws Exception;
	
	/**
	 * 保存专案信息
	 * @param fitler
	 * @return
	 * @throws Exception
	 */
	public int saveExemplaryCaseInfo(Map filter) throws Exception;
	
	/**
	 * 保存可疑案件信息
	 * @param caseSpecial
	 * @return
	 * @throws Exception
	 */
	public int saveCaseInfo(CaseSpecial caseSpecial) throws Exception;
	
	
	/**
	 * 修改可疑信息表
	 * @param kyzyxh
	 * @return
	 * @throws Exception
	 */
	public int updateCaseInfo(String []kyzyxh) throws Exception;
	
	
	/**
	 * 查询专案名称信息
	 * @return
	 * @throws Exception
	 */
	public List<CaseGroupEntity> getExemplaryCaseList() throws Exception;
	
	/**
	 * 快速查询
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> quicksearchListByPage(Map<String, Object> filter)
			throws Exception ;

}
