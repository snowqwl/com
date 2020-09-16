package com.sunshine.monitor.system.query.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.query.bean.Surveil;
import com.sunshine.monitor.system.query.bean.VehAlarmrecIntegrated;
import com.sunshine.monitor.system.query.bean.VehPassrecIntegrated;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface IntegrateQueryManager {
	/**
	 * 
	 * 函数功能说明:集中库查询过车数据
	 * @param map
	 * @param info
	 * @param citys
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForIntegratePass(Map filter,VehPassrec info,String citys)throws Exception;

	/**
	 * 
	 * 函数功能说明：查询交通违法记录
	 * @param filter
	 * @param info
	 * @param citys
	 * @param wflxTab 
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map<String,Object> getMapForIntegrateTraffic(Map<String,Object> filter, VehPassrec info,
			String citys, String wflxTab)throws Exception;
	/**
	 * 集中库过车记录查询明细
	 * @param hphm
	 * @param kdbh
	 * @param gcsj
	 * @param cxlx 
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public VehPassrecIntegrated getPassIntegrateDetail(String hphm, String kdbh, String gcsj,String cxlx)throws Exception;
	/**
	 * 
	 * 函数功能说明：查询v_veh_alarmrec表信息
	 * @param filter
	 * @param info
	 * @param citys
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForIntegrateAlarm(Map filter,VehAlarmrecIntegrated info,String citys)throws Exception;
	/**
	 * 查询T_AP_VIO_SURVEIL表信息
	 * @param xh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public Surveil getSuvreitForXh(String xh);
	/**
	 * 查询T_AP_VIO_VIOLATION表信息
	 * @param wfbh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public List<Map<String, Object>> getViolationForWfbh(String dzwzId)throws Exception;
	/**
	 * 查询v_veh_alarmrec表信息
	 * @param bjxh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public VehAlarmrecIntegrated getAlarmIntegrateDetail(String bjxh)throws Exception;
	/**
	 * 查询T_AP_VIO_FORCE表信息
	 * @param xh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public List<Map<String, Object>> getForceForXh(String xh);
	
	/**
	 * 查询违法列表
	 * 缺少号牌种类，请使用getViolationDetail方法
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public Map<String,Object> getTrafficDetail(VehPassrec veh,Map<String,Object> map)throws Exception;
	
	public Map<String,Object> getViolationDetail(CarKey car,Map<String,Object> filter)throws Exception;

}
