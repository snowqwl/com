package com.sunshine.monitor.system.monitor.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.CallableStatementCallbackImpl;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.monitor.bean.KhzbProject;
import com.sunshine.monitor.system.monitor.bean.TimeCondition;
import com.sunshine.monitor.system.monitor.dao.KhzbProjectDao;

@Repository("khzbProjectDao")
public class KhzbProjectDaoImpl extends BaseDaoImpl implements KhzbProjectDao {

	@SuppressWarnings("unchecked")
	public KhzbProject getKhzbProjectInfo(String kssj,
			String jssj, String city) throws Exception {
		List param = new ArrayList<>();
		//获取年度规划完成率得分
		KhzbProject khbProject = new KhzbProject();
		StringBuffer sb = new StringBuffer("select tt31.ghjrs,tt30.city,tt31.ghnd,to_char(tt30.tjsj, 'yyyy-mm') as tjsj,tt30.sjjrs,round(sjjrs / ghjrs, 2) as wcl");
		sb.append(" from (select sum(pk1) over(partition by cstr_col05 order by cstr_col02) as ghjrs,cstr_col02 as ghnd from Monitor.t_monitor_jg_integrate_detail t");
		sb.append(" where khfa_id = '31' and date_col01 = (select max(date_col01) from Monitor.t_monitor_jg_integrate_detail where khfa_id = '31' and date_col01 between");
		sb.append("  to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date(?, 'yyyy-mm-dd " +
				"hh24:mi:ss')) and cstr_col01 like ?) tt31 ");
		sb.append(" join (select trunc(date_col01, 'mm') as tjsj, cstr_col05 as city,count(1) as sjjrs from Monitor.t_monitor_jg_integrate_detail ");
		sb.append(" where khfa_id = '30' and date_col01 = (select max(date_col01)  from Monitor.t_monitor_jg_integrate_detail where khfa_id = '30'");
		sb.append("  and date_col01 between  to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date(?, " +
				"'yyyy-mm-dd hh24:mi:ss') and cstr_col05 like ?)");
		sb.append(" group by trunc(date_col01, 'mm'), cstr_col05 order by tjsj) tt30 on tt31.ghnd = to_char(tt30.tjsj, 'yyyy')");
		param.add(kssj);
		param.add(jssj);
		param.add(city+"%");
		param.add(kssj);
		param.add(jssj);
		param.add(city+"%");
		Map map = this.findPageForMap(sb.toString(), param.toArray(),1, 1);
		Map kjrMap = null;
		List list = (List) map.get("rows");
		if (list != null && list.size() > 0) {
			kjrMap = (Map) list.get(0);
		}
		if (kjrMap != null) {
			khbProject.setGhwcl(Float.parseFloat(kjrMap.get("wcl").toString())*40 + "");
		}

		List param2 = new ArrayList<>();
		//获取卡口在线率得分
		StringBuffer kkstring = new StringBuffer("select count1 as bzxs,count2 as kkzs,tt26.ds as ds,tt26.tjsj as tjsj,round((count2 - count1) / count2, 2) as zxl");
		kkstring.append(" from (select count(distinct cstr_col02) as count1,cstr_col05 as ds,to_char(date_col02, 'yyyy-mm-dd') as tjsj from Monitor.t_monitor_jg_integrate_detail");
		kkstring.append("  where khfa_id = '26' and cstr_col05 like ? and date_col02 between  " +
				"to_date(?, 'yyyy-mm-dd hh24:mi:ss') and ");
		kkstring.append(" to_date(?, 'yyyy-mm-dd hh24:mi:ss') group by to_char(date_col02, " +
				"'yyyy-mm-dd'), cstr_col05) tt26 join (select count(1) as count2,");
		kkstring.append("  to_char(date_col01, 'yyyy-mm-dd') as tjsj from Monitor" +
				".t_monitor_jg_integrate_detail where khfa_id = '27'  and cstr_col01 like ?");
		kkstring.append(" and date_col01 between to_date(?, 'yyyy-mm-dd hh24:mi:ss') and to_date" +
				"(?, 'yyyy-mm-dd hh24:mi:ss')");
		kkstring.append(" group by to_char(date_col01, 'yyyy-mm-dd'), cstr_col01) tt27 on tt26.tjsj = tt27.tjsj order by tt26.tjsj desc");
		param2.add(city+"%");
		param2.add(kssj);
		param2.add(kssj);
		param2.add(city+"%");
		param2.add(kssj);
		param2.add(jssj);

		map = this.findPageForMap(kkstring.toString(), param.toArray(),1, 1);
		List listkkzx = (List) map.get("rows");
		Map kkzxMap = null;
		if (listkkzx != null && listkkzx.size() > 0) {
			kkzxMap = (Map) listkkzx.get(0);
		}
		if (kkzxMap != null) {
			khbProject.setKkzxl(Float.parseFloat(kkzxMap.get("zxl").toString())*10 + "");
		}
		
		return khbProject;
		
	}
	
