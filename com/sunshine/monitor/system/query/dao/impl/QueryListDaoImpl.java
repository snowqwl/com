
package com.sunshine.monitor.system.query.dao.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.ModelDetail;
import com.sunshine.monitor.system.query.bean.Surveil;
import com.sunshine.monitor.system.query.bean.SuspMonitor;
import com.sunshine.monitor.system.query.bean.VehAlarmrecIntegrated;
import com.sunshine.monitor.system.query.bean.VehPassrecIntegrated;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("queryListDao")
public class QueryListDaoImpl extends BaseDaoImpl implements QueryListDao {

	
	public Map getMapForFilter(Map map, String tableName, StringBuffer condition)
			throws Exception {
		StringBuffer sb = new StringBuffer("select *  from  ");
		sb.append(tableName);
		sb.append("  where  ").append(condition);
		return this.getSelf().findPageForMap(sb.toString(),
				Integer.parseInt(map.get("curPage").toString()),
				Integer.parseInt(map.get("pageSize").toString()));
	}

	public Map getMapForIntegrate(Map map, VehPassrec info, String citys)
			throws Exception {
		String table="";
		int cxlx=Integer.parseInt(map.get("cxlx").toString());
		if(cxlx==1){
		table = "V_HKMACAO_VEH_PASSREC";
		}else if(cxlx==2){
		table = "V_SPECIAL_VEH_PASSREC";	
		}else{
		table = "V_VEH_PASSREC"; 	
		}
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(
				"select v.* ,? as cxlx,nvl(d.fxmc,v.fxbh) as fxmc,nvl(g.kdmc,v.kdbh) as kdmc," +
						"to_char(gcsj,'yyyy-mm-dd hh24:mi:ss') as sj from ? v left join " +
						"v_code_Direct d on v.fxbh=d.fxbh left join v_code_Gate g on v.kdbh=g.kdbh where 1=1 ");
		param.add(cxlx);
		param.add(table);
		if (StringUtils.isNotBlank(info.getHphm())) {
			sql.append(" and hphm = ? ");
			param.add(URLDecoder.decode( info.getHphm(),"UTF-8"));
		}
		
		if (StringUtils.isNotBlank(info.getHpzl())) {
			sql.append(" and hpzl = ? ");
			param.add(info.getHpzl());
		}
		
		String temp = null;
		String dateTemp = "yyyy-mm-dd";
		String minTemp = " hh24:mi:ss";
		if (StringUtils.isNotBlank(info.getKssj())) {
			if (info.getKssj().length() > 13)
				temp = dateTemp + minTemp;
			else
				temp = dateTemp;
			sql.append(" and gcsj >= to_date(?,?) ");
			param.add(info.getKssj());
			param.add(temp);
		}

		if (StringUtils.isNotBlank(info.getJssj())) {
			if (info.getJssj().length() > 13)
				temp = dateTemp + minTemp;
			else
				temp = dateTemp;

			sql.append(" and gcsj <= to_date(?,?) ");
			param.add(info.getJssj());
			param.add(temp);

		}
				
		StringBuffer sb = new StringBuffer("select *  from (");
		sb.append(sql).append(" ) ");
		
		if(StringUtils.isNotBlank(citys)){
			sb.append("  where   ?  ") ;
			param.add(getSqlconForCity("gcxh",citys));
		}
		//System.out.println(sb.toString());
		return this.getSelf().queryPageLimitTotal(sb.toString(),param.toArray(),
				Integer.parseInt(map.get("curPage").toString()),
				Integer.parseInt(map.get("pageSize").toString()), 1000,
				this.jdbcTemplate,"gcsj");
	}

