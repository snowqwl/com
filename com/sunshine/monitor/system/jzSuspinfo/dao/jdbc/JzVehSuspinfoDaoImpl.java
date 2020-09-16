package com.sunshine.monitor.system.jzSuspinfo.dao.jdbc;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.DBaseDaoImpl;
import com.sunshine.monitor.system.jzSuspinfo.bean.JzVehSuspinfo;
import com.sunshine.monitor.system.jzSuspinfo.dao.JzVehSuspinfoDao;

@Repository("jzVehSuspinfoDao")
public class JzVehSuspinfoDaoImpl extends DBaseDaoImpl<JzVehSuspinfo> implements JzVehSuspinfoDao {

	public List<JzVehSuspinfo> getJzVehSuspinfoList(Date startDate, Date endDate) {
		String sql = "select * from jz_veh_suspinfo where gxsj < ? and gxsj > ? " +
				"and ysbh not in (select distinct ysbh from synchronization_error)";
		return this.jdbcTemplate.query(sql, new Object[]{endDate,startDate}, 
				new JdbcAutoRowMapper<JzVehSuspinfo>(JzVehSuspinfo.class));
	}
	
	@Override
	public String getTableName() {
		return "jz_veh_suspinfo";
	}

}
