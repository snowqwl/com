package com.sunshine.monitor.system.veh.action;

import static com.sunshine.monitor.comm.util.Common.putReParamToHttpP;
import static com.sunshine.monitor.comm.util.Common.sendHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.sunshine.monitor.system.gate.bean.CodeDirect;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/gdpassquery.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GdPassrecController {
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	private LogManager logManager;


	private Logger log = LoggerFactory.getLogger(GdPassrecController.class);

	/**
	 * 进入过车信息查询主页
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPassMain(HttpServletRequest request,
										HttpServletResponse response) {
		try {
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			request.setAttribute("fzjg", this.systemManager.getParameter(
					"fzjg", request));
		} catch (Exception e) {
			e.printStackTrace();
		}

//		String glbm = userSession.getDepartment().getGlbm();

//		if (StringUtils.isNotBlank(glbm) && glbm.indexOf("440000") == 0||!"44".equals(glbm.substring(0,2))) {

			return new ModelAndView("gdquery/querystpassmain");
//		} else
//			return new ModelAndView("gdquery/querypassmain");
	}

	/**
	 * 卡口的方向
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getDirectListByKdbh(HttpServletRequest request,
									  HttpServletResponse response) {
		String kdbh = request.getParameter("kdbh");
		StringBuffer sb = new StringBuffer();
		List<CodeDirect> list = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			if ((kdbh != null) && (kdbh.length() > 0)) {
				Map map = sendHttpClient("/api/gdservice/vehicle/passRecService/getDirectListByKdbh", putReParamToHttpP(conditions));
				list = (List<CodeDirect>) map.get("data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb.delete(0, sb.length());
			sb.append("{}");
		}
		return list;
	}

	/**
	 * 查询提示：提示用户查询数量过大是否继续查询
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryTips(HttpServletRequest request,
							HttpServletResponse response) throws Exception {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			result = sendHttpClient("/api/gdservice/vehicle/passRecService/queryTips", putReParamToHttpP(conditions));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.get("data");
	}


	@RequestMapping
	public ModelAndView lister(HttpServletRequest request,
							   HttpServletResponse response, VehPassrec command) {
		ModelAndView mv = null;
		try {
			if ((request.getParameter("kssj") == null)
					|| (request.getParameter("jssj").length() < 1)
					|| (request.getParameter("jssj") == null)
					|| (request.getParameter("kssj").length() < 1)) {
				Common.setAlerts("请输入必填项！", request);
				mv = new ModelAndView("gdquery/querystpassmain");
				return mv;
			}
			mv = new ModelAndView("gdquery/vehpassrecdetail");

			Map<String, Object> conditions = Common.getParamentersMap(request);
//			request.setAttribute("hpzllist", this.systemManager.getCodes("030107"));
			mv.addObject("fzjg", this.systemManager.getParameter("fzjg",
					request));
			mv.addObject("cds", conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@ResponseBody
	@RequestMapping
	public Object exportQueryPasslist(HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			result = sendHttpClient("/api/gdservice/vehicle/passRecService/exportQueryPasslist", putReParamToHttpP(conditions));

			List list = (List) result.get("rows");
			// 是否有数据判断
			if (list == null || list.size() == 0) {
				result.put("counts", "0");
			} else {
				result.put("counts", list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询过车列表
	 *
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryPasslist(HttpServletRequest request, HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		Map<String, Object> resultMap = new HashMap<String,Object>();
		Map<String, Object> conditions = new HashMap<String,Object>();
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		SysUser user = userSession.getSysuser();
		String cznr = "";
		String msg = "";
		long total = 0;
		String hphm = "";
		String hpzl = "";
		String kssj = "";
		String jssj = "";
		String sfzhm = "";
		String yhbm = "";
		String yhmc = "";
		Log log2 = new Log();
		if(user != null){
			log2.setGlbm(user.getGlbm());
			log2.setYhdh(user.getYhdh());
		}
		log2.setIp(request.getRemoteAddr());
		log2.setCzlx("5510");
		log2.setSqr(user.getYhmc());
		log2.setSfgccx("1");
		log2.setJh(user.getJh());
		try {
			sfzhm = userSession.getSysuser().getSfzmhm();
			yhbm = userSession.getSysuser().getBmmc();
			yhmc = userSession.getSysuser().getYhmc();
			conditions = Common.getParamentersMap(request);
			conditions.put("pageIndex", conditions.get("page"));
			conditions.put("pageSize", conditions.get("rows"));
			conditions.put("sfzmhm", sfzhm);
			conditions.remove("kdmc");
			conditions.remove("sort");
			conditions.remove("order");
			conditions.remove("page");
			conditions.remove("pageSign");
			conditions.remove("rows");
			if(conditions.get("hphm") != null) {
				hphm = conditions.get("hphm") + "";
				msg = "号牌号码：" + hphm + "，";
				log2.setHphm(hphm);
			}
			if(conditions.get("hpzl") != null) {
				hpzl = conditions.get("hpzl") + "";
				msg += "号牌种类：" + hpzl + "，";
			}
			if(conditions.get("kssj") != null) {
				kssj = conditions.get("kssj") + "";
				msg += "开始时间：" + kssj + "，";
				log2.setKssj(kssj);
			}
			if(conditions.get("jssj") != null) {
				jssj = conditions.get("jssj") + "";
				msg += "结束时间：" + jssj;
				log2.setJssj(jssj);
			}
			result = sendHttpClient("", putReParamToHttpP(conditions));
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			if(result.get("rows") != null && result.size() > 0) {
				list = (List<Map<String, Object>>) result.get("rows");
				for(Map<String, Object> map : list){
					map.put("kdmc", map.get("kkmc"));
					if(map.get("tp1") == null)
						map.put("tp1", "");
					if(map.get("tp2") == null)
						map.put("tp2", "");
					if(map.get("tp3") == null)
						map.put("tp3", "");
				}
				total = Long.parseLong(String.valueOf(result.get("totalCount")));
			}
			// 是否有数据判断
			resultMap.put("rows", list);
			if (list == null || list.size() == 0) {
				resultMap.put("total", "0");
			} else {
				resultMap.put("total", total);
			}
			cznr = "广东过车数据查询成功，查询用户："+yhmc+ "，身份证：" + sfzhm + "，所属部门：" + yhbm
					+"，查询条件：" + msg + "，返回总数：" + total;
			log2.setCxjg("成功");
		} catch (Exception e) {
			log.error("湖南查询广东过车数据失败，失败信息：{}", e.getMessage());
			cznr = "广东过车数据查询失败，查询用户："+yhmc+ "，身份证：" + sfzhm + "，所属部门：" + yhbm
					+"，查询条件：" + msg + "，返回总数：" + total;
			log2.setCxjg("失败");
			e.printStackTrace();
		}
		log2.setCznr(cznr);
		saveLog(log2);
		return resultMap;
	}
	
	private void saveLog(Log log){
		try{
			logManager.saveLog(log);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(params = "method=getGateTreeByGlbm")
	@ResponseBody
	public List<Map<String,Object>> getGateTreeByGlbm(HttpServletRequest request) throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();

		try {
			///判断是否是省厅版/////
			Map<String, Object> conditions = Common.getParamentersMap(request);
			Map map = sendHttpClient("/api/gdservice/vehicle/passRecService/getGateTreeByGlbm", putReParamToHttpP(conditions));
			list = (List) map.get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据条件展示卡口菜单树(显示方向)
	 */
	@RequestMapping(params = "method=getGateTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getGateTreeByFilter(HttpServletRequest request) {
		List<Map<String,Object>> re = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			Map map = sendHttpClient("/api/gdservice/vehicle/passRecService/getGateTreeByFilter", putReParamToHttpP(conditions));
			re = (List) map.get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	@ResponseBody
	@RequestMapping
	public Object getSuspinfo(HttpServletRequest request) {
		Map map = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			map = sendHttpClient("/api/gdservice/vehicle/passRecService/getSuspinfo", putReParamToHttpP(conditions));

		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return map.get("data");
	}

}
