package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.dao.LikeVehiclesDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
@Repository("likeSCSDao")
public class LikeVehiclesSCSDaoImpl extends ScsBaseDaoImpl implements LikeVehiclesDao {
	
	protected Logger debugLogger = LoggerFactory.getLogger(LikeVehiclesSCSDaoImpl.class);
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public String getSql(VehPassrec veh, Map<String, Object> filter) throws Exception{
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
				kdbh = "and t.kdbh in (" + veh.getKdbh() + ")";
			}
		}

		String sql = " select * from (select count(t.hphm) as GCCS,t.hphm as HPHM,t.hpys as HPYS,t.hpzl as HPZL from veh_passrec t"
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
				+ "  group by t.hphm,t.hpys,t.hpzl )c order by gccs desc ";		
		return sql;
	}
	
	public int getForLikeTotal(VehPassrec veh, Map<String, Object> filter) throws Exception{
		String sql = getSql(veh, filter);
		return super.getTotal(sql);
	}
	
	//按条件获取号牌号码和过车数
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryForLikeExt(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		String sql = getSql(veh, filter);
		List list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---主页条件查询---查询临时表end："+sdf.format(new Date()));
		return list;
	}
	
	//按条件获取号牌号码和过车数
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForLike(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		//条件整理
		String hpys="";
		if(veh.getHpys()!=null){
			if(!veh.getHpys().equals(""))
			hpys=" and t.hpys='"+veh.getHpys()+"'";
		}
		String csys="";
		if(veh.getCsys()!=null){
			if(!veh.getCsys().equals(""))
			csys=" and t.csys='"+veh.getCsys()+"'";
		}
		String hpzl="";
		if(veh.getHpzl()!=null){
			if(!veh.getHpzl().equals(""))
			hpzl=" and t.hpzl='"+veh.getHpzl()+"'";
		}
		
		String kdbh="";
		if(veh.getKdbh()!=null){
			if(!veh.getKdbh().equals("''")){
				kdbh="and t.kdbh in ("+veh.getKdbh()+")";
			}
		}
		
		
		String sql =" select * from (select count(t.hphm) as GCCS,t.hphm as HPHM,t.hpys as HPYS,t.hpzl as HPZL from veh_passrec t"
			+" where t.hphm like '"+veh.getHphm()+"'" 
			+" and t.gcsj >= '"+veh.kssj+"'"
		    +" and t.gcsj <= '"+veh.jssj+"'"
		    +kdbh
		    +hpys
		    +csys
		    +hpzl
		    +"  group by t.hphm,t.hpys,t.hpzl )c order by gccs desc "
		    ;
		Map map =this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---主页条件查询---查询临时表end："+sdf.format(new Date()));
		return map;
	}
	
	

	
	//根据号牌号码和部分条件获取过车的轨迹列表
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForLikeList(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		//条件整合
		String hpys="";
		if(veh.getHpys()!=null&&!veh.getHpys().equals("")){
			hpys=" and t.hpys='"+veh.getHpys()+"'";
		}
		@SuppressWarnings("unused")
		String csys="";
		if(veh.getCsys()!=null&&!veh.getCsys().equals("")){
			csys=" and t.csys='"+veh.getCsys()+"'";
		}
		String hpzl="";
		if(veh.getHpzl()!=null&&!veh.getHpzl().equals("")){
			hpzl=" and t.hpzl='"+veh.getHpzl()+"'";
		}
		
		String kdbh="";
		if(veh.getKdbh()!=null){
			if(!veh.getKdbh().equals("''")){
				kdbh="and t.kdbh in ("+veh.getKdbh()+")";
			}
		}
		//执行查询语句
		String sql ="select t.gcxh as GCXH,t.hphm as HPHM,t.kdbh as KDBH,substring(t.kdbh,1,6) as XZQH,t.fxbh as FXBH,t.cdbh as CDBH,str_to_date(t.gcsj,'%Y-%m-%d %H:%i:%s') as GCSJ,t.hpys as HPYS " 
				    +" from t("+Constant.SCS_PASS_TABLE+":: "
			        +" t.hphm = '"+veh.getHphm()+"'"
			        +" and t.gcsj >= '"+veh.kssj+"'"
				    +" and t.gcsj <= '"+veh.jssj+"'"
				    +kdbh
			        +hpys
				    //+csys
				    +hpzl
			        +" ) order GCSJ:desc ";
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---过车轨迹列表---查询sql："+sql);	
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---过车轨迹列表---查询start："+sdf.format(new Date()));	
		Map map=this.findPageForMap(sql,Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---过车轨迹列表---查询end："+sdf.format(new Date()));	
		return map;
	}
	
	//根据号牌号码和部分条件获取过车的轨迹视图
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForLikePic(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		//条件整理
		String hpys="";
		if(veh.getHpys()!=null){
			hpys=" and hpys='"+veh.getHpys()+"'";
		}
		@SuppressWarnings("unused")
		String csys="";
		if(veh.getCsys()!=null){
			csys=" and csys='"+veh.getCsys()+"'";
		}
		String hpzl="";
		if(veh.getHpzl()!=null){
			hpzl=" and hpzl='"+veh.getHpzl()+"'";
		}
		
		String kdbh="";
		if(veh.getKdbh()!=null){
			if(!veh.getKdbh().equals("''")){
				kdbh="and t.kdbh in ("+veh.getKdbh()+")";
			}
		}
		//执行查询语句
		String sql ="select t.gcxh as GCXH,t.hphm as HPHM,t.kdbh as KDBH,substring(t.kdbh,1,6) as XZQH,t.fxbh as FXBH,t.cdbh as CDBH,to_char (T .gcsj, 'yyyy-mm-dd hh24:mi:ss') AS GCSJ,t.hpys as HPYS ,t.tp1 as TP1" 
		    +" from veh_passrec t where "
	        +" t.hphm = '"+veh.getHphm()+"'"
	        +" and t.gcsj >= '"+veh.kssj+"'"
		    +" and t.gcsj <= '"+veh.jssj+"'"
	        +kdbh
	        +hpys
		    //+csys
		    +hpzl 
		    + " order by gcsj desc";

		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---过车轨迹视图---查询sql："+sql);	
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---过车轨迹视图---查询start："+sdf.format(new Date()));	
		Map map=this.findPageForMap(sql,Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---过车轨迹视图---查询end："+sdf.format(new Date()));	
		return map;
	}
	
	//获取行政区划
	@SuppressWarnings("unchecked")
	public List getXzqh() {
		String sql=" select xzqhdm,xzqhqc from frm_xzqh";
		List list=this.jdbcScsTemplate.queryForList(sql);
		return list;
	}
	
	//获取卡点名称
	@SuppressWarnings("unchecked")
	public List getKdmc() {
		String sql=" select * from code_gate";
		List list=this.jdbcScsTemplate.queryForList(sql);
		return list;
	}
	
	//获取最后一次过车记录
	@SuppressWarnings("unchecked")
	public List queryForLikeLast(VehPassrec veh) throws Exception {
		//条件整理
		String kdbh="";
		if(veh.getKdbh()!=null){
			if(!veh.getKdbh().equals("''")){
				kdbh="and t.kdbh in ("+veh.getKdbh()+")";
			}
		}
		//获取该号牌最后一次过车时间
		String maxsql=
			" select max(t.gcsj) from  veh_passrec t where "
			+"  t.gcsj >= '"+veh.kssj+"'"
		    +" and t.gcsj <= '"+veh.jssj+"'"
	        +kdbh
	        +" and t.hphm='"+veh.getHphm()+"'"
	        +" ";
		String maxtime=this.jdbcScsTemplate.queryForObject(maxsql, String.class).toString();
		//查询最后一次过车详情
		String sql =" select t.gcxh as GCXH,t.hphm as HPHM,t.kdbh as KDBH,t.hpzl as HPZL ,substring(t.kdbh,1,6) as XZQH,t.fxbh as FXBH,t.cdbh as CDBH,to_char(t.gcsj,'yyyy-mm-dd hh24:mi:ss') as GCSJ,t.hpys as HPYS ,t.tp1 as TP1 "
			+" from veh_passrec t where  "
			+" t.hphm='"+veh.getHphm()+"' "
			+" and t.gcsj='"+maxtime+"'"			
			+kdbh ;	

		List list=this.jdbcScsTemplate.queryForList(sql);
		return list;
	}
	
	//获取卡点名称
	public String getKdmcByKdbh(String kdbh) throws Exception {
		String sql=" select kdmc from code_gate " 
				  +" where kdbh='"+kdbh+"'";
		String kdmc=this.jdbcScsTemplate.queryForObject(sql, String.class);
		if (kdmc==null){
			kdmc="";
		}
		return kdmc;
	}
	
	//获取行政区划前6位
	@SuppressWarnings("unchecked")
	public String getXzqhByKdbh6(String kdbh6) throws Exception {
		String sql=" select xzqhqc from frm_xzqh " 
			  +"  where xzqhdm=substr('"+kdbh6+"',0,6)";
	List xzqhqc=this.jdbcScsTemplate.queryForList(sql);
	if (xzqhqc.size()!=0){
		Map map=(Map)xzqhqc.get(0);
		return map.get("xzqhqc").toString();
	}
	return "";
	}
	
	//获取方向名称
	@SuppressWarnings("unchecked")
	public List getFxmc() {
		return null;
	}
	
	//车辆轨迹查询
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForCarTrack(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		
		//条件整理
		String hpys="";
		if(veh.getHpys()!=null){
			if(!veh.getHpys().equals(""))
			hpys=" and t.hpys='"+veh.getHpys()+"'";
		}
		String csys="";
		if(veh.getCsys()!=null){
			if(!veh.getCsys().equals(""))
			csys=" and t.csys='"+veh.getCsys()+"'";
		}
		String hpzl="";
		if(veh.getHpzl()!=null){
			if(!veh.getHpzl().equals(""))
			hpzl=" and t.hpzl='"+veh.getHpzl()+"'";
		}
		
		String kdbh="";
		if(veh.getKdbh()!=null){
			if(!veh.getKdbh().equals("''")){
				kdbh="and t.kdbh in ("+veh.getKdbh()+")";
			}
		}
		
		//获取sessionid
		String sessionid="";
		if(filter.get("sessionid")!=null){
			sessionid=filter.get("sessionid").toString();
		}
		//定义表名
		String tablename=Constant.SCS_TEMP_PREFIX+"_LikeCarTrack_"+sessionid;
		
		//检索表是否存在
		    String ifexist ="DROP TABLE IF EXISTS "+tablename;
		    debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---检索表是否存在sql："+ifexist);
		    debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---检索表是否存在start："+sdf.format(new Date()));
			this.jdbcScsTemplate.update(ifexist);
			debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---检索表是否存在end："+sdf.format(new Date()));
		//创建临时表
			String sql ="create table "+tablename+"  (select t.*"
			//+" from "+Constant.SCS_PASS_TABLE+" t "
			+" from "+Constant.SCS_PASS_TABLE+" t use index(idx_gcsj_kdbh_hphm_hpzl_hpys)"
			+" where t.hphm like '"+veh.getHphm()+"'" 
			//+" and t.gcsj between str_to_date('"+veh.kssj+"','%Y-%m-%d %H:%i:%s') "
		   // +" and str_to_date('"+veh.jssj+"','%Y-%m-%d %H:%i:%s')"
			+" and t.gcsj >= '"+veh.kssj+"'"
		    +" and t.gcsj <= '"+veh.jssj+"'"
		    +kdbh
		    +hpys
		    +csys
		    +hpzl
		   // +"  group by t.hphm,t.hpys,t.hpzl) "
		    //+" ORDER  GCCS:DESC )"
		    +")"
		    ;
			debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---创建临时表sql："+sql);
			debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---创建临时表start："+sdf.format(new Date()));
			this.jdbcScsTemplate.update(sql);
			debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---创建临时表end："+sdf.format(new Date()));
		//查询临时表
        String selectSql="select  t.gcxh as GCXH,t.hphm as HPHM,t.kdbh as KDBH,t.hpzl as HPZL ,substring(t.kdbh,1,6) as XZQH,t.fxbh as FXBH,t.cdbh as CDBH,str_to_date(t.gcsj,'%Y-%m-%d %H:%i:%s') as GCSJ,t.hpys as HPYS ,t.tp1 as TP1 from t("+tablename+") ORDER GCSJ:DESC";
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---查询sql："+selectSql);
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---查询start："+sdf.format(new Date()));
        Map map =this.findPageForMap(selectSql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		debugLogger.info("o(∩_∩)oo(∩_∩)o模糊查询---车辆轨迹查询---查询end："+sdf.format(new Date()));
		return map;
	
		
		
		
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryCarTrackPage(Map<String, Object> filter)
			throws Exception {

		String sessionid="";
		if(filter.get("sessionid")!=null){
			sessionid=filter.get("sessionid").toString();
		}
		String tablename=Constant.SCS_TEMP_PREFIX+"_LikeCarTrack_"+sessionid;

        String selectSql="select  t.gcxh as GCXH,t.hphm as HPHM,t.kdbh as KDBH,t.hpzl as HPZL ,substring(t.kdbh,1,6) as XZQH,t.fxbh as FXBH,t.cdbh as CDBH,str_to_date(t.gcsj,'%Y-%m-%d %H:%i:%s') as GCSJ,t.hpys as HPYS ,t.tp1 as TP1 from t("+tablename+") ORDER GCSJ:DESC";
		Map map =this.findPageForMap(selectSql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}




	@Override
	public Map<String, Object> queryForLikePage(Map<String, Object> filter)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
