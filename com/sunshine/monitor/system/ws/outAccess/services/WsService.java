package com.sunshine.monitor.system.ws.outAccess.services;

import javax.servlet.http.HttpServletRequest;

import com.sunshine.monitor.system.ws.outAccess.bean.IntelApplyResult;
import com.sunshine.monitor.system.ws.outAccess.exception.OutAccessWSException;

public interface WsService {
	
	/**
	 * WebServices 请求结果
	 * @param systemType 
	 * @param businessType
	 * @param sn 
	 * @param xml 数据主体 
	 * @param request
	 * @return WebServices 请求结果实体
	 * @throws OutAccessWSException 
	 */
	 public void execute(String systemType, String businessType, String sn,
				String xml, HttpServletRequest request)  throws OutAccessWSException;
	 
	 
	 /**
	  * 检查访问权限
	  * @throws OutAccessWSException
	  */
	 public void checkAuthority() throws OutAccessWSException ;
	 
	 /**
	  * 检查数据完整性
	  * @throws OutAccessWSException
	  */
	 public void chechData() throws OutAccessWSException ;
}
