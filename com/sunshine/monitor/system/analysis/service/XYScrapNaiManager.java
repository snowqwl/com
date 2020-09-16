package com.sunshine.monitor.system.analysis.service;

import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface XYScrapNaiManager {
	
	/**
	 * 嫌疑报废、逾期未年检车辆查询
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findXyForPage(ScsVehPassrec veh, Map<String,Object> filter) throws Exception;
	
	/**
	 * <2016-11-23>licheng-嫌疑报废、逾期未年检车辆查询--显示过车次数
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findXyCsForPage(ScsVehPassrec veh, Map<String,Object> filter) throws Exception;
	
	
	/**
	 * 根据号牌号码和部分参数获取嫌疑库中的过车轨迹列表
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
//	public Map<String,Object> queryForXYList(VehPassrec veh,Map<String,Object> filter) throws Exception;
	
	/**
	 * 根据号牌号码和号牌种类在过车记录库中查询车辆信息
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findXYCarInfo(String id,String hphm,String hpzl) throws Exception;
	
	/**
	 * 根据号牌号码和号牌种类在过车记录库中查询车辆信息
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findXYCarInfo(String id,String hphm,String hpzl,String gcxh) throws Exception;
	
	/**
	 * 根据号牌号码和号牌种类在过车记录库中查询车辆信息
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getVehDetail(String hphm,String hpzl)throws Exception;
	
	/**
	 * 根据号牌号码和号牌种类在嫌疑库中查询车辆信息
	 * @param status 状态（1已操作，2：数据作废）
	 * @param xyids 修改记录IDs
	 * @return
	 * @throws Exception
	 */
	public int editXYStatus(String status,String xyids) throws Exception;
	
	/**
	 * 入库-新增记录到JM_ZYK_SCRAP_NAI (报废/逾期未年检库)
	 * @param xyid 嫌疑库主键ID
	 * @return
	 * @throws Exception
	 */
//	public int editScrapNaiInfo(String xyId) throws Exception;
	
	
	
		
}
