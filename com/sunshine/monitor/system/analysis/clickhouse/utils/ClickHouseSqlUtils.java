package com.sunshine.monitor.system.analysis.clickhouse.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.sunshine.monitor.system.analysis.bean.CombineCondition;
import com.sunshine.monitor.system.analysis.bean.HideDayOutNightDayBean;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.clickhouse.bean.ClickHouseConstant;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

/**
 * 拼接研判分析sql语句工具类--基于clickhouse
 * @author quxiaol
 * @date 2020-08-20
 *
 */
@Component
public class ClickHouseSqlUtils implements InitializingBean {
	
	@Autowired
	@Qualifier("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	private static JdbcTemplate jdbcOracleTemplate;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		jdbcOracleTemplate = jdbcTemplate;
	}

	/**
	 * 拼接频繁进出分析sql语句
	 * @param filter 
	 * @param cc
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public static String getOffenInOutSql(Map<String, Object> filter, CombineCondition cc,String tablename)throws Exception {
		// 拼接sql与参数预编译
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer("SELECT hphm, hpzl, cs, kks, xzqs, ts FROM (");
		sb.append("SELECT hphm,hpzl,COUNT (1) cs,COUNT (DISTINCT kkbh) kks,COUNT (DISTINCT xzqh) xzqs," +
				"COUNT (DISTINCT toDate(gcsj)) AS ts FROM ");
		sb.append(tablename);
		sb.append(" where 1 = 1 ");
		
		JSONObject jo = JSONObject.fromObject(cc.getCondition());
		String kssj = jo.getString("kssj");
		String jssj = jo.getString("jssj");
		String kssd1 = jo.getString("kssd1");
		String jssd1 = jo.getString("jssd1");
		String kssd2 = jo.getString("kssd2");
		String jssd2 = jo.getString("jssd2");
		String kssd3 = jo.getString("kssd3");
		String jssd3 = jo.getString("jssd3");
		String times = jo.getString("times");
		String kdbhs = jo.getString("kdbh");
		
		if(kdbhs != null && !"".equals(kdbhs)) {
			String[] kdbhArray = kdbhs.split(",");
			String kkbhSql = "";
			for(int j= 0 ;j<kdbhArray.length; j++) {
				kkbhSql += "'"+kdbhArray[j]+"'"; 
				if(j < kdbhArray.length-1) {
					kkbhSql += ",";
				}
			}
			sb.append("and kkbh  in ("+ kkbhSql +") ");
		}

		if(cc.getHphm() != null && !"".equals(cc.getHphm())) {
			String hphm = cc.getHphm();
			if (hphm.contains(",")) {
				//多个hphm，用in
				hphm = "'" + hphm.replaceAll(",", "','") + "'";
				sb.append("and hphm in (" + hphm + ") ");
			} else {
				//单个号牌号码，用=
				sb.append("and hphm = '" + hphm + "' ");
			}
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			sb.append("and hpzl = '"+cc.getHpzl()+"' ");
		}			
		if(kssj != null && !"".equals(kssj)) {
			sb.append("and gcsj >= '"+kssj.trim()+" 00:00:00' ");
		}
		if(jssj != null && !"".equals(jssj)) {
			sb.append("and gcsj <= '"+jssj.trim()+" 23:59:59' ");
		}
		sb.append(" AND (");
		if(kssd1 != null && !"".equals(kssd1)&& !"undefined".equals(kssd1)) {
			sb.append(" (formatDateTime(gcsj,'%H:%M') >= '"+kssd1.trim()+"' ");
		}
		if(jssd1 != null && !"".equals(jssd1)&& !"undefined".equals(jssd1)) {
			sb.append(" and formatDateTime(gcsj,'%H:%M') <= '"+jssd1.trim()+"')");
		}
		if(kssd2 != null && !"".equals(kssd2)&& !"undefined".equals(kssd2)) {
			sb.append(" or (formatDateTime(gcsj,'%H:%M') >= '"+kssd2.trim()+"' ");
		}
		if(jssd2 != null && !"".equals(jssd2)&& !"undefined".equals(jssd2)) {
			sb.append(" and formatDateTime(gcsj,'%H:%M') <= '"+jssd2.trim()+"')");
		}
		if(kssd3 != null && !"".equals(kssd3)&& !"undefined".equals(kssd3)) {
			sb.append(" or (formatDateTime(gcsj,'%H:%M') >= '"+kssd3.trim()+"' ");
		}
		if(jssd3 != null && !"".equals(jssd3)&& !"undefined".equals(jssd3)) {
			sb.append(" and formatDateTime(gcsj,'%H:%M') <= '"+jssd3.trim()+"')");
		}
		sb.append(")");
		sb.append(" GROUP BY hphm,hpzl ");
		sb.append(" ) a1 ");
		sb.append(" where 1=1 ");
		if(times!= null && !"".equals(times)) {
			sb.append(" and cs >= "+times+" ");
		}
		sb.append("ORDER BY	cs DESC");
		return sb.toString();
	}
	
	/**
	 * 拼接车辆关联分析sql语句
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public static String getLinkedAnalysisSql(ScsVehPassrec veh, Map<String, Object> filter) throws Exception{
		// 条件整理
		StringBuffer sb = new StringBuffer(" select concat(t.kkbh,'|',t.fxlx,'|',t.hphm,'|',t.hpzl,'|',toString(t.gcsj)) as gcxh,kkbh as kdbh,fxlx as fxbh,hpzl,gcsj,hpys,cllx,hphm,csys,tp1 from " + ClickHouseConstant.PASS_TABLE_NAME + " t ");
		sb.append(" where t.gcsj >= '").append(veh.getKssj()).append("'")
		  .append(" and  t.gcsj <= '").append(veh.getJssj()).append("'");
		if(veh.getHphm()!=null&&veh.getHphm().length()>0)
			sb.append(" and t.hphm like '").append(veh.getHphm()).append("'");
		if(veh.getKdbh()!=null&&veh.getKdbh().length()>2)
			sb.append(" and t.kkbh in (").append(veh.getKdbh()).append(")");
		if(veh.getHpys()!=null&&veh.getHpys().length()>0)
			sb.append(" and t.hpys = '").append(veh.getHpys()).append("'");
		if(veh.getCsys()!=null&&veh.getCsys().length()>0)
			sb.append(" and t.csys = '").append(veh.getCsys()).append("'");
		if(veh.getHpzl()!=null&&veh.getHpzl().length()>0)
			sb.append(" and t.hpzl = '").append(veh.getHpzl()).append("'");
		if(veh.getCllx()!=null&&veh.getCllx().length()>0)
			sb.append(" and t.cllx = '").append(veh.getCllx()).append("'");
		StringBuffer sql = new StringBuffer();
		sql.append(" select hphm,hpys,hpzl,"
				+ "count(gcxh) as cs from ( ").append(sb).append(")  x group by hphm,hpys,hpzl order by cs desc");
		return sql.toString();
	}
	
	/**
	 * 拼接区域碰撞分析sql语句
	 * @param filter
	 * @param consql
	 * @param conditionArray
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public static String getDistrictSql(Map filter, String consql, String[] conditionArray,String tablename) throws Exception{
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
			areaSelect.append(",SUM(area"+k+") as area"+k);
			areaSelect.append(",SUM(qy"+k+") as qt"+k);
			if(k==1){
				areaOrder.append(" area"+k);
				areaWhereSelect.append(" qt"+k);
			}else{
				areaWhereSelect.append(" + qt"+k);
				areaOrder.append(" + area"+k);
			}
		}
		
		StringBuffer sd = new StringBuffer( " select hphm as HPHM,hpzl as HPZL " + areaBeforSelect +
						  " from ( "+"select hphm,hpzl "+areaSelect +" from ( ");				
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
	
	/**
	 * 拼接昼伏夜出分析sql语句
	 * @param filter
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static String getDayNightSql(Map<String, Object> filter, HideDayOutNightDayBean bean) throws Exception{
		// 条件整理
		StringBuffer sb = new StringBuffer();
		sb.append(" select hphm,hpzl,hpys,gccs as cs,score,ts from (");
		sb.append(" select hphm,hpzl,hpys,sum(gccs) as gccs,sum(score) as score,count(distinct gcsj_date) as ts from (");
		sb.append(" select hphm,hpzl,hpys,count(1) as gccs,toDate(gcsj) as gcsj_date,toHour(gcsj) as gcsj_hour,");
		sb.append(" case when toHour(gcsj) >= 6 and toHour(gcsj) < 18 then -1 when toHour(gcsj) >= 18 and toHour(gcsj) < 23 then 0 else 2 end as score");
		sb.append(" from " + ClickHouseConstant.PASS_TABLE_NAME + " where 1 = 1 ");
		
		if(StringUtils.isNotBlank(bean.getKssj()))			
			sb.append(" and gcsj >= '").append(bean.getKssj().trim()).append(" 00:00:00'");
		if(StringUtils.isNotBlank(bean.getJssj()))			
			sb.append(" and gcsj <= '").append(bean.getJssj().trim()).append(" 23:59:59'");
		if(StringUtils.isNotBlank(bean.getHphm()))
			sb.append(" and hphm like '").append(bean.getHphm()).append("'");
		if(StringUtils.isNotBlank(bean.getHpys()))
			sb.append(" and hpys = '").append(bean.getHpys()).append("'");		
		if(StringUtils.isNotBlank(bean.getHpzl()))
			sb.append(" and hpzl = '").append(bean.getHpzl()).append("'");			
		sb.append(" group by hphm,hpzl,hpys,toDate(gcsj),toHour(gcsj))");
		sb.append(" group by hphm,hpzl,hpys)");
		sb.append(" where score/ts >= 4 and gccs ").append(bean.getComple()).append(bean.getCs()).append("");
		sb.append(" order by cs desc");		
		return sb.toString();
	}
	
	/**
	 * 拼接模糊号牌分析sql语句
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public static String getLikeSql(VehPassrec veh, Map<String, Object> filter) throws Exception{
		// 条件整理
		String hpys = "";
		if (veh.getHpys() != null) {
			if (!veh.getHpys().equals(""))
				hpys = " and t.hpys='" + veh.getHpys() + "'";
		}
		String csys = "";
		if (veh.getCsys() != null) {
			if (!veh.getCsys().equals(""))
				csys = " and t.csys='" + veh.getCsys() + "'";
		}
		String hpzl = "";
		if (veh.getHpzl() != null) {
			if (!veh.getHpzl().equals(""))
				hpzl = " and t.hpzl='" + veh.getHpzl() + "'";
		}

		String kdbh = "";
		if (veh.getKdbh() != null) {
			if (!veh.getKdbh().equals("''")) {
				kdbh = "and t.kkbh in (" + veh.getKdbh() + ")";
			}
		}

		String sql = " select * from (select count(t.hphm) as GCCS,t.hphm as HPHM,t.hpys as HPYS,t.hpzl as HPZL from " + ClickHouseConstant.PASS_TABLE_NAME + " t"
				+ " where t.hphm like '"
				+ veh.getHphm()
				+ "'"
				+ " and t.gcsj >= '"
				+ veh.kssj
				+ "'"
				+ " and t.gcsj <= '"
				+ veh.jssj
				+ "'"
				+ kdbh
				+ hpys
				+ csys
				+ hpzl
				+ "  group by t.hphm,t.hpys,t.hpzl ) c order by GCCS desc ";		
		return sql;
	}
	
	/**
	 * 拼接异地车辆分析sql语句
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public static String getAnotherPlaceSql(Map<String, Object> filter) throws Exception{
		//得到本地行政区划
		String local ="";
		boolean flag_shengting=false;
		if(filter.get("local")!=null){
			if(filter.get("local").toString().equals("430000000000")){
			local=filter.get("local").toString().substring(0,2);
			flag_shengting=true;}
			else
			local=filter.get("local").toString().substring(0,4);
		}
		//得到注册地
		String zcd="";
		if(filter.get("zcd")!=null){
			if(!filter.get("zcd").toString().equals("")){
			zcd="  province in ("+filter.get("zcd").toString()+")";
			}
		}			
		
		//获取session回话ID
		String sessionid="";
		if(filter.get("sessionid")!=null){
			sessionid=filter.get("sessionid").toString();
		}
		 
		//取本地所属地(现省厅改为全部市州)
		String localhp="";
		if(flag_shengting)
		localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '4300%' ";
		else
		localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"+local+"%' ";	
		String localhplists="";
		List localhplist = jdbcOracleTemplate.queryForList(localhp);
		for(int i=0;i<localhplist.size();i++){
			Map map=(Map)localhplist.get(i);
			localhplists+="'"+map.get("HPHM").toString()+"',";
			
		}
		if(localhplists.length()>0){
			localhplists=localhplists.substring(0, localhplists.length()-1);
		
		if(flag_shengting){
			localhplists=" AND SUBSTRING(hphm,1,1) NOT IN ("+localhplists+") ";
		}
		else
			localhplists=" AND SUBSTRING(hphm,1,2) NOT IN ("+localhplists+") ";
		}
		//过境型参数		
		int  gjx=3;
		if(filter.get("gjx")!=null){
			if(filter.get("gjx").toString().length()!=0){
				gjx=Integer.parseInt(filter.get("gjx").toString());
			}
		}
		//候鸟型参数
		//int hnx=0;
		//本地型参数
		int bdx=6;
		if(filter.get("bdx")!=null){
			if(filter.get("bdx").toString().length()!=0){
				bdx=Integer.parseInt(filter.get("bdx").toString());
			}
		}
		   
		//利用分析函数实现去重
	String temp_sql = "select t.*,(case when total<="+gjx+" then 'gjx' " 
		+" when total>"+gjx+" and total <="+bdx+" then 'hnx' else 'bdx' end) as zt " 
		+" from ( select hphm,hpzl,count(1) as total from " + ClickHouseConstant.PASS_TABLE_NAME + " where " 
		+zcd
		+" and gcsj >= '"+filter.get("kssj").toString()+"' "
	    +" and gcsj <= '"+filter.get("jssj").toString()+"' "	    
		+" group by hphm,hpzl ) t "
		;
		String sql = " select substr(hphm, 1, 4) HP, COUNT (1) TOTAL," +
				 " sum(case when zt='gjx' then 1 else 0 end) GJX," +
				 " sum(case when zt='hnx' then 1 else 0 end) HNX," +
				 " sum(case when zt='bdx' then 1 else 0 end) BDX" +
				 " from ("+temp_sql+") c1 GROUP BY substr(hphm, 1, 4) order by substr(hphm, 1, 4) asc ";
		return  sql;
	}
	
	/**
	 * 拼接异地车详情sql
	 * @param xh
	 * @param filter
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public static String getAnotherPlaceDetilSql(String xh,Map<String, Object> filter, String sign) throws Exception{
		//根据任务序号获取任务条件
		String sql ="select * from  " 
			+	" frm_otherplace_zcd  " 
			+	" where xh = '"+xh+"' ";
		Map contision=null;
		List list = jdbcOracleTemplate.queryForList(sql);
		//去重注册地
		Set<String> set_zcd=new HashSet<String>();
		if (list.size() > 0) {
			contision = (Map) list.get(0);
			for(int i =0;i<list.size();i++){
				Map map=(Map)list.get(i);
				set_zcd.add(map.get("ZCD").toString().substring(0,1));
			}
		}
		
		//得到本地行政区划
		String local ="";
		if(contision.get("local")!=null){
			local=contision.get("local").toString();
		}
		
		boolean flag_shengting=false;
		if(local.equals("430000000000")){
			local = local.substring(0,2);
			flag_shengting=true;
		}else{
			local = local.substring(0,4);
		}
		
		//取本地所属地
		String localhp="";
		if(flag_shengting)
		 localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '4300%' ";
		else
		 localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"+local+"%' ";
		String localhplists="";
		List localhplist = jdbcOracleTemplate.queryForList(localhp);
		if(localhplist.size()>0){
		for(int i=0;i<localhplist.size();i++){
			Map map=(Map)localhplist.get(i);
			localhplists+="'"+map.get("HPHM").toString()+"',";
			
		}
		if(localhplists.length()>0){
			localhplists=localhplists.substring(0, localhplists.length()-1);
		}
		
		if(flag_shengting){
			localhplists=" AND SUBSTRING(hphm,1,1) NOT IN ("+localhplists+") ";
		}
		else
			localhplists=" AND SUBSTRING(hphm,1,2) NOT IN ("+localhplists+") ";		
		}
		//得到注册地
		String zcd="";
		if(set_zcd.size()>0){
			for(Object s:set_zcd){
				zcd+="'"+s.toString()+"',";
			}
		}
		else
		zcd="''";
		if(zcd.length()>2){
			zcd=zcd.substring(0,zcd.length()-1);
		}
		
		//不同类型不同极限
		String type="";		
		if(sign!=null){
			if(sign.equals("BDX")){
				type="  cs > "+contision.get("TJ_BDX_RANGE").toString()+"  ";
			}
			if(sign.equals("HNX")){
				type="  cs > "+contision.get("TJ_GJX_RANGE").toString()+" and  cs <= "+contision.get("TJ_BDX_RANGE")+" ";				
			}
			if(sign.equals("GJX")){
				type="  cs <= "+contision.get("TJ_GJX_RANGE").toString()+"  ";
			}
			if(sign.equals("TOTAL")){
				type="  1=1  ";
			}
		}
					
		String temp_sql = " select hphm,hpzl,max(hpys) as hpys,COUNT(1) cs,COUNT(DISTINCT kkbh) kks, COUNT(DISTINCT xzqh) xzqs,"
			+"COUNT(DISTINCT toDate(gcsj)) AS ts from " + ClickHouseConstant.PASS_TABLE_NAME + " where " 
			+" province in ("+zcd+")"
			+" and gcsj >= '"+contision.get("TI_KSSJ").toString()+"' "
		    +" and gcsj <= '"+contision.get("TJ_JSSJ").toString()+"' "
		    +" GROUP BY hphm,hpzl ";
		
		String select_sql = " select hphm,hpzl,hpys,cs,kks,xzqs,ts FROM ("+temp_sql+") c1 " +
					 " where"+type+" order by cs desc ";
		return select_sql;	
	}

}
