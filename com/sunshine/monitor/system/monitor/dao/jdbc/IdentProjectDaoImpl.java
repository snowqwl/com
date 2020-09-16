package com.sunshine.monitor.system.monitor.dao.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.monitor.dao.IdentProjectDao;

@Repository("identProjectDao")
public class IdentProjectDaoImpl extends BaseDaoImpl implements IdentProjectDao {
	
	public List<Map<String,Object>> getIdentProjectInfo(String kssj,String jssj,String kdbhs) {
		List<List<String>> liststr = null;
		StringBuffer sb = new StringBuffer("Select cdg.kdbh,cdg.fxmc,nvl(btsl,0) btsl,nvl(ywsl,0) ywsl");
		sb.append(" from (Select ab.kdbh, ab.fxmc, ab.fxbh from (Select kdbh, fxbh, fxmc  from code_gate_extend Where kdbh in (" );
		if (kdbhs != null && kdbhs.length() > 0) {
	    sb.append(kdbhs + ")) ab ) cdg");
		} else {
	    sb.append(" select kdbh from code_gate_extend Where sbzt=0 )) ab ) cdg");
		}
		sb.append(" left join (Select CSTR_COL01 as kdbh, CSTR_COL02 as fxbh,case when sum(nvl(CSTR_COL07, 0)) = 0 then 0  else");
		sb.append(" Round(sum(nvl(CSTR_COL05, 0))/sum(nvl(CSTR_COL07, 0)),4) * 100 end btsl,case when  sum(nvl(CSTR_COL08, 0)) = 0 then");
		sb.append(" 0 else Round(sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL08, 0)),4) * 100 end ywsl from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL  Where KHFA_ID = '35'");
		if (kssj != null && kssj.length() > 0) {
	    sb.append("  and DATE_COL01 >= to_date('" + kssj + "', 'yyyy-MM-dd HH24:mi:ss')");  
		}
		if (jssj != null && jssj.length() > 0) {
	     sb.append(" and DATE_COL01 <=  to_date('" + jssj + "', 'yyyy-MM-dd HH24:mi:ss')");
		}
		sb.append("  group by CSTR_COL02, CSTR_COL01) bc  on cdg.kdbh = bc.kdbh  and cdg.fxbh = bc.fxbh");
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sb.toString());
		return list;
		}

	public List<Map<String, Object>> getIndetcdProjectInfo(String kssj,
			String jssj, String kdbh, String fxbh) {
		List<List<String>> liststr = null;             
		StringBuffer sb = new StringBuffer("Select cdg.kdbh,cdg.fxmc,nvl(bc.cdbh, '01') cdbh,nvl(btsl, 0) btsl,nvl(ywsl, 0) ywsl");
		sb.append(" from (Select ab.kdbh, ab.fxmc, ab.fxbh from (Select kdbh, fxbh, fxmc from code_gate_extend Where kdbh in (");
		if (kdbh != null && kdbh.length() > 0) {
			sb.append(kdbh + ") ");
		}
		if (fxbh != null && fxbh.length() > 0) {
			sb.append(" and fxbh='" + fxbh + "') ab) cdg ");
		}
		sb.append(" left join (Select CSTR_COL01 as kdbh,CSTR_COL02 as fxbh,CSTR_COL09 as cdbh,case when sum(nvl(CSTR_COL07, 0)) = 0 then 0 ");
		sb.append(" else  Round(sum(nvl(CSTR_COL05, 0)) /  sum(nvl(CSTR_COL07, 0)), 4) * 100  end btsl, case");
		sb.append(" when sum(nvl(CSTR_COL08, 0)) = 0 then  0 else Round(sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL08, 0)),4) * 100");
		sb.append("  end ywsl from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID = '35'");
		if (kssj != null && kssj.length() > 0) {
			sb.append(" and DATE_COL01 >= to_date('" + kssj + "', 'yyyy-MM-dd HH24:mi:ss')");
		}
		if (jssj != null && jssj.length() > 0) {
			sb.append(" and DATE_COL01 <= to_date('" + jssj + "', 'yyyy-MM-dd HH24:mi:ss')");
		}
		    sb.append("  group by CSTR_COL02, CSTR_COL01, CSTR_COL09) bc  on cdg.kdbh = bc.kdbh and cdg.fxbh = bc.fxbh");     
		    List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sb.toString());
		    return list;
	}
}
