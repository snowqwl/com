package com.sunshine.monitor.system.analysis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.system.analysis.action.NightCarController.StartEndDate;
import com.sunshine.monitor.system.analysis.bean.NightCarTemp;
import com.sunshine.monitor.system.analysis.bean.NightCarTotal;

public interface NightCarService {
	public Boolean computeNightCar(
			List<StartEndDate> searchDateList,String dayStart, String dayEnd,
			String[] gateIds,String groupId, String sessionId) 
					throws Exception;
	public String computeGroupId(String[] gateId, String dayStart, String dayEnd);
	
	public void createTempTable(String sessionId);
	
	public List<NightCarTemp> nightCarTempList(String sessionId,String groupId, Cnd cnd, Pager pager) throws Exception ;
	
	public List<NightCarTotal> nightCarTempTotalList(String sessionId,
			String groupId, String startTime, String endTime, Pager pager,  Integer thresholds, String kdbhs)  
					throws Exception;
	
	public Map<String, List<Date>> getAllGroupIdCarDate() throws Exception;
	
	public List<NightCarTotal> nightCarTempTotalList(String sessionId, 
			String groupId, Date[] times, Pager pager, Integer thresholds);
	
}
