package com.sunshine.monitor.system.susp.dao.jdbc;

import java.util.ArrayList;
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
		List param = new ArrayList<>();
		String tmpSql = "";
		if (info.getBkdl() != null && info.getBkdl().length() > 0) {
			tmpSql = tmpSql + " and bkdl=?";
			param.add(info.getBkdl());
		}
		if (info.getBklb() != null && info.getBklb().length() > 0) {
			tmpSql = tmpSql + " and bklb=?";
			param.add(info.getBklb());
		}
		if (info.getHphm() != null && info.getHphm().length() > 0) {
			tmpSql = tmpSql + " and hphm like ?";
			param.add("%"+info.getHphm()+"%");
		}
		if (info.getHpzl() != null && info.getHpzl().length() > 0) {
			tmpSql = tmpSql + " and hpzl=?";
			param.add( info.getHpzl());
		}
		if (info.getKssj() != null && info.getKssj().length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(info.getKssj());
		}
		if (info.getJssj() != null && info.getJssj().length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(info.getJssj());
		}

		/**
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			tmpSql += "  and bksj >= sysdate -365  ";
		}
		*/
		
		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm=?)";
			param.add(glbm);
		}
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql, param.toArray(),Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	public Map<String, Object> getSusinfoCancelApprovesOverTime(Map filter, VehSuspinfo info,
			String glbm) throws Exception {
		List param = new ArrayList<>();
		String tmpSql = "";
		if (info.getBkdl() != null && info.getBkdl().length() > 0) {
			tmpSql = tmpSql + " and bkdl=?";
			param.add(info.getBkdl());
		}
		if (info.getBklb() != null && info.getBklb().length() > 0) {
			tmpSql = tmpSql + " and bklb=?";
			param.add(info.getBklb());
		}
		if (info.getHphm() != null && info.getHphm().length() > 0) {
			tmpSql = tmpSql + " and hphm like ?";
			param.add("%"+info.getHphm()+"%");
		}
		if (info.getHpzl() != null && info.getHpzl().length() > 0) {
			tmpSql = tmpSql + " and hpzl=?";
			param.add(info.getHpzl());
		}
		if (info.getKssj() != null && info.getKssj().length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(info.getKssj());
		}
		if (info.getJssj() != null && info.getJssj().length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(info.getJssj());
		}

		/**
		if(StringUtils.isBlank(info.getKssj()) && StringUtils.isBlank(info.getJssj())){
			tmpSql += "  and bksj >= sysdate -365  ";
		}
		*/
		
		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm=?)";
			param.add(glbm);
		}
		tmpSql+=" and bkxh in (select bkxh from audit_approve where bzw='3' and (sysdate-czsj)*24*3600 >7200)";
		
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql,param.toArray(), Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}
	
	
	public Map<String, Object> getSuspinfoCancelClassApproves(Map filter, VehSuspinfo info, String glbm) {
		String tmpSql = "";
		List param = new ArrayList<>();
		if (info.getBklb() != null && info.getBklb().length() > 0) {
			tmpSql = tmpSql + " and bklb=?";
			param.add(info.getBklb());
		}
		if (info.getHphm() != null && info.getHphm().length() > 0) {
			tmpSql = tmpSql + " and hphm like ?";
			param.add("%" + info.getHphm() + "%");
		}
		if (info.getHpzl() != null && info.getHpzl().length() > 0) {
			tmpSql = tmpSql + " and hpzl=?";
			param.add(info.getHpzl());
		}
		if (info.getKssj() != null && info.getKssj().length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(info.getKssj());
		}
		if (info.getJssj() != null && info.getJssj().length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(info.getJssj());
		}
		
		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm=?)";
			param.add(glbm);
		}
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' "// and bkdl='1' 管控类新增撤控流程 liumeng
			+ tmpSql + " order by bksj desc";
	Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql, param.toArray(),Integer
			.parseInt(filter.get("curPage").toString()), Integer
			.parseInt(filter.get("pageSize").toString()));
		
		return queryMap;
	}
	
	public Map<String, Object> getSuspinfoCancelTrafficApproves(Map filter, VehSuspinfo info, String glbm) {
		String tmpSql = "";
		List param = new ArrayList<>();
		if (info.getBklb() != null && info.getBklb().length() > 0) {
			tmpSql = tmpSql + " and bklb=?";
			param.add(info.getBklb());
		}
		if (info.getHphm() != null && info.getHphm().length() > 0) {
			tmpSql = tmpSql + " and hphm like ?";
			param.add("%" + info.getHphm() + "%");
		}
		if (info.getHpzl() != null && info.getHpzl().length() > 0) {
			tmpSql = tmpSql + " and hpzl=?";
			param.add(info.getHpzl());
		}
		if (info.getKssj() != null && info.getKssj().length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(info.getKssj());
		}
		if (info.getJssj() != null && info.getJssj().length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(info.getJssj());
		}
		
		if (glbm != null && glbm.length() > 0) {
			tmpSql = tmpSql
					+ " and bkjg in (Select xjjg from frm_prefecture Where "
					+ "dwdm=?)";
			param.add(glbm);
		}
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' and bkdl='2' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql, param.toArray(),Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	public Object getCancelApprpvesDetailForBkxh(String bkxh, String glbm) throws Exception {
		String tmpSql = null;
		VehSuspinfo vehSupinfo = null;
		List param = new ArrayList<>();
		if (bkxh != null && bkxh.length() > 0) {
			tmpSql = "select a.*,b.xh picxh from(select * from veh_suspinfo where ywzt='42' " +
					" and xxly='0' and bkxh=? and bkjg in " +
					" (select xjjg from frm_prefecture where dwdm=?)) a,(select xh,bkxh from " +
					"susp_picrec where bkxh=?) b " +
			        " where a.bkxh=b.bkxh(+)";
			param.add(bkxh);
			param.add(glbm);
			param.add(bkxh);
		}
		List list = this.queryForList(tmpSql, param.toArray(),VehSuspinfo.class);
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
		List param = new ArrayList<>();

		if (aa.getBkxh() != null && aa.getBkxh().length() > 0) {
			tmpSql = tmpSql + " and bkxh=?";
			param.add(aa.getBkxh());
		}
		if (aa.getBzw() != null && aa.getBzw().length() > 0){
			if (aa.getBzw().length() == 1) {
				tmpSql = tmpSql + " and bzw=?";
				param.add(aa.getBzw());

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
		return this.queryForList(tmpSql, param.toArray(), AuditApprove.class);
	}

	
	public int saveSuspinfoCancelApprove(AuditApprove info) throws Exception {
		StringBuffer sb = null;
		int count = 0;
		List param = new ArrayList<>();
		if (info != null) {
			sb = new StringBuffer("Insert into AUDIT_APPROVE(xh,bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,czrjh,czrmc) values( ");
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

			count = this.jdbcTemplate.update(sb.toString(),param.toArray());
		}
		return count;
	}

	public int updateSuspinfoCancelApprove(String ywzt, String ljzt, String bkxh) {
		String tmpSql = null;
		List param = new ArrayList<>();
		if (bkxh != null && bkxh.length() > 0) {
		     tmpSql = "Update VEH_SUSPINFO set ywzt=?,jlzt=?,gxsj=sysdate,by2=to_char(sysdate," +
					 "'yyyy-mm-dd hh24:mi:ss') Where bkxh=?";
			param.add(ywzt);
			param.add(ljzt);
			param.add(bkxh);
		}
		int count = this.jdbcTemplate.update(tmpSql,param.toArray());
		return count;
	}

	
	public int saveTranSusp(String csqh, String jsdw, String bkxh, String type) {
		StringBuffer sb = new StringBuffer("");
		List param = new ArrayList<>();
		sb.append("Insert into JM_TRANS_SUSP(csxh,csdw,jsdw,csbj,cssj,ywxh,type) values(");
		sb.append("?,?,?,0,sysdate,?,?)");
		param.add(csqh+" || seq_trans_csxh.nextval");
		param.add(csqh);
		param.add(jsdw);
		param.add(bkxh);
		param.add(type);



		return this.jdbcTemplate.update(sb.toString());
	}

	public int saveSuspinfoCancelApproveLog(AuditApprove info, String type,
			VehSuspinfo vehInfo, String ssjz) throws Exception {
		StringBuffer sb = new StringBuffer();
		List param = new ArrayList<>();

		sb.append("Insert into BUSINESS_LOG(xh,ywxh,ywlb,ywjb,czrdh,czrjh,czrdwdm,czrdwjz,bzsj) values(");
		sb.append("seq_business_log_xh.nextval,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))");
		param.add(info.getBkxh());
		param.add(type);
		param.add(vehInfo.getBkjb());
		param.add(info.getCzr());
		param.add(info.getCzrjh());
		param.add(info.getCzrdw());
		param.add(ssjz);
		param.add(vehInfo.getGxsj());

		return this.jdbcTemplate.update(sb.toString(),param.toArray());
	}

	public List getCAlarmHistory(String bkxh) throws Exception {
		String tmpSql = "select * from veh_alarmrec where  BKXH=? order by bjsj desc";
	    return this.queryForList(tmpSql, new Object[]{bkxh},VehAlarmrec.class);
	}
	
	public int getSuspinfoCancelApproveCount(String begin, String end, String glbm) {
		List param = new ArrayList<>();
		String tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' and bkjg in " +
				"(Select xjjg from frm_prefecture Where dwdm=?)";
		param.add(glbm);

		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(begin + " 00:00:00");
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(end + " 23:59:59");
		}
		int count = this.getSelf().getRecordCounts(tmpSql, param.toArray(),0);
		return count;
	}
	
	public int getSuspinfoCancelApproveOverTimeCount(String begin, String end, String glbm) {
		List param = new ArrayList<>();
		String tmpSql = "Select * from VEH_SUSPINFO Where YWZT='42' and XXLY='0' and bkjg in " +
				"(Select xjjg from frm_prefecture Where dwdm=?)";
		param.add(glbm);
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(begin + " 00:00:00");
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(end + " 23:59:59");
		}
		tmpSql+=" and bkxh in (select bkxh from Audit_Approve where bzw='3' and (sysdate-czsj)*24*3600>7200)";
		int count = this.getSelf().getRecordCounts(tmpSql, param.toArray(),0);
		return count;
	}

}
