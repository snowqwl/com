package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.dao.CombineAnalysisDao;

@Repository("combineAnalysisDao")
public class CombineAnalysisDaoImpl extends ScsBaseDaoImpl implements
		CombineAnalysisDao {

	public Map<String, Object> combineAnalysisByScsDb(Map filter, String consql,String[] conditionArray) throws Exception {
		//获取当前时间戳，毫秒数
		Long current = new Date().getTime();
		
		//创建两个临时表，一个存放号牌号码，一个存放轨迹
		String ifexisthphm ="DROP TABLE IF EXISTS hphmtemp"+current;		
		this.jdbcScsTemplate.update(ifexisthphm);
		String ifexisttemp = "DROP TABLE IF EXISTS catchinfotemp"+current;
		this.jdbcScsTemplate.update(ifexisttemp);
		
		String create_hphm=" create table hphmtemp"+current+" select c.hphm,c.hpzl,c.hpys from (";
		
		
		for(int i =0;i<conditionArray.length;i++) {
			create_hphm += conditionArray[i];
			if(i<conditionArray.length-1) {
				create_hphm +=" union all ";
			}
		}
		create_hphm += " ) c group by c.hphm,c.hpzl,c.hpys having count(c.hphm) = "+conditionArray.length;
		this.jdbcScsTemplate.update(create_hphm);
		int count = this.queryForInt("select count(t.*) from t(hphmtemp"+current+")");
		if(count == 0) {
			return null;
		}
		
		String create_temp = " create table catchinfotemp"+current+" ";
		create_temp += "select c.* from (select b.* from "+Constant.SCS_PASS_TABLE+" b where 1=1 " + consql+") c,hphmtemp"+current+" t where c.hphm = t.hphm and c.hpzl = t.hpzl and c.hpys = t.hpys ";
		this.jdbcScsTemplate.update(create_temp);
		
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS xzqhtemp"+current);
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS kktemp"+current);
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS ttemp"+current);
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS counttemp"+current);
		
		String xzqh_temp = " create table xzqhtemp"+current+" select t.hphm,t.hpzl,t.hpys,substring(t.kdbh,1,6) as xzqh from catchinfotemp"+current+" t group by t.hphm,t.hpzl,t.hpys,substring(t.kdbh,1,6) ";
		String kk_temp = " create table kktemp"+current+"  select t.hphm,t.hpzl,t.hpys,t.kdbh as kk from catchinfotemp"+current+" t group by t.hphm,t.hpzl,t.hpys,t.kdbh ";
		String t_temp = " create table ttemp"+current+"  select t.hphm,t.hpzl,t.hpys ,date_format(t.gcsj,'%Y-%m-%d') as tt from catchinfotemp"+current+" t group by t.hphm,t.hpzl,t.hpys,date_format(t.gcsj,'%Y-%m-%d') ";
		String count_temp = " create table counttemp"+current
			+" select a.hphm,a.hpzl,a.hpys,a.cs,b.xzqs,c.kks,d.ts from "
			+" (select hphm,hpzl,hpys,count(*) as cs from catchinfotemp"+current+" group by hphm,hpzl,hpys) a "
			+" left join "
			+" (select hphm,hpzl,hpys,count(xzqh) as xzqs from xzqhtemp"+current+" group by hphm,hpzl,hpys) b "
			+" on a.hphm = b.hphm and a.hpzl = b.hpzl and a.hpys = b.hpys "
			+" left join "
			+" (select hphm,hpzl,hpys,count(kk) as kks from kktemp"+current+" group by hphm,hpzl,hpys) c "
			+" on a.hphm = c.hphm and a.hpzl = c.hpzl and a.hpys = c.hpys "
			+" left join "
			+" (select hphm,hpzl,hpys,count(tt) as ts from ttemp"+current+" group by hphm,hpzl,hpys) d "
			+" on a.hphm = d.hphm and a.hpzl = d.hpzl and a.hpys = d.hpys ";
		this.jdbcScsTemplate.update(xzqh_temp);
		this.jdbcScsTemplate.update(kk_temp);
		this.jdbcScsTemplate.update(t_temp);
		this.jdbcScsTemplate.update(count_temp);
		
		String sql = " select t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS,t.xzqs as XZQS,t.kks as KKS,t.ts as TS,t.cs as CS from t(counttemp"+current+") order t.cs:desc ";		
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		
		//删除临时表
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS catchinfotemp"+current);	
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS hphmtemp"+current);	
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS xzqhtemp"+current);
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS kktemp"+current);
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS ttemp"+current);
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS counttemp"+current);
		return map;
	}

	public Map<String, Object> offenInOutByScsDb(Map filter, String consql,String pl,String tablename,String cssql) {		
		StringBuffer sbf = new StringBuffer("select * from ( select a.hphm,a.hpzl,a.hpys,count(*) cs,count(distinct kdbh) kks,count(distinct substr(kdbh,0,6)) xzqs," );
					 sbf.append(" count(distinct to_char(gcsj,'yyyy-mm-dd')) as ts  from ( select distinct hphm,hpzl,gcsj,hpys,kdbh from veh_passrec where 1=1 " );
					 sbf.append(consql).append(" )a group by hphm,hpzl,hpys ) b where 1=1 ");
					 sbf.append(cssql);
		if(pl != null && !"".equals(pl)) {			//ct1_sql += " having count(*) >= "+pl;
			String[] pcgzs = pl.split("通行次数");
			if(pcgzs.length == 2) {
					 sbf.append(" having 1=1 ");
				if(pcgzs[0] != null && !"".equals(pcgzs[0])) {
					 sbf.append(" and count(*) >= ").append(pcgzs[0].substring(0, pcgzs[0].length()-2));
				}
				if(pcgzs[1] != null && !"".equals(pcgzs[1])) {
					 sbf.append(" and count(*) ").append(pcgzs[1]);
				}
			}
		}
						
		String sql = sbf.append(" order by decode(b.hphm,'','1','-','2','无车牌','3',b.hphm), cs desc ").toString();	
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));				
		return map;
	}
	public List<Map<String, Object>> offenInOutByScsDbExt(Map<String, Object> filter, String sql)throws Exception {		
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	public int getTotals(String sql)throws Exception {
		return super.getTotal(sql);
	}
//	public String getSql(Map filter, String consql,String pl,String tablename,String cssql) throws Exception{
//		StringBuffer sbf = new StringBuffer("select * from ( select a.hphm,a.hpzl,a.hpys,count(*) cs,count(distinct kdbh) kks,count(distinct substr(kdbh,0,6)) xzqs," );
//		sbf.append(" count(distinct to_char(gcsj,'yyyy-mm-dd')) as ts  from ( select distinct hphm,hpzl,gcsj,hpys,kdbh from veh_passrec where 1=1 " );
//		sbf.append(consql).append(" )a group by hphm,hpzl,hpys ) b where 1=1 ");
//		sbf.append(cssql);
//		if(pl != null && !"".equals(pl)) {			//ct1_sql += " having count(*) >= "+pl;
//			String[] pcgzs = pl.split("通行次数");
//			if(pcgzs.length == 2) {
//				 sbf.append(" having 1=1 ");
//				 if(pcgzs[0] != null && !"".equals(pcgzs[0])) {
//					 sbf.append(" and count(*) >= ").append(pcgzs[0].substring(0, pcgzs[0].length()-2));
//				 }
//				 if(pcgzs[1] != null && !"".equals(pcgzs[1])) {
//					 sbf.append(" and count(*) ").append(pcgzs[1]);
//				 }
//			}
//		}	
//		String sql = sbf.append(" order by decode(b.hphm,'','1','-','2','无车牌','3',b.hphm), cs desc ").toString();	
//		return sql ;
//	}
	public Map<String, Object> districtbyScsDb(Map filter, String consql, String[] conditionArray,String tablename) throws Exception {

		
		StringBuffer sd = new StringBuffer( " select t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS," +
						  "count(distinct substr(kdbh,0,6)) as XZQS,count(distinct kdbh) as KKS,count(distinct to_char(gcsj, 'yyyy-mm-dd')) as TS,count(1) as CS from ( ");				
		for(int i =0;i<conditionArray.length;i++) {
				sd.append(conditionArray[i]);
			if(i<conditionArray.length-1) {
				sd.append(" union all ");
			}
		}
				sd.append(") t where 1=1 ").append(" group by t.hphm,t.hpzl,t.hpys having count(t.hphm) = ").append(conditionArray.length);
							
		String sql = sd.append(" order by count(distinct kdbh) desc ").toString();		
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		
		return map;
	
	}
	
	/**
	 * <first written> licheng 2016-5-25 区域碰撞去掉原有区域数、卡口数、天数、次数，改为统计各个域出现次数
	 * @param filter
	 * @param consql
	 * @param conditionArray
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> district2byScsDb(Map filter, String consql, String[] conditionArray,String tablename) throws Exception {
		// 拼接area查询字符串
		StringBuffer areaSelect = new StringBuffer();
		// 拼接area查询字符串
		StringBuffer areaOrder = new StringBuffer();
		for(int k = 1; k <= conditionArray.length; k++){
			areaSelect.append(",SUM (area"+k+") as AREA"+k);
			areaOrder.append(" area"+k+" desc,");
		}
		
		StringBuffer sd = new StringBuffer( " select t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS " + areaSelect +
						  " from ( ");				
		for(int i =0;i<conditionArray.length;i++) {
				sd.append(conditionArray[i]);
			if(i<conditionArray.length-1) {
				sd.append(" union all ");
			}
		}
		sd.append(") t where 1=1 ").append(" group by t.hphm,t.hpzl,t.hpys ");
		String sql = sd.append(" order by "+areaOrder.substring(0,areaOrder.length()-1)).toString();		
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		
		return map;
	
	}
	public List<Map<String, Object>> district2byScsDbExt(Map filter, String consql, String[] conditionArray,String tablename,Object[] params) throws Exception {
		String sql = getSql2(filter, consql,conditionArray,tablename);	
		long pstart = System.currentTimeMillis();
		//List<Map<String, Object>> list = super.jdbcScsTemplate.queryForList(sql);
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		long pend = System.currentTimeMillis();
		log.info("区域碰撞:JCBKSQL-PAGE:(" + (pend-pstart) + "毫秒)" + sql);
		return list;
	
	}
	public int getTotal2(Map filter, String consql, String[] conditionArray,String tablename)
	throws Exception {
		String sql = getSql2(filter, consql,conditionArray,tablename);
		return super.getTotal(sql);
	}
	public String getSql2(Map filter, String consql, String[] conditionArray,String tablename) throws Exception{
		// 拼接area查询字符串
		StringBuffer areaBeforSelect = new StringBuffer();
		// 拼接area查询字符串
		StringBuffer areaWhereSelect = new StringBuffer();
		// 拼接area查询字符串
		StringBuffer areaSelect = new StringBuffer();
		// 拼接area查询字符串
		StringBuffer areaOrder = new StringBuffer();
		for(int k = 1; k <= conditionArray.length; k++){
			areaBeforSelect.append(",area"+k);
			areaSelect.append(",SUM (area"+k+") as AREA"+k);
			areaSelect.append(",SUM (qy"+k+") as qt"+k);
			if(k==1){
				areaOrder.append(" qt"+k);
				areaWhereSelect.append(" qt"+k);
			}else{
				areaWhereSelect.append(" + qt"+k);
				areaOrder.append(" + qt"+k);
			}
		}
		
		StringBuffer sd = new StringBuffer( " select HPHM,HPZL " + areaBeforSelect +
						  " from ( "+"select HPHM,HPZL "+areaSelect +" from ( ");				
		for(int i =0;i<conditionArray.length;i++) {
				sd.append(conditionArray[i]);
			if(i<conditionArray.length-1) {
				sd.append(" UNION  ALL");
			}
		}
		sd.append(") a1 ").append(" group by hphm,hpzl ) b1 where 1=1");
		sd.append("  and ( "+areaWhereSelect+" ) > 1");
		sd.append("  ORDER BY ( "+areaOrder+" ) DESC");
		return sd.toString();
	}
	
	public Map<String, Object> pageByScsDb(Map filter, String tablename) throws Exception {
		String sql = " select t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS,t.xzqs as XZQS,t.kks as KKS,t.ts as TS,t.cs as CS from t("+tablename+") order t.cs : desc,t.hphm : desc ";
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}

	public Map<String, Object> queryGjList(Map filter, String consql) {
		String sql = "select distinct t.gcxh as GCXH,t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS,t.kdbh as KDBH,t.fxbh as FXBH,t.gcsj as GCSJ,t.tp1 as TP1 "
			+ " from veh_passrec t where  1=1 "+consql + " order by t.gcsj desc ";
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}
	public List<Map<String, Object>> queryGjListExt(Map filter, String consql) throws Exception {
		String sql = "select distinct t.gcxh as GCXH,t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS,t.kdbh as KDBH,t.fxbh as FXBH,t.gcsj as GCSJ,t.tp1 as TP1 "
			+ " from veh_passrec t where  1=1 "+consql + " order by t.gcsj desc ";
		List<Map<String, Object>> list = this.getPageDatas(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return list;
	}
	public int getTotal3(Map filter, String consql)
	throws Exception {
		String sql = "select distinct t.gcxh as GCXH,t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS,t.kdbh as KDBH,t.fxbh as FXBH,t.gcsj as GCSJ,t.tp1 as TP1 "
			+ " from veh_passrec t where  1=1 "+consql + " order by t.gcsj desc ";
		return super.getTotal(sql);
	}
	public Map<String, Object> supposeGjList(Map filter, String[] conditionArray) {
		StringBuffer sd1 = new StringBuffer();
		StringBuffer sd = new StringBuffer( " select t.gcxh as GCXH,t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS,t.kdbh as KDBH,t.fxbh as FXBH,t.gcsj as GCSJ,t.tp1 as TP1 from (" );		  
		for(int i =0;i<conditionArray.length;i++) {
			sd1.append(conditionArray[i]);
		if(i<conditionArray.length-1) {
			sd1.append(" union all ");
		}
		}
		sd.append(sd1.toString()).append(") t where 1=1 and gcxh in (select min(gcxh) from ( ").append(sd1.toString()).append(" )c group by hphm,hpzl,kdbh,gcsj)");
		if(filter == null) {
			List<Map<String, Object>> list = this.jdbcScsTemplate.queryForList(sd.toString());
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("rows", list);
			return result;
		}
		
		Map<String, Object> map = this.findPageForMap(sd.toString(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}
	
	public int alarmRule(String condition) {
		try {
			String sql = "select count(*) from veh_passrec where 1=1  "
					+ condition + "";
			//System.out.println(sql);
			return this.queryForInt(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	

}
