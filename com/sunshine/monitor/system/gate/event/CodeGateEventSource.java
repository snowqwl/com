package com.sunshine.monitor.system.gate.event;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.comm.context.AbstractEventSource;
import com.sunshine.monitor.comm.context.ApplicationEventListerner;

/**
 * Gate event source
 * 
 * @author OUYANG 2014/4/17
 * 
 */
@Component("codeGateEventSource")
public class CodeGateEventSource extends AbstractEventSource implements
		InitializingBean {

	@Autowired
	@Qualifier("alarmPrearrangQueueProducerManager")
	private ApplicationEventListerner lisn;

	/**
	 * Init Listerner list by default
	 */
	public CodeGateEventSource() {

	}

	/**
	 * Program auto add listerner by default
	 * 
	 * @see com.sunshine.monitor.system.activemq.service.send.impl.BoundaryGateQueueProducerManagerImpl
	 */
	public void afterPropertiesSet() throws Exception {
		if (lisn != null)
			this.addListerner(lisn);
	}
}
