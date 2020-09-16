package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.manager.bean.ZgfxBean;

public interface ZgfxManager {
	
	/**
	 * 查询
	 * @param filter
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryListZgzf(Map<String, Object> filter,ZgfxBean bean) throws Exception;
	
	/**
	 * 保存
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveZgzf(ZgfxBean bean) throws Exception;
	
	/**
	 * 删除
	 */
	public int deleteZgzf(String zgId) throws Exception;
	
	/**
	 * 修改
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int updateZgzf(ZgfxBean bean) throws Exception;
	
	/**
	 * 获取详情
	 * @param zgId
	 * @return
	 * @throws Exception
	 */
	public ZgfxBean queryDetail(String zgId) throws Exception;
	
	/**
	 * 查询查询最新的前十条
	 * @return
	 * @throws Exception
	 */
	public List<ZgfxBean> queryZgfxForMainPage() throws Exception;
	
	/**
	 * 主页专用-分页查询
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryForShowPage(Map<String, Object> filter,String title) throws Exception;
	
}