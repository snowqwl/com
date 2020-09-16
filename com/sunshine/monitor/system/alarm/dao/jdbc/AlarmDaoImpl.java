package com.sunshine.monitor.system.alarm.dao.jdbc;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.easymap.util.ElementUtils;
import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.bean.SqlConditionFactory.SqlCondition;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.dao.page.PagingParameter;
import com.sunshine.monitor.comm.util.InformationSource;
import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.AlarmDao;
import com.sunshine.monitor.system.alarm.util.OptimizeStrategy;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

@Repository
public class AlarmDaoImpl extends BaseDaoImpl implements AlarmDao {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public AlarmDaoImpl() {
		super.setTableName("VEH_ALARMREC");
	}

	public Map<String, Object> getAlarmList(Map<String, Object> conditions, String tableName, String bkr) {
		String table = "";
		String temp = "";
		String glbm = String.valueOf(conditions.get("glbm"));
		List vl = new ArrayList<>();
		List<Map<String, Object>> queryForList = new ArrayList<>();
		int totalRows = -1;
		conditions.remove("glbm");
		if (tableName == null || "".equals(tableName)) {
			table = this.getTableName();
		} else {
			table = tableName;
		}
		OptimizeStrategy opt = new OptimizeStrategy("a");
		StringBuffer sql = new StringBuffer(50);
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key) || "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key) || "rows".equalsIgnoreCase(key) || "city".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					String[] temps = key.split("_");
					if (temps.length != 2) {
						continue;
					}
					opt.addIndexList(opt.index4);
					sql.append(" and a." + temps[1] + " >= ").append("to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					vl.add(value);
				} else if (key.indexOf("jssj") != -1) {
					String[] temps = key.split("_");
					if (temps.length != 2) {
						continue;
					}
					sql.append(" and a." + temps[1] + " <= ").append("to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					vl.add(value);
				} else if (key.indexOf("kdbh") != -1) {
					opt.addIndexList(opt.index8);
					sql.append(" and a.");
					sql.append(key);
					// sql.append(" in
					// (substr('").append(value).append("',0,18),'").append(value).append("')");
					sql.append(" in (?) ");
					vl.add(value);
					// 用于综合查询
				} else if (key.indexOf("bjdwdm") != -1) {
					opt.addIndexList(opt.index3);
					sql.append(" and ").append("instr(a.").append(key).append(",?,1,1)>0");
					vl.add(value);
				} else if (key.indexOf("hphm") != -1) {
					opt.addIndexList(opt.index7);
					sql.append(" and a.");
					sql.append(key);
					// bug19:原代码用=匹配，控件默认有湘字，直接点查询查不到
					sql.append(" like ?");
					vl.add(value + "%");
				} else if (key.indexOf("sflj") != -1) {
					opt.addIndexList(opt.index13);
					sql.append(" and a.");
					sql.append(key);
					sql.append(" = ?");
					vl.add(value);
				} else if (key.indexOf("sffk") != -1) {
					opt.addIndexList(opt.index12);
					sql.append(" and a.");
					sql.append(key);
					sql.append(" = ?");
					vl.add(value);
				} else if (key.indexOf("sfxdzl") != -1) {
					opt.addIndexList(opt.index14);
					sql.append(" and a.");
					sql.append(key);
					sql.append(" = ?");
					vl.add(value);
				} else if (key.indexOf("qrzt") != -1) {
					opt.addIndexList(opt.index11);
					sql.append(" and a.");
					sql.append(key);
					sql.append(" = ?");
					vl.add(value);
				} else if (key.indexOf("bjdl") != -1) {
					opt.addIndexList(opt.index2);
					sql.append(" and a.");
					sql.append(key);
					sql.append(" = ?");
					vl.add(value);
				} else if (key.indexOf("citylike") != -1) {
					opt.addIndexList(opt.index8);
					sql.append(" and a.kdbh like  ? ");
					vl.add("%" + value + "%");
				} else {
					sql.append(" and a.");
					sql.append(key);
					sql.append(" = ?");
					vl.add(value);
				}
			}
		}
		sql.append(temp);
		vl.add(glbm);
		sql.append(" and b.bkjg in (select xjjg from frm_prefecture where dwdm = ?");
		sql.append(" order by ");
		sql.append("a.").append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));
		StringBuffer sqlResult = new StringBuffer("select " + opt.algorithmMix() + "a.BJXH,a.BJDL,a.BJLX,a.BKXH,"
				+ "a.BJSJ,a.BJDWDM,a.BJDWMC,a.BJDWLXDH,a.HPHM,a.HPZL,a.SFXDZL,a.GCXH,a.GCSJ,a.SBBH,"
				+ "a.SBMC,a.KDBH,a.KDMC,a.FXBH,a.FXMC,a.QRZT,ROUND(TO_NUMBER(sysdate - a.BJSJ) * 24 * 60) as cssj,"
				+ "(sysdate - a.BJSJ)*24*60*60*1000 as cssjms from " + table
				+ " a,veh_suspinfo b  where 1=1 and a.bkxh = b.bkxh ");
		sqlResult.append(sql);
		String countsql = "SELECT COUNT(1) from (" + sqlResult + ")s";
		sqlResult.insert(0, "select rownum rn, t.* from (");
		sqlResult.append(")t");

		totalRows = this.jdbcTemplate.queryForInt(countsql.toString(), vl.toArray());
		if (totalRows > 0) {
			String orgSql = "select * from (" + sqlResult + " where rownum <= ?) where rn >= ?";
			PagingParameter pagingParameter = new PagingParameter(
					Integer.parseInt(String.valueOf(conditions.get("page"))),
					Integer.parseInt(String.valueOf(conditions.get("rows"))), totalRows);
			Map<String, Integer> map = pagingParameter.getStartAndEndRow();
			int start = map.get("start");
			int end = map.get("end");
			vl.add(end);
			vl.add(start);

			queryForList = this.jdbcTemplate.queryForList(orgSql, vl.toArray());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("rows", queryForList);
		map.put("total", totalRows);
		return map;
	}

	public Map<String, Object> getConfirmOutList(Map<String, Object> conditions) throws Exception {
		String temp = "";
		StringBuffer sql = new StringBuffer(50);
		List vl = new ArrayList<>();
		sql.append("select /*+RULE*/ a.*  from " + this.getTableName()
				+ " a  where qrzt > '0'  and TO_NUMBER(qrsj - bjsj)*24*60*60 > 2*60 ");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key) || "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key) || "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					String[] temps = key.split("_");
					if (temps.length != 2) {
						continue;
					}
					sql.append(" and " + temps[1] + " >= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					vl.add(value);
				} else if (key.indexOf("jssj") != -1) {
					String[] temps = key.split("_");
					if (temps.length != 2) {
						continue;
					}
					vl.add(value);
					sql.append(" and " + temps[1] + " <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				} else if (key.indexOf("kdbh") != -1) {
					sql.append(" and ");
					sql.append(key);
					// sql.append(" in
					// (substr('").append(value).append("',0,18),'").append(value).append("')");
					sql.append(" in (?) ");
					vl.add(value);
					// 用于综合查询
				} else if (key.indexOf("bjdwdm") != -1) {
					sql.append(" and ").append("instr(").append(key).append(",?,1,1)>0");
					vl.add(value);
				} else {
					sql.append(" and ");
					sql.append(key);
					sql.append(" = ?");
					vl.add(value);
				}
			}
		}
		sql.append(temp);
		sql.append(" order by ");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));

		String countsql = "SELECT COUNT(1) from (" + sql + ")s";
		sql.insert(0, "select rownum rn, t.* from (");
		sql.append(")t");
		int totalRows = -1;
		List<Map<String, Object>> queryForList = new ArrayList<>();
		totalRows = this.jdbcTemplate.queryForInt(countsql.toString(), vl.toArray());
		if (totalRows > 0) {
			String orgSql = "select * from (" + sql + " where rownum <= ?) where rn >= ?";
			PagingParameter pagingParameter = new PagingParameter(
					Integer.parseInt(String.valueOf(conditions.get("page"))),
					Integer.parseInt(String.valueOf(conditions.get("rows"))), totalRows);
			Map<String, Integer> map = pagingParameter.getStartAndEndRow();
			int start = map.get("start");
			int end = map.get("end");
			vl.add(end);
			vl.add(start);
			queryForList = this.jdbcTemplate.queryForList(orgSql, vl.toArray());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("rows", queryForList);
		map.put("total", totalRows);
		return map;
	}

	public Map<String, Object> getAlarmListInfo(Map<String, Object> conditions, String tableName, String bkr) {
		String table = "";
		String temp = "";
		if (tableName == null || "".equals(tableName)) {
			table = this.getTableName();
		} else {
			table = tableName;
			temp = " and ROUND(TO_NUMBER(sysdate - BJSJ) * 24 * 60)<10";
		}
		List vl = new ArrayList<>();
		StringBuffer sql = new StringBuffer(50);
		if (conditions.get("qrzt") != null && conditions.get("qrzt").equals("9")) {
			sql.append(
					"select /*+RULE*/ a.* ,ROUND(TO_NUMBER(sysdate - BJSJ) * 24 * 60) as cssj,(sysdate - BJSJ)*24*60*60*1000 as cssjms from "
							+ table + " a  where 1=1 ");
			sql.append(" and ");
			sql.append(" bjdl = ");
			sql.append("'3'");
			sql.append(" and ");
			sql.append("bkxh");
			vl.add(bkr);
			sql.append(" in (select bkxh from veh_suspinfo where bkr= ? ) ");
			// sql.append(" in (select bkxh from veh_suspinfo where bkr=
			// '033634' ) ");
			Set<Entry<String, Object>> set = conditions.entrySet();
			Iterator<Entry<String, Object>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
				String key = entry.getKey();
				String value = (String) entry.getValue();
				boolean isFilter = "sort".equalsIgnoreCase(key) || "order".equalsIgnoreCase(key)
						|| "page".equalsIgnoreCase(key) || "rows".equalsIgnoreCase(key);
				if (!isFilter) {
					if (key.indexOf("kssj") != -1) {
						String[] temps = key.split("_");
						if (temps.length != 2) {
							continue;
						}
						sql.append(" and " + temps[1] + " >= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
						vl.add(value);
					} else if (key.indexOf("jssj") != -1) {
						String[] temps = key.split("_");
						if (temps.length != 2) {
							continue;
						}
						sql.append(" and " + temps[1] + " <= ").append("to_date(?,'yyyy-mm-dd hh24:mi:ss')");
						vl.add(value);
					} else if (key.indexOf("kdbh") != -1) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" in (substr(?,0,18),?)");
						vl.add(value);
						vl.add(value);

					}

					// 用于综合查询
					else if (key.indexOf("bjdwdm") != -1) {
						sql.append(" and ").append("instr(").append(key).append(",?,1,1)>0");
						vl.add(value);
					}
				}
			}
			sql.append(temp);
			sql.append(" order by ");
			sql.append(conditions.get("sort"));
			sql.append(" ");
			sql.append(conditions.get("order"));

		} else {
			sql.append(
					"select /*+RULE*/ a.* ,ROUND(TO_NUMBER(sysdate - BJSJ) * 24 * 60) as cssj,(sysdate - BJSJ)*24*60*60*1000 as cssjms from "
							+ table + " a  where 1=1 ");
			Set<Entry<String, Object>> set = conditions.entrySet();
			Iterator<Entry<String, Object>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
				String key = entry.getKey();
				String value = (String) entry.getValue();
				boolean isFilter = "sort".equalsIgnoreCase(key) || "order".equalsIgnoreCase(key)
						|| "_currpage".equalsIgnoreCase(key) || "_pagelines".equalsIgnoreCase(key);
				if (!isFilter) {
					if (key.indexOf("kssj") != -1) {
						String[] temps = key.split("_");
						if (temps.length != 2) {
							continue;
						}
						sql.append(" and " + temps[1] + " >= ").append("to_date(?,'yyyy-mm-dd hh24:mi:ss')");
						vl.add(value);
					} else if (key.indexOf("jssj") != -1) {
						String[] temps = key.split("_");
						if (temps.length != 2) {
							continue;
						}
						sql.append(" and " + temps[1] + " <= ").append("to_date(?,'yyyy-mm-dd hh24:mi:ss')");
						vl.add(value);
					} else if (key.indexOf("kdbh") != -1) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" in (substr(?,0,18),?)");
						vl.add(value);
						vl.add(value);
						// 用于综合查询
					} else if (key.indexOf("bjdwdm") != -1) {
						sql.append(" and ").append("instr(").append(key).append(",?,1,1)>0");
						vl.add(value);
					} else {
						sql.append(" and ");
						sql.append(key);
						sql.append(" = ?");
						vl.add(value);
					}
				}
			}
			// sql.append(temp);
			// sql.append(" order by ");
			// sql.append(conditions.get("sort"));
			// sql.append(" ");
			// sql.append(conditions.get("order"));
		}
		String countsql = "SELECT COUNT(1) from (" + sql + ")s";
		int totalRows = -1;
		Map<String, Object> map = new HashMap<>();
		totalRows = this.jdbcTemplate.queryForInt(countsql.toString(), vl.toArray());
		List<Map<String, Object>> queryForList = new ArrayList<>();
		if (totalRows > 0) {
			String orgSql = "select * from (" + sql + " where rownum <= ?) where rn >= ?";
			PagingParameter pagingParameter = new PagingParameter(
					Integer.parseInt(String.valueOf(conditions.get("page"))),
					Integer.parseInt(String.valueOf(conditions.get("rows"))), totalRows);
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

	public Map<String, Object> getAlarmListByHphm(Map<String, Object> conditions) throws Exception {
		List vl = new ArrayList<>();
		StringBuffer sql = new StringBuffer(" select * from VEH_ALARMREC where ");
		sql.append(" hphm like upper(?) order by bjsj desc");
		vl.add("%" + conditions.get("hphm") + "%");

		Map<String, Object> map = queryforMapnew(conditions.get("page").toString(), conditions.get("rows").toString(),
				sql.toString(), vl);
		// this.getSelf().findPageForMap(sql.toString(),
		// Integer.parseInt(conditions.get("page").toString()),
		// Integer.parseInt(conditions.get("rows").toString()));
		return map;
	}

	public VehAlarmrec getVehAlarmrec(String bjxh) throws Exception {
		String sql = "SELECT /*+INDEX(PK_VEH_ALARMREC_BJXH)*/* FROM VEH_ALARMREC WHERE BJXH = ?";
		List<VehAlarmrec> list = this.queryForList(sql, new Object[] { bjxh }, VehAlarmrec.class);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public VehAlarmrec getCityVehAlarmrec(String bjxh, String cityname) throws Exception {
		String sql = "SELECT /*+INDEX(PK_VEH_ALARMREC_BJXH)*/* FROM VEH_ALARMREC";
		if (cityname != null && !"".equals(cityname)) {
			sql += "@" + cityname;
		}
		/*
		 * sql += " WHERE BJXH = '" + bjxh + "'"; List<VehAlarmrec> list =
		 * this.queryForList(sql, VehAlarmrec.class);
		 */
		sql += " WHERE BJXH = ?";
		List<VehAlarmrec> list = this.queryForList(sql, new Object[] { bjxh }, VehAlarmrec.class);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public TransAlarm getTransAlarmDetail(String ywxh) throws Exception {
		// String sql = "SELECT * FROM VEH_ALARMREC WHERE BJXH = '" + ywxh +
		// "'";
		// TransAlarm bean = this.queryForObject(sql, TransAlarm.class);
		Field[] field = TransAlarm.class.getDeclaredFields();
		StringBuffer sqlbuf = new StringBuffer("select ");

		boolean isFirst = true;
		for (int i = 1; i < field.length; i++) {
			if (field[i].getModifiers() == 2) {
				if (isFirst) {
					sqlbuf.append(field[i].getName());
					isFirst = false;
				} else {
					sqlbuf.append(",").append(field[i].getName());
				}
			}
		}
		List vl = new ArrayList<>();
		sqlbuf.append(" from ").append("VEH_ALARMREC");
		sqlbuf.append(" where ").append("BJXH=?");
		vl.add(ywxh);
		TransAlarm bean = (TransAlarm) this.queryForList(sqlbuf.toString(), vl.toArray(), TransAlarm.class);

		return bean;
	}

	public int saveAlarmSign(VehAlarmrec vehAlarmrec) throws Exception {
		List vl = new ArrayList<>();
		vl.add(vehAlarmrec.getQrdwdm());
		vl.add(vehAlarmrec.getQrdwdmmc());
		vl.add(vehAlarmrec.getQrr());
		vl.add(vehAlarmrec.getQrrjh());
		vl.add(vehAlarmrec.getQrrmc());
		vl.add(vehAlarmrec.getQrdwlxdh());
		vl.add(vehAlarmrec.getQrzt());
		vl.add(vehAlarmrec.getQrjg());
		vl.add(vehAlarmrec.getJyljtj());
		vl.add(vehAlarmrec.getSffk() == null ? "0" : vehAlarmrec.getSffk());
		vl.add(vehAlarmrec.getSfxdzl() == null ? "0" : vehAlarmrec.getSfxdzl());
		vl.add(vehAlarmrec.getSflj() == null ? "0" : vehAlarmrec.getSflj());
		vl.add(vehAlarmrec.getBjxh());
		String u_sql = "UPDATE veh_alarmrec set qrdwdm = ?, qrdwdmmc = ?, qrr = ?, qrrjh =?,"
				+ " qrrmc = ?, qrdwlxdh = ?, qrzt = ?, qrsj = sysdate, qrjg = ?, jyljtj =?,"
				+ " gxsj = sysdate, sffk=?, sfxdzl= ?, sflj= ? where bjxh = ?";
		return this.jdbcTemplate.update(u_sql, vl.toArray());
	}

	public int saveAlarmLink(TransAlarm bean) throws Exception {
		String alarm_sql = "SELECT count(1) FROM VEH_ALARMREC WHERE BJXH = ?";
		int count = this.jdbcTemplate.queryForInt(alarm_sql, new String[] { bean.getBjxh() });
		String i_sql = "";
		List vl = new ArrayList<>();
		if (count == 0) {
			vl.add(bean.getBjxh());
			vl.add(bean.getBjdl() == null ? "1" : bean.getBjdl());
			vl.add(bean.getBjlx() == null ? " " : bean.getBjlx());
			vl.add(bean.getBkxh() == null ? " " : bean.getBkxh());
			vl.add(bean.getBjsj() == null ? " " : bean.getBjsj());
			vl.add(bean.getBjdwdm() == null ? " " : bean.getBjdwdm());
			vl.add(bean.getBjdwmc() == null ? " " : bean.getBjdwmc());
			vl.add(bean.getBjdwlxdh() == null ? " " : bean.getBjdwlxdh());
			vl.add(bean.getHphm() == null ? " " : bean.getHphm());
			vl.add(bean.getHpzl() == null ? " " : bean.getHpzl());
			vl.add(bean.getGcxh() == null ? " " : bean.getGcxh());
			vl.add(bean.getGcsj() == null ? " " : bean.getGcsj());
			vl.add(bean.getSbbh() == null ? " " : bean.getSbbh());
			vl.add(bean.getSbmc() == null ? " " : bean.getSbmc());
			vl.add(bean.getKdbh() == null ? " " : bean.getSbmc());
			vl.add(bean.getKdmc() == null ? " " : bean.getKdmc());
			vl.add(bean.getFxbh() == null ? " " : bean.getFxbh());
			vl.add(bean.getFxmc() == null ? " " : bean.getFxmc());
			vl.add(bean.getCllx() == null ? " " : bean.getCllx());
			vl.add(bean.getClsd() == null ? " " : bean.getClsd());
			vl.add(bean.getHpys() == null ? " " : bean.getHpys());
			vl.add(bean.getCwhphm() == null ? " " : bean.getCwhphm());
			vl.add(bean.getCwhpys() == null ? " " : bean.getCwhpys());
			vl.add(bean.getHpyz() == null ? " " : bean.getHpyz());
			vl.add(bean.getCdbh() == null ? " " : bean.getCdbh());
			vl.add(bean.getClwx() == null ? " " : bean.getClwx());
			vl.add(bean.getCsys() == null ? " " : bean.getCsys());
			vl.add(bean.getTp1() == null ? " " : bean.getTp1());
			vl.add(bean.getTp2() == null ? " " : bean.getTp2());
			vl.add(bean.getTp3() == null ? " " : bean.getTp3());
			vl.add(bean.getQrr() == null ? " " : bean.getQrr());
			vl.add(bean.getQrrjh() == null ? " " : bean.getQrrjh());
			vl.add(bean.getQrdwdm() == null ? " " : bean.getQrdwdm());
			vl.add(bean.getQrdwdmmc() == null ? " " : bean.getQrdwdmmc());
			vl.add(bean.getQrdwlxdh() == null ? " " : bean.getQrdwlxdh());
			vl.add(bean.getQrzt() == null ? " " : bean.getQrzt());
			vl.add(bean.getQrjg() == null ? " " : bean.getQrjg());
			vl.add(bean.getGxsj() == null ? " " : bean.getGxsj());
			vl.add(bean.getJyljtj() == null ? " " : bean.getJyljtj());
			vl.add(bean.getXxly() == null ? " " : bean.getXxly());
			vl.add(bean.getBy1() == null ? " " : bean.getBy1());
			vl.add(bean.getBy2() == null ? " " : bean.getBy2());
			vl.add(bean.getBy3() == null ? " " : bean.getBy3());
			vl.add(bean.getBy4() == null ? " " : bean.getBy4());
			vl.add(bean.getBy5() == null ? " " : bean.getBy5());
			vl.add(bean.getSfxdzl() == null ? " " : bean.getSfxdzl());
			vl.add(bean.getSffk() == null ? " " : bean.getSffk());
			vl.add(bean.getSflj() == null ? " " : bean.getSflj());
			vl.add(bean.getQrrmc() == null ? " " : bean.getQrrmc());

			i_sql = " INSERT INTO VEH_ALARMREC (BJXH,BJDL,BJLX,BKXH,BJSJ,BJDWDM,BJDWMC,BJDWLXDH,HPHM,HPZL,GCXH,GCSJ,"
					+ "SBBH,SBMC,KDBH,KDMC,FXBH,FXMC,CLLX,CLSD,HPYS,CWHPHM,CWHPYS,HPYZ,CDBH,CLWX,CSYS,"
					+ "TP1,TP2,TP3,QRR,QRRJH,QRDWDM,QRDWDMMC,QRDWLXDH,QRSJ,QRZT,QRJG,GXSJ,JYLJTJ,XXLY,"
					+ "BY1,BY2,BY3,BY4,BY5,SFXDZL,SFFK,SFLJ,QRRMC) " + " Values (?,?,?,?,"
					+ " to_date(?,'YYYY-MM-DD HH24:MI:SS'), ?,?,?,?,?,?," + "  to_date(?"
					+ ",'YYYY-MM-DD HH24:MI:SS'), ?,?,?,?,?,?,?" + "',TRIM(?) ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
					+ "sysdate,?,?," + " to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?,?,?,?,?,?" + "')";

		} else {
			// 预警签收确认
			vl.add(bean.getQrr());
			vl.add(bean.getQrrmc());
			vl.add(bean.getQrrjh());
			vl.add(bean.getQrdwdm());
			vl.add(bean.getQrdwdmmc());
			vl.add(bean.getQrdwlxdh());
			vl.add(bean.getQrsj());
			vl.add(bean.getQrzt());
			vl.add(bean.getQrjg());
			vl.add(bean.getJyljtj());
			vl.add(bean.getSfxdzl());
			vl.add(bean.getSffk());
			vl.add(bean.getSflj());
			vl.add(bean.getBjxh());
			i_sql = " UPDATE VEH_ALARMREC SET  QRR=?,QRRMC=?,QRRJH=?,"
					+ "QRDWDM=?,QRDWDMMC=?,QRDWLXDH=?,QRSJ=to_date(?,'yyyy-mm-dd hh24:mi:ss'),"
					+ "QRZT=?,QRJG=?,GXSJ=SYSDATE,JYLJTJ=?,SFXDZL=?,SFFK=?,SFLJ=? WHERE BJXH=?";
		}
		log.info("添加报警信息或预警签收确认sql-----" + i_sql);
		return this.jdbcTemplate.update(i_sql, vl.toArray());
	}

	public boolean saveTransAlarm_test(VehAlarmrec vehAlarmrec, VehSuspinfo vehSuspinfo, String xzqh) throws Exception {
		String xxly = vehSuspinfo.getXxly();
		String i_sql = "";
		// 联动
		List vl = new ArrayList<>();
		if (InformationSource.LINKAGE_WRITER.getCode().equals(xxly)) {
			if (vehSuspinfo.getBkpt() != xzqh) {
				vl.add(xzqh);
				vl.add(xzqh);
				vl.add(vehSuspinfo.getBkpt());
				vl.add(vehAlarmrec.getBjxh());
				i_sql = "INSERT into trans_alarm(CSXH, CSDW, JSDW, CSBJ, CSSJ, YWXH, TYPE) values(? || SEQ_TRANS_CSXH.NEXTVAL, ?, ?, 0, sysdate, ?,'22','0')";
			}

		}

		if (!"".equals(i_sql)) {
			this.jdbcTemplate.update(i_sql);
		}
		return true;
	}

	public boolean saveTransAlarm(VehAlarmrec vehAlarmrec, VehSuspinfo vehSuspinfo, String xzqh) throws Exception {
		String xxly = vehSuspinfo.getXxly();
		String i_sql = "";
		List vl = new ArrayList<>();
		// 情报平台写入
		if (InformationSource.INTELLIGENCE_WRITER.getCode().equals(xxly)) {
			if (xzqh.equals(vehSuspinfo.getBkpt())) {
				// 地市应该不会执行下面这段
				vl.add(xzqh);
				vl.add(vehAlarmrec.getBjxh());

				i_sql = "INSERT into trans_intelligence(CSXH, CSBJ, CSSJ, YWXH, TYPE)values(?|| SEQ_TRANS_INTELL_XH.NEXTVAL,0,sysdate, ?,'22')";
			} else {
				vl.add(xzqh);
				vl.add(xzqh);
				vl.add(vehSuspinfo.getBkfw());
				vl.add(vehAlarmrec.getBjxh());
				i_sql = "INSERT into JM_trans_alarm(CSXH, CSDW, JSDW, CSBJ, CSSJ, YWXH, TYPE, DDBJ) values(? || SEQ_TRANS_CSXH.NEXTVAL, ?, ?, 0, sysdate, ?,'22','0')";
			}
			// 联动写入
		} else if (InformationSource.LINKAGE_WRITER.getCode().equals(xxly)) {
			if (!xzqh.equals(vehSuspinfo.getBkpt())) {
				vl.add(xzqh);
				vl.add(xzqh);
				vl.add(vehSuspinfo.getBkfw());
				vl.add(vehAlarmrec.getBjxh());
				i_sql = "INSERT into JM_trans_alarm(CSXH, CSDW, JSDW, CSBJ, CSSJ, YWXH, TYPE, DDBJ) values(?|| SEQ_TRANS_CSXH.NEXTVAL, ?, ?, 0, sysdate, ?,'22','0')";
			}
			// 六合一
		} else if (InformationSource.SISX_INTERGRATE.getCode().equals(xxly)) {
			if (xzqh.equals(vehSuspinfo.getBkpt())) {
				vl.add(xzqh);
				vl.add(vehAlarmrec.getBjxh());
				i_sql = "INSERT into trans_trff(CSXH, CSBJ, CSSJ, YWXH, TYPE) values (? || SEQ_TRANS_TRFF_XH.NEXTVAL, 0, sysdate, ?,'22')";
			} else {
				vl.add(xzqh);
				vl.add(xzqh);
				vl.add(vehSuspinfo.getBkfw());
				vl.add(vehAlarmrec.getBjxh());
				i_sql = "INSERT into JM_trans_alarm(CSXH, CSDW, JSDW, CSBJ, CSSJ, YWXH, TYPE) values('" + xzqh
						+ "' || SEQ_TRANS_CSXH.NEXTVAL, '" + xzqh + "', '"
						// + vehSuspinfo.getBkpt()
						+ vehSuspinfo.getBkfw() + "', 0, sysdate, '" + vehAlarmrec.getBjxh() + "','22')";
			}
		}
		if (!"".equals(i_sql)) {
			this.jdbcTemplate.update(i_sql, vl.toArray());
		}
		return true;
	}

	public boolean saveBoundaryTransAlarm(String bkfw, String bjxh, String xzqh, String type) throws Exception {
		List vl = new ArrayList<>();
		vl.add(xzqh);
		vl.add(xzqh);
		vl.add(bkfw);
		vl.add(bjxh);
		vl.add(type);
		String i_sql = "INSERT into JM_trans_alarm(CSXH, CSDW, JSDW, CSBJ, CSSJ, YWXH, TYPE) values(?|| SEQ_TRANS_CSXH.NEXTVAL, ?, ?, 0, sysdate, ?,?)";
		int flag = this.jdbcTemplate.update(i_sql, vl.toArray());
		return (flag == 1) ? true : false;
	}

	public List<VehAlarmCmd> getCmdlist(String bjxh) throws Exception {

		String sql = "select * from veh_alarm_cmd where bjxh = ? order by zlsj";
		return queryForList(sql, new Object[] { bjxh }, VehAlarmCmd.class);
	}

	public List<VehAlarmCmd> getCityCmdlist(String bjxh, String cityname) throws Exception {
		String sql = "SELECT * FROM veh_alarm_cmd";
		if (cityname != null) {
			sql += "@" + cityname;
		}
		sql += " WHERE BJXH = ?  order by ZLSJ ";
		return queryForList(sql, new String[] { bjxh }, VehAlarmCmd.class);
	}

	public List<VehAlarmCmd> getCmdScopelist(String zlxh) throws Exception {
		String sql = "select * from veh_alarm_cmdscope where zlxh = ?";
		return queryForList(sql, new Object[] { zlxh }, VehAlarmCmd.class);
	}

	public String saveCmd(VehAlarmCmd vehAlarmCmd) throws Exception {
		String v_zlxh = "";
		if (vehAlarmCmd.getZlxh() != null) {
			// 联动过来的
			v_zlxh = vehAlarmCmd.getZlxh();
		} else {
			String zldw = vehAlarmCmd.getZldw();
			// 获取序号
			String seq_sql = "SELECT " + zldw + " || SEQ_ALARM_CMD_ZLXH.nextval from dual";
			v_zlxh = this.jdbcTemplate.queryForObject(seq_sql, String.class);
		}
		List vl = new ArrayList<>();
		vl.add(v_zlxh);
		vl.add(vehAlarmCmd.getBjxh());
		vl.add(vehAlarmCmd.getZlfs());
		vl.add(vehAlarmCmd.getZlr());
		vl.add(vehAlarmCmd.getZlrjh());
		vl.add(vehAlarmCmd.getZlrmc());
		vl.add(vehAlarmCmd.getZldw());
		vl.add(vehAlarmCmd.getZldwmc());
		vl.add(vehAlarmCmd.getZldwlxdh());
		// vl.add(sysdate );
		vl.add(vehAlarmCmd.getZlnr());
		vl.add(vehAlarmCmd.getBy1());
		vl.add(vehAlarmCmd.getBy2());
		String i_sql = "INSERT into veh_alarm_cmd(ZLXH, BJXH, ZLFS,ZLR, ZLRJH,ZLRMC, ZLDW, ZLDWMC, "
				+ "zldwlxdh, ZLSJ, ZLNR, BY1, BY2) values(?,?,?,?,?,?,?,?,?, sysdate, ?,?,?)";
		int count = this.jdbcTemplate.update(i_sql, vl.toArray());
		if (count == 1) {
			// 保存成功，返回指令序号
			return v_zlxh;
		}
		return null;
	}

	public String saveCmd(TransCmd vehAlarmCmd) throws Exception {
		String v_zlxh = "";
		List vl = new ArrayList<>();
		if (vehAlarmCmd.getZlxh() != null) {
			// 联动过来的
			v_zlxh = vehAlarmCmd.getZlxh();
			// 已存在该指令则先删除
			String susp_sql = "SELECT count(*) FROM veh_alarm_cmd WHERE ZLXH = ?";
			int count = this.jdbcTemplate.queryForInt(susp_sql, new String[] { vehAlarmCmd.getZlxh() });
			if (count > 0) {
				susp_sql = "delete from veh_alarm_cmd where ZLXH=?";
				this.jdbcTemplate.update(susp_sql, new String[] { vehAlarmCmd.getZlxh() });
			}
		} else {
			String zldw = vehAlarmCmd.getZldw();
			// 获取序号
			String seq_sql = "SELECT " + zldw + " || SEQ_ALARM_CMD_ZLXH.nextval from dual";
			v_zlxh = this.jdbcTemplate.queryForObject(seq_sql, String.class);
		}
		vl.add(v_zlxh);
		vl.add(vehAlarmCmd.getBjxh());
		vl.add(vehAlarmCmd.getZlfs());
		vl.add(vehAlarmCmd.getZlr());
		vl.add(vehAlarmCmd.getZlrjh());
		vl.add(vehAlarmCmd.getZlrmc());
		vl.add(vehAlarmCmd.getZldw());
		vl.add(vehAlarmCmd.getZldwmc());
		vl.add(vehAlarmCmd.getZldwlxdh());
		// vl.add(sysdate );
		vl.add(vehAlarmCmd.getZlnr());
		vl.add(vehAlarmCmd.getBy1());
		vl.add(vehAlarmCmd.getBy2());
		vl.add(vehAlarmCmd.getXxly());
		String i_sql = "INSERT into veh_alarm_cmd(ZLXH, BJXH, ZLFS,ZLR, ZLRJH,ZLRMC, ZLDW, ZLDWMC, "
				+ "zldwlxdh, ZLSJ, ZLNR, BY1, BY2, XXLY) values(?,?,?,?,?,?,?,?,?, sysdate, ?,?,?,?)";
		int count = this.jdbcTemplate.update(i_sql, vl.toArray());
		if (count == 1) {
			// 保存成功，返回指令序号
			return v_zlxh;
		}
		return null;
	}

	public boolean saveCmdScope(VehAlarmCmd vehAlarmCmd) throws Exception {
		boolean success = true;
		String by1 = vehAlarmCmd.getBy1().replace(";", ",");
		StringTokenizer st = new StringTokenizer(by1, ",");
		String sql = "INSERT into veh_alarm_cmdscope(XH, ZLXH, ZLJSDW, SFFK)values(SEQ_ALARM_CMDSCOPE_XH.NEXTVAL, ?,?, '0')";
		while (st.hasMoreTokens()) {
			String jsdw = st.nextToken();
			if (jsdw != null && !"".equals(jsdw)) {
				int count = this.jdbcTemplate.update(sql, new Object[] { vehAlarmCmd.getZlxh(), jsdw },
						new int[] { Types.VARCHAR });
				if (count == 0) {
					success = false;
				}
			}
		}
		return success;
	}

	public boolean updateAlarmIsxdzl(String bjxh) throws Exception {
		boolean success = true;
		String sql = "UPDATE veh_alarmrec set sfxdzl = '1', gxsj = sysdate where bjxh = ?";
		int count = this.jdbcTemplate.update(sql, new String[] { bjxh });
		if (count == 0) {
			success = false;
		}
		return success;
	}

	public boolean saveAlarmandSigntoTemptable(VehAlarmrec vehAlarmrec) throws Exception {
		List vl = new ArrayList<>();
		vl.add(vehAlarmrec.getBjxh());
		vl.add(vehAlarmrec.getBjdl());
		vl.add(vehAlarmrec.getBjlx());
		vl.add(vehAlarmrec.getBkxh());
		vl.add(vehAlarmrec.getBjsj());
		// vl.add( to_date( vehAlarmrec.getBjsj() 'YYYY-MM-DD HH24:MI:SS') );
		vl.add(vehAlarmrec.getBjdwdm());
		vl.add(((vehAlarmrec.getBjdwmc() == null) ? " " : vehAlarmrec.getBjdwmc()));
		vl.add(((vehAlarmrec.getBjdwlxdh() == null) ? " " : vehAlarmrec.getBjdwlxdh()));
		vl.add(vehAlarmrec.getHphm());
		vl.add(((vehAlarmrec.getHpzl() == null) ? " " : vehAlarmrec.getHpzl()));
		vl.add(vehAlarmrec.getGcxh());
		vl.add(((vehAlarmrec.getGcsj() == null) ? " " : vehAlarmrec.getGcsj()));
		// vl.add( to_date( vehAlarmrec.getGcsj() 'YYYY-MM-DD HH24:MI:SS')) );
		
		vl.add(vehAlarmrec.getSbbh());
		vl.add(((vehAlarmrec.getSbmc() == null) ? " " : vehAlarmrec.getSbmc()));
		vl.add(vehAlarmrec.getKdbh());
		vl.add(((vehAlarmrec.getKdmc() == null) ? " " : vehAlarmrec.getKdmc()));
		vl.add(vehAlarmrec.getFxbh());
		vl.add(((vehAlarmrec.getFxmc() == null) ? " " : vehAlarmrec.getFxmc()));
		vl.add(((vehAlarmrec.getCllx() == null) ? " " : vehAlarmrec.getCllx()));
		vl.add(((vehAlarmrec.getClsd() == null) ? " " : vehAlarmrec.getClsd()));
		vl.add(((vehAlarmrec.getHpys() == null) ? " " : vehAlarmrec.getHpys()));
		vl.add(((vehAlarmrec.getCwhphm() == null) ? " " : vehAlarmrec.getCwhphm()));
		vl.add(((vehAlarmrec.getCwhpys() == null) ? " " : vehAlarmrec.getCwhpys()));
		vl.add(((vehAlarmrec.getHpyz() == null) ? " " : vehAlarmrec.getHpyz()));
		vl.add(((vehAlarmrec.getCdbh() == null) ? " " : vehAlarmrec.getCdbh()));
		vl.add(((vehAlarmrec.getClwx() == null) ? " " : vehAlarmrec.getClwx()));
		vl.add(((vehAlarmrec.getCsys() == null) ? " " : vehAlarmrec.getCsys()));
		vl.add(((vehAlarmrec.getTp1() == null) ? " " : vehAlarmrec.getTp1()));
		vl.add(((vehAlarmrec.getTp2() == null) ? " " : vehAlarmrec.getTp2()));
		vl.add(((vehAlarmrec.getTp3() == null) ? " " : vehAlarmrec.getTp3()));
		vl.add(vehAlarmrec.getQrr());
		vl.add(vehAlarmrec.getQrrjh());
		vl.add(vehAlarmrec.getQrrmc());
		vl.add(vehAlarmrec.getQrdwdm());
		vl.add(vehAlarmrec.getQrdwdmmc());
		vl.add(((vehAlarmrec.getQrdwlxdh() == null) ? " " : vehAlarmrec.getQrdwlxdh()));
		vl.add(vehAlarmrec.getQrsj());
		// vl.add( to_date(vehAlarmrec.getQrsj() 'YYYY-MM-DD HH24:MI:SS') );
		vl.add(vehAlarmrec.getQrzt());
		vl.add(vehAlarmrec.getQrjg());
		vl.add(vehAlarmrec.getGxsj());
		// vl.add( to_date( vehAlarmrec.getGxsj() 'YYYY-MM-DD HH24:MI:SS') );
		vl.add(vehAlarmrec.getJyljtj());
		vl.add(vehAlarmrec.getXxly());
		vl.add(vehAlarmrec.getSfxdzl());
		vl.add(vehAlarmrec.getSffk());
		vl.add(vehAlarmrec.getSflj());
		vl.add(((vehAlarmrec.getBy1() == null) ? " " : vehAlarmrec.getBy1()));
		vl.add(((vehAlarmrec.getBy2() == null) ? " " : vehAlarmrec.getBy2()));
		vl.add(((vehAlarmrec.getBy3() == null) ? " " : vehAlarmrec.getBy3()));
		vl.add(((vehAlarmrec.getBy4() == null) ? " " : vehAlarmrec.getBy4()));
		vl.add(((vehAlarmrec.getBy5() == null) ? " " : vehAlarmrec.getBy5()));

		String sql = "INSERT INTO JM_VEH_ALARMREC(bjxh, bjdl, bjlx, bkxh, bjsj, bjdwdm, bjdwmc, bjdwlxdh, hphm, hpzl, gcxh, gcsj, sbbh, sbmc, kdbh, kdmc, fxbh, fxmc, cllx, clsd, hpys, cwhphm, cwhpys, hpyz, cdbh, clwx, csys, tp1, tp2, tp3, qrr, qrrjh, qrrmc, qrdwdm, qrdwdmmc, qrdwlxdh, qrsj, qrzt, qrjg, gxsj, jyljtj, xxly, sfxdzl, sffk, sflj, by1, by2, by3, by4, by5)"
				+ "values(?,?,?,?,to_date( ? 'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?,to_date( ? 'YYYY-MM-DD HH24:MI:SS'),"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date( ? 'YYYY-MM-DD HH24:MI:SS'),"
				+ "?,?,to_date( ? 'YYYY-MM-DD HH24:MI:SS'),"
				+ "?,?,?,?,?,?,?,?,?,?)";
		int result = this.jdbcTemplate.update(sql,vl.toArray());
		if (result == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 签收确认时间为第一次签收时间，签收确应时间不做更新
	 * 
	 */
	public Map<String, Object> updateAlarmSign(VehAlarmrec vehAlarmrec) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 是否有对应的布控记录
		 
		String susp_sql = "SELECT count(1) v_count from veh_suspinfo where bkxh = ?";
		int count = this.jdbcTemplate.queryForObject(susp_sql, new Object[] { vehAlarmrec.getBkxh() }, Integer.class);

		if (count == 0) {
			result.put("code", "0");
			result.put("msg", "未找到对应的布控信息");
			return result;
		}
List vl=new ArrayList<>();
vl.add(vehAlarmrec.getQrdwdm()        );
vl.add(vehAlarmrec.getQrdwdmmc()      );
vl.add(vehAlarmrec.getQrr()           );
vl.add(vehAlarmrec.getQrrjh()         );
vl.add(vehAlarmrec.getQrrmc()         );
vl.add(vehAlarmrec.getQrdwlxdh()      );
vl.add(vehAlarmrec.getQrzt()          );
vl.add(vehAlarmrec.getQrjg()          );
vl.add(vehAlarmrec.getJyljtj()        );
//vl.add(sysdate                        );
vl.add(vehAlarmrec.getBjxh()          );
		String u_sql = "UPDATE veh_alarmrec set qrdwdm = ?, qrdwdmmc = ?, qrr = ?, qrrjh = ?, qrrmc = ?, qrdwlxdh = ?, qrzt = ?, qrjg = ?, jyljtj = ?, gxsj = sysdate where bjxh =?";
		int flag = this.jdbcTemplate.update(u_sql,vl.toArray());
		if (flag > 0) {
			result.put("code", "1");
			result.put("msg", "预警签收修改成功");
		}
		return result;
	}

	public List<VehAlarmrec> queryTimeoutTempAlarmsign(int timeOut) {
		String sql = "SELECT BJXH,BJSJ,HPHM,KDBH FROM JM_VEH_ALARMREC WHERE ceil(((sysdate - BJSJ)* 24 * 60)) >= ?";
		List<VehAlarmrec> list = this.queryForList(sql,new Object[]{timeOut}, VehAlarmrec.class);
		return list;
	}

	public int deleteTimeoutTempAlarmsign(String bjxh) {
		String sql = "DELETE FROM JM_VEH_ALARMREC WHERE BJXH = ?";
		int result = this.jdbcTemplate.update(sql,new Object[]{bjxh});
		return result;
	}

	public Map<String, Object> queryAlarmhandleConfirmlist(Map<String, Object> conditions) throws Exception {
		StringBuffer sql = new StringBuffer(50);
		List vl=new ArrayList<>();
		sql.append(
				"select distinct t.fkbh, t.bkxh, t.bjxh,t.zlxh,a.hphm, a.bjdl,a.bjlx,a.hpzl, a.kdbh, a.kdmc, a.fxmc, t.bjsj, t.sflj, t.lrsj, t.by1 from veh_alarm_handled t, veh_alarmrec a where t.bjxh = a.bjxh and t.sflj = '1'");
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key) || "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key) || "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					String[] temps = key.split("_");
					if (temps.length != 2) {
						continue;
					}
					sql.append(" and t." + temps[1] + " >= ")
							.append("to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					vl.add(value);
				} else if (key.indexOf("jssj") != -1) {
					String[] temps = key.split("_");
					if (temps.length != 2) {
						continue;
					}
					sql.append(" and t." + temps[1] + " <= ")
							.append("to_date(?,'yyyy-mm-dd hh24:mi:ss')");
					vl.add(value);
				} else if ("BJDWDM".equals(key)) {
					sql.append(" and t.BJDWDM =?");
					vl.add(value);
				} else if ("by1".equals(key)) {
					if ("1".equals(value)) {
						sql.append(" and t.by1 is not null");
					} else if ("0".equals(value)) {
						sql.append(" and t.by1 is null");
					}
				} else if (key.indexOf("kdbh") != -1) {
					sql.append(" and a.").append(key)
							// .append(" in
							// (substr('").append(value).append("',0,18),'").append(value).append("')");
							.append(" in (?)");
					vl.add(value);
				} else if (key.indexOf("hphm") != -1) {
					sql.append(" and a.").append(key).append(" like ?");
					vl.add("%"+value+"%");
				} else {
					sql.append(" and a.").append(key).append(" = ?");
					vl.add(value);
				}
			}
		}
		sql.append(" order by t.");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));
		// System.out.println(sql.toString());
		Map<String, Object> map =queryforMapnew(conditions.get("page").toString(), conditions.get("rows").toString(), sql.toString(), vl);
