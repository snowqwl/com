package com.sunshine.monitor.system.activemq.service.send;

import com.sunshine.monitor.system.activemq.bean.AlarmrecMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;


public interface AlarmsureQueueProducerManager {

	public void send(AlarmrecMessage message) throws Exception;
	
	public void send(TransObj message) throws Exception;
	
	public int updateTransStatus(String tableName, String csxh) throws Exception;
}
