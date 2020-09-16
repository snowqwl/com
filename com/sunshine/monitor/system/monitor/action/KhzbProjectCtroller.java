package com.sunshine.monitor.system.monitor.action;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.monitor.bean.KhzbProject;
import com.sunshine.monitor.system.monitor.service.KhzbProjectManager;

@Controller
@RequestMapping(value = "/khzbproject.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KhzbProjectCtroller {

	@Autowired
	private UrlManager urlManager;

	@Autowired
	@Qualifier("khzbProjectManager")
	private KhzbProjectManager khzbProjectManager;
	/**
	 * 跳转考核指标情况统计页面
	 * @param respons
	 * @param request
	 * @param khzbProject
	 * @param params
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,
			HttpServletResponse respons, KhzbProject khzbProject, String params) {
		try {
			request.setAttribute("urlList", urlManager.getCodeUrls());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "monitor/khzbmain";
	}
	/**
	 * 跳转考核指标情况统计详细页面
	 * @param response
	 * @param request
	 * @param kssj
	 * @param jssj
	 * @param city
	 * @param tjfs
	 * @param khzbProject
	 * @param sign
	 * @return
	 */
	@RequestMapping(params = "method=khzbDetail")
	public String khzbDetail(HttpServletRequest request,
			HttpServletResponse response, String kssj, String jssj,
			String city, String tjfs,KhzbProject khzbProject,String sign) {
		KhzbProject khzb = null;
		try {
			if (sign != null && sign.equals("detail")) {
				khzb = khzbProject;
			} else {
				khzb = khzbProjectManager.getKhzbProjectInfo(kssj, jssj, city);
			}
			UserSession userSession = (UserSession) request.getSession()
					.getAttribute("userSession");
			SysUser user = userSession.getSysuser();
			request.setAttribute("khzb", khzb);
			request.setAttribute("kssj", kssj);
			request.setAttribute("jssj", jssj);
			request.setAttribute("city", city);
			request.setAttribute("tjfs", tjfs);
			request.setAttribute("sign", sign);
			request.setAttribute("lrr", user.getYhmc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "monitor/khzbdetail";
	}
	/**
	 * 跳转车速监测详细页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "method=khzbDetailQuery")
	public String khzbDetailQuery(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("urlList", urlManager.getCodeUrls());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "monitor/khzbdetaillist";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=queryList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest request,
			HttpServletResponse respons,String rows,String page,KhzbProject khzbProject) {
		Map map = null;
		try {
			Map filter = new HashMap();
			KhzbProject khzb = khzbProject;
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			map = this.khzbProjectManager.getKhzbProjectList(filter, khzb);
		}catch(Exception e) {
			e.printStackTrace();
		 }
		return map;
	}

	@RequestMapping(params = "method=save", method = RequestMethod.POST)
	@ResponseBody
	public Map save(HttpServletRequest request, HttpServletResponse response,
			KhzbProject khzbProject) {
		Map result = null;
		int count = 0;
		try {
			count = this.khzbProjectManager.saveKhzb(khzbProject);
			if (count > 0) {
				result = Common.messageBox("保存成功！", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常!", "0");
		}
		return result;
	}

	public UrlManager getUrlManager() {
		return urlManager;
	}

	public void setUrlManager(UrlManager urlManager) {
		this.urlManager = urlManager;
	}
	
	@RequestMapping
	@ResponseBody
	public Map queryKhzbStatics(HttpServletRequest request, HttpServletResponse response) {
		Map result = new HashMap();
		try {
			String kssj = request.getParameter("kssj");
			String jssj = request.getParameter("jssj");
			if(kssj != null && jssj != null){
				List datalist = this.khzbProjectManager.queryKhzbStatics(kssj, jssj);
				result.put("rows", datalist);
				result.put("total", 1);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常!", "0");
		}
		return result;
	}
	

}
