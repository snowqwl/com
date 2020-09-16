package com.sunshine.monitor.system.activemq.dao;

import java.util.List;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransAlarmHandled;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.activemq.bean.TransSusp;

public abstract interface TransDao extends BaseDao {
	
	public List getTransList(String tablename, String type, String maxrow);
	
	/**
	 * 获取布控审信息
	 * @param ywxh
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public TransAuditApprove getTransSuspApproval(String ywxh, String type)throws Exception;
	
	/**
	 * 查询传输布/撤控信息
	 * @param ywxh
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public TransSusp getTransSuspDetail(String ywxh, int type)throws Exception;
	
	/**
	 * 查询传输告警信息
	 * @param ywxh
	 * @return
	 * @throws Exception
	 */
	public TransAlarm getTransAlarmDetail(String ywxh) throws Exception;
	
	/**
	 * 查询传输出警信息
	 * @param ywxh
	 * @return
	 * @throws Exception
	 */
	public TransAlarmHandled getTransAlarmHandledDetail(String ywxh) throws Exception;
	/**
	 * 更新传输调度状态
	 * @return
	 * @throws Exception
	 */
	public int updateTransStatus(String tableName, String csxh) throws Exception;
	
}