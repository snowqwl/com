package com.sunshine.monitor.system.analysis.zyk.dao.imp;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.zyk.bean.PoliceSituation;
import com.sunshine.monitor.system.analysis.zyk.dao.PoliceSituationDao;

@Repository("policeSituationDao")
public class PoliceSituationDaoImpl extends BaseDaoImpl implements PoliceSituationDao {

	@Override
	public int update(String sql, List<Object> params) throws Exception {
		return this.jdbcTemplate.update(sql, params.toArray());
	}

//	@Override
//	public Map<String,Object> queryForPoliceSituationMap(String sql, List<Object> params) {
//		return this.jdbcTemplate.queryForMap(sql, params.toArray());
//	}
}
