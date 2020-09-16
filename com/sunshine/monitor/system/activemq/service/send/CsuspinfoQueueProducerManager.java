package com.sunshine.monitor.system.activemq.service.send;

import com.sunshine.monitor.system.activemq.bean.SuspinfoMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;


public interface CsuspinfoQueueProducerManager {

	public void send(SuspinfoMessage message) throws Exception;
	
	public void send(TransObj message) throws Exception;
	
	public int updateTransStatus(String tableName, String csxh) throws Exception;
	
}
