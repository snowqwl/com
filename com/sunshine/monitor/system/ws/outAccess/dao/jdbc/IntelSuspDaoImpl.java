package com.sunshine.monitor.system.ws.outAccess.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.DBaseDaoImpl;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelSuspBean;
import com.sunshine.monitor.system.ws.outAccess.dao.IntelSuspDao;

@Repository("intelSuspDao")
public class IntelSuspDaoImpl extends DBaseDaoImpl<IntelSuspBean> implements IntelSuspDao {

	@Override
	public String getTableName() {
		return "Intel_Suspinfo";
	}
}
