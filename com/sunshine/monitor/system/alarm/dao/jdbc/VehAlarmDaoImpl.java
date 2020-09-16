package com.sunshine.monitor.system.alarm.dao.jdbc;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.ObjArray;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.dao.page.PagingParameter;
import com.sunshine.monitor.comm.util.AbstractLobCreatingPreparedStatementCallbackImpl;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.activemq.bean.TransAlarmHandled;
import com.sunshine.monitor.system.activemq.bean.TransCmd;
import com.sunshine.monitor.system.activemq.bean.TransLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.VehAlarmDao;
import com.sunshine.monitor.system.manager.bean.SysUser;

@Repository("vehAlarmDao")
public class VehAlarmDaoImpl extends BaseDaoImpl implements VehAlarmDao {

	public Map getAlarmHandleForMap(Map filter, VehAlarmCmd qc) throws Exception {

		StringBuffer sb = new StringBuffer(" ");
		sb.append(
				"select c.zldwmc as ZLXDDWMC,c.zlxh,a.bkxh, a.bjxh, a.hpzl, a.hphm, to_char(a.bjsj,'yyyy-mm-dd hh24:mi:ss') as  bjsj, a.kdbh, a.bjdl, a.bjlx, to_char(c.zlsj,'yyyy-mm-dd hh24:mi:ss') as zlxdsj, s.sffk ");
		sb.append("from veh_alarmrec a, veh_alarm_cmd c, veh_alarm_cmdscope s ");
		sb.append("where a.bjxh=c.bjxh and c.zlxh=s.zlxh");
		List vl = new ArrayList<>();
		if ((qc.getHpzl() != null) && (!"".equals(qc.getHpzl()))) {
			sb.append(" and a.hpzl=?");
			vl.add(qc.getHpzl());
		}
		if ((qc.getHphm() != null) && (!"".equals(qc.getHphm()))) {
			vl.add("%" + qc.getHphm() + "%");
			sb.append(" and a.hphm  like  ?");

		}
		if ((qc.getBjdl() != null) && (!"".equals(qc.getBjdl()))) {
			sb.append(" and a.bjdl= ?");
			vl.add(qc.getBjdl());
		}
		if ((qc.getBjlx() != null) && (!"".equals(qc.getBjlx()))) {
			vl.add(qc.getBjlx());
			sb.append(" and a.bjlx= ?");
		}
		if ((qc.getKdbh() != null) && (!"".equals(qc.getKdbh()))) {
			sb.append(" and a.kdbh in ('").append(qc.getKdbh()).append("')");
		}
		if ((qc.getZldw() != null) && (!"".equals(qc.getZldw()))) {
			sb.append(" and c.zldw=?");
			vl.add(qc.getZldw());
		}

		// 查询出指令接受单位为登录用户单位的指令信息
		if ((qc.getZljsdw() != null) && (!"".equals(qc.getZljsdw()))) {
			sb.append(" and s.zljsdw='").append(qc.getZljsdw()).append("'");
			vl.add(qc.getZldw());
		}
		if ((qc.getSffk() != null) && (!"".equals(qc.getSffk()))) {
			sb.append(" and s.sffk=?");
			vl.add(qc.getSffk());
		}
		if ((qc.getJssj() != null) && (!"".equals(qc.getJssj()))) {
			vl.add(qc.getJssj());
			sb.append(" and to_char(c.zlsj,'yyyy-mm-dd')<=?");
		}
		if ((qc.getKssj() != null) && (!"".equals(qc.getKssj()))) {
			sb.append(" and to_char(c.zlsj,'yyyy-mm-dd')>=? ");
			vl.add(qc.getKssj());
		}
		if (qc.getKdbh() != null && !"".equals(qc.getKdbh()) && qc.getCity() != null && !"".equals(qc.getCity())) {
			sb.append(" and substr(?,0,4) = substr(?,0,4)");
			vl.add(qc.getKdbh());
			vl.add(qc.getCity());
		}

		sb.append("  order by sffk asc ,c.zlsj desc   ");

		return queryforMapnew(filter.get("curPage").toString(), filter.get("pageSize").toString(), sb.toString(), vl);
		// this.getSelf().findPageForMap(sb.toString(),
		// Integer.parseInt(filter.get("curPage").toString()),
		// Integer.parseInt(filter.get("pageSize").toString()));
	}

	public VehAlarmrec getVehAlarmForBjxh(String bjxh) throws Exception {

		String sql = "select * from veh_alarmrec where bjxh = ?";
		List list = this.queryForList(sql, new Object[] { bjxh }, VehAlarmrec.class);
		if (list.size() > 0)
			return (VehAlarmrec) list.get(0);
		else
			return null;
	}

