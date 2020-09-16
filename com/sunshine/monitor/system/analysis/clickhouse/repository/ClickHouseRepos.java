package com.sunshine.monitor.system.analysis.clickhouse.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.system.analysis.bean.CaseTouch;
import com.sunshine.monitor.system.analysis.bean.HideDayOutNightDayBean;
import com.sunshine.monitor.system.analysis.bean.NightCarTotal;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.clickhouse.bean.ClickHouseConstant;
import com.sunshine.monitor.system.analysis.clickhouse.jdbc.ClickHouseJDBC;
import com.sunshine.monitor.system.analysis.clickhouse.utils.ClickHouseSqlUtils;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

/**
 * 研判分析数据持久层(clickhouse)
 * @author quxiaol
 * @date 2020-08-20
 * 
 */
@Repository("clickHouseRepos")
public class ClickHouseRepos extends ClickHouseJDBC {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	@Qualifier("jdbcTemplate")
	protected JdbcTemplate jdbcOracleTemplate;
	
	/**
	 * 查询频繁进出列表数据
	 * @param filter
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> offenInOutByScsDbExt(Map<String, Object> filter, String sql)throws Exception {		
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	/**
	 * 查询轨迹数据
	 * @param filter
	 * @param consql
	 * @return
	 */
	public Map<String, Object> queryGjList(Map filter, String consql) {
		String sql = "select distinct concat(t.kkbh,'|',t.fxlx,'|',t.hphm,'|',t.hpzl,'|',toString(t.gcsj)) as GCXH,t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS,t.kkbh as KDBH,t.fxlx as FXBH,t.gcsj as GCSJ,t.tp1 as TP1 "
			+ " from " + ClickHouseConstant.PASS_TABLE_NAME + " t where 1=1 "+consql + " order by t.gcsj desc ";
		Map<String, Object> map = this.findPageForMap(sql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}
	
	/**
	 * 相当于  List&lt;Entity&gt; <br />
	 * Map key 为大写字母
	 */
	public List<Map<String,Object>> findList(String gcxh) {
		String sql = "select '" + gcxh + "' as GCXH,kkbh as KDBH,fxlx as FXBH,cdh as CDBH,'' as CDLX,hpzl as HPZL,gcsj as GCSJ,clsd as CLSD,hpys as HPYS,cllx as CLLX,hphm as HPHM,hphm as CWHPHM,hpys as CWHPYS,'' as HPYZ,csys as CSYS,'' as CLXS,clpp as CLPP,'' as XSZT,tp1 as TP1,tp2 as TP2,tp3 as TP3,gcsj as RKSJ from " + ClickHouseConstant.PASS_TABLE_NAME + " t where 1=1";
		if (StringUtils.isNotBlank(gcxh)) {
			String[] values = gcxh.split("\\|");
			String[] keys = {"kkbh","fxlx","hphm","hpzl","gcsj"};
			if (keys.length == values.length) {
				for (int i = 0; i < keys.length; i ++) {
					sql = sql + " and " + keys[i] + " = '" + values[i] + "'";
				}
			}
		}
		sql = sql + " limit 1";
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return list ;
	}
	
	/**
	 * 查询车辆关联分析数据
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findLinkForPageExt(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		//查询关联统计结果
		String sql = ClickHouseSqlUtils.getLinkedAnalysisSql(veh, filter);
		List<Map<String, Object>> list = this.getPageDatas(sql,Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	/**
	 * 统计车辆关联分析总数
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int getForLinkTotal(ScsVehPassrec veh, Map<String, Object> filter)
			throws Exception {
		String sql = ClickHouseSqlUtils.getLinkedAnalysisSql(veh, filter);
		return super.getTotal(sql);
	}
	
	/**
	 * 查询车辆关联分析详情
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findLinkDetailForPage(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		StringBuffer sb = new StringBuffer(" select concat(kkbh,'|',fxlx,'|',hphm,'|',hpzl,'|',toString(gcsj)) as gcxh,hphm,hpzl,hpys,kkbh as kdbh,fxlx as fxbh,cdh as cdbh,clsd,csys,clpp,cllx,tplj,tp1,tp2,tp3,gcsj from " + ClickHouseConstant.PASS_TABLE_NAME + " t ");
		sb.append(" where t.gcsj >= '").append(veh.getKssj()).append("'")
		  .append(" and  t.gcsj <= '").append(veh.getJssj()).append("'")
		  .append(" and t.hphm = '").append(veh.getHphm()).append("'")
		  .append(" and t.hpzl = '").append(veh.getHpzl()).append("'")
			.append(" and t.hpys = '").append(veh.getHpys()).append("'");
		
		if(veh.getKdbh()!=null&&!"".equals(veh.getKdbh())){
			String kdbh = "'"+veh.getKdbh().replace(",", "','")+"'";
			sb.append(" and t.kkbh in (").append(kdbh).append(")");
		}
		sb.append(" order by t.gcsj desc");
		
		Map<String,Object> map = this.findPageForMap(sb.toString(),
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()),ScsVehPassrec.class);
		return map;
	}
	
	/**
	 * 根据号牌号码和部分条件获取过车的轨迹视图
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryForLikePic(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		//条件整理
		String hpys="";
		if(veh.getHpys()!=null){
			hpys=" and hpys='"+veh.getHpys()+"'";
		}
		
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
				kdbh="and t.kkbh in ("+veh.getKdbh()+")";
			}
		}
		//执行查询语句
		String sql ="select concat(kkbh,'|',fxlx,'|',hphm,'|',hpzl,'|',toString(gcsj)) as GCXH,t.hphm as HPHM,t.kkbh as KDBH,t.xzqh as XZQH,t.fxlx as FXBH,t.cdh as CDBH,toString(t.gcsj) AS GCSJ,t.hpys as HPYS ,t.tp1 as TP1" 
		    +" from " + ClickHouseConstant.PASS_TABLE_NAME + " t where "
	        +" t.hphm = '"+veh.getHphm()+"'"
	        +" and t.gcsj >= '"+veh.kssj+"'"
		    +" and t.gcsj <= '"+veh.jssj+"'"
	        +kdbh
	        +hpys
		    //+csys
		    +hpzl 
		    + " order by gcsj desc";

		Map map = this.findPageForMap(sql,Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		
		return map;
	}
	
	/**
	 * 查询时空轨迹分析数据
	 * @param filter
	 * @param consql
	 * @param conditionArray
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
    public Map<String, Object> districtbyScsDb(Map filter, String consql, String[] conditionArray,String tablename) throws Exception {
		StringBuffer sd = new StringBuffer( " select t.hphm as HPHM,t.hpzl as HPZL,t.hpys as HPYS," +
						  "count(distinct substring(kdbh,1,6)) as XZQS,count(distinct kdbh) as KKS,count(distinct formatDateTime(gcsj, '%Y-%m-%d')) as TS,count(1) as CS from ( ");				
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
     * 查询时空轨迹分析的轨迹详情
     * @param filter
     * @param conditionArray
     * @return
     */
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
			List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sd.toString());
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("rows", list);
			return result;
		}
		
		Map<String, Object> map = this.findPageForMap(sd.toString(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return map;
	}
    
    /**
     * 查询区域碰撞分析列表数据
     * @param filter
     * @param consql
     * @param conditionArray
     * @param tablename
     * @param params
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> district2byScsDbExt(Map filter, String consql, String[] conditionArray,String tablename,Object[] params) throws Exception {
		String sql = ClickHouseSqlUtils.getDistrictSql(filter, consql,conditionArray,tablename);	
		long pstart = System.currentTimeMillis();
        
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		long pend = System.currentTimeMillis();
		log.info("区域碰撞:JCBKSQL-PAGE:(" + (pend-pstart) + "毫秒)" + sql);
		
		return list;
	}
    
    /**
     * 统计区域碰撞分析总数
     * @param filter
     * @param consql
     * @param conditionArray
     * @param tablename
     * @return
     * @throws Exception
     */
    public int getDistrictTotal2(Map filter, String consql, String[] conditionArray,String tablename)
    		throws Exception {
    	String sql = ClickHouseSqlUtils.getDistrictSql(filter, consql,conditionArray,tablename);
    	return super.getTotal(sql);
    }
    
    /**
     * 查询昼伏夜出数据（旧）
     * @param sessionId
     * @param groupId
     * @param pager
     * @param startTime
     * @param endTime
     * @param thresholds
     * @return
     */
    public List<NightCarTotal> nightCarTempTotalList(String sessionId,String groupId,
			Pager pager, String startTime, String endTime,  Integer thresholds, String kdbhs) throws Exception {
    	    
    	String baseSql = "select hphm as carNo,hpzl as licenseType,hpys as licenseColor,gccs as totalDay"
    			+ " from (select hphm,hpzl,hpys,max(hpys) as hpys,sum(gccs) as gccs,"
    			+ " sum(score) as score from (select hphm,hpzl,max(hpys) as hpys,"
    			+ " count(1) as gccs,toHour(gcsj) as gcsj_hour,case when toHour(gcsj) >= 6 "
    			+ " and toHour(gcsj) < 18 then -1 when toHour(gcsj) >= 18 "
    			+ " and toHour(gcsj) < 23 then 0 else 2 end as score "
    			+ " from " + ClickHouseConstant.PASS_TABLE_NAME
    			+ " where 1 = 1 ";
    			
    	if (StringUtils.isNotBlank(startTime)) {
    		baseSql = baseSql + " and gcsj >= '" + startTime.trim() + " 00:00:00'";
    	}
    	if (StringUtils.isNotBlank(endTime)) {
    		baseSql = baseSql + " and gcsj <= '" + endTime.trim() + " 23:59:59'";
    	}
    	if (StringUtils.isNotBlank(kdbhs)) {
    		String kdbh = "'"+kdbhs.replace(",", "','")+"'";
    		baseSql = baseSql + " and kkbh in (" + kdbh + ")";
    	}
    			
    	baseSql = baseSql + " group by hphm,hpzl,toHour(gcsj)) group by hphm,hpzl)"
    			+ " where score > 0 and gccs >= " + thresholds;
    	baseSql = baseSql + " order by gccs desc";
    	
    	Map<String,Object> map = this.findPageForMap(baseSql,
				pager.getPageNumber(),pager.getPageSize()
				,NightCarTotal.class);
    	
    	pager.setPageCount(Integer.parseInt(map.get("total").toString()));
    	List<NightCarTotal> list = (List<NightCarTotal>) map.get("rows");
		
    	return list;
	}
    
    /**
     * 查询昼伏夜出列表数据
     * @param filter
     * @param bean
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getDayNightListExt(Map<String, Object> filter, HideDayOutNightDayBean bean) throws Exception {
		String sql = ClickHouseSqlUtils.getDayNightSql(filter,bean);
		List<Map<String, Object>> list = this.getPageDatas(sql,Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}

    /**
     * 统计昼伏夜出总数
     * @param filter
     * @param bean
     * @return
     * @throws Exception
     */
	public int getDayNightTotal(Map<String, Object> filter,
			HideDayOutNightDayBean bean) throws Exception {
		String sql = ClickHouseSqlUtils.getDayNightSql(filter,bean);
		return super.getTotal(sql);
	}
	
	/**
	 * 查询轨迹列表（扩展）
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryGjPassrecListExt(String sql,
			Object[] params) throws Exception {
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = super.jdbcTemplate.queryForList(sql, params);
		long pend = System.currentTimeMillis();
		log.info("JCBKSQL-PAGE:(" + (pend-pstart) + "毫秒)" + sql);
		return list;
	}
	
	/**
	 * 查询轨迹总数（扩展）
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Integer queryGjPassrecListTotal(String sql, Object[] params)
			throws Exception {
		log.info("JCBKSQL-COUNT:" + sql);
		int result = 0;
		try{
			Object r = this.jdbcTemplate.queryForInt(sql, params);
			result = Integer.valueOf(r.toString());
		}catch(EmptyResultDataAccessException e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询模糊号牌分析列表数据
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryForLikeExt(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		String sql = ClickHouseSqlUtils.getLikeSql(veh, filter);
		List list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	/**
	 * 统计模糊号牌分析总数
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int getForLikeTotal(VehPassrec veh, Map<String, Object> filter) throws Exception{
		String sql = ClickHouseSqlUtils.getLikeSql(veh, filter);
		return super.getTotal(sql);
	}
	
	/**
	 * 查询模糊号牌最后一条记录
	 * @param veh
	 * @return
	 * @throws Exception
	 */
	public List queryForLikeLast(VehPassrec veh) throws Exception {
		//条件整理
		String kdbh="";
		if(veh.getKdbh()!=null){
			if(!veh.getKdbh().equals("''")){
				kdbh="and t.kkbh in ("+veh.getKdbh()+")";
			}
		}
		//获取该号牌最后一次过车时间
		String maxsql=
			" select max(t.gcsj) from  " + ClickHouseConstant.PASS_TABLE_NAME + " t where "
			+"  t.gcsj >= '"+veh.kssj+"'"
		    +" and t.gcsj <= '"+veh.jssj+"'"
	        +kdbh
	        +" and t.hphm='"+veh.getHphm()+"'"
	        +" ";
		String maxtime = this.jdbcTemplate.queryForObject(maxsql, String.class).toString();
		
		//查询最后一次过车详情
		String sql =" select concat(kkbh,'|',fxlx,'|',hphm,'|',hpzl,'|',toString(gcsj)) as GCXH,t.hphm as HPHM,t.kkbh as KDBH,t.hpzl as HPZL ,t.xzqh as XZQH,t.fxlx as FXBH,t.cdh as CDBH,toString(t.gcsj) as GCSJ,t.hpys as HPYS ,t.tp1 as TP1 "
			+" from " + ClickHouseConstant.PASS_TABLE_NAME + " t where  "
			+" t.hphm = '"+veh.getHphm()+"' "
			+" and t.gcsj = '"+maxtime+"'"			
			+ kdbh + " limit 1";

		List list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	/**
	 * 查询异地车辆分析列表数据
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAnotherPlaceListExt(Map<String, Object> filter)
			throws Exception {
		String sql = ClickHouseSqlUtils.getAnotherPlaceSql(filter);
		List<Map<String, Object>> data = this.getPageDatas(sql, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		if(data==null || data.size()==0){
			return null;
		}
		Map<String,Object> resultmap = new HashMap<String, Object>();
		resultmap.put("rows", data);

		return  resultmap;
	}
	
	/**
	 * 统计异地车辆分析总数
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public int getAnotherPlaceListTotal(Map<String, Object> filter) throws Exception {
		String sql = ClickHouseSqlUtils.getAnotherPlaceSql(filter);
		return super.getTotal(sql);
	}
	
	/**
	 * 保存异地车辆分析数据
	 * @param taskname
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public String saveZcd( String taskname, Map<String, Object> filter) throws Exception {
		//检索任务名是否重复
		List taskNameCheck = this.jdbcOracleTemplate.queryForList("select taskname from  FRM_OTHERPLACE_ZCD where taskname='"+taskname+"' and rownum=1");
		if(taskNameCheck.size()!=0){
			return "sameName";
		}
		String sqltemp = ClickHouseSqlUtils.getAnotherPlaceSql(filter);
		List<Map<String, Object>> list =this.getPageDatas(sqltemp, 1, 1000);
		
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
			zcd="  and province in ("+filter.get("zcd").toString()+")";
			}
		}	
		
		//取本地所属地
		@SuppressWarnings("unused")
		String localhp="";
		if(flag_shengting)
		localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '4300%' ";
		else
		localhp=" SELECT HPHM FROM frm_hphm WHERE dwdm LIKE '"+local+"%' ";
		
		//统计总过车数
		String tablename_count = "  select count(1) from "
			+ ClickHouseConstant.PASS_TABLE_NAME + " t where t.gcsj >= '"
			+ filter.get("kssj").toString() + "' and t.gcsj <= '"
			+ filter.get("jssj").toString() + "'" + zcd;	
		
		int AllVehTotal = this.jdbcTemplate.queryForInt(tablename_count);
	
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
		
		//写入表
	    String xh = sdf.format(new Date());
	    String condition = "开始时间:"+filter.get("kssj").toString()
		+"结束时间:"+filter.get("jssj").toString()
	    +"注册地:"+filter.get("zcd").toString()
	    +",过车次数:"+"过境型<="+gjx+" 候鸟型("+gjx+","+bdx+"] 本地型>"+bdx;
	    
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
				+ "' ',"
				+ "' ',"
				+ "' ',"
				+ "' '"
				+")";
			
			work += this.jdbcOracleTemplate.update(sql);
		}	
	    
		if(work>0){
			return "success";
		}
		return "";
	}
	
	/**
	 * 查询异地车详情列表
	 * @param xh
	 * @param filter
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryAnotherPlaceDetilList(String xh,
			Map<String, Object> filter, String sign) throws Exception {
		String sql = ClickHouseSqlUtils.getAnotherPlaceDetilSql(xh, filter, sign);
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	/**
	 * 获取异地车详情总数
	 * @param xh
	 * @param filter
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public int getAnotherPlaceDetilTotal(String xh, Map<String, Object> filter,
			String sign) throws Exception {
		String sql = ClickHouseSqlUtils.getAnotherPlaceDetilSql(xh, filter, sign);
		return super.getTotal(sql);
	}
	
	/**
	 * 查询异地车轨迹详情
	 * @param filter
	 * @return
	 */
	public Map queryAnotherPlaceDetil(Map filter){
		//根据任务序号获取任务条件
		String sql ="select * from  " 
			+	" frm_otherplace_zcd  " 
			+	" where xh = '"+filter.get("xh").toString()+"' " 
			+	" and rownum=1";
		Map contision=null;
		List list = this.jdbcOracleTemplate.queryForList(sql);
		if (list.size() > 0) {
			contision = (Map) list.get(0);
		}
		
		//分析函数去重
		String result =" select concat(t.kkbh,'|',t.fxlx,'|',t.hphm,'|',t.hpzl,'|',toString(t.gcsj)) as GCXH,t.hphm as HPHM,t.kkbh as KDBH,xzqh as XZQH,t.fxlx as FXBH,t.cdh as CDBH,toString(t.gcsj) as GCSJ,t.hpzl as HPZL ,t.tp1 as TP1 from " + ClickHouseConstant.PASS_TABLE_NAME + " t where " 
			+" 1=1 "
			+" and t.gcsj >= '"+contision.get("TI_KSSJ").toString()+"'"
		    +" and t.gcsj <= '"+contision.get("TJ_JSSJ").toString()+"'"
			+" and hphm = '"+filter.get("hphm").toString()+"'"
			+" and hpzl = '"+filter.get("hpzl").toString()+"'"
			+" order by gcsj desc ";
		
		Map map = this.findPageForMap(result, Integer.parseInt(filter.get("page").toString()), Integer.parseInt(filter.get("rows").toString()));
		
		return map;
	}
	
	/**
	 * 查询流量统计--小时
	 * @param tableName
	 * @param condition
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public <T> List<? extends Object> queryTotalFlowPeerHour(String tableName,
			String condition, Object... args) throws Exception {
		String sql = "select formatDateTime(gcsj,'%Y-%m-%d') as rp, hour as rt, count(1) as total from " + ClickHouseConstant.PASS_TABLE_NAME + " where "
				  + condition + " group by formatDateTime(gcsj,'%Y-%m-%d'), hour";
		return this.jdbcTemplate.queryForList(sql, args);
	}
	
	/**
	 * 查询流量统计--天
	 * @param tableName
	 * @param condition
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public <T> List<? extends Object> queryTotalFlowPeerDay(String tableName,
			String condition, Object... args) throws Exception {
		StringBuffer sql = new StringBuffer("select formatDateTime(gcsj,'%Y-%m') as rp , day as  rt, count(1) as total from " + ClickHouseConstant.PASS_TABLE_NAME + " where ");
				     sql.append(condition);
				     sql.append(" group by formatDateTime(gcsj,'%Y-%m'), day");
		return this.jdbcTemplate.queryForList(sql.toString(), args);
	}
/**
 * 查询案件分析两个以上碰撞分析
 * @param filter
 * @param list
 * @return
 * @throws Exception
 */
	public Map<String,Object> getCaseTouchAnaiysisDatagrid(Map<String,Object> filter,List<CaseTouch> list)throws Exception{
		
		StringBuffer sb = new StringBuffer();
//		sb.append("SELECT * FROM (");
		sb.append(" SELECT hphm,hpzl,count(1) AS cs,count(city) AS qys,count(kkbh) AS kks  ");
		sb.append(" from  "+ ClickHouseConstant.PASS_TABLE_NAME +" ");
		sb.append("	  WHERE 1=1 ");
		int i = 0;
//		for (CaseTouch caseTouch : list) {
//			if(i==0) 
//				sb.append(" and ");
//			else sb.append(" or ");
//			sb.append(" (gcsj >= '").append(caseTouch.getFasjcz()).append("'");
//			sb.append(" AND gcsj <= '").append(caseTouch.getFasjzz()).append("'");
//			sb.append(" AND city = '").append(caseTouch.getFadd_qx()).append("')");
//			i++;
//		}
		sb.append(" GROUP BY hphm,hpzl ");
//		sb.append(" GROUP BY hphm,hpzl) a1");
		sb.append(" ORDER BY cs DESC ");

	Map<String,Object> map = new HashMap<String,Object>();
	try {		
		map = this.findPageForMap(sb.toString(), Integer.parseInt(filter
				.get("page").toString()), Integer.parseInt(filter.get(
				"rows").toString()));
	} catch (Exception e) {
		e.printStackTrace();
	}
	return map;
}
}