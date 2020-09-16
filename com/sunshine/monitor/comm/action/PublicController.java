package com.sunshine.monitor.comm.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.service.PublicManager;
import com.sunshine.monitor.comm.util.Common;

@Controller
@RequestMapping(value="/public.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PublicController {

	@Autowired
	private PublicManager publicManager;
	
	@RequestMapping
	public ModelAndView CityReport(HttpServletRequest request,
			HttpServletResponse response) {
		/*
		try {
			List queryList = this.publicManager.CityReport();
			request.setAttribute("queryList", queryList);
		} catch (Exception e) {
			Common.setAlerts("无法连接！", request);
		}
		*/
		return new ModelAndView("manager/cityReportMain");
	}
}
