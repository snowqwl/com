package com.sunshine.monitor.system.monitor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;

@Controller
@RequestMapping(value="/kkjrjcmonitor.do", params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KkjrjcMonitorCtroller {
	
	/**
	 * 跳转卡口接入监测页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,HttpServletResponse response) {
		return "monitor/kkjrjcmonitormain";
	}
	
	
	@RequestMapping(params="method=kkjrjcDetail")
	public String  kkjrjcDetail(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		 kssj=kssj+" 00:00:00";
		 jssj=jssj+" 23:59:59";
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		
		if(dp.getGlbm().startsWith("4300")){
			return "monitor/st_report/st_kkjrjcmonitordetail";
		}
		return "monitor/kkjrjcmonitordetail";
	}
}
