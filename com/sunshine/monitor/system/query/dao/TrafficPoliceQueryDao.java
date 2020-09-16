package com.sunshine.monitor.system.query.dao;

import java.util.Map;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.query.bean.TrafficPolicePassrec;

public interface TrafficPoliceQueryDao extends BaseDao{
	
	public Map<String,Object> getPassrecList(TrafficPolicePassrec veh,Page page)throws Exception;

}
