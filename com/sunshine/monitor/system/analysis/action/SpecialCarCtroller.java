package com.sunshine.monitor.system.analysis.action;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.analysis.bean.SpecialCarQueryGz;
import com.sunshine.monitor.system.analysis.service.SpecialCarQueryManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.notice.bean.SysInformation;
import com.sunshine.monitor.system.notice.bean.SysInformationreceive;
import com.sunshine.monitor.system.query.service.SpecialCarManager;

@Controller
@RequestMapping(value = "/specarCtr.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpecialCarCtroller {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired(required=true)
	private SpecialCarQueryManager specialCarQueryManager;


	/**
	 * 特殊车辆库查询首页
	 * 
	 * @return
	 */
	@RequestMapping
	public ModelAndView findForward(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();		
		try {
			List<Code> scbdList = this.systemManager.getCodes("200003");
			request.setAttribute("scbdList", scbdList);
			mv.setViewName("analysis/specialCarQueryMain");		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 特殊车辆库比对规则首页
	 * 
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardSpecialCarRuleMain(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();		
		try {
			List<Code> scbdList = this.systemManager.getCodes("200003");
			request.setAttribute("scbdList", scbdList);
			mv.setViewName("analysis/specialCarRuleMain");		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 特殊车辆库比对规则详细页面
	 * 
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardSpecialCarRuleWin(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();		
		try {
			List<Code> scbdList = this.systemManager.getCodes("200003");
			List cityList = this.systemManager.getCityList();
			request.setAttribute("scbdList", scbdList);
			request.setAttribute("cityList", cityList);
			request.setAttribute("sign",request.getParameter("sign"));
			request.setAttribute("gzbh",request.getParameter("gzbh"));
			mv.setViewName("analysis/specialCarRuleWin");		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	@ResponseBody
	@RequestMapping
	public List getKd(HttpServletRequest request,
			HttpServletResponse response){
		String dwdm = request.getParameter("dwdm");
		List kdList = null;
		try {
			kdList = this.systemManager.getKd(dwdm.substring(0,4).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kdList;
	}
	
	@ResponseBody
	@RequestMapping
	public Object saveBdgz(HttpServletRequest request,
			HttpServletResponse response, SpecialCarQueryGz command) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			SpecialCarQueryGz information = command;
			String xh = this.specialCarQueryManager.saveBdgz(information);
			if (xh != null) {
				_map.put("msg", "保存成功!");
			} else {
				_map.put("msg", "保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	@ResponseBody
	@RequestMapping
	public Object querySpecialCarRuleList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {						
				Map<String, Object> conditions = Common.getParamentersMap(request);
				result = this.specialCarQueryManager.querySpecialCarRuleList(conditions);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping
	public Object getSpecialCarRule(HttpServletRequest request,
			HttpServletResponse response) {
		SpecialCarQueryGz result = null;
		try {						
				String gzbh = request.getParameter("gzbh");
				result = this.specialCarQueryManager.getSpecialCarRule(gzbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
