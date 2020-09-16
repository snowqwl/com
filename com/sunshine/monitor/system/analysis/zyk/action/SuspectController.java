package com.sunshine.monitor.system.analysis.zyk.action;

import java.util.HashMap;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.core.util.FileUtil;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.analysis.zyk.bean.PoliceSituation;
import com.sunshine.monitor.system.analysis.zyk.bean.Suspect;
import com.sunshine.monitor.system.analysis.zyk.service.SuspectManager;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.ReCode;
import com.sunshine.monitor.system.manager.service.SystemManager;

/**
 * 可疑库管理
 * @author licheng
 * @date 2016-10-24
 *
 */
@Controller
@RequestMapping(value = "/suspect.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspectController {

	@Autowired
	private SuspectManager suspectManager;
	@Autowired
	private SystemManager systemManager;
	
	private Logger log = LoggerFactory.getLogger(SuspectController.class);
	
	private String main = "analysis/suspectMain";
	private String win = "analysis/suspectWin";
	
	
	/**
	 * 初始化页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String findForward(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return main;
	}
	
	@RequestMapping
	public ModelAndView forwardWin(HttpServletRequest request,
			HttpServletResponse response, String model, String id) {
		try {

			if ("edit".equals(model)) {
				request.setAttribute("id", id);
			}else{
				UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
						request, "userSession");
				String yhdh = userSession.getSysuser().getYhdh();
				request.setAttribute("yhdh", yhdh);
			}
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			request.setAttribute("model", model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(win);
	}
	
	@ResponseBody
	@RequestMapping
	public Map<String,Object> getList(HttpServletRequest request,HttpServletResponse response,
			String rows, String page, Suspect susp){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		map = suspectManager.queryList(susp, filter);
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object saveSuspect(HttpServletRequest req,Suspect suspect) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			suspectManager.saveSuspect(suspect);
			map = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map = Common.messageBox("程序异常！", "0");
		}
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object getSuspect(HttpServletRequest request, String id){
		Suspect suspect = new Suspect();
		try {
			suspect = suspectManager.getSuspectById(id);
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suspect;
	}
	
	@RequestMapping
	@ResponseBody
	public Object deleteSuspect(HttpServletRequest request, String id){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			suspectManager.deleteSuspect(id);
			map.put("message", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "删除失败！");
		}
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object updateSuspect(HttpServletRequest req,Suspect suspect) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		if(StringUtils.isBlank(suspect.getId() )){
			return Common.messageBox("参数传输异常！", "0");
		}
		try {
			suspectManager.updateSuspect(suspect);
			map = Common.messageBox("更新成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map = Common.messageBox("程序异常！", "0");
		}
		
		return map;
	}
	
	/**
	 *  excel导出
	 * @param request
	 * @param response
	 * @return url
	 */
	@ResponseBody
	@RequestMapping
	public String outPutSuspect(HttpServletRequest request, HttpServletResponse response,Suspect bean) {
		String filename = "";
		try {
			String path = request.getSession().getServletContext().getRealPath("/")+"/download/";
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("rows", "1000");
			filter.put("page", "1");
			if(!StringUtils.isBlank(bean.getHphm())){
				String hphm = bean.getHphm().replace("?", "_").replace("？", "_");
				bean.setHphm(hphm);
			}
			filename = this.suspectManager.outPutSuspectList(filter,path,bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename;
	}
}
