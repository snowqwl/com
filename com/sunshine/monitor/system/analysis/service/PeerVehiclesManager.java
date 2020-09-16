package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface PeerVehiclesManager {
   
	/**
	 * 同行车查询
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryForPeers(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 同行车查询
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryForPeersExt(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 同行车查询
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer queryForPeersTotal(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 轨迹查询(分页查询)
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryForContrail(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 轨迹查询(不计总数)
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryForContrailExt(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 轨迹查询(不计总数)
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer queryForContrailTotal(ScsVehPassrec veh,Map<String,Object> map) throws Exception;
	
	/**
	 * 轨迹查询
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map getContrailList(String sessionId)throws Exception;
	
	/**
	 * 轨迹查询
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map getContrailListBySave(String ztbh,Map<String,Object> filter) throws Exception;

	/**
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getPeerList(String sessionId)throws Exception;

	/**
	 * 同行车轨迹查询
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryPeerDetail(ScsVehPassrec veh,Map<String,Object> map)throws Exception;
	
	/**
	 * <2016-10-20> 同行改造，根据详情同行车轨迹查询,原有方法为queryPeerDetail(...)
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	//public Map<String,Object> queryPeerCsDetail(ScsVehPassrec veh,Map<String,Object> map)throws Exception;
	
	/**
	 * 查询同行车目标车对比列表
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryComparePic(ScsVehPassrec veh,Map<String,Object> map)throws Exception;
	
	/**
	 * 更新轨迹临时表状态
	 * @param veh
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public int updateCheck(ScsVehPassrec veh,String sessionId)throws Exception;
	
	/**
	 * 访问接口获取同行数据
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loadPeer(String page, String rows, VehPassrec veh,
			String sign, String cs, String symbol, String gaptime)
			throws Exception;
}
