package com.sunshine.monitor.system.monitor.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.monitor.dao.SjcsjcProjectDao;

@Repository("sjcsjcProjectDao")
public class SjcsjcProjectDaoImpl extends BaseDaoImpl implements
		SjcsjcProjectDao {

	@SuppressWarnings("unchecked")
	public int getSjcsQueryCount() {
		int count = 0;
		Map sjcsMap = null;
		StringBuffer sb = new StringBuffer(" Select /*+FIRST_ROWS(1)*/ count(1) as ycss from (Select CSTR_COL01 as kdbh,sum(nvl(to_number(CSTR_COL03), 0)) as ysc,");
						sb.append(" sum(nvl(to_number(CSTR_COL06), 0)) ssmynzcsc from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID in ('28','41') and DATE_COL01 >= to_date(to_char(add_months(sysdate, -1),'yyyy/mm/')||'25 00:00:00','yyyy/mm/dd hh24:mi:ss') ");
						sb.append(" and DATE_COL01 <= to_date(to_char(trunc(sysdate),'yyyy/mm/')||'26 23:59:59','yyyy/mm/dd hh24:mi:ss')  and WorkItem_id||CSTR_COL02 in (Select max(to_number(WorkItem_id))||CSTR_COL02  as mid ");
						sb.append(" from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID = '28' and DATE_COL01 >= to_date(to_char(add_months(sysdate, -1),'yyyy/mm/')||'25 00:00:00','yyyy/mm/dd hh24:mi:ss') and DATE_COL01 <= to_date(to_char(trunc(sysdate),'yyyy/mm/')||'26 23:59:59','yyyy/mm/dd hh24:mi:ss')  ");
						sb.append(" group by CSTR_COL02 union all Select max(to_number(WorkItem_id))|| CSTR_COL02 as mid from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL ");
						sb.append(" Where KHFA_ID = '41' and DATE_COL01 >= to_date(to_char(add_months(sysdate, -1),'yyyy/mm/')||'25 00:00:00','yyyy/mm/dd hh24:mi:ss') and DATE_COL01 <= to_date(to_char(trunc(sysdate),'yyyy/mm/')||'26 23:59:59','yyyy/mm/dd hh24:mi:ss') group by CSTR_COL02)");
						sb.append(" group by CSTR_COL01) td Where ysc <> ssmynzcsc or (YSC = 0 and ssmynzcsc = 0)");
		List list = this.getRecordData(sb.toString(), null);
		/** add_months(sysdate,-1) */
		if (list != null && list.size() > 0) {
			sjcsMap = (Map) list.get(0);
		}
		if (sjcsMap != null) {
			count = Integer.parseInt(sjcsMap.get("ycss").toString());
		}
		return count;
	}

	/**
	 * 统计卡口上传情况（关联车道）,在Monitor用户抽取数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSjcsQuery(String kssj, String jssj,
			String jkdss) {
		List<Map<String, Object>> sjcsList = null;
		/*
		 * StringBuffer sb = new StringBuffer(
		 * "Select td.fxmc,nvl(sum(ysc), 0) ysc,nvl(sum(ssmynzcsc), 0) as ssmynzcsc,nvl(sum(ssmcssc),0) as ssmcssc, nvl(sum(ssmynzcscl), 0) ssmynzcscl,nvl(sum(ycscl), 0) ycscl from (Select cg.kdbh,cg.fxbh,cg.fxmc,cg.kdmc,dwdm,nvl(ysc, 0) as ysc,nvl(ssmynzcsc, 0) as ssmynzcsc,(nvl(ysc, 0) - nvl(ssmynzcsc, 0)) as ssmcssc,nvl(yfynzcsc, 0) as yfynzcsc,nvl(yscycs, 0) as yscycs,nvl(sfynzcsc, 0) as sfynzcsc,nvl(ssmynzcscl, 0) as ssmynzcscl,nvl(ycscl,0) as ycscl from (Select kdbh, fxbh, dwdm, fxmc, kdmc from Monitor.code_gate_extend"
		 * ); if (jkdss == "1") sb.append(" "); else { String[] kdbh =
		 * jkdss.split(","); sb.append(" Where kdbh in ( "); for(int i = 0;
		 * i<kdbh.length; i++) { if( i != (kdbh.length - 1))
		 * sb.append("'").append(kdbh[i]).append("',"); else
		 * sb.append("'").append(kdbh[i]).append("')"); } } sb.append(
		 * ")cg left join (Select CSTR_COL01 as kdbh,CSTR_COL08 as fxbh,sum(nvl(to_number(CSTR_COL03), 0)) as ysc,sum(nvl(to_number(CSTR_COL06), 0)) ssmynzcsc,sum(nvl(to_number(CSTR_COL04), 0)) yfynzcsc,sum(nvl(to_number(CSTR_COL07), 0)) yscycs,sum(nvl(to_number(CSTR_COL05), 0)) sfynzcsc,case when sum(nvl(CSTR_COL05, 0)) = 0 then 0  else  Round(sum(nvl(CSTR_COL06, 0))/sum(nvl(CSTR_COL03, 0)),4) * 100 end ssmynzcscl,case when sum(nvl(CSTR_COL03, 0)) = 0 then 0 else Round(1-sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL03, 0)),4) * 100 end ycscl from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID = '28' and DATE_COL01 >= to_date('"
		 * ); sb.append(kssj).append(
		 * "','yyyy-mm-dd hh24:mi:ss') and DATE_COL01 <= to_date('"); sb
		 * .append(jssj) .append(
		 * "', 'yyyy-mm-dd hh24:mi:ss') group by CSTR_COL01, CSTR_COL08) a on a.kdbh = cg.kdbh and a.fxbh = cg.fxbh) td left join frm_department fd on td.dwdm = fd.glbm group by td.kdmc,fd.jb,td.fxbh,fd.glbm,td.fxmc,fd.bmmc,td.kdbh"
		 * );
		 */
		StringBuffer sb = new StringBuffer(
				"Select cg.fxmc,nvl(ysc, 0) ysc,nvl(ssmynzcsc, 0)  ssmynzcsc,nvl(nvl(ysc, 0)-nvl(ssmynzcsc, 0),0) as ycsc,nvl(ssmynzcscl, 0) ssmynzcscl,nvl(ycscl, 0) ycscl from (Select ab.kdbh,ab.fxbh,ab.fxmc from (Select kdbh, fxbh,fxmc from code_gate_extend ");
		if (jkdss == "1")
			sb.append(" ");
		else {
			String[] kdbh = jkdss.split(",");
			sb.append(" Where kdbh in ( ");
			for (int i = 0; i < kdbh.length; i++) {
				if (i != (kdbh.length - 1))
					sb.append("'").append(kdbh[i]).append("',");
				else
					sb.append("'").append(kdbh[i]).append("')");
			}
		}
		sb
				.append(") ab) cg left join (Select CSTR_COL01 as kdbh,CSTR_COL08 as fxbh,sum(nvl(to_number(CSTR_COL03), 0)) as ysc,sum(nvl(to_number(CSTR_COL06), 0)) ssmynzcsc,sum(nvl(to_number(CSTR_COL04), 0)) yfynzcsc,sum(nvl(to_number(CSTR_COL07), 0)) yscycs,sum(nvl(to_number(CSTR_COL05), 0)) sfynzcsc,case when sum(nvl(CSTR_COL03, 0)) = 0 then 0 else Round(sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL03, 0)),4) * 100 end ssmynzcscl,case when sum(nvl(CSTR_COL03, 0)) = 0 then 0 else Round(1 - sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL03, 0)),4) * 100 end ycscl from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID = '28' and DATE_COL01 >= to_date('");
		sb.append(kssj).append(
				"','yyyy-mm-dd hh24:mi:ss') and DATE_COL01 <= to_date('");
		sb
				.append(jssj)
				.append(
						"', 'yyyy-mm-dd hh24:mi:ss') group by CSTR_COL01, CSTR_COL08) bc on bc.kdbh = cg.kdbh and bc.fxbh = cg.fxbh");
		sjcsList = this.jdbcTemplate.queryForList(sb.toString());
		return sjcsList;
	}

	/**
	 * 统计卡口上传情况（关联车道）,在Monitor用户抽取数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSjcsQueryCd(String kssj, String jssj,
			String jkdss, String fxbh) {
		List<Map<String, Object>> sjcsList = null;
		StringBuffer sb = new StringBuffer(
				"select cg.fxmc,nvl(bc.cdbh, '01') cdbh,nvl(ysc, 0) ysc, nvl(ssmynzcsc, 0) ssmynzcsc,nvl(nvl(ysc, 0) - nvl(ssmynzcsc, 0), 0) as ycsc,nvl(ssmynzcscl, 0) ssmynzcscl,nvl(ycscl, 0) ycscl from (Select ab.kdbh, ab.fxbh, ab.fxmc from (Select kdbh, fxbh, fxmc from code_gate_extend");
		if (jkdss == "1")
			sb.append(" ");
		else {
			String[] kdbh = jkdss.split(",");
			sb.append(" Where kdbh in ( ");
			for (int i = 0; i < kdbh.length; i++) {
				if (i != (kdbh.length - 1))
					sb.append("'").append(kdbh[i]).append("',");
				else
					sb.append("'").append(kdbh[i]).append("')");
			}
		}
		if (fxbh != null) {
			sb.append(" and fxbh = '").append(fxbh).append("'");
		}
		sb
				.append(") ab) cg left join (Select CSTR_COL01 as kdbh,CSTR_COL08 as fxbh,CSTR_COL09 as cdbh, sum(nvl(to_number(CSTR_COL03), 0)) as ysc,sum(nvl(to_number(CSTR_COL06), 0)) ssmynzcsc,sum(nvl(to_number(CSTR_COL04), 0)) yfynzcsc,sum(nvl(to_number(CSTR_COL07), 0)) yscycs,sum(nvl(to_number(CSTR_COL05), 0)) sfynzcsc,case when sum(nvl(CSTR_COL03, 0)) = 0 then 0 else Round(sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL03, 0)),4) * 100 end ssmynzcscl,case when sum(nvl(CSTR_COL03, 0)) = 0 then 0 else Round(1 - sum(nvl(CSTR_COL06, 0)) / sum(nvl(CSTR_COL03, 0)),4) * 100 end ycscl from Monitor.T_MONITOR_JG_INTEGRATE_DETAIL Where KHFA_ID = '28' and DATE_COL01 >= to_date('");
		sb.append(kssj).append(
				"','yyyy-mm-dd hh24:mi:ss') and DATE_COL01 <= to_date('");
		sb
				.append(jssj)
				.append(
						"', 'yyyy-mm-dd hh24:mi:ss') group by CSTR_COL01, CSTR_COL08,CSTR_COL09) bc on bc.kdbh = cg.kdbh and bc.fxbh = cg.fxbh");
		sjcsList = this.jdbcTemplate.queryForList(sb.toString());
		return sjcsList;
	}

}
