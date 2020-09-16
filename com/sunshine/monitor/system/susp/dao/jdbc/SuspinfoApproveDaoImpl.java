package com.sunshine.monitor.system.susp.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoApproveDao;

@Repository("suspinfoApproveDao")
public class SuspinfoApproveDaoImpl extends BaseDaoImpl implements
		SuspinfoApproveDao {

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
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='12' and (XXLY='0'OR XXLY='2') and bkfwlx = '2' "
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

	public Map<String, Object> getSusinfoClassApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {

		String tmpSql = "";
		if (info.getBklb() != null && info.getBklb().length() > 0) {
			tmpSql = tmpSql + " and bklb='" + info.getBklb() + "'";
		}
		if (info.getHpzl() != null && info.getHpzl().length() > 0) {
			tmpSql = tmpSql + " and hpzl='" + info.getHpzl() + "'";
		}
		if (info.getHphm() != null && info.getHphm().length() > 0) {
			tmpSql = tmpSql + " and hphm='" + info.getHphm() + "'";
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
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='12' and XXLY='0' and BKDL='1'"
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql,
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	public Map<String, Object> getSusinfoTrafficApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {

		String tmpSql = "";
		if (info.getBklb() != null && info.getBklb().length() > 0) {
			tmpSql = tmpSql + " and bklb='" + info.getBklb() + "'";
		}
		if (info.getHpzl() != null && info.getHpzl().length() > 0) {
			tmpSql = tmpSql + " and hpzl='" + info.getHpzl() + "'";
		}
		if (info.getHphm() != null && info.getHphm().length() > 0) {
			tmpSql = tmpSql + " and hphm='" + info.getHphm() + "'";
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
		tmpSql = "Select * from VEH_SUSPINFO Where YWZT='12' and XXLY='0' and BKDL='2' "
				+ tmpSql + " order by bksj desc";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql,
				Integer.parseInt(filter.get("curPage").toString()),
				Integer.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	public Object getApprpvesDetailForBkxh111(String bkxh)
			 {
		String tmpSql = "";
		VehSuspinfo vehSupinfo = null;
		if (bkxh != null && bkxh.length() > 0) {
			tmpSql = "select  *  from  veh_suspinfo where bkxh= '"+bkxh+"'";
		}
		vehSupinfo = this.queryForObject(tmpSql, VehSuspinfo.class);
		return vehSupinfo;
	}
	
	public Object getApprpvesDetailForBkxh(String bkxh, String glbm)
			throws Exception {
		String tmpSql = "";
		VehSuspinfo vehSupinfo = null;
		if (bkxh != null && bkxh.length() > 0) {
			tmpSql = "select a.*,b.xh picxh from(select * from veh_suspinfo where ywzt='12' "
					//增加地市联动布控 待审批记录（详细信息）
					//+ " and xxly='0' and bkxh='"
					+ " and (XXLY='0' OR XXLY='2') and bkxh='"
					+ bkxh
					+ "'and bkjg in "
					+ " (select xjjg from frm_prefecture where dwdm='"
					+ glbm
					+ "')) a,(select xh,bkxh from susp_picrec where bkxh='"
					+ bkxh + "') b " + " where a.bkxh=b.bkxh(+)";
		}
		// List<VehSuspinfo> list = this.queryForList(tmpSql,
		// VehSuspinfo.class);
		vehSupinfo = this.queryForObject(tmpSql, VehSuspinfo.class);
		// if (list.size() > 0) {
		// vehSupinfo = list.get(0);
		// }
		return vehSupinfo;
	}

	public List getBkfwListTree() throws Exception {
		String sql = "select * from code_url order by dwdm";

		return queryForList(sql, CodeUrl.class);

	}
	public List getBkfwListTreenew() throws Exception {
//		String sql = "select * from code_url order by dwdm";
		String sql="select dmz||'000000' as dmz , dmsm1 from frm_code  where dmlb='000033' "+
				"and ( substr(dmz,0,2)='43' or substr(dmz,0,2)='44' or substr(dmz,0,2)='45' "+ 
				"or  substr(dmz,0,2)='35' or substr(dmz,0,2)='36' or substr(dmz,0,2)='46' ) "+
				"and substr(dmz,5,2)='00'";
		
		List<Code> list= queryForList(sql, Code.class);
		List<CodeUrl> lists=new ArrayList<>();
		for (Code c:list) {
			CodeUrl cu=new CodeUrl();
			cu.setDwdm(c.getDmz());
			cu.setJdmc(c.getDmsm1());
			lists.add(cu);
		}
		return lists;
		
	}

	public List getAuditApproves(AuditApprove aa) throws Exception {
		String tmpSql = "";
		if (aa.getBkxh() != null && aa.getBkxh().length() > 0) {
			tmpSql = tmpSql + " and bkxh='" + aa.getBkxh() + "'";
		}
		if (aa.getBzw() != null && aa.getBzw().length() > 0) {
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
		tmpSql = "Select * from audit_approve " + tmpSql
				+ " order by czsj desc";
		return this.queryForList(tmpSql, AuditApprove.class);
	}

	public int saveSuspinfoApprove(AuditApprove info) throws Exception {
		StringBuffer sb = null;
		int count = 0;
		if (info != null) {
			sb = new StringBuffer(
					"Insert into AUDIT_APPROVE(xh,bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,czrjh,czrmc,by1,by2) values( ");
			sb.append(info.getCzrdw()).append(" || seq_audit_xh.nextval,'");
			sb.append(info.getBkxh()).append("','").append(info.getCzr())
					.append("','");
			sb.append(info.getCzrdw()).append("','").append(info.getCzrdwmc())
					.append("',sysdate,'");
			sb.append(info.getCzjg()).append("','").append(info.getMs())
					.append("','");
			sb.append(info.getBzw()).append("','").append(info.getCzrjh())
					.append("','");
			sb.append(info.getCzrmc()).append("','");
			if (info.getBy1() != null && !info.getBy1().equals("")) {
				sb.append(info.getBy1()).append("','");
			} else {
				sb.append("','");
			}
			if (info.getBy2() != null && !info.getBy2().equals("")) {
				sb.append(info.getBy2()).append("')");
			} else {
				sb.append("')");
			}
			count = this.jdbcTemplate.update(sb.toString());
		}
		return count;
	}

	public int saveSuspinfoApprove(TransAuditApprove info) throws Exception {
		StringBuffer sb = null;
		int count = 0;
		if (info != null) {
			sb = new StringBuffer(
					"Insert into AUDIT_APPROVE(xh,bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,czrjh,czrmc,by1,by2) values( ");
			sb.append(info.getCzrdw()).append(" || seq_audit_xh.nextval,'");
			sb.append(info.getBkxh()).append("','").append(info.getCzr())
					.append("','");
			sb.append(info.getCzrdw()).append("','").append(info.getCzrdwmc())
					.append("',sysdate,'");
			sb.append(info.getCzjg()).append("','").append(info.getMs())
					.append("','");
			sb.append(info.getBzw()).append("','").append(info.getCzrjh())
					.append("','");
			sb.append(info.getCzrmc()).append("','");
			if (info.getBy1() != null && !info.getBy1().equals("")) {
				sb.append(info.getBy1()).append("','");
			} else {
				sb.append("','");
			}
			if (info.getBy2() != null && !info.getBy2().equals("")) {
				sb.append(info.getBy2()).append("')");
			} else {
				sb.append("')");
			}
			count = this.jdbcTemplate.update(sb.toString());
		}
		return count;
	}

	public int updateSuspinfoApprove(String ywzt, String ljzt, String bkxh) {
		String tmpSql = null;
		if (bkxh != null && bkxh.length() > 0) {
			tmpSql = "Update VEH_SUSPINFO set ywzt='" + ywzt + "',jlzt='"
					+ ljzt + "',gxsj=sysdate Where bkxh='" + bkxh + "'";
		}
		int count = this.jdbcTemplate.update(tmpSql);
		return count;
	}

	public int saveTranSusp(String csqh, String jsdw, String bkxh, String type) {
		StringBuffer sb = new StringBuffer("");
		sb
				.append("Insert into JM_TRANS_SUSP(csxh,csdw,jsdw,csbj,cssj,ywxh,type) values(");
		sb.append(csqh).append(" || seq_trans_csxh.nextval,'").append(csqh)
				.append("','");
		sb.append(jsdw).append("',0,sysdate,'").append(bkxh).append("','");
		sb.append(type).append("')");
		return this.jdbcTemplate.update(sb.toString());
	}

	public int saveSuspinfoApproveLog(AuditApprove info, String type,
			VehSuspinfo vehInfo, String ssjz) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb
				.append("Insert into BUSINESS_LOG(xh,ywxh,ywlb,ywjb,czrdh,czrjh,czrdwdm,czrdwjz,bzsj) values(");
		sb.append("seq_business_log_xh.nextval,'").append(info.getBkxh())
				.append("','");
		sb.append(type).append("','").append(vehInfo.getBkjb()).append("','");
		sb.append(info.getCzr()).append("','").append(info.getCzrjh()).append(
				"','");
		sb.append(info.getCzrdw()).append("','").append(ssjz).append(
				"',to_date('").append(vehInfo.getGxsj());
		sb.append("','yyyy-mm-dd hh24:mi:ss'))");
		return this.jdbcTemplate.update(sb.toString());
	}

	public int getSusupinfoApproveCount(String begin, String end, String glbm) {
		String tmpSql = null;
		if (glbm.substring(4, 6).equals("00")) {
			tmpSql = "Select * from veh_suspinfo Where YWZT='12' and XXLY='0' and bkjg in (Select xjjg from frm_prefecture Where dwdm='"
					+ glbm + "')";
		} else {
			tmpSql = "Select * from veh_suspinfo Where YWZT='12' and XXLY='0' and bkfwlx='1' and bkjg in (Select xjjg from frm_prefecture Where dwdm='"
					+ glbm + "')";
		}
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin
					+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end
					+ " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}

	public int getSusupinfoApproveOverTimeCount(String begin, String end,
			String glbm) {
		String tmpSql = null;
		if (glbm.substring(4, 6).equals("00")) {
			tmpSql = "Select * from veh_suspinfo Where YWZT='12' and XXLY='0' and bkjg in (Select xjjg from frm_prefecture Where dwdm='"
					+ glbm + "')";
		} else {
			tmpSql = "Select * from veh_suspinfo Where YWZT='12' and XXLY='0' and bkfwlx='1' and bkjg in (Select xjjg from frm_prefecture Where dwdm='"
					+ glbm + "')";
		}
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin
					+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end
					+ " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		tmpSql += " and bkxh IN (SELECT bkxh FROM Audit_Approve WHERE bzw='1' AND (SYSDATE-czsj)*24*3600>7200)";
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}

	public int getExpireCancelSuspinfoCount(String begin, String end,
			String glbm, String yhmc) {
		String tmpSql = "Select * from VEH_SUSPINFO Where (((BKDL='1' or BKDL= '2') and bkjg='"
				+ glbm
				+ "') or (bkdl='3' and bkr='"
				+ yhmc
				+ "')) and XXLY='0' and  YWZT = '14'";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin
					+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end
					+ " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		//System.out.println(tmpSql);
		//int count = this.getRecordCounts(tmpSql, 0);
		return this.getSelf().getRecordCounts(tmpSql, 0);
	}

	public int getSuspinfoOuttimeCountCount(String begin, String end,
			String glbm, String yhmc) throws Exception {
		String tmpSql = "Select /*+index(veh_suspinfo IDX_VEH_SUSPINFO_BKSJ )+*/* from VEH_SUSPINFO Where BKJZSJ <= sysdate and (((BKDL='1' or BKDL= '2') and bkjg ='"
				+ glbm
				+ "') or (bkdl='3' and bkr='"
				+ yhmc
				+ "')) and XXLY='0' and ywzt = '14' and cxsqsj is null and by2 is not null and ROUND(TO_NUMBER(sysdate - to_date(by2,'yyyy-mm-dd hh24:mi:ss'))) >= 1 ";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin
					+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end
					+ " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		//System.out.println(tmpSql);
		//Map map = this.getSelf().findPageForMap(tmpSql, 1, 10);
		//int count = (Integer) map.get("total");
		return this.getSelf().getRecordCounts(tmpSql, 0);
	}

	public Map getSuspinfoOuttime(int page,int pagesize) throws Exception {
		String sql = "select a.bkr, nvl(b.lxdh3, b.lxdh1) as lxdh, a.overcount "
				+ " from (Select bkr, count(1) as overcount from VEH_SUSPINFO "
				+ " Where XXLY = '0' and ywzt = '14' and cxsqsj is null and by2 is not null "
				+ " and ROUND(TO_NUMBER(sysdate - by2) * 24 * 60 * 60) >= 24 * 60 * 60 "
				+ " and bksj >= to_date('2013-10-17 00:00:00', 'yyyy-mm-dd hh24:mi:ss') "
				+ " and bksj <= to_date('2014-10-17 23:59:59', 'yyyy-mm-dd hh24:mi:ss') "
				+ " group by bkr having count(1) > 0) a, frm_sysuser b where a.bkr = b.yhdh and b.zt = '1' ";
		return this.findPageForMap(sql, page, pagesize);
	}

	public List<Code> getUserSuspinfo(String begin, String end, String yhdh)
			throws Exception {
		String tmpSql = "Select a.dmz, to_char(nvl(b.c,0)) dmsm1 from (Select dmz from frm_code Where dmlb='120008' order by dmz) a,";
		tmpSql = tmpSql
				+ "(Select count(bkxh) c,ywzt from veh_suspinfo Where xxly='0' and (bkr='"
				+ yhdh + "' or cxsqr='" + yhdh + "')";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin
					+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql
					+ " and bksj <= to_date('"
					+ end
					+ " 23:59:59','yyyy-mm-dd hh24:mi:ss') group by ywzt) b Where a.dmz=b.ywzt(+)";
		}
		return this.queryList(tmpSql, Code.class);
	}

	public int getSuspinfoBkfwlxCount(String begin, String end, String yhdh)
			throws Exception {
		String tmpSql = "Select * from VEH_SUSPINFO Where XXLY='0' and bkfwlx='2' and bjzt='1' and bkr='"
				+ yhdh + "'";
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin
					+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end
					+ " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}

	public int getSuspinfoNoBkfwlxCount(String begin, String end, String yhdh)
			throws Exception {
		String tmpSql = "Select * from VEH_SUSPINFO Where XXLY='0' and bkfwlx='2' and bjzt='0' and bkr='"
				+ yhdh + "'";
		if (tmpSql != null && tmpSql.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date('" + begin
					+ " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (tmpSql != null && tmpSql.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date('" + end
					+ " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		int count = this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
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
	 
	 public List<Map<String,Object>> queryBatchApproveList() throws Exception {
		String sql = "select * from veh_suspinfo where ywzt='12' and jlzt='0' and " +
				"bkfwlx='3' order by bksj desc";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
}
