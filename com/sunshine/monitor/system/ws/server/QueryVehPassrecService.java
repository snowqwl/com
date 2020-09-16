package com.sunshine.monitor.system.ws.server;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.ws.WebFault;

import com.sunshine.monitor.system.ws.QueryVehPassrecConditions;
import com.sunshine.monitor.system.ws.VehPassrecEntity;

/**
 * Querying car informations web service interface
 * Return a list object
 * Using JAX-WS API
 * Binding method is JAXB
 * Implementor Architecture Apache CXF 2.7.6
 * @author OUYANG 2013/8/5
 *
 */
@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface QueryVehPassrecService {
	/**
	 * Query Conditions is packaged vehPassrec object
	 * JAXB support Lists and handles Lists gre	at
	 * @param vehPassrec
	 * @return
	 */
	@WebMethod
	public List<VehPassrecEntity> queryVehPassrec(
			@WebParam(name = "queryConditions") QueryVehPassrecConditions queryConditions);
	
}
