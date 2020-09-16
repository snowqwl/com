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
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(info.getBkdl())) {
			sb.append(" and BKDL = ? ");
			param.add(info.getBkdl());
		}

		if (StringUtils.isNotBlank(info.getBklb())){
			sb.append(" and BKLB = ? ");
			param.add(info.getBklb());
		}

		if (StringUtils.isNotBlank(info.getHphm())) {
			sb.append(" and hphm like ? ");
			param.add("%" + info.getHphm() + "%");
		}
		if (StringUtils.isNotBlank(info.getHpzl())){
			sb.append(" and hpzl = ? ");
			param.add(info.getHpzl());
		}

		if (StringUtils.isNotBlank(info.getKssj())){
			sb.append(" and bksj >=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(info.getKssj());
		}

		if (StringUtils.isNotBlank(info.getJssj())){
			sb.append(" and bksj <=to_date(?, 'yyyy-mm-dd hh24:mi:ss') ");
			param.add(info.getJssj());
		}

		if (StringUtils.isBlank(info.getKssj())
				&& StringUtils.isBlank(info.getJssj())) {
			sb.append("  and bksj >= sysdate-365  ");
		}
		List list = new ArrayList<>();
		StringBuffer sql = new StringBuffer(
				"select bkxh, hpzl,hphm,bkdl,bklb,to_char(bksj,'yyyy-mm-dd hh24:mi:ss') as bksj,to_char(bkjzsj,'yyyy-mm-dd hh24:mi:ss') as bkjzsj, lar,ladw,ywzt,BKJGMC,bkfwlx  from ");

		sql.append(" VEH_SUSPINFO where 1=1 ");
		if (StringUtils.isNotBlank(info.getBkfwlx())){
			sql.append(" and BKFWLX = ?");
			list.add(info.getBkfwlx());
		}

		if (StringUtils.isNotBlank(info.getYwzt())) {
			sql.append(" and ywzt in(?) ");
			list.add(info.getYwzt());
		} else {
			sql.append(" and ywzt in('11','13')");
		}
		sql.append(sb);
		for (Object o : param) {
			list.add(o);
		}

		sql.append(" and bkr = ? ");
		list.add(info.getBkr());
		sql.append(" order by gxsj desc ");

		Object[] array = list.toArray(new Object[list.size()]);
		Map mapt = new HashMap();
		mapt.put("sql", sql);
		mapt.put("data", this.getSelf().findPageForMap(sql.toString(),array,
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

		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(
				"UPDATE veh_suspinfo set bksj = sysdate,gxsj = sysdate ");
		if (StringUtils.isNotBlank(suspInfo.getHphm())) {
			sql.append(", hphm = ? ");
			param.add(suspInfo.getHphm());
		}
		if (StringUtils.isNotBlank(suspInfo.getHpzl())){
			sql.append(", hpzl = ? ");
			param.add(suspInfo.getHpzl());
		}
		if (StringUtils.isNotBlank(suspInfo.getBkdl())) {
			sql.append(", bkdl = ? ");
			param.add(suspInfo.getBkdl());
		}
		if (StringUtils.isNotBlank(suspInfo.getBklb())) {
			sql.append(",bklb = ? ");
			param.add(suspInfo.getBklb());
		}
		if (StringUtils.isNotBlank(suspInfo.getClxh())) {
			sql.append(",clxh = ? ");
			param.add(suspInfo.getClxh());
		}
		if (StringUtils.isNotBlank(suspInfo.getFdjh())) {
			sql.append(",fdjh = ? ");
			param.add(suspInfo.getFdjh());
		}
		if (StringUtils.isNotBlank(suspInfo.getClsyr())) {
			sql.append(", clsyr = ? ");
			param.add(suspInfo.getClsyr());
		}
		if (StringUtils.isNotBlank(suspInfo.getSyrlxdh())) {
			sql.append(", syrlxdh = ? ");
			param.add(suspInfo.getSyrlxdh());
		}
		if (StringUtils.isNotBlank(suspInfo.getSyrxxdz())) {
			sql.append(", syrxxdz = ? ");
			param.add(suspInfo.getSyrxxdz());
		}
		if (StringUtils.isNotBlank(suspInfo.getBkqssj())) {
			sql.append(", bkqssj = to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
			param.add(suspInfo.getBkqssj().substring(0, 10)+" 00:00:00");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkjzsj())) {
			sql.append(", bkjzsj = to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
			param.add(suspInfo.getBkjzsj().substring(0, 10)+" 23:59:59");
		}
		if (StringUtils.isNotBlank(suspInfo.getBkjglxdh())) {
			sql.append(", bkjglxdh = ? ");
			param.add(suspInfo.getBkjglxdh());
		}
		if (StringUtils.isNotBlank(suspInfo.getBkfw())) {
			sql.append(", bkfw = ? ");
			param.add(suspInfo.getBkfw());
		}
		if (StringUtils.isNotBlank(suspInfo.getJyaq())) {
			sql.append(", jyaq = ? ");
			param.add(suspInfo.getJyaq());
		}
		if (StringUtils.isNotBlank(suspInfo.getClpp())) {
			sql.append(",clpp = ? ");
			param.add(suspInfo.getClpp());
		}
		if (StringUtils.isNotBlank(suspInfo.getCsys())) {
			sql.append(", csys = ? ");
			param.add(suspInfo.getCsys());
		}
		if (StringUtils.isNotBlank(suspInfo.getBjfs())) {
			sql.append(", bjfs = ? ");
			param.add(suspInfo.getBjfs());
		}
		if (StringUtils.isNotBlank(suspInfo.getBkjb())) {
			sql.append(", bkjb = ? ");
			param.add(suspInfo.getBkjb());
		}
		if (StringUtils.isNotBlank(suspInfo.getDxjshm())) {
			sql.append(", dxjshm = ? ");
			param.add(suspInfo.getDxjshm());
		}
		if (StringUtils.isNotBlank(suspInfo.getBkxz())) {
			sql.append(", bkxz = ? ");
			param.add(suspInfo.getBkxz());
		}
		if (StringUtils.isNotBlank(suspInfo.getBjya())) {
			sql.append(", bjya = ? ");
			param.add(suspInfo.getBjya());
		}
		if (StringUtils.isNotBlank(suspInfo.getHpys())) {
			sql.append(", hpys = ? ");
			param.add(suspInfo.getHpys());
		}
		if (StringUtils.isNotBlank(suspInfo.getSqsb())) {
			sql.append(", sqsb = ? ");
			param.add(suspInfo.getSqsb());
		}
		if (StringUtils.isNotBlank(suspInfo.getLadw())) {
			sql.append(", ladw = ? ");
			param.add(suspInfo.getLadw());
		}
		if (StringUtils.isNotBlank(suspInfo.getLadwlxdh())) {
			sql.append(", ladwlxdh = ? ");
			param.add(suspInfo.getLadwlxdh());
		}
		if (StringUtils.isNotBlank(suspInfo.getLar())) {
			sql.append(", lar = ? ");
			param.add(suspInfo.getLar());
		}
		if (StringUtils.isNotBlank(suspInfo.getCllx())) {
			sql.append(", cllx = ? ");
			param.add(suspInfo.getCllx());
		}
		if (StringUtils.isNotBlank(suspInfo.getYwzt())) {
			sql.append(", ywzt = ? ");
			param.add(suspInfo.getYwzt());
		}
		if (StringUtils.isNotBlank(suspInfo.getCltz())) {
			sql.append(", cltz = ? ");
			param.add(suspInfo.getCltz());
		}
		if (StringUtils.isNotBlank(suspInfo.getClsbdh())) {
			sql.append(", clsbdh = ? ");
			param.add(suspInfo.getClsbdh());
		}

		sql.append("  where bkxh = ? ");
		param.add(suspInfo.getBkxh());

		Object[] array = param.toArray(new Object[param.size()]);
		int i = this.jdbcTemplate.update(sql.toString(),array);

		return i;
	}

	// 更新susp_picrec(图片表)

	// 删除未审批布控记录需更新布控表
	public int updateSuspInfoForDel(VehSuspinfo info) throws Exception {
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer(
				"UPDATE veh_suspinfo set ywzt = '99',jlzt = '2',gxsj = sysdate,ckyydm = '00' ");
		sb.append(",ckyyms = ? ");
		param.add(info.getCkyyms());
		sb.append(", cxsqr = ? ");
		param.add(info.getCxsqr());
		sb.append(", cxsqrjh = ? ");
		param.add(info.getCxsqrjh());
		sb.append(", cxsqrmc = ? ");
		param.add(info.getCxsqrmc());
		sb.append(", cxsqdw = ? ");
		param.add(info.getCxsqdw());
		sb.append(", cxsqdwmc = ? ");
		param.add(info.getCxsqdwmc());
		sb.append(" , cxsqsj   = sysdate ");
		sb.append("  where bkxh = ? ");
		param.add(info.getBkxh());
		Object[] array = param.toArray(new Object[param.size()]);
		int i = this.jdbcTemplate.update(sb.toString(),array);

		return i;

	}

	// 删除未审批布控写入日志表
	public int insertBusinessLogForDel(VehSuspinfo info, String ssjz)
			throws Exception {
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(
				"INSERT into BUSINESS_LOG(xh, ywxh, ywlb, ywjb, czrdh, czrjh, czrdwdm, czrdwjz) ");
		sql.append(" values(seq_business_log_xh.nextval,? ").append(",'13','2',?, ?,?,?) ");
		param.add(info.getBkxh());
		param.add(info.getCxsqr());
		param.add(info.getCxsqrjh());
		param.add(info.getCxsqdw());
		param.add(ssjz);

		int i = this.jdbcTemplate.update(sql.toString(),param.toArray());
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
		List param = new ArrayList<>();
		String tmpSql = "Select * from veh_suspinfo Where BKFWLX=? and YWZT in ('13') and " +
				"XXLY='0' and bkr=?";
		param.add(bkfwlx);
		param.add(yhdh);
		if (begin != null && begin.length() > 0) {
			tmpSql = tmpSql + " and bksj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(begin+" 00:00:00");
		}
		if (end != null && end.length() > 0) {
			tmpSql = tmpSql + " and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			param.add(end+" 23:59:59");
		}
		int count = this.getSelf().getRecordCounts(tmpSql, param.toArray(),0);
		return count;
	}

	public AuditApprove getAuditApprove(String bkxh)
			throws Exception {
        String sql = " select * from audit_approve where bkxh=? and czsj = (select max(czsj) from" +
				" audit_approve where bkxh = ?)";
        AuditApprove au = this.queryForObject(sql,new Object[]{bkxh,bkxh},
				AuditApprove.class);
        if(au==null)
        	return null;
		return au;
	}

}
