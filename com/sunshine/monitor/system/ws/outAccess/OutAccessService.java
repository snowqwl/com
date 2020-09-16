package com.sunshine.monitor.system.ws.outAccess;

import java.rmi.RemoteException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 外系统 调用本地方法接口 如 : 布控撤控
 * @author Administrator
 *
 */
@WebService
public interface OutAccessService {
	/**
	 * 根据 systemType ， systemType 查找 WsManagerDao 找到执行类
	 * 
	 * @param systemType
	 * @param businessType
	 * @param sn 身份验证
	 * @param xml xPath: Parameters/InfoSet/Field  &lt;name&gt;fieldName&lt;/name&gt; &lt;value&gt;value &lt;/value&gt;
	 * @return
	 * @throws RemoteException
	 */
	@WebMethod
	public String executes(
			@WebParam(name="systemType")String systemType, 
			@WebParam(name="businessType")String businessType, 
			@WebParam(name="sn")String sn, 
			@WebParam(name="xml")String xml)
    	throws RemoteException;
}
