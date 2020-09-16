package com.sunshine.monitor.system.ws.util;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Message Interceptor
 * @author OUYANG
 *
 */
public class MessageInterceptor extends AbstractPhaseInterceptor<Message>{
	
	public MessageInterceptor() {
		super(Phase.RECEIVE);
	}

	public void handleMessage(Message message) throws Fault {
		System.out.println(">>>>>>>return message<<<<<<<<<");
		System.out.println(message);
		if(message.getDestination() != null) {
			System.out.println(message.getId()+"#" + message.getDestination().getMessageObserver());
		}
		if(message.getExchange() != null){
			System.out.println(message.getExchange().getInMessage() + "#" + message.getExchange().getInFaultMessage());
			System.out.println(message.getExchange().getOutMessage() + "#" + message.getExchange().getOutFaultMessage());
		}
	}
}