	public VehAlarmCmd getVehAlarmCmdForZlxh(String zlxh) throws Exception {
		VehAlarmCmd cmd = null;

		String sql = "select * from veh_alarm_cmd  where zlxh = ?";
		List list = this.queryForList(sql, new Object[] { zlxh }, VehAlarmCmd.class);
		if (list.size() > 0) {
			cmd = (VehAlarmCmd) list.get(0);
		}
		return cmd;
	}

	public List getVehAlarmLivetraceListForBjxh(String bjxh) throws Exception {

		String sql = "select * from veh_alarm_livetrace where bjxh = ?";

		return this.queryForList(sql, new Object[] { bjxh }, VehAlarmLivetrace.class);
	}

	public int insertAlarmHandle(final VehAlarmHandled handle, String xzqh) throws Exception {
		List vl = new ArrayList<>();
		vl.add(xzqh);
		vl.add(handle.getBkxh());
		vl.add(handle.getBjxh());
		vl.add(handle.getZlxh());
		vl.add(handle.getGcxh());
		vl.add(handle.getLrr());
		vl.add(handle.getLrrjh());
		vl.add(handle.getLrrmc());
		vl.add(handle.getLrdwdm());
		vl.add(handle.getLrdwmc());
		// vl.add( sysdate );
		vl.add(handle.getDdr());
		vl.add(handle.getDdrmc());
		vl.add(handle.getXbr());
		vl.add(handle.getXbrmc());
		vl.add(handle.getSflj());
		vl.add(handle.getWljdyy() == null ? "" : handle.getWljdyy());
		vl.add(handle.getClgc());

		StringBuffer sb = new StringBuffer(
				"INSERT into veh_alarm_handled (FKBH, BKXH, BJXH, ZLXH, GCXH, LRR, LRRJH, LRRMC,LRDWDM, LRDWMC,");
		sb.append("  LRSJ, DDR, DDRMC, XBR, XBRMC, SFLJ,WLJDYY, CLGC, BJSJ, BJDWDM, BJDWMC, BJDWLXDH, XXLY,by3) ");
		sb.append(" values(");
		sb.append("?||SEQ_ALARM_HANDLED_FKBH.NEXTVAL").append(",");
		sb.append("?,?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,");
		if (StringUtils.isNotBlank(handle.getBjsj()) && handle.getBjsj().length() > 10) {
			vl.add(handle.getBjsj());
			sb.append("to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
		} else {
			vl.add(handle.getBjsj());
			sb.append("to_date(?,'yyyy-mm-dd'),");
		}
		vl.add(handle.getBjdwdm());
		vl.add(handle.getBjdwmc());
		vl.add(handle.getBjdwlxdh());
		vl.add(handle.getBy3());
		sb.append("?,?,?,?");
		sb.append(")");

		return this.jdbcTemplate.update(sb.toString(), vl.toArray());

	}

	public int insertLjTp(final String fkbh, final VehAlarmHandled handle) throws Exception {
		String sql = " INSERT INTO VEH_ALARM_HANDLED_PICREC(fkbh,ljtp)  values(?,?)";

		int c = this.jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallbackImpl(lobHandler) {
			public void setValues(PreparedStatement pstmt, LobCreator lobCreator)
					throws SQLException, DataAccessException {

				pstmt.setString(1, fkbh);
				lobCreator.setBlobAsBytes(pstmt, 2, handle.getTp());

			}
		});
		return c;
	}

	public TransAlarmHandled getAlarmHandledLink(String ywxh) throws Exception {
		Field[] field = TransAlarmHandled.class.getDeclaredFields();
		StringBuffer sqlbuf = new StringBuffer("select ");

		boolean isFirst = true;
		for (int i = 0; i < field.length; i++) {
			if (field[i].getModifiers() == 2) {
				if (isFirst) {
					sqlbuf.append(field[i].getName());
					isFirst = false;
				} else {
					sqlbuf.append(",").append(field[i].getName());
				}
			}
		}
		sqlbuf.append(" from ").append("VEH_ALARM_HANDLED");
		sqlbuf.append(" where ").append("FKBH").append("='").append(ywxh).append("'");
		TransAlarmHandled bean = (TransAlarmHandled) this.queryForObject(sqlbuf.toString(), TransAlarmHandled.class);
		// .jdbcTemplate.queryForBean(sqlbuf.toString(),
		// TransAlarmHandled.class);
		if (bean == null) {
			throw new Exception("出警反馈传输，获取传输对象为空！");
		}
		List cmdList = this.queryForList("select * from veh_alarm_cmd where bjxh=?", new Object[] { bean.getBjxh() },
				TransCmd.class);
		bean.setCmdList(cmdList);
		List livetraceList = this.queryForList("select * from veh_alarm_livetrace where bjxh=?",
				new Object[] { bean.getBjxh() }, TransLivetrace.class);
		bean.setLivetraceList(livetraceList);
		return bean;
	}

