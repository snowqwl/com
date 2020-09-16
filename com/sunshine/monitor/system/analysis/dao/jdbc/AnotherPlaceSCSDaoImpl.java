package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.impl.Indenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.HideDayOutNightDayBean;
import com.sunshine.monitor.system.analysis.dao.AnotherPlaceDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;



@Repository("anotherSCSPlaceDao")
public class AnotherPlaceSCSDaoImpl extends ScsBaseDaoImpl implements AnotherPlaceDao {

	protected Logger debugLogger = LoggerFactory.getLogger(AnotherPlaceSCSDaoImpl.class);
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	@Qualifier("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;	
	

	@SuppressWarnings("unchecked")
	private static Map<String, Object> pageCountResult=null;
	@SuppressWarnings("unused")
	private static double anotherPlaceVehCount=0L;
	
	/**
	 * 根据指定条件查询外地号牌的车辆信息列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForAnotherPlaceList(VehPassrec veh,
			Map<String, Object> filter, String local) throws Exception {
		//条件整理
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
		//查询本地省份各地市的城市代码
		String sqlhphm="select HPHM from frm_hphm " 
				+" where " 
				+"dwdm like '"+local+"%'"
				;
		debugLogger.info("o(∩_∩)oo(∩_∩)o异地牌专题查询---查询本地省份各地市的城市代码sql："+sqlhphm);
		debugLogger.info("o(∩_∩)oo(∩_∩)o异地牌专题查询---查询本地省份各地市的城市代码start："+sdf.format(new Date()));
		List list=this.jdbcScsTemplate.queryForList(sqlhphm);
		debugLogger.info("o(∩_∩)oo(∩_∩)o异地牌专题查询---查询本地省份各地市的城市代码end："+sdf.format(new Date()));
		String hp="";
		for(int i=0;i<list.size();i++){
			Map map=(Map) list.get(i);
			hp+="'"+map.get("HPHM")+"',";
		}
		if(hp.length()!=0){
			hp=hp.substring(0, hp.length()-1);
		}
		//使用开窗函数分组统计异地省份下面各地市的过车数、卡口数、天数。
		String sql ="select DISTINCT HPHM ,HPYS , "
			+" COUNT(DISTINCT(SUBSTR(KDBH, 1, 6))) OVER(PARTITION BY SUBSTR(KDBH, 1, 6), HPHM) AS XZQS,"
			+" COUNT(DISTINCT(KDBH)) OVER(PARTITION BY KDBH, HPHM) AS KKS,"
			+" COUNT(DISTINCT(TRUNC(GCSJ))) OVER(PARTITION BY TRUNC(GCSJ), HPHM) AS TS,"
			+" COUNT(HPHM) OVER(PARTITION BY HPHM) AS GCCS"
			+" from "+Constant.SCS_PASS_TABLE+""
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
	
	/**
	 * 根据指定条件查询分组数据(注册地)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getList(Map<String, Object> filter)
			throws Exception {		
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
			zcd="  and substring(HPHM, 1, 1) in ("+filter.get("zcd").toString()+")";
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
		List localhplist = jdbcTemplate.queryForList(localhp);
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
		+" from ( select hphm,hpzl,hpys,count(hphm) as total from ( select hphm,hpzl,hpys ,rank()over(PARTITION by hphm,hpzl,hpys,gcsj order by gcsj desc )mm " 
		+" from veh_passrec  where  " 
		+" KDBH like '"
		+ local
		+"%' "
		+zcd
		+" and gcsj >= to_date('"+filter.get("kssj").toString()+"','yyyy-mm-dd hh24:mi:ss') "
	    +" and gcsj <= to_date('"+filter.get("jssj").toString()+"','yyyy-mm-dd hh24:mi:ss')"
	    +localhplists 	    
		+" )a where mm=1 group by hphm,hpzl,hpys)t "
		;
	String sql = " select substr(hphm,1,2) HP,count(hphm) TOTAL," +
				 " sum(case when zt='gjx' then 1 else 0 end) GJX," +
				 " sum(case when zt='hnx' then 1 else 0 end) HNX," +
				 " sum(case when zt='bdx' then 1 else 0 end) BDX" +
				 " from ("+temp_sql+")c group by substr(hphm,1,2) order by substr(hphm,1,2) ";
	
		Map<String, Object> resultmap = this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		int bjtotal=0;
        String dangerousMatchCity="";
        //高危地区城市
        String [] dangerousCity = null;
        if(filter.get("dangerousCity")!=null){
         dangerousCity= (String[]) filter.get("dangerousCity");
        }
        //高危地区同行次数
        int dangerousCs=1;
        int hpbjcount=0;
        if(filter.get("dangerousCs")!=null){
        	if(!filter.get("dangerousCs").toString().equals(""))
        	dangerousCs=Integer.parseInt(filter.get("dangerousCs").toString());
        }
        List<Map<String,Object>> list = (List<Map<String, Object>>)resultmap.get("rows");
		 //判断各地区的过车总量是否超过高危通行次数
		for(int i =0;i<list.size();i++){
			 Map map = list.get(i);	
			 if(Integer.parseInt(map.get("TOTAL").toString())>=dangerousCs){
				 if(dangerousCity!=null){
					 for(int j=0;j<dangerousCity.length;j++){				 
						 if(map.get("HP")!=null){
							 if(map.get("HP").toString().equals(dangerousCity[j])){
								 hpbjcount=Integer.parseInt(map.get("TOTAL").toString());
								 dangerousMatchCity+=map.get("HP").toString()+",";
								 break;
							 	}
						 	}
					 	}				
				 	}
			 	}
		 	bjtotal+=hpbjcount;
		}
		//放入预警总数
		resultmap.put("bjcount", bjtotal);
		//放入高危通行次数
		resultmap.put("dangerousCs", dangerousCs);
		resultmap.put("DANGEROUSID", filter.get("arrayid"));
		resultmap.put("dangerousMatchCity", dangerousMatchCity);
		//int rows = Integer.parseInt(map.get("total").toString());
		/*取全部的结果保存在内存***/
		//pageCountResult=this.findPageForMap(sql,1,rows);
		pageCountResult = resultmap;
		return  resultmap;
	}
	
	
	//分页查询分组数据（注册地）
	@SuppressWarnings("unchecked")
	public Map<String, Object> getListForpage(Map<Object,String> filter) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		List pageList=(List)pageCountResult.get("rows");
		int first=0;
		int end=0;
		first=(Integer.parseInt(filter.get("page").toString())-1)*Integer.parseInt(filter.get("rows").toString());
		end=first+Integer.parseInt(filter.get("rows").toString());
		if(end>pageList.size()){
			end=pageList.size();
		}
		/*if(first!=0)
			first=first+1;*/
		map.put("rows", pageList.subList(first, end));
		map.put("total", pageCountResult.get("total"));
		return map;
	}
	
	/**
	 * 保存分组的注册地异地牌信息
	 */
	@SuppressWarnings("unchecked")
	public String saveZcd( String taskname, Map<String, Object> filter) throws Exception {
		//查看缓存数据是否存在
		//if(pageCountResult==null)
		//	return "";
		//检索任务名是否重复
		List taskNameCheck=this.jdbcTemplate.queryForList("select taskname from  FRM_OTHERPLACE_ZCD where taskname='"+taskname+"' and rownum=1");
		if(taskNameCheck.size()!=0){
			return "sameName";
		}
		String sqltemp = getSql(filter);
		List<Map<String, Object>> list =this.getPageDatas(sqltemp, 1, 1000);
		
		//获取缓存分组分析数据
		//不能从缓存中获取数据
		//List list =(List) pageCountResult.get("rows");
		
		
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
		@SuppressWarnings("unused")
		String zcd="";
		if(filter.get("zcd")!=null){
			if(!filter.get("zcd").toString().equals("")){
			zcd="  and cl_sf in ("+filter.get("zcd").toString()+")";
			}
		}					 
		//取本地所属地
		@SuppressWarnings("unused")
		String localhp="";
		if(flag_shengting)
		localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '4300%' ";
		else
		localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"+local+"%' ";
		
		//高危地区id
		/*String dangerousId="";
		if(filter.get("dangerousId")!=null){
			if(!filter.get("dangerousId").toString().equals("")){
				dangerousId= "  and dangerousId = '"+filter.get("dangerousId").toString()+"'";
			}
		}*/


	/*	String sqlcount = " select  count(t.*)  from "
				+ "t("+Constant.SCS_PASS_TABLE+"::t.gcsj >='"
				+ filter.get("kssj").toString() + "' and t.gcsj <='"
				+ filter.get("jssj").toString() + "'" + " and KDBH like ('" + local
				+ "%') ) ";	*/
		
		//统计总过车数
	
		String tablename_count="  select count(1) from "
			+ " yp_passrec_ydcp t where t.gcsj >=to_date('"
			+ filter.get("kssj").toString() + "','yyyy-mm-dd hh24:mi:ss') and t.gcsj <=to_date('"
			+ filter.get("jssj").toString() + "','yyyy-mm-dd hh24:mi:ss')" + zcd;	
		
		
		int AllVehTotal =this.jdbcScsTemplate.queryForInt(tablename_count);
	
		//设置参数	
		//过境型参数		
		String gjx="3";
		if(filter.get("gjx")!=null){
			if(filter.get("gjx").toString().length()!=0){
				gjx=filter.get("gjx").toString();
			}
		}
		//候鸟型参数
		//int hnx=0;
		//本地型参数
		String bdx="6";
		if(filter.get("bdx")!=null){
			if(filter.get("bdx").toString().length()!=0){
				bdx=filter.get("bdx").toString();
			}
		}
		
		for(int i=0;i<list.size();i++){
			Map<String, Object> totalmap=list.get(i);
			totalmap.put("ALL_TOTAL", AllVehTotal);
		}
		//double present=anotherPlaceVehCount/Double.parseDouble(AllVehTotal);
		
		//写入表
	String xh=sdf.format(new Date());
	String condition= "开始时间:"+filter.get("kssj").toString()
		+"结束时间:"+filter.get("jssj").toString()
	    +"注册地:"+filter.get("zcd").toString()
	    +",过车次数:"+"过境型<="+gjx+" 候鸟型("+gjx+","+bdx+"] 本地型>"+bdx;
	    //+"高危地区:"+filter.get("gw").toString()
		;
		condition=condition.replace("'", "");
		String sql=null;
		int work=0;//成功记录数
		
		for(int i=0;i<list.size();i++){
			Map map=(Map) list.get(i);
			sql="insert into FRM_OTHERPLACE_ZCD (ZCD,GJXS,HNXS,BDXS,TOTAL,XH,TASKNAME,CONDISION,TI_KSSJ,TJ_JSSJ,ALL_TOTAL,TJ_GJX_RANGE,TJ_BDX_RANGE,LOCAL,BJTOTAL,DANGEROUSID,DANGEROUSCS,DANGEROUSMATCHCITY) values ("
				+ "'"+map.get("HP").toString()+"',"
				+ "'"+map.get("GJX").toString()+"',"
				+ "'"+map.get("HNX").toString()+"',"
				+ "'"+map.get("BDX").toString()+"',"
				+ "'"+map.get("TOTAL").toString()+"',"
				+ "'"+xh+"',"
				+ "'"+taskname+"',"
				+ "'"+condition+"',"
				+ "'"+filter.get("kssj").toString()+"',"
				+ "'"+filter.get("jssj").toString()+"',"
				+ "'"+map.get("ALL_TOTAL").toString()+"',"	
				+ "'"+gjx+"',"
				+ "'"+bdx+"',"
				+ "'"+filter.get("local").toString()+"',"
				//暂屏蔽高危神马鬼
				//+ "'"+pageCountResult.get("bjcount")+"',"
				//+ "'"+pageCountResult.get("DANGEROUSID")+"',"
				//+ "'"+pageCountResult.get("dangerousCs")+"',"
				//+ "'"+pageCountResult.get("dangerousMatchCity")+"'"
				+ "' ',"
				+ "' ',"
				+ "' ',"
				+ "' '"
				/*+ "'',"
				+ "'',"
				+ "'',"
				+ "''"*/
				+")";
			work+=this.jdbcTemplate.update(sql);
		}	
		pageCountResult=null;
		anotherPlaceVehCount=0l;
		if(work>0){
			return "success";
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public Map findtask(String xh,Map<String, Object> filter,String taskname) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map findZcdList(String xh, Map<String, Object> filter)
			throws Exception {
		return null;
	}

	
	/**
	 * 查询具体每种型的号牌统计页面
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryDetilList(String xh ,Map<String ,Object> filter,String sign) throws Exception {
		
		//根据任务序号获取任务条件
		String sql ="select * from  " 
			+	" frm_otherplace_zcd  " 
			+	" where xh = '"+xh+"' " 
			//+	" and rownum=1"
			;
		Map contision=null;
		List list = this.jdbcTemplate.queryForList(sql);
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
		List localhplist = jdbcTemplate.queryForList(localhp);
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
				type="  HAVING  COUNT(1) > "+contision.get("TJ_BDX_RANGE").toString()+"  ";
			}
			if(sign.equals("HNX")){
				type="  HAVING  COUNT(1) > "+contision.get("TJ_GJX_RANGE").toString()+" and  COUNT(1) <= "+contision.get("TJ_BDX_RANGE")+" ";				
			}
			if(sign.equals("GJX")){
				type="  HAVING  COUNT(1) <= "+contision.get("TJ_GJX_RANGE").toString()+"  ";
			}
		}
					
		String temp_sql = " select distinct hphm,hpzl,hpys,kdbh,gcsj from veh_passrec t where  " 
			+"  SUBSTRING(HPHM, 1, 1) in ("+zcd+")"
			+ " and KDBH like ('"+local
			+ "%') "
			+" and gcsj >= '"+contision.get("TI_KSSJ").toString()+"' "
		    +" and gcsj <= '"+contision.get("TJ_JSSJ").toString()+"' "
		    +localhplists
			//+"  "
			;
		String select_sql = " select t.hphm,t.hpys,t.hpzl,count(1) cs," +
					 " count(distinct kdbh) kks,count(distinct substr(kdbh,0,6)) xzqs,"+ 
					 " count(distinct to_char(gcsj,'yyyy-mm-dd')) as ts from ("+temp_sql+")t " +
					 " group by hphm,hpzl,hpys "+type+" order by count(1) desc ";
		Map<String, Object> map = this.findPageForMap(select_sql, Integer
				.parseInt(filter.get("page").toString()), Integer
				.parseInt(filter.get("rows").toString()));				
		return map;										
	}

	
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map queryForDetilList(Map filter){
		//根据任务序号获取任务条件
		String sql ="select * from  " 
			+	" frm_otherplace_zcd  " 
			+	" where xh = '"+filter.get("xh").toString()+"' " 
			+	" and rownum=1";
		Map contision=null;
		List list = this.jdbcTemplate.queryForList(sql);
		if (list.size() > 0) {
			contision = (Map) list.get(0);
		}
		
		//得到本地行政区划
		String local ="";
		if(contision.get("local")!=null){
			local=contision.get("local").toString();
			if("430000000000".equals(local)){
				local = local.substring(0, 2);
			}else{
				local = local.substring(0, 4);
			}
		}
		
		//取本地所属地
		String localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"+local+"%' ";
		String localhplists="";
		List localhplist = jdbcTemplate.queryForList(localhp);
		if(localhplist.size()>0){
		for(int i=0;i<localhplist.size();i++){
			Map map=(Map)localhplist.get(i);
			localhplists+="'"+map.get("HPHM").toString()+"',";
			
		}
		}
		if(localhplists.length()>0){
			localhplists=localhplists.substring(0, localhplists.length()-1);
		}
		String localhpstr = "";
		if(localhplists!=""){
		localhpstr = " AND SUBSTRING(hphm,1,2) NOT IN ("+localhplists+") ";
		}
		
		//得到注册地
		String zcd="''";
		zcd = filter.get("hphm").toString().substring(0,1);
		//分析函数去重
		String result =" select * from (select distinct rank()over(partition by hphm,hpzl,gcsj order by gcsj desc) mm,t.gcxh as GCXH,t.hphm as HPHM,t.kdbh as KDBH,substring(t.kdbh,1,6) as XZQH,t.fxbh as FXBH,t.cdbh as CDBH, GCSJ,t.hpzl as HPZL ,t.tp1 as TP1  from veh_passrec t where " 
			+"  SUBSTRING(HPHM, 1, 1) = '"+zcd+"'"
			/*+ " and KDBH like ('"
			+ local
			+ "%') "*/
			+" and t.gcsj > '"+contision.get("TI_KSSJ").toString()+"'"
		    +" and t.gcsj <= '"+contision.get("TJ_JSSJ").toString()+"'"
			//+" AND SUBSTRING(hphm,1,2) NOT IN ("+localhplists+") "
		    //+ localhpstr
			+" and hphm ='"+filter.get("hphm").toString()+"'"
			+" and hpzl ='"+filter.get("hpzl").toString()+"'"
			//+" and hpys ='"+filter.get("hpys").toString()+"'"
			+")t where mm=1  order by gcsj desc "
			;
		Map map =this.findPageForMap(result, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		
		return map;
	}
	
	
	/**
	 * 查询不同过车类型的号牌详情列表数据（高危地区）
	 */
	@SuppressWarnings("unchecked")
	public Map queryDangerousDetailList(String xh ,Map<String ,Object> filter,String sign){
	
	//根据任务序号获取任务条件
	String sql ="select * from  " 
		+	" frm_otherplace_zcd  " 
		+	" where xh = '"+xh+"' " 
		;
	Map contision=null;
	List list = this.jdbcTemplate.queryForList(sql);
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
		if("430000000000".equals(local)){
			local = local.substring(0, 2);
		}else{
			local = local.substring(0, 4);
		}
	}
	
	boolean flag_shengting=false;
	if(local.equals("43")){
		flag_shengting=true;
	}
	
	
	//取本地所属地
	String localhp="";
	if(flag_shengting)
	 localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '43%' ";
	else
	 localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"+local+"%' ";
	String localhplists="";
	List localhplist = jdbcTemplate.queryForList(localhp);
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
	
	//高危地区条件
	String dangerousCity="";
	if(filter.get("dangerousCity")!=null){
		if(!"".equals(filter.get("dangerousCity"))&&!"1".equals(filter.get("dangerousCity"))){
			dangerousCity=" AND SUBSTRING(hphm,1,2)  IN ('"+filter.get("dangerousCity")+"') ";
		}
	}
		
	Map map=null;	
	try{	
		String temp_sql = " select hphm,hpzl,hpys,count(*) as cs from ( select hphm,hpzl,hpys from veh_passrec  where  " 
			+"  SUBSTRING(HPHM, 1, 1) in ("+zcd+")"
			+ " and KDBH like ('"
			+ local
			+ "%') "
			+" and gcsj >='"+contision.get("TI_KSSJ").toString()+"' "
		    +" and gcsj <='"+contision.get("TJ_JSSJ").toString()+"' "
		    +localhplists
		    +dangerousCity			
			+" )t group by hphm,hpzl,hpys "
			;
		map = this.findPageForMap(temp_sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		}catch(Exception e){			
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 分页
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map queryDangerousDetailList(Map filter){
		String sessionid="";
		if(filter.get("sessionid")!=null){
			sessionid=filter.get("sessionid").toString();
		}
		String tablename_result=Constant.SCS_TEMP_PREFIX+sessionid;

		Map map=null;
		String sql2="select t.* from t("+tablename_result+") order t.cs:desc";
		map = this.findPageForMap(sql2, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	
	
	/**
	 * 分页
	 */
	@SuppressWarnings("unchecked")
	public Map queryDetilList(Map filter){
		String sessionid="";
		if(filter.get("sessionid")!=null){
			sessionid=filter.get("sessionid").toString();
		}
		String tablename_result=Constant.SCS_TEMP_PREFIX+sessionid;

		Map map=null;
		String sql2="select t.* from t("+tablename_result+") order t.cs:desc";
		map = this.findPageForMap(sql2, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	@SuppressWarnings("unchecked")
	public String sureDeleteTask(Map filter) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List cityTree() throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	public int insertDangerousArray(Map filter) throws Exception {
		return 0;
	}

	public Map queryDangerous(Map filter) throws Exception {
		return null;
	}

	public int deleteById(Map filter) throws Exception {
		return 0;
	}


	@Override
	public int getForDetilTotal(String xh, Map<String, Object> filter,
			String sign) throws Exception {
		String sql = getSql(xh, filter, sign);
		return super.getTotal(sql);
	}

	@Override
	public List<Map<String, Object>> queryDetilListExt(String xh,
			Map<String, Object> filter, String sign) throws Exception {
		String sql = getSql(xh, filter, sign);
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	public String getSql(String xh,Map<String, Object> filter, String sign) throws Exception{
		//根据任务序号获取任务条件
		String sql ="select * from  " 
			+	" frm_otherplace_zcd  " 
			+	" where xh = '"+xh+"' " 
			//+	" and rownum=1"
			;
		Map contision=null;
		List list = this.jdbcTemplate.queryForList(sql);
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
		List localhplist = jdbcTemplate.queryForList(localhp);
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
					
		String temp_sql = " select hphm,hpzl,COUNT (1) cs,COUNT (DISTINCT kkbh) kks, COUNT (DISTINCT gc_qx) xzqs,"
			+"COUNT (DISTINCT gc_rq) AS ts from yp_passrec_ydcp where " 
			+"  cl_sf in ("+zcd+")"
			+" and gcsj >= '"+contision.get("TI_KSSJ").toString()+"' "
		    +" and gcsj <= '"+contision.get("TJ_JSSJ").toString()+"' "
		    +" GROUP BY hphm,hpzl "
		    //+localhplists
			//+"  "
			;
		String select_sql = " select hphm,hpzl,cs,kks,xzqs,ts FROM ("+temp_sql+") c1 " +
					 " where"+type+" order by cs desc ";
		return select_sql;	
	}


	@Override
	public int getForDetilDangerousTotal(String xh ,Map<String ,Object> filter,String sign)	throws Exception {
		String sql = getDangerousSql2(xh, filter, sign);
		return super.getTotal(sql);
	}

	@Override
	public List<Map<String, Object>> queryDangerousDetailListExt(String xh ,Map<String ,Object> filter,String sign) throws Exception {
		String sql = getDangerousSql2(xh, filter, sign);
		List<Map<String, Object>> list = this.getPageDatas(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	public String getDangerousSql2(String xh ,Map<String ,Object> filter,String sign) throws Exception{
		//根据任务序号获取任务条件
		String sql ="select * from  " 
			+	" frm_otherplace_zcd  " 
			+	" where xh = '"+xh+"' " 
			;
		Map<String, Object> contision=null;
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		//去重注册地
		Set<String> set_zcd=new HashSet<String>();
		if (list.size() > 0) {
			contision = (Map<String, Object>) list.get(0);
			for(int i =0;i<list.size();i++){
				Map<String, Object> map=(Map<String, Object>)list.get(i);
				set_zcd.add(map.get("ZCD").toString().substring(0,1));
			}
		}
		
		//得到本地行政区划
		String local ="";
		if(contision.get("local")!=null){
			local=contision.get("local").toString();
			if("430000000000".equals(local)){
				local = local.substring(0, 2);
			}else{
				local = local.substring(0, 4);
			}
		}
		//得到高危次数
		String dangerousCs = "";
		if(contision.get("DANGEROUSCS")!=null){
			dangerousCs = " cs > "+contision.get("DANGEROUSCS").toString();
		}
		
		boolean flag_shengting=false;
		if(local.equals("43")){
			flag_shengting=true;
		}
		
		
		//取本地所属地
		String localhp="";
		if(flag_shengting)
		 localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '43%' ";
		else
		 localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"+local+"%' ";
		String localhplists="";
		List<Map<String, Object>> localhplist = jdbcTemplate.queryForList(localhp);
		for(int i=0;i<localhplist.size();i++){
			Map<String, Object> map=(Map<String, Object>)localhplist.get(i);
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
		
		//高危地区条件
		
		String dangerousCity="";
		if(filter.get("dangerousCity")!=null){
			if(!"".equals(filter.get("dangerousCity"))&&!"1".equals(filter.get("dangerousCity"))){
				dangerousCity=" AND SUBSTRING(hphm,1,2)  IN ('"+filter.get("dangerousCity")+"') ";
			}
		}
			
		String temp_sql = "select hphm,hpzl,hpys,cs FROM ( SELECT hphm,hpzl,hpys,COUNT (1) AS cs FROM yp_passrec_ydcp WHERE  " 
			+" cl_sf in ("+zcd+")"
			+" and gcsj >='"+contision.get("TI_KSSJ").toString()+"' "
		    +" and gcsj <='"+contision.get("TJ_JSSJ").toString()+"' "
		    +" GROUP BY hphm, hpzl, hpys ) c1 WHERE "
		    + dangerousCs
		    //+localhplists
		    //+dangerousCity			
			+" ORDER BY cs DESC "
			;
		return temp_sql;
	}
	public String getDangerousSql(String xh ,Map<String ,Object> filter,String sign) throws Exception{
		//根据任务序号获取任务条件
		String sql ="select * from  " 
			+	" frm_otherplace_zcd  " 
			+	" where xh = '"+xh+"' " 
			;
		Map<String, Object> contision=null;
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		//去重注册地
		Set<String> set_zcd=new HashSet<String>();
		if (list.size() > 0) {
			contision = list.get(0);
			for(int i =0;i<list.size();i++){
				Map<String, Object> map=list.get(i);
				set_zcd.add(map.get("ZCD").toString().substring(0,1));
			}
		}
		
		//得到本地行政区划
		String local ="";
		if(contision.get("local")!=null){
			local=contision.get("local").toString();
			if("430000000000".equals(local)){
				local = local.substring(0, 2);
			}else{
				local = local.substring(0, 4);
			}
		}
		
		boolean flag_shengting=false;
		if(local.equals("43")){
			flag_shengting=true;
		}
		
		
		//取本地所属地
		String localhp="";
		if(flag_shengting)
		 localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '43%' ";
		else
		 localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"+local+"%' ";
		String localhplists="";
		List<Map<String, Object>> localhplist = jdbcTemplate.queryForList(localhp);
		for(int i=0;i<localhplist.size();i++){
			Map<String, Object> map=localhplist.get(i);
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
		
		//高危地区条件
		String dangerousCity="";
		if(filter.get("dangerousCity")!=null){
			if(!"".equals(filter.get("dangerousCity"))&&!"1".equals(filter.get("dangerousCity"))){
				dangerousCity=" AND SUBSTRING(hphm,1,2)  IN ('"+filter.get("dangerousCity")+"') ";
			}
		}
			
		String temp_sql = " select hphm,hpzl,hpys,count(*) as cs from ( select hphm,hpzl,hpys from veh_passrec  where  " 
			+"  SUBSTRING(HPHM, 1, 1) in ("+zcd+")"
			+ " and KDBH like ('"
			+ local
			+ "%') "
			+" and gcsj >='"+contision.get("TI_KSSJ").toString()+"' "
		    +" and gcsj <='"+contision.get("TJ_JSSJ").toString()+"' "
		    +localhplists
		    +dangerousCity			
			+" )t group by hphm,hpzl,hpys "
			;
		return temp_sql;
	}
	
	public String getSql(Map<String, Object> filter) throws Exception{
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
			zcd="  cl_sf in ("+filter.get("zcd").toString()+")";
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
		List localhplist = jdbcTemplate.queryForList(localhp);
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
		+" from ( select hphm,hpzl,count(1) as total from yp_passrec_ydcp  where " 
		//+" KDBH like '"
		//+ local
		//+"%' "
		+zcd
		+" and gcsj >= '"+filter.get("kssj").toString()+"' "
	    +" and gcsj <= '"+filter.get("jssj").toString()+"' "
	    //+localhplists 	    
		+" group by hphm,hpzl )t "
		;
		String sql = " select substr(hphm, 0, 3) HP, COUNT (1) TOTAL," +
				 " sum(case when zt='gjx' then 1 else 0 end) GJX," +
				 " sum(case when zt='hnx' then 1 else 0 end) HNX," +
				 " sum(case when zt='bdx' then 1 else 0 end) BDX" +
				 " from ("+temp_sql+")c1 GROUP BY substr(hphm, 0, 3) order by substr(hphm, 0, 3) asc ";
		return  sql;
	}
	
	@Override
	public int getListTotal(Map<String, Object> filter) throws Exception {
		String sql = getSql(filter);
		return super.getTotal(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getListExt(Map<String, Object> filter)
			throws Exception {
		String sql = getSql(filter);
		List<Map<String, Object>> data = this.getPageDatas(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		if(data==null || data.size()==0){
			return null;
		}
		Map<String,Object> resultmap = new HashMap<String, Object>();
		resultmap.put("rows", data);
		/*int bjtotal=0;
        String dangerousMatchCity="";
        //高危地区城市
        String [] dangerousCity = null;
        if(filter.get("dangerousCity")!=null){
         dangerousCity= (String[]) filter.get("dangerousCity");
        }
        //查询定义高危地区次数
        int dangerousCs=1;
        int hpbjcount=0;
        if(filter.get("dangerousCs")!=null){
        	if(!filter.get("dangerousCs").toString().equals(""))
        	dangerousCs=Integer.parseInt(filter.get("dangerousCs").toString());
        }
        List<Map<String,Object>> list = (List<Map<String, Object>>)resultmap.get("rows");
		 //判断各地区的过车总量是否超过高危通行次数
		for(int i =0;i<list.size();i++){
			 Map<String,Object> map = list.get(i);	
			 if(Integer.parseInt(map.get("TOTAL").toString())>=dangerousCs){
				 if(dangerousCity!=null){
					 for(int j=0;j<dangerousCity.length;j++){				 
						 if(map.get("HP")!=null){
							 if(map.get("HP").toString().equals(dangerousCity[j])){
								 hpbjcount=Integer.parseInt(map.get("TOTAL").toString());
								 dangerousMatchCity+=map.get("HP").toString()+",";
								 break;
							 	}
						 	}
					 	}				
				 	}
			 	}
		 	bjtotal+=hpbjcount;
		}
		//放入预警总数
		resultmap.put("bjcount", bjtotal);
		//放入高危通行次数
		resultmap.put("dangerousCs", dangerousCs);
		resultmap.put("DANGEROUSID", filter.get("arrayid"));
		resultmap.put("dangerousMatchCity", dangerousMatchCity);
		//int rows = Integer.parseInt(map.get("total").toString());
		取全部的结果保存在内存**
		//pageCountResult=this.findPageForMap(sql,1,rows);
		//不能将数据放入缓存中，并发保存数据有问题
		//pageCountResult = resultmap;
*/		return  resultmap;
	}

}
