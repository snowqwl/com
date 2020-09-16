package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.analysis.dao.SuitLicenseDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Repository("suitLicenseDao")
public class SuitLicenseDaoImpl extends BaseDaoImpl implements SuitLicenseDao{

	@Override
	public boolean exitSuitLicense(Map<String, Object> filter) {
		int total = 0;
		String sql = "select count(*) as total from JM_ZYK_SUIT where 1=1";
		if(filter.get("hphm")!=null&&!"".equals(filter.get("hphm").toString())){
			sql += " and hphm = '"+filter.get("hphm").toString()+"'";
		}
		if(filter.get("hpzl")!=null&&!"".equals(filter.get("hpzl").toString())){
			sql += " and hpzl = '"+filter.get("hpzl").toString()+"'";
		}
		List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql);
		if(result.size()>0){
			Map<String, Object> map = result.get(0);
			try {
				total = Integer.parseInt(map.get("TOTAL").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return total>0?true:false;
	}
	
	public void insertSuitLicense2(Map sl) {
		String sql = " insert into JM_ZYK_SUIT(hphm,hpzl,hpys,cllx,csys,clpp,clwx,tp1,tp2,tp3,bdlj,rksj,xxly,xzqh,czr,by1,by2,by3)";
			sql += " values('";
			sql += sl.get("hphm")!=null?sl.get("hphm").toString():"";
			sql +=  "','";
			sql += sl.get("hpzl")!=null?sl.get("hpzl").toString():"";
			sql += "','";
			sql += sl.get("hpys")!=null?sl.get("hpys").toString():"";
			sql += "','";
			sql += sl.get("cllx")!=null?sl.get("cllx").toString():"";
			sql += "','";
			sql += sl.get("csys")!=null?sl.get("csys").toString():"";
			sql += "','";
			sql += sl.get("clpp")!=null?sl.get("clpp").toString():"";
			sql += "','";
			sql += sl.get("clwx")!=null?sl.get("clwx").toString():"";
			sql += "','";
			sql += sl.get("tp1")!=null?sl.get("tp1").toString():"";
			sql += "','";
			sql += sl.get("tp2")!=null?sl.get("tp2").toString():"";
			sql += "','";
			sql += sl.get("tp3")!=null?sl.get("tp3").toString():"";
			sql += "','";
			sql += sl.get("bdlj")!=null?sl.get("bdlj").toString():"";
			sql += "',sysdate";
			sql += ",'";
			sql += sl.get("xxly")!=null?sl.get("xxly").toString():"";
			sql += "','";
			sql += sl.get("xzqh")!=null?sl.get("xzqh").toString():"";
			sql += "','";
			sql += sl.get("czr")!=null?sl.get("czr").toString():"";
			sql += "','";
			sql += sl.get("by1")!=null?sl.get("by1").toString():"";
			sql += "','";
			sql += sl.get("by2")!=null?sl.get("by2").toString():"";
			sql += "','";
			sql += sl.get("by3")!=null?sl.get("by3").toString():"";
			sql += "')";
		try {
			this.jdbcTemplate.execute(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	public String getSuitSql(Map<String, Object> filter, VehPassrec veh){
		String sql = "select hphm,hpzl,hpys,rksj,clpp , tp1 as gctp1 ,tp2  as gctp2 , tp3  as gctp3 from jm_zyk_suit  ";
		sql += " where 1=1 ";
		if(veh.getHphm()!=null && !"".equals(veh.getHphm())) {
			sql += " and hphm='"+veh.getHphm()+"'";
		}
		if(veh.getHpzl()!=null && !"".equals(veh.getHpzl())) {
			sql += " and hpzl='"+veh.getHpzl()+"'";
		}
		if(veh.getHpys()!=null && !"".equals(veh.getHpys())) {
			sql += " and hpys='"+veh.getHpys()+"'";
		}
		if (veh.getKssj() != null && !"".equals(veh.getKssj())) {
			sql += " and rksj > to_date('" + veh.getKssj() + "','yyyy-mm-dd,hh24:mi:ss')";
		}
		if (veh.getJssj() != null && !"".equals(veh.getJssj())) {
			sql += " and rksj <= to_date('" + veh.getJssj()+ "','yyyy-mm-dd,hh24:mi:ss')";
		}
		sql += " order by rksj desc ";
		return sql;
	}
	
	@Override
	public int getSuitTotal(Map<String, Object> filter, VehPassrec veh)
			throws Exception {
		String sql = this.getSuitSql(filter, veh);
		return super.getTotal(sql);
	}

	@Override
	public List<Map<String, Object>> querySuitListExt(Map<String, Object> filter, VehPassrec veh)
			throws Exception {
		String sql = getSuitSql(filter, veh);
		List<Map<String, Object>> list = this.getPageDatas(sql,
				Integer.parseInt(filter.get("page").toString()),
				Integer.parseInt(filter.get("rows").toString()));
		return list;
	}
	
}
