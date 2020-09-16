package com.sunshine.monitor.system.susp.service;

import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.exception.SuspDaoAccessException;
import com.sunshine.monitor.system.susp.exception.SuspinfoValidateException;

public interface ExternalSuspinfoManager {
	/**
	 * 布控
	 */
	public void suspinfo(VehSuspinfo suspinfo, AuditApprove audit)
			throws SuspinfoValidateException, SuspDaoAccessException;
	
	/**
	 * 撤控
	 */
	public void suspinfoCancle(VehSuspinfo suspinfo, AuditApprove audit)
			throws SuspinfoValidateException, SuspDaoAccessException;
	
}
