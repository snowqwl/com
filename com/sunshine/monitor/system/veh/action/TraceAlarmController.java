package com.sunshine.monitor.system.veh.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.PropertyBean;
import com.sunshine.monitor.system.veh.service.RealManager;

@Controller
@RequestMapping(value="/traceAlarmCtrl.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TraceAlarmController {

	@Autowired
	@Qualifier("realManager")
	private RealManager realManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	/**
	 * 进入实时报警追踪主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findForward(HttpServletRequest request){
		
		request.setAttribute("sign", "add");
		
		return "alarm/realalarm";
	}
	/**
	 * 进入实时报警追踪主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findList(HttpServletRequest request){
		
		request.setAttribute("sign", "list");
		try {
			request.setAttribute("hpzllist", this.systemManager.getCodes("030107"));
			
			request.setAttribute("bjlxlist", systemManager.getCodes("120005"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] temp = new String[10];
		temp[0] = request.getParameter("hphm1").toUpperCase();
		temp[1] = request.getParameter("hphm2").toUpperCase();
		temp[2] = request.getParameter("hphm3").toUpperCase();
		temp[3] = request.getParameter("hphm4").toUpperCase();
		temp[4] = request.getParameter("hphm5").toUpperCase();
		temp[5] = request.getParameter("hphm6").toUpperCase();
		temp[6] = request.getParameter("hphm7").toUpperCase();
		temp[7] = request.getParameter("hphm8").toUpperCase();
		temp[8] = request.getParameter("hphm9").toUpperCase();
		temp[9] = request.getParameter("hphm10").toUpperCase();

		request.setAttribute("hphm1", temp[0]);
		request.setAttribute("hphm2", temp[1]);
		request.setAttribute("hphm3", temp[2]);
		request.setAttribute("hphm4", temp[3]);
		request.setAttribute("hphm5", temp[4]);
		request.setAttribute("hphm6", temp[5]);
		request.setAttribute("hphm7", temp[6]);
		request.setAttribute("hphm8", temp[7]);
		request.setAttribute("hphm9", temp[8]);
		request.setAttribute("hphm10", temp[9]);
		
		return "alarm/realalarmList";
	}
	/**
	 *
	 * 
	 * @param page
	 * @param rows
	 * @param bean
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map queryList(String page, String rows,PropertyBean bean){
		Map map = new HashMap();
		
		map.put("curPage", page);	//页数
		map.put("pageSize", rows);	//每页显示行数
		
		Map result = new HashMap();
		try {
			
			result = this.realManager.findAlarmForMap(map, bean);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
}
