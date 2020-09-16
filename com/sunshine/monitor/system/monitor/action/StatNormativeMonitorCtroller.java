package com.sunshine.monitor.system.monitor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Department;

@Controller
@RequestMapping(value = "/normativemonitor.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatNormativeMonitorCtroller {
	@Autowired
	private GateManager gateManager;

	/**
	 * 跳转数据规范性情况监测页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			UserSession userSession = (UserSession) request.getSession()
			.getAttribute("userSession");
	        Department dp = userSession.getDepartment();
	        if (!"4300".equals(dp.getGlbm().substring(0, 4))) {
	        	request.setAttribute("departmentList", this.gateManager
	        			.getDepartmentsByKdbh());
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "monitor/normmonitormain";
	}
	/**
	 * 跳转数据规范性情况监测页面
	 * @param request
	 * @param response
	 * @param kssj
	 * @param jssj
	 * @param jkdss
	 * @return
	 */
	@RequestMapping()
	public String normtavieDetail(HttpServletRequest request,
			HttpServletResponse response, String kssj, String jssj,
			String jkdss) {
		UserSession userSession = (UserSession) request.getSession()
				.getAttribute("userSession");
		Department dp = userSession.getDepartment();
		if (jkdss == null || "".equals(jkdss)) {
			jkdss = "1";
		}
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		request.setAttribute("kdbhs", jkdss);
		if ("4300".equals(dp.getGlbm().substring(0, 4))) {
			return "monitor/st_report/st_normativeonitordetail";
		}
        return "monitor/normmonitordetail";
			
	}


}
