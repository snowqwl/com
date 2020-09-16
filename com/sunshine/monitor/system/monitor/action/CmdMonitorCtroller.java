package com.sunshine.monitor.system.monitor.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.service.DepartmentManager;


@Controller
@RequestMapping(value = "/cmdMonitor.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmdMonitorCtroller {

	
	@Autowired
	@Qualifier("departmentManager")
	private DepartmentManager departmentManager;
	
	
	@RequestMapping(params = "method=index")
	public String index() {
		return "monitor/cmdmonitormain";
	}
	
	@RequestMapping
	public String indexHandle() {
		return "monitor/cmdhandlemonitormain";
	}
	
	@RequestMapping
	public String indexAlarm() {
		return "monitor/alarmmonitormain";
	}
	
	/**
	 * 跳转指令下达超时详细页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String query(HttpServletRequest request, String kssj,String jssj,String dwdm){
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
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
			return "monitor/st_report/st_cmdmonitordetail";
		}
		return "monitor/cmdmonitordetail";
	}
	/**
	 * 跳转指令反馈超时详细页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String queryHandle(HttpServletRequest request, String kssj,String jssj,String dwdm){
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
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
			return "monitor/st_report/st_cmdhandlemonitordetail";
		}
		return "monitor/cmdhandlemonitordetail";
	}
	/**
	 * 跳转预警签收超时详细页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String queryAlarm(HttpServletRequest request, String kssj,String jssj,String dwdm){
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
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
			return "monitor/st_report/st_alarmmonitordetail";
		}
		return "monitor/alarmmonitordetail";
	}

	public DepartmentManager getDepartmentManager() {
		return departmentManager;
	}

	public void setDepartmentManager(DepartmentManager departmentManager) {
		this.departmentManager = departmentManager;
	}
	
}
