package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.bean.StatisEntity;
import com.sunshine.monitor.system.analysis.bean.SubjectEntity;

public interface SubjectManager {
	
	/**
	 * 根据分析条件查询相应的统计数据（limit 40）
	 * @param Subject
	 * @return
	 */
	public List<Map<String,Object>> getStatisList(SubjectEntity subsject);
	
	/**
	 * 保存分析，一个分析主题对应多条统计信息
	 * @param subject
	 * @param statislist
	 * @return
	 */
	public int saveSubject(SubjectEntity subject,List<Map<String,Object>> statislist) throws Exception;
	
	/**
	 * 根据分析主题编号查询统计信息列表
	 * @param filter
	 * @param ztbh
	 * @return
	 */
	public Map queryStatisList(Map filter,String ztbh);
	
	/**
	 * 查询分析主题明细
	 * @param ztbh
	 * @return
	 */
	public SubjectEntity querySubjectDetail(String ztbh);
	
	/**
	 * 保存轨迹信息
	 * @param list
	 * @param ztbh
	 * @return
	 */
	public int[] saveContrail(List<ScsVehPassrec> list,String ztbh)throws Exception;
	
	/**
	 * 保存同行结果信息
	 * @param list
	 * @param ztbh
	 * @return
	 */
	public int[] savePeerInfo(List<Map<String,Object>> list,String ztbh)throws Exception;
	
	/**
	 * 保存分析主题
	 * @param subject
	 * @return ztbh 主题编号
	 * @throws Exception
	 */
	public String saveAnalysisSubject(SubjectEntity subject)throws Exception;
	
	/**
	 * 保存同行分析主题信息，结果信息
	 * @param subject
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public int savePeerAnalysis(SubjectEntity subject,String sessionId) throws Exception;
	
	/**
	 * 保存车辆轨迹查询结果
	 * @param subject
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public int saveVehicleContrail(SubjectEntity subject,String sessionId)throws Exception;

	
	/**
	 * 查询已保存分析数据轨迹结果
	 * @param ztbh
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> loadContrail(String ztbh,Map<String,Object> filter)throws Exception;
	
	/**
	 * 查询已保存分析数据同行信息结果
	 * @param ztbh
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> loadPeerInfo(String ztbh,Map<String,Object> filter)throws Exception;
	
	/**
	 * 查询已保存分析同行轨迹信息
	 * @param ztbh
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getPeerContrail(String ztbh,ScsVehPassrec veh,Map<String,Object> filter)throws Exception;
	
	/**
	 * 查询已保存分析同行对比图片
	 * @param ztbh
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getPeerComparePic(String ztbh,ScsVehPassrec veh,Map<String,Object> filter)throws Exception;
	
	/**
	 * 保存关联分析信息
	 * @return
	 * @throws Exception
	 */
	public int[] saveLinkInfo(SubjectEntity subject,String sessionId)throws Exception;
	
	
	/**
	 * 查询关联分析结果
	 * @param ztbh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getLinkList(String ztbh,Map<String,Object> filter)throws Exception;
	
	
	/**
	 * 保存案件对碰分析
	 * @param subject
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public int[] saveCaseTouch(SubjectEntity subject,String sessionId) throws Exception;


}
