package com.sunshine.monitor.system.monitor.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.monitor.bean.KhzbProject;

@Controller
@RequestMapping(value = "/szpgmonitor.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SzpgMonitorCtroller {
	
	@RequestMapping()
	public String forward(HttpServletRequest request,HttpServletResponse respons){
		return "monitor/szpgmain";
	}
	
	
	@RequestMapping(params = "method=szpgDetail")
	public String szpgDetail(HttpServletRequest request,
			HttpServletResponse response, String kssj, String jssj) {
		try {
			request.setAttribute("kssj", kssj+" 00:00:00");
			request.setAttribute("jssj", jssj+" 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "monitor/szpgdetail";
	}
}
