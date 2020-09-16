package com.sunshine.monitor.system.activemq.service.send.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.activemq.bean.SuspinfoMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.activemq.service.send.SuspinfoQueueProducerManager;

@Transactional
@Service("suspinfoQueueProducerManager")
public class SuspinfoQueueProducerManagerImpl implements
	SuspinfoQueueProducerManager {

	@Autowired
    private JmsTemplate jmsTemplate;
	@Autowired
    //private Queue destination;
	private Destination suspinfoQueueDestination;
	@Autowired
	private TransDao transDao;

	public void send(SuspinfoMessage message) throws Exception {
		this.jmsTemplate.convertAndSend(this.suspinfoQueueDestination, message);
		//this.jmsTemplate.convertAndSend("QUEUE.Suspinfo", message);
	}

	public void send(TransObj message) throws Exception {
		this.jmsTemplate.setPubSubDomain(false);	//p2p模式
		this.jmsTemplate.convertAndSend(this.suspinfoQueueDestination, message);
		
	}

	public int updateTransStatus(String tableName, String csxh)
			throws Exception {
		return this.transDao.updateTransStatus(tableName, csxh);
	}

}
