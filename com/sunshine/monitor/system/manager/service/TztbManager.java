package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.manager.bean.TztbBean;

public interface TztbManager {
	
	/**
	 * 查询
	 * @param filter
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryList(Map<String, Object> filter,TztbBean bean) throws Exception;
	
	/**
	 * 保存
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int save(TztbBean bean) throws Exception;
	
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
	public int update(TztbBean bean) throws Exception;
	
	/**
	 * 获取详情
	 * @param zgId
	 * @return
	 * @throws Exception
	 */
	public TztbBean queryDetail(String Id) throws Exception;
	
	/**
	 * 主页专用-分页查询
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryTztbForMainPage(Map<String, Object> filter,String title) throws Exception;
	
}