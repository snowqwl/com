package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.sunshine.monitor.system.analysis.bean.SuitLicense;
import com.sunshine.monitor.system.analysis.bean.WhilteName;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface SuitLicenseManager {
	
	public int insertWhite(WhilteName wn);

	public Map<String,Object> deleteWhite(JSONArray list) ;
	
	public List<Map>  queryhpzl() throws Exception;
	
	public Map whiteList(Map condition) throws Exception;
	
	/**
	 * 查询套牌库统计
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public Map querySuitLicense(Map filter, String kssj, String jssj) ;
	
	/**
	 * 查询嫌疑套牌库统计
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @param zt
	 * @param hphm
	 * @param hpzl
	 * @param hpys
	 * @return
	 */
	public Map queryXySuitLicense (Map filter, String kssj, String jssj, String zt, String hphm, String hpzl, String hpys) throws Exception;
	
	/**
	 * 改造 2016-9-13 liumeng
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @param zt
	 * @param hphm
	 * @param hpzl
	 * @param hpys
	 * @return
	 */
	public List<Map<String, Object>> queryXySuitLicenseExt(Map<String, Object> filter, String kssj, String jssj, String zt, String hphm, String hpzl, String hpys) throws Exception;
	
	/**
	 * 改造 2016-9-13 liumeng
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @param zt
	 * @param hphm
	 * @param hpzl
	 * @param hpys
	 * @return
	 */
	public int getXySuitTotal(Map<String, Object> filter, String kssj, String jssj, String zt, String hphm, String hpzl, String hpys) throws Exception;
	
	/**
	 * 嫌疑套牌统计详情
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @param zt
	 * @return
	 */
	public Map getXySuit(Map filter,String kssj,String jssj,String zt, String hpzl);
	
	/**
	 * 获取某号牌号码的轨迹组列表
	 * @param hphm
	 * @param kssj
	 * @param jssj
	 * @param zt
	 * @return
	 * @throws Exception 
	 */
	public Map getSuitList(Map filter, String hphm,String hpzl,String kssj, String jssj, String zt) throws Exception;
	
	/**
	 * 套牌统计
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public Map getSuitCount(Map filter, String kssj, String jssj,String hpzl);
	
	/**
	 * 轨迹列表
	 * @param filter
	 * @param suitLicense
	 * @return
	 * @throws Exception
	 */
	public Map getSuitVehList(Map filter,SuitLicense suitLicense) throws Exception;
	
	/**
	 * 更新套牌库
	 * @param suitLicense
	 * @param newzt
	 */
	public void updateSuitLicense(SuitLicense suitLicense,String newzt);
	
	/**
	 * oracle中更新套牌库
	 * @param suitLicense
	 * @param newzt
	 */
	public void updateSuitLicenseOracle(Map fifter,String newzt);
	
	/**
	 * 根据条件查询套牌组合列表
	 * @param request
	 * @return
	 */
	public Map querySuitList(Map filter,VehPassrec veh) throws Exception;
	
	
	/**
	 * 改造 2016-9-13 liumeng
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @param zt
	 * @param hphm
	 * @param hpzl
	 * @param hpys
	 * @return
	 */
	public List<Map<String, Object>> querySuitListExt(Map<String, Object> filter,VehPassrec veh) throws Exception;
	
	/**
	 * 改造 2016-9-13 liumeng
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @param zt
	 * @param hphm
	 * @param hpzl
	 * @param hpys
	 * @return
	 */
	public int getSuitTotal(Map<String, Object> filter,VehPassrec veh) throws Exception;
}
