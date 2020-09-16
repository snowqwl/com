package com.sunshine.monitor.system.veh.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.veh.bean.VehRealpass;
import com.sunshine.monitor.system.veh.dao.RealDao;

@Repository("realDao")
public class RealDaoImpl extends BaseDaoImpl implements RealDao {

	public VehRealpass getVehRealPassM(String kdbh, String fxbh, String cdbh) {
		String tmpSql = "select * from Veh_Realpass where kdbh in ('" + kdbh
				+ "') ";
		if ((cdbh != null) && (!"".equals(cdbh.trim()))) {
			tmpSql = tmpSql + " and cdbh = '" + cdbh + "'";
		}
		if ((fxbh != null) && (!"".equals(fxbh.trim()))) {
			tmpSql = tmpSql + " and fxbh = '" + fxbh + "'";
		}
		tmpSql += " and rownum < 2";
		tmpSql = tmpSql + " order by gcsj desc,gcxh desc";
		// System.out.println(tmpSql);
		List list = this.queryForList(tmpSql, VehRealpass.class);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return (VehRealpass) list.get(0);
	}

	public Map findAlarmForMap(Map filter, StringBuffer condition) {

		StringBuffer sb = new StringBuffer(
				"select b.jdmc as bjcs,a.*  from  (select gcsj,bjxh,hpzl,hphm,bjlx,sbbh,kdbh,kdmc,fxbh,bjsj,bkxh,gcxh,bjdwdm from veh_alarmrec ");

		sb.append(condition);

		sb
				.append("  )a,(select * from code_url)b  where b.dwdm = substr(a.bjdwdm,0,4)||'00000000' ");

		System.out.println("sql ---> " + sb.toString());
		return this.findPageForMap(sb.toString(), Integer.parseInt(filter.get(
				"curPage").toString()), Integer.parseInt(filter.get("pageSize")
				.toString()));
	}

	public List getRealPassList(String[] kds) {
		String sql = "";
		String tmpSql = "";
		for (int i = 0; i < kds.length; i++) {
			tmpSql = tmpSql + "'" + kds[i] + "',";
		}
		tmpSql = tmpSql.substring(0, tmpSql.length() - 1);
		sql = sql + "select a.* from ";
		sql = sql + "(select * from veh_realpass) a,";
		sql = sql
				+ "(select max(gcsj) gcsj,kdbh from Veh_Realpass where kdbh in ("
				+ tmpSql + ") group by kdbh) b ";
		sql = sql
				+ "where a.gcsj=b.gcsj and a.kdbh=b.kdbh order by b.gcsj desc,b.kdbh";
		System.out.println("1:" + sql);
		List list = this.queryForList(sql, VehRealpass.class);
		return list;
	}

	public VehRealpass getVehRealDetail(String gcxh) {
		String tmpSql = "select * from Veh_passrec where gcxh='" + gcxh + "'";

		List list = this.queryForList(tmpSql, VehRealpass.class);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return (VehRealpass) list.get(0);
	}
	
	public VehRealpass getVehRealDetailByParams(String gcxh ) {
			StringBuffer sb=new StringBuffer();
			sb.append(" select t.* from veh_passrec t where 1=1");			
			if(gcxh!=null){
				sb.append(" and gcxh = '").append(gcxh).append("'");
			}
			sb.append(" and rownum < 2");
		 /*String tmpSql = "select * from Veh_passrec where kdbh='" + kdbh + "'";
		 if (StringUtils.isNotBlank(fxbh)) {
			 tmpSql += "  and fxbh = '" + fxbh + "' ";
			}

			if (StringUtils.isNotBlank(cd)) {
				//if(cd.startsWith("0")){
				//cd= cd.substring(cd.length()-1,cd.length());}
				tmpSql += " and cdbh = '" + cd + "'";
			}*/
		List list = this.queryForList(sb.toString(), VehRealpass.class);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return (VehRealpass) list.get(0);
	}
	
	public long getVehpassByKdbh(String kdbh, String fxbh, String cdbh) {
		String sql = "select nvl(sum(gcs),0)  from stat_flow_sunshine where kdbh  in ('"
				+ kdbh + "')";

		if (StringUtils.isNotBlank(fxbh)) {
			sql += "  and fxbh = '" + fxbh + "' ";
		}

		if (StringUtils.isNotBlank(cdbh)) {
			sql += " and cdbh = '" + cdbh + "'";
		}

		sql += "  and trunc(TJRQ,'DD') = trunc(sysdate,'DD')   group by kdbh ";

		return this.jdbcTemplate.execute(sql,
				new PreparedStatementCallback<Long>() {

					public Long doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ResultSet rs = ps.executeQuery();
						if (rs.next()) {
							return rs.getLong(1);
						} else
							return 0l;
					}
				});
	}

	public VehRealpass getJianKongInfo(String kdbh, String fxbh) {
		String sql = "select kdbh,cdbh,tp1,gcsj,(sysdate-gcsj)*24*60 comparedate from veh_realpass where kdbh=?  and  fxbh = ?";
		List list = this.queryForList(sql, new Object[]{kdbh,fxbh},VehRealpass.class);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		return (VehRealpass) list.get(0);
	}

	public Map<String, Object> findPageRealForMap(Map<String, Object> map,
			Class<?> clazz) throws Exception {
		int curPage = 1;
		int pageSize = 10;
		StringBuffer sb = new StringBuffer();
		Map<String, Object> result = null;
		if (map != null) {
			Set<String> set = map.keySet();
			for (Object o : set) {
				String val = o.toString();
				if (val.equalsIgnoreCase("kssj")) {
					sb.append(" and v.gcsj > to_date('" + map.get(val)
							+ "','yyyy-mm-dd hh24:mi:ss')");
				} else if (val.equalsIgnoreCase("jssj")) {
					sb.append(" and v.gcsj <= to_date('" + map.get(val)
							+ "','yyyy-mm-dd hh24:mi:ss')");
				} else if (val.equalsIgnoreCase("hphm")) {
					sb.append(" and v.hphm like '%" + map.get(val) + "%' ");
				} else if (!(val.equalsIgnoreCase("page") || val
						.equalsIgnoreCase("rows"))) {
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
		StringBuffer sql = new StringBuffer(" select * from  veh_realpass v where 1=1 ");
		sql.append(sb);
		sql.append(" order by gcsj desc");

		result = this.findPageForMap(sql.toString(), curPage, pageSize, clazz);
		return result;
	}

	public long getDiffTime(String time) throws Exception {
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT (SYSDATE - to_date('"+time+"','yyyy-mm-dd hh24:mi:ss')) * 3600 *24 as diff FROM dual");
		Long l = this.jdbcTemplate.queryForLong(sb.toString());
		/*long l = 0l;
		if(list!=null){
			Map map = (Map)list.get(0);
			l = Long.parseLong(map.get("diff").toString());
		}*/
		return l;
	}

	

}
