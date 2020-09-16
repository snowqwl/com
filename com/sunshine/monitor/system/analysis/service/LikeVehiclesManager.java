package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface LikeVehiclesManager {
	//按条件获取号牌号码和过车数
	public Map<String,Object> queryForLike(VehPassrec veh,Map<String,Object> filter) throws Exception;
	public List<Map<String,Object>> queryForLikeExt(VehPassrec veh, Map<String, Object> filter) throws Exception;
	public Integer getForLikeTotal(VehPassrec veh, Map<String, Object> filter) throws Exception;
	//按条件获取号牌号码和过车数(临时表翻页)
	public Map<String,Object> queryForLikePage(Map<String,Object> filter) throws Exception;
	//根据号牌号码和部分条件获取过车的轨迹列表
	public Map<String,Object> queryForLikeList(VehPassrec veh,Map<String,Object> filter) throws Exception;
	//根据号牌号码和部分条件获取过车的轨迹视图
	public Map<String,Object> queryForLikePic(VehPassrec veh,Map<String,Object> filter) throws Exception;
	//根据号牌号码和部分条件获取最新的一条过车详细
	public Map queryForLikeLast(VehPassrec veh) throws Exception;
	//根据卡点编号获取卡点名称
	public String getKdmcByKdbh(String kdbh) throws Exception;
	//根据卡点编号前6位获取行政区划
	public String getXzqhByKdbh6(String kdbh6) throws Exception;
	//得到行政区划列表
	public List getXzqh();
	//得到卡口名称
	public List getKdmc();
	//得到方向列表
	public List getFxmc();
	
	//查询过车车辆查询
	public Map<String,Object> queryCarTrack(VehPassrec veh,Map<String,Object> filter) throws Exception ;
	//查询过车车辆查询分页
	public Map<String, Object> queryCarTrackPage(Map<String, Object> filter)
	throws Exception;
}