	public int saveKhzb(KhzbProject khzbProject) throws Exception {
		List param = new ArrayList<>();
		String sql =
				"Select count(*) from JM_KHXM Where tjfs=? and kssj=to_date(?,'yyyy-MM-dd " +
						"HH24:mi:ss') and jssj = to_date(?,'yyyy-MM-dd HH24:mi:ss') and city=?";
		param.add(khzbProject.getTjfs());
		param.add(khzbProject.getKssj());
		param.add(khzbProject.getJssj());
		param.add(khzbProject.getCity());
		String tmpSql = null;
		int count = this.jdbcTemplate.queryForInt(sql,param.toArray());

		List param1 = new ArrayList<>();
		if (count > 0) {
			tmpSql =
					"Update JM_KHXM set ghwcl=?,cscks=?,lhs=?,wcks=?,kkzxl=?,xjlhs=?,gzxyjsl=? " +
							"Where tjfs=? and kssj=to_date(?,'yyyy" +
							"-MM-dd HH24:mi:ss') and jssj=" +
			"to_date(?,'yyyy-MM-dd HH24:mi:ss') and city=?";
			param1.add(khzbProject.getGhwcl());
			param1.add(khzbProject.getCscks());
			param1.add(khzbProject.getLhs());
			param1.add(khzbProject.getWcks());
			param1.add(khzbProject.getKkzxl());
			param1.add(khzbProject.getXjlhs());
			param1.add(khzbProject.getGzxyjsl());
			param1.add(khzbProject.getTjfs());
			param1.add(khzbProject.getKssj());
			param1.add(khzbProject.getJssj());
			param1.add(khzbProject.getCity());

		} else {
			StringBuffer bzsb = new StringBuffer("Insert into JM_KHXM(tjfs,KSSJ,JSSJ,CITY,GHWCL,CSCKS,LHS,wcks,kkzxl,gzxyjsl,xjlhs,pf,zdf,lrr) values(");
			bzsb.append("?,to_date(?,'yyyy" +
					"-MM-dd HH24:mi:ss')" + ",to_date(?,'yyyy-MM-dd HH24:mi:ss'),");
			bzsb.append("?,?,?,?,?,?,?,?,?,?,?)");
			param1.add(khzbProject.getTjfs());
			param1.add(khzbProject.getKssj());
			param1.add(khzbProject.getJssj() );
			param1.add(khzbProject.getCity());
			param1.add(khzbProject.getGhwcl());
			param1.add(khzbProject.getCscks());
			param1.add(khzbProject.getLhs() );
			param1.add(khzbProject.getWcks());
			param1.add(khzbProject.getKkzxl());
			param1.add(khzbProject.getGzxyjsl());
			param1.add(khzbProject.getXjlhs());
			param1.add(khzbProject.getPf());
			param1.add(khzbProject.getZdf());
			param1.add(khzbProject.getLrr());

			tmpSql = bzsb.toString();

		}
		return this.jdbcTemplate.update(tmpSql,param1.toArray());
	}

