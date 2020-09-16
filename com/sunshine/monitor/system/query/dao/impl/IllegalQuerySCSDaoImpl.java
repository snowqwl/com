package com.sunshine.monitor.system.query.dao.impl;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.analysis.dao.IllegalSCSDao;
import com.sunshine.monitor.system.query.dao.IllegalQuerySCSDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("illegalQuerySCSDao")
public class IllegalQuerySCSDaoImpl extends ScsBaseDaoImpl implements IllegalQuerySCSDao{
	
	private String getSqlconForCity(String parmp,String citys){
		StringBuffer sb = new StringBuffer("");
		if(StringUtils.isNotBlank(citys)){
			String[]cityList = citys.split(",");
			for(String city : cityList){
				if( sb.length() > 2 ){
					sb.append(" or ").append(parmp).append(" like '").append(city).append("%'  ");
				}else{
					sb.append("   ").append(parmp).append(" like '").append(city).append("%'  ");
				}
			}
			
		}
		
		return sb.toString();
	}
	@Override
	public Map getMapForIntegrateTraffic(Map filter, VehPassrec info,
			String citys, String wflxTab) throws Exception {
		StringBuffer sql = new StringBuffer("select to_char(wfsj::TIMESTAMP, 'yyyy-mm-dd hh24:mi:ss') as wfsj,dzwzid,"
				+ " cjjg,cjjgmc,clfl,hpzl,hphm,fzjg,wfdd,wfdz,wfxw,fkje,cljgmc,cljg,clsj,wfbh,dsr,zsxxdz,zsxzqh,"
				+ " jdsbh,jdcsyr,lrr,zqmj,to_char(lrsj::TIMESTAMP, 'yyyy-mm-dd hh24:mi:ss') lrsj"
				+ " FROM jm_zyk_illegal  where 1=1 "); 
		
		
		if (StringUtils.isNotBlank(info.getHphm())) {
			sql.append(" and hphm = '").append(URLDecoder.decode( info.getHphm(),"UTF-8")).append("' ");
		}
		
		//if(StringUtils.isNotBlank(citys) && !"T_AP_VIO_FORCE".equals(wflxTab)){
		if(StringUtils.isNotBlank(citys)){
			sql.append("  and  (").append(getSqlconForCity("cjjg",citys)).append(")  ") ;
		}
		
		if (StringUtils.isNotBlank(info.getHpzl())) {
			sql.append("  and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		String temp = null;
		String dateTemp = "yyyy-mm-dd";
		String minTemp = " hh24:mi:ss";
		if (StringUtils.isNotBlank(info.getKssj())) {
			if (info.getKssj().length() > 13)
				temp = dateTemp + minTemp;
			else
				temp = dateTemp;
			
			sql.append(" and wfsj >= to_date('").append(info.getKssj()).append("','").append(temp).append("')  ");
		}

		if (StringUtils.isNotBlank(info.getJssj())) {
			if (info.getJssj().length() > 13)
				temp = dateTemp + minTemp;
			else
				temp = dateTemp;
			
			sql.append(" and wfsj <= to_date('").append(info.getJssj()).append("','").append(temp).append("')  ");
		}
		
		sql.append(" order by wfsj desc"); 
		return this.findPageForMap(sql.toString(), Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	@Override
	public List<Map<String, Object>> getViolationForWfbh(String dzwzId)
			throws Exception {
		String sql = "select wfsj,dzwzid,clsj,"
				+ " cjjg,cjjgmc,clfl,hpzl,hphm,fzjg,wfdd,wfdz,wfxw,fkje,cljgmc,cljg,wfbh,dsr,zsxxdz,zsxzqh,"
				+ " jdsbh,jdcsyr,lrr,zqmj,lrsj"
				+ " FROM jm_zyk_illegal   where dzwzid = '"+dzwzId+"'";
		return this.jdbcScsTemplate.queryForList(sql);
	}
	
	@Override
	public Map<String, Object> getViolationDetail(CarKey car,
			Map<String, Object> filter) throws Exception {
		 StringBuffer condition = new StringBuffer();
	        condition.append(" where hphm ='").append(car.getCarNo()).append("' and hpzl ='").append(car.getCarType()).append("'");
	      
	        StringBuffer sql = new StringBuffer("select wfsj,dzwzid,"
					+ " cjjg,cjjgmc,clfl,hpzl,hphm,fzjg,wfdd,wfdz,wfxw,fkje,cljgmc,cljg,clsj,wfbh,dsr,zsxxdz,zsxzqh,"
					+ " jdsbh,jdcsyr,lrr,zqmj,lrsj"
					+ " FROM jm_zyk_illegal  ").append(condition).append(" order by wfsj desc");
	        
			return this.findPageForMap(sql.toString(), Integer.parseInt(filter.get("curPage").toString()),
					Integer.parseInt(filter.get("pageSize").toString()));
	}
 }
