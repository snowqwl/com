package com.sunshine.monitor.system.redlist.dao;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Element;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.redlist.bean.RedList;

public interface RedListDao extends BaseDao{
	
	/**
	 * 初始加载红名单
	 */
	public void initRedList();
	
	/**
	 * 添加红名单
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public int addRedList(RedList redlist) throws Exception;
	
	
	
	/** 
	 * 将已经存的并且是无效数据更新成录入状态
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public int updateinValidRedList(RedList redlist) throws Exception;
	
	
	/**
	 * 查询红名单记录
	 * @param subSql
	 * @return
	 * @throws Exception
	 */
	public RedList queryRedList(String subSql) throws Exception;
	
	
	/**
	 * 查询红名单列表
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryRedList(Map<String, Object> conditions) throws Exception;
	
	
	/**
	 * 审批
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public int updateRedList(RedList redlist) throws Exception;
	
	
	/**
	 * 解封红名单
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public int deblockingRedList(RedList redlist) throws Exception;
	
	
	/**
	 * delete
	 * @return
	 * @throws Exception
	 */
	public int deleteRedList(String hphm) throws Exception;
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getTime() throws Exception ;
	
	
	
	/**
	 * 查询所有有效红名单
	 * @return
	 * @throws Exception
	 */
	public List<RedList> queryAllRedList() throws Exception;
	
	
	/**
	 * 
	 * @param hphm
	 * @throws Exception
	 */
	public void deleteCacheData(String hphm) throws Exception;
	
	
	/**
	 * 
	 * @param hphm
	 * @throws Exception
	 */
	public void addDatatoCache(String hphm) throws Exception;
	
	
	/**
	 * 
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryValidRedlist(Map<String, Object> conditions) throws Exception;
}
