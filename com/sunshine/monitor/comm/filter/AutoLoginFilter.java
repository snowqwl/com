package com.sunshine.monitor.comm.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SysUserManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;

public class AutoLoginFilter implements Filter {
	
	public void destroy() {
		

	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		Object userSessionObj = req.getAttribute("userSession");
		Object sfz = req.getParameter("sfz");
		
		SysUserManager sysuserManager = SpringApplicationContext.getBean("sysUserManager", SysUserManager.class);
		
		if(userSessionObj == null && sfz != null){
			try {
				//DragonEncrypt et = new DragonEncrypt();
				//String sfz = et.unEncrypt(_tempSfz.toString().trim());
				DepartmentManager departmentManager = SpringApplicationContext.getBean("departmentManager", DepartmentManager.class);
				SysparaManager sysparaManager = SpringApplicationContext.getBean("sysparaManager", SysparaManager.class);
				SysUser user = sysuserManager.getSysuserBySfzmhm("'"+(String)sfz+"'");
				if(user == null) throw new Exception("查询不到身份证为："+sfz+"的用户！");
				HttpSession session = ((HttpServletRequest)req).getSession();
				UserSession userSession = new UserSession();
				userSession.setSysuser(user);
				Department department = departmentManager
								.getDepartment(user.getGlbm());
				
				List list = sysparaManager.getSysparas(department.getGlbm());
				department.setAgent(department.getGlbm(), department.getBmlx());
				//department.setPrefecture(departmentManager
					//	.getPrefecture(department));
				
			//	department.setKey(this.departmentManager.getDepartmentKey(department.getGlbm()));
				
				userSession.setDepartment(department);
				userSession.setSyspara(list);
				session.setAttribute("userSession", userSession);
				if (list != null) {
					for (Iterator it = list.iterator(); it.hasNext();) {
						Syspara syspara = (Syspara) it.next();
						if ((syspara.getGjz().equalsIgnoreCase("users"))
								&& (syspara.getSfgy().equals("1"))) {
							session.setAttribute("totalP", syspara.getCsz());
						}
					}
				}
				String ip = ((HttpServletRequest)req).getRemoteAddr();
				user.setIp(ip);
				
				LogManager logManager = SpringApplicationContext.getBean("logManager", LogManager.class);
				Log log = new Log();
				log.setGlbm(user.getGlbm());
				log.setYhdh(user.getYhdh());
				log.setCzlx("1001");
				log.setCznr("用户登录成功");
				log.setIp(user.getIp());
				logManager.saveLog(log);
				setOnline((HttpServletRequest)req, userSession,session.getServletContext(),ip,"SFZ登录", "系统用户");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		chain.doFilter(req, resp);

	}
	
	private void setOnline(HttpServletRequest request, UserSession userSession, ServletContext application,
			String ip, String type, String system) throws Exception {
		SysUser s = new SysUser();
		s.setYhdh(userSession.getSysuser().getYhdh());
		s.setYhmc(userSession.getSysuser().getYhmc());
		s.setJh(userSession.getSysuser().getJh());
		s.setIp(ip);
		s.setGlbm(userSession.getDepartment().getGlbm());
		s.setBmmc(userSession.getDepartment().getBmmc());
		s.setZt(Common.getNow());
		s.setYzm(type);
		s.setMm(system);
		/*HashMap map = (HashMap) application.getAttribute("onlines");
		if (map == null) {
			map = new HashMap();
			map.put(s.getYhdh(), s);
			application.setAttribute("onlines", map);
			application.setAttribute("online", String.valueOf(1));
		} else if (map.containsKey(s.getYhdh())) {
			map.put(s.getYhdh(), s);
			application.setAttribute("onlines", map);
		} else {
			map.put(s.getYhdh(), s);
			int c = Integer.parseInt((String) application
					.getAttribute("online"));
			c++;
			application.setAttribute("onlines", map);
			application.setAttribute("online", String.valueOf(c));
		}*/
		request.getSession().setMaxInactiveInterval(30 * 60);
		request.getSession().setAttribute(OnlineUserListener.LISTENER_NAME, s);
		application.setAttribute("online", OnlineUserListener.lineCount.get());
	}

	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
