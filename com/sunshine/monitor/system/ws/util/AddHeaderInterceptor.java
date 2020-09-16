package com.sunshine.monitor.system.ws.util;

import java.util.List;

import javax.xml.namespace.QName;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Adding Header
 * @author OUYANG
 * @version 1.0
 * @since 2013/11/5
 */
public class AddHeaderInterceptor extends AbstractPhaseInterceptor<SoapMessage>{

	private static String qname = "http://util.ws.system.monitor.sunshine.com/";
	
	private String sysId = "001" ;
	
	private String user = "jcbk" ;;
	
	private String pass = "jcbk" ; ;
	
	public AddHeaderInterceptor() {
		super(Phase.PREPARE_SEND);
	}

	public void handleMessage(SoapMessage message) throws Fault {
		
		List<Header> headers = message.getHeaders();
		
		Document document = DOMUtils.createDocument();
		//创建根节点
		Element root = document.createElement("authority");
		Element _sysid = document.createElement("sysid");
		_sysid.setTextContent(sysId);
		Element _user = document.createElement("user");
		_user.setTextContent(user);
		Element _pass = document.createElement("pass");
		_pass.setTextContent(pass);
		root.appendChild(_sysid);
		root.appendChild(_user);
		root.appendChild(_pass);
		//将element封装成header并添加到soap的header列表中
		SoapHeader header = new SoapHeader(new QName(qname),root);
		headers.add(header);
	}
}
