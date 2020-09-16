package com.sunshine.monitor.system.alarm.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.activemq.bean.TransAlarmHandled;
import com.sunshine.monitor.system.activemq.bean.TransLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.manager.bean.SysUser;

public interface VehAlarmDao  extends BaseDao{
	/**
	 * 出警反馈信息
	 * @param fillter
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public Map getAlarmHandleForMap(Map filter,VehAlarmCmd info) throws Exception;
	
	public Map getAlarmLiveTraceForMap(Map filter,VehAlarmCmd info, String gzbj) throws Exception;
	/**
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec  getVehAlarmForBjxh(String bjxh)throws Exception;
	/**
	 * （根据指令序号）查询预警命令信息
	 * @param zlxh 指令序号
	 * @return
	 * @throws Exception
	 */
	public VehAlarmCmd getVehAlarmCmdForZlxh(String zlxh)throws Exception;
	
	public String getVehAlarmHandleFkxhForBean(VehAlarmHandled  handle)throws Exception;
	/**
	 * （根据报警序号）查询veh_alarm_livetrace表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List getVehAlarmLivetraceListForBjxh(String bjxh)throws Exception;
	/**
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List getVehAlarmCmdListForBjxh(String bjxh)throws Exception;
	/**
	 * （根据报警序号）查询veh_alarm_cmd表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List getCmdscopeListForBjxh(String bjxh)throws Exception;
	/**
	 * 
	 * 函数功能说明:保存出警反馈
	 * 修改日期 	2013-6-24
	 * @param handle
	 * @param xzqh
	 * @param ssjz
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public int insertAlarmHandle(VehAlarmHandled handle,String xzqh)throws Exception;
	/**
	 * 查询传输出警反馈信息(联动)
	 * @param ywxh  反馈编号
	 * @return
	 * @throws Exception
	 */
	public TransAlarmHandled getAlarmHandledLink(String ywxh) throws Exception;
	/**
	 * 保存出警反馈(联动)
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveAlarmHandleLink(TransAlarmHandled bean) throws Exception; 
	/**
	 * （根据参数）更新veh_alarmrec表
	 * @param sflj
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public int updateAlarmForHandle(String sflj,String bjxh)throws Exception;
	/**
	 * （根据参数）保存business_log表
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public int insertBusinessForHandle(String fkbh,String ywlb,String bkjb,String bzsj,SysUser user,String ssjz,String sfwfzr)throws Exception;
	/**
	 * （根据参数）更新veh_alarm_cmdscope表
	 * @param zlxh 
	 * @return
	 * @throws Exception
	 */
	public int updateAlarmScopeForHandle(String zlxh)throws Exception;
	/**
	 * （根据各参数）保存表各个字段
	 * @param csdw 
	 * @param jsdw
	 * @param ywxh
	 * @param type
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public int insertTransTabForHandle(String csdw,String jsdw,String ywxh,String type,String tableName)throws Exception;
	/**
	 * （根据参数）保存veh_alarm_livetrace表
	 * @param liveTrace(VehAlarmLivetrace)
	 * @return
	 * @throws Exception
	 */
	public int saveLiveTrace(VehAlarmLivetrace liveTrace)throws Exception;
	/**
	 * 重新保存veh_alarm_livetrace表
	 * @param liveTrace 
	 * @return
	 * @throws Exception
	 */
	public int saveLiveTrace(TransLivetrace liveTrace)throws Exception;
	/**
	 * （根据报警序号）查询veh_alarm_handled表信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public List getAlarmHandleForBjxh(String bjxh)throws Exception;
	/**
	 * （根据布控序号）查询veh_alarm_handled表信息
	 * @param bkxh 布控序号
	 * @return
	 * @throws Exception
	 */
	public List getAlarmHandleForBkxh(String bkxh)throws Exception;
	/**
	 * 查询报警未确认数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getAlarmNoHandleCount(String begin, String end, String glbm) throws Exception;
	/**
	 * 查询报警指未下达指令数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 */
	public int getAlarmCmdCount(String begin, String end, String glbm) throws Exception;
	/**
	 * 查询报警未反馈数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 */
	public int getAlarmNoHandledBackCount(String begin, String end, String glbm) throws Exception;
	/**
	 * 查询报警未拦截数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getAlarmNoLjSuspinfoCount(String begin, String end, String glbm,String yhmc,String jb,String bmlx) throws Exception;
	/**
	 * 查询报警未拦截数量(指挥中心)
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getAlarmNoLjSuspinfoCountForZx(String begin, String end, String glbm,String yhmc,String jb,String bmlx) throws Exception;
	/**
	 * 查询报警未撤控数量
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getAlarmNoCancelSuspinfoCount(String begin, String end, String glbm,String yhmc) throws Exception;
	/**
	 * 查询报警数量
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @return
	 * @throws Exception
	 */
	public int getAlarmSuspinfoCount(String begin, String end, String yhdh) throws Exception;
	/**
	 * 拦截到的报警信息
	 * @param filter
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public Map getAlarmLJForMap(Map filter, VehAlarmCmd info)throws Exception;
	/**
	 * （根据反馈编号）查询veh_alarm_handled表信息
	 * @param fkbh 反馈编号
	 * @return
	 * @throws Exception
	 */
	public VehAlarmHandled getAlarmHandleForFkbh(String fkbh)throws Exception;
	/**
	 * （根据参数）联合查询veh_alarmrec表、veh_alarm_handled
	 * @param filter
	 * @param cityname
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public Map getAlarmSTLJForMap(Map filter, VehAlarmCmd info, String cityname)throws Exception;
	/**
	 * 插入成功拦截图片
	 * @param fkbh
	 * @param handle
	 * @return
	 * @throws Exception
	 */
	public int insertLjTp(String fkbh, VehAlarmHandled handle)throws Exception;
	/**
	 * 查询veh_alarmrec表列表
	 * @param map
	 * @param classz
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findPageVehAlarmForMap(Map<String,Object> map,Class<?> classz) throws Exception;

}
