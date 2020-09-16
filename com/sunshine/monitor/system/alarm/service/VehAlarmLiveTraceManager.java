package com.sunshine.monitor.system.alarm.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.activemq.bean.TransLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;

public interface VehAlarmLiveTraceManager {

	public Map getAlarmLiveTraceForMap(Map fillter,VehAlarmCmd info, String gzbj) throws Exception;
	/**
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List  getVehAlarmCmdListForBjxh(String bjxh)throws Exception;
	/**
	 * （根据参数）保存veh_alarm_livetrace表
	 * @param liveTrace(VehAlarmLivetrace)
	 * @return
	 * @throws Exception
	 */
	public Map saveLiveTrace(VehAlarmLivetrace liveTrace)throws Exception;
	/**
	 * （根据参数）保存veh_alarm_livetrace表
	 * @param liveTrace(TransLivetrace)
	 * @return
	 * @throws Exception
	 */
	public Map saveLiveTrace(TransLivetrace liveTrace)throws Exception;
	/**
	 * （根据参数）联合查询veh_alarmrec表、veh_alarm_handled
	 * @param filter、info
	 * @return
	 * @throws Exception
	 */
	public Map getAlarmLJForMap(Map filter, VehAlarmCmd info)throws Exception;
	/**
	 * （根据参数）联合查询veh_alarmrec表、veh_alarm_handled
	 * @param filter、info、cityname
	 * @return
	 * @throws Exception
	 */
	public Map getAlarmSTLJForMap(Map filter, VehAlarmCmd info, String cityname)throws Exception;
	
}
