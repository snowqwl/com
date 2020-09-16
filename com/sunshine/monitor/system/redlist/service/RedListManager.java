package com.sunshine.monitor.system.redlist.service;

import java.util.Map;
import java.util.Set;

import com.sunshine.monitor.system.redlist.bean.RedList;

public interface RedListManager {
	
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
	 * 批量导入红名单,返回执行成功数
	 * @param set
	 * @return 
	 * @throws Exception
	 */
	public int addRedListBatch(Set<RedList> set) throws Exception;
	
	/**
	 * @Description:上传红名单校验车牌号是否已被添加到红名单表中
	 * @param: 
	 * @return: 
	 * @version:
	 * @author: TDD
	 * @date: 2014-9-30 下午04:49:47
	 */
	public RedList queryRedList(String subSql) throws Exception ;
	
	/**
	 * 查询记录
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public RedList queryRedList(RedList redlist) throws Exception;
	
	
	/**
	 * 查询无效红名单
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public RedList queryInvalidRedList(RedList redlist) throws Exception;
	
	/**
	 * 查询列表(未审批)
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryRedList(Map<String, Object> conditions) throws Exception ;
	
	
	/**
	 * 审批
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public int updateRedList(RedList redlist) throws Exception ;
	
	
	/**
	 * 查询列表(已审批)
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryAuidtedRedList(Map<String, Object> conditions) throws Exception;
	
	
	/**
	 * 解封红名单
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public int deblockingRedList(RedList redlist) throws Exception ;
	
	
	/**
	 * delete
	 * @param hphm
	 * @return
	 * @throws Exception
	 */
	public int deleteRedList(String hphm,String id) throws Exception ;
	
	
	/**
	 * 过滤红名单
	 * @param redlist
	 * @return
	 */
	public boolean filterRedList(String hphm) throws Exception;
	
	/** 
	 * 将已经存的并且是无效数据更新成录入状态
	 * @param redlist
	 * @return
	 * @throws Exception
	 */
	public int updateinValidRedList(RedList redlist) throws Exception;
	
	/**
	 * 
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryValidRedlist(Map<String, Object> conditions)throws Exception;
	
	/**
	 * 判断是否有还有效的号码
	 * 
	 * 
	 * **/
	public RedList existRedName(RedList redlist) throws Exception;
}
