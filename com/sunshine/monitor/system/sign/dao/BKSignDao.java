package com.sunshine.monitor.system.sign.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.sign.bean.BKSign;
import com.sunshine.monitor.system.sign.bean.Duty;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

public interface BKSignDao{
	/**
	 * 添加交接人(即签到)
	 * @param bs
	 * @return 返回主键编号
	 * @throws Exception
	 */
	public String addSign(BKSign bs) throws Exception;
	
	/**
	 * 查询今天是否签到时间
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public BKSign querySign(BKSign bs) throws Exception;
	
	
	/**
	 * 获取当天日期如:2013-11-11
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
	 * 签到情况列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSignList(Map<String, Object> conditions) throws Exception;
	
	/**
	 * 未签出列表(需要做任务交接班)
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public List<BKSign> queryNotSignout(String yhdh) throws Exception;
	
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
	 * 根据编号查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BKSign queryBKSignById(String id) throws Exception;
	
	
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
	 * 指挥中心反馈签收数
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryZLFKSList(BKSign bs) throws Exception;
	
		
	/**
	 * 子查询
	 */
	public BKSign queryBkSignListBysubsql(String subSql) throws Exception;
	
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
	
	public SysUser getJjrdh(String jjrmc) throws Exception;
	public BKSign getDqzbrsj() throws Exception;
	public List<Duty> queryNextZbr(String yhdh) throws Exception;
	public int saveZbrlist(Map<String, Object> conditions) throws Exception;
	public Map<String, Object> getZblist(Map<String, Object> conditions) throws Exception;
	public Map<String, Object> getZblistView(Map<String, Object> conditions) throws Exception;
	public int addZxzb(UserSession userSession) throws Exception;
		
	public Map<String, Object> getPeople(Map<String, Object> conditions);
	public Map changePeople(Map<String, Object> conditions);
	public String getTime();
	public List<Duty> editZblist(String glbm) throws Exception;

	public void updateCountWork(String id, int y_bcksp, int y_yjqs, int y_xdzl,
			int y_zlfk);
	
	
	/**
	 * 获取值班人电话
	 * @return
	 * @throws Exception
	 */
	public List dutyInfo() throws Exception ;
	
	
	
}