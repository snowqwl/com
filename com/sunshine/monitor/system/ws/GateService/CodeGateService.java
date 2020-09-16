package com.sunshine.monitor.system.ws.GateService;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface CodeGateService {

	@WebMethod
    public String getGate(@WebParam(name="conditions")String conditions);	
	
	@WebMethod
	public String queryGate(@WebParam(name="conditions")String conditions);

}
