package com.sunshine.monitor.system.query.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.query.dao.StatSignQueryDao;

@Repository("statSignQueryDao")
public class StatSignQueryDaoImpl extends BaseDaoImpl implements
		StatSignQueryDao {

	public List<Map<String,Object>> getSignQuery(String kssj,String jssj,String glbm,String bkdl,String jb) {
		List<List<String>> listData = null;
		List param = new ArrayList<>();
		StringBuffer sb = new StringBuffer("select bmmc,(case when bmmc = '直属部门' then");
		sb.append(" ? else  glbm end) glbm, case when yqs=0 then 0 else Round(sjqs/yqs,4) * 100 " +
				"end yqsl,case when yqs=0 then 0 else Round(qrwx/yqs,4) * 100 end qrwl,case when xdzl=0 then 0 else Round(yxdzl/xdzl,4) * 100 end yxdl");
		sb.append(" from (select lx as bmmc,jb, glbm, sum(yqs) as yqs,sum(sjqs) as sjqs,sum(qrwx) as qrwx,sum(yxdzl) as yxdzl, sum(xdzl) as xdzl");
		sb.append(" from (select fd.bmmc,fd.jb,(case when sfzs = '1' then substr(?,0,6)||'1'  " +
				"else fd.glbm end) glbm ,(case when sfzs = '1' then '直属部门'  else fd.bmmc end) lx ,nvl(yqs, 0) yqs,");
		sb.append(" nvl(sjqs,0) sjqs, nvl(qrwx,0) qrwx,nvl(yxdzl,0) yxdzl, nvl(xdzl,0) xdzl from (select jb, bmmc, xjjg as glbm, sfzs from frm_department, frm_prefecture");
		sb.append(" where dwdm = ? and frm_department.glbm = frm_prefecture.xjjg and (jb = ? or " +
				"jb = ? + 1) and (substr(dwdm, length(dwdm) - 5, 6) = '000000' or");
		sb.append(" substr(xjjg, 0, 8) = substr(dwdm, 0, 8)) and (glbm <> ?  or sfzs = '2')) fd " +
				"left join (select bm.glbm,");
		sb.append(" bm.bmmc,sum(yqs) as yqs,sum(sjqs) as sjqs,sum(qrwx) as qrwx, sum(yxdzl) as yxdzl,sum(xdzl) as xdzl from (select m.*, n.xjjg");
		sb.append(" from (select jb, bmmc, xjjg as glbm from frm_department,frm_prefecture where dwdm = '441200000000' and frm_department.glbm = frm_prefecture.xjjg");
		sb.append(" and (jb = 2 + 1 or jb = 2)) m   inner join frm_prefecture n on m.glbm = n.dwdm where substr(glbm, length(glbm) - 5, 6) = '000000' or substr(glbm, 0, 8) = ");
		sb.append(" substr(n.xjjg, 0, 8)) bm,(select bjdwdm, count(bjxh) yqs,sum(decode(qrzt, 0, 0, 1)) sjqs, sum(decode(qrzt, 2, 1, 0)) qrwx,sum(case when qrzt = '1' then");
		sb.append(" 1  else 0 end) yxdzl, sum(case when qrzt = '1' and sfxdzl = '1' then 1 else 0 end) xdzl from veh_alarmrec Where 1 = 1 ");
		param.add(glbm);
		param.add(glbm);
		param.add(glbm);
		param.add(jb);
		param.add(jb);
		param.add(glbm);

		if (kssj != null && kssj.length() > 0) {
			sb.append("and bjsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			param.add(kssj);
		}
		if (jssj != null && jssj.length() > 0) {
			sb.append(" and bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			param.add(jssj);
		}
		if (bkdl != null && bkdl.length() > 0) {
			sb.append(" and bjdl = ?" );
			param.add(bkdl);
		}
		sb.append("  group by bjdwdm) a where bm.xjjg = a.bjdwdm group by bm.glbm, bm.bmmc) tip on fd.glbm = tip.glbm order by fd.glbm) group by lx, jb, glbm order by glbm, jb)");
	    List<Map<String,Object>> data = this.jdbcTemplate.queryForList(sb.toString(),param.toArray());
	    return data;
	}

}
