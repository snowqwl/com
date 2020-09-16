package com.sunshine.monitor.system.activemq.service.send;

import com.sunshine.monitor.system.activemq.bean.TransObj;

/**
 * 边界预警推送
 * @author OUYANG
 *
 */
public interface BoundaryAlarmQueueProducerManager {
	
	/**
	 * 
	 * @param transObj
	 * @throws Exception
	 * @see com.sunshine.monitor.system.activemq.bean.BoundaryAlarmrecMessage
	 */
	public void sendBoundaryAlarm(TransObj transObj) throws Exception;
	
	/**
	 * Update transport table status
	 * @param tableName
	 * @param csxh
	 * @return
	 * @throws Exception
	 */
	public int updateTransStatus(String tableName, String csxh) throws Exception;
	
}
