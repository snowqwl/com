package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.DayNightAnalysis;
import com.sunshine.monitor.system.analysis.bean.DayNigntNewAnaiysis;
import com.sunshine.monitor.system.analysis.dao.DaysNightAnalysisDao;

@Repository("DaysNightAnalysisDao")
public class DaysNightAnalysisDaoImpl extends BaseDaoImpl  implements DaysNightAnalysisDao {
	public int saveDayNightGz(DayNightAnalysis info) throws Exception {

		String seSql = "SELECT SEQ_JM_DAYNIGHT_REG.nextval from dual";
		String gzxh = this.jdbcTemplate.queryForObject(seSql, String.class);

		String isExit = "SELECT count(*) from JM_DAYNIGHT_REG where gzxh = '"
				+ gzxh + "'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is == 0) {
			String insertSql = "INSERT into JM_DAYNIGHT_REG(GZXH, GZNAME,DAYKSSJ, DAYJSSJ, PRENIGHTKSSJ, PRENIGHTJSSJ, NEXTNIGHTKSSJ,NEXTNIGHTJSSJ)values"
					+ "('"
					+ gzxh
					+ "', '"
					+ info.getGzname()
					+ "', '"
					+ info.getDaykssj()
				
					+ "', '"
					+ info.getDayjssj()
				
					+ "', '"
					+ info.getPrenightkssj()
				
					+ "', '"
					+ info.getPrenightjssj()
			
					+ "', '"
					+ info.getNextnightkssj()
			
					+ "', '"
					+ info.getNextnightjssj()
				
					//+ "', '"
					//+info.getValid()
					+ "')";
			int result = this.jdbcTemplate.update(insertSql);
			if (result == 1) {
				return 1;
			}
		}
		return 0;
	}
	public List<DayNightAnalysis>  getDqValid() throws Exception{
		
		String sql = "select * from JM_DAYNIGHT_REG where valid='1'";
		return this.queryForList(sql,DayNightAnalysis.class);
	}
	public Map<String, Object> queryGZList(Map<String, Object> conditions) throws Exception {
		StringBuffer sql = new StringBuffer(20);
		sql.append("select * from JM_DAYNIGHT_REG ");
		
		//System.out.println(sql);
		Map<String, Object> map = findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public int delDayNightAna(String gzxh) throws Exception {
		String isExit = "SELECT count(*) from JM_DAYNIGHT_REG where gzxh = '"
				+ gzxh + "'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is > 0) {
			String upSql = "DELETE from JM_DAYNIGHT_REG "					
					+ "where GZXH = '" + gzxh + "'";
			return this.jdbcTemplate.update(upSql);
		}
		return 0;
	}
	public DayNightAnalysis getEditDaynightInfo(String gzxh)
	throws Exception {
		DayNightAnalysis dayNightAnalysis = null;
		String sql = "select * from JM_DAYNIGHT_REG where gzxh= '"
		+ gzxh+"'";
		List<DayNightAnalysis> list = this.queryForList(sql, DayNightAnalysis.class);
		if(list.size() > 0){
			dayNightAnalysis = list.get(0);
		}
		return dayNightAnalysis;
		}
	public int editDayNightGz(DayNightAnalysis info) throws Exception {
		String isExit = "SELECT count(*) from JM_DAYNIGHT_REG where gzxh = '"
				+ info.getGzxh() + "'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is > 0) {
			String upSql = "UPDATE JM_DAYNIGHT_REG set GZNAME = '"
					+ info.getGzname()
					+ "', DAYKSSJ =  '"
					+ info.getDaykssj()
					+ "', DAYJSSJ = '"
					+ info.getDayjssj()
					+ "', PRENIGHTKSSJ = '"
					+ info.getPrenightkssj()
					+ "',PRENIGHTJSSJ = '"
					+ info.getPrenightjssj()
					+ "',NEXTNIGHTKSSJ = '"
					+ info.getNextnightkssj()
					+ "',NEXTNIGHTJSSJ = '"
					+ info.getNextnightjssj()
				    + "' where GZXH='"
					+ info.getGzxh()
					+"'";
			return this.jdbcTemplate.update(upSql);
		}
		return 0;
	}
	
	public int editZt(DayNightAnalysis info,String zt) throws Exception {
		String isExit = "SELECT count(*) from JM_DAYNIGHT_REG where gzxh = '"
				+ info.getGzxh() + "'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is > 0) {
			String upSql = "UPDATE JM_DAYNIGHT_REG set VALID = '"
					+ zt			
				    + "' where GZXH='"
					+ info.getGzxh()
					+"'";
			return this.jdbcTemplate.update(upSql);
		}
		return 0;
	}
	
	public int editOtherZt() throws Exception {
		String isExit = "SELECT count(*) from JM_DAYNIGHT_REG where valid = '1'";
		int is = this.jdbcTemplate.queryForInt(isExit);
		if (is > 0) {
			String upSql = "UPDATE JM_DAYNIGHT_REG set VALID = '0'"		
				    + " where GZXH=(select gzxh from JM_DAYNIGHT_REG where valid='1')";
			return this.jdbcTemplate.update(upSql);
		}
		return 0;
	}
	public DayNightAnalysis getZyxx() throws Exception {
		String sql = "SELECT * FROM JM_DAYNIGHT_REG WHERE valid = '1'";
		List<DayNightAnalysis> list = this.queryForList(sql, DayNightAnalysis.class);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	
	public Map<String,Object> queryList(DayNigntNewAnaiysis dn,Map<String, Object> filter)throws Exception{
		
		return null;
	}
}
