package com.sunshine.monitor.system.query.dao;

import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface SpecialCarDao extends BaseDao {

	/**
	 * 查询特殊车辆库列表
	 * 
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map querySpecialList(Map conditions) throws Exception;

}
