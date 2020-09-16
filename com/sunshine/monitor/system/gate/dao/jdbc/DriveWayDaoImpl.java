package com.sunshine.monitor.system.gate.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.gate.dao.DriveWayDao;

@Repository("driveWayDao")
public class DriveWayDaoImpl  extends BaseDaoImpl implements  DriveWayDao {
	@Override
	public String getTableName() {
		return "code_gate_cd";
	}
}
