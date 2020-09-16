package com.sunshine.monitor.system.query.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface CollectVehManager {
	
	/**
	 * 查询关注的车辆列表
	 * @param page
	 * @param info
	 * @param conSql
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCollectVehMapForFilter(Page page,VehSuspinfo info,String conSql)throws Exception;
	
	/**
	 * 新增关注车辆
	 * @param user
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public int saveCollectVeh(SysUser user,VehSuspinfo info)throws Exception;
	
	/**
	 * 新增关注车辆
	 * @param user
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public int update(SysUser user,VehSuspinfo info)throws Exception;
	
	/**
	 * 修改关注车辆状态为无效
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public int editCollectVeh(String hphm,String hpzl,String status)throws Exception; 
	
	/**
	 * 判断关注车牌总数是否超过20条
	 * @param jh
	 * @return
	 * @throws Exception
	 */
	public int checkMaxCount(String jh) throws Exception;
	
	/**
	 * 查询关注车辆列表前10条显示在主页上
	 * @param conSql
	 * @return
	 * @throws Exception
	 */
	public List<VehPassrec> getCollectVehForMainPage(String conSql)throws Exception;
	
	/**
	 * 设置车辆是否已读
	 * @param hphm
	 * @param hpzl
	 * @param isread
	 * @return
	 * @throws Exception
	 */
	public int editCollectVehIsRead(String hphm,String hpzl,String isread)throws Exception;
	
	/**
	 * 判断号牌号码是否已经存在
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public int checkHphm(String jh,String hphm,String hpzl)throws Exception;
	
	/**
	 * 获得单个关注车辆的详细信息
	 * @param jh
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo findVehCollect(String jh,String hphm,String hpzl)throws Exception;
	
}
