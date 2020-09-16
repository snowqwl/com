package com.sunshine.monitor.system.activemq.service.send.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.activemq.bean.SuspinfoMessage;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.activemq.service.send.SuspinfoTopicProducerManager;

@Transactional
@Service("suspinfoTopicProducerManager")
public class SuspinfoTopicProducerManagerImpl implements
		SuspinfoTopicProducerManager {

	@Autowired
    private JmsTemplate jmsTemplate;
	@Autowired
	private Destination suspinfoTopicDestination;
    //private Topic destination;
	
	@Autowired
	private TransDao transDao;
	
   /* public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setDestination(Topic destination) {
		this.destination = destination;
	}
   */
	public void sendTopicMessage(SuspinfoMessage message) throws Exception {

		this.jmsTemplate.convertAndSend(this.suspinfoTopicDestination, message);
	}
	
	public void sendTopicMessage(TransObj message) throws Exception {
		this.jmsTemplate.setPubSubDomain(true);	//订阅模式
		this.jmsTemplate.convertAndSend(this.suspinfoTopicDestination, message);
	}
	
	public int updateTransStatus(String tableName, String csxh) throws Exception {
			return this.transDao.updateTransStatus(tableName, csxh);
	}


}
