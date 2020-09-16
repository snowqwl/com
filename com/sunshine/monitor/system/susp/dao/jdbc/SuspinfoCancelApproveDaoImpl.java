package com.sunshine.monitor.system.susp.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoCancelApproveDao;

@Repository("suspinfoCancelApproveDao")
public class SuspinfoCancelApproveDaoImpl extends BaseDaoImpl implements
		SuspinfoCancelApproveDao {

	public Map<String, Object> getSusinfoCancelApproves(Map filter, VehSuspinfo info,
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

		/**
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			tmpSql += "  and bksj >= sysdate -365  ";
		}
		*/
		
		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm='" + glbm + "')";
		}
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	public Map<String, Object> getSusinfoCancelApprovesOverTime(Map filter, VehSuspinfo info,
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

		/**
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			tmpSql += "  and bksj >= sysdate -365  ";
		}
		*/
		
		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm='" + glbm + "')";
		}
		tmpSql+=" and bkxh in (select bkxh from audit_approve where bzw='3' and (sysdate-czsj)*24*3600 >7200)";
		
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	
	public Map<String, Object> getSuspinfoCancelClassApproves(Map filter, VehSuspinfo info, String glbm) {
		String tmpSql = "";
		
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
		
		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm='" + glbm + "')";
		}
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' "// and bkdl='1' 管控类新增撤控流程 liumeng
			+ tmpSql + " order by bksj desc";
	Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql, Integer
			.parseInt(filter.get("curPage").toString()), Integer
			.parseInt(filter.get("pageSize").toString()));
		
		return queryMap;
	}
	
	public Map<String, Object> getSuspinfoCancelTrafficApproves(Map filter, VehSuspinfo info, String glbm) {
		String tmpSql = "";
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
		
		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm='" + glbm + "')";
		}
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' and bkdl='2' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql, Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	public Object getCancelApprpvesDetailForBkxh(String bkxh, String glbm) throws Exception {
		String tmpSql = null;
		VehSuspinfo vehSupinfo = null;
		if (bkxh != null && bkxh.length() > 0) {
			tmpSql = "select a.*,b.xh picxh from(select * from veh_suspinfo where ywzt='42' " +
					" and xxly='0' and bkxh='"+ bkxh + "'and bkjg in " +
					" (select xjjg from frm_prefecture where dwdm='" + glbm + "')) a,(select xh,bkxh from susp_picrec where bkxh='" + bkxh + "') b " +
			        " where a.bkxh=b.bkxh(+)";
		}
		List list = this.queryForList(tmpSql, VehSuspinfo.class);
		if (list.size() > 0) {
			vehSupinfo = (VehSuspinfo) list.get(0);
		}
		return vehSupinfo;
	}
	
	
	public List getBkfwListTree() throws Exception {
		 String sql = "select * from code_url order by dwdm";
	   
	     return queryForList(sql, CodeUrl.class);
		
	}

	public List getAuditApproves(AuditApprove aa) throws Exception {
		String tmpSql = "";
		if (aa.getBkxh() != null && aa.getBkxh().length() > 0) {
			tmpSql = tmpSql + " and bkxh='" + aa.getBkxh() + "'";
		}
		if (aa.getBzw() != null && aa.getBzw().length() > 0){
			if (aa.getBzw().length() == 1) {
				tmpSql = tmpSql + " and bzw='" + aa.getBzw() + "'";
			} else if (aa.getBzw().equals("12")) {
				tmpSql = tmpSql + " and (bzw='1' or bzw='2')";
			} else if (aa.getBzw().equals("34")) {
				tmpSql = tmpSql + " and (bzw='3' or bzw='4')";
			}
		}
		if (tmpSql.length() > 1) {
			tmpSql = " Where " + tmpSql.substring(5, tmpSql.length()) + " ";
		}
		tmpSql = "Select * from audit_approve " + tmpSql + " order by czsj desc";
		return this.queryForList(tmpSql, AuditApprove.class);
	}

	
	public int saveSuspinfoCancelApprove(AuditApprove info) throws Exception {
		StringBuffer sb = null;
		int count = 0;
		if (info != null) {
			sb = new StringBuffer("Insert into AUDIT_APPROVE(xh,bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,czrjh,czrmc) values( ");
			sb.append(info.getCzrdw()).append(" || seq_audit_xh.nextval,'");
			sb.append(info.getBkxh()).append("','").append(info.getCzr()).append("','");
			sb.append(info.getCzrdw()).append("','").append(info.getCzrdwmc()).append("',sysdate,'");
			sb.append(info.getCzjg()).append("','").append(info.getMs()).append("','");
			sb.append(info.getBzw()).append("','").append(info.getCzrjh()).append("','");
			sb.append(info.getCzrmc()).append("' )");
			count = this.jdbcTemplate.update(sb.toString());
		}
		return count;
	}

	public int updateSuspinfoCancelApprove(String ywzt, String ljzt, String bkxh) {
		String tmpSql = null;
		if (bkxh != null && bkxh.length() > 0) {
		     tmpSql = "Update VEH_SUSPINFO set ywzt='" + ywzt + "',jlzt='" + 
		     ljzt + "',gxsj=sysdate,by2=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') Where bkxh='" + bkxh + "'";
		}
		int count = this.jdbcTemplate.update(tmpSql);
		return count;
	}

	
	public int saveTranSusp(String csqh, String jsdw, String bkxh, String type) {
		StringBuffer sb = new StringBuffer("");
		sb.append("Insert into JM_TRANS_SUSP(csxh,csdw,jsdw,csbj,cssj,ywxh,type) values(");
		sb.append(csqh).append(" || seq_trans_csxh.nextval,'").append(csqh).append("','");
		sb.append(jsdw).append("',0,sysdate,'").append(bkxh).append("','");
		sb.append(type).append("')");
		return this.jdbcTemplate.update(sb.toString());
	}

	public int saveSuspinfoCancelApproveLog(AuditApprove info, String type,
			VehSuspinfo vehInfo, String ssjz) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("Insert into BUSINESS_LOG(xh,ywxh,ywlb,ywjb,czrdh,czrjh,czrdwdm,czrdwjz,bzsj) values(");
		sb.append("seq_business_log_xh.nextval,'").append(info.getBkxh()).append("','");
		sb.append(type).append("','").append(vehInfo.getBkjb()).append("','");
		sb.append(info.getCzr()).append("','").append(info.getCzrjh()).append("','");
		sb.append(info.getCzrdw()).append("','").append(ssjz).append("',to_date('").append(vehInfo.getGxsj());
		sb.append("','yyyy-mm-dd hh24:mi:ss'))");
		return this.jdbcTemplate.update(sb.toString());
	}

	public List getCAlarmHistory(String bkxh) throws Exception {
		String tmpSql = "select * from veh_alarmrec where  BKXH='" + bkxh + "' order by bjsj desc";
	    return this.queryForList(tmpSql, VehAlarmrec.class);
	}
	
	public int getSuspinfoCancelApproveCount(String begin, String end, String glbm) {
		String tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' and bkjg in (Select xjjg from frm_prefecture Where dwdm='" + glbm + "')";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}
	
	public int getSuspinfoCancelApproveOverTimeCount(String begin, String end, String glbm) {
		String tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' and bkjg in (Select xjjg from frm_prefecture Where dwdm='" + glbm + "')";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		tmpSql+=" and bkxh in (select bkxh from Audit_Approve where bzw='3' and (sysdate-czsj)*24*3600>7200)";
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}

}
