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
@RequestMapping(value = "/bckywkhmonitor.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BckywkhMonitorCtroller {
	@Autowired
	private GateManager gateManager;
	
	/**
	 * 跳转布撤控业务考核指标统计页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,HttpServletResponse response){
	    return "monitor/bckywkhmonitormain";
	}
	
	@RequestMapping(params="method=bckywkhDetail", method=RequestMethod.POST)
	public String  dalayedavgDetail(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String bkdl) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		if(bkdl.equals("1")){//管控
			return "monitor/bckywkh_gk_monitordetail";
		}else{
			return "monitor/bckywkh_lj_monitordetail";
		}
	}
}
