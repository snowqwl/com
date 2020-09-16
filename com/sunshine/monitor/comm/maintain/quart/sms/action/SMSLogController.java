package com.sunshine.monitor.comm.maintain.quart.sms.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.maintain.quart.sms.service.SMSLogManager;

@Controller
@RequestMapping(value = "/smslog.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SMSLogController {

	@Autowired
	private SMSLogManager smsLogManager;

	private String smslog_main = "maintain/smsloglist";

	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView(this.smslog_main);
	}

	@ResponseBody
	@RequestMapping
	public Object getSmslogList(HttpServletRequest request,
			@RequestParam int rows, @RequestParam int page,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String order) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kssj", request.getParameter("kssj"));
		params.put("jssj", request.getParameter("jssj"));
		params.put("rows", rows);
		params.put("page", page);
		params.put("sort", sort);
		params.put("order", order);
		Map<String, Object> result = this.smsLogManager
				.querySmslogListByPage(params);
		return result;
	}
}
