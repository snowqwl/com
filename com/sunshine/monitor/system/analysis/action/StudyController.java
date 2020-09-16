package com.sunshine.monitor.system.analysis.action;

import java.util.HashMap;
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
import com.sunshine.monitor.system.analysis.service.StudyManager;

@Controller
@RequestMapping(value = "/study.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StudyController {

	@Autowired
	@Qualifier("studyManager")
	private StudyManager studyManager;

	/**
	 * 敏感卡口设置主页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	public ModelAndView forwardGate(HttpServletRequest request)
			throws Exception {
		return new ModelAndView("analysis/sensitivegatemain");
	}

	/**
	 * 敏感时段设置主页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	public ModelAndView forwardTime(HttpServletRequest request)
			throws Exception {
		return new ModelAndView("analysis/sensitivetimemain");
	}

	/**
	 * 频次规则设置主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardRule(HttpServletRequest request) {
		return new ModelAndView("analysis/raterulemain");
	}

	/**
	 * 新增敏感卡口组
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object insertGateGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		int code = 0;
		String msg = null;
		try {
			String gname = request.getParameter("gname");
			String kdbhs = request.getParameter("kdbh");
			code = this.studyManager.insertGateGroup(gname, kdbhs);
			msg = "保存成功";
		} catch (Exception e) {
			code = 0;
			msg = "保存失败";
			e.printStackTrace();
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;

	}

	/**
	 * 更新敏感卡口组
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object updateGateGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		int code = 0;
		String msg = null;
		try {
			String gid = request.getParameter("gid");
			String gname = request.getParameter("gname");
			String kdbhs = request.getParameter("kdbh");
			code = this.studyManager.updateGateGroup(gid, gname, kdbhs);
			msg = "修改成功";
		} catch (Exception e) {
			code = 0;
			msg = "修改失败";
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	@RequestMapping
	@ResponseBody
	public Object deleteGateGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		int code = 0;
		String msg = null;
		try {
			String gid = request.getParameter("gid");
			this.studyManager.deleteGateGroup(gid);
			code = 1;
			msg = "删除成功";
		} catch (Exception e) {
			code = 0;
			msg = "删除失败";
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	/**
	 * 查询敏感卡口组列表
	 * 
	 * @param request
	 * @param page
	 * @param rows
	 * @param gname
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryGateGroupList(HttpServletRequest request, String page,
			String rows, String gname) {
		try {
			// 预警规则--敏感卡口
			String rule = request.getParameter("param");
			if ("rule".equals(rule)) {
				Map result = this.studyManager.queryGateGroupList(null, null);
				return result.get("rows");
			}
			Map filter = new HashMap();
			filter.put("curPage", page); // 页数
			filter.put("pageSize", rows); // 每页显示行数
			Map map = this.studyManager.queryGateGroupList(filter, gname);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据敏感卡口组编号查询卡口列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryGateListByGid(HttpServletRequest request) {
		try {
			String gid = request.getParameter("gid");
			return this.studyManager.queryGateListByGid(gid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 新增敏感时段
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object insertTimeGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map result = new HashMap();
		int code = 0;
		String msg = "";
		try {
			Map params = Common.getParamentersMap(request);
			code = this.studyManager.insertTimeGroup(params);
			if (code == 0) {
				msg = "已存在相同名称的记录";
			} else {
				msg = "保存成功";
			}
		} catch (Exception e) {
			code = 0;
			msg = "发生异常";
			e.printStackTrace();
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	@RequestMapping
	@ResponseBody
	public Object updateTimeGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map result = new HashMap();
		int code = 0;
		String msg = "";
		try {
			Map params = Common.getParamentersMap(request);
			code = this.studyManager.updateTimeGroup(params);
			if (code == 0) {
				msg = "已存在其他相同名称的记录";
			} else {
				msg = "保存成功";
			}
		} catch (Exception e) {
			code = 0;
			msg = "发生异常";
			e.printStackTrace();
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	@RequestMapping
	@ResponseBody
	public Object deleteTimeGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map result = new HashMap();
		int code = 1;
		String msg = "";
		try {
			String tid = request.getParameter("tid");
			this.studyManager.deleteTimeGroup(tid);
			msg = "删除成功";
		} catch (Exception e) {
			code = 0;
			msg = "发生异常";
			e.printStackTrace();
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	/**
	 * 查询敏感时段列表
	 * 
	 * @param request
	 * @param page
	 * @param rows
	 * @param tname
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryTimeGroupList(HttpServletRequest request, String page,
			String rows, String tname) {
		try {
			// 预警规则--敏感时段
			String rule = request.getParameter("param");
			if ("rule".equals(rule)) {
				Map result = this.studyManager.queryTimeGroupList(null, null);
				return result.get("rows");
			}
			Map filter = new HashMap();
			filter.put("curPage", page); // 页数
			filter.put("pageSize", rows); // 每页显示行数
			Map map = this.studyManager.queryTimeGroupList(filter, tname);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取具体敏感时段
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object getTimeGroupByTid(HttpServletRequest request) {
		String tid = request.getParameter("tid");

		return this.studyManager.getTimeGroupByTid(tid);
	}

	/**
	 * 查询频次规则
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryRuleList(HttpServletRequest request, String page,
			String rows) {
		try {
			String rule = request.getParameter("param");
			if ("rule".equals(rule)) {
				Map result = this.studyManager.queryRuleList(null, null);
				return result.get("rows");
			}
			Map filter = new HashMap();
			filter.put("curPage", page); // 页数
			filter.put("pageSize", rows); // 每页显示行数
			Map params = Common.getParamentersMap(request);
			Map map = this.studyManager.queryRuleList(filter, params);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新频次规则
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object updateRule(HttpServletRequest request) {
		Map result = new HashMap();
		int code = 0;
		String msg = "";
		try {
			Map params = Common.getParamentersMap(request);
			code = this.studyManager.updateRule(params);
			if (code == 0) {
				msg = "已存在其他相同的频次规则";
			} else {
				msg = "保存成功";
			}
		} catch (Exception e) {
			code = 0;
			msg = "发生异常";
			e.printStackTrace();
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}
	
	/**
	 * 删除频次规则
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object deleteRule(HttpServletRequest request,
			HttpServletResponse response) {
		Map result = new HashMap();
		int code = 1;
		String msg = "";
		try {
			String rid = request.getParameter("rid");
			code = this.studyManager.deleteRule(rid);
			if(code == 1) {
				msg = "删除成功";
			} else {
				msg = "不存在该记录";
			}
		} catch (Exception e) {
			code = 0;
			msg = "发生异常";
			e.printStackTrace();
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}
}
