package com.sunshine.monitor.system.activemq.service.send.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.activemq.bean.TransSuspmonitor;
import com.sunshine.monitor.system.activemq.service.send.SuspinfomonitorQueueProducerManager;

@Transactional
@Service("suspinfomonitorQueueProducerManager")
public class SuspinfomonitorQueueProducerManagerImpl implements
	SuspinfomonitorQueueProducerManager {

	@Autowired
    private JmsTemplate jmsTemplate;
	@Autowired
    //private Queue destination;
	private Destination suspinfomonitorQueueDestination;

	public void send(TransSuspmonitor message) throws Exception {
		this.jmsTemplate.setPubSubDomain(false);	//p2p模式
		this.jmsTemplate.convertAndSend(this.suspinfomonitorQueueDestination, message);
		
	}


}
