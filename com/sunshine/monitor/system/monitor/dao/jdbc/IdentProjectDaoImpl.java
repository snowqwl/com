package com.sunshine.monitor.system.monitor.dao.jdbc;

import java.util.ArrayList;
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
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer("Select cdg.kdbh,cdg.fxmc,nvl(btsl,0) btsl,nvl(ywsl,0) ywsl");
		sb.append(" from (Select ab.kdbh, ab.fxmc, ab.fxbh from (Select kdbh, fxbh, fxmc  from code_gate_extend Where kdbh in (" );
		if (kdbhs != null && kdbhs.length() > 0) {
	    sb.append("?)) ab ) cdg");
	    param.add(kdbhs);
		} else {
	    sb.append(" select kdbh from code_gate_extend Where sbzt=0 )) ab ) cdg");
		}
		sb.append(" left join (Select CSTR_COL01 as kdbh, CSTR_COL02 as fxbh,case when sum(nvl(CSTR_COL07, 0)) = 0 then 0  else");
		sb.append(" Round(sum(nvl(CSTR_COL05, 0))/sum(nvl(CSTR_COL07, 0)),4) * 100 end btsl,case when  sum(nvl(CSTR_COL08, 0)) = 0 then");
		sb.append(" 0 else Round(sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL08, 0)),4) * 100 end ywsl from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL  Where KHFA_ID = '35'");
		if (kssj != null && kssj.length() > 0) {
	    sb.append("  and DATE_COL01 >= to_date(?, 'yyyy-MM-dd HH24:mi:ss')");
	    param.add(kssj);
		}
		if (jssj != null && jssj.length() > 0) {
	     sb.append(" and DATE_COL01 <=  to_date(?, 'yyyy-MM-dd HH24:mi:ss')");
	     param.add(jssj);
		}
		sb.append("  group by CSTR_COL02, CSTR_COL01) bc  on cdg.kdbh = bc.kdbh  and cdg.fxbh = bc.fxbh");
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sb.toString(),param.toArray());
		return list;
		}

	public List<Map<String, Object>> getIndetcdProjectInfo(String kssj,
			String jssj, String kdbh, String fxbh) {
		List<List<String>> liststr = null;
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer("Select cdg.kdbh,cdg.fxmc,nvl(bc.cdbh, '01') cdbh,nvl(btsl, 0) btsl,nvl(ywsl, 0) ywsl");
		sb.append(" from (Select ab.kdbh, ab.fxmc, ab.fxbh from (Select kdbh, fxbh, fxmc from code_gate_extend Where kdbh in (");
		if (kdbh != null && kdbh.length() > 0) {
			sb.append("?) ");
			param.add(kdbh);
		}
		if (fxbh != null && fxbh.length() > 0) {
			sb.append(" and fxbh=?) ab) cdg ");
			param.add(fxbh);
		}
		sb.append(" left join (Select CSTR_COL01 as kdbh,CSTR_COL02 as fxbh,CSTR_COL09 as cdbh,case when sum(nvl(CSTR_COL07, 0)) = 0 then 0 ");
		sb.append(" else  Round(sum(nvl(CSTR_COL05, 0)) /  sum(nvl(CSTR_COL07, 0)), 4) * 100  end btsl, case");
		sb.append(" when sum(nvl(CSTR_COL08, 0)) = 0 then  0 else Round(sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL08, 0)),4) * 100");
		sb.append("  end ywsl from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID = '35'");
		if (kssj != null && kssj.length() > 0) {
			sb.append(" and DATE_COL01 >= to_date(?, 'yyyy-MM-dd HH24:mi:ss')");
			param.add(kssj);
		}
		if (jssj != null && jssj.length() > 0) {
			sb.append(" and DATE_COL01 <= to_date(?, 'yyyy-MM-dd HH24:mi:ss')");
			param.add(jssj);
		}
		    sb.append("  group by CSTR_COL02, CSTR_COL01, CSTR_COL09) bc  on cdg.kdbh = bc.kdbh and cdg.fxbh = bc.fxbh");     
		    List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sb.toString(),param.toArray());
		    return list;
	}
}
