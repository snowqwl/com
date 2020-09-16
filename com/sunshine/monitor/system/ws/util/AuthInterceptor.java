package com.sunshine.monitor.system.ws.util;


import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Server check
 * @author OUYANG
 * @version 1.0
 * @since 2013/11/5
 */
public class AuthInterceptor extends AbstractPhaseInterceptor<SoapMessage>{

	private final String SYS_ID = "001";
	
	private final String SYS_USER = "jcbk";
	
	private final String SYS_PASS = "jcbk";
	
	public AuthInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}

	public void handleMessage(SoapMessage message) throws Fault {
		List<Header> headers = message.getHeaders();
		if(headers == null || headers.size() < 1){
			SOAPException soaExc = new SOAPException("认证错误");
			throw new Fault(soaExc);
		}
		Header firstHeader = headers.get(0);
		Element ele = (Element)firstHeader.getObject();
		NodeList sysidNode = ele.getElementsByTagName("sysid");
		NodeList userNode = ele.getElementsByTagName("user");
		NodeList passNode = ele.getElementsByTagName("pass");
		if(SYS_ID.equals(sysidNode.item(0).getTextContent()) 
				&& SYS_USER.equals(userNode.item(0).getTextContent())
				&& SYS_PASS.equals(passNode.item(0).getTextContent())){
			//System.out.println(">>>>>>全省漫游查询认证成功<<<<<<<<");
		} else {
			SOAPException soaExc = new SOAPException("认证错误");
			throw new Fault(soaExc);
		}
	}
}
