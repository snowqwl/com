package com.sunshine.monitor.system.activemq.service.send.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.activemq.service.send.BoundaryAlarmQueueProducerManager;
/**
 * 
 * @author OUYANG
 *
 */
@Transactional
@Service("boundaryAlarmQueueProducerManager")
public class BoundaryAlarmQueueProducerManagerImpl implements
		BoundaryAlarmQueueProducerManager {

	@Autowired
    private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("boundaryAlarmQueueDestination")
	private Destination boundaryAlarmQueueDestination;
	
	@Autowired
	private TransDao transDao;
	
	public void sendBoundaryAlarm(TransObj transObj) throws Exception {
		this.jmsTemplate.setPubSubDomain(false);
		this.jmsTemplate.convertAndSend(boundaryAlarmQueueDestination, transObj);
	}

	public int updateTransStatus(String tableName, String csxh)
			throws Exception {
		return this.transDao.updateTransStatus(tableName, csxh);
	}
}
