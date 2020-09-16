package com.sunshine.monitor.system.monitor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Department;

@Controller
@RequestMapping(value = "/delayedavgmonitor.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DelayedAvgMonitorCtroller {
	@Autowired
	private GateManager gateManager;
	
	/**
	 * 跳转通行车辆延时平均值统计页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,HttpServletResponse response){
	    return "monitor/delayedavgmonitormain";
	}
	
	@RequestMapping(params="method=dalayedavgDetail", method=RequestMethod.POST)
	public String  dalayedavgDetail(HttpServletRequest request,HttpServletResponse response,String yyyy,String sesson) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		request.setAttribute("yyyy", yyyy);
		request.setAttribute("sesson", sesson);
		return "monitor/delayedavgmonitordetail";
	}
}
