package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.analysis.bean.FalseLicense;
import com.sunshine.monitor.system.analysis.dao.FalseLicenseDao;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("falseDao")
public class FalseLicenseDaoImpl extends BaseDaoImpl implements FalseLicenseDao {

	public FalseLicenseDaoImpl() {
		this.setTableName("jm_fack_license");
	}


	/**
	 * query FalseLicense for Page
	 */
	public Map<String, Object> findPageForFL(Map<String, Object> filter,
			FalseLicense fl) throws Exception {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> map = null;
		sb.append(" select * from ").append(getTableName()).append(
				" where 1=1 ");
		if (fl.getHphm() != null && fl.getHphm().length() > 0) {
			sb.append(" and ").append(" hphm = '").append(fl.getHphm()).append(
					"'");
		}
		if (fl.getHpzl() != null && fl.getHpzl().length() > 0) {
			sb.append(" and ").append(" hpzl = '").append(fl.getHpzl()).append(
					"'");
		}
		map = this.findPageForMap(sb.toString(), Integer.parseInt(filter.get(
				"curPage").toString()), Integer.parseInt(filter.get("pageSize")
				.toString()),FalseLicense.class);
		return map;
	}

	/**
	 * delete FalseLicense from jm_fack_license
	 */
	public void delFalseLicense(List<FalseLicense> list) throws Exception {
		String[] sqls = new String[list.size()];
		int i = 0;
		for (FalseLicense fl : list) {
			StringBuffer sb = new StringBuffer();
			sb.append(" delete from ").append(getTableName()).append(
					" where hphm = '").append(fl.getHphm()).append(
					"' and hpzl = ").append(fl.getHpzl());
			sqls[i] = sb.toString();
			i++;
		}
		this.jdbcTemplate.batchUpdate(sqls);
	}
	
	public Map getAllHphm(Map<String,Object> filter) throws Exception {
		String sql =" SELECT hphm,hpzl FROM veh_passrec where " 
				+" hphm <>'无牌' " 
				//+" and gcsj between to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss') "
			   // +" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
				+" GROUP BY hphm,hpzl ";
		
		return this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
	}

	public List getTrajectoryByHphm(Map<String, Object> filter) throws Exception {
		JdbcTemplate jdbcScsTemplate =  (JdbcTemplate) SpringApplicationContext.getStaticApplicationContext().getBean("jdbcScsTemplate");
		String sql =" select t.gcxh,t.hphm,t.kdbh,substring(t.kdbh,1,6) xzqh,t.fxbh,t.gcsj,t.hpys,t.tp1,t.tp2,t.tp3,t.hpzl from t(veh_passrec::"
				+" t.hphm='"+filter.get("hphm").toString()+"' "
				+" and t.gcsj between str_to_date('"+filter.get("kssj").toString()+"','%Y-%m-%d %H:%i:%s') "
			    +" and str_to_date('"+filter.get("jssj").toString()+"','%Y-%m-%d %H:%i:%s')"
				+		")"
		;
		String sql2 ="select gcxh,hphm,kdbh,substr(kdbh,0,6) xzqh,fxbh,to_char(gcsj,'yyyy-mm-dd hh24:mi:ss') gcsj,hpys,tp1,tp2,tp3,hpzl  from veh_passrec "
	       +" where hphm = '"+filter.get("hphm").toString()+"'"
	       +" and gcsj between to_date('2014-05-05 17:50:00','yyyy-mm-dd hh24:mi:ss') "
		   +" and to_date('2014-05-05 17:52:59','yyyy-mm-dd hh24:mi:ss')"
	      // +" and gcsj between to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss') "
		  // +" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
	       ;
		List list=jdbcScsTemplate.queryForList(sql);
		return list;
	}

