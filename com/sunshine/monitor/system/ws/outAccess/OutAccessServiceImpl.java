package com.sunshine.monitor.system.ws.outAccess;

import java.rmi.RemoteException;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.ws.outAccess.bean.IntelApplyResult;
import com.sunshine.monitor.system.ws.outAccess.bean.WsManager;
import com.sunshine.monitor.system.ws.outAccess.dao.WsManagerDao;
import com.sunshine.monitor.system.ws.outAccess.exception.OutAccessWSException;
import com.sunshine.monitor.system.ws.outAccess.services.WsService;

@WebService(endpointInterface = "com.sunshine.monitor.system.ws.outAccess.OutAccessService", portName = "OutAccess", serviceName = "OutAccessService", name = "OutAccess")
@Service("OutAccessService")
public class OutAccessServiceImpl implements OutAccessService {

	public String executes(String systemType, String businessType, String sn,
			String xml) throws RemoteException {
		MessageContext mc = MessageContext.getCurrentContext();
		HttpServletRequest request = (HttpServletRequest) mc
				.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		

		WsManager wsManager = null;
		ApplicationContext ac = SpringApplicationContext.getStaticApplicationContext();
		WsManagerDao wsManagerDao = ac.getBean("wsManagerDao",WsManagerDao.class);
		try {
			wsManager = wsManagerDao.queryDetail(systemType, businessType);
		} catch (Exception e) {
			// DebugLog.debug("exception", "[OutAccess]" + e.getMessage());
			IntelApplyResult result = new IntelApplyResult();
			result.setCode("100");
			result.setExceptionMsg(e.getMessage());
			result.setResultInfo("无法取得接口定义信息；");
			return getXml(result);
		}
		IntelApplyResult result = new IntelApplyResult();;
		if (wsManager != null) {
			try {
				Class clazz = Class.forName(wsManager.getClassname());
				WsService service = SpringApplicationContext
						.getStaticApplicationContext()
						.getBean(clazz.getSimpleName(), WsService.class);
				service.checkAuthority();
				service.chechData();
				service.execute(systemType, businessType, sn, xml, request);
			} catch (OutAccessWSException e) {
				result.setCode(e.getErrorCode());
				result.setExceptionMsg(e.getExceptionMsg());
				result.setResultInfo(e.getInfo());
				return getXml(result);
			} catch (Exception ex) {
				ex.printStackTrace();
				// DebugLog.debug("exception", "[OutAccess]" + ex.getMessage());
				result.setCode("999");
				result.setExceptionMsg(ex.getMessage());
				result.setResultInfo("服务端发生未知错误");
				return getXml(result);
			}
		}
		result.setCode("000");
		result.setResultInfo("操作成功");
		result.setExceptionMsg("");
		return getXml(result);
	}
	
	private String getXml(IntelApplyResult intelApplyRS) {
		StringBuffer sb = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<ResultSet>");
		sb.append("<ReturnValue>").append(intelApplyRS.getCode()).append(
				"<ReturnValue>");
		sb.append("<InfoSet>");
		sb.append("<Field>");
		sb.append("<Name>bh</Name>");
		sb.append("<Value>").append(intelApplyRS.getExceptionMsg()).append(
				"</Value>");
		sb.append("</Field>");
		sb.append("<Field>");
		sb.append("<Name>msg</Name>");
		sb.append("<Value>").append(intelApplyRS.getResultInfo()).append("</Value>");
		sb.append("</Field>");
		sb.append("</InfoSet>");
		sb.append("</ResultSet>");
		return sb.toString();
	}

}
