package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.dao.LinkedAnalysisDao;

@Repository("linkedDao")
public class LinkedAnalysisDaoImpl extends ScsBaseDaoImpl implements
		LinkedAnalysisDao {
	
    //private static final String LINKTEMP = Constant.SCS_TEMP_PREFIX+"_link_";
    //模糊查询结果列表临时表
    private static final String BLURTEMP = Constant.SCS_TEMP_PREFIX+"_link_blur_";
    //天云星机动车库临时表（目前使用oracle机动车库）
    private static final String VEHTEMP = Constant.SCS_TEMP_PREFIX+"_link_veh_";
    
    private static final String RESULTTEMP = Constant.SCS_TEMP_PREFIX+"_link_result_";
      
	public Map<String, Object> findLinkForPage(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		
		//StringBuffer sb = new StringBuffer(" select gcxh,kdbh,fxbh,cdbh,hpzl,gcsj,clsd,hpys,cllx,hphm,cwhphm,cwhpys,csys,tp1,rksj from veh_passrec t ");
		StringBuffer sb = new StringBuffer(" select gcxh,kdbh,fxbh,hpzl,gcsj,hpys,cllx,hphm,csys,tp1 from veh_passrec t ");
		sb.append(" where t.gcsj > '").append(veh.getKssj()).append("'")
		  .append(" and  t.gcsj <= '").append(veh.getJssj()).append("'");
		if(veh.getHphm()!=null&&veh.getHphm().length()>0)
			sb.append(" and t.hphm like '").append(veh.getHphm()).append("'");
		if(veh.getKdbh()!=null&&veh.getKdbh().length()>2)
			sb.append(" and t.kdbh in (").append(veh.getKdbh()).append(")");
		if(veh.getHpys()!=null&&veh.getHpys().length()>0)
			sb.append(" and t.hpys = '").append(veh.getHpys()).append("'");
		if(veh.getCsys()!=null&&veh.getCsys().length()>0)
			sb.append(" and t.csys = '").append(veh.getCsys()).append("'");
		if(veh.getHpzl()!=null&&veh.getHpzl().length()>0)
			sb.append(" and t.hpzl = '").append(veh.getHpzl()).append("'");
		if(veh.getCllx()!=null&&veh.getCllx().length()>0)
			sb.append(" and t.cllx = '").append(veh.getCllx()).append("'");
		//sb.append(" and t.hphm not in (").append(Constant.SCS_NO_PLATE).append(")");				
		
		//查询关联统计结果
		StringBuffer sql = new StringBuffer();
		sql.append(" select hphm,hpys,hpzl,"
				+ "count(gcxh) as cs from ( ").append(sb).append(")  x group by hphm,hpys,hpzl order by cs desc");
		
		Map<String,Object> map = this.findPageForMap(sql.toString(),
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return map;
	}

	/**
	 * 创建模糊查询临时表
	 */
	private  void createBlurTemp(ScsVehPassrec veh,String sessionId) throws Exception {
		String tablename = BLURTEMP+sessionId;
		StringBuffer sb = new StringBuffer(" select gcxh,kdbh,fxbh,cdbh,cdlx,hpzl,gcsj,clsd,hpys,cllx,hphm,cwhphm,cwhpys,csys,tp1,rksj from  "+Constant.SCS_PASS_TABLE+" t ");
		sb.append(" where t.gcsj > '").append(veh.getKssj()).append("'")
		  .append(" and  t.gcsj <= '").append(veh.getJssj()).append("'");
		if(veh.getHphm()!=null&&veh.getHphm().length()>0)
			sb.append(" and t.hphm like '").append(veh.getHphm()).append("'");
		if(veh.getKdbh()!=null&&veh.getKdbh().length()>2)
			sb.append(" and t.kdbh in (").append(veh.getKdbh()).append(")");
		if(veh.getHpys()!=null&&veh.getHpys().length()>2)
			sb.append(" and t.hpys = '").append(veh.getHpys()).append("'");
		if(veh.getCsys()!=null&&veh.getCsys().length()>2)
			sb.append(" and t.csys = '").append(veh.getCsys()).append("'");
		if(veh.getHpzl()!=null&&veh.getHpzl().length()>2)
			sb.append(" and t.hpzl = '").append(veh.getHpzl()).append("'");
		if(veh.getCllx()!=null&&veh.getCllx().length()>2)
			sb.append(" and t.cllx = '").append(veh.getCllx()).append("'");
		sb.append(" and t.hphm not in (").append(Constant.SCS_NO_PLATE).append(")");
		//创建临时表
	    this.createTemp(sb.toString(), tablename);
		this.jdbcScsTemplate.update("create index ct1_index_link"+sessionId+" on "+tablename+"(hphm,hpzl,hpys)");

	    /**
	    //预处理
	    System.out.println("start"+sdf.format(new Date()));
		System.out.println("模糊查询临时表预处理");		
	    StringBuffer preprocess = new StringBuffer(" preprocess ");
	    preprocess.append(" drop table if exists ").append(fulltable).append(";")
	              .append(" create table ").append(fulltable).append(" like ").append(tablename).append(";")
	              .append(" define tmp0 full into ").append(fulltable).append(" {")
	              .append(" select t.* from t(").append(tablename).append(")")
	              .append("};")
	              .append(" end preprocess ")
	              .append(" select count(t.*) from t(").append(fulltable).append(")");
	    this.jdbcScsTemplate.queryForList(preprocess.toString());
	    System.out.println("end"+sdf.format(new Date()));
	    **/
	}

	/**
	 * 根据模糊车牌查询天云星机动车库（目前使用oracle机动车库）
	 */
	private void createVehinfoTemp(ScsVehPassrec veh,String sessionId) throws Exception {
		String tablename = VEHTEMP+sessionId;
		String fulltable = VEHTEMP+sessionId+"_full";
		StringBuffer sb = new StringBuffer(" select hphm,hpzl,fzjg from vehicle where ");
        sb.append(" hphm like '").append(veh.getHphm().substring(1,veh.getHphm().length())).append("'")
          .append(" and fzjg = '").append(veh.getHphm().substring(0, 2)).append("'");
        if(veh.getCsys()!=null&&veh.getCsys().length()>0)
        	sb.append(" and t.csys='").append(veh.getCsys()).append("'");
        if(veh.getCllx()!=null&&veh.getCllx().length()>0)
        	sb.append(" and t.cllx='").append(veh.getCllx()).append("'");
        //创建临时表
		this.createTemp(sb.toString(),tablename);
		
	    //预处理
	    StringBuffer preprocess = new StringBuffer(" preprocess ");
	    preprocess.append(" drop table if exists ").append(fulltable).append(";")
	              .append(" create table ").append(fulltable).append(" like ").append(tablename).append(";")
	              .append(" define tmp0 full into ").append(fulltable).append(" {")
	              .append(" select t.* from t(").append(tablename).append(")")
	              .append("};")
	              .append(" end preprocess ")
	              .append(" select count(t.*) from t(").append(fulltable).append(")");
	    this.jdbcScsTemplate.queryForList(preprocess.toString());
	}

	public Map<String, Object> findLinkDetailForPage(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		StringBuffer sb = new StringBuffer(" select t.* from  "+Constant.GP_PASS_TABLE+" t ");
		sb.append(" where t.gcsj >= '").append(veh.getKssj()).append("'")
		  .append(" and  t.gcsj <= '").append(veh.getJssj()).append("'")
		  .append(" and t.hphm = '").append(veh.getHphm()).append("'")
		  .append(" and t.hpzl = '").append(veh.getHpzl()).append("'")
			.append(" and t.hpys = '").append(veh.getHpys()).append("'");
		
		if(veh.getKdbh()!=null&&!"".equals(veh.getKdbh())){
			String kdbh = "'"+veh.getKdbh().replace(",", "','")+"'";
			sb.append(" and t.kdbh in (").append(kdbh).append(")");
		}
		sb.append(" order by t.gcsj desc");
		
		Map<String,Object> map = this.findPageForMap(sb.toString(),
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()),ScsVehPassrec.class);
		return map;
	}
	
	public List<Map<String,Object>> getLinkList(String sessionId)throws Exception{
	    String tablename =  RESULTTEMP+sessionId;
		StringBuffer sb = new StringBuffer();
		sb.append("select t.* from t(").append(tablename).append(") limit 100");
		return this.jdbcScsTemplate.queryForList(sb.toString());
	}

	@Override
	public List<Map<String, Object>> findLinkForPageExt(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		//查询关联统计结果
		String sql = getSql(veh, filter);
		List<Map<String, Object>> list = this.getPageDatas(sql,Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
	public String getSql(ScsVehPassrec veh, Map<String, Object> filter) throws Exception{
		// 条件整理
		StringBuffer sb = new StringBuffer(" select gcxh,kdbh,fxbh,hpzl,gcsj,hpys,cllx,hphm,csys,tp1 from veh_passrec t ");
		sb.append(" where t.gcsj >= '").append(veh.getKssj()).append("'")
		  .append(" and  t.gcsj <= '").append(veh.getJssj()).append("'");
		if(veh.getHphm()!=null&&veh.getHphm().length()>0)
			sb.append(" and t.hphm like '").append(veh.getHphm()).append("'");
		if(veh.getKdbh()!=null&&veh.getKdbh().length()>2)
			sb.append(" and t.kdbh in (").append(veh.getKdbh()).append(")");
		if(veh.getHpys()!=null&&veh.getHpys().length()>0)
			sb.append(" and t.hpys = '").append(veh.getHpys()).append("'");
		if(veh.getCsys()!=null&&veh.getCsys().length()>0)
			sb.append(" and t.csys = '").append(veh.getCsys()).append("'");
		if(veh.getHpzl()!=null&&veh.getHpzl().length()>0)
			sb.append(" and t.hpzl = '").append(veh.getHpzl()).append("'");
		if(veh.getCllx()!=null&&veh.getCllx().length()>0)
			sb.append(" and t.cllx = '").append(veh.getCllx()).append("'");
		StringBuffer sql = new StringBuffer();
		sql.append(" select hphm,hpys,hpzl,"
				+ "count(gcxh) as cs from ( ").append(sb).append(")  x group by hphm,hpys,hpzl order by cs desc");
		return sql.toString();
	}

	@Override
	public int getForLinkTotal(ScsVehPassrec veh, Map<String, Object> filter)
			throws Exception {
		String sql = getSql(veh, filter);
		return super.getTotal(sql);
	}

}
