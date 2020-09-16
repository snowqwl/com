package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.dao.XYScrapNaiDao;

@Repository("xyScrapNaiDao")
public class XYScrapNaiDaoImpl extends BaseDaoImpl  implements XYScrapNaiDao {

	private static Logger log = LoggerFactory.getLogger(XYScrapNaiDaoImpl.class);
	@Override
	public Map<String, Object> findXyForPage(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		// TODO Auto-generated method stub
		/*StringBuffer sb = new StringBuffer("select t.ID,t.HPHM,t.HPZL,t.HPYS,t.CLLX,t.CSYS,t.CLPP,t.CLWX,"
				+ "t.TP1,t.TP2,t.TP3,t.BDLJ,t.RKSJ,t.XXLY,t.XZQH,t.CZR,t.LB,t.BY1,t.BY2,t.BY3,t.zt"
				+ " from jm_zyk_scrap_nai t");*/
		StringBuffer sb = new StringBuffer("select t.ID as ID,t.HPHM as HPHM,t.HPZL as HPZL,t.HPYS as HPYS,t.CLLX as CLLX,t.CSYS as CSYS,t.CLWX as CLWX,"
				+ "t.TP1 as TP1,t.TP2 as TP2,t.TP3 as TP3,t.RKSJ as RKSJ,t.XXLY as XXLY,t.XZQH as XZQH,t.CZR as CZR,t.LB as LB,t.ZT as ZT,'' as GCXH"
				+ " from jm_zyk_scrap_nai t");
		//时间必选
		sb.append(" where t.rksj > to_date('").append(veh.getKssj()).append("','yyyy-MM-dd HH24:mi:ss')")
		  .append(" and  t.rksj <= to_date('").append(veh.getJssj()).append("','yyyy-MM-dd HH24:mi:ss')");	
		if(veh.getHphm()!=null&&veh.getHphm().length()>0)
			sb.append(" and t.hphm like '").append(veh.getHphm()).append("'");
		if(veh.getKdbh()!=null&&veh.getKdbh().length()>0)
			sb.append(" and t.kkbh in (").append(veh.getKdbh()).append(")");
		if(veh.getHpys()!=null&&veh.getHpys().length()>0)
			sb.append(" and t.hpys = '").append(veh.getHpys()).append("'");
		if(veh.getCsys()!=null&&veh.getCsys().length()>0)
			sb.append(" and t.csys = '").append(veh.getCsys()).append("'");
		if(veh.getHpzl()!=null&&veh.getHpzl().length()>0)
			sb.append(" and t.hpzl = '").append(veh.getHpzl()).append("'");
		if(veh.getCllx()!=null&&veh.getCllx().length()>0)
			sb.append(" and t.cllx = '").append(veh.getCllx()).append("'");
		if(veh.getXxly()!=null&&veh.getXxly().length()>0)
			sb.append(" and t.xxly = '").append(veh.getXxly()).append("'");
		if(veh.getLb()!=null&&veh.getLb().length()>0)
			sb.append(" and t.lb = '").append(veh.getLb()).append("'");
		sb.append(" and t.hphm not in (").append(Constant.SCS_NO_PLATE).append(")");				
		
		//查询关联统计结果
		//StringBuffer sql = new StringBuffer();
//		sql.append("  select x.hphm,x.hpys,x.hpzl,"
//				+ "count(distinct x.gcxh) as cs,count(distinct x.kkbh) as kks,"
//				+ "count(distinct substr(x.kkbh,1,6)) as xzqs,max(x.gcsj) as zhgcsj from ( ").append(sb).append(")  x group by hphm,hpys,hpzl order by zhgcsj desc");
		sb.append(" order by t.rksj desc");
		log.info("报废未年检-orcl-sql: "+sb.toString());
		Map<String,Object> map = this.findPageForMap(sb.toString(),
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	@Override
	public Map<String, Object> findXyCsForPage(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		// TODO Auto-generated method stub
		/*StringBuffer sb = new StringBuffer("select t.ID,t.HPHM,t.HPZL,t.HPYS,t.CLLX,t.CSYS,t.CLPP,t.CLWX,"
				+ "t.TP1,t.TP2,t.TP3,t.BDLJ,t.RKSJ,t.XXLY,t.XZQH,t.CZR,t.LB,t.BY1,t.BY2,t.BY3,t.zt"
				+ " from jm_zyk_scrap_nai t");*/
		StringBuffer sb = new StringBuffer("select t.HPHM,t.HPZL,t.HPYS,count(1) CS from jm_zyk_scrap_nai t");
		//时间必选
		sb.append(" where t.rksj > to_date('").append(veh.getKssj()).append("','yyyy-MM-dd HH24:mi:ss')")
		  .append(" and  t.rksj <= to_date('").append(veh.getJssj()).append("','yyyy-MM-dd HH24:mi:ss')");	
		if(veh.getHphm()!=null&&veh.getHphm().length()>0)
			sb.append(" and t.hphm like '").append(veh.getHphm()).append("'");
		if(veh.getKdbh()!=null&&veh.getKdbh().length()>0)
			sb.append(" and t.kkbh in (").append(veh.getKdbh()).append(")");
		if(veh.getHpys()!=null&&veh.getHpys().length()>0)
			sb.append(" and t.hpys = '").append(veh.getHpys()).append("'");
		if(veh.getCsys()!=null&&veh.getCsys().length()>0)
			sb.append(" and t.csys = '").append(veh.getCsys()).append("'");
		if(veh.getHpzl()!=null&&veh.getHpzl().length()>0)
			sb.append(" and t.hpzl = '").append(veh.getHpzl()).append("'");
		if(veh.getCllx()!=null&&veh.getCllx().length()>0)
			sb.append(" and t.cllx = '").append(veh.getCllx()).append("'");
		if(veh.getXxly()!=null&&veh.getXxly().length()>0)
			sb.append(" and t.xxly = '").append(veh.getXxly()).append("'");
		if(veh.getLb()!=null&&veh.getLb().length()>0)
			sb.append(" and t.lb = '").append(veh.getLb()).append("'");
		//sb.append(" and t.hphm not in (").append(Constant.SCS_NO_PLATE).append(")");
		/**
		 * sql优化  hyj
		 * */
		sb.append(" and not exists( select z.hphm from jm_zyk_scrap_nai z where ( z.hphm in ( ").append(Constant.SCS_NO_PLATE).append("")
		.append(" ) and z.id=t.id ))");
		
		//查询关联统计结果
		//StringBuffer sql = new StringBuffer();
//		sql.append("  select x.hphm,x.hpys,x.hpzl,"
//				+ "count(distinct x.gcxh) as cs,count(distinct x.kkbh) as kks,"
//				+ "count(distinct substr(x.kkbh,1,6)) as xzqs,max(x.gcsj) as zhgcsj from ( ").append(sb).append(")  x group by hphm,hpys,hpzl order by zhgcsj desc");
		sb.append(" group by t.hphm,t.hpzl,t.hpys");
		sb.append(" order by cs desc");
		log.info("报废未年检-orcl-sql: "+sb.toString());
		Map<String,Object> map = this.findPageForMap(sb.toString(),
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
//	@Override
//	public Map<String, Object> queryForXYList(VehPassrec veh,Map<String, Object> filter) throws Exception {
//		String hpys="";
//		if(veh.getHpys()!=null&&!veh.getHpys().equals("")){
//			hpys=" and hpys='"+veh.getHpys()+"'";
//		}
//		String csys="";
//		if(veh.getCsys()!=null&&!veh.getCsys().equals("")){
//			csys=" and csys='"+veh.getCsys()+"'";
//		}
//		String hpzl="";
//		if(veh.getHpzl()!=null&&!veh.getHpzl().equals("")){
//			hpzl=" and hpzl='"+veh.getHpzl()+"'";
//		}
//		String kkbh="";
//		if(veh.getKdbh()!=null&&!veh.getKdbh().equals("")&&!veh.getKdbh().equals("''")){
//			kkbh=" and kkbh in ("+veh.getKdbh()+")";
//		}
//		
//		String sql ="select xyid,gcxh,hphm,kkbh,substr(kkbh,0,6) xzqh,fxbh,to_char(gcsj,'yyyy-mm-dd hh24:mi:ss') gcsj,hpys,tp1,xxly,lb,zt  from jm_zyk_xy_scrap_nai "
//	        +" where hphm = '"+veh.getHphm()+"'"
//	        +" and gcsj between to_date('"+veh.kssj+"','yyyy-mm-dd hh24:mi:ss')  "
//	        +" and to_date('"+veh.jssj+"','yyyy-mm-dd hh24:mi:ss')"
//	        //+" and kkbh in ("+veh.getKdbh()+")"
//	        +kkbh
//	        +hpys
//		    +csys
//		    +hpzl
//		    +" order by gcsj desc ";
//		    
//		Map<String, Object> map=this.findPageForMap(sql,Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
//
//		return map;
//	}

	@Override
	public Map<String, Object> getXYCarInfo(String id) throws Exception {
		// TODO Auto-generated method stub
		
		String sql = "select ID,HPHM,HPZL,HPYS,CLLX,CSYS,CLPP,CLWX,"
				+ "TP1,TP2,TP3,BDLJ,RKSJ,XXLY,XZQH,CZR,LB,BY1,BY2,BY3,ZT"
				+ " from jm_zyk_scrap_nai t where t.id = '"+id+"'";
		Map<String, Object> list = this.jdbcTemplate.queryForMap(sql);
	    return list;
	}
	
	@Override
	public int updateXYStatus(String status, String xyids) throws Exception {
		// TODO Auto-generated method stub
		 String sql ="update jm_zyk_scrap_nai set zt='"+status+"' where id = '"+xyids+"'";
	     return this.jdbcTemplate.update(sql);
	}

//	@Override
//	public int insertScrapNaiInfo(String xyId) throws Exception {
//		// TODO Auto-generated method stub
//		String sql ="select hphm,hpzl,hpys,cllx,csys,clpp,clwx from jm_zyk_xy_scrap_nai where xyid='"+xyId+"'";
//		Map<String, Object> map = this.queryDetail("jm_zyk_xy_scrap_nai", "xyid", xyId, null);
//		
//		
//		String sql2 ="insert into jm_zyk_scrap_nai values(ID,HPHM,HPZL,HPYS,CLLX,CSYS,CLPP,CLWX,TP1,TP2,TP3,BDLJ,RKSJ,XXLY,XZQH,CZR,LB,BY1,BY2,BY3,zt)"
//				+ "values(SEQ_SCRAP_NAI_XH.Nextval,)";
//	     return this.jdbcTemplate.update(sql2);
//	}

	
}
