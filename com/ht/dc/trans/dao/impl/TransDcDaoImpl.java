package com.ht.dc.trans.dao.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ht.dc.trans.bean.TransAlarm;
import com.ht.dc.trans.bean.TransAlarmHandled;
import com.ht.dc.trans.bean.TransAuditApprove;
import com.ht.dc.trans.bean.TransCmd;
import com.ht.dc.trans.bean.TransLivetrace;
import com.ht.dc.trans.bean.TransObj;
import com.ht.dc.trans.bean.TransSusp;
import com.ht.dc.trans.dao.TransDcDao;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;

@Repository("transDcDao")
public class TransDcDaoImpl extends BaseDaoImpl implements TransDcDao {

	public int saveSuspInfo(TransSusp bean) throws Exception {
		String sql = "";
		List vl = new ArrayList<>();
		String s_susp = "SELECT count(1) v_count from veh_suspinfo where bkxh = ?";
		int _susp = this.jdbcTemplate.queryForObject(s_susp, Integer.class, new Object[] { bean.getBkxh() });
		if (_susp > 0) {
			if (bean.getJlzt().equals("1")) {// 记录状态为"布控"
				vl.add(bean.getBksj());
				vl.add(bean.getBkjzsj());
				vl.add(bean.getBkxh());
				sql = "UPDATE VEH_SUSPINFO SET BKSJ=to_date(?,'YYYY-MM-DD HH24:MI:SS'),"
						+ " BKJZSJ=to_date(?,'YYYY-MM-DD HH24:MI:SS')," + " gxsj=sysdate" + " where bkxh=?";
			}
		} else {
			vl.add(bean.getBkxh());
			vl.add(bean.getBkdl() == null ? " " : bean.getBkdl());
			vl.add(bean.getBklb() == null ? " " : bean.getBklb());
			vl.add(bean.getHphm() == null ? " " : bean.getHphm());
			vl.add(bean.getHpzl() == null ? " " : bean.getHpzl());
			vl.add(bean.getClxh() == null ? " " : bean.getClxh());
			vl.add(bean.getFdjh() == null ? " " : bean.getFdjh());
			vl.add(bean.getBkjg() == null ? " " : bean.getBkjg());
			vl.add(bean.getBkjgmc() == null ? " " : bean.getBkjgmc());
			vl.add(bean.getClsyr() == null ? " " : bean.getClsyr());
			vl.add(bean.getCllx() == null ? " " : bean.getCllx());
			vl.add(bean.getCltz() == null ? " " : bean.getBkjg());
			vl.add(bean.getClsbdh() == null ? " " : bean.getClsbdh());
			vl.add(bean.getSyrlxdh() == null ? " " : bean.getSyrlxdh());
			vl.add(bean.getSyrxxdz() == null ? " " : bean.getSyrxxdz());
			vl.add(bean.getBkqssj() == null ? " " : bean.getBkqssj());
			vl.add(bean.getBkjzsj() == null ? " " : bean.getBkjzsj());
			vl.add(bean.getBkr() == null ? " " : bean.getBkr());
			vl.add(bean.getBkrmc() == null ? " " : bean.getBkrmc());
			vl.add(bean.getBkrjh() == null ? " " : bean.getBkrjh());
			vl.add(bean.getBkjglxdh() == null ? " " : bean.getBkjglxdh());
			vl.add(bean.getBkfw() == null ? " " : bean.getBkfw());
			vl.add(bean.getBkfwlx() == null ? " " : bean.getBkfwlx());
			vl.add(bean.getMhbkbj() == null ? " " : bean.getMhbkbj());
			vl.add(bean.getClpp() == null ? " " : bean.getClpp());
			vl.add(bean.getCsys() == null ? " " : bean.getCsys());
			vl.add(bean.getBjfs() == null ? " " : bean.getBjfs());
			vl.add(bean.getBkjb() == null ? " " : bean.getBkjb());
			vl.add(bean.getDxjshm() == null ? " " : bean.getDxjshm());
			vl.add(bean.getBkxz() == null ? " " : bean.getBkxz());
			vl.add(bean.getBjya() == null ? " " : bean.getBjya());
			vl.add(bean.getHpys() == null ? " " : bean.getHpys());
			vl.add(bean.getBkpt() == null ? " " : bean.getBkpt());
			vl.add(bean.getSqsb() == null ? " " : bean.getSqsb());
			vl.add(bean.getLadw() == null ? " " : bean.getLadw());
			vl.add(bean.getLadwlxdh() == null ? " " : bean.getLadwlxdh());
			vl.add(bean.getLar() == null ? " " : bean.getLar());
			vl.add(bean.getYwzt() == null ? " " : bean.getYwzt());
			vl.add(bean.getJlzt() == null ? " " : bean.getJlzt());
			vl.add(bean.getYsbh() == null ? " " : bean.getYsbh());
			sql = "INSERT into veh_suspinfo(bkxh, bkdl, bklb, hphm, hpzl, clxh, fdjh, bkjg, "
					+ "bkjgmc, clsyr, cllx, cltz, clsbdh, syrlxdh, syrxxdz, bkqssj, bkjzsj, bkr, bkrmc, "
					+ "bkrjh, bkjglxdh, bkfw, bkfwlx, jyaq, mhbkbj, bksj, gxsj, clpp, csys, bjfs, bkjb, dxjshm,"
					+ "bkxz, bjya, hpys, bkpt, sqsb, ladw, ladwlxdh, lar, ywzt, xxly, jlzt, ysbh) "
					+ "Values(?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?," + " to_date(?,'YYYY-MM-DD HH24:MI:SS'), "
					+ "to_date(?,'YYYY-MM-DD HH24:MI:SS'), ?, ?,?,?,?,?,?,?, sysdate,sysdate, ?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,'2',?,?)";

		}
		if (sql != null && sql.length() > 0) {
			return this.jdbcTemplate.update(sql, vl.toArray());
		} else {
			return 0;
		}
	}

