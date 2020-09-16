package com.sunshine.monitor.system.alarm.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.dao.page.PagingParameter;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.RealalarmDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

@Repository
public class RealalarmDaoImpl extends BaseDaoImpl implements RealalarmDao {

	public List<VehAlarmrec> getAlarmForGIS(String bjsj) throws Exception {
		String sql = "";
		sql = sql + "select m.bkjg,m.bkjb,n.* from ";
		sql = sql + "(select bkxh,bkjg,bkjb from veh_suspinfo) m,";
		sql = sql
				+ "(select bjxh,bjdwdm,bjlx,bkxh,bjsj,bjdwmc,bjdwlxdh,hphm,hpzl,sbbh,kdbh,fxbh from veh_alarmrec where bjsj >to_date(?,'yyyy-mm-dd hh24:mi:ss') order by bjsj) n ";
		sql = sql + "where m.bkxh=n.bkxh";
		List<VehAlarmrec> list = this.queryForList(sql,new Object[]{bjsj}, VehAlarmrec.class);
		return list;
	}

	public List<VehAlarmrec> getAlarmForTrace(String condition)
			throws Exception {
		String sql = "select bjxh,hpzl,hphm,bjlx,sbbh,kdbh,fxbh,bjsj,bkxh,gcxh,bjdwdm from veh_alarmrec where "
				+ condition + " order by bjsj";
		List<VehAlarmrec> list = this.queryForList(sql, VehAlarmrec.class);
		return list;
	}

