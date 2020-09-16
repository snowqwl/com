package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.dao.AcrossRegionsSCSDao;


@Repository("acrossRegionsSCSDao")
public class AcrossRegionsSCSDaoImpl extends ScsBaseDaoImpl implements AcrossRegionsSCSDao {

	@Autowired
	@Qualifier("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	@Override
	public Map<String, Object> getList(Map<String, Object> filter)
			throws Exception {		
		//得到注册地
		String zcd="";
		if(filter.get("zcd")!=null){
			if(!filter.get("zcd").toString().equals("")){
			zcd="  and substring(HPHM, 1, 2) in ("+filter.get("zcd").toString()+")";
			}
		}	
		
		// 得到本地行政区划
		String xzqh = "";
		if(filter.get("local")!=null){
			xzqh = filter.get("local").toString();
			xzqh = xzqh.substring(0,xzqh.indexOf(":")-2);
		}
		
		//过境型参数		
		int  gjx=3;
		if(filter.get("gjx")!=null){
			if(filter.get("gjx").toString().length()!=0){
				gjx=Integer.parseInt(filter.get("gjx").toString());
			}
		}
		//本地型参数
		int bdx=6;
		if(filter.get("bdx")!=null){
			if(filter.get("bdx").toString().length()!=0){
				bdx=Integer.parseInt(filter.get("bdx").toString());
			}
		}
	        
		//利用分析函数实现去重
	    String temp_sql = "select t.*,(case when total<="+gjx+" then 'gjx' " 
						+ " when total>"+gjx+" and total <="+bdx+" then 'hnx' else 'bdx' end) as zt " 
						+ " from ( select hphm,hpzl,hpys,count(hphm) as total from ( select hphm,hpzl,hpys ,rank()over(PARTITION by hphm,hpzl,hpys,gcsj order by gcsj desc )mm " 
						+ " from veh_passrec where  " 
						+ " KDBH like '"
						+ xzqh
						+ "%'"
						+ zcd
						+ " and gcsj >= to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss') "
					    + " and gcsj <= to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
						+ " )a where mm=1 group by hphm,hpzl,hpys)t ";
		String sql = " select substr(hphm,1,2) HP,count(hphm) TOTAL," +
					 " sum(case when zt='gjx' then 1 else 0 end) GJX," +
					 " sum(case when zt='hnx' then 1 else 0 end) HNX," +
					 " sum(case when zt='bdx' then 1 else 0 end) BDX" +
					 " from ("+temp_sql+")c group by substr(hphm,1,2) order by substr(hphm,1,2) ";
	
		Map<String, Object> resultmap = this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
        
		return  resultmap;
	}
	
	@Override
	public Map<String, Object> queryDetilList(Map<String ,Object> filter,String sign) throws Exception {
		//得到注册地
		String zcd="";
			if(filter.get("zcd")!=null){
				if(!filter.get("zcd").toString().equals("")){
				zcd="  and substring(HPHM, 1, 2) in ('"+filter.get("zcd").toString()+"')";
			}
		}
		// 得到本地行政区划
		String xzqh = "";
		if(filter.get("local")!=null){
			xzqh = filter.get("local").toString();
			xzqh = xzqh.substring(0,xzqh.indexOf(":")-2);
		}	
		//过境型参数		
		int  gjx=3;
		if(filter.get("gjx")!=null){
			if(filter.get("gjx").toString().length()!=0){
				gjx=Integer.parseInt(filter.get("gjx").toString());
			}
		}
		//本地型参数
		int bdx=6;
		if(filter.get("bdx")!=null){
			if(filter.get("bdx").toString().length()!=0){
				bdx=Integer.parseInt(filter.get("bdx").toString());
			}
		}
		//不同类型不同极限
		String type="";		
		if(sign!=null){
			if(sign.equals("BDX")){
				type="  HAVING  COUNT(1) > "+bdx+"  ";
			}
			if(sign.equals("HNX")){
				type="  HAVING  COUNT(1) > "+gjx+" and  COUNT(1) <= "+bdx+" ";				
			}
			if(sign.equals("GJX")){
				type="  HAVING  COUNT(1) <= "+gjx+"  ";
			}
		}
					
		String temp_sql = " select distinct hphm,hpzl,hpys,kdbh,gcsj from veh_passrec t where 1 =1 " 
						+ zcd
						+ " and kdbh like'"
						+ xzqh
						+ "%'"
						+ " and gcsj >= to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss') "
						+ " and gcsj <= to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
						;
		
		String select_sql = " select t.hphm,t.hpys,t.hpzl,count(1) cs," 
						  + " count(distinct kdbh) kks,count(distinct substr(kdbh,0,6)) xzqs,"
						  + " count(distinct to_char(gcsj,'yyyy-mm-dd')) as ts from ("+temp_sql+")t "
					 	  + " group by hphm,hpzl,hpys "+type+" order by count(1) desc "
					 	  ;
		
		Map<String, Object> map = this.findPageForMap(select_sql, Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()));	
		
		return map;										
	}

