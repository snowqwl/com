package com.sunshine.monitor.system.query.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.query.dao.KsywtjQueryDao;

@Repository("ksywtjQueryDao")
public class KsywtjQueryDaoImpl extends BaseDaoImpl implements KsywtjQueryDao{
	
	@Autowired
	@Qualifier("jdbcTemplate")
	public JdbcTemplate jdbcTemplate;


	@Override
	public List ksywtjList(String kssj, String jssj, String sssf)
			throws Exception {
		String sql = "select * from(select c.jdmc,nvl(f2.zs,0) gccxzs,nvl(f3.zs,0) bkzs,nvl(f4.zs,0) ckzs,nvl(f5.zs,0) yjzs ,c.dwdm dwdm from code_url c"
				+" left outer join"
						  +" (select f.xzqh,count(f.xzqh) zs  from(select  substr(l.glbm,0,4 ) || '00000000' as xzqh from frm_log l,frm_department d,frm_sysuser s"
						   +" where l.yhdh = s.yhdh and l.glbm = d.glbm and czlx = '5510' )f"
						   +" group by f.xzqh) f2"
						   +" on c.dwdm = f2.xzqh"
						+" left outer join "
						  +" (select f.xzqh,count(f.xzqh) zs  from(select  substr(d.glbm,0,4 ) || '00000000' as xzqh from  frm_department d, veh_suspinfo s"
						   +" where  d.glbm = s.bkjg and s.bkfwlx = '3'"
						   +" and s.ywzt = '14')f"
						   +" group by f.xzqh) f3"
						   +" on c.dwdm = f3.xzqh"
						+" left outer join "
						  +" (select f.xzqh,count(f.xzqh) zs  from(select  substr(d.glbm,0,4 ) || '00000000' as xzqh from  frm_department d ,veh_suspinfo s"
						   +" where d.glbm = s.bkjg and s.bkfwlx = '3'"
						   +" and s.ywzt = '99')f"
						   +" group by f.xzqh) f4"
						   +" on c.dwdm = f4.xzqh"
						+" left outer join"
						  +" (select f.xzqh,count(f.xzqh) zs  from(select substr(a.bjdwdm,0,4 ) || '00000000' as xzqh from veh_alarmrec a,veh_suspinfo s,frm_department d"
						   +" where a.bkxh = s.bkxh and s.bkjg = d.glbm and s.bkfwlx = '3')f"
						   +" group by f.xzqh) f5"
						   +" on c.dwdm = f5.xzqh "
						+" union all"
						   +" select '合计',s1.gccxzs,s2.bkzs,s3.ckzs,s4.yjzs,'' dwdm from ("
						   +" select count(*) as gccxzs from frm_log l,frm_department d,frm_sysuser s"
						   +" where l.yhdh = s.yhdh and l.glbm = d.glbm and czlx = '5510') s1,"
						   +" (select  count(*) as bkzs from  frm_department d, veh_suspinfo s"
						   +" where  d.glbm = s.bkjg and s.bkfwlx = '3'"
						   +" and s.ywzt = '14') s2,"
						   +" (select  count(*) as ckzs from  frm_department d ,veh_suspinfo s"
						   +" where d.glbm = s.bkjg and s.bkfwlx = '3'"
						   +" and s.ywzt = '99') s3,"
						   +" (select count(*) as yjzs from veh_alarmrec a,veh_suspinfo s,frm_department d"
						   +" where a.bkxh = s.bkxh and s.bkjg = d.glbm and s.bkfwlx = '3') s4)"
						+" order by  dwdm";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public Map<String,Object> getGccxList(Map<String, Object> conditions)
			throws Exception{
		
		String kssj = conditions.get("kssj").toString();
		String jssj = conditions.get("jssj").toString();
		String dwdm = conditions.get("dwdm").toString();
		StringBuffer sql = new StringBuffer("select to_char(x.czsj, 'yyyy-mm-dd hh24:mi') czsj, x.cznr, x.ip, x.bmmc, x.yhmc, y.czlx");
				sql.append(" from (select a.czsj, a.czlx, a.cznr, a.glbm, a.ip, b.bmmc, c.yhmc");
				sql.append(" from frm_log a, frm_department b, frm_sysuser c");
				sql.append(" where a.glbm = b.glbm");
				sql.append(" and a.yhdh = c.yhdh");
				sql.append(" and a.czlx ='5510'");
				sql.append(" order by a.czsj desc) x,");
				sql.append(" (select dmz, dmsm1 czlx from frm_code where dmlb = '000012') y");
				sql.append(" where x.czlx = y.dmz");
				sql.append(" and x.czsj >= to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss')");
				sql.append(" and x.czsj <= to_date('"+jssj+"','yyyy-mm-dd hh24:mi:ss')");
				sql.append(" and substr(x.glbm, 0, 4) || '00000000' = '"+dwdm+"'");
				sql.append(" order by x.czsj desc");
	
		Map<String, Object> map = this.findPageForMap(sql.toString(),
				Integer.parseInt(conditions.get("page").toString()),
				Integer.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public Map<String,Object> getBkList(Map<String, Object> conditions)
			throws Exception{
		
		String kssj = conditions.get("kssj").toString();
		String jssj = conditions.get("jssj").toString();
		String dwdm = conditions.get("dwdm").toString();
		StringBuffer sql = new StringBuffer("select s.*,");
				sql.append(" (select c.dmsm1 from frm_code c where c.dmlb = '030107' and c.dmz = s.hpzl) as HPZLMC,");
				sql.append(" (select c.dmsm1 from frm_code c where c.dmlb = '120019' and c.dmz = s.bkdl) as BKDLMC");
				sql.append(" from  frm_department d ,veh_suspinfo s ");
				sql.append(" where d.glbm = s.bkjg and s.bkfwlx = '3' ");
				sql.append(" and s.bksj >= to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss')");
				sql.append(" and s.bksj <= to_date('"+jssj+"','yyyy-mm-dd hh24:mi:ss')");
				sql.append(" and s.ywzt = '14' and substr(s.bkjg,0,4) || '00000000' = '"+dwdm+"' order by bksj desc ");		
		Map<String, Object> map = this.findPageForMap(sql.toString(),
				Integer.parseInt(conditions.get("page").toString()),
				Integer.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public Map<String,Object> getCkList(Map<String, Object> conditions)
			throws Exception{
		
		String kssj = conditions.get("kssj").toString();
		String jssj = conditions.get("jssj").toString();
		String dwdm = conditions.get("dwdm").toString();
		StringBuffer sql = new StringBuffer("select s.*,");
				sql.append(" (select c.dmsm1 from frm_code c where c.dmlb = '030107' and c.dmz = s.hpzl) as HPZLMC,");
				sql.append(" (select c.dmsm1 from frm_code c where c.dmlb = '120019' and c.dmz = s.bkdl) as BKDLMC,");
				sql.append(" (select c.dmsm1 from frm_code c where c.dmlb = '130009' and c.dmz = s.bjzt) as BJZTMC");
				sql.append(" from  frm_department d ,veh_suspinfo s ");
				sql.append(" where d.glbm = s.bkjg and s.bkfwlx = '3' ");
				//由于2016-11-22开始走一键撤控流程，流程里没有更新撤控相关的信息（撤控申请时间为空）
//				sql.append(" and s.cxsqsj >= to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss')");
//				sql.append(" and s.cxsqsj <= to_date('"+jssj+"','yyyy-mm-dd hh24:mi:ss')");
				sql.append(" and s.ywzt = '99' and substr(s.bkjg,0,4) || '00000000' = '"+dwdm+"' order by bksj desc ");		
		Map<String, Object> map = this.findPageForMap(sql.toString(),
				Integer.parseInt(conditions.get("page").toString()),
				Integer.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public Map<String,Object> getYjList(Map<String, Object> conditions)
			throws Exception{
		
		String kssj = conditions.get("kssj").toString();
		String jssj = conditions.get("jssj").toString();
		String dwdm = conditions.get("dwdm").toString();
		StringBuffer sql = new StringBuffer("select a.*,");
				sql.append(" (select c.dmsm1 from frm_code c where c.dmlb = '030107' and c.dmz = a.hpzl) as HPZLMC,");
				sql.append(" (select c.dmsm1 from frm_code c where c.dmlb = '120019' and c.dmz = a.bjdl) as BJDLMC,");
				sql.append(" (select c.dmsm1 from frm_code c where c.dmlb = '120005' and c.dmz = a.bjlx) as BJLXMC");
				sql.append(" from veh_alarmrec a,veh_suspinfo s,frm_department d "); 
				sql.append(" where a.bkxh = s.bkxh and s.bkjg = d.glbm and s.bkfwlx = '3'");
				sql.append(" and a.bjsj >=to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss')");
				sql.append(" and a.bjsj <=to_date('"+jssj+"','yyyy-mm-dd hh24:mi:ss')");
				sql.append(" and substr(a.bjdwdm,0,4) || '00000000' = '"+dwdm+"' order by a.bjsj desc");		
		Map<String, Object> map = this.findPageForMap(sql.toString(),
				Integer.parseInt(conditions.get("page").toString()),
				Integer.parseInt(conditions.get("rows").toString()));
		return map;
	}
}
