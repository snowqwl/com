package com.sunshine.monitor.system.monitor.dao.jdbc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ibm.icu.util.Calendar;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.monitor.dao.KkjrjcProjectDao;

@Repository("kkjrjcProjectDao")
public class KkjrjcProjectDaoImpl extends BaseDaoImpl implements KkjrjcProjectDao {

	@SuppressWarnings("unchecked")
	public int getKkjrjcQueryCount() throws Exception {
		int count = 0;
		Map kkjrjcMap = null;
		/*StringBuffer sb = new StringBuffer(" select sum(nvl(t.sjjrs, 0)) as sjjrs from (select distinct ghjrs, sjjrs, tt31.ghnd, tt30.city as dsdm ");
						sb.append(" from (select sum(pk1) over(partition by cstr_col01 order by cstr_col02) as ghjrs,cstr_col01 as city,cstr_col02 as ghnd ");
						sb.append(" from monitor.t_monitor_jg_integrate_detail where khfa_id = '31' and workitem_id in (select max(to_number(workitem_id)) ");
						sb.append(" from monitor.t_monitor_jg_integrate_detail where khfa_id = '31' group by cstr_col01)) tt31 ");
						sb.append(" right join (select max(sjjrs) as sjjrs, ghnd, city from (select count(distinct pk1) as sjjrs, ");
						sb.append(" to_char(date_col01, 'yyyy') as ghnd,CSTR_COL04 as city from monitor.t_monitor_jg_integrate_detail ");
						sb.append(" where khfa_id = '30' and workitem_id in (select max(to_number(workitem_id)) as workitem_id from monitor.t_monitor_jg_integrate_detail ");
						sb.append(" where khfa_id = '30' and date_col01 between to_date(to_char(add_months(sysdate, -1),'yyyy/mm/')||'25 00:00:00','yyyy/mm/dd hh24:mi:ss') and to_date(to_char(trunc(sysdate),'yyyy/mm/')||'26 23:59:59','yyyy/mm/dd hh24:mi:ss') group by cstr_col04) ");
						sb.append(" group by to_char(date_col01, 'yyyy'), cstr_col04 union select count(distinct pk1) as sjjrs, ");
						sb.append(" to_char(date_col01, 'yyyy') as ghnd,CSTR_COL04 as city from monitor.t_monitor_jg_integrate_detail ");
						sb.append(" where khfa_id = '40' and workitem_id in (select max(to_number(workitem_id)) as workitem_id ");
						sb.append(" from monitor.t_monitor_jg_integrate_detail where khfa_id = '40' and date_col01 between  to_date(to_char(add_months(sysdate, -1),'yyyy/mm/')||'25 00:00:00','yyyy/mm/dd hh24:mi:ss') and to_date(to_char(trunc(sysdate),'yyyy/mm/')||'26 23:59:59','yyyy/mm/dd hh24:mi:ss') group by cstr_col04) ");
						sb.append(" group by to_char(date_col01, 'yyyy'), cstr_col04) group by ghnd, city) tt30 on tt31.ghnd = tt30.ghnd and tt31.city = tt30.city) t ");
						sb.append(" right join monitor.code_url c on c.dwdm = t.dsdm order by dsdm" );	*/
		StringBuffer sb = new StringBuffer(" select count(1) as sjjrs from code_gate ");
		List list = this.getRecordData(sb.toString(),null);
		if (list != null && list.size() > 0) {
			 kkjrjcMap = (Map) list.get(0);
		}
		if (kkjrjcMap != null) {
			count = Integer.parseInt(kkjrjcMap.get("sjjrs").toString());
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public int getKkzxQueryCount() throws Exception {
	   int count = 0;
       Map kkzxMap = null;
       
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String this_Month=format.format(new Date());
		
		Calendar c= Calendar.getInstance();
		c.add(Calendar.MONTH,-1);
		String last_Month = new SimpleDateFormat("yyyy-MM").format(c.getTime());
		
        String kssj = last_Month+"-26 00:00:00";
        String jssj = this_Month+"-25 23:59:59";

        List param = new ArrayList<>();
       StringBuffer sb = new StringBuffer("select count(distinct cstr_col10) as bzxs  from Monitor.t_monitor_jg_integrate_detail where khfa_id = '26' and date_col02 ");
       sb.append(" between to_date(?,'yyyy-mm-dd hh24:mi:ss') and to_Date(?,'yyyy-mm-dd " +
			   "hh24:mi:ss')");      /**  add_months(sysdate, -1)  */
       param.add(kssj);
       param.add(jssj);
       List list = this.getRecordData(sb.toString(),param.toArray(),null);
       if (list != null && list.size() > 0) {
    	   kkzxMap = (Map) list.get(0);
       }
       if (kkzxMap != null) {
    	   count = Integer.parseInt(kkzxMap.get("bzxs").toString());
       }
		return count;
	}
	
	public int getKkzxCount(String kssj,String jssj) throws Exception {
		List param = new ArrayList<>();
		StringBuffer sb= new StringBuffer("select count(1) from monitor.t_monitor_jg_integrate_detail t where t.khfa_id = '27' and workitem_id =(");
		sb.append("select max(to_number(workitem_id)) from monitor.t_monitor_jg_integrate_detail where khfa_id = '27' and date_col01 ");
		sb.append(" between to_date(?,'yyyy-mm-dd hh24:mi:ss') and to_Date(?,'yyyy-mm-dd" +
				" hh24:mi:ss')) ");
		sb.append("and t.cstr_col02 not in(select cstr_col10 from monitor.t_monitor_jg_integrate_detail  where khfa_id = '26' and workitem_id =");
		sb.append("(select max(to_number(workitem_id)) from monitor.t_monitor_jg_integrate_detail where khfa_id = '26' and date_col02 ");
		sb.append(" between to_date(?,'yyyy-mm-dd hh24:mi:ss') and to_Date(?,'yyyy-mm-dd" +
				" hh24:mi:ss') ");
		sb.append(")) group by workitem_id");
		param.add(kssj);
		param.add(jssj);
		param.add(kssj);
		param.add(jssj);
		System.out.println(sb.toString());
		List list =  this.jdbcTemplate.queryForList(sb.toString(), param.toArray(),Integer.class);
		if(list.size()>0){
			return Integer.parseInt(list.get(0).toString());
		}
		return 0;
	}
	
	


}
