package com.sunshine.monitor.comm.maintain.quart.sms.service;

import java.util.Map;

import com.sunshine.monitor.comm.maintain.quart.sms.bean.SMSLog;

public interface SMSLogManager {
	
	public int addSMSLog(SMSLog smsLog) throws Exception;
	
	public Map<String, Object> querySmslogListByPage(
			Map<String, Object> condition) throws Exception;
}
