package com.sunshine.monitor.comm.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.comm.dao.PublicDao;

@Repository
public class PublicDaoImpl extends BaseDaoImpl implements PublicDao {

	public List CityReport() throws Exception {
		String sql = "select * from stat_system order by dwdm";
		return this.queryForList(sql, StatSystem.class);
	}

	public Map FullTextSearch(String search_text) throws Exception {
		String gate_sql = "SELECT KDBH AS XH,KDMC AS DMSM FROM CODE_GATE WHERE KKZT=1 AND  KDMC like '%"+search_text+"%' order by kdbh ";
		//List<Map<String,Object>> gatelist = this.jdbcTemplate.queryForList(gate_sql);

		Map map = this.findPageForMap(gate_sql, 1, 10);
		return map;
	}

}