	public Map getMapForIntegrateTraffic(Map filter, VehPassrec info,
			String citys,String wflxTab) throws Exception {
		StringBuffer sql = new StringBuffer(
				StringUtils.isNotBlank(info.getHphm())?"select  '":"select /*+INDEX(t T_AP_VIO_VIOLATION_HPHM)*/ '")
		.append(wflxTab)
		.append("'  as TABNAME,     t.* from   ")
		.append(wflxTab).append("  t   where 1=1  ");
		
		if (StringUtils.isNotBlank(info.getHphm())) {
			sql.append(" and hphm = '").append(URLDecoder.decode( info.getHphm(),"UTF-8")).append("' ");
		}
		
		if(StringUtils.isNotBlank(citys) && !"T_AP_VIO_FORCE".equals(wflxTab)){
			sql.append("  and  (").append(getSqlconForCity("wfbh",citys)).append(")  ") ;
		}
		
		if (StringUtils.isNotBlank(info.getHpzl())) {
			sql.append("  and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		String temp = null;
		String dateTemp = "yyyy-mm-dd";
		String minTemp = " hh24:mi:ss";
		if (StringUtils.isNotBlank(info.getKssj())) {
			if (info.getKssj().length() > 13)
				temp = dateTemp + minTemp;
			else
				temp = dateTemp;
			
			sql.append(" and wfsj >= to_date('").append(info.getKssj()).append("','").append(temp).append("')  ");
		}

		if (StringUtils.isNotBlank(info.getJssj())) {
			if (info.getJssj().length() > 13)
				temp = dateTemp + minTemp;
			else
				temp = dateTemp;
			
			sql.append(" and wfsj <= to_date('").append(info.getJssj()).append("','").append(temp).append("')  ");
		}
		
		return this.getSelf().queryPageLimitTotal(sql.toString(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()), 1000,
				this.jdbcTemplate,"  wfsj  desc  ");
	}
	
	/**
	 * 集中库过车查询明细
	 */
	public VehPassrecIntegrated getIntegratedPassDetail(String hphm,String kdbh,String gcsj,String cxlx)throws Exception{
		String table="";
		int lx=Integer.parseInt(cxlx);
		if(lx==1){
		table = "V_HKMACAO_VEH_PASSREC";
		}else if(lx==2){
		table = "V_SPECIAL_VEH_PASSREC";	
		}else{
		table = "V_VEH_PASSREC"; 	
		}
		String sql = "select v.*,nvl(g.kdmc,v.kdbh) as kdmc,nvl(d.sbmc,v.sbbh) as sbmc,nvl(f.fxmc,v.fxbh) as fxmc from " 
				+table+
		        " v left join v_code_Gate g on v.kdbh=g.kdbh " +
				" left join v_code_device d on v.sbbh=d.sbbh " +
				" left join v_code_direct f on v.fxbh=f.fxbh " +
				" where hphm = '" + 
		URLDecoder.decode( hphm,"UTF-8") 
		+ "'  and v.kdbh = '"+kdbh+"' and gcsj = to_date('"+gcsj+"','yyyy-mm-dd hh24:mi:ss') ";
		List<VehPassrecIntegrated> list = this.queryForList(
				this.jdbcTemplate, sql, VehPassrecIntegrated.class);
		if (list.size() > 0)
			return (VehPassrecIntegrated) list.get(0);
		return null;
	}


	public List<SuspMonitor> getSuspMonitorListForBkxh(String bkxh)
			throws Exception {
		String sql = "select distinct(DWDM),BKXH,BKJG,LRSJ  from JM_SUSPINFO_MONITOR  where bkxh " +
				"= ?";
		return this.queryForList(sql,new Object[]{bkxh}, SuspMonitor.class);
	}
	
	private String getSqlconForCity(String parmp,String citys){
		StringBuffer sb = new StringBuffer("");
		if(StringUtils.isNotBlank(citys)){
			String[]cityList = citys.split(",");
			for(String city : cityList){
				if( sb.length() > 2 ){
					sb.append(" or ").append(parmp).append(" like '").append(city).append("%'  ");
				}else{
					sb.append("   ").append(parmp).append(" like '").append(city).append("%'  ");
				}
			}
		}
		return sb.toString();
	}

	public Surveil getSuvreitForXh(String xh) {
		String sql = "select *  from  T_AP_VIO_SURVEIL   where WFBH = ?";
		
		List<Surveil> list = this.queryForList(sql,new Object[]{xh}, Surveil.class);
		
		if(list != null && list.size() > 0)
			return list.get(0);
		else 
			return null;
	}

	public Map getMapForSuspinfoFilter(Map map, StringBuffer condition)
			throws Exception {
		List param = new ArrayList<>();
		String idxSql = map.get("idx_sql").toString();
		StringBuffer sb = new StringBuffer("select ? BKXH,HPHM,HPZL,BKLB,BKJGMC,BKSJ,YWZT,BJZT," +
				"XXLY,BKRMC  from ");
		sb.append(" veh_suspinfo ");
		sb.append("  where  ?");
		param.add(idxSql);
		param.add(condition);
		return this.getSelf().findPageForMap(sb.toString(),param.toArray(),
				Integer.parseInt(map.get("curPage").toString()),
				Integer.parseInt(map.get("pageSize").toString()));
	}
	
public Map getMapForCitySuspinfoFilter(Map map,StringBuffer condition,String cityname)throws Exception {
		List param = new ArrayList<>();
	StringBuffer sb = new StringBuffer("select BKXH,HPHM,HPZL,BKLB,BKJGMC,BKSJ,YWZT,BJZT,XXLY  " +
			"from ? where ?");
	param.add(" veh_suspinfo@"+cityname);
	param.add(condition);
	System.out.println(sb.toString());
	return this.getSelf().findPageForMap(sb.toString(),param.toArray(),
			Integer.parseInt(map.get("curPage").toString()),
			Integer.parseInt(map.get("pageSize").toString()));
	}
	

	public Map getSuspinfoMapForFilerByHphm(Map map, String hphm, String bkjg)
			throws Exception {
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer("select * from (");
		sb.append(" select BKXH,HPHM,HPZL,BKLB,BKJGMC,BKSJ,YWZT,BJZT,XXLY,BKDL  from veh_suspinfo ");
		sb.append(" where bkdl <> '3' or bkjg = ?)a ");
		sb.append(" where a.HPHM like upper(?)  order by a.BKSJ desc ");
		param.add(bkjg);
		param.add(hphm+"%");

		Map result = this.getSelf().findPageForMapNoLimit(sb.toString(),param.toArray(),
				Integer.parseInt(map.get("curPage").toString()),
				Integer.parseInt(map.get("pageSize").toString()));
		return result;
	}

	
	public String getPicPath(String xh)throws Exception {
		String sql = "select zp  from v_veh_picture  where xh = ?";
			
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,xh);
		if(list.size() > 0){
			
			return (String)list.get(0).get("zp");
		}
		
		return null;
	}

	public VehPassrecIntegrated getAlarmrec(String gcxh) throws Exception {
		String sql = "select bjxh,bjdl,bjlx,bjsj,bjdwmc from v_veh_alarmrec where gcxh =?";
		List<VehPassrecIntegrated> list = this.queryForList(this.jdbcTemplate,new Object[]{gcxh},
				sql,
				VehPassrecIntegrated.class);
		if (list != null && list.size() > 0)
			return (VehPassrecIntegrated) list.get(0);
		else
			return null;
	}

	public Map getMapForIntegrateAlarm(Map filter, VehAlarmrecIntegrated info,
			String citys) throws Exception {
		List param = new ArrayList<>();
       StringBuffer sql=new StringBuffer("select * from v_veh_alarmrec where 1=1 ");
       if(StringUtils.isNotBlank(info.getBjdl())){
    	  sql.append(" and bjdl = ?");
    	  param.add(info.getBjdl());
       }
       if(StringUtils.isNotBlank(info.getBjlx())){
    	  sql.append(" and bjlx = ?");
		   param.add(info.getBjlx());
       }
       if(StringUtils.isNotBlank(info.getHphm())) {
		  sql.append(" and hphm = ? ");
		  param.add(URLDecoder.decode( info.getHphm(),"UTF-8"));
       }
		String temp = null;
		String dateTemp = "yyyy-mm-dd";
		String minTemp = " hh24:mi:ss";
		if (StringUtils.isNotBlank(info.getKssj())) {
			if (info.getKssj().length() > 13)
				temp = dateTemp + minTemp;
			else
				temp = dateTemp;
			
			sql.append(" and bjsj >= to_date(?,?)  ");
			param.add(info.getKssj());
			param.add(temp);
		}

		if (StringUtils.isNotBlank(info.getJssj())) {
			if (info.getJssj().length() > 13)
				temp = dateTemp + minTemp;
			else
				temp = dateTemp;
			
			sql.append(" and bjsj <= to_date(?,?)  ");
			param.add(info.getJssj());
			param.add(temp);
		}
		StringBuffer sb = new StringBuffer("select *  from (");
		sb.append(sql).append(" ) ");
		
		if(StringUtils.isNotBlank(citys)){
			sb.append("  where   ?  ") ;
			param.add(getSqlconForCity("gcxh",citys));
		}
		return this.getSelf().queryPageLimitTotal(sb.toString(),param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()), 1000,
				this.jdbcTemplate,"  bjsj  desc  ");
	}