	public int saveSuspinfoApprove(TransAuditApprove info) throws Exception {
		StringBuffer sb = null;
		int count = 0;
		if (info != null) {
			List vl = new ArrayList<>();
//			vl.add(info.getCzrdw());
			vl.add(info.getBkxh());
			vl.add(info.getCzr());
			vl.add(info.getCzrdw());
			vl.add(info.getCzrdwmc());
			vl.add(info.getCzjg());
			vl.add(info.getMs());
			vl.add(info.getBzw());
			vl.add(info.getCzrjh());
			vl.add(info.getCzrmc());
			sb = new StringBuffer(
					"Insert into AUDIT_APPROVE(xh,bkxh,czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,czrjh,czrmc,by1,by2) values( ");

			sb.append(info.getCzrdw()).append(" || seq_audit_xh.nextval,");
			sb.append("?,?,?,?,sysdate,?,?,?,?,?");

			if (info.getBy1() != null && !info.getBy1().equals("")) {
				vl.add(info.getBy1());
				sb.append(",?");
			}
			if (info.getBy2() != null && !info.getBy2().equals("")) {
				vl.add(info.getBy2());
				sb.append(",?");
			}
			sb.append(" )");
			count = this.jdbcTemplate.update(sb.toString(), vl.toArray());
		}
		return count;
	}

