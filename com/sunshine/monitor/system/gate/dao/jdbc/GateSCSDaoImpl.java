package com.sunshine.monitor.system.gate.dao.jdbc;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.dao.GateSCSDao;
import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;

@Repository("gateSCSDao")
public class GateSCSDaoImpl extends ScsBaseDaoImpl implements GateSCSDao {

	public String getTableName() {
		return "code_gate";
	}

	public List<CodeGate> getGatesForMap(CodeGate d, String gabh) throws Exception {
		// TODO Auto-generated method stub		
		StringBuffer tmpSql = new StringBuffer(
				" SELECT t.kdbh,t.kdmc,t.kkjd,t.kkwd,t.kkzt,t.kkdz,t.gabh,t.lxdh,t.gxsj FROM t(CODE_GATE::(1=1) ");
		if ((d.getKkzt() != null) && (d.getKkzt().length() > 0)) {
			tmpSql.append(" AND (KKZT = '").append(d.getKkzt()).append("')");
		}
		if ((d.getKdbh() != null) && (d.getKdbh().length() > 0)) {
			tmpSql.append(" AND (KDBH = '").append(d.getKdbh()).append("')");
		}
		if ((d.getKdmc() != null) && (d.getKdmc().length() > 0)) {
			tmpSql.append(" AND (KDMC LIKE '%").append(d.getKdmc()).append("%')");
		}
		if ((d.getKklx() != null) && (d.getKklx().length() > 0)) {
			tmpSql.append(" AND (KKLX = '").append(d.getKklx()).append("')");
		}
		if ((d.getGabh() != null) && (d.getGabh().length() > 0)) {
			tmpSql.append(" AND (GABH LIKE '%").append(d.getGabh()).append("%')");
		}
		if ((d.getDwdm() != null) && (d.getDwdm().length() > 0)) {
			tmpSql.append(" AND (DWDM = '").append(d.getDwdm()).append("')");
		}
		
		//天云星下没有部门辖区表
		/*if ((gabh != null) && (gabh.length() > 1)) {
			tmpSql.append(" AND DWDM IN (").append(
					"select xjjg from frm_prefecture where dwdm = '" + gabh
							+ "'").append(")");
		}*/
		tmpSql.append(")");
		return this.queryForList(tmpSql.toString(),CodeGate.class);
	}

	public List<CodeGateExtend> getGatesExtend(String KDBHs) throws Exception{
		// TODO Auto-generated method stub
		StringBuffer tmpSql = new StringBuffer(
				" SELECT t.fxmc,t.sbzt FROM t(CODE_GATE_EXTEND ::(1=1) ");
		tmpSql.append("and (kdbh in ( " + KDBHs+"))");
		tmpSql.append(" )");
		//return this.jdbcScsTemplate.queryForList(tmpSql.toString(), CodeGateExtend.class);
		return this.queryForList(tmpSql.toString(), CodeGateExtend.class);
	}
}