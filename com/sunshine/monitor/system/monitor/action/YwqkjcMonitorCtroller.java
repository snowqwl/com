package com.sunshine.monitor.system.monitor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;

@Controller
@RequestMapping(value="/ywqkjcmonitor.do", params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class YwqkjcMonitorCtroller {
	
	/**
	 * 跳转故障响应情况监测页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,HttpServletResponse response) {
		return "monitor/ywqkjcmonitormain";
	}
	
	
	@RequestMapping(params="method=ywqkjcDetail", method=RequestMethod.POST)
	public String  kkjrjcDetail(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		return "monitor/ywqkjcmonitordetail";
	}

}
