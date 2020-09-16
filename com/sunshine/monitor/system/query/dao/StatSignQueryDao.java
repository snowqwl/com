package com.sunshine.monitor.system.query.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface StatSignQueryDao extends BaseDao {

	/**
	 * 签收统计处置统计
	 * @param kssj
	 * @param jssj
	 * @param glbm
	 * @param bkdl
	 * @return
	 */
	public List<Map<String,Object>> getSignQuery(String kssj,String jssj,String glbm,String bkdl,String jb);
}
