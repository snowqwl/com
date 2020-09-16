package com.sunshine.monitor.system.alarm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

/**
 * 
 * @author OUYANG 2013/6/13
 *
 */
public interface VehAlarmManager {
	
	/**
	 * 超时时间，单位毫秒
	 */
	public static long overTime = 2*60*1000;
	
	/**
	 * 根据Map参数(页面提交参数)查询预警未签收列表
	 * @param conditions 条件
	 * @return
	 */
	public Map<String, Object> getAlarmsignList(Map<String,Object> conditions) throws Exception;
	
	
	public Map<String, Object> getAlarmsignListInfo(Map<String,Object> conditions) throws Exception;
	
	/**
	 * 查询预警详细信息
	 * @param bjxh
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getAlarmsignDetail(String bjxh) throws Exception;
	

	/**
	 * 查询某地市预警详细信息
	 * @param bjxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getCityAlarmsignDetail(String bjxh,String cityname) throws Exception;
	

	/**
	 * 边界预警信息写入传输表
	 * @param vehAlarmrec
	 * @param vehSuspinfo
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean saveBoundaryTransAlarm(String bkfw, String bjxh, String xzqh ,String type)throws Exception ;

	
	/**
	 * 查询预警信息
	 * @param bjxh
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getAlarmsign(String bjxh) throws Exception;
	
	/**
	 * 保存预警签收
	 * @param vehAlarmrec
	 * @return
	 * @throws Exception
	 */
	public int saveAlarmSign(VehAlarmrec vehAlarmrec)throws Exception ;
	
	/**
	 * 保存预警信息(联动)
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveAlarmLink(TransAlarm bean) throws Exception;
	
	
	/**
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveBoundaryAlarm(TransAlarm bean) throws Exception;
	
	/**
	 * 保存告警签收信息(联动)
	 */
	public int saveAlarmsureLink(TransAlarm bean) throws Exception;
	
	/**
	 * 查询传输告警信息(联动)
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public TransAlarm getTransAlarm(String ywxh) throws Exception;
	
	
	/**
	 * 签收后的信息写入同步传输表
	 * @param vehAlarmrec
	 * @param vehSuspinfo
	 * @return
	 * @throws Exception
	 */
	public boolean saveTransAlarm(VehAlarmrec vehAlarmrec,VehSuspinfo vehSuspinfo, String xzqh) throws Exception;
	
	
	/**
	 * 事务保存存预警签收
	 * @param vehAlarmrec
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doTransactionalSaveAlarmSign(VehAlarmrec vehAlarmrec)throws Exception ;



	/**
	 * 根据Map参数(页面提交参数)查询下达指令列表
	 * @param conditions 条件
	 * @return
	 */
	public Map<String, Object> getAlarmListForCmd(Map<String,Object> conditions) throws Exception;
	
	
	
	/**
	 * 报警指令列表(多条批令)
	 * @param bjxh
	 * @return
	 */
	public List<VehAlarmCmd> getCmdlist(String bjxh, boolean isQueryCmdScope) throws Exception ;
	
	/**
	 * 报警指令列表（各地市）
	 * @param bjxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmCmd> getCityCmdlist(String bjxh, String cityname) throws Exception ;
	/**
	 * 指令范围列表(即指令接受单位列表)
	 * @param zlxh
	 * @return
	 */
	public List<VehAlarmCmd> getCmdScopelist(String zlxh) throws Exception ;
	
	
	/**
	 * 保存下达指令
	 * @param vehAlarmCmd
	 * @return
	 */
	public String saveCmd(VehAlarmCmd vehAlarmCmd) throws Exception;
	
	public String saveCmd(TransCmd vehAlarmCmd) throws Exception;
	
