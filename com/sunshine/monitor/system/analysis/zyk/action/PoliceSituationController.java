package com.sunshine.monitor.system.analysis.zyk.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.analysis.zyk.bean.PoliceSituation;
import com.sunshine.monitor.system.analysis.zyk.service.PoliceSituationManager;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.SystemManager;

/**
 * 可疑库管理
 * @author liumeng
 * @date 2016-10-25
 *
 */
@Controller
@RequestMapping(value = "/policesituation.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PoliceSituationController {
	@Autowired
	private PoliceSituationManager policeSituationManager;
	@Autowired
	private SystemManager systemManager;
	
	private Logger log = LoggerFactory.getLogger(SuspectController.class);
	
	private String main = "analysis/policesituationmain";
	
	private String win = "analysis/policesituationwin";
	
	/**
	 * 初始化页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String findForward(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return main;
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @param rows
	 * @param page
	 * @param lrr
	 * @param bean
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map<String,Object> queryList(HttpServletRequest request,HttpServletResponse response,
			String rows, String page, String lrr, PoliceSituation bean){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		try {
			if(!StringUtils.isBlank(bean.getHphm())){
				String hphm = URLDecoder.decode(bean.getHphm(), "UTF-8");
//				hphm = hphm.replace("?", "_").replace("？", "_");
				bean.setHphm(hphm);
			}
			map = policeSituationManager.queryList(bean, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping
	public String findForwardWin(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			String id=request.getParameter("id");
			if(!id.equals("0")){
				PoliceSituation bean = policeSituationManager.queryPoliceSituationById(id);
				request.setAttribute("bean", bean);
			}
			request.setAttribute("id", id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return win;
	}
	
	@RequestMapping()
	@ResponseBody
	public Object save(HttpServletRequest request,HttpServletResponse response,PoliceSituation bean){
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int i = policeSituationManager.save(bean, user);
			if(i>0){				
				map.put("msg", "保存成功");
			}else
				map.put("msg", "保存失败");
			map.put("flag", i);
		} catch (Exception e) {
			log.info("保存警情专题库数据出错");
			e.printStackTrace();
		}
		return map;
	}
	@RequestMapping()
	@ResponseBody
	public Object edit(HttpServletRequest request,HttpServletResponse response,PoliceSituation bean){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int i = policeSituationManager.update(bean);
			if(i>0){				
				map.put("msg", "修改成功");
			}else
				map.put("msg", "修改失败");
			map.put("flag", i);
		} catch (Exception e) {
			log.info("修改警情专题库数据出错");
			e.printStackTrace();
		}
		return map;
	}
	@RequestMapping()
	@ResponseBody
	public Object delete(HttpServletRequest request,HttpServletResponse response,PoliceSituation bean){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String id=request.getParameter("id");
			int i = policeSituationManager.delete(id);
			map.put("flag", i);
		} catch (Exception e) {
			log.info("删除警情专题库数据出错");
			e.printStackTrace();
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
	public String outPutPoliceSituation(HttpServletRequest request, HttpServletResponse response,PoliceSituation bean) {
		String filename = "";
		try {
			String path = request.getSession().getServletContext().getRealPath("/")+"/download/";
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("rows", "1000");
			filter.put("page", "1");
			if(!StringUtils.isBlank(bean.getHphm())){
				String hphm = URLDecoder.decode(bean.getHphm(), "UTF-8");
				bean.setHphm(hphm);
			}
			filename = this.policeSituationManager.outPutPoliceSituationList(filter,path,bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename;
	}
}
