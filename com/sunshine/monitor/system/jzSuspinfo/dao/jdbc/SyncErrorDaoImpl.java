package com.sunshine.monitor.system.jzSuspinfo.dao.jdbc;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.DBaseDaoImpl;
import com.sunshine.monitor.system.jzSuspinfo.bean.SyncError;
import com.sunshine.monitor.system.jzSuspinfo.dao.SyncErrorDao;

@Repository("syncErrorDao")
public class SyncErrorDaoImpl extends DBaseDaoImpl<SyncError> implements SyncErrorDao {

	@Override
	public String getTableName() {
		return "SYNCHRONIZATION_ERROR";
	}

}
