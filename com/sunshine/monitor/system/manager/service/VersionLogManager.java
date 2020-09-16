package com.sunshine.monitor.system.manager.service;

import java.util.Map;

import com.sunshine.monitor.system.manager.bean.VersionLogBean;

public interface VersionLogManager {
	
	/**
	 * 查询
	 * @param filter
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryList(Map<String, Object> filter,VersionLogBean bean) throws Exception;
	
	/**
	 * 保存
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int save(VersionLogBean bean) throws Exception;
	
	/**
	 * 删除
	 */
	public int delete(String zgId) throws Exception;
	
	/**
	 * 修改
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int update(VersionLogBean bean) throws Exception;
	
	/**
	 * 获取详情
	 * @param zgId
	 * @return
	 * @throws Exception
	 */
	public VersionLogBean queryDetail(String Id) throws Exception;
	
	/**
	 * 主页专用-分页查询
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryVersionForMainPage(Map<String, Object> filter,String name) throws Exception;
	
}