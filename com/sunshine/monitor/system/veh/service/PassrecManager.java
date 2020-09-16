package com.sunshine.monitor.system.veh.service;

import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.dom4j.DocumentException;

import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.ws.VehPassrecEntity;
import com.sunshine.monitor.system.ws.util.NotDefPaserException;

public interface PassrecManager {
	/**
	 * 查询过车信息列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecList(Map<String, Object> conditions) throws Exception; 
	
	/**
	 * 查询过车信息列表（含交警卡口）
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecAndJjList(Map<String, Object> conditions) throws Exception; 
	
	
	
	/**
	 * 根据号牌号码查询过车信息列表(模糊查询)
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecListByHphm(Map<String, Object> conditions) throws Exception;
    
	/**
	 * 查询过车详细信息
	 * @param gcxh
	 * @return
	 * @throws Exception
	 */
	public VehPassrec getVehPassrecDetail(String gcxh) throws Exception;
	
  
	/**
	 * 过车数据导出
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> exportPassrecList(Map<String, Object> conditions) throws Exception;

	
	/**
	 * 查询一定数量的过车信息
	 * 功能:全省漫游查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<VehPassrecEntity> queryPassrecLimitedList(String sql) throws Exception;
	
	/**
	 * 使用axis2 对 /dc 系统 进行漫游查询
	 * @return
	 */
	public List<VehPassrecEntity> queryPassrecDataByAxis2(CodeUrl codeUrl,
			VehPassrec vehPassrec)throws AxisFault, DocumentException, 
			NotDefPaserException ;
	
	/**
	 * 查询当月中的过车数量
	 * @return
	 */
	public int getPassrecCountInThisMonth() throws Exception;
		
	/**
	 * 用户查询提示
	 * @param condition
	 * @return
	 */
	public int queryTips(Map condition);
	
	
	/**
	 * 用户查询提示（含交警过车查询）
	 * @param condition
	 * @return
	 */
	public int queryTipsAndJj(Map condition) throws Exception;
		
	/**
	 * 统计查询总数
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public int getAllCount(Map<String,Object> conditions) throws Exception;
	
	/**
	 * 统计查询总数(含交警卡口)
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public int getPassAndJjAllCount(Map<String,Object> conditions) throws Exception;
	
	/**
	 * 导出过车信息列表前200条写入系统目录下
	 * @param conditions
	 * @return url
	 * @throws Exception
	 */
	public String PassrecAndJjListFileUrl(Map<String,Object> condition) throws Exception;
	
	//==============================过车查询新改造===============================================
	/**
	 * 获取过车总记录数
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public int getPassCount(Map<String, Object> condition) throws Exception;
	/**
	 * 查询过车列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPasslists(Map<String, Object> conditions) throws Exception;
	
	/**
	 * <2016-12-19> licheng 精确号牌,查询第一页不计总数
	 * 查询过车列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPasslistsExt(Map<String, Object> conditions) throws Exception;

	public Map<String, Object> getPasslistsExtDS(Map<String, Object> conditions) throws Exception;
	
	public Integer queryPassAndJjlist3Total(Map<String, Object> conditions) throws Exception;
	/**
	 * 查询过车列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPasslistsNoCount(Map<String, Object> conditions) throws Exception;
	
	public Map<String, Object> getPasslistsNoCountDS(Map<String, Object> conditions) throws Exception;
	
}
