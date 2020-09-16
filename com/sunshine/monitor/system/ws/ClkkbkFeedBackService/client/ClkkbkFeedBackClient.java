package com.sunshine.monitor.system.ws.ClkkbkFeedBackService.client;

import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.ClkkbkFeedBackService;

@Component
public class ClkkbkFeedBackClient implements ClkkbkFeedBackService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 初始值为测试需要，测试完成删除
	private String wsdlUrl ; //= "http://localhost:8080/jcbk/services/ClkkbkFeedBackService?wsdl";
	
	public ClkkbkFeedBackClient() {
		
	}

	public String feedBack(String xml) {
		String resultxml = "";
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(getWsdlUrl());
			call.setOperationName("feedBack");
			call.addParameter("xml", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("http://www.wj.com/Rpc");
			resultxml = (String) call.invoke(new Object[] { xml });
		} catch (Exception e) {
			e.printStackTrace();
			resultxml = "";
		}
		return resultxml;
	}
	
	public String getWsdlUrl(){
		if(this.wsdlUrl == null)
			this.wsdlUrl = (String) jdbcTemplate.queryForObject(
					"select csz from frm_syspara where gjz='IntelligenceWsdl'",
					String.class);
		return this.wsdlUrl;
	}

	public JdbcTemplate getJdbcTemplate() {
		if(wsdlUrl == null)
			wsdlUrl = (String) jdbcTemplate.queryForObject(
					"select csz from frm_syspara where gjz='IntelligenceWsdl'",
					String.class);
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
}
