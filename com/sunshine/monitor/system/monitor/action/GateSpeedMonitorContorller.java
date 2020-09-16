package com.sunshine.monitor.system.monitor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
@Controller
@RequestMapping(value = "/gatespeed.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GateSpeedMonitorContorller {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	/**
	 * 跳转过车流量统计页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setAttribute("flag", "list");
			if (this.systemManager.getParameter("xzqh", request).equals("430000000000")) {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "true");
			} else {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "false");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
		}
		return "monitor/gatespeedmain";
	}
	/**
	 * 跳转车速监测详细页面
	 * @param kdbhs
	 * @param request
	 * @param kssj
	 * @param jssj
	 * @param tp
	 * @return
	 */
	@RequestMapping(params = "method=query", method = RequestMethod.POST)
	public String query(HttpServletRequest request,String kdbhs,String kssj,String jssj,String tp) {
        kdbhs=kdbhs.substring(0,kdbhs.length()-1);
        request.setAttribute("kdbhs",kdbhs);
        request.setAttribute("kssj",kssj);
        request.setAttribute("jssj",jssj);
        request.setAttribute("type",tp);
		return "monitor/gatespeeddetail";
	}
}
