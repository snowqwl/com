package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.bean.CaseTouch;
import com.sunshine.monitor.system.analysis.dao.CaseTouchSCSDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("caseTouchSCSDao")
public  class CaseTouchSCSDaoImpl extends ScsBaseDaoImpl implements CaseTouchSCSDao{
	
	protected Logger debugLogger =  LoggerFactory.getLogger(CaseTouchSCSDaoImpl.class);
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Map<String,Object> getCaseTouchAnaiysisDatagrid(Map<String,Object> filter,List<CaseTouch> list)throws Exception{
				
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT * FROM (");
			sb.append(" SELECT HPHM,HPZL,COUNT (1) AS cs,COUNT (DISTINCT gc_qx) AS qys,COUNT (DISTINCT kkbh) AS kks");
			sb.append("	FROM yp_passrec_ydcp ");
			sb.append("	WHERE 1=1 ");
			int i = 0;
			for (CaseTouch caseTouch : list) {
				if(i==0) 
					sb.append(" and ");
				else sb.append(" or ");
				sb.append(" (gcsj >= '").append(caseTouch.getFasjcz()).append("'");
				sb.append(" AND gcsj <= '").append(caseTouch.getFasjzz()).append("'");
				sb.append(" AND gc_qx = '").append(caseTouch.getFadd_qx()).append("')");
				i++;
			}
			sb.append(" GROUP BY hphm,hpzl) a1");
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
	
	//根据号牌号码和部分条件获取过车的轨迹视图
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCaseTouchVehList(VehPassrec veh,Map<String, Object> filter) throws Exception {
		List<CaseTouch> list = (List<CaseTouch>)filter.get("list");
		StringBuffer sb = new StringBuffer();		
		for (int i = 0;i<list.size();i++) {				
			if(i==0){
				sb.append(" (gcsj>='"+list.get(i).getFasjcz().toString()+"' "
				    +" and gcsj<='"+list.get(i).getFasjzz().toString()+"' "
				    +" and kdbh like '"+list.get(i).getFadd_qx()+"%')");
			 }else{
				sb.append(" or (");
				sb.append(" gcsj>= '"+list.get(i).getFasjcz().toString()+"' "
				    +" and  gcsj<= '"+list.get(i).getFasjzz().toString()+"' "
				    +" and  kdbh like '"+list.get(i).getFadd_qx()+"%')"
				);
			}
		}
		
		
		//执行查询语句
		String sql =" select * from (select gcxh as GCXH,hphm as HPHM,kdbh as KDBH,substring(kdbh,1,6) as XZQH,fxbh as FXBH,to_char(gcsj,'yyyy-mm-dd hh24:mi:ss') as GCSJ,hpys as HPYS ,tp1 as TP1,hpzl " 
		    +" from  veh_passrec where 1=1 and "
	        + sb.toString() 
	        +" )t where hphm='"+veh.getHphm()+"' and hpzl='"+veh.getHpzl()+"' order by GCSJ desc ";
		Map map = new HashMap();
		try {		
			map = this.findPageForMap(sql, Integer.parseInt(filter
					.get("page").toString()), Integer.parseInt(filter.get(
					"rows").toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public List<Map<String,Object>> getCaseTouchList(String fxtj)throws Exception{
		StringBuffer sb = new StringBuffer();
		JSONObject json = JSONObject.fromObject(fxtj);		
		Iterator iter = json.keys();
		String conditions = "";
		while(iter.hasNext()){
			String key = iter.next().toString();
			if("conditions".equals(key)){
				conditions = json.getString(key);
			}
		}
		JSONArray arry  = JSONArray.fromObject(conditions);
		JSONArray temparray = new JSONArray();
		for(int i = 0;i<arry.size();i++){
			if(arry.getJSONObject(i)!=null){
			 	JSONObject jsonObject = arry.getJSONObject(i);
			 	JSONObject jsonObject1 = new JSONObject();
			 	Iterator iter1 = jsonObject.keys();
	            while(iter1.hasNext()){
	            String keystr = iter1.next().toString();
	            String key	= keystr.toLowerCase();
	            String value = jsonObject.getString(keystr);
	            jsonObject1.put(key, value);
	            }
	            temparray.add(jsonObject1);
			}
		}
		List<CaseTouch> list = temparray.toList(temparray, CaseTouch.class);
		for (int i = 0;i<list.size();i++) {				
			if(i==0){
				sb.append(" (gcsj>='"+list.get(i).getFasjcz().toString()+"' "
				    +" and gcsj<='"+list.get(i).getFasjzz().toString()+"' "
				    +" and kdbh like '"+list.get(i).getFadd_qx()+"%')");
			 }else{
				sb.append(" or (");
				sb.append(" gcsj>= '"+list.get(i).getFasjcz().toString()+"' "
				    +" and  gcsj<= '"+list.get(i).getFasjzz().toString()+"' "
				    +" and  kdbh like '"+list.get(i).getFadd_qx()+"%')"
				);
			}
		}
		String sql =" select hphm,hpzl,hpys,count(hphm) as cs ,count(distinct xzqh) as xzqs,count(distinct kdbh) as kks,max(gcsj) as gcsj  from (select gcxh as GCXH,hphm as HPHM,kdbh as KDBH,substring(kdbh,1,6) as XZQH,fxbh as FXBH, GCSJ,hpys as HPYS ,tp1 as TP1,hpzl " 
		    +" from  veh_passrec where 1=1 and "
	        + sb.toString() 
	        +" )t group by hphm,hpzl,hpys";
		
		return this.jdbcScsTemplate.queryForList(sql);
	}
	
	
	public Map<String,Object> getCaseForList(Map<String,Object> conditions)throws Exception{
 		StringBuffer sql = new StringBuffer(50);
		sql.append("SELECT * FROM (");
		sql.append(" SELECT a1.anjianbianhao AS ajbh,a1.anjianmingcheng AS ajmc,a1.anjianlaiyuan AS ajly," +
				" a1.anjianleibie AS ajlx,a2.faanshijiankaishi AS fasjcz,a2.faanshijianjieshu AS fasjzz," +
				" a2.jianyaoanqing AS zyaq,a2.faxiandidiandm AS fadd_qx	FROM jm_zyk_case a1,jm_zyk_jcjxx a2");
		sql.append(" WHERE a1.anjianbianhao = a2.anjianbianhao");
		
//		sql.append(" AND a2.faanshijiankaishi >= '20160801000000'");
//		sql.append(" AND a2.faanshijianjieshu <= '20161019235959'");
		//开始时间
		if (conditions.get("afkssj") != null && !"".equals(conditions.get("afkssj"))) {
			String value = (String) conditions.get("afkssj");
			value = value.replace("-", "").replace(":", "").replace(" ","");
			sql.append(" and a2.faanshijiankaishi >= ").append("'" + value + "' ");
		}
		//结束时间
		if (conditions.get("afjssj") != null && !"".equals(conditions.get("afjssj"))) {
			String value = (String) conditions.get("afjssj");
			value = value.replace("-", "").replace(":", "").replace(" ","");
			sql.append(" and a2.faanshijianjieshu <= ").append(" '" + value + "'");
		}
		//案件来源
		if (conditions.get("ajly") != null && !"".equals(conditions.get("ajly"))) {
			sql.append(" and a1.anjianlaiyuan = '").append(conditions.get("ajly")).append("' ");
		}
		//案件类型
		if (conditions.get("ajlx") != null && !"".equals(conditions.get("ajlx"))) {
			sql.append(" and a1.anjianleibie='").append(conditions.get("ajlx")).append("'");
		}	
		//案件名称
		if (conditions.get("ajmc") != null && !"".equals(conditions.get("ajmc"))) {
			sql.append(" and a1.anjianmingcheng like '%").append(conditions.get("ajmc")).append("%'");
		}
		//案件区域
		if (conditions.get("slfaqh") != null && !"".equals(conditions.get("slfaqh"))) {
			sql.append(" and a2.faxiandidiandm = '").append(conditions.get("slfaqh")).append("'");
		}
//		sql.append(" AND a2.faxiandidiandm = '431002'");
		sql.append(" ORDER BY fasjcz DESC");
		sql.append(" ) b");
//		sql.append(" select t1.ajbh,ajmc,ajly,ajlx,fasjcz,fasjzz,zyaq,fadd_qx from ")
//		   .append("(select anjianmingcheng as ajmc,anjianlaiyuan as ajly,anjianleibie as ajlx,anjianbianhao as ajbh from jm_zyk_case_jbxx where anjianbianhao is not null)t1, ")
//		   .append("(select anjianbianhao as ajbh,faanshijiankaishi  as fasjcz,faanshijianjieshu as fasjzz,jianyaoanqing as zyaq," )
//		   .append(" substr(jiejingdanweiid, 0, 7) as fadd_qx from jm_zyk_case_jcjxx where 1=1 ");
//		Set<Entry<String, Object>> set = conditions.entrySet();
//		Iterator<Entry<String, Object>> it = set.iterator();
//		String ajlxstr = "";
//		while (it.hasNext()) {
//			Entry<String, Object> entry = it.next();
//			String key = entry.getKey();
//			String value = (String) entry.getValue();
//			boolean isFilter = "sort".equalsIgnoreCase(key)
//					|| "order".equalsIgnoreCase(key)
//					|| "page".equalsIgnoreCase(key)
//					|| "rows".equalsIgnoreCase(key);
//			if (!isFilter) {
//				if (key.equals("afkssj")) {
//					value = value.replace("-", "").replace(":", "").replace(" ","");
//					sql.append(" and faanshijiankaishi >= ").append(
//							"'" + value + "' ");
//				} else if (key.equals("afjssj")) {
//					value = value.replace("-", "").replace(":", "").replace(" ","");
//					sql.append(" and faanshijianjieshu <= ").append(
//							" '" + value + "'");
//				}else if (key.equals("slfaqh")) {
//					sql.append(" and substr(jiejingdanweiid, 0, 7) = '").append(value).append("' ");
//							
//				}else if (key.equals("ajly")) {
//					sql.append(" and anjianlaiyuan = '").append(value).append("' ");
//				}
//				else if (key.equals("ajlx")) {
//					ajlxstr = value;
//				}
//				else {
//					sql.append(" and ");
//					sql.append(key);
//					sql.append(" = '").append(value).append("'");
//				}
//			}
//		}
//		sql.append(" and faanshijiankaishi is not null and faanshijianjieshu is not null and anjianbianhao is not null)t2 where t1.ajbh=t2.ajbh  ");
//		if(ajlxstr!=""){
//			sql.append(" and ajlx='").append(ajlxstr).append("'");
//		}
//		sql.append(" order by ");
//		sql.append(conditions.get("sort"));
//		sql.append(" ");
//		sql.append(conditions.get("order"));
		Map<String, Object> map = this.findPageForMap(sql.toString(), Integer
				.parseInt(conditions.get("page").toString()), Integer
				.parseInt(conditions.get("rows").toString()));
		return map;
	}
	
	public List getCaseDm(String ywxtmc,String dmlb,String dmmc) throws Exception{
		String sql = "select dm, dmmc from jm_zyk_case_dm where 1=1   ";
		if(dmlb!=""){
			sql+=" and dmlb like '%"+dmlb+"%' ";
		}
		if(ywxtmc!=""){
			sql+=" and ywxtmc like '%"+ywxtmc+"%' ";
		}
		if(dmmc!=""){
			sql+=" and dmmc like '%"+dmmc+"%' ";
		}
		return this.jdbcScsTemplate.queryForList(sql);
	}
	
	
	
}