	// 更新布控表(针对撤控申请)
	public int updateSuspInfoForCancel(TransSusp info) throws Exception {
		List vl = new ArrayList<>();
		StringBuffer sqlCancel = new StringBuffer("UPDATE veh_suspinfo  set ywzt = '41' ,gxsj = sysdate ");
		if (StringUtils.isNotBlank(info.getCkyydm())) {
			sqlCancel.append(",ckyydm = ? ");
			vl.add(info.getCkyydm());
		}

		if (StringUtils.isNotBlank(info.getCkyyms())) {
			sqlCancel.append(",ckyyms = ? ");
			vl.add(info.getCkyyms());
		}

		if (StringUtils.isNotBlank(info.getCxsqr())) {
			sqlCancel.append(", cxsqr = ? ");
			vl.add(info.getCxsqr());
		}

		if (StringUtils.isNotBlank(info.getCxsqrjh())) {
			sqlCancel.append(",cxsqrjh =? ");
			vl.add(info.getCxsqr());
		}

		if (StringUtils.isNotBlank(info.getCxsqrmc())) {
			sqlCancel.append(",cxsqrmc = ?");
			vl.add(info.getCxsqrmc());
		}

		if (StringUtils.isNotBlank(info.getCxsqdw())) {
			sqlCancel.append(", cxsqdw = ? ");
			vl.add(info.getCxsqdw());
		}
		if (StringUtils.isNotBlank(info.getCxsqdwmc())) {
			sqlCancel.append(", cxsqdwmc = ? ");
			vl.add(info.getCxsqdwmc());
		}
		if (StringUtils.isBlank(info.getBy1())) {
			sqlCancel.append(", by1 = to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')");
		}
		sqlCancel.append(", cxsqsj = sysdate  where bkxh = ? ");
		vl.add(info.getBkxh());

		int i = this.jdbcTemplate.update(sqlCancel.toString(), vl.toArray());

		return i;
	}

	public int saveAlarmLink(TransAlarm bean) throws Exception {
		String alarm_sql = "SELECT count(1) FROM VEH_ALARMREC WHERE BJXH = ?";
		int count = this.jdbcTemplate.queryForInt(alarm_sql, new String[] { bean.getBjxh() });
		String i_sql = "";
		List vl = new ArrayList<>();
		if (count == 0) {
			vl.add(bean.getBjxh());
			vl.add(bean.getBjdl() == null ? "1" : bean.getBjdl());
			vl.add(bean.getBjlx() == null ? "00" : bean.getBjlx());
			vl.add(bean.getBkxh() == null ? " " : bean.getBkxh());
			vl.add(bean.getBjsj());
			vl.add(bean.getBjdwdm() == null ? " " : bean.getBjdwdm());
			vl.add(bean.getBjdwmc() == null ? " " : bean.getBjdwmc());
			vl.add(bean.getBjdwlxdh() == null ? " " : bean.getBjdwlxdh());
			vl.add(bean.getHphm() == null ? " " : bean.getHphm());
			vl.add(bean.getHpzl() == null ? " " : bean.getHpzl());
			vl.add(bean.getGcxh() == null ? " " : bean.getGcxh());
			vl.add(bean.getGcsj() == null ? " " : bean.getGcsj());
			vl.add(bean.getSbbh() == null ? " " : bean.getSbbh());
			vl.add(bean.getSbmc() == null ? " " : bean.getSbmc());
			vl.add(bean.getKdbh() == null ? " " : bean.getKdbh());
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
			vl.add(bean.getClwx() == null ? " " : bean.getClwx());
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
			vl.add(bean.getSffk() == null ? "0" : bean.getSffk());
			vl.add(bean.getSflj() == null ? "0" : bean.getSflj());
			vl.add(bean.getQrrmc() == null ? " " : bean.getQrrmc());
			i_sql = " INSERT INTO VEH_ALARMREC (BJXH,BJDL,BJLX,BKXH,BJSJ,BJDWDM,BJDWMC,BJDWLXDH,HPHM,HPZL,GCXH,GCSJ,"
					+ "SBBH,SBMC,KDBH,KDMC,FXBH,FXMC,CLLX,CLSD,HPYS,CWHPHM,CWHPYS,HPYZ,CDBH,CLWX,CSYS,"
					+ "TP1,TP2,TP3,QRR,QRRJH,QRDWDM,QRDWDMMC,QRDWLXDH,QRSJ,QRZT,QRJG,GXSJ,JYLJTJ,XXLY,"
					+ "BY1,BY2,BY3,BY4,BY5,SFXDZL,SFFK,SFLJ,QRRMC) " + " Values (?,?,?,?,"
					+ " to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?,"
					+ " to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?, ?,?,?,sysdate,?,?,"
					+ " to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?," + "?,?,?,?,?,?,?)";
		} else {
			// 预警签收确认
			vl.add(bean.getQrr() == null ? " " : bean.getQrr());
			vl.add(bean.getQrrmc() == null ? " " : bean.getQrrmc());
			vl.add(bean.getQrrjh() == null ? " " : bean.getQrrjh());
			vl.add(bean.getQrdwdm() == null ? " " : bean.getQrdwdm());
			vl.add(bean.getQrdwdmmc() == null ? " " : bean.getQrdwdmmc());
			vl.add(bean.getQrdwlxdh() == null ? " " : bean.getQrdwlxdh());
			vl.add(bean.getQrsj() == null ? " " : bean.getQrsj());
			vl.add(bean.getQrzt() == null ? " " : bean.getQrzt());
			vl.add(bean.getQrjg() == null ? " " : bean.getQrjg());
			vl.add(bean.getJyljtj() == null ? " " : bean.getJyljtj());
			vl.add(bean.getSfxdzl() == null ? " " : bean.getSfxdzl());
			vl.add(bean.getSffk() == null ? "0" : bean.getSffk());
			vl.add(bean.getSflj() == null ? "0" : bean.getSflj());
		
			vl.add(bean.getBjxh());
			i_sql = " UPDATE VEH_ALARMREC SET  QRR=?,QRRMC=?,QRRJH=?,QRDWDM=?,QRDWDMMC=?,QRDWLXDH=?,QRSJ=to_date(?,'yyyy-mm-dd hh24:mi:ss'),QRZT=?,QRJG=?,GXSJ=SYSDATE,JYLJTJ=?,SFXDZL=?,SFFK=?,SFLJ=? WHERE BJXH=?";
		}
		return this.jdbcTemplate.update(i_sql,vl.toArray());
	}

