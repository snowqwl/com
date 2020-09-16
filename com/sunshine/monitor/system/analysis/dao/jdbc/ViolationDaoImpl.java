package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.dao.ViolationDao;

@Repository("violationDao")
public class ViolationDaoImpl extends BaseDaoImpl implements ViolationDao {

	public List getViolationList(String wflx, String hphm, String hpzl) {
		String sql = "select * from " + wflx + " where hphm = '" + hphm + "'"
				+ "and hpzl = '" + hpzl + "'";
		return this.jdbcTemplate.queryForList(sql);
	}
}
