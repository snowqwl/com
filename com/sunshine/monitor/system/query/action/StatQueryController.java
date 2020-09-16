package com.sunshine.monitor.system.query.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.query.service.StatQueryManager;
import com.sunshine.monitor.system.query.service.StatSignQueryManager;

@Controller
@RequestMapping(value="/statQueryCtrl.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatQueryController {

	@Autowired
	private StatQueryManager statQueryManager;
	
	@Autowired
	@Qualifier("statSignQueryManager")
	private StatSignQueryManager statSignQueryManager;
	/**
	 * 系统应用统计 
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request){
		
		UserSession userSession = (UserSession) WebUtils
									.getSessionAttribute(request, "userSession");
		Department dp = userSession.getDepartment();
		
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		request.setAttribute("zsdw", dp.getSfzs());
		
		return "monitor/StatApplicationMain";
	}
	
	/**
	 * 签收处置率
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findForwardSgin(HttpServletRequest request){
		
		UserSession userSession = (UserSession) WebUtils
									.getSessionAttribute(request, "userSession");
		Department dp = userSession.getDepartment();
		
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		request.setAttribute("zsdw", dp.getSfzs());
		
		return "monitor/StatSginMain";
	}
	
	@RequestMapping
	public String findForwardSginChar(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String tjlx) {
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		Department dp = userSession.getDepartment();
		String glbm = dp.getGlbm();
		String jb = dp.getJb();
		List<List<String>> data = this.statSignQueryManager.getSignQuery(kssj, jssj, glbm, tjlx, jb);
		String listString = FusionChartsXMLGenerator.getInstance().getMultiDSXML(data, "签收处置率统计【统计时间:" + kssj + "至" + jssj + "】", "","1","%25",false,"部门名称", "签收率", 1, 1, 2, 1);
		request.setAttribute("responseText", listString);
		return "monitor/StatSignchart";
	}
	
	/**
	 * 四率两数统计
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findForwardSAJT(HttpServletRequest request){
		
		UserSession userSession = (UserSession) WebUtils
									.getSessionAttribute(request, "userSession");
		Department dp = userSession.getDepartment();
		
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		request.setAttribute("zsdw", dp.getSfzs());
		
		return "monitor/stat4Rate2NumSAJTMain";
	}
	
	/**
	 * 四率两数统计（涉案类）
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findForwardSA(HttpServletRequest request){
		
		UserSession userSession = (UserSession) WebUtils
									.getSessionAttribute(request, "userSession");
		Department dp = userSession.getDepartment();
		
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		request.setAttribute("zsdw", dp.getSfzs());
		
		return "monitor/stat4Rate2NumSAMain";
	}
	
	/**
	 * 四率两数统计（交通违法类）
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String findForwardJT(HttpServletRequest request){
		
		UserSession userSession = (UserSession) WebUtils
									.getSessionAttribute(request, "userSession");
		Department dp = userSession.getDepartment();
		
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		request.setAttribute("zsdw", dp.getSfzs());
		
		return "monitor/stat4Rate2NumJTMain";
	}
	
	/**
	 * 四率两数统计--图形展示
	 * @param request
	 * @param dwdm
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	@RequestMapping
	public String showStat4Rate2Num(HttpServletRequest request,String dwdm,String kssj,String jssj){
		
		try {
			
			String[] strs = statQueryManager.getShowStat2Rate4XML(dwdm, kssj, jssj);
			request.setAttribute("multiXml01", strs[0]);
			request.setAttribute("multiXml02", strs[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "monitor/showStat4Rate2NumChar";
	}

	public StatSignQueryManager getStatSignQueryManager() {
		return statSignQueryManager;
	}

	public void setStatSignQueryManager(StatSignQueryManager statSignQueryManager) {
		this.statSignQueryManager = statSignQueryManager;
	}
	
}
