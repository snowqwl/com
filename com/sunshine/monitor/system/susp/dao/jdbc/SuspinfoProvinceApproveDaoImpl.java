package com.sunshine.monitor.system.susp.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoProvinceApproveDao;

@Repository("suspinfoProvinceApproveDao")
public class SuspinfoProvinceApproveDaoImpl extends BaseDaoImpl implements
SuspinfoProvinceApproveDao {

	public Map<String, Object> getSusinfoApproves(Map filter, VehSuspinfo info,
			String glbm) throws Exception {

		String tmpSql = "";
		if (info.getBkdl() != null && info.getBkdl().length() > 0) {
			tmpSql = tmpSql + " and bkdl='" + info.getBkdl() + "'";
		}
		if (info.getBklb() != null && info.getBklb().length() > 0) {
			tmpSql = tmpSql + " and bklb='" + info.getBklb() + "'";
		}
		if (info.getHphm() != null && info.getHphm().length() > 0) {
			tmpSql = tmpSql + " and hphm like '%" + info.getHphm() + "%'";
		}
		if (info.getHpzl() != null && info.getHpzl().length() > 0) {
			tmpSql = tmpSql + " and hpzl='" + info.getHpzl() + "'";
		}
		if (info.getKssj() != null && info.getKssj().length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + info.getKssj()
					+ "', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if (info.getJssj() != null && info.getJssj().length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + info.getJssj()
					+ "', 'yyyy-mm-dd hh24:mi:ss')";
		}

		if (StringUtils.isBlank(info.getKssj())
				&& StringUtils.isBlank(info.getJssj())) {
			tmpSql += "  and bksj >= sysdate - 365  ";
		}

		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm='" + glbm + "')";
		}
		//增加地市联动待审批布控信息
		//tmpSql = "Select * from VEH_SUSPINFO Where YWZT='12' and XXLY='0' "
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='12' and (XXLY='0'OR XXLY='2') and bkfwlx = '3' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql,
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	public Map<String, Object> getSusinfoApprovesOverTime(Map filter,
			VehSuspinfo info, String glbm) throws Exception {

		String tmpSql = "";
		if (info.getBkdl() != null && info.getBkdl().length() > 0) {
			tmpSql = tmpSql + " and bkdl='" + info.getBkdl() + "'";
		}
		if (info.getBklb() != null && info.getBklb().length() > 0) {
			tmpSql = tmpSql + " and bklb='" + info.getBklb() + "'";
		}
		if (info.getHphm() != null && info.getHphm().length() > 0) {
			tmpSql = tmpSql + " and hphm like '%" + info.getHphm() + "%'";
		}
		if (info.getHpzl() != null && info.getHpzl().length() > 0) {
			tmpSql = tmpSql + " and hpzl='" + info.getHpzl() + "'";
		}
		if (info.getKssj() != null && info.getKssj().length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + info.getKssj()
					+ "', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if (info.getJssj() != null && info.getJssj().length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + info.getJssj()
					+ "', 'yyyy-mm-dd hh24:mi:ss')";
		}

		if (StringUtils.isBlank(info.getKssj())
				&& StringUtils.isBlank(info.getJssj())) {
			tmpSql += "  and bksj >= sysdate - 365  ";
		}

		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm='" + glbm + "')";
		}
		tmpSql += " and bkxh in ( select bkxh from audit_approve where bzw='1' and (sysdate-czsj)*24*3600 >7200)";
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='12' and XXLY='0' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql,
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	
	 public String getBkshrmc(String bkxh)throws Exception{
		 String bkshrmc ="";
		 String sql = " SELECT czrmc FROM Audit_Approve  WHERE bkxh = '"+bkxh+"' AND bzw='1'";
		 List<Map<String,Object>> suslist = this.jdbcTemplate.queryForList(sql);
		 if(suslist.size()>0){
			 bkshrmc = suslist.get(0).get("czrmc").toString();
		 }
		 return bkshrmc;
	 }
	
	
}
