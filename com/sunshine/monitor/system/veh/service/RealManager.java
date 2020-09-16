package com.sunshine.monitor.system.veh.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.veh.bean.PropertyBean;
import com.sunshine.monitor.system.veh.bean.VehRealpass;


public interface RealManager {
	
	public abstract VehRealpass getVehRealPassM(String paramString, String fxbh, String cdbh);
	
	/**
	 * <2016-11-3> licheng 从redis中查询卡口实时过车数据 
	 * @param paramString
	 * @param fxbh
	 * @param cdbh
	 * @return
	 */
	public abstract VehRealpass getVehRealPassRedis(String paramString, String fxbh, String cdbh);
	
	/**
	 * <2016-11-9> licheng 从redis中查询多卡口实时过车数据
	 * @param kds
	 * @return
	 */
	public abstract List getRealPassListRedis(String[] kds);

	public abstract Map findAlarmForMap(Map map, PropertyBean bean)throws Exception;
	/**
	 * 查询实时过车信息
	 * @param kds
	 * @return
	 */
	public abstract List getRealPassList(String[] kds);
	/**
	 * (根据参数)查询Veh_passrec表信息
	 * @param gcxh
	 * @return
	 */
	public abstract VehRealpass getVehRealDetail(String gcxh);
	
	/**
	 * (根据参数)查询Veh_passrec表信息
	 * @param gcxh
	 * @return
	 */
	//public abstract VehRealpass getVehRealDetailByParams(String kdbh,String fxbh,String cd);
	public abstract VehRealpass getVehRealDetailByParams(String gcxh);
	
	/**
	 * 查询某卡口今日过车总数
	 * @oy
	 * @param kdbh
	 * @param cdbh 
	 * @param fxbh 
	 * @return
	 */
	public long getVehpassByKdbh(String kdbh, String fxbh, String cdbh);
	/**
	 * 查询实时监控信息
	 * @param kdbh
	 * @param fxbh
	 * @return
	 */
	public abstract VehRealpass getJianKongInfo(String kdbh,String fxbh);
	
	
	public long getDiffTime(String time) throws Exception ;
}
