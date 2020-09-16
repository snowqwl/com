package com.sunshine.monitor.system.query.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.bean.TrafficPolicePassrec;
import com.sunshine.monitor.system.query.service.TrafficPoliceQueryManager;

@Controller
@RequestMapping(value = "/trafficPoliceQuery.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrafficPoliceQueryController {
	
	@Autowired
	@Qualifier("trafficPoliceQueryManager")
	private TrafficPoliceQueryManager trafficPoliceQueryManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@RequestMapping()
	public String findForward(HttpServletRequest request) {
		try {
			request.setAttribute("citylist", this.systemManager
					.getCodes("105000"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "query/trafficPolicePassMain";
	}
	
	@RequestMapping()
	@ResponseBody
	public Map<String,Object> getPassrecList(HttpServletRequest request,TrafficPolicePassrec veh,String page,String rows){
        
		Page p = new Page(Integer.parseInt(page),Integer.parseInt(rows));
		Map<String,Object> result = null;
		try {
			result = this.trafficPoliceQueryManager.getPassrecList(veh, p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping
	public ModelAndView lister(HttpServletRequest request,
			HttpServletResponse response, TrafficPolicePassrec command) {
		ModelAndView mv = null;
		try {
			if ((request.getParameter("kssj") == null)
					|| (request.getParameter("jssj").length() < 1)
					|| (request.getParameter("jssj") == null)
					|| (request.getParameter("kssj").length() < 1)) {
				Common.setAlerts("请输入必填项！", request);
				mv = new ModelAndView("query/trafficPolicePassDetail");
				return mv;
			}
			command.setKdbh(request.getParameter("f_kdbh"));
			command.setHphm(request.getParameter("f_hphm"));
			mv = new ModelAndView("query/trafficPolicePassDetail");
			mv.addObject("hpzllist", this.systemManager.getCodes("030107"));
			mv.addObject("hpyslist", this.systemManager.getCodes("031001"));
			mv.addObject("condition", command);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

}
