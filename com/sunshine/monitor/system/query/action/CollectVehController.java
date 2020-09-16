package com.sunshine.monitor.system.query.action;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.service.CollectVehManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

@Controller
@RequestMapping(value = "/collectveh.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CollectVehController {

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private CollectVehManager collectVehManager;
	
	private String collectVehMain = "query/collectvehmain";
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 跳转关注车辆查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardMain(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			request.setAttribute("hpzlList", hpzlList);
			mv.setViewName(collectVehMain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	@RequestMapping
	public ModelAndView forwardWin(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			String hphm = URLDecoder.decode(request.getParameter("hphm"),"UTF-8");
			String hpzl = request.getParameter("hpzl");
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			
			request.setAttribute("hpzlList", hpzlList);
			if(!"".equals(hphm) && !"".equals(hpzl)){ 
				request.setAttribute("hphm", hphm);
				request.setAttribute("hpzl", hpzl);
			}
			mv.setViewName("query/collectwin");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 判断是否超过关注上限20条
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object checkMaxVehCount(String page, String rows, VehSuspinfo info,HttpServletRequest request) {
		int result	= 0;
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			int temp  = this.collectVehManager.checkMaxCount(userSession.getSysuser().getJh());
			if(temp>20){
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询当前用户所关注的车辆列表失败");
		}
		return result;
	}
	

	/**
	 * 查询当前用户所关注的车辆列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryList(String page, String rows, VehSuspinfo info,HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			Page p = new Page(Integer.parseInt(page),Integer.parseInt(rows));
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			String conSql = " and jh='"+userSession.getSysuser().getJh()+"'";
			
			result = this.collectVehManager.getCollectVehMapForFilter(p, info, conSql);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询当前用户所关注的车辆列表失败");
		}
		return result;
	}
	
	/**
	 * 页面手动录入新增关注车辆
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object save(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			String hphm = request.getParameter("hphm");
			String hpzl = request.getParameter("hpzl");
			String xxly = request.getParameter("xxly");
			String bz = request.getParameter("bz");
			
			VehSuspinfo info = new VehSuspinfo();
			info.setHphm(hphm);
			info.setHpzl(hpzl);
			info.setXxly(xxly); 
			info.setBz(bz);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			//判断号牌号码是否已经关注
			int count = collectVehManager.checkHphm(userSession.getSysuser().getJh(),hphm, hpzl);
			if(count<=0){			
				collectVehManager.saveCollectVeh(userSession.getSysuser(), info);
				return 1;
			}else{
				return 2;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}
	}
	
	/**
	 * 页面直接增加车辆关注
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveCollectVeh(HttpServletRequest request,HttpServletResponse response) {
		try {
			String hphm = URLDecoder.decode(request.getParameter("hphm"),"UTF-8");
			String hpzl = request.getParameter("hpzl");
			String xxly = URLDecoder.decode(request.getParameter("xxly"),"UTF-8");
			
			VehSuspinfo info = new VehSuspinfo();
			info.setHphm(hphm);
			info.setHpzl(hpzl);
			info.setXxly(xxly); 
			info.setBz("");
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			collectVehManager.saveCollectVeh(userSession.getSysuser(), info);
			return Common.messageBox("关注成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}
	}
	
	/**
	 * 设置关注车辆状态为无效
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object editCollectVeh(HttpServletRequest request,HttpServletResponse response) {
		try {
			String hphm = URLDecoder.decode(request.getParameter("hphm"),"UTF-8");
			String hpzl = request.getParameter("hpzl");
			String status = "1";
			
			collectVehManager.editCollectVeh(hphm,hpzl,status);
			return Common.messageBox("取消关注！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}
	}
	
	/**
	 * 修改关注车辆信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object edit(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			String hphm = request.getParameter("hphm");
			String hpzl = request.getParameter("hpzl");
			String xxly = request.getParameter("xxly");
			String bz = request.getParameter("bz");
			
			VehSuspinfo info = new VehSuspinfo();
			info.setHphm(hphm);
			info.setHpzl(hpzl);
			info.setXxly(xxly); 
			info.setBz(bz);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			//判断号牌号码是否已经关注
			int count = collectVehManager.update(userSession.getSysuser(), info);
			return count;			
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}
	}
	
	@ResponseBody
	@RequestMapping
	public Object getVehCollectByHphm(HttpServletRequest request,HttpServletResponse response) {
		try {
			String hphm = URLDecoder.decode(request.getParameter("hphm"),"UTF-8");
			String hpzl = request.getParameter("hpzl");
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			
			VehSuspinfo veh = collectVehManager.findVehCollect(userSession.getSysuser().getJh(),hphm,hpzl);
			return veh;
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}
	}
	
	/**
	 * 批量取消关注
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object batchDeleteHphm(HttpServletRequest request,HttpServletResponse response) {
		try {
			String hphms = request.getParameter("hphms");
			String[] tempStr = hphms.split("/");
			String status = "1";
			for(int i =0;i<tempStr.length;i++){
				String[] temp =tempStr[i].split(",");
				String hphm=temp[0];
				String hpzl=temp[1];
				collectVehManager.editCollectVeh(hphm,hpzl,status);
			}
			
			return Common.messageBox("取消关注！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}
	}
}
