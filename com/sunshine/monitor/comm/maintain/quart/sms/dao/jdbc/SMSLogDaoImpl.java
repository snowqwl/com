package com.sunshine.monitor.comm.maintain.quart.sms.dao.jdbc;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.maintain.quart.sms.bean.SMSLog;
import com.sunshine.monitor.comm.maintain.quart.sms.dao.SMSLogDao;

@Repository("SMSLogDao")
public class SMSLogDaoImpl extends BaseDaoImpl implements SMSLogDao {

	public int addSMSLog(SMSLog smsLog) throws Exception {
		String sql = "insert into JM_SMS_LOG(xh,glbm,kdmc,wtms,fshm,jshm,fssj,fszt)values(SEQ_SMSLOG_XH.NEXTVAL,'"
				+ ((smsLog.getGlbm() == null) ? "" : smsLog.getGlbm())
				+ "','"
				+ smsLog.getKdmc()
				+ "','"
				+ smsLog.getWtms()
				+ "','"
				+ smsLog.getFshm()
				+ "','"
				+ smsLog.getJshm()
				+ "',sysdate,'"
				+ smsLog.getFszt() + "')";
		return this.jdbcTemplate.update(sql);
	}

	public Map<String, Object> querySmslogListByPage(
			Map<String, Object> condition) throws Exception {
		StringBuffer sql = new StringBuffer(
				"SELECT xh, glbm, kdmc, wtms, fshm, jshm, fssj, fszt FROM JM_SMS_LOG WHERE 1=1 ");
		
		String kssj = (String)condition.get("kssj");
		String jssj = (String)condition.get("jssj");
		if(kssj != null && !"".equals(kssj)){
			sql.append("and fssj >= to_date('" + kssj + "','yyyy-mm-dd hh24:mi:ss')");
		}
		if(jssj != null && !"".equals(jssj)){
			sql.append("and fssj <= to_date('" + jssj + "','yyyy-mm-dd hh24:mi:ss')");	
		}
		sql.append(" order by ");
		sql.append(condition.get("sort"));
		sql.append(" ");
		sql.append(condition.get("order"));
		return findPageForMap(sql.toString(),
				Integer.parseInt(condition.get("page").toString()),
				Integer.parseInt(condition.get("rows").toString()));
	}

}
