package com.sunshine.monitor.system.monitor.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.gate.bean.CodeDirect;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.monitor.service.IdentProjectManager;
import com.sunshine.monitor.system.monitor.service.StatIdentManager;

@Controller
@RequestMapping(value="/identmonitor.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatIdentMonitorCtroller {
	
	@Autowired
	private GateManager gateManager;
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private StatIdentManager statManager;
	
	@Autowired
	private IdentProjectManager identProjectManager;
	/**
	 * 跳转号牌识别情况检测页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setAttribute("flag", "list");
			if (this.systemManager.getParameter("xzqh", request).equals("430000000000")) {
				request.setAttribute("sign", "true");
			} else {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "false");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
		}
		return "monitor/identmonitormain";
	}
	/**
	 * 跳转过车流量统计页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForwardFlow(HttpServletRequest request,HttpServletResponse response) {
		try {
			request.setAttribute("flag", "list");
			if (this.systemManager.getParameter("xzqh", request).equals("430000000000")) {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "true");
			} else {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "false");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
		}
		return "monitor/StatFlowAnalysisMain";
	}
	/**
	 * 跳转过车流量图形展示页面
	 * @param request
	 * @param response
	 * @param kssj
	 * @param jssj
	 * @param jkdss
	 * @return
	 */
	@RequestMapping()
	public String showMultiFlow(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String jkdss){
		
		try {
			   
			String multiXml = statManager.getIdentListXML(kssj,jssj,jkdss);
			request.setAttribute("multiXml", multiXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "monitor/showMultiFlow";
	}
	/**
	 * 跳转过车(小时)流量图形展示页面
	 * @param request
	 * @param response
	 * @param kssj
	 * @param jssj
	 * @param kdbh
	 * @param fxbh
	 * @param cdbh
	 * @return
	 */
	@RequestMapping
	public String showMultiHourFlow(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String kdbh,String fxbh,String cdbh){
		
		try {
			String multiXml = statManager.getHourIdentListXML(kssj, jssj, kdbh, fxbh,cdbh);
			request.setAttribute("multiXml", multiXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "monitor/showMultiHourFlow";
	}
	/**
	 * 跳转号牌识别情况检测页面
	 * @param request
	 * @param response
	 * @param kssj
	 * @param jssj
	 * @param jkdss
	 * @return
	 */
	@RequestMapping(params="method=identDetail", method=RequestMethod.POST)
	public String  identDetail(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String jkdss) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
	  if (jkdss != null && !"".equals(jkdss)) {
		String[] kdbhs = jkdss.split(",");
		jkdss = "";
		for(int i=0;i<kdbhs.length;i++){
			jkdss+=kdbhs[i] + ",";
			if ( i == kdbhs.length - 1) {
				jkdss=jkdss.substring(0,jkdss.length() - 1);
			}
		  }
		}
		Department dp = userSession.getDepartment();
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		request.setAttribute("kdbhs",jkdss);
		if ("4300".equals(dp.getGlbm().substring(0, 4))) {
			return "monitor/st_report/st_identmonitordetail";
		}
		return "monitor/identmonitordetail";
	}
	/**
	 * 跳转号牌识别率统计报表页面
	 * @param request
	 * @param response
	 * @param kssj
	 * @param jssj
	 * @param jkdss
	 * @param fxbh
	 * @param fxmc
	 * @return
	 */
	@RequestMapping(params="method=identDetailcdChart", method=RequestMethod.POST)
	public String identDetailcdChart(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String jkdss,String fxbh,String fxmc) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Department dp = userSession.getDepartment();
		List<String> list = new ArrayList<String>();
		if (fxbh != null && fxbh.length() > 0 && jkdss != null) {
			List<List<String>> data = identProjectManager.getIdentcdProjectInfo(kssj, jssj, jkdss, fxbh);
			String responseText = FusionChartsXMLGenerator.getInstance().getMultiDSXML(data, "方向名称" + fxmc + " 号牌识别率统计【统计时间：" + kssj + "至" + jssj + "】", "", "1", "%25",true,"卡口车道", "识别数量", 1, 1, 2, 2);
			list.add(responseText);
			request.setAttribute("responseText",list);
			return "monitor/identcdmonitorchart";
		} else {
			try {
				List<CodeGateExtend> directList =  gateManager.getDirectList(fxbh);
				for (CodeGateExtend direct : directList) {
					List<List<String>> data = identProjectManager.getIdentcdProjectInfo(kssj, jssj, jkdss, direct.getFxbh());
					String responseText = FusionChartsXMLGenerator.getInstance().getMultiDSXML(data, "方向名称" + direct.getFxmc() + "号牌识别率统计【统计时间：" + kssj + "至" + jssj + "】", "", "1", "%25", true, "卡口车道", "识别率", 1, 1, 2, 2);
					list.add(responseText);
				}
				request.setAttribute("responseText", list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "monitor/identcdmonitorchart";
		}
	}	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param kssj
	 * @param jssj
	 * @param jkdss
	 * @return
	 */
	@RequestMapping(params="method=identDetailChart", method=RequestMethod.POST)
	public String  identDetailChart(HttpServletRequest request,HttpServletResponse response,String kssj,String jssj,String jkdss) {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		String[] kdbhs = jkdss.split(",");
		jkdss = "";
		for(int i=0;i<kdbhs.length;i++){
			jkdss+=kdbhs[i] + ",";
			if ( i == kdbhs.length - 1) {
				jkdss=jkdss.substring(0,jkdss.length() - 1);
			}
		}
		
		Department dp = userSession.getDepartment();
		List<List<String>> data = identProjectManager.getIdentProjectInfo(kssj, jssj, jkdss);
		String responseText = FusionChartsXMLGenerator.getInstance().getMultiDSXML(data, "号牌识别率统计【统计时间:" + kssj + "至" + jssj + "】", "","1","%25",true,"卡口方向", "识别数量", 1, 1, 2, 1);
		request.setAttribute("responseText", responseText);
		return "monitor/identmonitorchart";
	}
	

	public GateManager getGateManager() {
		return gateManager;
	}


	public void setGateManager(GateManager gateManager) {
		this.gateManager = gateManager;
	}


	public SystemManager getSystemManager() {
		return systemManager;
	}


	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	public IdentProjectManager getIdentProjectManager() {
		return identProjectManager;
	}

	public void setIdentProjectManager(IdentProjectManager identProjectManager) {
		this.identProjectManager = identProjectManager;
	}
}
