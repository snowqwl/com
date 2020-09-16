package com.sunshine.monitor.system.alarm.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
/**
 * 
 * @author OUYANG 2013/7/2
 *
 */
public interface RealalarmDao extends BaseDao {
	
	public Map<String,Object> findPageAlarmForMap(Map<String,Object> map,Class<?> classz) throws Exception;
	/**
	 * (VEH_REALALARM表，veh_suspinfo表)
	 * @param bjsj 报警时间(字符串)
	 * @return 
	 * @throws Exception
	 */
	public List<VehAlarmrec> getAlarmForGIS(String bjsj) throws Exception;
	/**
	 * 根据conditions查询(VEH_REALALARM表)
	 * @param condition(字符串)
	 * @return 
	 * @throws Exception
	 */
	public List<VehAlarmrec> getAlarmForTrace(String condition) throws Exception;
	/**
	 * 查询多卡口实时报警信息
	 * @param kds 卡点编号(数组)
	 * @param bklbs 布控类别(数组)
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getVehRealalarmM(String[] kds, String[] bklbs)throws Exception;

	/**
	 * 查询某卡口的某种类别的实时报警信息
	 * @param kdbh 卡点编号
	 * @param bklb 布控类别(00,01,...)
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getVehAlarmDetail(String kdbh, String bklb)throws Exception;

	/**
	 * 查询多卡口实时报警信息
	 * @param kds 卡点编号(数组)
	 * @param bklb 布控类别(数组)
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmrec> getVehAlarmList(String[] kds, String[] bklb)throws Exception;
	/**
	 * 多卡口实时报警详细查询(VEH_ALARMREC)
	 * @param bjxh
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getVehAlarmDetail(String bjxh)throws Exception;
	/**
	 * 根据报警信息查询布控信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List<VehSuspinfo> getAlarmSuspList(String bjxh)throws Exception;
	
	/**
	 * 查询某卡口今是报警数量
	 * @param kdbh
	 * @return
	 * @throws Exception
	 */
	public long getRealAlarmByKdbh(String kdbh) throws Exception;
}