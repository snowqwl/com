package com.sunshine.monitor.system.query.action;

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

import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.service.SpecialCarManager;

@Controller
@RequestMapping(value = "/specialCar.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpecialCarQueryCtroller {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	private SpecialCarManager specialCarManager;

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
			Map<String, List<Code>> map = this.getCodeList();
			mv.setViewName("query/specialCarMain");
			mv.addObject("codemap", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	@RequestMapping
	@ResponseBody
	public Object querySpecialCarList(HttpServletRequest request) {
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			Map map = this.specialCarManager.querySpecialList(conditions);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping
	@ResponseBody
	public Object querySpecialCarDetail() {
		return null;
	}
	
	@RequestMapping
	@ResponseBody
	public Object getConditionList(HttpServletRequest request) {
		List<Code> list = null;
		try {
			String param = request.getParameter("param");
			if("bkdl".equals(param)) {
				list = this.systemManager.getCodes("120019");
			}
			if("bjfs".equals(param)) {
				list = this.systemManager.getCodes("130000");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 代码列表
	 * 
	 * @return
	 */
	private Map<String, List<Code>> getCodeList() throws Exception {
		Map<String, List<Code>> map = null;
		try {
			// 布控列表
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			List<Code> bklbList = this.systemManager.getCodes("120005");
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			List<Code> ywztList = this.systemManager.getCodes("120008");

			// 新增布控
			List<Code> cllxList = this.systemManager.getCodes("030104");
			List<Code> csysList = this.systemManager.getCodes("030108");
			List<Code> hpysList = this.systemManager.getCodes("031001");
			List<Code> bjfsList = this.systemManager.getCodes("130000");

			map = new HashMap<String, List<Code>>();
			map.put("bkdlList", bkdlList);
			map.put("bklbList", bklbList);
			map.put("hpzlList", hpzlList);
			map.put("ywztList", ywztList);
			map.put("cllxList", cllxList);
			map.put("csysList", csysList);
			map.put("hpysList", hpysList);
			map.put("bjfsList", bjfsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
