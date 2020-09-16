package com.sunshine.monitor.system.ws.util;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class HeaderInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

	public HeaderInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}

	public void handleMessage(SoapMessage message) throws Fault {
	    Message m=message.getMessage();
	    m.get("entries");
	}
}
