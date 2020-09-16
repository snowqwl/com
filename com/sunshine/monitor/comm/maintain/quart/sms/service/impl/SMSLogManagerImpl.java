package com.sunshine.monitor.comm.maintain.quart.sms.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.maintain.quart.sms.bean.SMSLog;
import com.sunshine.monitor.comm.maintain.quart.sms.dao.SMSLogDao;
import com.sunshine.monitor.comm.maintain.quart.sms.service.SMSLogManager;

@Service("smsLogManager")
public class SMSLogManagerImpl implements SMSLogManager{

	@Autowired
	@Qualifier("SMSLogDao")
	private SMSLogDao smsLogDao;
	
	public int addSMSLog(SMSLog smsLog) throws Exception {
		
		return this.smsLogDao.addSMSLog(smsLog);
	}

	public Map<String, Object> querySmslogListByPage(
			Map<String, Object> condition) throws Exception {
		
		return this.smsLogDao.querySmslogListByPage(condition);
	}
	
}
