package com.sunshine.monitor.system.query.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.query.bean.SuspMonitor;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;


public interface SuspinfoQueryManager {
	/**
	 * 查询布/管控信息
	 * @param filter
	 * @param info
	 * @param conSql
	 * @return
	 */
	public Map getSuspinfoMapForFilter(Map filter, VehSuspinfo info,String conSql)throws Exception ;
	
	/**
	 * 查询各地市的布控信息
	 * @param filter
	 * @param info
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public Map getCitySuspinfoMapForFilter(Map filter, VehSuspinfo info, String cityname)throws Exception;
	
	/**
	 * (根据参数)查询veh_suspinfo表信息
	 * @param filter
	 * @param hphm 号牌号码
	 * @param bkjg 布控申请机关
	 * @return
	 * @throws Exception
	 */
	public Map getSuspinfoMapForFilerByHphm(Map filter, String hphm, String bkjg)throws Exception;
	/**
	 * 查询JM_SUSPINFO_MONITOR表信息
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public List<SuspMonitor> getSuspMonitorListForBkxh(String bkxh)throws Exception ;
	/**
	 * 查询省厅KK库中是否存在当前地市的dblink
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public int getAllDbLink(String cityname)throws Exception;
	/**
	 * 布撤控信息查询中增加布控审批人
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public String getBksprByBkxh(String bkxh) throws Exception;
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 获取联动布控反馈列表
	 * @param bkxh 布控序号
	 * @param bkjg 布控结果：已布控，已撤控
	 * @return
	 * @throws Exception
	 */
	public List<SuspMonitor> getSuspMonitorList(String bkxh, String bkjg)throws Exception;

}
