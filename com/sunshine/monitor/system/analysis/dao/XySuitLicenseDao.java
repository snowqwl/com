package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.bean.SuitLicense;
import com.sunshine.monitor.system.analysis.bean.WhilteName;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface XySuitLicenseDao extends BaseDao {
	
	
	public int insertWhite(WhilteName wn);

	public int isHaveWhite(String hphm,String hpzl) ;

	public int deleteWhite(String hphm,String hpzl);
	
	public List<Map>  queryhpzl() throws Exception;
	
	public Map<String, Object> whitelist(Map<String, Object> conditions);
	/**
	 * 套牌分析，查询一天内通过的车牌
	 * @param curPage
	 * @param total
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public Map getHphm(int curPage, int total, String kssj ,String jssj);
	
	/**
	 * 套牌分析，根据号牌号码查询过车列表信息
	 * @param hphm
	 * @param hpzl
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public List getListByHphm(String hphm,String hpzl, String kssj, String jssj);
	
	/**
	 * 套牌库插入
	 * @param sl
	 */
	public void insertSuitLicense(SuitLicense sl);
	
	/**
	 * 统计
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public Map getCount(Map filter,String kssj, String jssj);
	
	/**
	 * 套牌统计
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public Map getSuitCount(Map filter,String kssj,String jssj, String hpzl);
	
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
	 * 更新套牌库，更新状态
	 * @param condition
	 * @param zt
	 */
	public void updateSuitLicense(Map fifter,String zt);
	
	/**
	 * 获取某号牌的的嫌疑轨迹组列表
	 * @param hphm
	 * @param sj
	 * @param zt
	 * @return
	 */
	public Map getSuitList(Map filter, String hphm,String hpzl, String kssj, String jssj, String zt);
	
	/**
	 * 获取套牌库中某车牌的过车轨迹
	 * @param filter
	 * @param suitLicense
	 * @return
	 */
	public Map getSuitVehList(Map filter,SuitLicense suitLicense);
	
	/**
	 * 根据条件查询套牌组合列表
	 * @param request
	 * @return
	 */
	public Map querySuitList(Map filter,VehPassrec veh) throws Exception;
	
	/**
	 * 嫌疑套牌统计
	 * @param filter
	 * @param kssj
	 * @param jssj
	 * @param zt
	 * @param hphm
	 * @param hpzl
	 * @param hpys
	 * @return
	 */
	public Map getXySuit(Map filter, String kssj, String jssj, String zt, String hphm, String hpzl, String hpys);
	
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
	public List<Map<String,Object>> getXySuitExt(Map<String,Object> filter, String kssj, String jssj, String zt, String hphm, String hpzl, String hpys) throws Exception;
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
	public int getXySuitTotal(Map<String,Object> filter, String kssj, String jssj, String zt, String hphm, String hpzl, String hpys) throws Exception;
	
	/**
	 * 获取行政区划名称
	 * @param xzqh
	 * @return
	 */
	public Map<String,Object> getXzqhmc(String xzqh);
	
	/**
	 * 获取市州
	 * @return
	 */
	public List<Map<String,Object>> getSz();
	
	/**
	 * 获取研判意见
	 * @param fifter
	 * @return
	 */
	public Map<String,Object> getYpyj(Map<String,Object> fifter);
}


