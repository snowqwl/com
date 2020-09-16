package com.sunshine.monitor.comm.quart.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.quart.bean.JobEntity;
import com.sunshine.monitor.comm.quart.service.ScheduleJobManager;
import com.sunshine.monitor.comm.util.EasyuiDateFormat;

@Controller
@RequestMapping(value = "/schedule.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScheduleController {

	@Autowired
	private ScheduleJobManager scheduleJobManager;

	private String schedule_main_page = "schedule/schedulemain";

	@RequestMapping
	public ModelAndView forward() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName(this.schedule_main_page);
		return mv;
	}

	/**
	 * 查询后台任务列表
	 * 
	 * @param request
	 * @param rows
	 * @param page
	 * @param sort
	 * @param order
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryScheduleList(HttpServletRequest request,
			@RequestParam String rows, @RequestParam String page,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String order,
			HttpServletResponse response) {
		Map<String, Object> map = null;
		try {
			Map<String, Object> params = getParamentersMap(request);
			map = this.scheduleJobManager.queryScheduleList(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 添加定时调动任务
	 * 
	 * @param request
	 * @param rows
	 * @param page
	 * @param sort
	 * @param order
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object addSchedule(HttpServletRequest request, JobEntity jobEntity) {
		Map<String, String> map = null;
		try {
			map = this.scheduleJobManager.addSchedule(jobEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 永久删除任务
	 * 
	 * @param request
	 * @param jobEntity
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object deleteSchedule(HttpServletRequest request) {
		Map<String, String> map = null;
		try {
			String rwbh = request.getParameter("rwbh");
			map = this.scheduleJobManager.deleteScheduleJobByrwbh(rwbh);
		} catch (Exception e) {
			map.put("code", "0");
			map.put("msg", "任务删除失败");
			e.printStackTrace();
			return map;
		}
		return map;
	}

	/**
	 * 启动任务
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object startSchedule(HttpServletRequest request) {
		Map<String, String> map = null;
		try {
			String rwbh = request.getParameter("rwbh");
			map = this.scheduleJobManager.startScheduleJob(rwbh);
		} catch (Exception e) {
			map.put("code", "0");
			map.put("msg", "任务启动失败");
			e.printStackTrace();
			return map;
		}
		return map;
	}

	/**
	 * 任务停止
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object stopSchedule(HttpServletRequest request) {
		Map<String, String> map = null;
		try {
			String rwbh = request.getParameter("rwbh");
			map = this.scheduleJobManager.stopScheduleJob(rwbh);
		} catch (Exception e) {
			map.put("code", "0");
			map.put("msg", "任务停止失败");
			e.printStackTrace();
			return map;
		}
		return map;
	}

	/**
	 * 获取任务
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getScheduleByRwbh(HttpServletRequest request) {
		JobEntity jobEntity = null;
		try {
			String rwbh = request.getParameter("rwbh");
			jobEntity = this.scheduleJobManager.getJobEntityByRwbh(rwbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobEntity;
	}

	@ResponseBody
	@RequestMapping
	public Object updateSchedule(HttpServletRequest request, JobEntity jobEntity) {
		Map<String, String> map = null;
		try {
			map = this.scheduleJobManager.updateScheduleJob(jobEntity);
		} catch (ObjectAlreadyExistsException e) {
			map = new HashMap<String, String>();
			map.put("code", "0");
			map.put("msg", "请稍后再更新");
			e.printStackTrace();
		} catch (SchedulerException e) {
			map = new HashMap<String, String>();
			map.put("code", "0");
			map.put("msg", "请稍后再更新");
			e.printStackTrace();
		} catch (Exception e) {
			map = new HashMap<String, String>();
			map.put("code", "0");
			map.put("msg", "任务更新失败");
			e.printStackTrace();
			return map;
		}
		return map;
	}

	/**
	 * 查询请求Map参数
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getParamentersMap(HttpServletRequest request)
			throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Enumeration<String> enumes = request.getParameterNames();
		while (enumes.hasMoreElements()) {
			String param = enumes.nextElement();
			if (param != null && param.length() > 0
					&& !"method".equalsIgnoreCase(param)) {
				Object value = request.getParameter(param);
				if (value != null && !"".equals(value)) {
					if (param.indexOf("kssj") != -1
							|| param.indexOf("jssj") != -1) {
						// 处理时间格式
						paraMap.put(param, EasyuiDateFormat
								.getStandardDate((String) value));
					} else {
						paraMap.put(param, value);
					}
				}
			}
		}
		return paraMap;
	}
}
