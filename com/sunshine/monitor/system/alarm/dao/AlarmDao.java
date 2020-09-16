package com.sunshine.monitor.system.alarm.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.bean.SqlConditionFactory.SqlCondition;
import com.sunshine.monitor.comm.dao.BaseDao;
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
public interface AlarmDao extends BaseDao{
	
	/**
	 * 根据Map参数(页面提交参数)查询预警未签收列表
	 * @param conditions 条件
	 */
	public Map<String, Object> getAlarmList(Map<String,Object> conditions, String tableName,String bkr) throws Exception;
	
	/**
	 * 查询确认超时列表
	 * @param filter
	 * @param alarm
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getConfirmOutList(Map<String,Object> conditions) throws Exception;
	
	/**
	 * 根据(号牌号码)查询报警信息
	 * @param conditions  条件
	 */
	public Map<String,Object> getAlarmListByHphm(Map<String,Object> conditions) throws Exception;
	
	public Map<String, Object> getAlarmListInfo(Map<String,Object> conditions, String tableName,String bkr) throws Exception;
	
	
	/**
	 * 根据报警序号查询报警信息
	 * @param bjxh 报警序号
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getVehAlarmrec(String bjxh) throws Exception ;
	

	/**
	 * 根据报警序号查询各地市的报警信息
	 * @param bjxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public VehAlarmrec getCityVehAlarmrec(String bjxh,String cityname) throws Exception ;
	
	
	/**
	 * 保存预警签收
	 * @param vehAlarmrec 报警记录
	 * @return
	 */
	public int saveAlarmSign(VehAlarmrec vehAlarmrec) throws Exception;
	
	/**
	 * 保存预警信息(联动)
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveAlarmLink(TransAlarm bean) throws Exception;
	
	public TransAlarm getTransAlarmDetail(String ywxh) throws Exception;
	
	/**
	 * 签收时写传输表(用于布控信息来源为联动布控、情报平台、六合一转入情况)
	 * @param vehAlarmrec
	 * @param vehSuspinfo
	 * @return
	 * @throws Exception
	 */
	public boolean saveTransAlarm(VehAlarmrec vehAlarmrec,VehSuspinfo vehSuspinfo, String param)throws Exception ;
	
	
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
	 * 
	 * 函数功能说明:未安装新系统报警传输按原传输方案
	 * 修改日期 	2013-9-4
	 * @param vehAlarmrec
	 * @param vehSuspinfo
	 * @return
	 * @throws Exception    
	 * @return boolean
	 */
	public boolean saveTransAlarm_test(VehAlarmrec vehAlarmrec,VehSuspinfo vehSuspinfo, String param)throws Exception ;
	
	
	/**
	 * 报警指令列表(多条批令)
	 * @param bjxh
	 * @return
	 */
	public List<VehAlarmCmd> getCmdlist(String bjxh) throws Exception ;
	
	/**
	 * 报警指令列表（各地市）
	 * @param bjxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmCmd> getCityCmdlist(String bjxh, String cityname) throws Exception;
	
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
	 * 将报警信息写入临时表(报警表副本)
	 * 业务:用于报警签收修改
	 * @param vehAlarmrec
	 * @return
	 */
	public boolean saveAlarmandSigntoTemptable(VehAlarmrec vehAlarmrec) throws Exception;
	
	/**
	 * 更新预警签收信息(直接修改)
	 * @param vehAlarmrec
	 * @return
	 */
	public Map<String, Object> updateAlarmSign(VehAlarmrec vehAlarmrec);
	
	
	/**
	 * 查询超时临时预警签收
	 * @return
	 */
	public List<VehAlarmrec> queryTimeoutTempAlarmsign(int timeOut);
	
	/**
	 * 删除超时修改签收
	 * @param bjxh
	 * @return
	 */
	public int deleteTimeoutTempAlarmsign(String bjxh);
	
	/**
	 * 指挥中心确认列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryAlarmhandleConfirmlist(Map<String,Object> conditions) throws Exception;

	
	/**
	 * 报警出警反馈列表
	 * @param bjxh
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmHandled> queryAlarmHandleList(String bjxh) throws Exception;

	/**报警出警反馈列表
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
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmLivetrace> getCityLivetraceList(String bjxh,String cityname) throws Exception;
	
	
	/**
	 * 保存反馈签收信息
	 * @param fkbh
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
	
	/**
	 * 根据条件查询预警条数
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public int getAlarmCount(List<SqlCondition> conditions) throws Exception;
	
	/**
	 * 查询部门的预警签收未确认条数
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public int getAlarmSignOutCount(String glbm) throws Exception;
	
	/**
	 * 统计确认超时的信息
	 * @return
	 * @throws Exception
	 */
	public int getConfirmOut() throws Exception;
	/**
	 * 保存指挥中心反馈签收后发送短信数据
	 * @param dxcondition
	 * @return
	 * @throws Exception
	 */
	public int saveDxsj(Map<String, String> dxcondition) throws Exception;
	
	/**
	 * 查询已经反馈签收但还没拦截的记录
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmrec> getNoLJ() throws Exception;
	
	/**
	 * 查询已经拦截确认但还没撤控的记录
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmrec> getNoCancelSus() throws Exception;
	
	/**
	 * 获取预警信息列表
	 * @param kdbh
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getAlarmList(String kdbh,Page page)throws Exception;
	
	/**
	 * 轮询跨省预警记录（湖南）
	 * @param bjxh
	 * @throws Exception
	 */
	public List<VehAlarmrec> selectToHnKSAlarm(String xzqh) throws Exception;
	
	/**
	 * 轮询跨省预警记录（广东）
	 * @param bjxh
	 * @throws Exception
	 */
	public List<VehAlarmrec> selectToGdKSAlarm(String xzqh) throws Exception;
}
