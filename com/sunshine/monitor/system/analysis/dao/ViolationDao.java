package com.sunshine.monitor.system.analysis.dao;

import java.util.List;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface ViolationDao extends BaseDao {
	
	public List getViolationList(String wflx,String hphm,String hpzl);

}
