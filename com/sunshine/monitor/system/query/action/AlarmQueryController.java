package com.sunshine.monitor.system.query.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

@Controller
@RequestMapping(value = "/alarmquery.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlarmQueryController {

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private GateManager gateManager;

	@Autowired
	private VehAlarmManager alarmManager;

	@Autowired
	private SuspinfoManager suspinfoManager;

	private String alarmQueryMain = "query/queryalarmmain";
	
	private String alarmStQueryMain = "query/querystalarmmain";

	private String queryAlarmDetail = "query/queryalarmdetail";
	
	private String confirmOutMain = "query/confirmOutMain";

	/**
	 * 跳转报警信息查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardAlarmQueryMain(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, List<Code>> map = null;
		ModelAndView mv = new ModelAndView();
		try {
			map = new HashMap<String, List<Code>>();
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			//List<Code> bjdlList = this.systemManager.getCodesByDmsm("120019","1", 4);
			List<Code> bjdlList = this.systemManager.getCodes("120019");
			List<Code> qrztList = this.systemManager.getCodes("120014");
			List<Code> xxlyList = this.systemManager.getCodes("120012");
			List<Code> sfxdzlList = this.systemManager.getCodes("130030");
			List<Code> sffkList = this.systemManager.getCodes("130031");
			List<Code> sfljList = this.systemManager.getCodes("130032");
			// 报警小类
			List<Code> bklxList = this.systemManager.getCodes("120005");
			//List<CodeGate> gateList = this.gateManager.getAllGates();
			
			//城市列表
			//List<Code> cityList = this.systemManager.getCodes("105000");
			

			map.put("hpzlList", hpzlList);
			map.put("bjdlList", bjdlList);
			map.put("qrztList", qrztList);
			map.put("xxlyList", xxlyList);
			map.put("sfxdzlList", sfxdzlList);
			map.put("sffkList", sffkList);
			map.put("sfljList", sfljList);
			map.put("bklxList", bklxList);
			//map.put("cityList", cityList);

			//mv.addObject("gateList", gateList);
			mv.addObject("codemap", map);
			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String glbm = userSession.getDepartment().getGlbm();

			if (StringUtils.isNotBlank(glbm) && glbm.indexOf("430000") == 0) {			
				mv.setViewName(alarmStQueryMain);
			} else {
				List<AdministrativeDivision> divisionsList = this.systemManager.queryAdministrativeAdvisions();
				mv.addObject("divisionsList", divisionsList);
				mv.setViewName(alarmQueryMain);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
		}
	
	/**
	 * 跳转确认超时查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardConfirmOutMain(HttpServletRequest request,
			HttpServletResponse response,String param) {
		Map<String, List<Code>> map = null;
		ModelAndView mv = new ModelAndView();
		try {
			map = new HashMap<String, List<Code>>();
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			List<Code> bjdlList = this.systemManager.getCodesByDmsm("120019",
					"1", 4);
			//List<Code> qrztList = this.systemManager.getCodes("120014");
			List<Code> xxlyList = this.systemManager.getCodes("120012");
			List<Code> sfxdzlList = this.systemManager.getCodes("130030");
			List<Code> sffkList = this.systemManager.getCodes("130031");
			List<Code> sfljList = this.systemManager.getCodes("130032");
			// 报警小类
			List<Code> bklxList = this.systemManager.getCodes("120005");
			List<CodeGate> gateList = this.gateManager.getAllGates();
			
			List<AdministrativeDivision> divisionsList = this.systemManager.queryAdministrativeAdvisions();

			map.put("hpzlList", hpzlList);
			map.put("bjdlList", bjdlList);
			//map.put("qrztList", qrztList);
			map.put("xxlyList", xxlyList);
			map.put("sfxdzlList", sfxdzlList);
			map.put("sffkList", sffkList);
			map.put("sfljList", sfljList);
			map.put("bklxList", bklxList);

			if(param!=null && "count".equals(param)) {
				mv.addObject("initparam", param);
			}
			mv.addObject("divisionsList", divisionsList);
			mv.addObject("gateList", gateList);
			mv.addObject("codemap", map);
			mv.setViewName(confirmOutMain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 报警信息查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAlarmList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
			.getSessionAttribute(request, "userSession");
			
			result = this.alarmManager.getAlarmList(conditions,userSession);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 确认超时查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryConfirmOutList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			map = this.alarmManager.getConfirmOutList(conditions, userSession);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据号牌号码报警信息查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAlarListByHphm(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
			.getSessionAttribute(request, "userSession");
			result = this.alarmManager.getAlarmListByHphm(conditions, userSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 报警详细信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAlarmDetail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			String bjxh = request.getParameter("bjxh");
			VehAlarmrec alarm = this.alarmManager.getAlarmsignDetail(bjxh);
			if ((alarm.getBjdl() != null) ) {
				if (alarm != null) {
					List<VehAlarmCmd> cmdList = this.alarmManager.getCmdlist(
							bjxh, false);
					if ((cmdList != null) && (cmdList.size() == 0)) {
						cmdList = null;
					}
					List<VehAlarmLivetrace> livetraceList = this.alarmManager
							.getLivetraceList(bjxh);
					if ((livetraceList != null) && (livetraceList.size() == 0)) {
						livetraceList = null;
					}
					List<VehAlarmHandled> handledList = this.alarmManager
							.queryAlarmHandleList(bjxh);
					if ((handledList != null) && (handledList.size() == 0)) {
						handledList = null;
					}

					VehSuspinfo susp = this.suspinfoManager.getSuspDetail(alarm
							.getBkxh());
					List<VehAlarmHandled> feedbackList = this.alarmManager.queryAlarmhandleConfirmed(bjxh);
					// request.setAttribute("susp", susp);
					result = new HashMap<String, Object>();
					result.put("susp", susp);
					result.put("alarm", alarm);
					result.put("historyCmdList", cmdList);
					result.put("livetraceList", livetraceList);
					result.put("handled", handledList);
					result.put("feedbackList", feedbackList);
				}
			// 管控类查询有限制，只能是本人才能查询
			} else if (("3".equals(alarm.getBjdl())) && (alarm != null)) {
				VehSuspinfo susp = this.suspinfoManager.getSuspDetail(alarm
						.getBkxh());
				
				
				if ((susp.getBkr().equals(userSession.getSysuser().getYhdh()))
						&& (susp.getBkjg().equals(userSession.getSysuser()
								.getGlbm()))) {
					result = new HashMap<String, Object>();
					result.put("susp", susp);
					result.put("alarm", alarm);
					result.put("historyCmdList", null);
					result.put("livetraceList", null);
					result.put("handled", null);
					result.put("feedbackList", null);
				}
				else{
					result = new HashMap<String, Object>();
					return result;
		}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 某地市的报警详细信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryCityAlarmDetail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			String bjxh = request.getParameter("bjxh");
			String city = request.getParameter("city");
			String cityname = this.systemManager.getCodeValue("105000", city);
			VehAlarmrec alarm = this.alarmManager.getCityAlarmsignDetail(bjxh, cityname);
			if ((alarm.getBjdl() != null) ) {
				if (alarm != null) {
					List<VehAlarmCmd> cmdList = this.alarmManager.getCityCmdlist(
							bjxh, cityname);
					if ((cmdList != null) && (cmdList.size() == 0)) {
						cmdList = null;
					}
					List<VehAlarmLivetrace> livetraceList = this.alarmManager
							.getCityLivetraceList(bjxh,cityname);
					if ((livetraceList != null) && (livetraceList.size() == 0)) {
						livetraceList = null;
					}
					List<VehAlarmHandled> handledList = this.alarmManager
							.queryCityAlarmHandleList(bjxh,cityname);
					if ((handledList != null) && (handledList.size() == 0)) {
						handledList = null;
					}

					VehSuspinfo susp = this.suspinfoManager.getCitySuspDetail(alarm
							.getBkxh(),cityname);
					List<VehAlarmHandled> feedbackList = this.alarmManager.queryCityAlarmhandleConfirmed(bjxh,cityname);
					// request.setAttribute("susp", susp);
					result = new HashMap<String, Object>();
					result.put("susp", susp);
					result.put("alarm", alarm);
					result.put("historyCmdList", cmdList);
					result.put("livetraceList", livetraceList);
					result.put("handled", handledList);
					result.put("feedbackList", feedbackList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
