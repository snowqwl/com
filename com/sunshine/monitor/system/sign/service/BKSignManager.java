package com.sunshine.monitor.system.sign.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.sign.bean.BKSign;
import com.sunshine.monitor.system.sign.bean.Duty;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface BKSignManager {
	/**
	 * 签到
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public String addSign(BKSign bs) throws Exception;
	/**
	 * 查询答到时间
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public BKSign querySign(BKSign bs) throws Exception;
	
	
	/**
	 * 获取当天时间
	 * @return
	 * @throws Exception
	 */
	public String getSysteDate() throws Exception;
	
	/**
	 * 签出
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public int signout(BKSign bs) throws Exception;
	
	/**
	 * 
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSignList(Map<String, Object> conditions) throws Exception;
	
	/**
	 * 
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public List<BKSign> queryNotSignout(String yhdh) throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BKSign queryBKSignById(String id) throws Exception;
	
	/**
	 * 布撤控审批数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public long getBCKSPS(BKSign bs) throws Exception;
	
	/**
	 * 预警签收数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public long getYJQSS(BKSign bs) throws Exception;
	
	/**
	 * 指令下达数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public long getZLXDS(BKSign bs) throws Exception;
	
	/**
	 * 指挥中心反馈签收数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public long getZLFKS(BKSign bs) throws Exception;
	
	/**
	 * 
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public int dosignout(BKSign bs) throws Exception;
	
	/**
	 * 布撤控审批列表
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryBCKSPSList(BKSign bs) throws Exception;
	
	/**
	 * 预警签收数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryYJQSSList(BKSign bs) throws Exception;
	
	/**
	 * 下达指令数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryXDZLSList(BKSign bs) throws Exception;
	
	/**
	 * 指令反馈数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryZLFKSList(BKSign bs) throws Exception;
	
	/**
	 * 子查询
	 */
	public BKSign queryBkSignListBysubsql(BKSign bs) throws Exception;
	
	/**
	 * 验证用户密码
	 * @param sysuser
	 * @return
	 * @throws Exception
	 */
	public int checkUser(SysUser sysuser) throws Exception;
	
	/**
	 * 布撤控未审批列表
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public List<VehSuspinfo> queryNoBCKSPSList(BKSign bs)throws Exception;
	
	/**
	 * 预警未签收列表
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmrec> queryNoYJQSSList(BKSign bs) throws Exception;
	
	/**
	 * 未下达指令列表
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public List<VehAlarmrec> queryNoXDZLSList(BKSign bs) throws Exception;
	
	/**
	 * 指挥中心未反馈签收数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryNoZLFKSList(BKSign bs) throws Exception;
	
	public SysUser getJjrdh(String jjr) throws Exception;
	
	/**
	 * 查询签到表中当前值班人数据
	 */
	public BKSign getDqzbrsj() throws Exception;
	
	/**
	 * 查询值班表中下一值班的人
	 */
	public List<Duty> queryNextZbr(String yhmc) throws Exception;
	/**
	 * 增加值班
	 */
	public int saveZbrlist(Map<String, Object> conditions) throws Exception;
	
	/**
	 * 查询值班
	 */
	public Map<String, Object> getZblist(Map<String, Object> conditions) throws Exception;
	
	public Map<String, Object> getZblistView(Map<String, Object> conditions) throws Exception;
	
	/**
	 * 查询值班
	 */
	public int addZxzb(UserSession userSession) throws Exception;
	
	
	public Map<String, Object> getPeople(Map<String, Object> conditions);
	public Map changePeople(Map<String, Object> conditions);
	public String getTime();
	public List<Duty> editZblist(String glbm) throws Exception;
	public void countWork(String id,int y_bcksp, int y_yjqs, int y_xdzl, int y_zlfk);
}
