package com.sunshine.monitor.system.activemq.service.send;

import com.sunshine.monitor.comm.context.ApplicationEventListerner;
import com.sunshine.monitor.system.activemq.bean.TransObj;

/**
 * 边界预警卡口信息推送
 * @author OUYANG 2014/4/16
 *
 */
public interface BoundaryGateQueueProducerManager extends ApplicationEventListerner  {
	
	public void send(TransObj message) throws Exception;
	
}
