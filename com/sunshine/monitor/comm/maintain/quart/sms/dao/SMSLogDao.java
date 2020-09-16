package com.sunshine.monitor.comm.maintain.quart.sms.dao;

import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.comm.maintain.quart.sms.bean.SMSLog;

public interface SMSLogDao extends BaseDao {

	public int addSMSLog(SMSLog smsLog) throws Exception;

	public Map<String, Object> querySmslogListByPage(
			Map<String, Object> condition) throws Exception;

}
