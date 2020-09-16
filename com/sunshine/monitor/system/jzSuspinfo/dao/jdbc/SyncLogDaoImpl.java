package com.sunshine.monitor.system.jzSuspinfo.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.DBaseDaoImpl;
import com.sunshine.monitor.system.jzSuspinfo.bean.SyncLog;
import com.sunshine.monitor.system.jzSuspinfo.dao.SyncLogDao;

@Repository("syncLogDao")
public class SyncLogDaoImpl extends DBaseDaoImpl<SyncLog> implements SyncLogDao {

	@Override
	public String getTableName() {
		return "SYNCHRONIZATION_LOG";
	}

}
