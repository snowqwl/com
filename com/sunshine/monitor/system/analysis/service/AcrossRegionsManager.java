package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * 本地跨区域车辆分析
 * @author licheng
 *
 */
public interface AcrossRegionsManager {
	
    /**
     * 根据指定条件查询分组数据(注册地)
     * @param filter
     * @return
     * @throws Exception
     */
	public Map<String ,Object > getList(Map<String,Object> filter) throws Exception;
	
	/**
	 * 改造 2016-9-10 liumeng
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getListExt(Map<String,Object> filter) throws Exception;
	/**
	 * 改造 2016-9-10 liumeng
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int getListTotal(Map<String,Object> filter) throws Exception;
	
	/**
	 * 查出不同过车类型的号牌统计数据
	 * @param filter
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public Map<String ,Object > queryDetilList(Map<String ,Object> filter,String sign) throws Exception;
	
	/**
	 * 改造 2016-9-10 liumeng
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDetilListExt(Map<String,Object> filter,String sign) throws Exception;
	/**
	 * 改造 2016-9-10 liumeng
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int queryDetilListTotal(Map<String,Object> filter,String sign) throws Exception;
	
	/**
	 * 根据号牌号码查询过车轨迹
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryForDetilList(Map<String, Object> filter) throws Exception ;
}
