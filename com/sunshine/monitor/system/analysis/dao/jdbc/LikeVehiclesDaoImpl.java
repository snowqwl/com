package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.dao.LikeVehiclesDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
@Repository("likeDao")
public class LikeVehiclesDaoImpl extends BaseDaoImpl implements LikeVehiclesDao {

	public Map<String, Object> queryForLike(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		
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
		String sql ="select DISTINCT HPHM ,"
			+" COUNT(DISTINCT(SUBSTR(KDBH, 1, 6))) OVER(PARTITION BY SUBSTR(KDBH, 1, 6), HPHM) AS XZQS,"
			+" COUNT(DISTINCT(KDBH)) OVER(PARTITION BY KDBH, HPHM) AS KKS,"
			+" COUNT(DISTINCT(TRUNC(GCSJ))) OVER(PARTITION BY TRUNC(GCSJ), HPHM) AS TS,"
			+" COUNT(HPHM) OVER(PARTITION BY HPHM) AS GCCS"
			+" from "+Constant.SCS_PASS_TABLE+""
			+" where hphm like '"+veh.getHphm()+"'" 
			+" and gcsj between to_date('"+veh.kssj+"','yyyy-mm-dd hh24:mi:ss') "
		    +" and to_date('"+veh.jssj+"','yyyy-mm-dd hh24:mi:ss')"
		    +" and kdbh in ("+veh.getKdbh()+")"
		    +hpys
		    +csys
		    +hpzl
		    +" ORDER BY XZQS DESC, KKS DESC, TS DESC, GCCS DESC";
		Map map =this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	public Map<String, Object> queryForLikeList(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
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
		
		
		String sql ="select gcxh,hphm,kdbh,substr(kdbh,0,6) xzqh,fxbh,cdbh,to_char(gcsj,'yyyy-mm-dd hh24:mi:ss') gcsj,hpys  from "+Constant.SCS_PASS_TABLE+" "
			        +" where hphm = '"+veh.getHphm()+"'"
			        +" and gcsj between to_date('"+veh.kssj+"','yyyy-mm-dd hh24:mi:ss')  "
			        +" and to_date('"+veh.jssj+"','yyyy-mm-dd hh24:mi:ss')"
			        +" and kdbh in ("+veh.getKdbh()+")" 
			        +hpys
				    +csys
				    +hpzl
			        +" order by gcsj desc ";
			      
		Map map=this.findPageForMap(sql,Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	public Map<String, Object> queryForLikePic(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		
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
		
		String sql ="select gcxh,hphm,kdbh,substr(kdbh,0,6) xzqh,fxbh,cdbh,to_char(gcsj,'yyyy-mm-dd hh24:mi:ss') gcsj,hpys,tp1  from "+Constant.SCS_PASS_TABLE+" "
	        +" where hphm = '"+veh.getHphm()+"'"
	        +" and gcsj between to_date('"+veh.kssj+"','yyyy-mm-dd hh24:mi:ss')  "
	        +" and to_date('"+veh.jssj+"','yyyy-mm-dd hh24:mi:ss')"
	        +" and kdbh in ("+veh.getKdbh()+")"
	        +hpys
		    +csys
		    +hpzl
		    +" order by gcsj desc ";
		    
		Map map=this.findPageForMap(sql,Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));

		return map;
	}

	public List getXzqh() {
		String sql=" select xzqhdm,xzqhqc from frm_xzqh";
		List list=this.jdbcTemplate.queryForList(sql);
		return list;
	}

	public List getKdmc() {
		String sql=" select * from dc_code_gate";
		List list=this.jdbcTemplate.queryForList(sql);
		return list;
	}

	public List queryForLikeLast(VehPassrec veh) throws Exception {
	
		
		String sql =" select  gcxh,hphm,kdbh,substr(kdbh,0,6) xzqh,fxbh,cdbh,to_char(gcsj,'yyyy-mm-dd hh24:mi:ss') gcsj,hpys,tp1 from "+Constant.SCS_PASS_TABLE+" v"
			+" where v.hphm='"+veh.getHphm()+"' "
			+" and v.gcsj=("
			+" select max(gcsj) from "+Constant.SCS_PASS_TABLE+"   "
			+" where gcsj between to_date('"+veh.kssj+"','yyyy-mm-dd hh24:mi:ss')  "
	        +" and to_date('"+veh.jssj+"','yyyy-mm-dd hh24:mi:ss')"
	        +" and kdbh in ("+veh.getKdbh()+")"
	        +" and hphm='"+veh.getHphm()+"'"
	        +" )"
	        +" and v.kdbh in ("+veh.getKdbh()+")"
	        ;
		List list=this.jdbcTemplate.queryForList(sql);
		System.out.println("");
		return list;
	}

	public String getKdmcByKdbh(String kdbh) throws Exception {
		String sql=" select kdmc from dc_code_gate " 
				  +" where kdbh='"+kdbh+"'";
		String kdmc=this.jdbcTemplate.queryForObject(sql, String.class);
		if (kdmc==null){
			kdmc="";
		}
		return kdmc;
	}

	public String getXzqhByKdbh6(String kdbh6) throws Exception {
		String sql=" select xzqhqc from frm_xzqh " 
			  +"  where xzqhdm=substr('"+kdbh6+"',0,6)";
		
	List xzqhqc=this.jdbcTemplate.queryForList(sql);
	if(xzqhqc!=null){
	if (xzqhqc.size()!=0){
	 Map map=(Map)xzqhqc.get(0);		
	 	return map.get("xzqhqc").toString();
	}
	}
	return "";
	}

	public Map<String, Object> queryForLikePage(Map<String, Object> filter)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List getFxmc() {
		StringBuffer sb=new StringBuffer();
		sb.append(" select fxbh,fxmc from dc_code_direct ");
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sb.toString());
		return list;
	}

	public Map<String, Object> queryForCarTrack(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		return null;
	}

	public Map<String, Object> queryCarTrackPage(Map<String, Object> filter)
			throws Exception {
		return null;
	}


	@Override
	public List<Map<String, Object>> queryForLikeExt(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getForLikeTotal(VehPassrec veh, Map<String, Object> filter)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> List<T> queryForListByMap(Map<String, Object> map,
			String tablename, Class<T> classz) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}







}
