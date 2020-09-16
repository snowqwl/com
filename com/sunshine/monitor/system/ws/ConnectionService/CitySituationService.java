package com.sunshine.monitor.system.ws.ConnectionService;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.system.ws.ConnectionService.bean.CitySituationEntity;

/**
 * city informations web service interface
 * Using JAX-WS API
 * Binding method is JAXB
 * Implementor Architecture Apache CXF 2.7.6
 * 
 *
 */
@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface CitySituationService {
	//各地市调用，通过省厅数据库地市连接情况
	@WebMethod
	public List<CitySituationEntity> getSurvey();
	
	//省厅调用，获取各地市的连接情况
	@WebMethod
	public String cityResponse();

}
