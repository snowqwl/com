package com.sunshine.monitor.system.analysis.zyk.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.zyk.bean.PoliceSituation;

public interface PoliceSituationDao extends BaseDao{
	public int update(String sql,List<Object> params) throws Exception;
//	public Map<String,Object> queryForPoliceSituationMap(String sql, List<Object> params);
}
