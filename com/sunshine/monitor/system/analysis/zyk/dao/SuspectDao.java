package com.sunshine.monitor.system.analysis.zyk.dao;

import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.zyk.bean.Suspect;

public interface SuspectDao extends BaseDao{

	/**
	 * 分页查询
	 * @param susp
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findPageForMap(Suspect susp, Map<String, Object>filter) throws Exception;
	
	/**
	 * 保存可疑库信息
	 * @param suspect
	 */
	public void saveSuspect(Suspect suspect);
	
	/**
	 * 根据ID获取可疑库单条记录
	 * @param suspectId
	 * @return
	 * @throws Exception
	 */
	public Suspect getSuspectById(String suspectId) throws Exception;
	
	/**
	 * 根据ID删除可疑库记录
	 * @param id
	 * @throws Exception
	 */
	public void deleteSuspect(String id) throws Exception;
	
	/**
	 * 更新可疑库信息记录
	 * @param suspect
	 * @throws Exception
	 */
	public void updateSuspect(Suspect suspect) throws Exception;
}
