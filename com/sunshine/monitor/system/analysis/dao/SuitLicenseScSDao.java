package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.analysis.bean.SuitLicense;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface SuitLicenseScSDao extends ScsBaseDao {
	
	/**
	 * 套牌分析，查询一天内通过的车牌
	 * @param curPage
	 * @param total
	 * @param sj
	 * @return
	 */
	public Map getHphm(int curPage, int total, String sj,String tablename);
	
	/**
	 * 套牌分析，根据号牌号码查询过车列表信息
	 * @param hphm
	 * @param hpzl
	 * @param kssj
	 * @param jssj
	 * @return
	 * @throws Exception 
	 */
	public List getListByHphm(String hphm,String hpzl, String kssj, String jssj) throws Exception;
	
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
	public void updateSuitLicense(SuitLicense suitLicense,String zt);
	
	/**
	 * 获取某号牌的的嫌疑轨迹组列表
	 * @param hphm
	 * @param sj
	 * @param zt
	 * @return
	 */
	public Map getSuitList(Map filter, String hphm,String hpzl,String sj,String zt);
	
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

}