	public VehAlarmrecIntegrated getIntegratedAlarmDetial(String bjxh)
			throws Exception {
		String sql ="select * from v_veh_alarmrec where bjxh=?";
		List<VehAlarmrecIntegrated> list = this.queryForList(
				this.jdbcTemplate,new Object[]{bjxh},sql, VehAlarmrecIntegrated.class);
		if (list.size() > 0)
			return (VehAlarmrecIntegrated) list.get(0);
		return null;
	}

	public List<Map<String, Object>> getstat4Rate2NumList(String glbm,
			String kssj, String jssj) throws Exception {
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select jb, bmmc, glbm, sum(nvl(yqs_total, 0)) yqs_total,  sum(nvl(csqs_total, 0)) csqs_total,  sum(nvl(wqs_total, 0)) wqs_total, ");
		sql.append(" sum(nvl(yxqs, 0)) yxqs,sum(nvl(yxqs_cs, 0)) yxqs_cs,  sum(nvl(yfk, 0)) yfk,     sum(nvl(jsfk, 0)) jsfk,  sum(nvl(csfk, 0)) csfk, ");
		sql.append("  sum(nvl(ylj_total, 0)) ylj_total, sum(nvl(cglj_total, 0)) cglj_total from  (select m.*, n.xjjg from (  select  *  from frm_department t  where  ");
		sql.append("  (t.sjbm= ?  or t.glbm= ? ) ) m  ");
		sql.append(" inner join frm_prefecture n   on m.glbm = n.dwdm   where substr(glbm,   length(glbm) - 5,6) = '000000'  ");
		sql.append("  or substr(glbm, 0, 8) = substr(n.xjjg, 0, 8)) bm  inner join  ");
		sql.append("  (select bjdwdm, count(bjxh) yqs_total, sum(case when bjdl in (1, 2) and  qrzt > 0 then    (case  when (nvl(qrsj, sysdate) - bjsj) * 24 * 60 * 60 > 600  ");
		sql.append(" then  1   else    0   end)   end) csqs_total,  sum(case  when bjdl in (1, 2) and qrzt = 0 then   1   else 0   end) wqs_total, ");
		sql.append(" sum(case   when qrzt = '1' then   1  else   0  end) yxqs, sum(case  when qrzt = '1' and   (nvl(qrsj,sysdate) - bjsj) * 24 * 60 * 60 > 600  ");
		sql.append(" then  1 else       0 end) yxqs_cs, sum(case    when qrzt = '1' then    1  else 0  end) ylj_total,  sum(yfk) yfk,   sum(jsfk) jsfk,  sum(csfk) csfk ");
		sql.append(" from  (select a.yfk, va.*, vh.jsfk,vh.csfk  from (select *   from veh_alarmrec  where bjdl in   (1, 2)     and bjdwdm in ");
		sql.append("  (select xjjg   from frm_prefecture    where dwdm = ?  )     and bjsj " +
				"between   to_date(?,'YYYY-MM-DD HH24:MI:SS') and  to_date(?, 'YYYY-MM-DD " +
				"HH24:MI:SS')) va    left join ");
		sql.append("   (  select  bjxh,count(s.zljsdw)  as yfk  from  veh_alarm_cmd cmd,veh_alarm_cmdscope s     where cmd.zlxh = s.zlxh   ")
		.append("    and bjxh in(select   bjxh  from veh_alarmrec     where bjdl in (1, 2)    and bjdwdm in    (select xjjg      from frm_prefecture ")
		.append("    where dwdm =  ? )  and bjsj between    to_date(?, " +
				"'YYYY-MM-DD HH24:MI:SS') and ")
			.append("  to_date(?, 'YYYY-MM-DD HH24:MI:SS'))   group by bjxh   ) a      on va.bjxh" +
					" = a.bjxh  left join (select bjxh,  sum(case  when (lrsj - bjsj) < 1 then    1  else   0 end) jsfk, ")
		.append("   sum(case   when (lrsj - bjsj) > 1 then    1 else       0    end) csfk   from (select bjxh,  zlxh, lrdwdm, ")
		.append("    min(lrsj) as lrsj,min(bjsj) as bjsj  from veh_alarm_handled    where bjxh in(select  bjxh   from veh_alarmrec ")
		.append("   where bjdl in (1, 2)  and bjdwdm in     (select xjjg   from frm_prefecture   " +
				"where dwdm = ?) ")
		.append(" and bjsj between     to_date(?, 'YYYY-MM-DD HH24:MI:SS') and  to_date(?, " +
				"'YYYY-MM-DD HH24:MI:SS'))    group by bjxh, zlxh,  lrdwdm)  group by bjxh) vh  on vh.bjxh = va.bjxh) tip group by tip.bjdwdm) e ")
		.append("  on bm.xjjg = e.bjdwdm left join (select va.bjdwdm as dwdm, sum(case  when vah.sflj = 1 then    1   else   0  end) cglj_total    from (select *  from veh_alarmrec ")
		.append(" where bjdl in (1, 2)   and bjdwdm in   (select xjjg    from frm_prefecture    " +
				"where dwdm = ?)  ")
		.append("  and bjsj between   to_date(?, 'YYYY-MM-DD HH24:MI:SS') and    to_date(?," +
				"'YYYY-MM-DD HH24:MI:SS')) va  ")
		.append(" inner join veh_alarm_handled vah  on va.bjxh = vah.bjxh  group by va.bjdwdm) g  on bm.xjjg = g.dwdm     group by bm.jb, bm.bmmc, bm.glbm ");
		param.add(glbm);
		param.add(glbm);
		param.add(glbm);
		param.add(kssj);
		param.add(jssj);
		param.add(glbm);
		param.add(kssj);
		param.add(jssj);
		param.add(glbm);
		param.add(kssj);
		param.add(jssj);
		param.add(glbm);
		param.add(kssj);
		param.add(jssj);

		return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
	}

