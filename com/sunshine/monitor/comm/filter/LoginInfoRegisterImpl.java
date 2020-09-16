/*package com.sunshine.monitor.comm.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import cn.com.hnisi.authentication.client.ILoginInfoRegister;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SysUserManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;

public class LoginInfoRegisterImpl implements ILoginInfoRegister {
	private static Log log = LogFactory.getLog(LoginInfoRegisterImpl.class);
	
	public void regist(HttpServletRequest request, HttpServletResponse response) {
		UserSession userSession = null;
		userSession = (UserSession) WebUtils.getSessionAttribute(request,
				"userSession");
		if (userSession == null) {
			log.debug("【cas】通过单点登录方式进入");
			String userid = (String) request.getSession().getAttribute(
					"SSOUserId");
			ApplicationContext ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(request.getSession()
							.getServletContext());
			
			DepartmentManager departmentManager = (DepartmentManager) ctx
					.getBean("departmentManager");
			SysparaManager sysparaManager = (SysparaManager) ctx
					.getBean("sysparaManager");
			SysUserManager sysuserManager = (SysUserManager)ctx
					.getBean("sysUserManager");
			LogManager logManager = (LogManager)ctx.getBean("logManager");

			if ((departmentManager == null) || (sysuserManager == null)
					|| (sysparaManager == null)) {
				sendError(request, response, "获取数据源失败");
				return;
			}

			SysUser sysuser = null;
			try {
				sysuser = sysuserManager.getSysUser(userid);
			} catch (Exception e1) {
				e1.printStackTrace();
				sysuser = null;
			}
			if (sysuser == null) {
				sendError(request, response, "获取用户信息失败");
				return;
			}
			sysuser.setIp(request.getRemoteAddr());
			Department department = null;
			try {
				department = departmentManager.getDepartment(sysuser.getGlbm());
				department.setAgent(department.getGlbm(), department.getBmlx());
				department.setPrefecture(departmentManager.getPrefecture(department));
			} catch (Exception e) {
				e.printStackTrace();
				department = null;
			}
			if (department == null) {
				
				com.sunshine.monitor.system.manager.bean.Log log = new com.sunshine.monitor.system.manager.bean.Log();
				log.setGlbm(sysuser.getGlbm());
				log.setYhdh(sysuser.getYhdh());
				log.setCzlx("1006");
				log.setCznr("获取用户部门信息失败，url:"+ request.getRequestURI());
				log.setIp(sysuser.getIp());
				
				try {
					logManager.saveLog(log);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				sendError(request, response, "获取用户部门信息失败");
				return;
			}
			List list = null;
			try {
				list = sysparaManager.getSysparas(department.getGlbm());
			} catch (Exception e) {
				e.printStackTrace();
				list = null;
			}
			if (list == null) {
				com.sunshine.monitor.system.manager.bean.Log log = new com.sunshine.monitor.system.manager.bean.Log();
				log.setGlbm(sysuser.getGlbm());
				log.setYhdh(sysuser.getYhdh());
				log.setCzlx("1006");
				log.setCznr("获取系统参数信息失败，url:"+ request.getRequestURI());
				log.setIp(sysuser.getIp());
				
				try {
					logManager.saveLog(log);
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendError(request, response, "获取系统参数信息失败");
				return;
			}
			userSession = new UserSession();
			userSession.setSysuser(sysuser);
			userSession.setDepartment(department);
			userSession.setSyspara(list);
			request.getSession().setAttribute("userSession", userSession);


			com.sunshine.monitor.system.manager.bean.Log log = new com.sunshine.monitor.system.manager.bean.Log();
			log.setGlbm(sysuser.getGlbm());
			log.setYhdh(sysuser.getYhdh());
			log.setCzlx("1005");
			log.setCznr("单点登录成功，url:"	+ request.getRequestURI());
			log.setIp(sysuser.getIp());
			
			try {
				logManager.saveLog(log);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sendError(HttpServletRequest request,
			HttpServletResponse response, String msg) {
		try {
			request.setAttribute("message", msg);
			response.sendRedirect("./reset.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
*/