package com.sunshine.monitor.system.query.service;

import java.util.Map;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.system.query.bean.TrafficPolicePassrec;

public interface TrafficPoliceQueryManager {

	public Map<String,Object> getPassrecList(TrafficPolicePassrec veh,Page page)throws Exception;

}
