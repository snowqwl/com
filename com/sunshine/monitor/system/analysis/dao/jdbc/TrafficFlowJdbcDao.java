package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.dao.TrafficFlowDao;

@Repository("TrafficFlowJdbcDao")
public class TrafficFlowJdbcDao extends ScsBaseDaoImpl implements
		TrafficFlowDao {

	public int copyDatasTotempTable(String condition, String tableName) {
		String sql = "create table "
				+ tableName
				+ " SELECT GCXH,KDBH,FXBH,HPZL,HPHM,GCSJ,HPYS,CLLX,CSYS,CDBH,RKSJ FROM "+Constant.SCS_PASS_TABLE+" where "
				+ condition;
		return this.jdbcScsTemplate.update(sql);
	}

	public int deleteTempTable(String tableName) {

		return this.jdbcScsTemplate.update("drop table if exists " + tableName);
	}

	public <T> List<? extends Object> queryTotalFlowPeerDay(String tableName,
			String condition, Object... args) throws Exception {
		/*String sql = "select to_char(t.gcsj,'yyyy-mm') rp, to_char(t.gcsj,'dd') rt, count(t.gcxh) total from ("
				   + " SELECT GCXH,KDBH,FXBH,HPZL,HPHM,GCSJ,HPYS,CLLX,CSYS,CDBH,RKSJ FROM veh_passrec where "
				   + tableName+")t where "+condition+" group by to_char(t.gcsj,'yyyy-mm'),to_char(t.gcsj,'dd')";*/
		StringBuffer sql = new StringBuffer("select lyear || '-' || lmonth as rp , lday as  rt, SUM(gcl) as total from " + tableName+" where ");
				     sql.append(condition);
				     sql.append(" group by lyear, lmonth, lday");
		return this.jdbcScsTemplate.queryForList(sql.toString(), args);
	}

	public <T> List<? extends Object> queryTotalFlowPeerHour(String tableName,
			String condition, Object... args) throws Exception {
		String sql = "select to_char(to_date(lyear || '-' || lmonth || '-' || lday,'yyyy-MM-dd'),'yyyy-MM-dd') as rp, lhour as rt, SUM(gcl) as total from "+tableName+" where "
				  + condition + " group by lyear, lmonth, lday, lhour";
		return this.jdbcScsTemplate.queryForList(sql, args);
	}
	

	public int createTableIdx(String tableName, String idxName, String field) {
		String sql = "create index " + idxName + " on " + tableName + "("
				+ field + ")";
		return this.jdbcScsTemplate.update(sql);
	}

	public <T> List<? extends Object> queryTotalFlowPeerDay(String tableName,
			String condition) throws Exception {
		String sql = "select DATE_FORMAT(t.gcsj,'%Y-%m') rp, DATE_FORMAT(t.gcsj,'%d') rt, count(1) total from t("
				+ tableName + "::" + condition + ") group rp,rt";
		return this.jdbcScsTemplate.queryForList(sql);
	}

	public <T> List<? extends Object> queryTotalFlowPeerHour(String tableName,
			String condition) throws Exception {
		String sql = "select DATE_FORMAT(t.gcsj,'%Y-%m-%d') rp, DATE_FORMAT(t.gcsj,'%H') rt, count(t.gcxh) total from t("
				+ tableName + "::" + condition + ") group rp,rt";
		return this.jdbcScsTemplate.queryForList(sql);
	}

	public <T> List<? extends Object> queryTotalFlow(String sql, Object[] args)
			throws Exception {

		return this.jdbcScsTemplate.queryForList(sql, args);
	}

	public int copyDatasTotempTable(String condition, String tableName,
			Object[] args) {
		String sql = "create table if not exists "
				+ tableName
				+ " SELECT GCXH,KDBH,FXBH,HPZL,HPHM,GCSJ,HPYS,CLLX,CSYS,CDBH,RKSJ FROM "+Constant.SCS_PASS_TABLE+" where "
				+ condition;
		return this.jdbcScsTemplate.update(sql, args);
	}

	public boolean isExstisTable(String name) throws Exception {
		List<Map<String, Object>> list = this.jdbcScsTemplate
				.queryForList("show tables like '" + name + "'");
		if(list.size() > 0){
			return true;
		}
		return false;
	}
}
