package com.sunshine.monitor.system.analysis.dao;

import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;

import java.util.List;
import java.util.Map;

public interface PeerVehiclesDao extends ScsBaseDao{
   
	/**
	 * 查询同行车辆
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryForPeers(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	public List<Map<String, Object>> queryForPeersExt(ScsVehPassrec veh,Map<String,Object> map) throws Exception;

	public int queryForPeersTotal(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 查询目标车辆过车轨迹(分页查询)
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryForContrail(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 查询目标车辆过车轨迹(不计总数)
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryForContrailExt(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 查询目标车辆过车轨迹总计数
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer queryForContrailTotal(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 查询目标车辆过车轨迹
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getContrailList(String fxtj)throws Exception;
	
	/**
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getPeerList(String fxtj)throws Exception;
	
	/**
	 * 查询同行车过车轨迹
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryPeerDetail(ScsVehPassrec veh,Map<String,Object> map)throws Exception;
	
	/**
	 * <2016-10-20> 同行改造，根据详情同行车轨迹查询,原有方法为queryPeerDetail(...)
	 * @param sql
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryPeerCsDetail(String sql, Map<String, Object> filter) throws Exception;
	
	/**
	 * 查询同行车过车轨迹
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryPeerDetailForMap(ScsVehPassrec veh,Map<String,Object> map)throws Exception;
	
	/**
	 * 更新轨迹临时表状态
	 * @param gcxh
	 * @param check
	 * @return
	 * @throws Excepiton
	 */
	public int updateCheck(ScsVehPassrec veh,String sessionId)throws Exception;

}