	public int saveAlarmHandleLink(TransAlarmHandled handle) throws Exception {
		String alarm_sql = "SELECT count(*) FROM VEH_ALARM_HANDLED WHERE FKBH = ?";
		List vl = new ArrayList<>();
		int count = this.jdbcTemplate.queryForInt(alarm_sql,new String []{handle.getFkbh()});
		if (count == 0) {
			StringBuffer sb = new StringBuffer(
					"INSERT into veh_alarm_handled (FKBH, BKXH, BJXH, ZLXH, GCXH, LRR, LRRJH, LRRMC,LRDWDM, LRDWMC,");
			sb.append("  LRSJ, DDR, DDRMC, XBR, XBRMC, SFLJ,WLJDYY, CLGC, BJSJ, BJDWDM, BJDWMC, BJDWLXDH, XXLY) ");
			sb.append(" values( ");
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
sb.append("?,?,?,?,?,?,?,?,?,?,sysdate,");
vl.add(handle.getDdr());
vl.add(handle.getDdrmc());
vl.add(handle.getXbr());
vl.add(handle.getXbrmc());
vl.add(handle.getSflj());
vl.add(handle.getWljdyy());
vl.add(handle.getClgc());
sb.append("?,?,?,?,?,?,?,");
			if (StringUtils.isNotBlank(handle.getBjsj()) && handle.getBjsj().length() > 10)
				sb.append("to_date('").append(handle.getBjsj()).append("','yyyy-mm-dd hh24:mi:ss')");
			
			else{
				sb.append("to_date('").append(handle.getBjsj()).append("','yyyy-mm-dd')");
				
			}
			vl.add(handle.getBjsj());
			vl.add(handle.getBjdwdm());
			vl.add(handle.getBjdwmc());
			vl.add(handle.getBjdwlxdh());
			vl.add(handle.getXxly());
			sb.append("?,?,?,?,? )");
			return this.jdbcTemplate.update(sb.toString(),vl.toArray());
		} else {
			// 指挥中心签收反馈
			vl.add(handle.getBy1() == null ? " " : handle.getBy1());
			vl.add(handle.getBy2() == null ? " " : handle.getBy2());
			vl.add(handle.getBy3() == null ? " " : handle.getBy3());
			vl.add(handle.getBy4() == null ? " " : handle.getBy4());
			vl.add(handle.getBy5() == null ? " " : handle.getBy5());
			vl.add(handle.getFkbh());
			String sql = "UPDATE veh_alarm_handled set by1=?,by2=?, by3=?, by4=?, by5=?  WHERE FKBH = ?";
			return this.jdbcTemplate.update(sql,vl.toArray());
		}
	}

