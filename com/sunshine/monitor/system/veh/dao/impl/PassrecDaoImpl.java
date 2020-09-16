package com.sunshine.monitor.system.veh.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.CallableStatementCallbackImpl;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.dao.PassrecDao;
import com.sunshine.monitor.system.ws.VehPassrecEntity;
import com.sunshine.monitor.system.ws.PassrecService.bean.ContrailEntity;

@Repository("passrecDao")
public class PassrecDaoImpl extends BaseDaoImpl implements PassrecDao {

	public PassrecDaoImpl() {

		super.setTableName("VEH_PASSREC");
	}

	public Map<String, Object> getPassrecList(Map<String, Object> conditions,
			String tableName) throws Exception {
		String table = "";
		if (tableName == null || "".equals(tableName)) {
			table = this.getTableName();
		} else {
			table = tableName;
		}
		StringBuffer sql = new StringBuffer(50);

		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();

			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					sql.append(" and gcsj >= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.indexOf("jssj") != -1) {
					sql.append(" and gcsj <= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if ("hphm".equalsIgnoreCase(key)) {
					String temp = Common.changeHphm(value).toUpperCase();
					if ((temp.indexOf("%") > 0) || (temp.indexOf("_") > 0)) {
						sql.append(" and hphm like '" + temp + "' ");
					} else {
						sql.append(" and hphm = '" + temp + "' ");
					}
				} else if ("kdbh".equalsIgnoreCase(key)) {

					sql.append(" and kdbh in ('" + value + "') ");

				} /*else if ("glbm".equalsIgnoreCase(key)) {
					String sonSql = "select * from code_gate where dwdm='"
							+ value + "'";
					List<CodeGate> list = this.queryForList(sonSql,
							CodeGate.class);
					String kdbhsql = "";
					if (list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							CodeGate cg = (CodeGate) list.get(i);
							kdbhsql = kdbhsql + "kdbh='" + cg.getKdbh()
									+ "' or ";
						}
						sql.append(" and ("
								+ kdbhsql.substring(0, kdbhsql.length() - 3)
								+ ") ");
					}
				}*/ else if ("licesenHeader".equalsIgnoreCase(key)) {
					String[] licesenHeaders = value.trim().split("\\p{Punct}");
					if (licesenHeaders.length <= 0)
						continue;
					sql.append("and ( ");
					for (String licesenHeader : licesenHeaders) {
						sql.append(" hphm like '" + licesenHeader + "%' or ");
					}
					sql.delete(sql.length() - 3, sql.length());
					sql.append(")");
				} else {
					
					if("hphm".equalsIgnoreCase(key)||"hpzl".equalsIgnoreCase(key)||"hpys".equalsIgnoreCase(key)||"fxbh".equalsIgnoreCase(key)) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" = '").append(value).append("'");
					}

				}
			}
		}
		// 暂时不排序
		sql.append(" order by ");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));

		StringBuffer sb = new StringBuffer("select t.* from " + table
				+ " t where 1=1 ");
		sb.append(sql);
		Map<String, Object> map  = null;
		//第一次查询统计总数，点击datagrid分页按钮只查询当页数据，使用分页标记
		if("0".equals(conditions.get("pageSign"))) {
		if (conditions.get("cityname") == null
				|| conditions.get("cityname").toString().length() < 1) {
			map = this.getSelf().findPageForMapNoLimit(sb.toString(), Integer
					.parseInt(conditions.get("page").toString()), Integer
					.parseInt(conditions.get("rows").toString()),
					VehPassrec.class);
		} else {
			JdbcTemplate jd =  SpringApplicationContext.getRemoteSourse(conditions.get("cityname").toString(), "jcbk", false);
			map = this.getSelf().findPageForMapNoLimitByJdbc(sb.toString(), Integer.parseInt(conditions.get("page").toString()),
				Integer.parseInt(conditions.get("rows").toString()),
				VehPassrec.class, jd);
		}
		} else {
			if (conditions.get("cityname") == null
					|| conditions.get("cityname").toString().length() < 1) {
				map = this.getSelf().queryPageForMap(sb.toString(), Integer
						.parseInt(conditions.get("page").toString()), Integer
						.parseInt(conditions.get("rows").toString()), VehPassrec.class, Integer.parseInt(conditions.get("total").toString()), "");
			} else {
				JdbcTemplate jd =  SpringApplicationContext.getRemoteSourse(conditions.get("cityname").toString(), "jcbk", false);
				map = this.getSelf().queryPageForMapByJdbc(sb.toString(), Integer
						.parseInt(conditions.get("page").toString()), Integer
						.parseInt(conditions.get("rows").toString()), VehPassrec.class, Integer.parseInt(conditions.get("total").toString()), jd);
			}
			
		}
		return map;
	}

	/**
	 * 根据号牌号码查询过车信息列表（模糊查询）
	 * 
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPassrecListByHphm(
			Map<String, Object> conditions) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select * from veh_passrec t where hphm = '");
		sql.append(conditions.get("hphm")+"'");
		//.append(" order by gcsj desc");
		Map<String, Object> map = this.getSelf().findPageForMapNoLimit(
				sql.toString(),
				Integer.parseInt(conditions.get("page").toString()),
				Integer.parseInt(conditions.get("rows").toString()),
				VehPassrec.class);
		return map;
	}

	/**
	 * 查询过车详细信息
	 * 
	 * @param gcxh
	 * @return
	 * @throws Exception
	 */
	public VehPassrec getVehPassrecDetail(String gcxh) throws Exception {
		String sql = " select t.* from veh_passrec t where gcxh  ='" + gcxh
				+ "'";
		VehPassrec v = this.queryForObject(sql, VehPassrec.class);
		return v;
	}

	/**
	 * 查询旧版过车信息列表
	 * 
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOldPassrecList(
			Map<String, Object> conditions, String tableName) throws Exception {

		String table = "";
		if (tableName == null || "".equals(tableName)) {
			table = this.getTableName();
		} else {
			table = tableName;
		}
		StringBuffer sql = new StringBuffer(50);

		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();

			if (("cityname").equalsIgnoreCase(key)) {
				table += "@" + value + "  ";
			}
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					sql.append(" and gcsj >= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.indexOf("jssj") != -1) {
					sql.append(" and gcsj <= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if ("hphm".equalsIgnoreCase(key)) {
					String temp = Common.changeHphm(value).toUpperCase();
					if ((temp.indexOf("%") > 0) || (temp.indexOf("_") > 0)) {
						sql.append(" and hphm like '" + temp + "' ");
					} else {
						sql.append(" and hphm = '" + temp + "' ");
					}
				} else if ("kdbh".equalsIgnoreCase(key)) {

					sql.append(" and kdbh in ('" + value + "') ");

				} else if ("glbm".equalsIgnoreCase(key)) {
					String sonSql = "select * from dc.code_gate where dwdm='"
							+ value + "'";
					List<CodeGate> list = this.queryForList(sonSql,
							CodeGate.class);
					String kdbhsql = "";
					if (list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							CodeGate cg = (CodeGate) list.get(i);
							kdbhsql = kdbhsql + "kdbh='" + cg.getKdbh()
									+ "' or ";
						}
						sql.append(" and ("
								+ kdbhsql.substring(0, kdbhsql.length() - 3)
								+ ") ");
					}
				} else {

					if (!("cityname".equalsIgnoreCase(key) || "city"
							.equalsIgnoreCase(key))) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" = '").append(value).append("'");
					}

				}
			}
		}
		// 暂时不排序
		sql.append(" order by ");
		sql.append(conditions.get("sort"));
		sql.append(" ");
		sql.append(conditions.get("order"));

		StringBuffer sb = new StringBuffer("select * from " + table
				+ " t where 1=1 ");
		sb.append(sql);
		Map<String, Object> map = this.getSelf().queryPageForMap(sb.toString(),
				Integer.parseInt(conditions.get("page").toString()),
				Integer.parseInt(conditions.get("rows").toString()),
				VehPassrec.class, 501, this.getTableName());
		return map;

	}

	public List<VehPassrecEntity> queryPassrecLimitedList(String sql)
			throws Exception {
		List<VehPassrecEntity> list = this.queryForList(sql,
				VehPassrecEntity.class);
		return list;
	}

	public List<VehPassrec> queryPassrecForList(Map<String, Object> conditions)
			throws Exception {
		return this.queryForListByMap(conditions, this.getTableName(),
				VehPassrec.class);
	}

	public Map<String, Object> findPagePassrecForMap(Map<String, Object> map,
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
					sb.append(" and gcsj > to_date('" + map.get(val)
							+ "','yyyy-mm-dd hh24:mi:ss')");
				} else if (val.equalsIgnoreCase("jssj")) {
					sb.append(" and gcsj <= to_date('" + map.get(val)
							+ "','yyyy-mm-dd hh24:mi:ss')");
				} else if (val.equalsIgnoreCase("hphm")) {
					sb.append(" and hphm like '%" + map.get(val) + "%' ");
				} else if (!(val.equalsIgnoreCase("page") || val
						.equalsIgnoreCase("rows"))) {
					sb.append(" and " + val + "='" + map.get(val) + "'");
				}
			}
			if (map.containsKey("page")) {
				curPage = Integer.parseInt(map.get("page").toString());
			}
			if (map.containsKey("rows")) {
				pageSize = Integer.parseInt(map.get("rows").toString());
			}
		}
		StringBuffer sql = new StringBuffer(
				" select * from veh_passrec where 1=1 ");
		sql.append(sb);
		sql.append(" order by gcsj desc ");
		result = this.findPageForMap(sql.toString(), curPage, pageSize, clazz);
		return result;
	}

	public int getPassrecCountInThisMonth() throws Exception {
		// String sql =
		// "select count(1) from veh_passrec where gcsj between trunc(sysdate,'mm') and sysdate";
		String sql = "select sum(gcs) from stat_flow_sunshine";
		return this.jdbcTemplate.queryForInt(sql);
	}

	public Map<String, Object> getHphm(Map<String, Object> map)
			throws Exception {
		int curPage = 1;
		int pageSize = 10;
		StringBuffer sb = new StringBuffer();
		if (map != null) {
			Set<String> set = map.keySet();
			for (String s : set) {
				if (s.equalsIgnoreCase("hphm")) {
					sb.append(" and hphm like '%" + map.get(s) + "%' ");
				}
				if (s.equalsIgnoreCase("kssj")) {
					sb.append(" and gcsj > to_date('" + map.get(s)
							+ "','yyyy-mm-dd hh24:mi:ss')");
				}
				if (s.equalsIgnoreCase("jssj")) {
					sb.append(" and gcsj <= to_date('" + map.get(s)
							+ "','yyyy-mm-dd hh24:mi:ss')");
				}
				if (s.equalsIgnoreCase("page")) {
					curPage = Integer.parseInt(map.get(s).toString());
				}
				if (s.equalsIgnoreCase("rows")) {
					pageSize = Integer.parseInt(map.get(s).toString());
				}
			}
		}
		StringBuffer sql = new StringBuffer(
				"select distinct HPHM, CWHPHM from veh_passrec where 1=1");
		sql.append(sb).append(" order by hphm desc");
		Map<String, Object> result = this.findPageForMap(sql.toString(),
				curPage, pageSize, VehPassrecEntity.class);
		return result;
	}

	public Map<String, Object> queryContrailForMap(Map<String, Object> map)
			throws Exception {
		int curPage = 1;
		int pageSize = 10;
		StringBuffer sb = new StringBuffer();
		if (map != null) {
			Set<String> set = map.keySet();
			for (String s : set) {
				if (s.equalsIgnoreCase("hphm")) {
					sb.append(" and hphm = '" + map.get(s) + "' ");
				}
				if (s.equalsIgnoreCase("kssj")) {
					sb.append(" and gcsj > to_date('" + map.get(s)
							+ "','yyyy-mm-dd hh24:mi:ss')");
				}
				if (s.equalsIgnoreCase("jssj")) {
					sb.append(" and gcsj <= to_date('" + map.get(s)
							+ "','yyyy-mm-dd hh24:mi:ss')");
				}
				if (s.equalsIgnoreCase("page")) {
					curPage = Integer.parseInt(map.get(s).toString());
				}
				if (s.equalsIgnoreCase("rows")) {
					pageSize = Integer.parseInt(map.get(s).toString());
				}
			}
		}
		StringBuffer sql = new StringBuffer(
				"select to_char(v.gcsj, 'yyyy-mm-dd hh24:mi:ss') as gcsj,c.kdmc,c.KKJD,c.KKWD,v.GCXH,v.KDBH from CODE_GATE c, veh_passrec v where  v.KDBH = c.KDBH ");
		sql.append(sb).append(" order by v.gcsj asc");
		Map<String, Object> result = this.findPageForMap(sql.toString(),
				curPage, pageSize, ContrailEntity.class);
		return result;
	}

	public Map getCityFlow(String cityname) throws Exception {
		String sql = "select sum(gcs) as gcs,sum(zcs) as zcs,sum(sncs) as sncs, sum(swcs) as swcs,sum(ygcs) as ygcs,sum(jjcs) as jjcs, sum(wpcs) as wpcs, sum(lpcs) as lpcs,sum(hpcs) as hpcs, sum(qtcs) as qtcs from stat_flow_sunshine";
		sql += "@" + cityname;
		return this.jdbcTemplate.queryForMap(sql);
	}

	public void updateSTFlow(final String cityname) throws Exception {
		String callString = "{call JM_STAT_FLOW_PKG.GET_FLOW(?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				cstmt.setString(1,cityname);
				return new Object();
			}
		};
		this.jdbcTemplate.execute(callString, callBack);
	}
	
	/**
	 * 用户查询提示
	 * @param condition
	 * @return
	 */
	public int queryTips(Map condition) {
		StringBuffer sql = new StringBuffer("");
		if(condition.get("kdbh")!=null && !"".equals(condition.get("kdbh"))) {
			sql.append(" and kdbh = '").append(condition.get("kdbh")).append("' ");
		}
		if(condition.get("fxbh")!=null && !"".equals(condition.get("fxbh"))) {
			sql.append(" and fxbh = '").append(condition.get("fxbh")).append("' ");
		}
		if(condition.get("licesenHeader")!=null && !"".equals(condition.get("licesenHeader"))) {
			String[] licesenHeaders = condition.get("licesenHeader").toString().trim().split("\\p{Punct}");
			if (licesenHeaders.length > 0) {
			sql.append("and ( ");
			for (String licesenHeader : licesenHeaders) {
				sql.append(" hphm like '" + licesenHeader + "%' or ");
			}
			sql.delete(sql.length() - 3, sql.length());
			sql.append(") ");
			}
		}
		if(condition.get("hphm")!=null && !"".equals(condition.get("hphm"))) {
			sql.append(" and hphm = '").append(condition.get("hphm")).append("' ");
		}
		if(condition.get("hpzl")!=null && !"".equals(condition.get("hpzl"))) {
			sql.append(" and hpzl = '").append(condition.get("hpzl")).append("' ");
		}
		if(condition.get("kssj")!=null && !"".equals(condition.get("kssj"))) {
			sql.append(" and gcsj >= ").append(
					"to_date('" + condition.get("kssj") + "','yyyy-mm-dd hh24:mi:ss')");
		}
		if(condition.get("jssj")!=null && !"".equals(condition.get("jssj"))) {
			sql.append(" and gcsj <= ").append(
					"to_date('" + condition.get("jssj") + "','yyyy-mm-dd hh24:mi:ss')");
		}
		StringBuffer indexSql=new StringBuffer("select * from veh_passrec t where 1=1 "); 
		sql=indexSql.append(sql);
		Map map = null;
		int total=0;
		if (condition.get("cityname") == null
				|| condition.get("cityname").toString().length() < 1) {
		    
			total = this.getTotalCount(sql);
			//map = this.getSelf().findPageForMap(sql.toString(), 1, 1, VehPassrec.class);
		} else {
			JdbcTemplate jd =  SpringApplicationContext.getRemoteSourse(condition.get("cityname").toString(), "jcbk", false);
			map = this.getSelf().findPageForMapByJdbc(sql.toString(), 1, 10,VehPassrec.class, jd);
		
		}
		// total = Integer.parseInt(map.get("total").toString());
		return total;
	}
    public int getTotalCount(StringBuffer sql) {
    	StringBuffer totalSQL = new StringBuffer(" SELECT count(1) FROM ( select /*+FIRST_ROWS*/*  from (");	
    	totalSQL.append(sql);
    	totalSQL.append(") where rownum <= 1000 ) totalTable ");
    	int total=0;
    	List list=null;
    	try{
    		System.out.println(totalSQL.toString());
    	 total=this.jdbcTemplate.queryForInt(totalSQL.toString());
    	 if(total!=0){
    		 return total;
    	 }
    	}
    	catch(Exception e){
    		return -1;
    	}
    	return total;
    }

    
    
  

	public int getAllCount(Map<String, Object> conditions) throws Exception {
		String table = "";
			table = this.getTableName();
		StringBuffer sql = new StringBuffer(50);
		boolean existHphm= false;
		Set<Entry<String, Object>> set = conditions.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			// 时间段，则按以下规定命名表单域kksj_字段(即开始时间)或jssj_字段(即结束时间)
			String key = entry.getKey();
			String value = (String) entry.getValue();
			boolean isFilter = "sort".equalsIgnoreCase(key)
					|| "order".equalsIgnoreCase(key)
					|| "page".equalsIgnoreCase(key)
					|| "rows".equalsIgnoreCase(key);
			if (!isFilter) {
				if (key.indexOf("kssj") != -1) {
					sql.append(" and gcsj >= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if (key.indexOf("jssj") != -1) {
					sql.append(" and gcsj <= ").append(
							"to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
				} else if ("hphm".equalsIgnoreCase(key)) {
					String temp = Common.changeHphm(value).toUpperCase();
					existHphm=true;
					if ((temp.indexOf("%") > 0) || (temp.indexOf("_") > 0)) {
						sql.append(" and hphm like '" + temp + "' ");
					} else {
						sql.append(" and hphm = '" + temp + "' ");
					}
				} else if ("kdbh".equalsIgnoreCase(key)) {

					sql.append(" and kdbh in ('" + value + "') ");

				}  else if ("licesenHeader".equalsIgnoreCase(key)) {
					String[] licesenHeaders = value.trim().split("\\p{Punct}");
					if (licesenHeaders.length <= 0)
						continue;
					sql.append("and ( ");
					for (String licesenHeader : licesenHeaders) {
						sql.append(" hphm like '" + licesenHeader + "%' or ");
					}
					sql.delete(sql.length() - 3, sql.length());
					sql.append(")");
				} else {
					
					if("hphm".equalsIgnoreCase(key)||"hpzl".equalsIgnoreCase(key)||"hpys".equalsIgnoreCase(key)||"fxbh".equalsIgnoreCase(key)) {
						sql.append(" and ");
						sql.append(key);
						sql.append(" = '").append(value).append("'");
					}

				}
			}
		}
		StringBuffer sb = new StringBuffer();
	
		sb.append(sql);
	    
		return this.jdbcTemplate.queryForInt(sb.toString());
	}

}
