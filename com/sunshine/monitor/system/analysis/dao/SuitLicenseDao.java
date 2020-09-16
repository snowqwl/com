package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

/**
 * 套牌库数据层操作接口
 * @author yingshaobu
 *
 */
public interface SuitLicenseDao extends BaseDao {

	/**
	 * 判断是否已存入套牌库
	 * @param filter
	 * @return false 未存入
	 * 		   true 已存入
	 */
	public boolean exitSuitLicense(Map<String, Object> filter);
	
	/**
	 * 套牌库插入
	 * @param sl
	 */
	public void insertSuitLicense2(Map sl);
	
	/**
	 * 改造 2016-9-13 liumeng
	 * @param filter
	 * @param veh
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> querySuitListExt(Map<String, Object> filter,VehPassrec veh) throws Exception;
	/**
	 * 改造 2016-9-13 liumeng
	 * @param filter
	 * @param veh
	 * @return
	 * @throws Exception
	 */
	public int getSuitTotal(Map<String,Object> filter,VehPassrec veh) throws Exception;
}
