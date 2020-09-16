package com.sunshine.monitor.system.query.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface CollectVehDao extends BaseDao{
	
	/**
	 * 查询关注的车辆列表
	 * @param page
	 * @param info
	 * @param conSql
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getCollectVehList(Page page,VehSuspinfo info,String conSql)throws Exception;

	/**
	 * 编辑关注车辆信息
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public int insert(SysUser user,VehSuspinfo info)throws Exception;
	
	/**
	 * 修改关注车辆状态
	 * @param hphm
	 * @param hpzl
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public int update(String hphm,String hpzl,String status)throws Exception;
	
	/**
	 * 修改关注车牌是否已读
	 * @param hphm
	 * @param hpzl
	 * @param isread
	 * @return
	 * @throws Exception
	 */
	public int updateIsRead(String hphm, String hpzl, String isread) throws Exception;
	
	/**
	 * 查询当前用户关注的车牌总数
	 * @param jh
	 * @return
	 * @throws Exception
	 */
	public int checkMaxCount(String jh) throws Exception;
	
	/**
	 * 查询前十条关注车辆的数据显示在主页
	 * @param conSql
	 * @return
	 * @throws Exception
	 */
	public List<VehPassrec> queryCollectVehList(String conSql)throws Exception;
	
	/**
	 * 获得最新的过车时间
	 * @param hphm
	 * @param hpzl
	 * @param gcsj
	 * @return
	 * @throws Exception
	 */
	public int checkLastGcsj(String hphm, String hpzl,String gcsj) throws Exception;
	
	/**
	 * 查询号牌号码是否已经存在	
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public int checkByHphm(String jh,String hphm, String hpzl) throws Exception;
	
	/**
	 * 获取单个关注车辆的详细信息
	 * @param jh
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getVehCollect(String jh,String hphm, String hpzl) throws Exception;
	
	/**
	 * 编辑关注车辆信息
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public int edit(SysUser user,VehSuspinfo info)throws Exception;
}


