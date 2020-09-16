package com.sunshine.monitor.system.susp.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;

@Repository("suspinfoAuditDao")
public class SuspInfoAuditDaoImpl extends BaseDaoImpl  implements SuspInfoAuditDao {

	public Map findSuspinfoAuditForMap(Map filter, VehSuspinfo info,String glbm)
			throws Exception {
		StringBuffer sql = new StringBuffer("select v.* ");
		sql.append("from VEH_SUSPINFO v ");
		sql.append("  where YWZT = '11'  and XXLY = '0'  ");
		
		if (StringUtils.isNotBlank(info.getBkfwlx())){
			sql.append(" and BKFWLX = '").append(info.getBkfwlx()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBkdl())){
			sql.append("  and bkdl = '").append(info.getBkdl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBklb())){
			sql.append(" and bklb = '").append(info.getBklb()).append("'  ");
		}
		
		if(StringUtils.isNotBlank(info.getHpzl())){
			sql.append(" and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHphm())){
			sql.append(" and hphm like '%").append(info.getHphm()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(glbm)){
			sql.append(" and bkjg in (Select xjjg from frm_prefecture Where dwdm='").append(glbm).append("')");
		}
		
		if(StringUtils.isNotBlank(info.getKssj()))
			sql.append(" and bksj >=to_date('").append(info.getKssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		
		if(StringUtils.isNotBlank(info.getJssj()))
			sql.append(" and bksj <=to_date('").append(info.getJssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			sql.append("  and bksj >= sysdate - 365  ");
		}
		
		sql.append("  order by bksj desc ");
		
		return this.getSelf().findPageForMap(sql.toString(), 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	
	
	public Map findSuspinfoAuditOverTimeForMap(Map filter, VehSuspinfo info, String glbm)
			throws Exception {
		StringBuffer sql = new StringBuffer("select v.* ");
		sql.append("from VEH_SUSPINFO v ");
		sql.append("  where YWZT = '11'  and XXLY = '0'  ");

		sql.append(" and bkdl='3'" );

		if (StringUtils.isNotBlank(info.getBklb())) {
			sql.append(" and bklb = '").append(info.getBklb()).append("'  ");
		}

		if (StringUtils.isNotBlank(info.getHpzl())) {
			sql.append(" and hpzl = '").append(info.getHpzl()).append("' ");
		}

		if (StringUtils.isNotBlank(info.getHphm())) {
			sql.append(" and hphm like '%").append(info.getHphm())
					.append("%' ");
		}

		if (StringUtils.isNotBlank(glbm)) {
			sql.append(" and bkjg = '").append(glbm).append("' ");
		}

		if (StringUtils.isNotBlank(info.getKssj()))
			sql.append(" and bksj >=to_date('").append(info.getKssj()).append(
					"', 'yyyy-mm-dd hh24:mi:ss') ");

		if (StringUtils.isNotBlank(info.getJssj()))
			sql.append(" and bksj <=to_date('").append(info.getJssj()).append(
					"', 'yyyy-mm-dd hh24:mi:ss') ");

		if (StringUtils.isBlank(info.getKssj())
				&& StringUtils.isBlank(info.getJssj())) {
			sql.append("  and bksj >= sysdate - 365  ");
		}
		sql.append(" and (sysdate-bksj)*24*3600>7200");
		sql.append("  order by bksj desc ");

		return this.getSelf().findPageForMap(sql.toString(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
	}

	//保存审核记录
	public int insertAuditApprove(AuditApprove info)throws Exception{
		
		StringBuffer sb = new StringBuffer("INSERT into audit_approve ");
		sb.append("(xh, bkxh, czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,czrjh,czrmc) values( '");
		sb.append(info.getCzrdw()).append("' || seq_audit_xh.nextval,'")
		.append(info.getBkxh()).append("','").append(info.getCzr()).append("','")
		.append(info.getCzrdw()).append("','").append(info.getCzrdwmc()).append("',sysdate,'")
		.append(info.getCzjg()).append("','").append(info.getMs()).append("','").append(info.getBzw()).append("','")
		.append(info.getCzrjh()).append("','").append(info.getCzrmc()).append("')");
		
		int i= this.jdbcTemplate.update(sb.toString());
		return i;
	}
	
	//更新布控表(更新jlzt字段)
	public int updateSuspInfo(String T_YWZT,String T_JLZT,String bkxh)throws Exception{
		
		//更新布控状态(如通过的布控记录为管控类则直接布控成功，不须审批)
		StringBuffer sbVeh = new StringBuffer("UPDATE veh_suspinfo set ");
		sbVeh.append(" ywzt = '").append(T_YWZT).append("'").append(", jlzt = '")
		.append(T_JLZT).append("' ").append(",gxsj = sysdate ")
		.append("  where bkxh ='").append(bkxh).append("' ");
		
		int i = this.jdbcTemplate.update(sbVeh.toString());
		return i;
	}
	
	//更新自动撤控表
	public int updateSuspInfo(String T_YWZT,String T_JLZT,String bkxh, String ckyydm)throws Exception{
		
		StringBuffer sbVeh = new StringBuffer("UPDATE veh_suspinfo set ");
		sbVeh.append(" ywzt = '").append(T_YWZT).append("'").append(", jlzt = '")
		.append(T_JLZT).append("' ").append(",gxsj = sysdate ").append(",ckyydm='").append(ckyydm).append("'")
		.append("  where bkxh ='").append(bkxh).append("' ");
		
		int i = this.jdbcTemplate.update(sbVeh.toString());
		return i;
	}
	
	//插入传输表
	public int insertTransSusp(String xzqh,String jsdw,String bkxh,String type)throws Exception{
		
		StringBuffer sqlJs = new StringBuffer("INSERT into JM_TRANS_SUSP(csxh, csdw, jsdw,csbj, cssj, ywxh, type)   values(");
		sqlJs.append(xzqh).append("|| seq_trans_csxh.nextval,'").append(xzqh)
		.append("','").append(jsdw).append("',0,sysdate,'").append(bkxh)
		.append("','").append(type).append("') ");
		
		int i = this.jdbcTemplate.update(sqlJs.toString());
		
		return i;
	}
	
	//插入传输表(未加入试运行地市)
	public int insertTransSusp_test(String xzqh,String jsdw,String bkxh,String type)throws Exception{
		
		StringBuffer sqlJs = new StringBuffer("INSERT into " +
				" TRANS_SUSP" +
				"(csxh, csdw, jsdw,csbj, cssj, ywxh, type)   values(");
		sqlJs.append(xzqh).append("|| seq_trans_csxh.nextval,'").append(xzqh)
		.append("','").append(jsdw).append("',0,sysdate,'").append(bkxh)
		.append("','").append(type).append("') ");
		
		int i = this.jdbcTemplate.update(sqlJs.toString());
		
		return i;
	}
	
	//写入日志表
	public int insertBusinessLog(VehSuspinfo suspInfo,AuditApprove info,String ssjz,String ywlb)throws Exception{
		
		//更新日志表
		StringBuffer sbLog = new StringBuffer("INSERT into BUSINESS_LOG (xh, ywxh, ywlb, ywjb, czrdh, czrjh, czrdwdm, czrdwjz, bzsj) ");
		sbLog.append("values (seq_business_log_xh.nextval,'").append(info.getBkxh()).append("','").append(ywlb).append("','")
		.append(suspInfo.getBkjb()).append("','").append(info.getCzr()).append("','").append(info.getCzrjh())
		.append("','").append(info.getCzrdw()).append("','").append(ssjz).append("',to_date('").append(suspInfo.getGxsj())
		.append("','yyyy-mm-dd hh24:mi:ss'))");
		
		int i = this.jdbcTemplate.update(sbLog.toString());
		
		return i;
	}

	public int getSupinfoAuditCont(String begin, String end, String glbm)
			throws Exception {
		String tmpSql = "Select * from veh_suspinfo Where YWZT='11' and XXLY='0' and bkjg='" + glbm + "'";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('"+ end +" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
//		List list =  (List) this.getSelf().findPageForMap(tmpSql, 1, 10).get("rows");
//		if (list != null && list.size() > 0) {
//			return list.size();
//		}
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}
	
	
	
	public int getSuspinfoAuditOverTimeCount(String begin, String end, String glbm)
			throws Exception {
		String tmpSql = "Select * from veh_suspinfo Where YWZT='11' and XXLY='0' and bkdl='3' and bkjg='"
				+ glbm + "'";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin
					+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end
					+ " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		tmpSql+=" and (sysdate-bksj)*24*3600>7200";
		// List list = (List) this.getSelf().findPageForMap(tmpSql, 1,
		// 10).get("rows");
		// if (list != null && list.size() > 0) {
		// return list.size();
		// }
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}
	
	public int getSuspinfoCancelAuditCount(String begin, String end, String glbm) throws Exception {
		String tmpSql = "Select * from VEH_SUSPINFO Where ywzt='41' and XXLY='0' and bkjg='" + glbm + "'";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + "and bksj <= to_date('" + end + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}
	
	public int getSuspinfoCancelAuditOverTimeCount(String begin, String end, String glbm) throws Exception {
		String tmpSql = "Select * from VEH_SUSPINFO Where ywzt='41' and bkdl ='3' and XXLY='0' and bkjg='" + glbm + "'";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + "and bksj <= to_date('" + end + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		tmpSql+=" and (sysdate-bksj)*24*3600>7200";
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}

	@Override
	public List<Map<String,Object>> getVehSuspinfoList() throws Exception {
		String sql = "select bkxh from veh_suspinfo where ywzt = '11'  and jlzt = '0' and "
				+ "bkfwlx = '3' order by bksj desc";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	
}
