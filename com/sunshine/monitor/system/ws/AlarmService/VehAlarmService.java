package com.sunshine.monitor.system.ws.AlarmService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding
public interface VehAlarmService {
    
	@WebMethod
	public String queryVehAlarm(@WebParam(name="conditions")String conditions);
}
