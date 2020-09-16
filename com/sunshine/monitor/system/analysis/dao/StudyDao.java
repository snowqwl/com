package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface StudyDao extends BaseDao {
	/**
	 * 查询敏感卡口组列表
	 * @param filter
	 * @param gname
	 * @return
	 */
	public Map queryGateGroupList(Map filter, String gname);
	
	/**
	 * 新增敏感卡口组
	 * @param gname
	 * @param kdbhs
	 * @return
	 */
	public int insertGateGroup(String gname,String kdbhs);
	
	/**
	 * 更新敏感卡口组
	 * @param gid
	 * @param gname
	 * @param kdbhs
	 * @return
	 */
	public int updateGateGroup(String gid,String gname,String kdbhs);
	
	/**
	 * 删除敏感卡口组
	 * @param gid
	 */
	public void deleteGateGroup(String gid);
	
	/**
	 * 根据敏感卡口组编号查询改组卡口
	 * @param gid
	 * @return
	 */
	public List queryGateListByGid(String gid);
	
	/**
	 * 查询敏感时段列表
	 * @param filter
	 * @param tname
	 * @return
	 */
	public Map queryTimeGroupList(Map filter, String tname);
	
	/**
	 * 新增敏感时段
	 * @param params
	 * @return
	 */
	public int insertTimeGroup(Map params);
	
	/**
	 * 更新敏感时段
	 * @param params
	 * @return
	 */
	public int updateTimeGroup(Map params);
	
	/**
	 * 删除敏感时段
	 * @param tid
	 */
	public void deleteTimeGroup(String tid);
	
	public List getTimeGroupByTid(String tid);
	
	/**
	 * 查询频次规则列表
	 * @param filter
	 * @param param
	 * @return
	 */
	public Map  queryRuleList(Map filter, Map param);
	
	/**
	 * 更新频次规则
	 * @param param
	 * @return
	 */
	public int updateRule(Map param);
	
	public int deleteRule(String rid);

}
