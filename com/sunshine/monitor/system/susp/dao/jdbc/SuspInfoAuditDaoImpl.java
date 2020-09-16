package com.sunshine.monitor.system.susp.dao.jdbc;

import java.util.ArrayList;
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
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select v.* ");
		sql.append("from VEH_SUSPINFO v ");
		sql.append("  where YWZT = '11'  and XXLY = '0'  ");
		
		if (StringUtils.isNotBlank(info.getBkfwlx())){
			sql.append(" and BKFWLX = ? ");
			param.add(info.getBkfwlx());
		}
		
		if(StringUtils.isNotBlank(info.getBkdl())){
			sql.append("  and bkdl = ? ");
			param.add(info.getBkdl());
		}
		
		if(StringUtils.isNotBlank(info.getBklb())){
			sql.append(" and bklb = ?  ");
			param.add(info.getBklb());
		}
		
		if(StringUtils.isNotBlank(info.getHpzl())){
			sql.append(" and hpzl = ? ");
			param.add(info.getHpzl());
		}
		
		if(StringUtils.isNotBlank(info.getHphm())){
			sql.append(" and hphm like ? ");
			param.add("%"+info.getHphm()+"%");
		}
		
		if(StringUtils.isNotBlank(glbm)){
			sql.append(" and bkjg in (Select xjjg from frm_prefecture Where dwdm=?)");
			param.add(glbm);
		}
		
		if(StringUtils.isNotBlank(info.getKssj())) {
			sql.append(" and bksj >=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(info.getKssj());
		}
		
		if(StringUtils.isNotBlank(info.getJssj())) {
			sql.append(" and bksj <=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(info.getJssj());
		}
		
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			sql.append("  and bksj >= sysdate - 365  ");
		}
		
		sql.append("  order by bksj desc ");
		
		return this.getSelf().findPageForMap(sql.toString(), param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	}
	
	
	public Map findSuspinfoAuditOverTimeForMap(Map filter, VehSuspinfo info, String glbm)
			throws Exception {
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select v.* ");
		sql.append("from VEH_SUSPINFO v ");
		sql.append("  where YWZT = '11'  and XXLY = '0'  ");

		sql.append(" and bkdl='3'" );

		if (StringUtils.isNotBlank(info.getBklb())) {
			sql.append(" and bklb = ?  ");
			param.add(info.getBklb());
		}

		if (StringUtils.isNotBlank(info.getHpzl())) {
			sql.append(" and hpzl = ? ");
			param.add(info.getHpzl());
		}

		if (StringUtils.isNotBlank(info.getHphm())) {
			sql.append(" and hphm like ? ");
			param.add("%" + info.getHphm() + "%");
		}

		if (StringUtils.isNotBlank(glbm)) {
			sql.append(" and bkjg = ? ");
			param.add(glbm);
		}

		if (StringUtils.isNotBlank(info.getKssj())){
			sql.append(" and bksj >=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(info.getKssj());
		}


		if (StringUtils.isNotBlank(info.getJssj())){
			sql.append(" and bksj <=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(info.getJssj());
		}


		if (StringUtils.isBlank(info.getKssj())
				&& StringUtils.isBlank(info.getJssj())) {
			sql.append("  and bksj >= sysdate - 365  ");
		}
		sql.append(" and (sysdate-bksj)*24*3600>7200");
		sql.append("  order by bksj desc ");

		return this.getSelf().findPageForMap(sql.toString(),param.toArray(),
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
	}

	//保存审核记录
	public int insertAuditApprove(AuditApprove info)throws Exception{
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer("INSERT into audit_approve ");
		sb.append("(xh, bkxh, czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,czrjh,czrmc) values( '");
		sb.append("?,?,?,?,?,sysdate,?,?,?,?,?)");

		param.add(info.getCzrdw()+" || seq_audit_xh.nextval");
		param.add(info.getBkxh());
		param.add(info.getCzr());
		param.add(info.getCzrdw());
		param.add(info.getCzrdwmc());
		param.add(info.getCzjg());
		param.add(info.getMs());
		param.add(info.getBzw());
		param.add(info.getCzrjh());
		param.add(info.getCzrmc());

		int i= this.jdbcTemplate.update(sb.toString(),param.toArray());
		return i;
	}
	
	//更新布控表(更新jlzt字段)
	public int updateSuspInfo(String T_YWZT,String T_JLZT,String bkxh)throws Exception{
		List param = new ArrayList<>();
		//更新布控状态(如通过的布控记录为管控类则直接布控成功，不须审批)
		StringBuffer sbVeh = new StringBuffer("UPDATE veh_suspinfo set ");
		sbVeh.append(" ywzt = ?").append(", jlzt = ? ").append(",gxsj = sysdate ")
		.append("  where bkxh =? ");

		param.add(T_YWZT);
		param.add(T_JLZT);
		param.add(bkxh);
		int i = this.jdbcTemplate.update(sbVeh.toString(),param.toArray());
		return i;
	}
	
	//更新自动撤控表
	public int updateSuspInfo(String T_YWZT,String T_JLZT,String bkxh, String ckyydm)throws Exception{
		List param = new ArrayList<>();
		StringBuffer sbVeh = new StringBuffer("UPDATE veh_suspinfo set ");
		sbVeh.append(" ywzt = ?").append(", jlzt = ? ").append(",gxsj = sysdate ").append("," +
				"ckyydm=?").append("  where bkxh =? ");
		param.add(T_YWZT);
		param.add(T_JLZT);
		param.add(ckyydm);
		param.add(bkxh);
		
		int i = this.jdbcTemplate.update(sbVeh.toString(),param.toArray());
		return i;
	}
	
	//插入传输表
	public int insertTransSusp(String xzqh,String jsdw,String bkxh,String type)throws Exception{
		List param = new ArrayList<>();
		StringBuffer sqlJs = new StringBuffer("INSERT into JM_TRANS_SUSP(csxh, csdw, jsdw,csbj, " +
				"cssj, ywxh, type)   values(?,?,?,0,sysdate,?,?) ");
		param.add(xzqh+"|| seq_trans_csxh.nextval");
		param.add(xzqh);
		param.add(jsdw);
		param.add(bkxh);
		param.add(type);
		
		int i = this.jdbcTemplate.update(sqlJs.toString(),param.toArray());
		
		return i;
	}
	
	//插入传输表(未加入试运行地市)
	public int insertTransSusp_test(String xzqh,String jsdw,String bkxh,String type)throws Exception{
		List param = new ArrayList<>();
		StringBuffer sqlJs = new StringBuffer("INSERT into " +
				" TRANS_SUSP" +
				"(csxh, csdw, jsdw,csbj, cssj, ywxh, type) values(?,?,?,0,sysdate,?,?) ");
		param.add(xzqh+"|| seq_trans_csxh.nextval");
		param.add(xzqh);
		param.add(jsdw);
		param.add(bkxh);
		param.add(type);
		
		int i = this.jdbcTemplate.update(sqlJs.toString(),param.toArray());
		
		return i;
	}
	
	//写入日志表
	public int insertBusinessLog(VehSuspinfo suspInfo,AuditApprove info,String ssjz,String ywlb)throws Exception{
		List param = new ArrayList<>();
		//更新日志表
		StringBuffer sbLog = new StringBuffer("INSERT into BUSINESS_LOG (xh, ywxh, ywlb, ywjb, czrdh, czrjh, czrdwdm, czrdwjz, bzsj) ");
		sbLog.append("values (seq_business_log_xh.nextval,?,?,?,?,?,?,?," +
				"to_date(?,'yyyy-mm-dd hh24:mi:ss'))");
		param.add(info.getBkxh());
		param.add(ywlb);
		param.add(suspInfo.getBkjb());
		param.add(info.getCzr());
		param.add(info.getCzrjh());
		param.add(info.getCzrdw());
		param.add(ssjz);
		param.add(suspInfo.getGxsj());
		
		int i = this.jdbcTemplate.update(sbLog.toString(),param.toArray());
		
		return i;
	}

	public int getSupinfoAuditCont(String begin, String end, String glbm)
			throws Exception {
		List param = new ArrayList<>();
		String tmpSql = "Select * from veh_suspinfo Where YWZT='11' and XXLY='0' and bkjg=?";
		param.add(glbm);
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(begin + " 00:00:00");
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(end +" 23:59:59");
		}
//		List list =  (List) this.getSelf().findPageForMap(tmpSql, 1, 10).get("rows");
//		if (list != null && list.size() > 0) {
//			return list.size();
//		}
		int count = this.getSelf().getRecordCounts(tmpSql, param.toArray(),0);
		return count;
	}
	
	
	
	public int getSuspinfoAuditOverTimeCount(String begin, String end, String glbm)
			throws Exception {
		List param = new ArrayList<>();
		String tmpSql = "Select * from veh_suspinfo Where YWZT='11' and XXLY='0' and bkdl='3' and" +
				" bkjg=?";
		param.add(glbm);
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(begin + " 00:00:00");
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(end +" 23:59:59");
		}
		tmpSql+=" and (sysdate-bksj)*24*3600>7200";
		// List list = (List) this.getSelf().findPageForMap(tmpSql, 1,
		// 10).get("rows");
		// if (list != null && list.size() > 0) {
		// return list.size();
		// }
		int count = this.getSelf().getRecordCounts(tmpSql, param.toArray(),0);
		return count;
	}
	
	public int getSuspinfoCancelAuditCount(String begin, String end, String glbm) throws Exception {
		List param = new ArrayList<>();
		String tmpSql = "Select * from VEH_SUSPINFO Where ywzt='41' and XXLY='0' and bkjg=?";
		param.add(glbm);
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(begin + " 00:00:00");
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + "and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(end +" 23:59:59");
		}
		int count = this.getSelf().getRecordCounts(tmpSql, param.toArray(),0);
		return count;
	}
	
	public int getSuspinfoCancelAuditOverTimeCount(String begin, String end, String glbm) throws Exception {
		List param = new ArrayList<>();
		String tmpSql = "Select * from VEH_SUSPINFO Where ywzt='41' and bkdl ='3' and XXLY='0' and bkjg='" + glbm + "'";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(begin + " 00:00:00");
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + "and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(end +" 23:59:59");
		}
		tmpSql+=" and (sysdate-bksj)*24*3600>7200";
		int count = this.getSelf().getRecordCounts(tmpSql, param.toArray(),0);
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