	public String saveCmd(TransCmd vehAlarmCmd) throws Exception {
		String v_zlxh = "";
		if (vehAlarmCmd.getZlxh() != null) {
			// 联动过来的
			v_zlxh = vehAlarmCmd.getZlxh();
			// 已存在该指令则先删除
			String susp_sql = "SELECT count(*) FROM veh_alarm_cmd WHERE ZLXH = ?";
			int count = this.jdbcTemplate.queryForInt(susp_sql,new String []{vehAlarmCmd.getZlxh() });
			if (count > 0) {
				susp_sql = "delete from veh_alarm_cmd where ZLXH=?";
				this.jdbcTemplate.update(susp_sql,new String []{vehAlarmCmd.getZlxh() });
			}
		} else {
			String zldw = vehAlarmCmd.getZldw();
			// 获取序号
			String seq_sql = "SELECT " + zldw + " || SEQ_ALARM_CMD_ZLXH.nextval from dual";
			v_zlxh = this.jdbcTemplate.queryForObject(seq_sql, String.class);
		}
		List vl=new ArrayList<>();
		vl.add(v_zlxh);
		vl.add(vehAlarmCmd.getBjxh());
		vl.add(vehAlarmCmd.getZlfs());
		vl.add(vehAlarmCmd.getZlr());
		vl.add(vehAlarmCmd.getZlrjh());
		vl.add(vehAlarmCmd.getZlrmc());
		vl.add(vehAlarmCmd.getZldw());
		vl.add(vehAlarmCmd.getZldwmc());
		vl.add(vehAlarmCmd.getZldwlxdh());//sys
		vl.add(vehAlarmCmd.getZlnr());
		vl.add(vehAlarmCmd.getBy1());
		vl.add(vehAlarmCmd.getBy2());
		vl.add(vehAlarmCmd.getXxly());
		String i_sql = "INSERT into veh_alarm_cmd(ZLXH, BJXH, ZLFS,ZLR, ZLRJH,ZLRMC, ZLDW, ZLDWMC, "
				+ "zldwlxdh, ZLSJ, ZLNR, BY1, BY2, XXLY) values(?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?)";
		int count = this.jdbcTemplate.update(i_sql,vl.toArray());
		if (count == 1) {
			// 保存成功，返回指令序号
			return v_zlxh;
		}
		return null;
	}

	public int saveLiveTrace(TransLivetrace liveTrace) throws Exception {
		// 如果存在该出警跟踪记录则先删除
		String susp_sql = "SELECT count(*) FROM veh_alarm_livetrace WHERE XH = ?";
		int count = this.jdbcTemplate.queryForInt(susp_sql,new String[]{liveTrace.getXh()});
		if (count > 0) {
			susp_sql = "delete from veh_alarm_livetrace where XH=?";
			this.jdbcTemplate.update(susp_sql,new String[]{liveTrace.getXh()});
		}
List vl=new ArrayList<>();
		StringBuffer sb = new StringBuffer(" INSERT into veh_alarm_livetrace");
		sb.append(
				"(XH, ZLXH,  BJXH, BGDW, BGDWMC, BGR, BGRJH, LRR, LRRJH, LRRMC, LRDWDM,LRDWMC, LRSJ, NR, XXLY)  values(");
		sb.append("?,?,?,?,?,");
		vl.add(liveTrace.getXh());
		vl.add(liveTrace.getBjxh());
		vl.add(liveTrace.getBgdw());
		vl.add(liveTrace.getBgdwmc());
		vl.add(liveTrace.getBgr());
		if (StringUtils.isNotBlank(liveTrace.getBgrjh())){
			sb.append("?,");
			vl.add(liveTrace.getBgrjh());
		}
		else{
			sb.append(" '', ");
		}
		vl.add(liveTrace.getLrr());
		vl.add(liveTrace.getLrrjh());
		vl.add(liveTrace.getLrrmc());
		vl.add(liveTrace.getLrdwdm());
		vl.add(liveTrace.getLrdwmc());
		vl.add(liveTrace.getNr());
		vl.add(liveTrace.getXxly());
		sb.append("?,?,?,?,? ,?,?)");
		return this.jdbcTemplate.update(sb.toString(),vl.toArray());
	}