	public int writeTrajectory(final List list) throws Exception {
		int count=0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time=sdf.format(new Date());
		try{
		for(int i =0;i<list.size();i++){
		/*String sql="insert into jm_false_license (GCXH,HPHM,KDBH,FXBH,TP1,TP2,TP3,GCSJ,HPYS,HPZL,XZQH,TJSJ) values ("
			+"?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss')"
			+")";*/
		Map map = (Map)list.get(i);
		StringBuffer sb=new StringBuffer();
				sb.append("insert into jm_false_license (GCXH,HPHM,KDBH,FXBH,TP1,TP2,TP3,GCSJ,HPYS,HPZL,XZQH,TJSJ) values (");
				sb.append("'");
				sb.append(map.get("GCXH") == null ? "" : map.get("GCXH")
						.toString());
				sb.append("','");
				sb.append(map.get("HPHM") == null ? "" : map.get("HPHM")
						.toString());
				sb.append("','");
				sb.append(map.get("KDBH") == null ? "" : map.get("KDBH")
						.toString());
				sb.append("','");
				sb.append(map.get("FXBH") == null ? "" : map.get("FXBH")
						.toString());
				sb.append("','");
				sb.append(map.get("TP1") == null ? "" : map.get("TP1")
						.toString());
				sb.append("','");
				sb.append(map.get("TP2") == null ? "" : map.get("TP2")
						.toString());
				sb.append("','");
				sb.append(map.get("TP3") == null ? "" : map.get("TP3")
						.toString());
				sb.append("',to_date('");
				sb.append(map.get("GCSJ") == null ? "" : map.get("GCSJ")
						.toString());
				sb.append("','yyyy-mm-dd hh24:mi:ss'),'");
				sb.append(map.get("HPYS") == null ? "" : map.get("HPYS")
						.toString());
				sb.append("','");
				sb.append(map.get("HPZL") == null ? "" : map.get("HPZL")
						.toString());
				sb.append("','");
				sb.append(map.get("XZQH") == null ? "" : map.get("XZQH")
						.toString());
				sb.append("',to_date('");
				sb.append(time);
				sb.append("','yyyy-mm-dd hh24:mi:ss')" + ")");
		int a= this.jdbcTemplate.update(sb.toString());
		if(a>0){
			count++;
		}
		}
		
	
			/*	BatchPreparedStatementSetter  bpss	=new 
			BatchPreparedStatementSetter()  {						
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							Map map=(Map)list.get(i);
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							String time=sdf.format(new Date());
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
						}					
						public int getBatchSize() {
							return list.size();
						}
					
					}	;

		int a[]=	this.jdbcTemplate.batchUpdate(sql,bpss);*/
					
		return count;
		}
		catch(org.springframework.dao.DuplicateKeyException ee){
			System.out.println("插入失败:假牌库已存在相同的过车轨迹");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return 0 ;
	}

	public boolean isExistHphm(Map<String, Object> filter) throws Exception {
		String sql="SELECT * FROM frm_false_table " 
			+"WHERE hphm ='"+filter.get("hphm").toString()+"'"
			;
		return this.jdbcTemplate.queryForList(sql).size()!=0?true:false;
	}

	public int writeFalseTable(final Map<String, Object> filter) throws Exception {
		String sql="insert into frm_false_table (HPHM,HPZL,ZT) values ("
			+"'"+filter.get("hphm").toString()+"','"+filter.get("hpzl")+"','0'"
			+")";
		int i=this.jdbcTemplate.update(sql);
			

	return i;
	}

	public Map getFalseCount(Map<String, Object> filter) throws Exception {
		String kssj="";
		if(filter.get("kssj")!=null){
			if(filter.get("kssj").toString()==null||"".equals(filter.get("kssj").toString())){
				kssj="2014-01-01 00:00:00";
			}
			else
				kssj=filter.get("kssj").toString();	
			
		}
		
		/*String sql2 = ""
			+" SELECT to_char(T1.TJ,'yyyy-mm-dd') tj,                                                                 "
			+"       NVL(T1.TOTAL, 0) TOTAL,                                                 "
			+"       NVL(T12.XYTOTAL, 0) XYTOTAL,                                            "
			+"       NVL(T13.ISTOTAL, 0) ISTOTAL,                                            "
			+"       NVL(T14.NOTOTAL, 0) NOTOTAL,                                            "
			+"       NVL(T15.UNKNOWTOTAL, 0) UNKNOWTOTAL,                                    "
			+"       NVL(T2.LPS, 0) LPS,                                                     "
			+"       NVL(T3.XYLPS, 0) XYLPS,                                                 "
			+"       NVL(T4.ISLPS, 0) ISLPS,                                                 "
			+"       NVL(T5.NOLPS, 0) NOLPS,                                                 "
			+"       NVL(T6.UNKNOWLPS, 0) UNKNOWLPS,                                         "
			+"       NVL(T7.ZPS, 0) ZPS,                                                     "
			+"       NVL(T8.XYZPS, 0) XYZPS,                                                 "
			+"       NVL(T9.ISZPS, 0) ISZPS,                                                 "
			+"       NVL(T10.NOZPS, 0) NOZPS,                                                "
			+"       NVL(T11.UNKNOWZPS, 0) UNKNOWZPS                                         "
			+"  FROM (SELECT COUNT(DISTINCT HPHM) AS TOTAL, TRUNC(TJSJ) AS TJ                "
			+"          FROM JM_FALSE_LICENSE    WHERE                                            "
			//+"          TJSJ BETWEEN                                                    "
			//+"               TO_DATE('2014-08-04 00:00:00', 'yyyy-mm-dd hh24:mi:ss') AND     "
			//+"               TO_DATE('2014-08-05 23:59:59', 'yyyy-mm-dd hh24:mi:ss')         "
			+" TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"         GROUP BY TRUNC(TJSJ)) T1                                              "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS XYTOTAL     "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN (SELECT HPHM FROM FRM_FALSE_TABLE)               "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T12 ON T1.TJ = T12.TJ                    "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS ISTOTAL     "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN                                                  "
			+"                    (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '1')          "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T13 ON T1.TJ = T13.TJ                    "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS NOTOTAL     "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN                                                  "
			+"                    (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '2')          "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T14 ON T1.TJ = T14.TJ                    "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ,                                       "
			+"                    COUNT(DISTINCT J.HPHM) AS UNKNOWTOTAL                      "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN                                                  "
			+"                    (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '0')          "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T15 ON T1.TJ = T15.TJ                    "
			+"  LEFT JOIN (SELECT COUNT(DISTINCT HPHM) AS LPS, TRUNC(TJSJ) AS TJ             "
			+"               FROM JM_FALSE_LICENSE                                           "
			+"              WHERE HPYS = '2'                                                 "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			//+"                AND TJSJ BETWEEN                                               "
			//+"                    TO_DATE('2014-08-04 00:00:00', 'yyyy-mm-dd hh24:mi:ss') AND"
			//+"                    TO_DATE('2014-08-05 23:59:59', 'yyyy-mm-dd hh24:mi:ss')    "
			+"              GROUP BY TRUNC(TJSJ)) T2 ON T1.TJ = T2.TJ                        "
			+"                                                                               "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS XYLPS       "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN (SELECT HPHM FROM FRM_FALSE_TABLE)               "
			+"                AND HPYS = '2'                                                 "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T3 ON T1.TJ = T3.TJ                      "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS ISLPS       "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN                                                  "
			+"                    (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '1')          "
			+"                AND HPYS = '2'                                                 "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T4 ON T1.TJ = T4.TJ                      "
			+"                                                                               "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS NOLPS       "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN                                                  "
			+"                    (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '2')          "
			+"                AND HPYS = '2'                                                 "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T5 ON T1.TJ = T5.TJ                      "
			+"                                                                               "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ,                                       "
			+"                    COUNT(DISTINCT J.HPHM) AS UNKNOWLPS                        "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN                                                  "
			+"                    (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '0')          "
			+"                AND HPYS = '2'                                                 "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T6 ON T1.TJ = T6.TJ                      "
			+"                                                                               "
			+"  LEFT JOIN (SELECT COUNT(DISTINCT HPHM) AS ZPS, TRUNC(TJSJ) AS TJ             "
			+"               FROM JM_FALSE_LICENSE                                           "
			+"              WHERE HPYS <> '2'                                                "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			//+"                AND TJSJ BETWEEN                                               "
			//+"                    TO_DATE('2014-08-04 00:00:00', 'yyyy-mm-dd hh24:mi:ss') AND"
			//+"                    TO_DATE('2014-08-05 23:59:59', 'yyyy-mm-dd hh24:mi:ss')    "
			+"              GROUP BY TRUNC(TJSJ)) T7 ON T1.TJ = T7.TJ                        "
			+"                                                                               "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS XYZPS       "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN (SELECT HPHM FROM FRM_FALSE_TABLE)               "
			+"                AND HPYS <> '2'                                                "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T8 ON T1.TJ = T8.TJ                      "
			+"                                                                               "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS ISZPS       "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN                                                  "
			+"                    (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '1')          "
			+"                AND HPYS <> '2'                                                "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"              GROUP BY TRUNC(J.TJSJ)) T9 ON T1.TJ = T9.TJ                      "
			+"                                                                               "
			+"  LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ, COUNT(DISTINCT J.HPHM) AS NOZPS       "
			+"               FROM JM_FALSE_LICENSE J                                         "
			+"              WHERE J.HPHM IN                                                  "
			+"                    (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '2')          "
			+"                AND HPYS <> '2'                                                "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"                GROUP BY TRUNC(J.TJSJ)) T10 ON T1.TJ = T10.TJ                  "
			+"                                                                               "
			+"    LEFT JOIN (SELECT TRUNC(J.TJSJ) AS TJ,                                     "
			+"                      COUNT(DISTINCT J.HPHM) AS UNKNOWZPS                      "
			+"                 FROM JM_FALSE_LICENSE J                                       "
			+"                WHERE J.HPHM IN                                                "
			+"                      (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '0')        "
			+"                  AND HPYS <> '2'                                              "
			+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
			+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
			+"                GROUP BY TRUNC(J.TJSJ)) T11 ON T1.TJ = T11.TJ                  "
            +" order by tj desc "
			;*/
		
		String sql = ""		
+"		SELECT to_char(T1.TJ, 'yyyy-mm-dd') tj,   "
+"	       NVL(T1.TOTAL, 0) TOTAL,"
+"	       NVL(T12.XYTOTAL, 0) XYTOTAL,"
+"	       NVL(T13.ISTOTAL, 0) ISTOTAL,"
+"	       NVL(T14.NOTOTAL, 0) NOTOTAL,"
+"	       NVL(T15.UNKNOWTOTAL, 0) UNKNOWTOTAL,"
+"	       NVL(T2.LPS, 0) LPS,"
+"	       NVL(T3.XYLPS, 0) XYLPS,"
+"	       NVL(T4.ISLPS, 0) ISLPS,"
+"	       NVL(T5.NOLPS, 0) NOLPS,"
+"	       NVL(T6.UNKNOWLPS, 0) UNKNOWLPS,"
+"	       NVL(T7.ZPS, 0) ZPS,"
+"	       NVL(T8.XYZPS, 0) XYZPS,"
+"	       NVL(T9.ISZPS, 0) ISZPS,"
+"	       NVL(T10.NOZPS, 0) NOZPS,"
+"	       NVL(T11.UNKNOWZPS, 0) UNKNOWZPS"
+"	  FROM (select tj, count(1) as total"
+"	          from (select hphm, hpys, TRUNC(TJSJ) AS TJ"
+"	                  FROM JM_FALSE_LICENSE"
+"	                 WHERE " 
+" TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                 GROUP BY TRUNC(TJSJ), hpys, hphm)"
+"	         group by tj) T1"
+"	  LEFT JOIN (select tj, count(1) as xytotal"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN (SELECT HPHM FROM FRM_FALSE_TABLE)"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T12"
+"	    ON T1.TJ = T12.TJ"
+"	  LEFT JOIN (select tj, count(1) as istotal"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '1')"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T13"
+"	    ON T1.TJ = T13.TJ"
+"	  LEFT JOIN (select tj, count(1) as nototal"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '2')"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T14"
+"	    ON T1.TJ = T14.TJ"
+"	  LEFT JOIN (select tj, count(1) as unknowtotal"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, hphm, hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '0')"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T15"
+"	    ON T1.TJ = T15.TJ"
+"	  LEFT JOIN (select tj, count(1) as lps"
+"	               from (select hphm, hpys, TRUNC(TJSJ) AS TJ"
+"	                       FROM JM_FALSE_LICENSE"
+"	                      WHERE HPYS = '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(TJSJ), hpys, hphm)"
+"	              group by tj) T2"
+"	    ON T1.TJ = T2.TJ"
+"	  LEFT JOIN (select tj, count(1) as xylps"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN (SELECT HPHM FROM FRM_FALSE_TABLE)"
+"	                        AND HPYS = '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T3"
+"	    ON T1.TJ = T3.TJ"
+"	  LEFT JOIN (select tj, count(1) as islps"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '1')"
+"	                        AND HPYS = '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T4"
+"	    ON T1.TJ = T4.TJ"
+"	  LEFT JOIN (select tj, count(1) as nolps"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '2')"
+"	                        AND HPYS = '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T5"
+"	    ON T1.TJ = T5.TJ"
+"	  LEFT JOIN (select tj, count(1) as unknowlps"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hphm"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '0')"
+"	                        AND HPYS = '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T6"
+"	    ON T1.TJ = T6.TJ"
+"	  LEFT JOIN (select tj, count(1) as zps"
+"	               from (SELECT hphm, hpys, TRUNC(TJSJ) AS TJ"
+"	                       FROM JM_FALSE_LICENSE"
+"	                      WHERE HPYS <> '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(TJSJ), hpys, hphm)"
+"	              group by tj) T7"
+"	    ON T1.TJ = T7.TJ"
+"	  LEFT JOIN (select tj, count(1) as xyzps"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN (SELECT HPHM FROM FRM_FALSE_TABLE)"
+"	                        AND HPYS <> '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T8"
+"	    ON T1.TJ = T8.TJ"
+"	  LEFT JOIN (select tj, count(1) as iszps"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '1')"
+"	                        AND HPYS <> '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T9"
+"	    ON T1.TJ = T9.TJ"
+"	  LEFT JOIN (select tj, count(1) as nozps"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '2')"
+"	                        AND HPYS <> '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T10"
+"	    ON T1.TJ = T10.TJ"
+"	  LEFT JOIN (select tj, count(1) as unknowzps"
+"	               from (SELECT TRUNC(J.TJSJ) AS TJ, j.hphm, j.hpys"
+"	                       FROM JM_FALSE_LICENSE J"
+"	                      WHERE J.HPHM IN"
+"	                            (SELECT HPHM FROM FRM_FALSE_TABLE WHERE ZT = '0')"
+"	                        AND HPYS <> '2'"
+" and TJSJ between to_date('"+kssj+"','yyyy-mm-dd hh24:mi:ss') "
+" and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
+"	                      GROUP BY TRUNC(J.TJSJ), hpys, hphm)"
+"	              group by tj) T11"
+"	    ON T1.TJ = T11.TJ"
+"	 order by tj desc"

		;
		
		
		
		System.out.println(sql);
		Map map =this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()),Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	public Map getSuspectedListBysign(Map<String, Object> filter)
			throws Exception {
		String tjsj="";
		String zt="";
		String hpys="";
		if(filter.get("tjsj")!=null){
			tjsj="and trunc(TJSJ)=trunc(to_date('"+filter.get("tjsj").toString()+"','yyyy-mm-dd'))";
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内的所有未确认号牌
			if(filter.get("sign").toString().equals("UNKNOWTOTAL")){
				zt=" where zt='0'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内的所有非假牌
			if(filter.get("sign").toString().equals("NOTOTAL")){
				zt=" where zt='2'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内的所有假牌
			if(filter.get("sign").toString().equals("ISTOTAL")){
				zt=" where zt='1'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内未确认的杂牌
			if(filter.get("sign").toString().equals("XYZPS")){
			  hpys=" and hpys<>'2'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内未确认的杂牌
			if(filter.get("sign").toString().equals("UNKNOWZPS")){
				zt=" where zt='0'";
			  hpys=" and hpys<>'2'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内非假牌的杂牌
			if(filter.get("sign").toString().equals("NOZPS")){
				zt=" where zt='2'";
			  hpys=" and hpys<>'2'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内假牌的杂牌
			if(filter.get("sign").toString().equals("ISZPS")){
				zt=" where zt='1'";
			  hpys=" and hpys<>'2'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内未确认的杂牌
			if(filter.get("sign").toString().equals("XYLPS")){
			  hpys=" and hpys='2'";
			}
		}
		
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内未确认的蓝牌
			if(filter.get("sign").toString().equals("UNKNOWLPS")){
				zt=" where zt='0'";
			  hpys=" and hpys='2'";
			}
		}
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内非假牌的蓝牌
			if(filter.get("sign").toString().equals("NOLPS")){
				zt=" where zt='2'";
			  hpys=" and hpys='2'";
			}
		}
		if(filter.get("sign")!=null){
			//sign:XYTOTAL,列出指定时间内假牌的蓝牌
			if(filter.get("sign").toString().equals("ISLPS")){
				zt=" where zt='1'";
			  hpys=" and hpys='2'";
			}
		}
		
		
		
		String sql="SELECT J.HPHM,J.HPYS, J.QYS, J.KKS, J.TS, J.XYGJCS, S.ZT"
 +" FROM (SELECT DISTINCT HPHM"
 +"            ,HPYS,           "
 +"                       COUNT(DISTINCT(SUBSTR(KDBH, 1, 6))) OVER(PARTITION BY SUBSTR(KDBH, 1, 6), HPHM) AS QYS,"
 +"                       COUNT(DISTINCT(KDBH)) OVER(PARTITION BY KDBH, HPHM) AS KKS,"
 +"                       COUNT(DISTINCT(TRUNC(GCSJ))) OVER(PARTITION BY TRUNC(GCSJ), HPHM) AS TS,"
 +"                       COUNT(HPHM) OVER(PARTITION BY HPHM,HPYS) AS XYGJCS"
 +"         FROM JM_FALSE_LICENSE"
 +"       WHERE HPHM IN (SELECT HPHM FROM FRM_FALSE_TABLE"
 +   zt
 +"       )"
 +   tjsj
 +   hpys    
 +"       ) J"
 +" LEFT JOIN (SELECT * FROM FRM_FALSE_TABLE) S ON S.HPHM = J.HPHM ORDER BY XYGJCS desc";
		
	Map map=	this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
	return map;
	}

	public Map getFalseListByHphm(Map<String, Object> filter) throws Exception {
		String hphm ="";
		if(filter.get("hphm")!=null){
			hphm=" where hphm='"+filter.get("hphm").toString()+"'";
		}
		String hpys="";
		if(filter.get("hpys")!=null){
			hpys=" and hpys='"+filter.get("hpys").toString()+"'";
		}
		String tjsj="";
		if(filter.get("tjsj")!=null){
			tjsj="and trunc(TJSJ)=trunc(to_date('"+filter.get("tjsj").toString()+"','yyyy-mm-dd'))";
		}
		String  sql =" select tp1,gcxh,hphm,kdbh,substr(kdbh,0,6) xzqh,fxbh,to_char(gcsj,'yyyy-mm-dd hh24:mi:ss') gcsj,hpys "
			+"FROM jm_false_license"
		//	+" WHERE "
			+hphm
			+hpys
			+tjsj
			;
		Map map=	this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
			
		return map;
	}

	public String sureFalse(String hphm, String flag,String hpzl) throws Exception {
		String sql ="update  frm_false_table set zt='"+flag+"'  where hphm='"+hphm+"' and hpzl ='"+hpzl+"'" ;
		int i=this.jdbcTemplate.update(sql);
		if(i>0){
			return "修改成功";
		}
		return null;
	}


	public Map queryFalseList(Map filter, VehPassrec veh) throws Exception {
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
		
		String sql = "SELECT hphm,hpys,hpzl,csys,rksj,czr,xzqh,xxly FROM jm_zyk_false WHERE rksj >= to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"+
		"AND rksj < to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"+hphm+hpys+hpzl;
		
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	private String getSql(Map filter, VehPassrec veh){
		String hpzl = "";
		if (veh.getHpzl() != null && !"".equals(veh.getHpzl())) {
			if (!veh.getHpzl().equals(""))
				hpzl = " and hpzl='" + veh.getHpzl() + "'";
		}
		String hphm = "";
		if (veh.getHphm() != null && !"".equals(veh.getHphm())) {
			if (!veh.getHphm().equals(""))
				hphm = " and hphm='" + veh.getHphm() + "'";
		}
		String hpys="";
		if (veh.getHpys() != null && !"".equals(veh.getHpys())) {
			if (!veh.getHpys().equals(""))
				hpys = " and hpys='" + veh.getHpys() + "'";
		}
		String kssj="";
		if (veh.getKssj() != null && !"".equals(veh.getKssj())) {
			if (!veh.getKssj().equals(""))
				kssj = " AND rksj >= to_date('" + veh.getKssj() + "','yyyy-mm-dd hh24:mi:ss')";
		}
		String jssj="";
		if (veh.getJssj() != null && !"".equals(veh.getJssj())) {
			if (!veh.getJssj().equals(""))
				jssj = " AND rksj < to_date('" + veh.getJssj() + "','yyyy-mm-dd hh24:mi:ss')";
		}
		String zt="";
		if(filter.get("zt")!=null&&(String)filter.get("zt")!=""){
			zt=" and zt='"+filter.get("zt").toString()+"'";
		}
		String jycl = " and hpzl != '23' and hpzl != '24'"; // 23警用汽车，24警用摩托
		
//		String sql = "SELECT trunc((rksj-gcsj),2) as sjc,gcxh,kkbh,hphm,hpys,hpzl,zt,csys,rksj,czr,czsj,jdcsyr,xzqh,xxly FROM jm_zyk_xy_false WHERE 1=1 "+kssj+jssj+hphm+hpys+hpzl+zt+" order by rksj desc";
		String sql = "SELECT hphm,hpzl,hpys,count(1) total FROM jm_zyk_xy_false WHERE 1=1 "+kssj+jssj+hphm+hpys+hpzl+zt+jycl;
			   sql += " group by hphm,hpzl,hpys ";
			   sql += " order by total desc";
		return sql;
	}
	
	public List<Map<String, Object>> queryXyFalseListExt(Map filter, VehPassrec veh) throws Exception {
		String sql = getSql(filter, veh);
		List<Map<String, Object>> list = super.getPageDatas(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	public Integer queryXyFalseListTotal(Map filter, VehPassrec veh) throws Exception {
		String sql = getSql(filter, veh);
		Integer total = super.getTotal(sql);
		return total;
	}
	
	public Map queryXyFalseList(Map filter) throws Exception{
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
		String zt="";
		if(filter.get("zt")!=null&&(String)filter.get("zt")!=""){
			zt=" and zt='"+filter.get("zt").toString()+"'";
		}
		
		String sql = "SELECT trunc((rksj-gcsj),2) as sjc,gcxh,kkbh,hphm,hpys,hpzl,zt,csys,rksj,czr,czsj,jdcsyr,xzqh,xxly FROM jm_zyk_xy_false WHERE rksj >= to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"+
		"AND rksj < to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"+hphm+hpys+hpzl+zt;
		
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	public Map getSuitList(Map filter, String hphm,String hpzl,String sj,String zt){
		
		return null;
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
		String zt="";
		if(filter.get("zt")!=null&&(String)filter.get("zt")!=""){
			zt=" and zt='"+filter.get("zt").toString()+"'";
		}
		String sql = "SELECT z.*,c.kkmc FROM jm_zyk_xy_false z,jm_code_gate c WHERE z.kkbh=c.kkbh and gcsj >= to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"+
		"AND gcsj < to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"+zt+hphm+hpys+hpzl;
		
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}


	
	public void updateXyFalse(String newzt, String hphm, String hpzl,String hpys,SysUser user) {
		String sql = "update jm_zyk_xy_false set zt = '" + newzt + "',czr = '"+user.getYhmc().toString()+"' ,czsj = sysdate where 1=1";
		if(hphm!= null && !"".equals(hphm)) {
			sql += " and hphm = '" + hphm + "' ";
		}
		if(hpzl!= null && !"".equals(hpzl)) {
			sql += " and hpzl = '" + hpzl +"' ";
		}
		if(hpys!= null && !"".equals(hpys)) {
			sql += " and hpys = '" + hpys +"' ";
		}
		log.info("假牌分析状态修改SQL："+sql);
		try {
			this.jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void insertFalse(VehPassrec vehPassrec) {
		String sql="INSERT INTO JM_ZYK_FALSE (hphm,hpzl,hpys,cllx,csys,clpp,clwx,tp1,tp2,tp3,bdlj,rksj,xxly,xzqh,czr,by1,by2,by3) VALUES ('";
		sql += vehPassrec.getHphm();
		sql += "','";
		sql += vehPassrec.getHpzl();
		sql += "','";
		sql += (vehPassrec.getHpys()==null||"".equals(vehPassrec.getHpys())?"":vehPassrec.getHpys());
		sql += "','";
		sql += (vehPassrec.getCllx()==null||"".equals(vehPassrec.getCllx())?"":vehPassrec.getCllx());
		sql += "','";
		sql += (vehPassrec.getCsys()==null||"".equals(vehPassrec.getCsys())?"":vehPassrec.getCsys());
		sql += "','";
		sql += (vehPassrec.getClpp()==null||"".equals(vehPassrec.getClpp())?"":vehPassrec.getClpp());
		sql += "','";
		sql += (vehPassrec.getClwx()==null||"".equals(vehPassrec.getClwx())?"":vehPassrec.getClwx());
		sql += "','";
		sql += (vehPassrec.getTp1()==null||"".equals(vehPassrec.getTp1())?"":vehPassrec.getTp1());
		sql += "','";
		sql += (vehPassrec.getTp2()==null||"".equals(vehPassrec.getTp2())?"":vehPassrec.getTp2());
		sql += "','";
		sql += (vehPassrec.getTp3()==null||"".equals(vehPassrec.getTp3())?"":vehPassrec.getTp3());
		sql += "','',"; // bdlj
		sql += " SYSDATE,"; // RKSJ
		sql += " '1', '"; // XXLY
		sql += vehPassrec.getKdbh().substring(0,4); // xzqh
		sql += "','','','','')"; // czr,by1,by2,by3
		
		//'' AS bdlj, SYSDATE AS RKSJ, '1' AS XXLY, T.XZQH, '' AS czr, '' AS by1, '' AS by2, '' AS by3 FROM JM_ZYK_XY_FALSE T WHERE 1=1";
		
		//sql += "AND ROWNUM <= 1";
		System.out.println("嫌疑分析结果为假牌插入假牌库："+sql);
		this.jdbcTemplate.execute(sql);
	}


	@Override
	public boolean isFalsed(String hphm, String hpzl) {
		int total = 0;
		String sql = "select count(*) as total from JM_ZYK_FALSE where 1=1";
		if(hphm!=null&&!"".equals(hphm)){
			sql += " and hphm = '"+hphm+"'";
		}
		if(hpzl!=null&&!"".equals(hpzl)){
			sql += " and hpzl = '"+hpzl+"'";
		}
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		if(result.size()>0){
			Map<String, Object> map = result.get(0);
			try {
				total = Integer.parseInt(map.get("TOTAL").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return total>0?true:false;
	}

	public void insertFalse(String newzt, String hphm, String hpzl,SysUser user){
		String sql = "INSERT INTO JM_ZYK_FALSE SELECT T.HPHM, T.HPZL, T.HPYS, T.CLLX, T.CSYS, T.CLPP, T.bdlj,t.clwx,t.tp1,t.tp2,t.tp3, SYSDATE AS RKSJ, '1' AS XXLY, T.XZQH, '"+user.getYhmc().toString()+"' AS czr, t.by1, t.by2, t.by3 FROM JM_ZYK_XY_FALSE T WHERE 1=1";
		if(hphm!=null && !"".equals(hphm)){
			sql += " and hphm = '"+hphm+"'";
		}
		if(hpzl!=null && !"".equals(hpzl)){
			sql += " and hpzl = '"+hpzl+"'";
		}
		if(newzt!=null && !"".equals(newzt)){
			sql += " and zt = '"+newzt+"'";
		}
		sql += "AND ROWNUM <= 1";
		//System.out.println("嫌疑分析结果为假牌插入假牌库："+sql);
		String msg="";
		try {
			this.jdbcTemplate.execute(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
			msg="插入假牌库失败";
		}
	}
	
	public List<Map<String,Object>> queryKksbl(Map<String,Object> filter) throws Exception {
		String kdbh = "";
		if(filter.get("kdbh")!=null&&filter.get("kdbh").toString()!=""){
			kdbh = " and CSTR_COL01 = '"+filter.get("kdbh").toString()+"'";
		}
		String sql = "SELECT SUM(NVL(CSTR_COL03, 0)) GCSL,SUM(NVL(CSTR_COL04, 0)) SBSL,sum(nvl(CSTR_COL05, 0)) BTSBSL," +
				" sum(nvl(CSTR_COL07, 0)) BTSBZS,sum(nvl(CSTR_COL06, 0)) YWSBSL,sum(nvl(CSTR_COL08, 0)) YWSBZS" +
				" FROM monitor.T_MONITOR_JG_INTEGRATE_DETAIL  WHERE KHFA_ID = '35' and DATE_COL01>=sysdate-7 and DATE_COL01<sysdate "+kdbh;
		//String sql = "SELECT SUM(NVL(NUM_COL01, 0)) GCSL, SUM(NVL(NUM_COL02, 0)) SBSL,SUM(NVL(NUM_COL01, 0)) BTSBZS, SUM(NVL(NUM_COL02, 0)) BTSBSL,SUM(NVL(NUM_COL01, 0)) YWSBZS, SUM(NVL(NUM_COL02, 0)) YWSBSL FROM MONITOR.T_MONITOR_JG_INTEGRATE_DETAIL WHERE KHFA_ID = '43'";
		//Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}


	@Override
	public List<Map<String, Object>> queryXyFalseListExt(Map<String, Object> filter, String sql) throws Exception {
		List<Map<String, Object>> list = super.getPageDatas(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	@Override
	public Integer queryXyFalseListTotal(Map<String, Object> filter, String sql) throws Exception {
		Integer total = super.getTotal(sql);
		return total;
	}
}
