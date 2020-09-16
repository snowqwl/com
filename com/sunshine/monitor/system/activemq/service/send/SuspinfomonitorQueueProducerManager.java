package com.sunshine.monitor.system.activemq.service.send;

import com.sunshine.monitor.system.activemq.bean.TransSuspmonitor;


public interface SuspinfomonitorQueueProducerManager {
	
	public void send(TransSuspmonitor message) throws Exception;
	
	
}
