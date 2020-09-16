package com.sunshine.monitor.system.ws.GateService;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.sunshine.monitor.system.ws.GateService.bean.CodeGateEntity;
import com.sunshine.monitor.system.ws.GateService.bean.CodeGateExtendEntity;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public  interface CodeGateConditionService {
	@WebMethod
	public List<CodeGateEntity> getGates();
	
	@WebMethod
	public List<CodeGateExtendEntity> getDirectName(@WebParam(name="kdbh")String kdbh);
	
}
