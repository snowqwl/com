package com.sunshine.monitor.system.veh.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.ws.VehPassrecEntity;

public interface HphmpassDao extends BaseDao {
	
	/**
	 * 用户查询提示
	 * @param condition
	 * @return
	 */
	public int queryTips(Map condition) ;
	
	/**
	 * 查询过车信息列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecList(Map<String, Object> conditions, String tableName) throws Exception;
	
}
