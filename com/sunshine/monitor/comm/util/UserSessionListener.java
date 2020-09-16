package com.sunshine.monitor.comm.util;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.sunshine.monitor.comm.bean.UserSession;

/**
 * Session监听
 * @author OUYANG
 *
 */
public class UserSessionListener implements HttpSessionListener{

	public void sessionCreated(HttpSessionEvent event) {
		
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		ServletContext application = session.getServletContext();
		
		if ((session != null) && (session.getAttribute("userSession") != null)) {
			

			String username = ((UserSession) session
					.getAttribute("userSession")).getSysuser().getYhdh();
			HashMap map = (HashMap) application.getAttribute("onlines");
			if (map.containsKey(username)) {
				map.remove(username);
				int c = Integer.parseInt((String) application
						.getAttribute("online"));
				if (c > 0) {
					c--;
				}
				application.setAttribute("onlines", map);
				application.setAttribute("online", String.valueOf(c));
			}
		
			
			
			//session.removeAttribute("userSession");
			// 必须清空当前线程的变量
			//UserContext.clear();
		}/*else {
		
			try {
				throw new ModelAndViewDefiningException(new ModelAndView("./logout.jsp"));
			} catch (ModelAndViewDefiningException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}

}
