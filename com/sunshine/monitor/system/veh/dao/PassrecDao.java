package com.sunshine.monitor.system.veh.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.ws.VehPassrecEntity;

public interface PassrecDao extends BaseDao {
	
	/**
	 * 查询过车信息列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecList(Map<String, Object> conditions, String tableName) throws Exception;
	
	/**
	 * 根据号牌号码查询过车信息列表(模糊查询)
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecListByHphm(Map<String, Object> conditions) throws Exception;
	
	/**
	 * 查询过车详细信息
	 * @param gcxh
	 * @return
	 * @throws Exception
	 */
	public VehPassrec getVehPassrecDetail(String gcxh) throws Exception;
	
	/**
	 * 查询旧版过车信息列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOldPassrecList(Map<String, Object> conditions, String tableName) throws Exception;
	
	/**
	 * 查询一定数量的过车信息
	 * 功能:全省漫游查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<VehPassrecEntity> queryPassrecLimitedList(String sql) throws Exception;
	
    /**
     * 查询过车信息列表(return List)
     * @param map
     * @return
     * @throws Exception
     */
	public List<VehPassrec> queryPassrecForList(Map<String,Object> map) throws Exception;
	
	public Map<String,Object> findPagePassrecForMap(Map<String,Object> map,Class<?> clazz)throws Exception;
	
	/**
     * 查询当月内的过车总量
     * @return
     */
	public int getPassrecCountInThisMonth() throws Exception;
	
	public Map<String,Object> queryContrailForMap(Map<String,Object> map)throws Exception;
	 
	public Map<String,Object> getHphm(Map<String,Object> map)throws Exception;
	
	public Map getCityFlow(String cityname) throws Exception;
	
	public void updateSTFlow(String cityname) throws Exception;
	
	/**
	 * 用户查询提示
	 * @param condition
	 * @return
	 */
	public int queryTips(Map condition);
	
	/**
	 * 统计查询总数
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public int getAllCount(Map<String,Object> conditions) throws Exception;
}
