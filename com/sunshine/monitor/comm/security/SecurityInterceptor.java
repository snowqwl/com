package com.sunshine.monitor.comm.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
	   
	protected final Log log = LogFactory.getLog(SecurityInterceptor.class); 
	@Autowired
	@Qualifier("jdbcTemplate")
	public JdbcTemplate jdbcTemplate; 
	public boolean preHandle(HttpServletRequest request,   
	            HttpServletResponse response, Object handler) throws Exception {   
		    
		    String currentURI = request.getRequestURI();
		    if (currentURI != null && currentURI != "") {
		    	if ((currentURI.indexOf("login.do") > 0) || (currentURI.indexOf("ajax.do") > 0) 
		    			|| (currentURI.indexOf("comm.do") > 0) || (currentURI.indexOf("code.do") > 0)) {
		    		return true;
		    	}
		    }
		    
	        HttpSession session = request.getSession(false);   
	        if (null == session)   
	        {   
	            throw new ModelAndViewDefiningException(new ModelAndView(new RedirectView("./login.do?method=signOut")));   
	        }   
	        UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
	        SysUser user = userSession.getSysuser();
	        Department dp = userSession.getDepartment();	       
	        if (user == null) {   
	            log.info("#user is logout.............");   
	            throw new ModelAndViewDefiningException(new ModelAndView(new RedirectView("./login.do?method=signOut")));   
	        }   
	        
	        /*判断权限处理*/
	        /*
	        String url="";
	        url += currentURI;
	        int beginIndex = url.lastIndexOf("/");
	        String result = url.substring(beginIndex+1);
	        String sql = " select count(1) from (select t.*,m.name,m.ymdz,m.lb from jm_rolemenu t,jm_menu m where t.cxdh=m.id and t.jsdh in("+user.getRoles()+")) a " +
	        		" where substr(ymdz,0,INSTR(ymdz,'?',1,1)-1)='"+result+"'";
            int num = this.jdbcTemplate.queryForInt(sql);
	        if (num==0) {
        		throw new ModelAndViewDefiningException(new ModelAndView("error/verifyerror")); 
			}
	        */
	        return true;   
	}
}
