package com.sunshine.monitor.system.ws.outAccess.dao.jdbc;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.DBaseDaoImpl;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelCSuspBean;
import com.sunshine.monitor.system.ws.outAccess.dao.IntelCSuspDao;

@Repository("intelCSuspDao")
public class IntelCSuspDaoImpl extends DBaseDaoImpl<IntelCSuspBean> implements IntelCSuspDao {
	
	@Override
	public String getTableName() {
		return "INTEL_SUSPCANCEL";
	}

}