//				this.getSelf().findPageForMap(sql.toString(),
//				Integer.parseInt(conditions.get("page").toString()),
//				Integer.parseInt(conditions.get("rows").toString()));
		return map;
	}

	public List<VehAlarmHandled> queryAlarmHandleList(String bjxh) throws Exception {
		String sql = "select * from veh_alarm_handled where bjxh = ?";
		return this.queryForList(sql, new Object[] { bjxh }, VehAlarmHandled.class);
	}

	public List<VehAlarmHandled> queryCityAlarmHandleList(String bjxh, String cityname) throws Exception {
		String sql = "select * from veh_alarm_handled";
		if (cityname != null) {
			sql += "@" + cityname;
		}
		sql += " where bjxh = ?";
		return this.queryForList(sql,new String[]{bjxh}, VehAlarmHandled.class);
	}

	public List<VehAlarmLivetrace> getLivetraceList(String bjxh) throws Exception {
		String sql = "select * from veh_alarm_livetrace where bjxh = ?";
		return this.queryForList(sql, new Object[] { bjxh }, VehAlarmLivetrace.class);
	}

	public List<VehAlarmLivetrace> getCityLivetraceList(String bjxh, String cityname) throws Exception {
		String sql = "SELECT * from VEH_ALARM_LIVETRACE";
		if (cityname != null) {
			sql += "@" + cityname;
		}
		sql += " where bjxh = ?";
		return this.queryForList(sql, new Object[] { bjxh }, VehAlarmLivetrace.class);
	}

	public int saveHandledConfirm(VehAlarmHandled vehAlarmHandled) throws Exception {
		String sql = "update veh_alarm_handled "
				+ "set by1=?,by2=?,fkqsdwmc=?,by4=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),by5=? where fkbh = ?";
		List vl=new ArrayList<>();
		vl.add(vehAlarmHandled.getBy1());
		vl.add(vehAlarmHandled.getBy2());
		vl.add(vehAlarmHandled.getFkqsdwmc());
		vl.add(vehAlarmHandled.getBy5());
		vl.add(vehAlarmHandled.getFkbh());
		return this.jdbcTemplate.update(sql,vl.toArray());
	}

	public VehAlarmHandled queryAlarmHandled(String fkbh) throws Exception {
		String sql = "select * from veh_alarm_handled where fkbh = ?";
		List<VehAlarmHandled> list = this.queryForList(sql, new Object[] { fkbh }, VehAlarmHandled.class);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public List<VehAlarmrec> getAlarmList(String bkxh) throws Exception {
		String sql = "select * from veh_alarmrec where bkxh = ?";
		return this.queryForList(sql, new Object[] { bkxh }, VehAlarmrec.class);
	}

	public List<VehAlarmHandled> queryAlarmhandleConfirmed(String bjxh) throws Exception {
		String sql = "select * from veh_alarm_handled where bjxh = ? and by1 is not null and by2 is not null";
		return this.queryForList(sql, new Object[] { bjxh }, VehAlarmHandled.class);
	}

	public List<VehAlarmHandled> queryCityAlarmhandleConfirmed(String bjxh, String cityname) throws Exception {
		String sql = "select * from veh_alarm_handled";
		if (cityname != null) {
			sql += "@" + cityname;
		}
		sql += " where bjxh = ? and by1 is not null and by2 is not null";
		return this.queryForList(sql,new String[]{bjxh}, VehAlarmHandled.class);
	}

	public int getAlarmCount(List<SqlCondition> conditions) throws Exception {
		
		StringBuffer sb = new StringBuffer("select count(*) from ");
		sb.append(getTableName());
		sb.append(" where qrzt = '0' ");
		return this.jdbcTemplate.queryForObject(sb.toString(), Integer.class);
	}

	public int getAlarmSignOutCount(String glbm) throws Exception {
		StringBuffer sb = new StringBuffer("select count(1) from ");
		sb.append(getTableName());
		sb.append(" where qrzt = '0' and bjdwdm = ?");
		 return this.jdbcTemplate.queryForObject(sb.toString(),new String[]{glbm},
		 Integer.class);
//		return this.getSelf().getRecordCounts(sb.toString(), 0);
	}

	public int getConfirmOut() throws Exception {
		StringBuffer sb = new StringBuffer("select bjxh,hphm from ");
		sb.append(getTableName());
		sb.append(
				" where qrzt > '0' and  TO_NUMBER(qrsj - bjsj)*24*60*60 > 2*60 and bjsj > trunc(sysdate, 'mm') and bjsj < sysdate ");
		// return this.jdbcTemplate.queryForObject(sb.toString(),
		// Integer.class);
		return this.getSelf().getRecordCounts(sb.toString(), 0);
	}

	public int saveDxsj(Map<String, String> dxcondition) throws Exception {
//		String xh = "";
//		String dxjshm = "";
//		String dxjsr = "";
//		if (dxcondition.get("xh") != null) {
//			if (!dxcondition.get("xh").toString().equals(""))
//				xh = "'" + dxcondition.get("xh") + "',";
//		}
//		dxjshm = "'" + dxcondition.get("dxjshm") + "',";
//		dxjsr = "'" + dxcondition.get("dxjsr") + "',";
//		// }
List vl=new ArrayList<>();
vl.add(dxcondition.get("xh"));
vl.add(dxcondition.get("ywlx"));
vl.add(dxcondition.get("dxjsr"));
vl.add(dxcondition.get("dxjshm"));
vl.add(dxcondition.get("dxnr"));
		String sql = "insert into frm_communication(id,xh,ywlx,dxjsr,dxjshm,dxnr,dxfssj)values(SEQ_FRM_COMMUNICATION.nextval,"
				+"?,?,?,?,?,"
				+ "sysdate" + ")";
		return this.jdbcTemplate.update(sql,vl.toArray());
	}

	public List<VehAlarmrec> getNoLJ() throws Exception {
		String sql = " select * from veh_alarm_handled where by1= '1' and nvl(by3,0) <> '1' ";

		return this.queryForList(sql, VehAlarmrec.class);
	}

	public List<VehAlarmrec> getNoCancelSus() throws Exception {
		String sql = " select * from veh_alarm_handled where by1= '1' and nvl(by3,0) <> '1' ";
		return this.jdbcTemplate.queryForList(sql, VehAlarmrec.class);
	}


	public Map<String, Object> getAlarmList(String kdbh, Page page) throws Exception {
		List vl=new ArrayList<>();
		StringBuffer sb = new StringBuffer(
				"select a.bjdl,a.bjlx,a.bjsj,a.bjdwdm,a.hphm,a.hpzl,a.kdbh,a.kdmc,a.fxbh,a.fxmc,a.cllx,a.clsd,a.hpys,s.bkjb,t.kkjd,t.kkwd,a.tp1"
						// + " from "+TabName.ALARMTAB+" a ,"+TabName.SUSPTAB+"
						// s "+","+TabName.CODE_Gate+" t "
						+ " from VEH_ALARMREC a,code_gate t,veh_suspinfo s " + " where 1 = 1 ");
		sb.append(" and a.kdbh=t.kdbh ");
		sb.append(" and a.bkxh = s.bkxh ");
		if (StringUtils.isNotBlank(kdbh)) {
			vl.add(kdbh);
			sb.append(" and a.kdbh in (?)");
		}
		sb.append(" and sysdate >= a.bjsj and sysdate < a.bjsj+1/24/60*10 ");
		// .append(" order by bjsj desc ");
		return queryforMapnew(String.valueOf(page.getCurPage()),String.valueOf( page.getPageSize()), sb.toString(), vl);
//		return this.findPageForMap(sb.toString(), page.getCurPage(), page.getPageSize());
	}

	/**
	 * 轮询跨省预警记录(5秒区间的数据)
	 * 
	 * @throws Exception
	 */
	public List<VehAlarmrec> selectToHnKSAlarm(String xzqh) throws Exception {

		String sql = "select BJXH, BJDL, BJLX, BKXH, to_char(BJSJ,'yyyy-mm-dd hh24:mi:ss') as BJSJ, BJDWDM, BJDWMC, BJDWLXDH, "
				+ "HPHM, HPZL, GCXH, to_char(GCSJ,'yyyy-mm-dd hh24:mi:ss') as GCSJ, SBBH, SBMC, KDBH, KDMC, FXBH, FXMC, CLLX, "
				+ "CLSD, HPYS, CWHPYS, HPYZ, CDBH, CLWX, CSYS, TP1, TP2, TP3, QRR, QRRJH, QRRMC,"
				+ "QRDWDM, QRDWDMMC, QRDWLXDH, to_char(QRSJ,'yyyy-mm-dd hh24:mi:ss') as QRSJ, QRZT, QRJG, to_char(GXSJ,'yyyy-mm-dd hh24:mi:ss') as GXSJ, JYLJTJ, XXLY, SFXDZL,SFFK,SFLJ from (select b.* from veh_alarmrec b,veh_suspinfo c where b.bkxh = c.bkxh and c.bkjg in (select xjjg from frm_prefecture t where dwdm = ?) and c.bkfwlx = '3' and b.gxsj >= sysdate-20/24/60/60 and  b.gxsj < sysdate)";
		log.info("执行的sql语句为：" + sql);
		RowMapper<VehAlarmrec> rm = ParameterizedBeanPropertyRowMapper.newInstance(VehAlarmrec.class);
		return this.jdbcTemplate.query(sql, new Object[] { xzqh }, rm);
	}

	/**
	 * 轮询跨省预警记录(10秒区间的数据)
	 * 
	 * @throws Exception
	 */
	public List<VehAlarmrec> selectToGdKSAlarm(String xzqh) throws Exception {

		String sql = "select BJXH, BJDL, BJLX, BKXH, to_char(BJSJ,'yyyy-mm-dd hh24:mi:ss') as BJSJ, BJDWDM, BJDWMC, BJDWLXDH, "
				+ "HPHM, HPZL, GCXH, to_char(GCSJ,'yyyy-mm-dd hh24:mi:ss') as GCSJ, SBBH, SBMC, KDBH, KDMC, FXBH, FXMC, CLLX, "
				+ "CLSD, HPYS, CWHPYS, HPYZ, CDBH, CLWX, CSYS, TP1, TP2, TP3, QRR, QRRJH, QRRMC,"
				+ "QRDWDM, QRDWDMMC, QRDWLXDH, to_char(QRSJ,'yyyy-mm-dd hh24:mi:ss') as QRSJ, QRZT, QRJG, to_char(GXSJ,'yyyy-mm-dd hh24:mi:ss') as GXSJ, JYLJTJ, XXLY, SFXDZL,SFFK,SFLJ from (select b.* from veh_alarmrec b,veh_suspinfo c where b.bkxh = c.bkxh and c.bkjg not in (select xjjg from frm_prefecture t where dwdm = ?) and c.bkfwlx = '3' and b.gxsj >= sysdate-10/24/60/60 and  b.gxsj < sysdate)";

		RowMapper<VehAlarmrec> rm = ParameterizedBeanPropertyRowMapper.newInstance(VehAlarmrec.class);
		log.debug("执行的sql语句为：" + sql);
		List<VehAlarmrec> list = this.jdbcTemplate.query(sql, new Object[] { xzqh }, rm);
		log.debug("轮询预警条数为：" + list.size());
		return list;
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