	public Map getKhzbProjectList(Map filter,KhzbProject khzbInfo) {
		List param = new ArrayList<>();
		String tmpSql = "";
		if (khzbInfo.getTjfs() != null && khzbInfo.getTjfs().length() > 0) {
			tmpSql = tmpSql + " and tjfs=?";
			param.add(khzbInfo.getTjfs());
		}
		if (khzbInfo.getKssj() != null && khzbInfo.getKssj().length() > 0) {
			tmpSql = tmpSql + " and kssj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(khzbInfo.getKssj());
		}
		if (khzbInfo.getJssj() != null && khzbInfo.getJssj().length() > 0) {
			tmpSql = tmpSql + " and jssj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(khzbInfo.getJssj());
		}

		tmpSql = "Select kh.tjfs,c.dwdm as city,kh.kssj,kh.jssj,kh.ghwcl,kh.cscks,kh.lhs,kh.wcks,kh.kkzxl,kh.gzxyjsl,kh.pf,kh.zdf,kh.lrsj,kh.lrr,c.jdmc from JM_KHXM kh,CODE_URL c Where kh.city=c.dwdm"
				+ tmpSql + " order by c.bz ASC";
		Map<String, Object> queryMap = this.getSelf().findPageForMap(tmpSql, param.toArray(),Integer
				.parseInt(filter.get("curPage").toString()), Integer
				.parseInt(filter.get("pageSize").toString()));
		return queryMap;
	}

	public KhzbProject getKhzbProjectforObject(KhzbProject khzbProject) throws Exception {
		String tmpSql = "";
		List param = new ArrayList<>();
		if (khzbProject.getTjfs() != null && khzbProject.getTjfs().length() > 0) {
			tmpSql = tmpSql + " and tjfs=?";
			param.add(khzbProject.getTjfs());
		}
		if (khzbProject.getKssj() != null && khzbProject.getKssj().length() > 0) {
			tmpSql = tmpSql + " and kssj >= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(khzbProject.getKssj());
		}
		if (khzbProject.getJssj() != null && khzbProject.getJssj().length() > 0) {
			tmpSql = tmpSql + " and jssj <= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			param.add(khzbProject.getJssj());
		}
		if (tmpSql.length() > 1) {
			tmpSql = " Where " + tmpSql.substring(5,tmpSql.length()) + " ";
			}
		tmpSql = "Select * from JM_KHXM" + tmpSql;
		return this.queryForObject(tmpSql,param.toArray(), KhzbProject.class);
	}

