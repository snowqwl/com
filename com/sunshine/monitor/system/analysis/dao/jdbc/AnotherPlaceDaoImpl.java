package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.Map;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ibm.icu.text.DecimalFormat;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.dao.AnotherPlaceDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("anotherPlaceDao")
public class AnotherPlaceDaoImpl extends BaseDaoImpl implements AnotherPlaceDao {

	public Map<String, Object> queryForAnotherPlaceList(VehPassrec veh,
			Map<String, Object> filter, String local) throws Exception {
		String hpys="";
		if(veh.getHpys()!=null&&!veh.getHpys().equals("")){
			hpys=" and hpys='"+veh.getHpys()+"'";
		}
		String csys="";
		if(veh.getCsys()!=null&&!veh.getCsys().equals("")){
			csys=" and csys='"+veh.getCsys()+"'";
		}
		String hpzl="";
		if(veh.getHpzl()!=null&&!veh.getHpzl().equals("")){
			hpzl=" and hpzl='"+veh.getHpzl()+"'";
		}
		
		String sqlhphm="select HPHM from frm_hphm " 
				+" where " 
				+"dwdm like '"+local+"%'"
				;
		List list=this.jdbcTemplate.queryForList(sqlhphm);
		String hp="";
		for(int i=0;i<list.size();i++){
			Map map=(Map) list.get(i);
			hp+="'"+map.get("HPHM")+"',";
		}
		if(hp.length()!=0){
			hp=hp.substring(0, hp.length()-1);
		}
		String sql ="select DISTINCT HPHM ,HPYS , "
			+" COUNT(DISTINCT(SUBSTR(KDBH, 1, 6))) OVER(PARTITION BY SUBSTR(KDBH, 1, 6), HPHM) AS XZQS,"
			+" COUNT(DISTINCT(KDBH)) OVER(PARTITION BY KDBH, HPHM) AS KKS,"
			+" COUNT(DISTINCT(TRUNC(GCSJ))) OVER(PARTITION BY TRUNC(GCSJ), HPHM) AS TS,"
			+" COUNT(HPHM) OVER(PARTITION BY HPHM) AS GCCS"
			+" from veh_passrec"
			+" where" 
			+" gcsj between to_date('"+veh.kssj+"','yyyy-mm-dd hh24:mi:ss') "
		    +" and to_date('"+veh.jssj+"','yyyy-mm-dd hh24:mi:ss')"
		    +" and kdbh in ("+veh.getKdbh()+")"
		    +hpys
		    +csys
		    +hpzl
		    +" and KDBH not in ("+hp+")"
		    +" ORDER BY XZQS DESC, KKS DESC, TS DESC, GCCS DESC";
		Map<String,Object> map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	public Map<String, Object> getList(Map<String, Object> filter)
			throws Exception {
		//得到本地行政区划
		String local ="";
		if(filter.get("local")!=null){
			local=filter.get("local").toString().substring(0,4);
		}
		//得到注册地
		String zcd="''";
		if(filter.get("zcd")!=null){
			zcd=filter.get("zcd").toString();
		}
		//本地型
		//String bex="";
		String sql = ""
				+ " SELECT nvl(t1.hp,0) AS hp                                                                "
				+ "   ,nvl(t1.c,0) AS total                                                                  "
				+ "   ,nvl(t2.c,0) AS bdx                                                                    "
				+ "   ,nvl(t3.c,0) AS hnx                                                                    "
				+ "   ,nvl(t4.c,0) AS gjx                                                                    "
				+ "   FROM                                                                                   "
				+ "  (                                                                                       "
				+ "  SELECT DISTINCT (substr(hphm,0,2)) hp,COUNT(hphm) c                                     "
				+ "  FROM VEH_PASSREC                                                                        "
				+ " WHERE KDBH like ('"
				+ local
				+ "%')                       "
				// +"   AND GCSJ BETWEEN TO_DATE('2014-04-01', 'yyyy-mm-dd hh24:mi:ss') AND                    "
				// +"       TO_DATE('2014-06-01', 'yyyy-mm-dd hh24:mi:ss')                                     "
				+ " and gcsj between to_date('"
				+ filter.get("kssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss') "
				+ " and to_date('"
				+ filter.get("jssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss')"
				+ "   AND substr(hphm,0,2) NOT IN (SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"
				+ local
				+ "%' )       "
				+ "   AND substr(hphm,0,1) IN ("
				+ zcd
				+ ")                                                    "
				+ "   GROUP BY substr(hphm,0,2)                                                              "
				+ "   ) t1                                                                                   "
				+ "  LEFT                                                                                    "
				+ "   JOIN                                                                                   "
				+ "   (                                                                                      "
				//+ "   --本地型                                                                               "
				+ "   SELECT DISTINCT (substr(hphm,0,2)) hp ,COUNT(hphm) c                                   "
				+ "  FROM VEH_PASSREC                                                                        "
				+ " WHERE KDBH like ('"
				+ local
				+ "%')                       "
				// +"   AND GCSJ BETWEEN TO_DATE('2014-04-01', 'yyyy-mm-dd hh24:mi:ss') AND                    "
				// +"       TO_DATE('2014-06-01', 'yyyy-mm-dd hh24:mi:ss')                                     "
				+ " and gcsj between to_date('"
				+ filter.get("kssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss') "
				+ " and to_date('"
				+ filter.get("jssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss')"
				+ "   AND substr(hphm,0,2) NOT IN (SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"
				+ local
				+ "%' )       "
				+ "   AND substr(hphm,0,1) IN ("
				+ zcd
				+ ")                                                    "
				+ "   GROUP BY substr(hphm,0,2)                                                              "
				+ "   HAVING COUNT(hphm) >3800                                                               "
				+ "  ) t2                                                                                    "
				+ "  ON  t1.hp=t2.hp                                                                         "
				+ "  LEFT                                                                                    "
				+ "   JOIN                                                                                   "
				+ "   (                                                                                      "
				//+ "   --候鸟型                                                                               "
				+ "   SELECT DISTINCT (substr(hphm,0,2)) hp ,COUNT(hphm) c                                   "
				+ "  FROM VEH_PASSREC                                                                        "
				+ " WHERE KDBH like ('"
				+ local
				+ "%')                       "
				// +"   AND GCSJ BETWEEN TO_DATE('2014-04-01', 'yyyy-mm-dd hh24:mi:ss') AND                    "
				// +"       TO_DATE('2014-06-01', 'yyyy-mm-dd hh24:mi:ss')                                     "
				+ " and gcsj between to_date('"
				+ filter.get("kssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss') "
				+ " and to_date('"
				+ filter.get("jssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss')"
				+ "   AND substr(hphm,0,2) NOT IN (SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"
				+ local
				+ "%' )       "
				+ "   AND substr(hphm,0,1) IN ("
				+ zcd
				+ ")                                                    "
				+ "   GROUP BY substr(hphm,0,2)                                                              "
				+ "   HAVING COUNT(hphm) >1800   and  COUNT(hphm) <=3800                                                           "
				+ "  ) t3                                                                                    "
				+ "  ON  t1.hp=t3.hp                                                                         "
				+ "    LEFT                                                                                  "
				+ "   JOIN                                                                                   "
				+ "   (                                                                                      "
				//+ "   --过境型                                                                               "
				+ "   SELECT DISTINCT (substr(hphm,0,2)) hp ,COUNT(hphm) c                                   "
				+ "  FROM VEH_PASSREC                                                                        "
				+ " WHERE KDBH like ('"
				+ local
				+ "%')                       "
				// +"   AND GCSJ BETWEEN TO_DATE('2014-04-01', 'yyyy-mm-dd hh24:mi:ss') AND                    "
				// +"       TO_DATE('2014-06-01', 'yyyy-mm-dd hh24:mi:ss')                                     "
				+ " and gcsj between to_date('"
				+ filter.get("kssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss') "
				+ " and to_date('"
				+ filter.get("jssj").toString()
				+ "','yyyy-mm-dd hh24:mi:ss')"
				+ "   AND substr(hphm,0,2) NOT IN (SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"
				+ local
				+ "%' )       "
				+ "   AND substr(hphm,0,1) IN ("
				+ zcd
				+ ")                                                    "
				+ "   GROUP BY substr(hphm,0,2)                                                              "
				+ "   HAVING COUNT(hphm) <=1800                                                              "
				+ "  ) t4                                                                                    "
				+ "  ON  t1.hp=t4.hp                                                                         "
				//+ "where rownum<=2 "
		;
		
		Map map =this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
			return map;
	}

	public String saveZcd(final String taskname,final Map<String, Object> filter) throws Exception {
		return null;/*
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		final String xh=sdf.format(new Date());
		String sql="insert into FRM_OTHERPLACE_ZCD (ZCD,GJXS,HNXS,BDXS,TOTAL,XH,TASKNAME,CONDISION,TI_KSSJ,TJ_JSSJ) values ("
			+"?,?,?,?,?,?,?,?,?,?"
			+")";
		final String condition= "开始时间:"+filter.get("kssj").toString()
				+"结束时间:"+filter.get("jssj").toString()
			    +"注册地:"+filter.get("zcd").toString()
				;
		System.out.println(condition);
		try{
			BatchPreparedStatementSetter  bpss	=new 
			BatchPreparedStatementSetter()  {						
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							Map map=(Map) list.get(i);
							String taskname_tmp=taskname;
							String xh_tmp=xh;
							String condition_tmp=condition;
							ps.setString(1,map.get("HP")==null?"":map.get("HP").toString());
							ps.setString(2,map.get("GJX")==null?"0":map.get("GJX").toString());
							ps.setString(3,map.get("HNX")==null?"0":map.get("HNX").toString());
							ps.setString(4,map.get("BDX")==null?"0":map.get("BDX").toString());
							ps.setString(5,map.get("TOTAL")==null?"0":map.get("TOTAL").toString());
							ps.setString(6,xh==null?"":xh_tmp);	
							ps.setString(7,xh==null?"":taskname_tmp);
							ps.setString(8,condition_tmp==null?"":condition_tmp);
							ps.setString(9,filter.get("kssj")==null?"":filter.get("kssj").toString());
							ps.setString(10,filter.get("jssj")==null?"":filter.get("jssj").toString());
						}					
						public int getBatchSize() {
							return list.size();
						}
					
					}	;

		int a[]=	this.jdbcTemplate.batchUpdate(sql,bpss);
					
		return xh;
		}
		catch(org.springframework.dao.DuplicateKeyException ee){
			System.out.println(ee.toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "";
	*/}

	public Map findtask(String xh,Map<String, Object> filter,String taskname) throws Exception {
	    String taskTime="";
			if(filter.get("kssj").toString().length()!=0&&filter.get("jssj").toString().length()!=0)
				taskTime=" WHERE to_date(XH,'yyyy-mm-dd hh24:mi:ss') between to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')";
		else
			taskTime="";
		if(taskname!=null&&taskname.length()!=0){
			taskname=" WHERE taskname like '%"+taskname+"'%";
		}
		
	/*	String sql2 =""
			+" SELECT xh,taskname,CONDISION,to_char(SUM(GJXS)) GJXS, to_char(SUM(HNXS)) HNXS, to_char(SUM(BDXS)) BDXS, to_char(SUM(TOTAL)) TOTAL "
            +" FROM FRM_OTHERPLACE_ZCD"
            +xh
            +taskname
            +" GROUP BY TASKNAME,CONDISION,xh"
            ;*/
		
		String sql=""
	+"		SELECT XH,"
	+"	       TASKNAME,"
	+"	       CONDISION,"
	+"	       TO_CHAR(SUM(GJXS)) GJXS,"
	+"	       TO_CHAR(SUM(HNXS)) HNXS,"
	+"	       TO_CHAR(SUM(BDXS)) BDXS,"
	+"	       TO_CHAR(SUM(TOTAL)) TOTAL,"
	+"	       ALL_TOTAL,"
	+"	       BJTOTAL,"
	+"	       DANGEROUSID,"
	+"         DANGEROUSMATCHCITY"
	+"	  FROM FRM_OTHERPLACE_ZCD"
	 +taskTime
     +taskname
	+"	 GROUP BY TASKNAME, CONDISION, XH,ALL_TOTAL,BJTOTAL,DANGEROUSID,DANGEROUSMATCHCITY order by XH desc"
	;	
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		List list=(List)map.get("rows");
	       for(int i=0;i<list.size();i++){
	    	   Map bfb=(Map)list.get(i);
	    	   double t1=(Double.parseDouble(bfb.get("TOTAL").toString()));
	    	   double t2=(Double.parseDouble(bfb.get("ALL_TOTAL").toString()));
	    	   DecimalFormat formatter = new DecimalFormat("##0.00000");
	    	   bfb.put("BFB",formatter.format((t1/t2)*100)+"%");
	       }
		
		return map;
	}

	public Map findZcdList(String xh,Map<String, Object> filter) throws Exception {
		String sql ="select * from frm_otherplace_zcd  "
			
		+		"WHERE xh = '"+xh+"'";
		
		Map map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));

		return map;
	}

	public Map queryDetilList(String xh ,Map<String ,Object> filter,String sign) throws Exception {return null;}

	public Map getListForpage(Map<Object, String> filter) throws Exception {
		return null;
	}

	public Map queryDetilList(Map filter) throws Exception {
		return null;
	}

	public Map queryForDetilList(Map filter) throws Exception {
		return null;
	}

	public String sureDeleteTask(Map filter) throws Exception {
		String xh="";
		if(filter.get("xh")!=null){
			xh=filter.get("xh").toString();
		}
		String sql =" delete from frm_otherplace_zcd where xh='"+xh+"'";
		int i=this.jdbcTemplate.update(sql);
		if(i>0){
			return "success";
		}
		return "";
	}

	public List cityTree() throws Exception {
		
		String sql=""
		+" select ID,PID,PROVINCE,CITY,NAME from frm_citytree   "
		;
		/*String sql =  ""
			+" SELECT T1.DMSM2 AS ID,                     "  
			+"        T2.DMZ   AS PID,                    "  
			+"        T2.DMSM1 AS PROVINCE,               "  
			+"        T1.DMSM3 AS CITY,                   "  
			+"        T1.DMZ   AS NAME                    "  
			+"   FROM (SELECT *                           "  
			+"           FROM FRM_CODE T                  "  
			+"          WHERE DMLB = '000033'             "  
			+"            AND DMZ LIKE '__0000') T2,      "  
			+"        (SELECT *                           "  
			+"           FROM FRM_CODE                    "  
			+"          WHERE DMLB = '000003'             "  
			+"            AND (DMSM3 NOT LIKE '总队%')) T1"  
			+"  WHERE T1.DMSM4 = T2.DMZ                   "  
			+"    AND T1.DMSM2 <> T2.DMZ                  "  
			+" UNION ALL (SELECT DMZ   AS ID,             "  
			+"                   DMZ   AS PID,            "  
			+"                   DMSM1 AS PROVINCE,       "  
			+"                   DMSM3 AS CITY,           "  
			+"                   DMSM4 AS NAME            "  
			+"              FROM FRM_CODE T               "  
			+"             WHERE DMLB = '000033'          "  
			+"               AND DMZ LIKE '__0000')       "  
			+"  ORDER BY NAME ASC                           "  
			;*/
		
		
		
		
		return this.jdbcTemplate.queryForList(sql);
	}

	public int insertDangerousArray(Map filter) throws Exception {
		String city="";
		if(filter.get("city")!=null){
			if(!filter.get("city").toString().equals(""))
			city=filter.get("city").toString();
		}
		String arrayname="";
		if(filter.get("arrayname")!=null){
			if(!filter.get("arrayname").toString().equals(""))
			arrayname=filter.get("arrayname").toString();
		}
		String uuid=UUID.randomUUID().toString();
		
			String sql =" insert into frm_dangerous (id,arrayname,updatetime,city) values ("
				+	"'"+uuid.substring(15,uuid.length()-1)+"',"
				+ "'"+arrayname+"',"
				+ "sysdate,"
				+ "'"+city+"'"	
				+" )"
				;
		return this.jdbcTemplate.update(sql);
	}


	//根据条件查询高危地区
	public Map queryDangerous(Map filter) throws Exception{
		String arrayname="";
		if(filter.get("arrayname")!=null){
			if(!filter.get("arrayname").toString().equals(""))
			arrayname=" and arrayname like '%"+filter.get("arrayname").toString()+"%'";
		}
		
		String arrayid="";
		if(filter.get("arrayid")!=null){
			if(!filter.get("arrayid").toString().equals(""))
			arrayname=" and id = '"+filter.get("arrayid").toString()+"'";
		}
		
		String sql =" select * from frm_dangerous where 1=1"
			+arrayname
			+arrayid
			;
		Map map=null;
		if(filter.get("stat")!=null){
		 map=this.findPageForMap(sql, 1,10);
		}
		else
		 map=this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	public int deleteById(Map filter) throws Exception {
		String sql =" delete from frm_dangerous where id = '"+filter.get("id").toString()+"'";
		return this.jdbcTemplate.update(sql);
	}

	public Map queryDangerousDetailList(String xh, Map<String, Object> filter,
			String sign) {
		return null;
	}

	public Map queryDangerousDetailList(Map<String, Object> filter) {
		return null;
	}

	@Override
	public int getForDetilTotal(String xh, Map<String, Object> filter,
			String sign) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, Object>> queryDetilListExt(String xh,
			Map<String, Object> filter, String sign) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getForDetilDangerousTotal(String xh, Map<String, Object> filter,
			String sign) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, Object>> queryDangerousDetailListExt(String xh,
			Map<String, Object> filter, String sign) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<String, Object> getListExt(Map<String, Object> filter)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getListTotal(Map<String, Object> filter) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