	/**
	 * 保存指令范围
	 * @param vehAlarmCmd
	 * @return
	 * @throws Exception
	 */
	public boolean saveCmdScope(VehAlarmCmd vehAlarmCmd) throws Exception;
	
	
	/**
	 * 下达指令后更新报警表
	 * @param bjxh
	 * @return
	 * @throws Exception
	 */
	public boolean updateAlarmIsxdzl(String bjxh) throws Exception;
	
	
	/**
	 * 保存下达指令(事务处理)
	 * @param vehAlarmCmd
	 * @return
	 */
	public boolean doTransactionalSaveCmd(VehAlarmCmd vehAlarmCmd) throws Exception;
	
	
	/**
	 * 根据Map参数(页面提交参数)查询预警未签收列表(签收待修改列表)
	 * @param conditions 条件
	 * @return
	 */
	public Map<String, Object> getAlarmsigntempList(Map<String,Object> conditions) throws Exception;
	
	
	/**
	 * 将报警信息和签收信息写入临时表(签收修改)
	 * @param vehAlarmrec
	 * @return
	 * @throws Exception
	 */
	public boolean saveAlarmandSigntoTemptable(VehAlarmrec vehAlarmrec) throws Exception;
	
	
	/**
	 * 更新预警签收(签收修改)
	 * @param vehAlarmrec
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAlarmSign(VehAlarmrec vehAlarmrec) throws Exception;
	
	
	/**
	 * 事务更新预警签收
	 * @param vehAlarmrec
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doTransactionalUpdateAlarmSign(VehAlarmrec vehAlarmrec) throws Exception;

	
	/**
	 * 指挥中心确认列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryAlarmhandleConfirmlist(Map<String, Object> conditions) throws Exception ;

	/**
	 * 出警反馈列表
	 * @param bjxh
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmHandled> queryAlarmHandleList(String bjxh) throws Exception ;
	
	/**
	 * 出警跟踪列表（各地市）
	 * @param bjxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmHandled> queryCityAlarmHandleList(String bjxh,String cityname) throws Exception;

	/**
	 * 报警跟踪列表
	 * @param bjxh
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmLivetrace> getLivetraceList(String bjxh) throws Exception;
	
	/**
	 * 报警追踪列表（各地市）
	 * @param bjxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmLivetrace> getCityLivetraceList(String bjxh,String cityname) throws Exception;
	
	
	/**
	 * 保存指挥中心反馈签收
	 * @param vehAlarmHandled
	 * @return
	 * @throws Exception
	 */
	public int saveHandledConfirm(VehAlarmHandled vehAlarmHandled) throws Exception;
	
	
	/**
	 * 查询报警拦截指挥中心反馈签收信息
	 * @param fkbh
	 * @return
	 * @throws Exception
	 */
	public VehAlarmHandled queryAlarmHandled(String fkbh) throws Exception;
	
	/**
	 * 保存指挥中心反馈签收后发送短信数据
	 * @param dxcondition
	 * @return
	 * @throws Exception
	 */
	public int saveDxsj(Map<String, String> dxcondition) throws Exception;
	/**
	 * 报警---指挥中心反馈签收信息
	 * @param bjxh
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmHandled> queryAlarmhandleConfirmed(String bjxh) throws Exception;
	
	/**
	 * 报警---指挥中心反馈签收信息（各地市）
	 * @param bjxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmHandled> queryCityAlarmhandleConfirmed(String bjxh,String cityname) throws Exception;


	/**
	 * 报警列表
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmrec> getAlarmList(String bkxh)throws Exception;
	
	public Map<String,Object> getAlarmList(String kdbh,Page page)throws Exception;
	
	
	/**
	 * 查询报警列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAlarmList(Map<String,Object> conditions,UserSession userSession) throws Exception;
	

	/**
	 * 查询确认超时列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getConfirmOutList(Map<String,Object> conditions,UserSession userSession) throws Exception;
	
	
	/**
	 * 查询某号牌号码的报警列表（模糊查询）
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAlarmListByHphm(Map<String,Object> conditions,UserSession userSession) throws Exception;

	/**
	 * 获取数据库当前时间
	 * @return
	 */
	public Date getDbNowTime();

	/**
	 * 获取当前用户超时的预警条数
	 * @return
	 */
	public int getOverTimeAlarmCount() throws Exception ;
	
	/**
	 * 获取未确认的预警条数
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getOverTimeAlarmCountInThisMonth(String glbm)throws Exception ;
	
	/**
	 * 统计确认超时的报警信息
	 * @return
	 * @throws Exception
	 */
	public int getConfirmOut() throws Exception;
	
	/**
	 * 查询已经反馈签收但还没拦截的记录
	 * @return
	 * @throws Exception
	 */
	public List getNoLJ() throws Exception;
	
	
	
	/**
	 * 删除报警副本
	 * @param bjxh
	 * @throws Exception
	 */
	public void deleteVehalarec(String bjxh) throws Exception;
	
	/**
	 * 轮询跨省预警记录（湖南）
	 * @param xzqh
	 * @throws Exception
	 */
	public List<VehAlarmrec> selectToHnKSAlarm(String xzqh) throws Exception;
	
	/**
	 * 轮询跨省预警记录（广东）
	 * @param xzqh
	 * @throws Exception
	 */
	public List<VehAlarmrec> selectToGdKSAlarm(String xzqh) throws Exception;
}