	public float queryNdghRate(String kssj, String jssj) throws Exception {
		String callString = "{call jcbk_khzb_pkg.queryNdghRate(?,?,?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				TimeCondition timeCondition = (TimeCondition) getParameterObject();
				if (timeCondition.getKssj() == null) {
					cstmt.setNull(1, 12);
				} else {
					cstmt.setString(1, timeCondition.getKssj());
				}
				if (timeCondition.getJssj() == null) {
					cstmt.setNull(2, 12);
				} else {
					cstmt.setString(2, timeCondition.getJssj());
				}
				cstmt.registerOutParameter(3, Types.FLOAT);
				cstmt.execute();
				float iResult = cstmt.getFloat(3);
				return iResult;
			}
		};
		TimeCondition timeCondition = new TimeCondition();
		timeCondition.setKssj(kssj);
		timeCondition.setJssj(jssj);
		callBack.setParameterObject(timeCondition);
		Float i = (Float) this.jdbcTemplate.execute(callString, callBack);
		return i.floatValue();
	}

	public float queryLHS(String kssj, String jssj) throws Exception {
		String callString = "{call jcbk_khzb_pkg.queryLHS(?,?,?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				TimeCondition timeCondition = (TimeCondition) getParameterObject();
				if (timeCondition.getKssj() == null) {
					cstmt.setNull(1, 12);
				} else {
					cstmt.setString(1, timeCondition.getKssj());
				}
				if (timeCondition.getJssj() == null) {
					cstmt.setNull(2, 12);
				} else {
					cstmt.setString(2, timeCondition.getJssj());
				}
				cstmt.registerOutParameter(3, Types.FLOAT);
				cstmt.execute();
				float iResult = cstmt.getFloat(3);
				return iResult;
			}
		};
		TimeCondition timeCondition = new TimeCondition();
		timeCondition.setKssj(kssj);
		timeCondition.setJssj(jssj);
		callBack.setParameterObject(timeCondition);
		Float i = (Float) this.jdbcTemplate.execute(callString, callBack);
		return i.floatValue();
	}

	public float queryCSCKS(String kssj, String jssj) throws Exception {
		String callString = "{call jcbk_khzb_pkg.queryCSCKS(?,?,?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				TimeCondition timeCondition = (TimeCondition) getParameterObject();
				if (timeCondition.getKssj() == null) {
					cstmt.setNull(1, 12);
				} else {
					cstmt.setString(1, timeCondition.getKssj());
				}
				if (timeCondition.getJssj() == null) {
					cstmt.setNull(2, 12);
				} else {
					cstmt.setString(2, timeCondition.getJssj());
				}
				cstmt.registerOutParameter(3, Types.FLOAT);
				cstmt.execute();
				float iResult = cstmt.getFloat(3);
				return iResult;
			}
		};
		TimeCondition timeCondition = new TimeCondition();
		timeCondition.setKssj(kssj);
		timeCondition.setJssj(jssj);
		callBack.setParameterObject(timeCondition);
		Float i = (Float) this.jdbcTemplate.execute(callString, callBack);
		return i.floatValue();
	}

	public float queryKkzxRate(String kssj, String jssj) throws Exception {
		String callString = "{call jcbk_khzb_pkg.queryKkzxRate(?,?,?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				TimeCondition timeCondition = (TimeCondition) getParameterObject();
				if (timeCondition.getKssj() == null) {
					cstmt.setNull(1, 12);
				} else {
					cstmt.setString(1, timeCondition.getKssj());
				}
				if (timeCondition.getJssj() == null) {
					cstmt.setNull(2, 12);
				} else {
					cstmt.setString(2, timeCondition.getJssj());
				}
				cstmt.registerOutParameter(3, Types.FLOAT);
				cstmt.execute();
				float iResult = cstmt.getFloat(3);
				return iResult;
			}
		};
		TimeCondition timeCondition = new TimeCondition();
		timeCondition.setKssj(kssj);
		timeCondition.setJssj(jssj);
		callBack.setParameterObject(timeCondition);
		Float i = (Float) this.jdbcTemplate.execute(callString, callBack);
		return i.floatValue();
	}

	public float queryGzjsRate(String kssj, String jssj) throws Exception {
		String callString = "{call jcbk_khzb_pkg.queryGzjsRate(?,?,?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				TimeCondition timeCondition = (TimeCondition) getParameterObject();
				if (timeCondition.getKssj() == null) {
					cstmt.setNull(1, 12);
				} else {
					cstmt.setString(1, timeCondition.getKssj());
				}
				if (timeCondition.getJssj() == null) {
					cstmt.setNull(2, 12);
				} else {
					cstmt.setString(2, timeCondition.getJssj());
				}
				cstmt.registerOutParameter(3, Types.FLOAT);
				cstmt.execute();
				float iResult = cstmt.getFloat(3);
				return iResult;
			}
		};
		TimeCondition timeCondition = new TimeCondition();
		timeCondition.setKssj(kssj);
		timeCondition.setJssj(jssj);
		callBack.setParameterObject(timeCondition);
		Float i = (Float) this.jdbcTemplate.execute(callString, callBack);
		return i.floatValue();
	}
}
