package com.sunshine.monitor.system.jzSuspinfo.dao;

import java.util.Date;
import java.util.List;

import com.sunshine.monitor.comm.dao.DBaseDao;
import com.sunshine.monitor.system.jzSuspinfo.bean.JzVehSuspinfo;

public interface JzVehSuspinfoDao extends DBaseDao<JzVehSuspinfo> {
	public List<JzVehSuspinfo> getJzVehSuspinfoList(Date startDate, Date endDate);
}