	@Override
	public Map<String, Object> queryForDetilList(Map<String, Object> filter) {
		
		//得到本地行政区划
		String xzqh = "";
		if(filter.get("local")!=null){
			xzqh = filter.get("local").toString();
			xzqh = xzqh.substring(0,xzqh.indexOf(":")-2);
		}
		
		//得到注册地
		String zcd="";
			if(filter.get("zcd")!=null){
				if(!filter.get("zcd").toString().equals("")){
				zcd="  and substring(HPHM, 1, 2) in ('"+filter.get("zcd").toString()+"')";
			}
		}
			
		//分析函数去重
		String result = " select * from (select distinct rank()over(partition by hphm,hpzl,gcsj order by gcsj desc) mm,t.gcxh as GCXH,t.hphm as HPHM,T .hpzl AS HPZL,t.kdbh as KDBH,substring(t.kdbh,1,6) as XZQH,t.fxbh as FXBH,t.cdbh as CDBH, GCSJ,t.tp1 as TP1  from veh_passrec t where 1 = 1 " 
			          + zcd
			          + " and KDBH like '"
			          + xzqh
			          + "%' "
			          + " and gcsj >= '"+filter.get("kssj").toString()+"' "
			          + " and gcsj <= '"+filter.get("jssj").toString()+"' "
			          + " and hphm ='"+filter.get("hphm").toString()+"'"
			          + " and hpzl ='"+filter.get("hpzl").toString()+"'"
			          //+ " and hpys ='"+filter.get("hpys").toString()+"'"
			          + ")t where mm=1  order by gcsj desc "
			          ;
		
		Map<String, Object> map =this.findPageForMap(result, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		
		return map;
	}

	@Override
	public List<Map<String, Object>> getListExt(Map<String, Object> filter)
			throws Exception {
		String sql = getSql(filter);
		List<Map<String, Object>> list = this.getPageDatas(sql,Integer.parseInt(filter.get("page").toString()),Integer.parseInt(filter.get("rows").toString()));
		return list;
	}

	@Override
	public int getListTotal(Map<String, Object> filter) throws Exception {
		String sql = getSql(filter);
		return super.getTotal(sql);
	}
	
	public String getSql(Map<String, Object> filter) throws Exception{
		//得到注册地
		String zcd="";
		if(filter.get("zcd")!=null){
			if(!filter.get("zcd").toString().equals("")){
			zcd=" and cl_cs in ("+filter.get("zcd").toString()+")";
			}
		}	
		
		// 得到本地行政区划
		String xzqh = "";
		if(filter.get("local")!=null){
			xzqh = filter.get("local").toString();
			xzqh = xzqh.substring(0,xzqh.indexOf(":")-2);
		}
		
		//过境型参数		
		int  gjx=3;
		if(filter.get("gjx")!=null){
			if(filter.get("gjx").toString().length()!=0){
				gjx=Integer.parseInt(filter.get("gjx").toString());
			}
		}
		//本地型参数
		int bdx=6;
		if(filter.get("bdx")!=null){
			if(filter.get("bdx").toString().length()!=0){
				bdx=Integer.parseInt(filter.get("bdx").toString());
			}
		}
	        
		//利用分析函数实现去重
	    String temp_sql = "select t.*,(case when total<="+gjx+" then 'gjx' " 
						+ " when total>"+gjx+" and total <="+bdx+" then 'hnx' else 'bdx' end) as zt " 
						+ " from ( select hphm,hpzl,count(1) as total from yp_passrec_ydcp where  " 
						+ " gc_cs = '"
						+ xzqh
						+ "'"
						+ zcd
						+ " and gcsj >='"+filter.get("kssj").toString()+"' "
					    + " and gcsj <='"+filter.get("jssj").toString()+"' "
						+ "  group by hphm,hpzl)t ";
		String sql = " select substr(hphm,1,2) HP,count(1) TOTAL," +
					 " sum(case when zt='gjx' then 1 else 0 end) GJX," +
					 " sum(case when zt='hnx' then 1 else 0 end) HNX," +
					 " sum(case when zt='bdx' then 1 else 0 end) BDX" +
					 " from ("+temp_sql+")c group by substr(hphm,1,2) order by substr(hphm,1,2) ";
		return sql;
	}

	@Override
	public List<Map<String, Object>> queryDetilListExt(
			Map<String, Object> filter, String sign) throws Exception {
		String sql = getDetailListSql(filter, sign);
		List<Map<String, Object>> list = this.getPageDatas(sql,Integer.parseInt(filter.get("page").toString()),Integer.parseInt(filter.get("rows").toString()));
		return list;
	}

	@Override
	public int queryDetilListTotal(Map<String, Object> filter, String sign)
			throws Exception {
		String sql = getDetailListSql(filter, sign);
		return super.getTotal(sql);
	}
	
	public String getDetailListSql(Map<String, Object> filter, String sign) throws Exception{
		//得到注册地
		String zcd="";
			if(filter.get("zcd")!=null){
				if(!filter.get("zcd").toString().equals("")){
				zcd="  and cl_cs = '"+filter.get("zcd").toString()+"'";
			}
		}
		// 得到本地行政区划
		String xzqh = "";
		if(filter.get("local")!=null){
			xzqh = filter.get("local").toString();
			xzqh = xzqh.substring(0,xzqh.indexOf(":")-2);
		}	
		//过境型参数		
		int  gjx=3;
		if(filter.get("gjx")!=null){
			if(filter.get("gjx").toString().length()!=0){
				gjx=Integer.parseInt(filter.get("gjx").toString());
			}
		}
		//本地型参数
		int bdx=6;
		if(filter.get("bdx")!=null){
			if(filter.get("bdx").toString().length()!=0){
				bdx=Integer.parseInt(filter.get("bdx").toString());
			}
		}
		//不同类型不同极限
		String type="";		
		if(sign!=null){
			if(sign.equals("BDX")){
				type="  cs >  "+bdx+"  ";
			}
			if(sign.equals("HNX")){
				type="  cs > "+gjx+" and  cs <= "+bdx+" ";				
			}
			if(sign.equals("GJX")){
				type="  cs <= "+gjx+" ";
			}
		}
					
		String temp_sql = " select hphm,hpzl,COUNT (1) cs,count (distinct kkbh) kks,count (distinct gc_qx) xzqs,count(distinct gc_rq) as ts from yp_passrec_ydcp where " 
						+ " gc_cs = '"
						+ xzqh
						+ "'"
						+ zcd
						+ " and gcsj >= '"+filter.get("kssj").toString()+"'"
						+ " and gcsj <= '"+filter.get("jssj").toString()+"'"
						;
		
		String select_sql = " select hphm,hpzl,cs,kks,xzqs,ts" 
						  + "  from ("+temp_sql+" "
					 	  + " group by hphm,hpzl ) c1 where "+type+" order by cs desc "
					 	  ;
		return select_sql;			
	}
}
