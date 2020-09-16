package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.ZgfxBean;

public interface ZgfxDao extends BaseDao {
	
	/**
	 * 查询
	 * @param filter
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryList(Map<String, Object> filter,ZgfxBean bean) throws Exception;
	
	/**
	 * 保存
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int save(ZgfxBean bean) throws Exception;
	
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
	public int update(ZgfxBean bean) throws Exception;

	/**
	 * 获取单个战果的详细信息
	 * @param zgId
	 * @return
	 * @throws Exception
	 */
	public ZgfxBean getZgfx(String zgId) throws Exception;
	
	/**
	 * 查询主页前十条
	 * @return
	 * @throws Exception
	 */
	public List<ZgfxBean> queryForMainPage() throws Exception;
	
	/**
	 * 查询主页前十条
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryForShowPage(Map<String, Object> filter,String title) throws Exception;
}