	// 保存撤控记录
	public int insertAuditApprove(TransAuditApprove info) throws Exception {
		StringBuffer sb = new StringBuffer("INSERT into audit_approve ");
List vl=new ArrayList<>();
vl.add(info.getBkxh());
vl.add(info.getCzr());
vl.add(info.getCzrdw());
vl.add(info.getCzrdwmc());//sysdate
vl.add(info.getCzjg());
vl.add(info.getMs());
vl.add(info.getBzw());
vl.add(info.getCzrjh());
vl.add(info.getCzrmc());
		sb.append("(xh, bkxh, czr,czrdw,czrdwmc,czsj,czjg,ms,bzw,czrjh,czrmc) values( '");
		sb.append(info.getCzrdw()).append("' || seq_audit_xh.nextval,'");
			sb.append("?,?,?,?,sysdate,?,? ,?,?,?)");
		int i = this.jdbcTemplate.update(sb.toString(),vl.toArray());
		return i;
	}

	public List getTransList(String tablename, String type, String maxrow) {
		String sql = "select CSXH, CSDW, JSDW, CSBJ, CSSJ, YWXH, TYPE, DDBJ, DDSJ from " + tablename
				+ " where DDBJ=0 and type=? and rownum<?";;
		List list = null;
		List vl=new ArrayList<>();
		vl.add(type);
		vl.add(maxrow);
		list=this.jdbcTemplate.queryForList(sql, vl.toArray(),  TransObj.class);
//		list = this.queryForList(sql, TransObj.class);
		return list;
	}

	public TransSusp getTransSuspDetail(String ywxh, int type) throws Exception {

		// 设置布控信息
		StringBuffer sqlbuf = new StringBuffer("select ");

		sqlbuf.append(" * from ").append("VEH_SUSPINFO");
		sqlbuf.append(" where ").append("BKXH=?");
		TransSusp bean = null;
	
		List vl=new ArrayList<>();
		vl.add(ywxh);
		List<TransSusp> list = this.queryForList(sqlbuf.toString(), vl.toArray(),TransSusp.class);
		if (list != null && list.size() > 0) {
			bean = list.get(0);
		}

		// 设置审核审批信息
		sqlbuf.setLength(0);
		String bzw = "";
		if (11 == type)
			bzw = "'1','2'";
		else if (13 == type)
			bzw = "'3','4'";
		else {
			bzw = "'1','2','3','4'";
		}

		sqlbuf.append("select * from AUDIT_APPROVE where bkxh='").append(ywxh).append("' and bzw in (").append(bzw)
				.append(")");
		List list1 = this.queryForList(sqlbuf.toString(), TransAuditApprove.class);
		if (list1.size() > 0)
			bean.setAuditList(list1);
		return bean;
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
		sqlbuf.append(" from ").append("VEH_ALARMREC");
		sqlbuf.append(" where ").append("BJXH").append("=?");
		List<TransAlarm> list = this.queryForList(sqlbuf.toString(),new String []{ywxh}, TransAlarm.class);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public TransAlarmHandled getTransAlarmHandledDetail(String ywxh) throws Exception {
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
		sqlbuf.append(" where ").append("FKBH").append("=?");
		List<TransAlarmHandled> list = this.queryForList(sqlbuf.toString(),new String []{ywxh}, TransAlarmHandled.class);
		TransAlarmHandled bean = null;
		if (list != null && list.size() > 0) {
			bean = list.get(0);
			List cmdList = this.queryForList("select * from veh_alarm_cmd where bjxh=?",new String []{bean.getBjxh()},
					TransCmd.class);
			bean.setCmdList(cmdList);
			List livetraceList = this.queryForList(
					"select * from veh_alarm_livetrace where bjxh=?",new String []{bean.getBjxh()}, TransLivetrace.class);

			bean.setLivetraceList(livetraceList);
		}
		return bean;
	}

}
