package com.sunshine.monitor.comm.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.view.RedirectView;

public class CheckLoginInterceptor implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,FilterChain filterChain) throws IOException, ServletException {		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;		
		String currentURI = request.getRequestURI();
		HttpSession session = request.getSession(false);  
		if (currentURI.indexOf("login.do")==-1&&currentURI.indexOf("ajax.do")==-1&&((null == session) || (null == session.getAttribute("userSession")))) {
		    response.sendRedirect("./logout.jsp");
		    }
		else{
		filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	   
	//protected final Log log = LogFactory.getLog(CheckLoginInterceptor.class); 
	


}
