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
@RequestMapping(value="/csckmonitor.do", params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CsckMonitorCtroller {
	
	
	@Autowired
	@Qualifier("departmentManager")
	private DepartmentManager departmentManager;
	
	@RequestMapping()
	public String findForward(HttpServletRequest request,HttpServletResponse response) {
		return "monitor/csckmonitormain";
	}
	
	/**
	 * 跳转超时撤控情况检测页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=ckckDetail", method=RequestMethod.POST)
	public String  ckckDetail(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String dwdm) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		if (dwdm != null && !dwdm.equals("")) {
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
			return "monitor/st_report/st_csckmonitordetail";
		}
		return "monitor/csckmonitordetail";
	}


	public DepartmentManager getDepartmentManager() {
		return departmentManager;
	}


	public void setDepartmentManager(DepartmentManager departmentManager) {
		this.departmentManager = departmentManager;
	}
	
	
}