	public VehAlarmrec getVehAlarmDetail(String kdbh, String bklb)
			throws Exception {
		String tmpSql = "select * from Veh_Realalarm where kdbh  in ('" + kdbh
				+ "')";
		if (!bklb.equals("")) {
			tmpSql = tmpSql + " and bjlx in (" + bklb + ") order by bjxh desc";
		}
		List<VehAlarmrec> list = this.queryForList(tmpSql, VehAlarmrec.class);
		VehAlarmrec va;
		if ((list == null) || (list.size() == 0)) {
			va = null;
		} else {
			va = list.get(0);
			tmpSql = "select qrzt from  VEH_ALARMREC where bjxh ='"
					+ va.getBjxh() + "'";
			try{
			List qrzt = this.jdbcTemplate
					.queryForList(tmpSql);
			
				if(qrzt.size()>0){
					Map map=(Map)qrzt.get(0);
			va.setQrzt(map.get("qrzt").toString());	
				}
				else
					va.setQrzt("");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return va;
	}

	public VehAlarmrec getVehRealalarmM(String[] kds, String[] bklbs)
			throws Exception {
		String kdbh = "";
		String bklb = "";

		for (int i = 0; i < bklbs.length; i++) {
			bklb = bklb + bklbs[i] + ",";
		}

		if (bklb.length() > 0) {
			bklb = bklb.substring(0, bklb.length() - 1);
		}

		if (kds.length > 0) {
			kdbh = kds[0];
		}

		String tmpSql = "select * from Veh_Realalarm where kdbh ='" + kdbh;
		tmpSql = tmpSql + "' and bjlx in (" + bklb + ") order by bjxh desc";

		List<VehAlarmrec> list = this.queryForList(tmpSql, VehAlarmrec.class);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return (VehAlarmrec) list.get(0);
	}

	public List<VehAlarmrec> getVehAlarmList(String[] kds, String[] bklb)
			throws Exception {
		String kdbh = "";
		String bklbs = "";
		for (int i = 0; i < bklb.length; i++) {
			bklbs = bklbs + bklb[i] + ",";
		}
		for (int j = 0; j < kds.length; j++) {
			kdbh = kdbh + "'" + kds[j] + "',";
		}
		if (kdbh.length() > 0) {
			/*
			 * kdbh = "'" + kdbh.substring(0, kdbh.length() - 1).replaceAll(",",
			 * "','") + "'";
			 */
			kdbh = kdbh.substring(0, kdbh.length() - 1);
		}
		if (bklbs.length() > 0) {
			bklbs = "'"
					+ bklbs.substring(0, bklbs.length() - 1).replaceAll(",",
							"','") + "'";
		}
		String sql = "";
		sql = sql + "select a.* from ";
		sql = sql + "(select * from Veh_Realalarm) a,";
		sql = sql
				+ "(select max(bjsj) bjsj,kdbh from Veh_Realalarm where kdbh in ("
				+ kdbh + ")  and bjlx in (" + bklbs + ") group by kdbh) b ";
		sql = sql
				+ "where a.bjsj=b.bjsj and a.kdbh=b.kdbh order by b.bjsj desc";
		List<VehAlarmrec> list = this.queryForList(sql, VehAlarmrec.class);

		return list;
	}

	public VehAlarmrec getVehAlarmDetail(String bjxh) throws Exception {
		String tmpSql = "select * from Veh_alarmrec where bjxh=?";
		List<VehAlarmrec> list = this.queryForList(tmpSql,new String[]{bjxh}, VehAlarmrec.class);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return list.get(0);
	}

	public List<VehSuspinfo> getAlarmSuspList(String bjxh) throws Exception {
		String tmpSql = "select * from Veh_alarmrec where bjxh=?";
		List<VehSuspinfo> retList = new ArrayList<VehSuspinfo>();
		try {
			String suspInfoSql = "";
			List<VehAlarmrec> list = this.queryForList(tmpSql,new String[]{bjxh},
					VehAlarmrec.class);
			if (list.size() == 0) {
				return null;
			}
			VehAlarmrec va = list.get(0);
			if (va != null) {
				/*
				suspInfoSql = "select *  from veh_suspinfo where bkxh='"
						+ va.getBkxh() + "'";
				retList = this.jdbcTemplate.queryForList(suspInfoSql,
						VehSuspinfo.class);
						*/
				suspInfoSql = "SELECT * FROM veh_suspinfo where bkxh = ?";
				retList = this.queryForList(suspInfoSql, new Object[]{va.getBkxh()}, VehSuspinfo.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;
	}

	public long getRealAlarmByKdbh(String kdbh) throws Exception {
		String sql = "select count(1) from veh_alarmrec where kdbh = ? and bjsj <= sysdate and bjsj >= trunc(sysdate)";
		return this.jdbcTemplate.queryForLong(sql,new String[]{kdbh});
	}

	public Map<String, Object> findPageAlarmForMap(Map<String,Object> map,
			Class<?> classz) throws Exception {
		int curPage = 1;
		int pageSize = 10;
		StringBuffer sb = new StringBuffer();
		Map<String, Object> result = null;
	

		List vl=new ArrayList<>();
		if (map != null) {
			Set<String> set = map.keySet();
			for (Object o : set) {
				String val = o.toString();
				if (val.equalsIgnoreCase("kssj")) {
					sb.append(" and v.bjsj > to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					vl.add(map.get(val));
				} else if (val.equalsIgnoreCase("jssj")) {
					sb.append(" and v.bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					vl.add(map.get(val));
				} else if(val.equalsIgnoreCase("hphm")){
					sb.append(" and v.hphm like '%"+map.get(val)+"%' ");
					vl.add("%"+map.get(val)+"%");
				} else if (!(val.equalsIgnoreCase("page") || val
						.equalsIgnoreCase("rows"))) {
					vl.add(map.get(val));
					sb.append(" and v." + val + "='" + map.get(val) + "'");
				}
			}
			if (map.containsKey("page")) {
				curPage = Integer.parseInt(map.get("page").toString());
			}
			if (map.containsKey("rows")) {
				pageSize = Integer.parseInt(map.get("rows").toString());
			}
		}
		StringBuffer sql = new StringBuffer(" select v.* from ");
		sql.append(" veh_realalarm v ");
		sql.append(" where 1=1 ").append(sb);
		sql.append(" order by ").append("bjsj");
		sql.append(" desc ");

		result = queryforMapnew(String.valueOf(curPage), String.valueOf(pageSize),sql.toString(),vl);
//				this.findPageForMap(sql.toString(), curPage, pageSize, classz);
		return result;
	}
	public Map queryforMapnew(String page, String rows, String sql, List vl) {
		String countsql = "SELECT COUNT(1) from (" + sql + ")s";
		int totalRows = -1;
		Map<String, Object> map = new HashMap<>();
		totalRows = this.jdbcTemplate.queryForInt(countsql.toString(), vl.toArray());
		List<Map<String, Object>> queryForList = new ArrayList<>();
		if (totalRows > 0) {
			String orgSql = "select * from (" + sql + " where rownum <= ?) where rn >= ?";
			PagingParameter pagingParameter = new PagingParameter(Integer.parseInt(page), Integer.parseInt(rows),
					totalRows);
			Map<String, Integer> map1 = pagingParameter.getStartAndEndRow();
			int start = map1.get("start");
			int end = map1.get("end");
			vl.add(end);
			vl.add(start);
			queryForList = this.jdbcTemplate.queryForList(orgSql, vl.toArray());
		}
		map.put("rows", queryForList);
		map.put("total", totalRows);

		return map;
	}
}