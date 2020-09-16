package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.dao.PeerVehiclesDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("peerDao")
public class PeerVehiclesDaoImpl extends ScsBaseDaoImpl implements
		PeerVehiclesDao {
	//轨迹临时表前缀
	private  final String CONTRAILTEMP=Constant.SCS_TEMP_PREFIX+"_contrail_";
	//同行车临时表前缀
	private  final String PEERTEMP=Constant.SCS_TEMP_PREFIX+"_peer_";
	//同行次数临时表前缀
	private  final String CSTEMP=Constant.SCS_TEMP_PREFIX+"_peercs_";
	//同行行政区划数临时表前缀
	private  final String XZQHTEMP=Constant.SCS_TEMP_PREFIX+"_peerxzqh_";
	//同行卡口数临时表前缀
	private  final String KKTEMP=Constant.SCS_TEMP_PREFIX+"_peerkk_";
	//同行天数临时表前缀
	private  final String TSTEMP=Constant.SCS_TEMP_PREFIX+"_peerts_";
	//同行车数临时表前缀
	private  final String TXCSTEMP=Constant.SCS_TEMP_PREFIX+"_peertxcs_";
	//同行统计结果临时表前缀
	private  final String RESULTTEMP=Constant.SCS_TEMP_PREFIX+"_peerresult_";

	
	//同行分析
	public Map<String, Object> queryForPeers(ScsVehPassrec veh,Map<String,Object> filter) throws Exception {
		Map<String,Object> map = null;
		String tempsql = "";
		String condition = getCondition(veh);
		
		
		String gaptime = String.valueOf((Integer.parseInt(veh.getGaptime())*60));	
		tempsql = " SELECT d.hphm,d.hpys,d.hpzl,count(d.hphm) cs,count(d.hphm) txcs,COUNT(distinct d.kdbh) kks,COUNT(distinct extract(day from d.gcsj)) ts,COUNT(distinct substr(d.kdbh, 0, 6)) xzqs FROM(select distinct a.* from "+ 
				  " (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh FROM veh_passrec )a where exists "+
				  " (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh FROM veh_passrec b WHERE "+condition+" and "+ 
				  " a.gcsj >= b.gcsj - INTERVAL '"+gaptime+"' MINUTE AND a.gcsj <= b.gcsj + INTERVAL '"+gaptime+"' MINUTE AND " +
				  " a.kdbh = b.kdbh AND a.hphm <> b.hphm ))d GROUP BY d.hphm, d.hpys, d.hpzl order by count(d.hphm) desc ";    
           
        map = this.findPageForMap(tempsql,
        		                 Integer.parseInt(filter.get("page").toString()),
        		                 Integer.parseInt(filter.get("rows").toString()));
		return map;
	}
	
	public String getSql(ScsVehPassrec veh,Map<String,Object> filter) throws Exception{
		String tempsql = "";
		String condition = getCondition(veh);
		
		String gaptime = String.valueOf((Integer.parseInt(veh.getGaptime())*60));	
		tempsql = " SELECT d.hphm,d.hpys,d.hpzl,count(d.hphm) cs,count(d.hphm) txcs,COUNT(distinct d.kdbh) kks,COUNT(distinct extract(day from d.gcsj)) ts,COUNT(distinct substr(d.kdbh, 0, 6)) xzqs FROM(select distinct a.* from "+ 
				  " (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh FROM veh_passrec )a where exists "+
				  " (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh FROM veh_passrec b WHERE 1=1 and "+condition+" and "+ 
				  " a.gcsj >= b.gcsj - INTERVAL '"+gaptime+"' SECOND AND a.gcsj <= b.gcsj + INTERVAL '"+gaptime+"' SECOND AND " +
				  " a.kdbh = b.kdbh AND a.hphm <> b.hphm ))d GROUP BY d.hphm, d.hpys, d.hpzl order by count(d.hphm) desc ";    
		return tempsql;
	}
	
	public List<Map<String, Object>> queryForPeersExt(ScsVehPassrec veh,Map<String,Object> filter) throws Exception {
		
        String tempsql = getSql(veh,filter);
		List<Map<String, Object>> list = this.getPageDatas(tempsql,
        		                 Integer.parseInt(filter.get("page").toString()),
        		                 Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	public int queryForPeersTotal(ScsVehPassrec veh,Map<String,Object> filter) throws Exception {
		int total = 0;
		String tempsql = getSql(veh,filter);
		total = this.getTotal(tempsql);
		return total;
	}

	public Map<String, Object> queryForContrail(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		StringBuffer createSql = new StringBuffer();		
		String condition = getCondition(veh);
			
			createSql = new StringBuffer(" select  distinct gcxh,kdbh,fxbh,hphm,gcsj,tp1,hpzl,hpys,'0' as checked from veh_passrec c where ");
		    createSql.append(condition);			
			createSql.append(" and gcxh in (select min(gcxh) from veh_passrec where ").append(condition).append(" group by hphm,hpzl,gcsj,kdbh)");
		StringBuffer sql = new StringBuffer("select t.* from ( ");
		sql.append(createSql);
		sql.append(" ) t order by gcsj desc ");
	    Map<String,Object> result = this.findPageForMap(sql.toString(),
				Integer.parseInt(filter.get("page").toString()), Integer
						.parseInt(filter.get("rows").toString()));
		return result;
	}
	
	public String getSqlForContrail(ScsVehPassrec veh,Map<String,Object> filter) throws Exception{
		StringBuffer createSql = new StringBuffer();		
		String condition = getCondition(veh);
			
			createSql = new StringBuffer(" select  distinct gcxh,kdbh,fxbh,hphm,gcsj,tp1,hpzl,hpys,'0' as checked from veh_passrec c where ");
		    createSql.append(condition);			
			createSql.append(" and gcxh in (select min(gcxh) from veh_passrec where ").append(condition).append(" group by hphm,hpzl,gcsj,kdbh)");
		StringBuffer sql = new StringBuffer("select t.* from ( ");
		sql.append(createSql);
		sql.append(" ) t order by gcsj desc ");
		return sql.toString();
	}
	
	/**
	 * 查询目标车辆过车轨迹(不计总数)
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryForContrailExt(ScsVehPassrec veh,Map<String,Object> filter) throws Exception {
		String sql = getSqlForContrail(veh,filter);
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()), Integer
						.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	/**
	 * 查询目标车辆过车轨迹总计数
	 * @param veh
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer queryForContrailTotal(ScsVehPassrec veh,Map<String,Object> filter) throws Exception {
		Integer total = 0;
		String sql = getSqlForContrail(veh,filter);
		total = this.getTotal(sql);
		return total;
	}
	
	public List getContrailList(String fxtj) throws Exception {
		JSONObject json = JSONObject.fromObject(fxtj);
		JSONObject veh_json = new JSONObject();
		Iterator iter = json.keys();
		ScsVehPassrec sc = new ScsVehPassrec();
		while(iter.hasNext()){
			String key = iter.next().toString();
			if(key.equals("condition")){
				sc.setCondition(json.getString("condition"));
			}
			if(key.equals("kssj")){
				sc.setKssj(json.getString("kssj"));
			}
			if(key.equals("jssj")){
				sc.setJssj(json.getString("jssj"));
			}
			if(key.equals("kdbh")){
				sc.setKdbh(json.getString("kdbh"));
			}
			if(key.equals("fxbh")){
				sc.setFxbh(json.getString("fxbh"));
			}
		}
		StringBuffer createSql = new StringBuffer();		
		String condition = getCondition(sc);			
			createSql = new StringBuffer(" select  distinct gcxh,kdbh,fxbh,hphm,gcsj,tp1,hpzl,hpys,'0' as checked from veh_passrec c where 1=1 ");
		    createSql.append(condition);			
			createSql.append(" and gcxh in (select min(gcxh) from veh_passrec where 1 = 1 ").append(condition).append(" group by hphm,hpzl,gcsj,kdbh)");
		StringBuffer sql = new StringBuffer("select t.* from ( ");
		sql.append(createSql);
		sql.append(" ) t order by gcsj desc ");	
		return this.jdbcScsTemplate.queryForList(sql.toString());
	}
	
	private String getCondition(ScsVehPassrec veh) {
		JSONArray array = JSONArray.fromObject(veh.getCondition());
		List<ScsVehPassrec> list = JSONArray.toList(array, ScsVehPassrec.class);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			ScsVehPassrec scs = list.get(i);
			if (scs != null) {
				sb.append("(hphm='").append(scs.getHphm()).append("'");
				if (scs.getHpys() != null && !"".equals(scs.getHpys())) {
					sb.append(" and hpys='").append(scs.getHpys()).append("'");
				}
				if (scs.getHpzl() != null && !"".equals(scs.getHpzl())) {
					sb.append(" and hpzl='").append(scs.getHpzl()).append("'");
					//sb.append(" and cllx='").append(scs.getCllx()).append("'");
				}
				if (scs.getCsys() != null && !"".equals(scs.getCsys())) {
					sb.append(" and csys='").append(scs.getCsys()).append("'");
				}
				sb.append(" and gcsj >'" + veh.getKssj() + "'");
				sb.append(" and gcsj <='" + veh.getJssj() + "'");
				if (veh.getKdbh() != null && !"".equals(veh.getKdbh()))
					sb.append(" and kdbh in (" + veh.getKdbh() + ")");
				if (veh.getFxbh() != null && !"".equals(veh.getFxbh()))
					sb.append(" and fxbh in (" + veh.getFxbh() + ")");
				sb.append(")");
				if (i != (list.size() - 1)) {
					sb.append(" or ");
				}
			}
		}
		return sb.toString();
	}


/*	public Map<String, Object> queryPeerDetail(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		String gaptime = String.valueOf((Integer.parseInt(veh.getGaptime())*60));
        StringBuffer sb = new StringBuffer();
        String condition = getCondition(veh);
        sb.append(" select distinct hphm,hpys,hpzl,gcsj,kdbh,fxbh,mbhp,mbsj,mbzl from (select a.hphm,a.hpys,a.hpzl,a.gcsj,a.kdbh,a.fxbh,a.tp1,b.hphm as mbhp,b.gcsj as mbsj,b.hpzl as mbzl,b.tp1 as mbtp from ")
	      .append(" (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh,tp1 FROM veh_passrec )a ,")  
	      .append(" (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh,tp1 FROM veh_passrec  WHERE "+condition+" )b where ")
          .append("  a.gcsj >= b.gcsj - INTERVAL '"+gaptime+"' MINUTE AND A.gcsj <= b.gcsj + INTERVAL '"+gaptime+"' MINUTE AND  a.kdbh = b.kdbh AND a.hphm <> b.hphm )c where hphm='")
          .append(veh.getHphm()).append("' and hpzl='").append(veh.getHpzl()).append("'");
        return this.findPageForMap(sb.toString(),Integer.parseInt(filter.get("page").toString()),
                Integer.parseInt(filter.get("rows").toString()));
	}*/
	
	public Map<String, Object> queryPeerDetail(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		String gaptime = String.valueOf((Integer.parseInt(veh.getGaptime())*60));
        StringBuffer sb = new StringBuffer();
        String condition = getCondition(veh);// a1.hphm as zcphm, a1.gcsj as zcgcsj
        sb.append(" select distinct a2.* from (select gcsj,kdbh from veh_passrec where "+condition+")a1,")
        .append(" (select gcxh, gcsj, hphm, hpzl, hpys, kdbh, fxbh, tp1 from veh_passrec where hpys = '"+veh.getHpys()+"' and hphm = '"+veh.getHphm()+"' and hpzl = '"+veh.getHpzl()+"')a2")
        .append(" where a2.kdbh=a1.kdbh and a2.gcsj >= a1.gcsj - INTERVAL '"+gaptime+"' MINUTE and a2.gcsj <= a1.gcsj + INTERVAL '"+gaptime+"' MINUTE order by a2.gcsj desc");
        return this.findPageForMap(sb.toString(),Integer.parseInt(filter.get("page").toString()),
                Integer.parseInt(filter.get("rows").toString()));
	}
	
	public Map<String, Object> queryPeerCsDetail(String sql, Map<String, Object> filter) throws Exception{
		return this.findPageForMap(sql, Integer.parseInt(filter.get("page").toString()),
                Integer.parseInt(filter.get("rows").toString()));
	}
	
	public Map<String, Object> queryPeerCsDetail(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		String gaptime = String.valueOf((Integer.parseInt(veh.getGaptime())*60));
        StringBuffer sb = new StringBuffer();
        String condition = getCondition(veh);
        sb.append(" select distinct hphm,hpys,hpzl,gcsj,kdbh,fxbh,mbhp,mbsj,mbzl from (select a.hphm,a.hpys,a.hpzl,a.gcsj,a.kdbh,a.fxbh,a.tp1,b.hphm as mbhp,b.gcsj as mbsj,b.hpzl as mbzl,b.tp1 as mbtp from ")
	      .append(" (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh,tp1 FROM veh_passrec )a ,")  
	      .append(" (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh,tp1 FROM veh_passrec  WHERE "+condition+" )b where ")
          .append("  a.gcsj >= b.gcsj - INTERVAL '"+gaptime+"' MINUTE AND A.gcsj <= b.gcsj + INTERVAL '"+gaptime+"' MINUTE AND  a.kdbh = b.kdbh AND a.hphm <> b.hphm )c where hphm='")
          .append(veh.getHphm()).append("' and hpzl='").append(veh.getHpzl()).append("'");
        return this.findPageForMap(sb.toString(),Integer.parseInt(filter.get("page").toString()),
                Integer.parseInt(filter.get("rows").toString()));
	}
	
	public Map<String, Object> queryPeerDetailForMap(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
	
        StringBuffer sb = new StringBuffer();
        sb.append(" select t.* from ")
          .append(" veh_passrec ")
          .append(" t where ")
          .append(" hphm='").append(veh.getHphm())
          .append("' and hpzl='").append(veh.getHpzl())
          .append("' ");
        return this.findPageForMap(sb.toString(),Integer.parseInt(filter.get("page").toString()),
                Integer.parseInt(filter.get("rows").toString()));
	}


	public int updateCheck(ScsVehPassrec veh,String sessionId)throws Exception{
		String contrailTab = CONTRAILTEMP+sessionId+"_full";
		StringBuffer sb = new StringBuffer();
		sb.append(" update t(").append(contrailTab)
		  .append("::gcxh ='")
		  .append(veh.getGcxh())
		  .append("') set t.checked = '").append(veh.getChecked()).append("'");
		return this.jdbcScsTemplate.update(sb.toString());
	}

	public List<Map<String, Object>> getPeerList(String fxtj)
			throws Exception {
		JSONObject json = JSONObject.fromObject(fxtj);
		JSONObject veh_json = new JSONObject();
		Iterator iter = json.keys();
		ScsVehPassrec sc = new ScsVehPassrec();
		while(iter.hasNext()){
			String key = iter.next().toString();
			if(key.equals("condition")){
				sc.setCondition(json.getString("condition"));
			}
			if(key.equals("kssj")){
				sc.setKssj(json.getString("kssj"));
			}
			if(key.equals("jssj")){
				sc.setJssj(json.getString("jssj"));
			}
			if(key.equals("kdbh")){
				sc.setKdbh(json.getString("kdbh"));
			}
			if(key.equals("fxbh")){
				sc.setFxbh(json.getString("fxbh"));
			}
			if(key.equals("gaptime")){
				sc.setGaptime(json.getString("gaptime"));
			}
		}
		String tempsql = "";
		String condition = getCondition(sc);
		String gaptime = "";
		StringBuffer sb = new StringBuffer();		
		if(sc.getGaptime()!=null){
			gaptime = String.valueOf((Integer.parseInt(sc.getGaptime())*60));
		}else{
		    gaptime = "300";
		}					
		sb.append(" select distinct hphm,hpys,hpzl,gcsj,kdbh,fxbh,mbhp,mbsj,mbzl from (select a.hphm,a.hpys,a.hpzl,a.gcsj,a.kdbh,a.fxbh,a.tp1,b.hphm as mbhp,b.gcsj as mbsj,b.hpzl as mbzl,b.tp1 as mbtp from ")
	      .append(" (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh,tp1 FROM veh_passrec )a ,")  
	      .append(" (SELECT gcsj,hphm,hpzl,hpys,kdbh,fxbh,tp1 FROM veh_passrec  WHERE "+condition+" )b where ")
          .append("  a.gcsj >= b.gcsj - INTERVAL '"+gaptime+"' MINUTE AND A.gcsj <= b.gcsj + INTERVAL '"+gaptime+"' MINUTE AND  a.kdbh = b.kdbh AND a.hphm <> b.hphm )");       
		return this.jdbcScsTemplate.queryForList(tempsql);
	}

}
