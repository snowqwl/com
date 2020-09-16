package com.sunshine.monitor.system.monitor.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.monitor.dao.StatMonitorDao;

@Repository("statMonitorDao")
public class StatMonitorDaoImpl extends BaseDaoImpl implements StatMonitorDao {

	
	public List<Map<String, Object>> getIdentList(String kssj, String jssj, String jkdss)
			throws Exception {
		
		StringBuffer sb = new StringBuffer(" select    kdmc, fxmc,  nvl(GCS, 0) GCS,nvl(SNCS, 0) SNCS,nvl(SWCS, 0) SWCS, nvl(YGCS, 0) YGC,nvl(JJCS, 0) JJC, ");
		sb.append("  nvl(WPCS, 0) WPC,nvl(LPCS, 0) LPC,nvl(HPCS, 0) HPC,nvl(QTCS, 0) QTYSC  from  ");
		sb.append(" (select kdbh, fxbh,sum(GCS) GCS,sum(SNCS) SNCS,sum(SWCS) SWCS,sum(YGCS) YGCS,sum(JJCS) JJCS, ");
		sb.append(" sum(WPCS) WPCS,sum(LPCS) LPCS,sum(HPCS) HPCS,sum(QTCS) QTCS ");
		sb.append("  from stat_flow_sunshine where kdbh in ( ").append(jkdss).append(") ");
		sb.append("   and tjrq >= TO_DATE('").append(kssj).append("', 'yyyy-mm-dd hh24:mi:ss')  and tjrq <= TO_DATE('")
		.append(jssj).append("', 'yyyy-mm-dd hh24:mi:ss')   group by kdbh, fxbh) a ");
		sb.append(" right join (select ex.fxbh as fxbh, ex.fxmc, ga.kdmc, ga.kdbh ");
		sb.append(" from      code_gate ga, code_gate_extend ex ");
		sb.append(" where ex.kdbh = ga.kdbh  and ga.kdbh in (").append(jkdss).append(") ");
		sb.append("  ) kd on a.fxbh = kd.fxbh and a.kdbh = kd.kdbh  order by kdmc, fxmc  ");
		
		
		return this.jdbcTemplate.queryForList(sb.toString());
	}

	public List<Map<String, Object>> getHourIdentList(String kssj, String jssj,
			String kdbh,String fxbh,String cdbh) throws Exception {
		StringBuffer sql = new StringBuffer("select to_char(tjrq, 'hh24') as HH ,sum(gcs) as GCS_T ");
		sql.append("  from stat_flow_sunshine st  where   tjrq between ");
		sql.append("  to_date('").append(kssj).append("', 'YYYY-MM-DD HH24:MI:SS') and   to_date('").append(jssj).append("', 'YYYY-MM-DD HH24:MI:SS') ");
		
		sql.append("   and st.kdbh = '").append(kdbh).append("'  ");
		if(StringUtils.isNotBlank(fxbh)){
			sql.append(" and fxbh = '").append(fxbh).append("' ");
		}
		
		if(StringUtils.isNotBlank(cdbh)){
			sql.append(" and cdbh = '").append(cdbh).append("' ");
		}
		
		sql.append("  group by tjrq  order by tjrq  ");
		
		
		return this.jdbcTemplate.queryForList(sql.toString());
	}
	
	public Map<String,Object> getFlow() {
		String sql = "select to_char(nvl(trunc(max(tjrq), 'mm'),trunc(sysdate,'mm')),'yyyy-mm') as tjrq, nvl(sum(nvl(gcs,0)),0) as gcs,"
			+" nvl(sum(nvl(xcs,0)),0) as xcs,"
			+" nvl(sum(nvl(dcs,0)),0) as dcs,"
			+" nvl(sum(nvl(wfcs,0)),0) as wfcs,"
			+" nvl(sum(nvl(zccs,0)),0) as zccs,"
			+" nvl(sum(nvl(xcwfs,0)),0) as xcwfs,"
			+" nvl(sum(nvl(dcwfs,0)),0) as dcwfs,"
			+" nvl(sum(nvl(sncs,0)),0) as sncs,"
			+" nvl(sum(nvl(swcs,0)),0) as swcs,"
			+" nvl(sum(nvl(qtysc,0)),0) as qtysc,"
			+" nvl(sum(nvl(ygcs,0)),0) as ygcs,"
			+" nvl(sum(nvl(jjcs,0)),0) as jjcs,"
			+" nvl(sum(nvl(wpcs,0)),0) as wpcs,"
			+" nvl(sum(nvl(lpcs,0)),0) as lpcs,"
			+" nvl(sum(nvl(hpcs,0)),0) as hpcs,"
			+" nvl(sum(nvl(qtcs,0)),0) as qtcs "
			+" from stat_flow_sunshine "
			+" where trunc(tjrq, 'mm') = (select trunc(nvl(trunc(max(tjrq), 'mm'),sysdate),'mm') from stat_flow_sunshine) ";
		return this.jdbcTemplate.queryForMap(sql);
	}
	
	public List<Map<String,Object>> getStFlow(List<Code> cityList) {
		StringBuffer sb = new StringBuffer("");
		for(int i = 0;i<cityList.size();i++) {
			Code c = cityList.get(i);
			if("430000".equals(c.getDmz())) {
				continue;
			}
			sb.append("select '").append(c.getDmsm2()).append("' as cityname, ");
			sb.append( "to_char(max(tjrq),'yyyy-mm') as tjrq,nvl(sum(nvl(gcs,0)),0) as gcs from stat_flow_sunshine ");
			sb.append(" where trunc(tjrq, 'mm') = (select trunc(max(tjrq), 'mm') from stat_flow_sunshine) ");
			sb.append(" and kdbh like '").append(c.getDmz().substring(0, 4)).append("%' ");
			if(i<cityList.size()-1) {
				sb.append(" union all ");
			}
		}
		return this.jdbcTemplate.queryForList(sb.toString());
	}

}
