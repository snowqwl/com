package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.FalseLicense;
import com.sunshine.monitor.system.analysis.dao.FalseLicenseSCSDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("falseSCSDao")
public class FalseLicenseSCSDaoImpl extends ScsBaseDaoImpl implements FalseLicenseSCSDao{

	protected Logger debugLogger = LoggerFactory.getLogger(FalseLicenseSCSDaoImpl.class);
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void delFalseLicense(List<FalseLicense> list) throws Exception {
		
	}

	public Map<String, Object> findPageForFL(Map<String, Object> map,
			FalseLicense fl) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map getAllHphm(Map<String, Object> filter) throws Exception {
		String sql =" SELECT t.hphm, t.hpzl FROM t("+Constant.SCS_PASS_TABLE+"::t.hphm <>'无牌' and t.hphm<>'-' and t.hphm<>'未识别牌' and t.hphm<>'NoPlate' and t.hphm<>'NA'"
			+" and t.gcsj > '"+filter.get("kssj").toString()+"'"
		    +" and t.gcsj <= '"+filter.get("jssj").toString()+"'"
		    +" ) group  t.hphm, t.hpzl" 
		    //+" group t.hphm,t.hpzl "
		    ;
		Map map =this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()),Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	/**
	 * 查询嫌疑假牌分组数
	 */
	@SuppressWarnings("unchecked")
	public Map getFalseCount(Map<String, Object> filter) throws Exception {
		Map map = new HashMap();
		//设置条件
		String kssj="";
		if(filter.get("kssj")!=null){
			if(filter.get("kssj").toString()==null||"".equals(filter.get("kssj").toString())){
				kssj="2015-05-01 00:00:00";//默认时间
			}
			else
				kssj=filter.get("kssj").toString();	
		}
		String uuid = UUID.randomUUID().toString().substring(0, 5);
		String tablename_veh=Constant.SCS_TEMP_PREFIX+"FalseLicense_getFalseCount"+uuid;
		try{
		String sql = ""		
			+"		SELECT T1.TJ TJ,   "
			+"	       ifnull(T1.TOTAL, 0) TOTAL,"
			+"	       ifnull(T12.XYTOTAL, 0) XYTOTAL,"
			+"	       ifnull(T13.ISTOTAL, 0) ISTOTAL,"
			+"	       ifnull(T14.NOTOTAL, 0) NOTOTAL,"
			+"	       ifnull(T15.UNKNOWTOTAL, 0) UNKNOWTOTAL,"
			+"	       ifnull(T2.LPS, 0) LPS,"
			+"	       ifnull(T3.XYLPS, 0) XYLPS,"
			+"	       ifnull(T4.ISLPS, 0) ISLPS,"
			+"	       ifnull(T5.NOLPS, 0) NOLPS,"
			+"	       ifnull(T6.UNKNOWLPS, 0) UNKNOWLPS,"
			+"	       ifnull(T7.ZPS, 0) ZPS,"
			+"	       ifnull(T8.XYZPS, 0) XYZPS,"
			+"	       ifnull(T9.ISZPS, 0) ISZPS,"
			+"	       ifnull(T10.NOZPS, 0) NOZPS,"
			+"	       ifnull(T11.UNKNOWZPS, 0) UNKNOWZPS"
			+"	  FROM (select tj, count(1) as total"
			+"	          from (select hphm, hpzl, DATE(J.TJSJ) AS TJ"
			+"	                  FROM hn_jm_false_license J"
			+"	                 WHERE " 
			+" TJSJ >= '"+kssj+"' "
			+" and TJSJ < '"+filter.get("jssj").toString()+"'"
			+"	                 GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	         group by tj) T1"
			+"	  LEFT JOIN (select tj, count(1) as xytotal"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1 "
			//+"	                      AND J.HPHM IN (SELECT HPHM FROM frm_false_table)"
			+" and TJSJ >= '"+kssj+"' "
			+" and TJSJ < '"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T12"
			+"	    ON T1.TJ = T12.TJ"
			+"	  LEFT JOIN (select tj, count(1) as istotal"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1 "
			//+"	                      AND  J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '1')"
			+"	                           AND ZT = '1'"
			+" and TJSJ >= '"+kssj+"'  "
			+" and TJSJ < '"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T13"
			+"	    ON T1.TJ = T13.TJ"
			+"	  LEFT JOIN (select tj, count(1) as nototal"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      and J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '2')"
			+"	                            AND ZT = '2'"
			+" and TJSJ >= '"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T14"
			+"	    ON T1.TJ = T14.TJ"
			+"	  LEFT JOIN (select tj, count(1) as unknowtotal"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, hphm, hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '0')"
			+"	                            AND ZT = '0'"
			+" and TJSJ >= '"+kssj+"'  "
			+" and TJSJ < '"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T15"
			+"	    ON T1.TJ = T15.TJ"
			+"	  LEFT JOIN (select tj, count(1) as lps"
			+"	               from (select hphm, hpzl, DATE(J.TJSJ) AS TJ"
			+"	                       FROM hn_jm_false_license J where 1=1"
			+"	                      AND hpzl = '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ),hpzl, hphm) a"
			+"	              group by tj) T2"
			+"	    ON T1.TJ = T2.TJ"
			+"	  LEFT JOIN (select tj, count(1) as xylps"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN (SELECT HPHM FROM frm_false_table)"
			+"	                        AND hpzl = '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T3"
			+"	    ON T1.TJ = T3.TJ"
			+"	  LEFT JOIN (select tj, count(1) as islps"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '1')"
			+"	                           AND ZT = '1' "
			+"	                        AND hpzl = '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T4"
			+"	    ON T1.TJ = T4.TJ"
			+"	  LEFT JOIN (select tj, count(1) as nolps"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '2')"
			+"	                            AND ZT = '2'"
			+"	                        AND hpzl = '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T5"
			+"	    ON T1.TJ = T5.TJ"
			+"	  LEFT JOIN (select tj, count(1) as unknowlps"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '0')"
			+"	                            AND ZT = '0'"
			+"	                        AND hpzl = '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T6"
			+"	    ON T1.TJ = T6.TJ"
			+"	  LEFT JOIN (select tj, count(1) as zps"
			+"	               from (SELECT hphm, hpzl, DATE(J.TJSJ) AS TJ"
			+"	                       FROM hn_jm_false_license J"
			+"	                      WHERE hpzl <> '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T7"
			+"	    ON T1.TJ = T7.TJ"
			+"	  LEFT JOIN (select tj, count(1) as xyzps"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN (SELECT HPHM FROM frm_false_table)"
			+"	                        AND hpzl <> '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T8"
			+"	    ON T1.TJ = T8.TJ"
			+"	  LEFT JOIN (select tj, count(1) as iszps"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '1')"
			+"	                            AND ZT = '1'"
			+"	                        AND hpzl <> '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T9"
			+"	    ON T1.TJ = T9.TJ"
			+"	  LEFT JOIN (select tj, count(1) as nozps"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '2')"
			+"	                            AND ZT = '2'"
			+"	                        AND hpzl <> '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE(J.TJSJ), hpzl, hphm) a"
			+"	              group by tj) T10"
			+"	    ON T1.TJ = T10.TJ"
			+"	  LEFT JOIN (select tj, count(1) as unknowzps"
			+"	               from (SELECT DATE(J.TJSJ) AS TJ, J.hphm, J.hpzl"
			+"	                       FROM hn_jm_false_license J where 1=1"
			//+"	                      AND J.HPHM IN"
			//+"	                            (SELECT HPHM FROM frm_false_table WHERE ZT = '0')"
			+"	                            AND ZT = '0'"
			+"	                        AND hpzl <> '02'"
			+" and TJSJ >='"+kssj+"' "
			+" and TJSJ <'"+filter.get("jssj").toString()+"'"
			+"	                      GROUP BY DATE_FORMAT(J.TJSJ,'%y-%m-%d'), hpzl, hphm) a"
			+"	              group by tj) T11"
			+"	    ON T1.TJ = T11.TJ"
			//+"	 order by tj desc"

					;
			/*String tablename_sqlTotal=Constant.SCS_TEMP_PREFIX+"FalseLicense_getsqlTotal"+uuid;
			StringBuffer sqlTotal=new StringBuffer();
			sqlTotal.append(" create table ").append(tablename_sqlTotal).append(" (");
			sqlTotal.append("SELECT hphm,  hpzl,DATE(J.TJSJ) AS TJ FROM hn_jm_false_license J WHERE TJSJ >= '2014-01-01 00:00:00' AND TJSJ < '2015-01-14 23:59:59' GROUP BY DATE(J.TJSJ), hpzl, hphm") ;
            sqlTotal.append(" )");
			this.jdbcScsTemplate.update(sqlTotal.toString());
            String tablename_sqlTotalCount=Constant.SCS_TEMP_PREFIX+"FalseLicense_getsqlTotalCount"+uuid;
            StringBuffer sqlTotalCount=new StringBuffer();
			sqlTotalCount.append(" create table ").append(tablename_sqlTotalCount).append(" (");
            sqlTotalCount.append("SELECT J.hphm  HPHM,  J.hpzl HPZL ,DATE(J.TJ) TJSJ  FROM J(").append(tablename_sqlTotal).append(")    GROUP  DATE(J.TJ), J.hpzl, J.hphm") ;
            sqlTotalCount.append(" )");
            //List list = this.jdbcScsTemplate.queryForList(sqlTotalCount.toString());
			this.jdbcScsTemplate.update(sqlTotal.toString());
			
            System.out.println("---");*/
                
            
			
			
		//创建临时表
		StringBuffer sb=new StringBuffer();
		sb.append(" create table "+tablename_veh+"("+sql+")" );
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---查询嫌疑假牌分组数(创建临时表)sql："+" create table "+tablename_veh+"("+sql+")");
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---查询嫌疑假牌分组数(创建临时表)start："+sdf.format(new Date()));
		this.jdbcScsTemplate.update(sb.toString());
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---查询嫌疑假牌分组数(创建临时表)end："+sdf.format(new Date()));

		//查询临时表
		 //map =this.findPageForMap("select t.* from t("+tablename_veh+")", Integer.parseInt(filter.get("page").toString()),Integer.parseInt(filter.get("rows").toString()));
		StringBuffer sqltemp=new StringBuffer();
		sqltemp.append("select ");
		sqltemp.append("t.TJ,");
		sqltemp.append("SUM(t.TOTAL) TOTAL,");
		sqltemp.append("SUM(t.XYTOTAL) XYTOTAL,");
		sqltemp.append("SUM(t.ISTOTAL) ISTOTAL,");
		sqltemp.append("SUM(t.NOTOTAL) NOTOTAL,");
		sqltemp.append("SUM(t.UNKNOWTOTAL) UNKNOWTOTAL,");
		sqltemp.append("SUM(t.LPS) LPS,");
		sqltemp.append("SUM(t.XYLPS) XYLPS,");
		sqltemp.append("SUM(t.ISLPS) ISLPS,");
		sqltemp.append("SUM(t.NOLPS) NOLPS,");
		sqltemp.append("SUM(t.UNKNOWLPS) UNKNOWLPS,");
		sqltemp.append("SUM(t.ZPS) ZPS,");
		sqltemp.append("SUM(t.XYZPS) XYZPS,");
		sqltemp.append("SUM(t.ISZPS) ISZPS,");
		sqltemp.append("SUM(t.NOZPS) NOZPS,");
		sqltemp.append("SUM(t.UNKNOWZPS) UNKNOWZPS");
		sqltemp.append(" from t("+tablename_veh+") GROUP TJ ORDER TJ:DESC");
		//sqltemp.append("select t.* from t("+tablename_veh+")");
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---归并数据查询表（天云星语法）sql："+sqltemp);
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---归并数据查询表（天云星语法）start："+sdf.format(new Date()));
		map =this.findPageForMap(sqltemp.toString(), Integer.parseInt(filter.get("page").toString()),Integer.parseInt(filter.get("rows").toString()));
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---归并数据查询表（天云星语法）end："+sdf.format(new Date()));
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+tablename_veh+"");
		 return map;
				
		}catch(Exception e){
			this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+tablename_veh+"");
			e.printStackTrace();
		}
		
		return map;
	}
	
	
	/**
	 * 根据号牌号码查询轨迹
	 */
	@SuppressWarnings("unchecked")
	public Map getFalseListByHphm(Map<String, Object> filter) throws Exception {
		String hphm ="";
		if(filter.get("hphm")!=null){
			hphm=" and hphm='"+filter.get("hphm").toString()+"'";
		}
		@SuppressWarnings("unused")
		String hpys="";
		
		if(filter.get("hpzl")!=null){
			if(filter.get("hpzl").toString().equals("02")){
				hpys=" and hpys='2'";
			}else
				hpys=" and hpys<>'2'";
		}
		String hpzl="";
		if(filter.get("hpzl")!=null){
				hpzl=" and hpzl='"+filter.get("hpzl").toString()+"'";
		}
		String tjsj="";
		if(filter.get("tjsj")!=null){
			tjsj="and date(TJSJ)=date('"+filter.get("tjsj").toString()+"')";
		}
		
		/*String zt ="";
		if(filter.get("zt")!=null){
			zt="AND hphm in (select HPHM from frm_false_table where zt ='"+filter.get("zt").toString()+"')";
		}*/
		String  sql =" select t.tp1 TP1,t.gcxh GCXH,t.hphm HPHM,t.kdbh KDBH,substring(t.kdbh,1,6) XZQH,t.fxbh FXBH,t.gcsj GCSJ,t.hpzl HPZL,t.hpys HPYS "
			+"FROM t(hn_jm_false_license::1=1"
			+hphm
			+hpzl
			//+hpys
			+tjsj
			//+zt
			+")"
			+"  ORDER t.GCXH:DESC"
			;
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---根据号牌号码查询轨迹列表sql："+sql);
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---根据号牌号码查询轨迹列表start："+sdf.format(new Date()));
				Map map=	this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---根据号牌号码查询轨迹列表end："+sdf.format(new Date()));
		return map;
	}
	
	/**
	 * 得到每种类型的统计列表
	 */
	@SuppressWarnings("unchecked")
	public Map getSuspectedListBysign(Map<String, Object> filter)
			throws Exception {

		String tjsj="";
		String zt="";
		String hpzl="";
		String uuid = UUID.randomUUID().toString().substring(0, 5);
		if(filter.get("tjsj")!=null){
			tjsj= filter.get("tjsj").toString();
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内的所有未确认号牌
			if(filter.get("sign").toString().equals("UNKNOWTOTAL")){
				zt=" AND zt='0'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内的所有非假牌
			if(filter.get("sign").toString().equals("NOTOTAL")){
				zt=" AND zt='2'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内的所有假牌
			if(filter.get("sign").toString().equals("ISTOTAL")){
				zt=" AND zt='1'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内未确认的杂牌
			if(filter.get("sign").toString().equals("XYZPS")){
			  hpzl=" and hpzl<>'02'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内未确认的杂牌
			if(filter.get("sign").toString().equals("UNKNOWZPS")){
				zt=" AND zt='0'";
				hpzl=" and hpzl<>'02'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内非假牌的杂牌
			if(filter.get("sign").toString().equals("NOZPS")){
				zt=" AND zt='2'";
				hpzl=" and hpzl<>'02'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内假牌的杂牌
			if(filter.get("sign").toString().equals("ISZPS")){
				zt=" AND zt='1'";
				hpzl=" and hpzl<>'02'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内未确认的杂牌
			if(filter.get("sign").toString().equals("XYLPS")){
				hpzl=" and hpzl='02'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内未确认的蓝牌
			if(filter.get("sign").toString().equals("UNKNOWLPS")){
				zt=" AND zt='0'";
				hpzl=" and hpzl='02'";
			}
		}
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内非假牌的蓝牌
			if(filter.get("sign").toString().equals("NOLPS")){
				zt=" AND zt='2'";
				hpzl=" and hpzl='02'";
			}
		}
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内假牌的蓝牌
			if(filter.get("sign").toString().equals("ISLPS")){
				zt=" AND zt='1'";
				hpzl=" and hpzl='02'";
			}
		}

		
		String tablename_veh=Constant.SCS_TEMP_PREFIX+"FalseLicense_getSuspectedListBysign"+uuid;
		StringBuffer sql =new StringBuffer();
		sql.append("create table "+tablename_veh+" (");
		sql.append(" SELECT T1.HPHM, T1.HPZL, T1.XYGJCS ,T2.QYS, T3.KKS, T4.TS,T1.ZT ");
		sql.append(" FROM" ); 
		sql.append("(SELECT HPHM,HPZL ,COUNT(HPHM) AS XYGJCS,ZT        FROM hn_jm_false_license WHERE 1=1 "+hpzl+" AND DATE(tjsj) = DATE('"+tjsj+"') "+zt+" GROUP BY HPHM,HPZL)T1 ");
		sql.append(" LEFT JOIN "); 
		sql.append("(SELECT HPHM,HPZL ,COUNT(DISTINCT(SUBSTRING(KDBH,1,6))) AS QYS  FROM hn_jm_false_license WHERE 1=1 "+hpzl+" AND DATE(tjsj) = DATE('"+tjsj+"') "+zt+" GROUP BY HPHM,HPZL,SUBSTRING(KDBH,1,6)) T2");
		sql.append(" ON T1.HPHM=T2.HPHM AND T1.HPZL=T2.HPZL"); 
		sql.append(" LEFT JOIN");
		sql.append(" (SELECT HPHM,HPZL ,COUNT(DISTINCT KDBH) AS KKS        FROM hn_jm_false_license WHERE 1=1 "+hpzl+" AND DATE(tjsj) = DATE('"+tjsj+"') "+zt+"  GROUP BY HPHM,HPZL,KDBH)T3 ");
		sql.append(" ON T1.HPHM=T3.HPHM AND T1.HPZL=T3.HPZL");
		sql.append(" LEFT JOIN");
		sql.append(" (SELECT HPHM,HPZL ,COUNT(DISTINCT DATE(GCSJ)) AS TS        FROM hn_jm_false_license WHERE 1=1 "+hpzl+" AND DATE(tjsj) = DATE('"+tjsj+"')  "+zt+" GROUP BY HPHM,HPZL,DATE(GCSJ))T4");
		sql.append(" ON T1.HPHM=T4.HPHM AND T1.HPZL=T4.HPZL");
		//sql.append(" LEFT JOIN"); 
		//sql.append(" (SELECT HPHM,HPZL ,ZT  FROM frm_false_table )T5");
		//sql.append(" ON T1.HPHM=T5.HPHM AND T1.HPZL=T5.HPZL");
		sql.append(" GROUP BY HPHM,HPZL ");
		sql.append(")");
		Map map=new HashMap();
		
		try{
		//创建分组统计临时表
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---得到每种类型的统计列表--创建分组统计临时表sql："+sql);
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---得到每种类型的统计列表--创建分组统计临时表start："+sdf.format(new Date()));
		this.jdbcScsTemplate.update(sql.toString());
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---得到每种类型的统计列表--创建分组统计临时表end："+sdf.format(new Date()));

		//查询临时表(过滤假牌库不存在的假牌轨迹)
		StringBuffer sqltemp=new StringBuffer();
		sqltemp.append("select t.HPHM HPHM,t.HPZL HPZL,t.ZT ZT,SUM(t.XYGJCS) XYGJCS,SUM(t.QYS) QYS, SUM(t.KKS) KKS, SUM(t.TS) TS from t("+tablename_veh+"::1=1) GROUP HPHM,HPZL ORDER t.XYGJCS:DESC");
		//sqltemp.append("select t.* from t("+tablename_veh+"::1=1)  ORDER t.XYGJCS:DESC");
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---得到每种类型的统计列表--归并统计分析数据sql："+sql);
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---得到每种类型的统计列表--归并统计分析数据start："+sdf.format(new Date()));
		map = this.findPageForMap(sqltemp.toString(), Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		debugLogger.info("o(∩_∩)oo(∩_∩)o假牌专题分析查询---得到每种类型的统计列表--归并统计分析数据end："+sdf.format(new Date()));
		//释放临时表
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+tablename_veh+"");
		//this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+tablename_falseHphmList+"");
		}catch(Exception e){
			this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+tablename_veh+"");
			//this.jdbcScsTemplate.update("DROP TABLE IF EXISTS "+tablename_falseHphmList+"");
			e.printStackTrace();
		}
	return map;
	
	}

	@SuppressWarnings("unchecked")
	public List getTrajectoryByHphm(Map<String, Object> filter)
			throws Exception {
		JdbcTemplate jdbcScsTemplate =  (JdbcTemplate) SpringApplicationContext.getStaticApplicationContext().getBean("jdbcScsTemplate");
		String sql =" select t.gcxh,t.hphm,t.kdbh,substring(t.kdbh,1,6) xzqh,t.fxbh,t.gcsj,t.hpys,t.tp1,t.tp2,t.tp3,t.hpzl from t("+Constant.SCS_PASS_TABLE+"::"
				+" t.hphm='"+filter.get("hphm").toString()+"' "
				+" and t.gcsj between str_to_date('"+filter.get("kssj").toString()+"','%Y-%m-%d %H:%i:%s') "
			    +" and str_to_date('"+filter.get("jssj").toString()+"','%Y-%m-%d %H:%i:%s')"
				+		")"
		;
		List list=jdbcScsTemplate.queryForList(sql);
		return list;
	}

	public boolean isExistHphm(Map<String, Object> filter) throws Exception {
		String sql="SELECT t.* FROM t(frm_false_table::1=1" 
			+" and hphm ='"+filter.get("hphm").toString()+"'"
			+")"
			;
		return this.jdbcScsTemplate.queryForList(sql).size()!=0?true:false;
	}

	public void saveFalseLicense(List<FalseLicense> list) throws Exception {
		
	}



	public int writeFalseTable(Map<String, Object> filter) throws Exception {
		String sql="insert into frm_false_table (HPHM,HPZL,ZT) values ("
			+"'"+filter.get("hphm").toString()+"','"+filter.get("hpzl")+"','0'"
			+")";
		int i=this.jdbcScsTemplate.update(sql);
			

	return i;
	}

	@SuppressWarnings("unchecked")
	public int writeTrajectory(final List list) throws Exception {
		try{
		
			String sql="insert into hn_jm_false_license (GCXH,HPHM,KDBH,FXBH,TP1,TP2,TP3,GCSJ,HPYS,HPZL,XZQH,TJSJ,ZT) values ("
				+"?,?,?,?,?,?,?,?,?,?,?,?,?"
				+")";
		BatchPreparedStatementSetter  bpss	=new BatchPreparedStatementSetter()  {						
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							Map map=(Map)list.get(i);
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							String time=sdf.format(new Date());
							time = getSpecifiedDayBefore(time);
							//System.out.println("-----"+time);
							ps.setString(1,map.get("GCXH")==null?"":map.get("GCXH").toString());
							ps.setString(2,map.get("HPHM")==null?"":map.get("HPHM").toString());
							ps.setString(3,map.get("KDBH")==null?"":map.get("KDBH").toString());
							ps.setString(4,map.get("FXBH")==null?"":map.get("FXBH").toString());
							ps.setString(5,map.get("TP1")==null?"":map.get("TP1").toString());
							ps.setString(6,map.get("TP2")==null?"":map.get("TP2").toString());
							ps.setString(7,map.get("TP3")==null?"":map.get("TP3").toString());
							ps.setString(8,map.get("GCSJ")==null?"":map.get("GCSJ").toString());
							ps.setString(9,map.get("HPYS")==null?"":map.get("HPYS").toString());
							ps.setString(10,map.get("HPZL")==null?"":map.get("HPZL").toString());
							ps.setString(11,map.get("XZQH")==null?"":map.get("XZQH").toString());	
							ps.setString(12,time==null?"":time);	
							ps.setString(13,"0");	
						}					
						public int getBatchSize() {
							return list.size();
						}
					
					}	;

		int a[]=	this.jdbcScsTemplate.batchUpdate(sql,bpss);
					
		return a.length;
		}
		catch(org.springframework.dao.DuplicateKeyException ee){
			System.out.println("插入失败:假牌库已存在相同的过车轨迹");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return 0 ;
	}

	public String sureFalse(String hphm, String flag, String hpzl)
			throws Exception {
		
		
		try{
			String vehUpdate=" update  t(hn_jm_false_license::hphm='"+hphm+"' and hpzl ='"+hpzl+"') set t.zt='"+flag+"' ";
			int i=this.jdbcScsTemplate.update(vehUpdate);
			//String baseUpdate ="update  t(frm_false_table::hphm='"+hphm+"' and hpzl ='"+hpzl+"') set t.zt='"+flag+"'" ;
			//int i2=this.jdbcScsTemplate.update(baseUpdate);
			//if(i>0&&i2>0){
			if(i>0){
				return "修改成功";
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
		
	}
	
	public static String getSpecifiedDayBefore(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(c
                .getTime());
        return dayBefore;
    }
	
	@SuppressWarnings("unchecked")
	public Map queryFalseList(Map filter, VehPassrec veh) throws Exception {
		String sql = "select t.* from t(hn_jm_false_license :: zt = '1' ";
		if(veh.getHphm()!=null && !"".equals(veh.getHphm())) {
			sql += " and t.hphm='"+veh.getHphm()+"'";
		}
		if(veh.getHpzl()!=null && !"".equals(veh.getHpzl())) {
			sql += " and t.hpzl='"+veh.getHpzl()+"'";
		}
		if(veh.getHpys()!=null && !"".equals(veh.getHpys())) {
			sql += " and t.hpys='"+veh.getHpys()+"'";
		}
		if(veh.getCsys()!=null && !"".equals(veh.getCsys())) {
			sql += " and t.csys='"+veh.getCsys()+"'";
		}
		if(veh.getKssj() != null && !"".equals(veh.getKssj())) {
			sql += " and t.gcsj >= '"+veh.getKssj()+"'";
		}
		if(veh.getJssj() != null && !"".equals(veh.getJssj())) {
			sql += " and t.gcsj <= '"+veh.getJssj()+"'";
		}
		sql += ") order t.tjsj:desc";
		Map map = this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	public Map queryXyFalseList(Map filter) throws Exception {
		String hphm = "";
		if (filter.get("hphm") != null && (String) filter.get("hphm") != "") {
			hphm = " and hphm='" + filter.get("hphm").toString() + "'";
		}
		String hpys = "";
		if (filter.get("hpys") != null && (String) filter.get("hpys") != "") {
			hpys = " and hpys='" + filter.get("hpys") + "'";
		}
		String hpzl = "";
		if (filter.get("hpzl") != null && (String) filter.get("hpzl") != "") {
			hpzl = " and hpzl='" + filter.get("hpzl").toString() + "'";
		}
		String end = " GROUP BY HPHM, HPZL, HPYS";
		String xyFalse = "SELECT A.hphm, A.hpzl, A.gcsj, A.kdbh, A.cdbh, A.csys, A.hpys FROM veh_passrec A "
				+ "WHERE A.hphm LIKE '湘%' AND A.hpzl != '99' AND EXISTS "
				+ "(SELECT T.hphm, T.hpzl, T.zt, T.gxrq FROM jm_zyk_vehicle T INNER JOIN "
				+ "(SELECT hphm, hpzl, MAX(gxrq) AS gxsjj FROM jm_zyk_vehicle WHERE gxrq >= to_date('"
				+ filter.get("kssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss')"
				+ "AND gxrq < to_date('"
				+ filter.get("jssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss')"
				+ hphm + hpys + hpzl
				+ "GROUP BY hphm, hpzl) q ON "
				+ "T.hphm = q.hphm AND T.hpzl = q.hpzl AND T.gxrq = q.gxsjj "
				+ "WHERE (POSITION('M' IN zt) > 0 OR POSITION('E' IN zt) > 0 OR POSITION('B' IN zt)>0) "
				+ "AND A.hphm = T.hphm AND A.hpzl = T.hpzl)";
		String latestXyFalse = "SELECT *  FROM (SELECT xyFalse.*, RANK() OVER(PARTITION BY hphm, hpzl ORDER BY gcsj DESC)rn from ("+xyFalse+")xyFalse)r WHERE rn <= 5";
		String sql = "SELECT hphm,hpys,hpzl,COUNT(1) AS total FROM ("+latestXyFalse+") latestXyFalse " + end;

		Map map = this.findPageForMap(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	public Map queryXyAllFalseList(Map filter) throws Exception{
		String hphm ="";
		if(filter.get("hphm")!=null&&(String)filter.get("hphm")!=""){
			hphm=" and hphm='"+filter.get("hphm").toString()+"'";
		}
		String hpys="";
		if(filter.get("hpys")!=null&&(String)filter.get("hpys")!=""){
			hpys=" and hpys='"+filter.get("hpys")+"'";
		}
		String hpzl="";
		if(filter.get("hpzl")!=null&&(String)filter.get("hpzl")!=""){
			hpzl=" and hpzl='"+filter.get("hpzl").toString()+"'";
		}
		String sql = "SELECT z.* FROM veh_passrec z WHERE 1 = 1 "+hphm+hpys+hpzl + " order by gcsj";
		
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	@SuppressWarnings("unchecked")
	public void updateXyFalse(String newzt, String hphm, String hpzl) {
		System.out.println("天云星SCS");
		
	}

	
	public void insertFalse(String newzt, String hphm, String hpzl) {
		// TODO Auto-generated method stub
		
	}

	
	public boolean isFalsed(String hphm, String hpzl) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public List queryPass(Map filter) throws Exception {
		String gcxh = "";
		if(filter.get("gcxh").toString()!=null&&filter.get("gcxh").toString()!=""){
			gcxh = " and gcxh='"+filter.get("gcxh").toString()+"'";
		}
		String sql = "select gcxh,kdbh,cdbh,fxbh,hphm,hpzl,hpys,csys,gcsj,to_char(gcsj,'HH24') as gcsd,tp1,tp2,tp3 from veh_passrec where 1=1"+gcxh;
		sql+="  order by gcsj desc";
		List list=this.jdbcScsTemplate.queryForList(sql);
 		return list;
	}
	
	public Map queryOneIllegal(Map filter) throws Exception {
		String hphm = "";
		if(filter.get("hphm")!=null&&filter.get("hphm").toString()!=""){
			hphm = filter.get("hphm").toString();
		}
		String hpzl = "";
		if(filter.get("hpzl")!=null&&filter.get("hpzl").toString()!=""){
			hpzl = filter.get("hpzl").toString();
		}
		String sql = "SELECT * FROM jm_zyk_illegal WHERE hphm = '"+hphm+"' and hpzl = '"+hpzl+"' order by gxsj desc";
		//System.out.println("查询区域首次车辆违法信息sql："+sql);
		//Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		Map map=this.jdbcScsTemplate.queryForMap(sql);
		return map;
	}
	
	
}
