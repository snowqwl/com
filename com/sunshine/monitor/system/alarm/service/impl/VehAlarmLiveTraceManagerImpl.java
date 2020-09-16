package com.sunshine.monitor.system.alarm.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.activemq.bean.TransLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.dao.VehAlarmDao;
import com.sunshine.monitor.system.alarm.service.VehAlarmLiveTraceManager;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.dao.DepartmentDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Transactional
@Service("vehAlarmLiveTraceManager")
public class VehAlarmLiveTraceManagerImpl implements VehAlarmLiveTraceManager{

	@Autowired
	@Qualifier("vehAlarmDao")
	private VehAlarmDao vehAlarmDao;

	@Autowired
	@Qualifier("departmentDao")
	private DepartmentDao departmentDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@OperationAnnotation(type=OperationType.CALARM_LIVE_QUERY,description="出警跟踪查询")
	public Map getAlarmLiveTraceForMap(Map filter, VehAlarmCmd info,String gzbj)
			throws Exception {
		Map map = vehAlarmDao.getAlarmLiveTraceForMap(filter, info,gzbj);
		List<Map<String,Object>> list =(List<Map<String,Object>>)map.get("rows");
        for(int i = 0 ;i<list.size();i++){
        	Map<String,Object> rec = list.get(i);
        	CodeGate gate = this.gateManager.getGate(rec.get("KDBH").toString());
        	if(gate!=null){
            	rec.put("KKZT",gate.getKkzt());
        	}else{
        		rec.put("KKZT","");
        	}
        }
        map.put("rows",list);
		return map;
	}
	/**
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List getVehAlarmCmdListForBjxh(String bjxh) throws Exception {
		
		List list = vehAlarmDao.getCmdscopeListForBjxh(bjxh);
		
		for(Object o : list){
			VehAlarmCmd cmd = (VehAlarmCmd)o;
			String zljsdw = cmd.getZljsdw();
			Department dp = departmentDao.getDepartment(zljsdw);
			if(dp != null) cmd.setZljsdwmc(dp.getBmmc());
			
			cmd.setSffkmc( systemDao.getCodeValue("130006", cmd.getSffk()));
		}
		return list;
	}
	/**
	 * （根据参数）保存veh_alarm_livetrace表
	 * @param liveTrace(VehAlarmLivetrace)
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type=OperationType.CALARM_LIVE,description="出警跟踪")
	public Map saveLiveTrace(VehAlarmLivetrace liveTrace) throws Exception {
		
		try{
			int i = vehAlarmDao.saveLiveTrace(liveTrace);
			
			return Common.messageBox("保存成功！", "1");
		}catch(Exception e){
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}
		
	}
	/**
	 * （根据参数）保存veh_alarm_livetrace表
	 * @param liveTrace(TransLivetrace)
	 * @return
	 * @throws Exception
	 */
	public Map saveLiveTrace(TransLivetrace liveTrace) throws Exception {
		
		try{
			liveTrace.setXxly("2");
			int i = vehAlarmDao.saveLiveTrace(liveTrace);
			
			return Common.messageBox("保存成功！", "1");
		}catch(Exception e){
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}	
		
	}
	/**
	 * （根据参数）联合查询veh_alarmrec表、veh_alarm_handled
	 * @param filter、info
	 * @return
	 * @throws Exception
	 */
	public Map getAlarmLJForMap(Map filter, VehAlarmCmd info) throws Exception {
		
		return vehAlarmDao.getAlarmLJForMap(filter, info);
	}
	/**
	 * （根据参数）联合查询veh_alarmrec表、veh_alarm_handled
	 * @param info
	 * @param cityname
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map getAlarmSTLJForMap(Map filter, VehAlarmCmd info, String cityname)
			throws Exception {
		
		return vehAlarmDao.getAlarmSTLJForMap(filter, info,cityname);
	}
	
	
}
