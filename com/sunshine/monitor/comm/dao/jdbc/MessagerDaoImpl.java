package com.sunshine.monitor.comm.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Messager;
import com.sunshine.monitor.comm.dao.MessagerDao;

@Repository("messagerDao")
public class MessagerDaoImpl extends BaseDaoImpl implements MessagerDao {

	public List<Messager> getMessagerInfo() throws SQLException {
		List<Messager> list = new ArrayList<Messager>();
		StringBuffer sb = new StringBuffer(" Select CSTR_COL02 as KDMC,CSTR_COL01 as KDBH, CSTR_COL05 as FXBH, DATE_COL01 as GCSJ,CSTR_COL04 as zdcxsj");
		sb.append(" from monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID = '36' and workItem_ID in (");
		sb.append(" Select max(to_number(workItem_ID)) from monitor.T_MONITOR_JG_INTEGRATE_DETAIL ");
		sb.append(" Where KHFA_ID='36' group by date_col02,CSTR_COL06)");
		sb.append("  AND trunc(CQSJ) = trunc(SYSDATE) ");
		DataSource ds = this.jdbcTemplate.getDataSource();
		Connection connection = ds.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Messager ms = new Messager();
		    ms.setKdmc(rs.getString("KDMC"));
		    ms.setKdbh(rs.getString("KDBH"));
		    ms.setFxbh(rs.getString("FXBH"));
		    ms.setGcsj(rs.getTimestamp("GCSJ"));
		    ms.setDzsj(rs.getString("ZDCXSJ"));
		    ms.setFxbh(rs.getString("FXBH"));
		    list.add(ms);
		}
		
		DataSourceUtils.releaseConnection(connection, ds);
		return list;
	}
	
	public List getStatHbInfo() throws Exception {
		
		StringBuffer sb = new StringBuffer(" select  to_char(a.TJRQ,'yyyy-mm-dd') as TJRQ , ");
		sb.append(" a.kdbh,a.fxbh,a.gcsl as tgcsl,a.sbsl as tsbsl,a.btsbsl as tbtsbsl, ");
		sb.append(" a.ywsbsl as tywsbsl,a.btsbzs as tbtsbzs, a.ywsbzs as tywsbzs, nvl(b.btsbsl, 0) as y_btsbsl, ");
		sb.append("  nvl(b.ywsbsl, 0) as y_ywsbsl, nvl(b.btsbzs, 0) as y_btsbzs, nvl(b.ywsbzs, 0) as y_ywsbzs ");
		sb.append(" from (Select CSTR_COL02 as kdbh,CSTR_COL03 as fxbh, ");
		sb.append("  TRUNC(DATE_COL01, 'DD') as tjrq,sum(nvl(CSTR_COL04, 0)) gcsl, sum(nvl(CSTR_COL05, 0)) sbsl, ");
		sb.append(" sum(nvl(CSTR_COL06, 0)) btsbsl,sum(nvl(CSTR_COL07, 0)) ywsbsl,sum(nvl(CSTR_COL08, 0)) btsbzs, ");
		sb.append("  sum(nvl(CSTR_COL09, 0)) ywsbzs   from monitor.T_MONITOR_JG_INTEGRATE_DETAIL ");
		sb.append("  Where KHFA_ID = '35'  and to_char(DATE_COL01,'yyyy-mm-dd') = to_char(sysdate - 1,'yyyy-mm-dd') ");
		sb.append(" group by CSTR_COL02, CSTR_COL01, CSTR_COL03, TRUNC(DATE_COL01, 'DD')) a ");
		sb.append("  left join (Select CSTR_COL02 as kdbh, CSTR_COL03 as fxbh, ");
		sb.append("    TRUNC(DATE_COL01, 'DD') as tjrq, sum(nvl(CSTR_COL04, 0)) gcsl, sum(nvl(CSTR_COL05, 0)) sbsl, ");
		sb.append("   sum(nvl(CSTR_COL06, 0)) btsbsl,sum(nvl(CSTR_COL07, 0)) ywsbsl, sum(nvl(CSTR_COL08, 0)) btsbzs, ");
		sb.append("    sum(nvl(CSTR_COL09, 0)) ywsbzs from monitor.T_MONITOR_JG_INTEGRATE_DETAIL ");
		sb.append("   Where KHFA_ID = '35'  and to_char(DATE_COL01,'yyyy-mm-dd') =  to_char(sysdate - 2,'yyyy-mm-dd') ");
		sb.append("   group by CSTR_COL02, CSTR_COL03, TRUNC(DATE_COL01, 'DD')) b ");
		sb.append("   on a.kdbh = b.kdbh  and a.fxbh = b.fxbh  and (a.tjrq - 1) = b.tjrq ");
		
		
		return this.jdbcTemplate.queryForList(sb.toString());
	}


}
