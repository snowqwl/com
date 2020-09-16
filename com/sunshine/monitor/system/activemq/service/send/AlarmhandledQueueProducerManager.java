package com.sunshine.monitor.system.activemq.service.send;

import com.sunshine.monitor.system.activemq.bean.AlarmhandledMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;


public interface AlarmhandledQueueProducerManager {

	public void send(AlarmhandledMessage message) throws Exception;
	
	public void send(TransObj message) throws Exception;
	
	public int updateTransStatus(String tableName, String csxh) throws Exception;

}
