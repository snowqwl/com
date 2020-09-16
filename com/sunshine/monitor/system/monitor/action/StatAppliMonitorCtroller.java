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

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.service.DepartmentManager;

@Controller
@RequestMapping(value="/applimonitor.do", params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatAppliMonitorCtroller {

	
	@Autowired
	@Qualifier("departmentManager")
	private DepartmentManager departmentManager;
	/**
	 * 跳转业务处置情况统计检测页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,HttpServletResponse response) {
		return "monitor/applimonitormain";
	}
	
	
	@RequestMapping(params="method=appliDetail", method=RequestMethod.POST)
	public String  appliDetail(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String dwdm) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		if (dwdm != null  && !dwdm.equals("")) {
			try {
				dp = departmentManager.getDepartment(dwdm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		if ("4300".equals(dp.getGlbm().substring(0, 4))) {
		    return "monitor/st_report/st_applimonitordetail";
		}
		return "monitor/applimonitordetail";
	}


	public DepartmentManager getDepartmentManager() {
		return departmentManager;
	}


	public void setDepartmentManager(DepartmentManager departmentManager) {
		this.departmentManager = departmentManager;
	}
}
