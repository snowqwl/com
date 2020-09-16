package com.sunshine.monitor.comm.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.SysUser;

public interface MainPageManager {
	public Map getDataCountInfo(HttpServletRequest request,
			HttpServletResponse response, UserSession userSession, SysUser user);
	
	public Map getDataCountDefereInfo(HttpServletRequest request,
			HttpServletResponse response, UserSession userSession, SysUser user);

	/**
	 * @Description:获取规划登记表（JM_GATE_PROJECT）数据，生成线状图
	 * @author: TDD
	 * @date: 2014-10-13 下午03:46:23
	 */
	public Map getGateProjectData(HttpServletRequest request,
			HttpServletResponse response, UserSession userSession, SysUser user);
   /**
    * 全省卡口备案情况
    * @return
    * @throws Exception
    */
	public Map getGateInCount() throws Exception;
	
	
	/**
	 * 城市卡口接入情况
	 * @return
	 * @throws Exception
	 */
	public Map getGateInCountByCity(String glbm) throws Exception;

	/**
	 * 流量统计
	 * 
	 * @param userSession
	 * @param user
	 * @return
	 */
	public Map getFlow(UserSession userSession, SysUser user);
	
	/**
	 * 最近一个月与上个月环比(用户数、查询数、布控数、卡口数)----按公安月环比
	 * @return
	 */
	public Map getPoliceRatio(Map condition);

}
