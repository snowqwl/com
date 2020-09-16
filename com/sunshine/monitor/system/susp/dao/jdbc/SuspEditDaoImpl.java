package com.sunshine.monitor.system.susp.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;

@Repository("suspinfoEditDao")
public class SuspEditDaoImpl extends BaseDaoImpl implements SuspinfoEditDao {

	public Map findSuspinfoForMap(Map map, VehSuspinfo info) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(info.getBkdl()))
			sb.append(" and BKDL = '").append(info.getBkdl()).append("' ");

		if (StringUtils.isNotBlank(info.getBklb()))
			sb.append(" and BKLB = '").append(info.getBklb()).append("' ");

		if (StringUtils.isNotBlank(info.getHphm()))
			sb.append(" and hphm like '%").append(info.getHphm()).append("%' ");

		if (StringUtils.isNotBlank(info.getHpzl()))
			sb.append(" and hpzl = '").append(info.getHpzl()).append("' ");

		if (StringUtils.isNotBlank(info.getKssj()))
			sb.append(" and bksj >=to_date('").append(info.getKssj()).append(
					"', 'yyyy-mm-dd hh24:mi:ss') ");

		if (StringUtils.isNotBlank(info.getJssj()))
			sb.append(" and bksj <=to_date('").append(info.getJssj()).append(
					"', 'yyyy-mm-dd hh24:mi:ss') ");

		if (StringUtils.isBlank(info.getKssj())
				&& StringUtils.isBlank(info.getJssj())) {
			sb.append("  and bksj >= sysdate-365  ");
		}

		StringBuffer sql = new StringBuffer(
				"select bkxh, hpzl,hphm,bkdl,bklb,to_char(bksj,'yyyy-mm-dd hh24:mi:ss') as bksj,to_char(bkjzsj,'yyyy-mm-dd hh24:mi:ss') as bkjzsj, lar,ladw,ywzt,BKJGMC,bkfwlx  from ");

		sql.append(" VEH_SUSPINFO where 1=1 ");
		if (StringUtils.isNotBlank(info.getBkfwlx())){
			sql.append(" and BKFWLX = '").append(info.getBkfwlx()).append("'");
		}

		if (StringUtils.isNotBlank(info.getYwzt())) {
			sql.append(" and ywzt in(").append(info.getYwzt()).append(") ");
		} else {
			sql.append(" and ywzt in('11','13')");
		}

		sql.append(sb);
		sql.append(" and bkr = '").append(info.getBkr()).append("' ");
		sql.append(" order by gxsj desc ");

		Map mapt = new HashMap();
		mapt.put("sql", sql);
		mapt.put("data", this.getSelf().findPageForMap(sql.toString(),
				Integer.parseInt(map.get("curPage").toString()),
				Integer.parseInt(map.get("pageSize").toString())));
		return mapt;
	}

	public VehSuspinfo getSuspinfoDetailForBkxh(String bkxh) throws Exception {
		String sql = "select BKXH, YSBH, HPHM, HPZL, BKDL, BKLB, BKQSSJ, BKJZSJ, JYAQ, BKFWLX, BKFW, BKJB, BKXZ, SQSB, BJYA, BJFS, DXJSHM, LAR, LADW, LADWLXDH, CLPP, HPYS, CLXH, CLLX, CSYS, CLSBDH, FDJH, CLTZ, CLSYR, SYRLXDH, SYRXXDZ, BKR, BKJG, BKJGMC, BKJGLXDH, BKSJ, CXSQR, CXSQDW, CXSQDWMC, CXSQSJ, CKYYDM, CKYYMS, YWZT, JLZT, GXSJ, XXLY, BKPT, BY1, BY2, BY3, BY4, BY5, BJZT, MHBKBJ, CXSQRJH, BKRJH, BJSJ, CXSQRMC, BKRMC from veh_suspinfo where  bkxh = ?";

		List<VehSuspinfo> list = this.queryForList(sql, new Object[]{bkxh}, VehSuspinfo.class);
		VehSuspinfo info = null;
		if (list.size() > 0)
			info = (VehSuspinfo) list.get(0);

		return info;
	}

	public VehSuspinfo getCitySuspinfoDetailForBkxh(String bkxh, String cityname)
			throws Exception {
        /*
		String sql = "select v.* from veh_suspinfo@" + cityname
				+ " v where  bkxh = '" + bkxh + "' ";
		List list = this.queryList(sql, VehSuspinfo.class);
		*/
		String sql = "select v.* from veh_suspinfo@" + cityname+ " v where  bkxh = ?";
		List list = this.queryForList(sql, new Object[]{bkxh}, VehSuspinfo.class);
		VehSuspinfo info = null;
		if (list.size() > 0)
			info = (VehSuspinfo) list.get(0);

		return info;
	}

	// 更新布控表
	public int updateSuspinfo(VehSuspinfo suspInfo) throws Exception {

		StringBuffer sql = new StringBuffer(
				"UPDATE veh_suspinfo set bksj = sysdate,gxsj = sysdate ");
		if (StringUtils.isNotBlank(suspInfo.getHphm())) {
			sql.append(", hphm = '").append(suspInfo.getHphm()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getHpzl())){
			sql.append(", hpzl = '").append(suspInfo.getHpzl()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkdl())) {
			sql.append(", bkdl = '").append(suspInfo.getBkdl()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBklb())) {
			sql.append(",bklb = '").append(suspInfo.getBklb()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getClxh())) {
			sql.append(",clxh = '").append(suspInfo.getClxh()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getFdjh())) {
			sql.append(",fdjh = '").append(suspInfo.getFdjh()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getClsyr())) {
			sql.append(", clsyr = '").append(suspInfo.getClsyr()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getSyrlxdh())) {
			sql.append(", syrlxdh = '").append(suspInfo.getSyrlxdh()).append(
					"' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getSyrxxdz())) {
			sql.append(", syrxxdz = '").append(suspInfo.getSyrxxdz()).append(
					"' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkqssj())) {
			sql.append(", bkqssj = to_date('").append(
					suspInfo.getBkqssj().substring(0, 10) + " 00:00:00")
					.append("','yyyy-mm-dd hh24:mi:ss') ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkjzsj())) {
			sql.append(", bkjzsj = to_date('").append(
					suspInfo.getBkjzsj().substring(0, 10) + " 23:59:59")
					.append("','yyyy-mm-dd hh24:mi:ss') ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkjglxdh())) {
			sql.append(", bkjglxdh = '").append(suspInfo.getBkjglxdh()).append(
					"' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkfw())) {
			sql.append(", bkfw = '").append(suspInfo.getBkfw()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getJyaq())) {
			sql.append(", jyaq = '").append(suspInfo.getJyaq()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getClpp())) {
			sql.append(",clpp = '").append(suspInfo.getClpp()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getCsys())) {
			sql.append(", csys = '").append(suspInfo.getCsys()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBjfs())) {
			sql.append(", bjfs = '").append(suspInfo.getBjfs()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkjb())) {
			sql.append(", bkjb = '").append(suspInfo.getBkjb()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getDxjshm())) {
			sql.append(", dxjshm = '").append(suspInfo.getDxjshm())
					.append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkxz())) {
			sql.append(", bkxz = '").append(suspInfo.getBkxz()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getBjya())) {
			sql.append(", bjya = '").append(suspInfo.getBjya()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getHpys())) {
			sql.append(", hpys = '").append(suspInfo.getHpys()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getSqsb())) {
			sql.append(", sqsb = '").append(suspInfo.getSqsb()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getLadw())) {
			sql.append(", ladw = '").append(suspInfo.getLadw()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getLadwlxdh())) {
			sql.append(", ladwlxdh = '").append(suspInfo.getLadwlxdh()).append(
					"' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getLar())) {
			sql.append(", lar = '").append(suspInfo.getLar()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getCllx())) {
			sql.append(", cllx = '").append(suspInfo.getCllx()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getYwzt())) {
			sql.append(", ywzt = '").append(suspInfo.getYwzt()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getCltz())) {
			sql.append(", cltz = '").append(suspInfo.getCltz()).append("' ");
		}
		if (StringUtils.isNotBlank(suspInfo.getClsbdh())) {
			sql.append(", clsbdh = '").append(suspInfo.getClsbdh())
					.append("' ");
		}

		sql.append("  where bkxh = '").append(suspInfo.getBkxh()).append("' ");

		int i = this.jdbcTemplate.update(sql.toString());

		return i;
	}

	// 更新susp_picrec(图片表)

	// 删除未审批布控记录需更新布控表
	public int updateSuspInfoForDel(VehSuspinfo info) throws Exception {

		StringBuffer sb = new StringBuffer(
				"UPDATE veh_suspinfo set ywzt = '99',jlzt = '2',gxsj = sysdate,ckyydm = '00' ");
		sb.append(",ckyyms = '").append(info.getCkyyms()).append("' ");
		sb.append(", cxsqr = '").append(info.getCxsqr()).append("' ");
		sb.append(", cxsqrjh = '").append(info.getCxsqrjh()).append("' ");
		sb.append(", cxsqrmc = '").append(info.getCxsqrmc()).append("' ");
		sb.append(", cxsqdw = '").append(info.getCxsqdw()).append("' ");
		sb.append(", cxsqdwmc = '").append(info.getCxsqdwmc()).append("' ");
		sb.append(" , cxsqsj   = sysdate ");
		sb.append("  where bkxh = '").append(info.getBkxh()).append("' ");

		int i = this.jdbcTemplate.update(sb.toString());

		return i;

	}

	// 删除未审批布控写入日志表
	public int insertBusinessLogForDel(VehSuspinfo info, String ssjz)
			throws Exception {

		StringBuffer sql = new StringBuffer(
				"INSERT into BUSINESS_LOG(xh, ywxh, ywlb, ywjb, czrdh, czrjh, czrdwdm, czrdwjz) ");
		sql.append(" values(seq_business_log_xh.nextval,'").append(
				info.getBkxh()).append("' ").append(",'13','2','").append(
				info.getCxsqr()).append("', '").append(info.getCxsqrjh())
				.append("' ").append(",'").append(info.getCxsqdw()).append(
						"','").append(ssjz).append("') ");

		int i = this.jdbcTemplate.update(sql.toString());

		return i;
	}

	public List getBkfwListTree() throws Exception {
		String sql = "SELECT DWDM, URL, PORT, CONTEXT, JB, JDMC, SN, SJJD, BZ FROM CODE_URL order by dwdm";

		return queryForList(sql, CodeUrl.class);

	}

	public List getSuspListForHphm(String hphm, String hpzl) throws Exception {
		String sql = "select * from veh_suspinfo where hphm = ?";
		List<String> list = new ArrayList<String>();
		list.add(hphm);
		if ((hpzl != null) && (hpzl.length() > 0)) {
			sql = sql + " and hpzl = ?";
			list.add(hpzl);
		}
		return this.queryForList(sql, list.toArray(), VehSuspinfo.class);
	}

	public int getSuspinfoEditCount(String begin, String end, String yhdh,
			String bkfwlx) throws Exception {
		String tmpSql = "Select * from veh_suspinfo Where BKFWLX='" + bkfwlx
				+ "' and YWZT in ('13') and XXLY='0' and bkr='" + yhdh + "'";
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

	public AuditApprove getAuditApprove(String bkxh)
			throws Exception {
        String sql = " select * from audit_approve where bkxh='"+bkxh+"' and czsj = (select max(czsj) from audit_approve where bkxh = '"+bkxh+"')";
        AuditApprove au = this.queryForObject(sql,AuditApprove.class);
        if(au==null)
        	return null;
		return au;
	}

}
