package com.sunshine.monitor.system.query.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.bean.UserSession;


@Controller
@RequestMapping(value = "/ks.do", params = "method")
public class KsController {

	
	/**
	 * 跳转跨省查询微服务页面
	 * @param request
	 * @param response
	 * @return
	 * @throws ConfigurationException 
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,
			HttpServletResponse response) throws ConfigurationException {
			PropertiesConfiguration config;
			config = new PropertiesConfiguration("ipport.properties");
			String portxy = config.getString("ks.ipport");//跳转ip
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String sfzmhm = userSession.getSysuser().getSfzmhm();
			String url="redirect:http://"+portxy+"/index.html?sfzmhm="+sfzmhm+"&bkpt=43";
			ModelAndView mv = new ModelAndView(url);
			return mv;
	}
	
}