	public List<Map<String, Object>> getViolationForWfbh(String wfbh) {
		
		String sql = " select * from T_AP_VIO_VIOLATION  where wfbh = ?";
		return this.jdbcTemplate.queryForList(sql,wfbh);
	}

	public List<Map<String, Object>> getForceForXh(String xh) {
		
		String sql = "select * from T_AP_VIO_FORCE   where xh = ?";
		return this.jdbcTemplate.queryForList(sql,xh);
	}

	/**
	 * 缺少号牌种类
	 */
	@Deprecated
	public Map<String, Object> getTrafficCount(List<String> hphms)
			throws Exception {
		Map<String,Object> result = new HashMap<String,Object>();
		StringBuffer temp = new StringBuffer();
		for(String s:hphms){
		    temp.append("'").append(s).append("'").append(",");
		}
		if(!temp.equals("")&&temp.length()>0){
		String hphm = temp.substring(0,(temp.length()-1));

		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(cs) as cs,hphm as hphm from (")
		     .append(" select hphm as hphm,count(1) as cs from T_AP_VIO_VIOLATION ")
		     .append(" where hphm in (?)")
		     .append(" group by hphm ")
		   .append(" union all ")
             .append(" select hphm as hphm,count(1) as cs from T_AP_VIO_SURVEIL ")
             .append(" where hphm in (?)")
             .append(" group by hphm ")
           .append(" union all ")
             .append(" select hphm as hphm,count(1) as cs from T_AP_VIO_FORCE ")
             .append(" where hphm in (?)")
             .append(" group by hphm ")
           .append(") group by hphm");
		param.add(hphm);
		param.add(hphm);
		param.add(hphm);
		
		List<Map<String,Object>> list =  this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		for(Map<String,Object> m:list){
			result.put(m.get("hphm").toString(),m.get("cs"));		
		}
		}
		return result;
	}


	public Map<String, Object> getTrafficDetail(String hphm,Map<String,Object> filter)
			throws Exception {
		List param = new ArrayList<>();
        StringBuffer condition = new StringBuffer();
        condition.append(" where hphm =? ");
        param.add(hphm);
        StringBuffer sql = new StringBuffer();
        sql.append(" select '' as wfbh,xh as xh, 'T_AP_VIO_FORCE' as tabname ,wfsj,hphm,hpzl,wfdz,cljgmc  from ")
           .append(" t_ap_vio_force ? union all ");
        sql.append(" select '' as wfbh,xh as xh, 'T_AP_VIO_SURVEIL' as tabname ,wfsj,hphm,hpzl,wfdz,cljgmc from ")
           .append(" t_ap_vio_surveil ? union all ");
        sql.append(" select wfbh as wfbh,'' as xh,'T_AP_VIO_VIOLATION' as tabname,wfsj,hphm,hpzl,wfdz,cljgmc from ")
           .append(" t_ap_vio_violation ?");
        param.add(condition);
        param.add(condition);
        param.add(condition);
         
		return this.findPageForMap(sql.toString(),param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	
	public Map<CarKey,Integer> getViolationCount(List<CarKey> cars){
		List param = new ArrayList<>();
		Map<CarKey,Integer> result = new HashMap<CarKey,Integer>();
		if (cars.size() > 1) {
			StringBuffer temp = new StringBuffer(" where ");
			for (CarKey s : cars) {
				temp.append(" (hphm = ? and ")
						.append(" hpzl = ?) or");
				param.add(s.getCarNo());
				param.add(s.getCarType());
			}
			String subSql = temp.substring(0, (temp.length() - 2));
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select hpzl,hphm,count(wfsj) cs  from jm_zyk_illegal ")//T_AP_VIO_VIOLATION 未能找到连接远程数据库的说明
		   .append(subSql)
		   .append(" group by hphm,hpzl");
			List<Map<String,Object>> list =  this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			if(list!=null && list.size()>0)
				for(Map<String,Object> m:list){
					CarKey car = new CarKey();
					car.setCarNo(m.get("hphm").toString());
					car.setCarType(m.get("hpzl").toString());
					result.put(car,Integer.parseInt(m.get("cs").toString()));		
				}
		}
		return result;
	}

	public Map<String, Object> getViolationDetail(CarKey car,Map<String, Object> filter) throws Exception {
		List param = new ArrayList<>();
        StringBuffer condition = new StringBuffer();
        condition.append(" where hphm =? and hpzl =?");
        param.add(car.getCarNo());
		param.add(car.getCarType());
        StringBuffer sql = new StringBuffer();
        /**
        sql.append(" select '' as wfbh,xh as xh, 'T_AP_VIO_FORCE' as tabname ,wfsj,hphm,hpzl,wfdz,cljgmc  from ")
           .append(" t_ap_vio_force ").append(condition);
        sql.append(" union all ");
        sql.append(" select '' as wfbh,xh as xh, 'T_AP_VIO_SURVEIL' as tabname ,wfsj,hphm,hpzl,wfdz,cljgmc from ")
           .append(" t_ap_vio_surveil").append(condition);
        sql.append(" union all ");
                   **/
        sql.append(" select wfbh as wfbh,'' as xh,'T_AP_VIO_VIOLATION' as tabname,wfsj,hphm,hpzl,wfdz,cljgmc from ")
           .append(" t_ap_vio_violation").append(condition);

        sql.append(" order by wfsj desc ");
         
		return this.findPageForMap(sql.toString(),param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	
	/**
	 * 查询省厅KK库中是否存在当前地市的dblink
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public int getAllDbLink(String cityname)throws Exception{
		int i = 0 ;
		if(cityname!=null){
			String sql = "select count(1) from all_db_links where owner='KK' and db_link=?";
			i =  this.jdbcTemplate.queryForInt(sql,cityname.toUpperCase());
		}
		return i ;
	}
	
	@Override
	public ModelDetail getById(String Id) throws Exception {
		String sb = "select id,title，content,cjsj,cjr,by1 from model_detail where id=?";
		return this.queryForObject(sb,new Object[]{Id}, ModelDetail.class);
	}
	
	public String getBksprByBkxh(String bkxh)throws Exception{
		try {
			StringBuffer sb = new StringBuffer("select czrmc from audit_approve where bzw='2' and" +
					" bkxh = ?  order by czsj desc");
			List<AuditApprove> list = this.queryForList(sb.toString(),
					new Object[]{bkxh},AuditApprove.class);
			if(list != null && list.size() > 0){
				return list.get(0).getCzrmc();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("布撤控查询布控审批人失败！");
		}
		return "";
	}
	
	public List<SuspMonitor> getSuspMonitorList(String bkxh, String bkjg)throws Exception{
		String jg = "";
		String msg = "";
		List<VehSuspinfo> ck_list = new ArrayList<VehSuspinfo>();
		if(bkjg.equals("ldbkTab")){
			bkjg = "已布控";
			jg = "布控";
			msg = jg + "信息已发送，待反馈";
		}else {
			bkjg = "已撤控";
			jg = "撤控";
			/**
			 * 该步骤主要是用于判断联动布控是否已经进行了联动撤控
			 */
			String ck_sql = "select t1.bkxh from veh_suspinfo t1, jm_suspinfo_monitor t2"
				+ " where t1.bkxh = t2.bkxh and t1.bkxh = ? and t1.jlzt = '1'"
				+"and t1.ywzt = '14' and t1.ckyydm is null";
			ck_list = this.queryForList(ck_sql, new Object[]{bkxh},VehSuspinfo.class);
			if(ck_list.size() == 0){
				msg = "待撤控";
			}
		}
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t1.xzqhdm as dwdm,t1.xzqhmc as bmmc,case when (t2.xzqhdm is not null and " +
				"t2.xzqhdm in (select DWDM from JM_SUSPINFO_MONITOR where bkxh=?" +
				" and bkjg=?)) then ? when( t2.xzqhdm is not null and t2.xzqhdm not in" +
				" " +
				"(select DWDM from JM_SUSPINFO_MONITOR where bkxh=? and bkjg=?)) then " +
				"? ");
		sql.append("else '未选择联动' end bkjg ,(select min(lrsj) from JM_SUSPINFO_MONITOR where" +
				" bkxh=? and bkjg=? and t1.xzqhdm = substr(dwdm,0,6) ) as lrsj ");
		sql.append("from (select xzqhxh,xzqhdm,xzqhmc from frm_xzqh where jb='3' " +
				"order by xzqhxh,xzqhdm asc) t1 ");
		sql.append("left join (select substr(x.col_1, x.pos1, x.pos2 - x.pos1 - 1) " +
				"xzqhdm,substr(substr(x.col_1, x.pos1, x.pos2 - x.pos1 - 1),0,6) " +
				"xzqhdm1,bkxh,bkfw from (select t.col_1, level as lv,  instr(',' || " +
				"t.col_1 || ',', ',', 1, level) as pos1, instr(',' || t.col_1 || ',', " +
				"',', 1, level + 1) as pos2 ,bkxh,bkfw from ( ");
		sql.append("select to_char(bkfw) col_1,bkxh,bkfw from veh_suspinfo where " +
				"bkxh=? group by bkxh,bkfw) t ");
		sql.append("connect by level <= (length(t.col_1) - length(replace(t.col_1, ',', ''))) + 1) x ) t2 " +
				"on t1.xzqhdm=t2.xzqhdm1 where substr(t1.xzqhdm, 0, 4)<>'4315' and length(t1.xzqhdm)=6 " +
				"and t1.xzqhdm<>'431600'");
		param.add(bkxh);
		param.add(bkjg);
		param.add(jg+"成功");
		param.add(bkxh);
		param.add(bkjg);
		param.add(msg);
		param.add(bkxh);
		param.add(bkjg);
		param.add(bkxh);
		List<SuspMonitor> list = this.queryList(sql.toString(), param.toArray(),SuspMonitor.class);
		//如果已联动布控但是未撤控，则联动布控展示不显示布控结果
		if(!bkjg.equals("已布控")){
			if(ck_list.size() > 0 && ck_list != null){
				for(SuspMonitor s : list)
					s.setBkjg("");
			}
		}
		List<SuspMonitor> gdList = ksbkList(bkxh, bkjg);
		if(gdList.size() > 0)
			list.addAll(gdList);
		return list;
	}
	/**
	 * 如果是跨省布控，需要展示跨省布控的联动情况
	 * @param bkxh
	 * @return
	 * @throws Exception 
	 */
	private List<SuspMonitor> ksbkList(String bkxh, String bkjg) {
		List<SuspMonitor> resList = new ArrayList<SuspMonitor>();
		try {
			List<SuspMonitor> gdBkList = new ArrayList<SuspMonitor>();
			String sql = "select bkfw from veh_suspinfo where bkxh = ? and bkfwlx = '3'";
			String bkfw = "";
			List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, bkxh);
			if(list.size() > 0) {
				List<Map<String, Object>> gdCitylist = new ArrayList<Map<String, Object>>();
				String gd_sql = "select dmz || '000000' as dwdm, substr(dmsm1, 0, 3) jdmc from frm_code where " +
						"dmlb = '000033' and substr(dmz, 0, 2) = '44' and dmsm2 = '1' order by dwdm asc";
				gdCitylist = this.jdbcTemplate.queryForList(gd_sql);
				bkfw = String.valueOf(list.get(0).get("BKFW"));
				String m_sql = "select * from jm_suspinfo_monitor where bkxh=? and bkjg=? "
						+ "and substr(dwdm,0,2) = '44' ";
				List<SuspMonitor> mList = this.queryForList(m_sql, new Object[]{bkxh, bkjg}, SuspMonitor.class);
				String res = "";
				if(bkjg.equals("已布控")) {
					res = "布控成功";
				}else {
					res = "撤控成功";
				}
				String[] bkfws = bkfw.split(",");
				//布控范围先和布控反馈情况关联，确定是否已经布控成功
				for(String str : bkfws) {
					if(str.startsWith("43"))
						continue;
					SuspMonitor susp = new SuspMonitor();
					susp.setDwdm(str);
					susp.setBkxh(bkxh);
					for(SuspMonitor s : mList) {
						if(str.equals(s.getDwdm())){
							susp.setBkjg(res);
							susp.setLrsj(s.getLrsj());
							break;
						}
					}
					if(StringUtils.isBlank(susp.getBkjg())){
						if(bkjg.equals("已布控")) 
							susp.setBkjg("布控信息已发送，待反馈");
						else 
							susp.setBkjg("待撤销");
						susp.setLrsj("");
					}
					gdBkList.add(susp);
				}
				//再通过整个广东省地市和已布控信息关联获取对应地市的布控情况，包括布控情况和撤控情况
				for(Map<String,Object> map : gdCitylist) {
					if(map.get("DWDM").equals("440000000000"))
						continue;
					SuspMonitor susp = new SuspMonitor();
					for(SuspMonitor s : gdBkList) {
						if(map.get("DWDM").equals(s.getDwdm())){
							susp = s;
							break;
						}
					}
					susp.setBmmc(String.valueOf(map.get("JDMC")));
					if(StringUtils.isBlank(susp.getBkjg())){
						susp.setBkjg("未选择联动");
						susp.setLrsj("");
					}
					resList.add(susp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resList;
	}
}