	public int saveAlarmHandleLink(TransAlarmHandled handle) throws Exception {
		String alarm_sql = "SELECT count(*) FROM VEH_ALARM_HANDLED WHERE FKBH = ?";
		int count = this.jdbcTemplate.queryForInt(alarm_sql, new Object[] { handle.getFkbh() });
		List vl = new ArrayList<>();
		if (count == 0) {
			vl.add(handle.getFkbh());
			vl.add(handle.getBkxh());
			vl.add(handle.getBjxh());
			vl.add(handle.getZlxh());
			vl.add(handle.getGcxh());
			vl.add(handle.getLrr());
			vl.add(handle.getLrrjh());
			vl.add(handle.getLrrmc());
			vl.add(handle.getLrdwdm());
			vl.add(handle.getLrdwmc());
			// vl.add("',sysdate,'");
			vl.add(handle.getDdr());
			vl.add(handle.getDdrmc());
			vl.add(handle.getXbr());
			vl.add(handle.getXbrmc());
			vl.add(handle.getSflj());
			vl.add(handle.getWljdyy());
			vl.add(handle.getClgc());
			StringBuffer sb = new StringBuffer(
					"INSERT into veh_alarm_handled (FKBH, BKXH, BJXH, ZLXH, GCXH, LRR, LRRJH, LRRMC,LRDWDM, LRDWMC,");
			sb.append("  LRSJ, DDR, DDRMC, XBR, XBRMC, SFLJ,WLJDYY, CLGC, BJSJ, BJDWDM, BJDWMC, BJDWLXDH, XXLY) ");
			sb.append(" values( ");
			sb.append("?,?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,");

			if (StringUtils.isNotBlank(handle.getBjsj()) && handle.getBjsj().length() > 10) {
				vl.add(handle.getBjsj());
				sb.append("to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
			} else {
				vl.add(handle.getBjsj());
				sb.append("to_date(?,'yyyy-mm-dd'),");
			}
			vl.add(handle.getBjdwdm());
			vl.add(handle.getBjdwmc());
			vl.add(handle.getBjdwlxdh());
			vl.add(handle.getXxly());
			sb.append("?,?,?,?");
			sb.append(")");

			return this.jdbcTemplate.update(sb.toString(), vl.toArray());
		} else {
			vl.add(handle.getBy1());
			vl.add(handle.getBy2() == null ? "" : handle.getBy2());
			vl.add(handle.getBy3() == null ? "" : handle.getBy3());
			vl.add(handle.getBy4() == null ? "" : handle.getBy4());
			vl.add(handle.getBy5() == null ? "" : handle.getBy5());
			vl.add(handle.getFkqsdwmc() == null ? "" : handle.getFkqsdwmc());
			vl.add(handle.getFkbh());

			// 指挥中心签收反馈
			String sql = "UPDATE veh_alarm_handled set" + " by1=?," + "by2=?," + "by3=?," + "by4=?," + "by5=?,"
					+ "fkqsdwmc=?  WHERE FKBH = ?";
			return this.jdbcTemplate.update(sql, vl.toArray());
		}
	}

	public int updateAlarmForHandle(String sflj, String bjxh) throws Exception {
		String sql = " UPDATE veh_alarmrec set sffk = '1',";
		List vl = new ArrayList<>();
		if ("1".equals(sflj)) {
			sql += " sflj = '1', ";
		}

		sql += " gxsj = sysdate where bjxh = ?";

		return this.jdbcTemplate.update(sql, new Object[] { bjxh });
	}

	public int insertBusinessForHandle(String fkbh, String ywlb, String bkjb, String bzsj, SysUser user, String ssjz,
			String sfwfzr) throws Exception {
		List vl = new ArrayList<>();
		StringBuffer sb = new StringBuffer(
				"INSERT into business_log(XH, YWXH, YWLB, YWJB, CZSJ, BZSJ, GXSJ, CZRDH, CZRJH, CZRDWDM, CZRDWJZ, SFWFZR, YYWK, YTJ) ");
		sb.append("   values(SEQ_BUSINESS_LOG_XH.nextval, ");
		sb.append("?,?,?,");
		sb.append(" sysdate,to_date(?,'yyyy-mm-dd");
		vl.add(fkbh);
		vl.add(ywlb);
		vl.add(bkjb);
		vl.add(bzsj);
		if (bzsj.length() > 10)
			sb.append(" hh24:mi:ss");

		sb.append("'),sysdate,'");
		vl.add(user.getYhdh());
		vl.add(user.getJh());
		vl.add(user.getGlbm());
		vl.add(ssjz);
		vl.add(sfwfzr);
		sb.append("?,?,?,?,?,'0','0'");
		return this.jdbcTemplate.update(sb.toString(), vl.toArray());
	}

	public int updateAlarmScopeForHandle(String zlxh) throws Exception {
		String sql = "UPDATE veh_alarm_cmdscope set sffk = '1' where zlxh = ?";

		return this.jdbcTemplate.update(sql, new Object[] { zlxh });
	}

	public int insertTransTabForHandle(String csdw, String jsdw, String ywxh, String type, String tableName)
			throws Exception {

		StringBuffer sb = new StringBuffer("INSERT into ");
		sb.append(tableName).append("(CSXH,");
		List vl = new ArrayList<>();
		if (jsdw != null)
			sb.append(" CSDW, JSDW,");

		sb.append(" CSBJ, CSSJ, YWXH, TYPE )  values(").append("?").append("|| SEQ_TRANS_CSXH.NEXTVAL,");
		vl.add(csdw);
		if (jsdw != null) {
			vl.add(csdw);
			vl.add(jsdw);
			sb.append("?,?,");
		}
		sb.append("0,sysdate,?,? )");
		vl.add(ywxh);
		vl.add(type);
		return this.jdbcTemplate.update(sb.toString(), vl.toArray());
	}

	public String getVehAlarmHandleFkxhForBean(VehAlarmHandled handle) throws Exception {
		String str = "";
		String sql = "select * from veh_alarm_handled where zlxh = ?  and lrr = ? order by lrsj desc";

		List list = this.queryForList(sql, new Object[] { handle.getZlxh(), handle.getLrr() }, VehAlarmHandled.class);
		if (list.size() > 0) {
			str = ((VehAlarmHandled) list.get(0)).getFkbh();
		}

		return str;
	}

	public Map getAlarmLiveTraceForMap(Map filter, VehAlarmCmd qc, String gzbj) throws Exception {
		List vl = new ArrayList<>();
		StringBuffer sb = new StringBuffer(
				"select aa.*,to_char(aa.bjsj,'yyyy-mm-dd hh24:mi:ss') as bjsjlive,(case when aa.num0 > 0 then 1 else 0 end)GZBJ   from  ");

		sb.append(
				"(select a.BJXH,a.bkxh,a.kdbh,a.HPZL,a.HPHM, a.bjsj,a.bjdl,a.bjlx,a.kdmc,a.fxmc  ,count(live.xh) as num0  from veh_alarmrec a,veh_alarm_livetrace live   where a.bjxh = live.bjxh(+) and  ");
		String zl = "select distinct bjxh from veh_alarm_cmd c where c.zldw=?";
		vl.add(qc.getZldw());
		if ((qc.getJssj() != null) && (!"".equals(qc.getJssj()))) {
			vl.add(qc.getJssj());
			zl = zl + " and c.zlsj<to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		if ((qc.getKssj() != null) && (!"".equals(qc.getKssj()))) {
			vl.add(qc.getKssj());
			zl = zl + " and c.zlsj>to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		sb.append(" a.bjxh in (").append(zl).append(")");

		if ((qc.getHpzl() != null) && (!"".equals(qc.getHpzl()))) {
			sb.append(" and a.hpzl='").append(qc.getHpzl()).append("'");
		}
		if ((qc.getHphm() != null) && (!"".equals(qc.getHphm()))) {
			if ((qc.getHphm().indexOf('*') != -1) || (qc.getHphm().indexOf('?') != -1)) {
				vl.add(Common.changeSqlChar(qc.getHpzl()));
				sb.append(" and a,hphm like ?");
			} else {
				vl.add(qc.getHphm() + "%");
				sb.append(" and a.hphm like ?");
			}
		}
		if ((qc.getBjdl() != null) && (!"".equals(qc.getBjdl()))) {
			vl.add(qc.getBjdl());
			sb.append(" and a.bjdl=?");
		}
		if ((qc.getBjlx() != null) && (!"".equals(qc.getBjlx()))) {
			vl.add(qc.getBjlx());
			sb.append(" and a.bjlx=?");
		}
		if ((qc.getKdbh() != null) && (!"".equals(qc.getKdbh()))) {
			vl.add(qc.getBjlx());
			sb.append(" and a.kdbh in (?)");
		}
		sb.append(
				"  group by a.BJXH,a.bkxh,a.HPZL,a.HPHM, a.bjsj,a.bjdl,a.bjlx,a.kdmc,a.fxmc,a.kdbh  order by a.bjsj desc)aa  ");

		if (StringUtils.isNotBlank(gzbj)) {
			if ("0".equals(gzbj)) {
				sb.append(" where aa.num0 = 0");
			} else {
				sb.append(" where aa.num0 > 0");
			}

		}
		return queryforMapnew(filter.get("curPage").toString(), filter.get("pageSize").toString(), sb.toString(), vl);
		// return this.getSelf().findPageForMap(sb.toString(),
		// Integer.parseInt(filter.get("curPage").toString()),
		// Integer.parseInt(filter.get("pageSize").toString()));
	}

	public List getVehAlarmCmdListForBjxh(String bjxh) throws Exception {
		String sql = "select * from veh_alarm_cmd  where bjxh = ?";

		return this.queryForList(sql, new Object[] { bjxh }, VehAlarmCmd.class);
	}

	public List getCmdscopeListForBjxh(String bjxh) throws Exception {
		StringBuffer sb = new StringBuffer(" select * from VEH_ALARM_CMDSCOPE where zlxh in(");
		sb.append("select zlxh  from veh_alarm_cmd where bjxh = ?");
		sb.append(") ");

		return this.queryForList(sb.toString(), new Object[] { bjxh }, VehAlarmCmd.class);
	}

	public int saveLiveTrace(VehAlarmLivetrace liveTrace) throws Exception {
		List vl = new ArrayList<>();
		vl.add(liveTrace.getBjxh());
		vl.add(liveTrace.getBgdw());
		vl.add(liveTrace.getBgdwmc());
		vl.add(liveTrace.getBgr());
		StringBuffer sb = new StringBuffer(" INSERT into veh_alarm_livetrace");
		sb.append("(XH, ZLXH,  BJXH, BGDW, BGDWMC, BGR, BGRJH, LRR, LRRJH, LRRMC, LRDWDM,LRDWMC, LRSJ, NR)  values(");
		sb.append(liveTrace.getLrdwdm()).append("|| SEQ_ALARM_LIVETRACE_XH.Nextval,'', ");
		sb.append("?,?,?,?,");
		if (StringUtils.isNotBlank(liveTrace.getBgrjh())) {
			vl.add(liveTrace.getBgrjh());
			sb.append("?,");
		} else {
			sb.append(" '' ,");
		}
		vl.add(liveTrace.getLrr());
		vl.add(liveTrace.getLrrjh());
		vl.add(liveTrace.getLrrmc());
		vl.add(liveTrace.getLrdwdm());
		vl.add(liveTrace.getLrdwmc());
		// vl.add(sysdate );
		vl.add(liveTrace.getNr());

		sb.append("?,?,?,?,?,sysdate,?");
		return this.jdbcTemplate.update(sb.toString(), vl.toArray());
	}

	public int saveLiveTrace(TransLivetrace liveTrace) throws Exception {
		// 如果存在该出警跟踪记录则先删除
		List vl = new ArrayList<>();
		String susp_sql = "SELECT count(*) FROM veh_alarm_livetrace WHERE XH = ?";
		int count = this.jdbcTemplate.queryForInt(susp_sql, new Object[] { liveTrace.getXh() });
		if (count > 0) {
			susp_sql = "delete from veh_alarm_livetrace where XH=?";
			this.jdbcTemplate.update(susp_sql, new Object[] { liveTrace.getXh() });
		}

		StringBuffer sb = new StringBuffer(" INSERT into veh_alarm_livetrace");
		sb.append(
				"(XH, ZLXH,  BJXH, BGDW, BGDWMC, BGR, BGRJH, LRR, LRRJH, LRRMC, LRDWDM,LRDWMC, LRSJ, NR, XXLY)  values(");
		vl.add(liveTrace.getXh());
		vl.add(liveTrace.getBjxh());
		vl.add(liveTrace.getBgdw());
		vl.add(liveTrace.getBgdwmc());
		vl.add(liveTrace.getBgr());
		sb.append("?,?,?,?,?,");
		if (StringUtils.isNotBlank(liveTrace.getBgrjh())) {
			vl.add(liveTrace.getBgrjh());
			sb.append("?,");
		} else {
			sb.append(" '', ");
		}
		vl.add(liveTrace.getLrr());
		vl.add(liveTrace.getLrrjh());
		vl.add(liveTrace.getLrrmc());
		vl.add(liveTrace.getLrdwdm());
		vl.add(liveTrace.getLrdwmc());
		vl.add(liveTrace.getNr());
		vl.add(liveTrace.getXxly());
sb.append("?,?,?,?,?,sysdate,?,?)");
		return this.jdbcTemplate.update(sb.toString(),vl.toArray());
	}

	public List getAlarmHandleForBjxh(String bjxh) throws Exception {
		String sql = "select * from veh_alarm_handled where bjxh = ? order by lrsj desc";

		return this.queryForList(sql, new Object[] { bjxh }, VehAlarmHandled.class);
	}

	public List getAlarmHandleForBkxh(String bkxh) throws Exception {
		String sql = "select * from veh_alarm_handled where bkxh = ? order by lrsj desc";

		return this.queryForList(sql, new Object[] { bkxh }, VehAlarmHandled.class);
	}

	public int getAlarmNoHandleCount(String begin, String end, String glbm) throws Exception {
		List vl=new ArrayList<>();
		vl.add(glbm);
		String tmpSql = "Select count(1) from VEH_ALARMREC Where BJDWDM=? and BJDL in('1','2') and QRZT='0'";
		if (begin != null && begin.length() > 0) {
			vl.add(begin + " 00:00:00");
			tmpSql = tmpSql + " and bjsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			vl.add(end + " 23:59:59");
			tmpSql = tmpSql + " and bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		int count =this.jdbcTemplate.queryForObject(tmpSql,Integer.class ,vl.toArray() );// this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}

	public int getAlarmCmdCount(String begin, String end, String glbm) throws Exception {
		String tmpSql = "Select * from VEH_ALARMREC Where BJDWDM = ? and BJDL in('1','2') and QRZT='1' and SFXDZL='0'";
		List vl=new ArrayList<>();
		vl.add(glbm);
		if (begin != null && begin.length() > 0) {
			vl.add(begin);
			tmpSql = tmpSql + " and bjsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			vl.add(end);
			tmpSql = tmpSql + " and bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		int count =this.jdbcTemplate.queryForObject(tmpSql,Integer.class ,vl.toArray() );// this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}

	public int getAlarmNoHandledBackCount(String begin, String end, String glbm) throws Exception {
		String tmpSql = "Select a.* from VEH_ALARMREC a,VEH_ALARM_CMD c, VEH_ALARM_CMDSCOPE s Where a.bjxh=c.bjxh and c.zlxh=s.zlxh and zljsdw=? and s.sffk='0'";
		List vl=new ArrayList<>();	
		vl.add(glbm);
		if (begin != null && begin.length() > 0) {
			vl.add(begin+ " 00:00:00");
			tmpSql = tmpSql + " and c.zlsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			vl.add(end+ " 23:59:59");
			tmpSql = tmpSql + " and c.zlsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		int count =this.jdbcTemplate.queryForObject(tmpSql,Integer.class ,vl.toArray() );// this.getSelf().getRecordCounts(tmpSql, 0);
		return count;
	}

	public int getAlarmNoLjSuspinfoCountForZx(String begin, String end, String glbm, String yhmc, String jb,
			String bmlx) throws Exception {
		List vl=new ArrayList<>();
		String tmpSql = "select a.* from veh_alarm_handled t, veh_alarmrec a, "
				+ " veh_suspinfo s where t.bjxh = a.bjxh(+)  "
				+ "and t.bkxh = s.bkxh and t.sflj = '1' and t.by1 = '1' and t.by4 is not null and s.BJZT = '1' and s.ywzt = '14' and s.jlzt = '1' and (s.by3 = '0' or s.by3 is null) ";
		if (begin != null && begin.length() > 0) {
			vl.add(begin+ " 00:00:00");
			tmpSql = tmpSql + " and bksj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			vl.add(end+ " 23:59:59");
			tmpSql = tmpSql + " and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}

		// tmpSql="select count(*) from ("+tmpSql+") t";
		// int count = this.jdbcTemplate.queryForInt(tmpSql);
		// Map map = this.getSelf().findPageForMap(tmpSql, 1, 10);
		// int count = (Integer) map.get("total");
		int count =this.jdbcTemplate.queryForObject(tmpSql,Integer.class ,vl.toArray() );// this.getSelf().getRecordCounts(tmpSql, 0);
		
		return count;//this.getSelf().getRecordCounts(tmpSql, 0);
	}

	public int getAlarmNoLjSuspinfoCount(String begin, String end, String glbm, String yhmc, String jb, String bmlx)
			throws Exception {
		List vl=new ArrayList<>();
		String tmpSql = "select a.bjxh, a.hphm from veh_alarm_handled t, veh_alarmrec a, "
				+ "(select bkxh, czr, czrjh, czrmc, czrdw, czrdwmc, czsj, czjg, bzw from audit_approve  where bzw = '2' and czjg = '1') r, veh_suspinfo s where t.bjxh = a.bjxh(+) and t.bkxh = r.bkxh(+)"
				+ "and t.bkxh = s.bkxh and t.sflj = '1' and t.by1 = '1' and t.by4 is not null and s.BJZT = '1' and s.ywzt = '14' and s.jlzt = '1' and (s.by3 = '0' or s.by3 is null) and r.czrdw = ?";
		vl.add(glbm);
		if (begin != null && begin.length() > 0) {
			vl.add(begin+ " 00:00:00");
			tmpSql = tmpSql + " and  s.bksj >= to_date('" + begin + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			vl.add(end+ " 23:59:59");
			tmpSql = tmpSql + " and s.bksj <= to_date('" + end + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		// tmpSql="select count(*) from ("+tmpSql+") t";
		// return this.jdbcTemplate.queryForInt(tmpSql);
int count =this.jdbcTemplate.queryForObject(tmpSql,Integer.class ,vl.toArray() );// this.getSelf().getRecordCounts(tmpSql, 0);
		
		return count;

	}

	public int getAlarmNoCancelSuspinfoCount(String begin, String end, String glbm, String yhmc) throws Exception {
		String tmpSql = "Select bkxh,hphm from VEH_SUSPINFO Where (((BKDL='1' or BKDL= '2') and bkjg='" + glbm
				+ "') or (bkdl='3' and bkr='" + yhmc + "')) and YWZT='14' and XXLY='0' and BJZT='1'";
		List vl=new ArrayList<>();
		vl.add(glbm);
		vl.add(yhmc);		 
		if (begin != null && begin.length() > 0) {
			vl.add(begin+ " 00:00:00");
			tmpSql = tmpSql + " and bksj >= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			vl.add(end+ " 23:59:59");
			tmpSql = tmpSql + " and bksj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		// tmpSql="select count(*) from ("+tmpSql+") t";
		// return this.jdbcTemplate.queryForInt(tmpSql);
int count =this.jdbcTemplate.queryForObject(tmpSql,Integer.class ,vl.toArray() );// this.getSelf().getRecordCounts(tmpSql, 0);
		
		return count;
	}

	public int getAlarmSuspinfoCount(String begin, String end, String yhdh) throws Exception {
		String tmpSql = "Select count(*) from VEH_SUSPINFO Where XXLY='0' and BJZT='1' and bkr='" + yhdh + "'";
		List vl=new ArrayList<>();
		vl.add(yhdh);
		if (begin != null && begin.length() > 0) {
			vl.add(begin+ " 00:00:00");
			tmpSql = tmpSql + " and bksj >= to_date('" + begin + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if (end != null && end.length() > 0) {
			vl.add(end+ " 23:59:59");
			tmpSql = tmpSql + " and bksj <= to_date('" + end + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}		
		return this.jdbcTemplate.queryForInt(tmpSql,vl.toArray());
	}

	public Map getAlarmLJForMap(Map filter, VehAlarmCmd info) throws Exception {
		List vl=new ArrayList<>();
		StringBuffer sb = new StringBuffer(
				"select  handle.bkxh as bkxh,handle.bjxh as bjxh, handle.zlxh as zlxh,rec.hpzl  ,rec.hphm,handle.bjsj,rec.bjdl,rec.bjlx,handle.lrdwmc as LJDWMC,handle.by3 as SFYX ");
		sb.append(
				",handle.fkbh  from  veh_alarmrec rec,veh_alarm_handled handle where rec.bjxh = handle.bjxh  and handle.sflj ='1' ");
		sb.append(" and handle.lrdwdm in (select xjjg from frm_prefecture  where dwdm = ?) ");
vl.add(info.getZldw());
		if (StringUtils.isNotBlank(info.getBjdl())) {
			vl.add(info.getBjdl());
			sb.append(" and rec.bjdl = ? ");
		}

		if (StringUtils.isNotBlank(info.getBjlx())) {
			vl.add(info.getBjdl());
			sb.append(" and rec.bjlx = ? ");
		}

		if (StringUtils.isNotBlank(info.getHphm())) {
			vl.add(info.getHphm()+"%");
			sb.append(" and rec.hphm like ? ");
		}

		if (StringUtils.isNotBlank(info.getKssj())) {
			vl.add(info.getKssj());
			sb.append(" and rec.bjsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
		}

		if (StringUtils.isNotBlank(info.getJssj())) {
			vl.add(info.getJssj());
			sb.append(" and rec.bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
		}
return queryforMapnew(filter.get("curPage").toString(), filter.get("pageSize").toString(), sb.toString(), vl);
//		return this.getSelf().findPageForMap(sb.toString(), Integer.parseInt(filter.get("curPage").toString()),
//				Integer.parseInt(filter.get("pageSize").toString()));
	}

	public VehAlarmHandled getAlarmHandleForFkbh(String fkbh) throws Exception {
		String sql = "select *  from veh_alarm_handled  where fkbh = ?";

		List list = this.queryForList(sql, new Object[] { fkbh }, VehAlarmHandled.class);

		if (list.size() > 0)
			return (VehAlarmHandled) list.get(0);
		else
			return null;
	}

	public Map getAlarmSTLJForMap(Map filter, VehAlarmCmd info, String cityname) throws Exception {
		StringBuffer sb = new StringBuffer("select " + "'" + cityname + "'"
				+ "  as city, handle.bkxh as bkxh,handle.bjxh as bjxh, handle.zlxh as zlxh,rec.hpzl  ,rec.hphm,handle.bjsj,rec.bjdl,rec.bjlx,handle.lrdwmc as LJDWMC,handle.by3 as SFYX ");
		sb.append(",handle.fkbh  from  veh_alarmrec@" + cityname + " rec,veh_alarm_handled@" + cityname
				+ "  handle where rec.bjxh = handle.bjxh  and handle.sflj ='1' ");
		List vl=new ArrayList<>();
		if (StringUtils.isNotBlank(info.getBjdl())) {
			vl.add(info.getBjdl());
			sb.append(" and rec.bjdl = ? ");
		}

		if (StringUtils.isNotBlank(info.getBjlx())) {
			vl.add(info.getBjlx());
			sb.append(" and rec.bjlx = ? ");
		}

		if (StringUtils.isNotBlank(info.getHphm())) {
			vl.add(info.getHphm()+"%");
			sb.append(" and rec.hphm like ? ");
		}

		if (StringUtils.isNotBlank(info.getKssj())) {
			vl.add(info.getKssj() );
			sb.append(" and rec.bjsj >= to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
		}

		if (StringUtils.isNotBlank(info.getJssj())) {
			vl.add(info.getJssj() );
			sb.append(" and rec.bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
		}
return queryforMapnew(filter.get("curPage").toString(), filter.get("pageSize").toString(), sb.toString(), vl);
//		return this.getSelf().findPageForMap(sb.toString(), Integer.parseInt(filter.get("curPage").toString()),
//				Integer.parseInt(filter.get("pageSize").toString()));
	}

	public Map<String, Object> findPageVehAlarmForMap(Map<String, Object> map, Class<?> classz) throws Exception {
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
					vl.add(map.get(val));
					sb.append(" and bjsj > to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				} else if (val.equalsIgnoreCase("jssj")) {
					vl.add(map.get(val));
					sb.append(" and bjsj <= to_date(?,'yyyy-mm-dd hh24:mi:ss')");
				} else if (!(val.equalsIgnoreCase("page") || val.equalsIgnoreCase("rows"))) {
					sb.append(" and " + val + "=?");
					vl.add(map.get(val));
				}
			}
			if (map.containsKey("page")) {
				curPage = Integer.parseInt(map.get("page").toString());
			}
			if (map.containsKey("rows")) {
				pageSize = Integer.parseInt(map.get("rows").toString());
			}
		}

		StringBuffer sql = new StringBuffer(" select * from ");
		sql.append(" veh_alarmrec ").append(" where 1=1 ").append(sb);
		sql.append(" order by bjsj");
		sql.append(" desc ");

		result = queryforMapnew( String.valueOf(curPage),String.valueOf( pageSize),sql.toString(), vl);//this.findPageForMap(sql.toString(), curPage, pageSize, classz);
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
