package com.sunshine.monitor.system.activemq.service.send;

import com.sunshine.monitor.system.activemq.bean.SuspinfoMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;


public interface CsuspinfoTopicProducerManager {
	
	public void sendTopicMessage(SuspinfoMessage message) throws Exception;
	
	public void sendTopicMessage(TransObj message) throws Exception;
	
	public int updateTransStatus(String tableName, String csxh) throws Exception;
}

