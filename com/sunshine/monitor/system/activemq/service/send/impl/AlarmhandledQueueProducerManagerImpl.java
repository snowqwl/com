package com.sunshine.monitor.system.activemq.service.send.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.activemq.bean.AlarmhandledMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.activemq.service.send.AlarmhandledQueueProducerManager;

@Transactional
@Service("alarmhandledQueueProducerManager")
public class AlarmhandledQueueProducerManagerImpl implements
	AlarmhandledQueueProducerManager {

	@Autowired
    private JmsTemplate jmsTemplate;
	@Autowired
	private Destination alarmhandledQueueDestination;
	@Autowired
	private TransDao transDao;
	
	public void send(AlarmhandledMessage message) throws Exception {
		this.jmsTemplate.convertAndSend(this.alarmhandledQueueDestination, message);
	}
	
	public void send(TransObj message) throws Exception {
		this.jmsTemplate.convertAndSend(this.alarmhandledQueueDestination, message);
	}

	public int updateTransStatus(String tableName, String csxh) throws Exception {
		return this.transDao.updateTransStatus(tableName, csxh);
	}
}
