package com.sunshine.monitor.system.monitor.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.FusionChartsXMLGenerator;
import com.sunshine.monitor.system.gate.bean.CodeDirect;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.monitor.service.SjcsjcProjectManager;

@Controller
@RequestMapping(value = "/transmonitor.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatTransMonitorCtroller {
	@Autowired
	private GateManager gateManager;

	@Autowired
	private SjcsjcProjectManager sjcsjcProjectManager;

	/**
	 * 跳转数据传输情况检测页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			UserSession userSession = (UserSession) request.getSession()
			.getAttribute("userSession");
	        Department dp = userSession.getDepartment();
	        if (!"4300".equals(dp.getGlbm().substring(0, 4))) {
	        	request.setAttribute("departmentList", this.gateManager
	        			.getDepartmentsByKdbh());
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "monitor/transmonitormain";
	}

	@RequestMapping()
	public String transDetail(HttpServletRequest request,
			HttpServletResponse response, String kssj, String jssj, String fs,
			String jkdss,String fxbh,String fxmc) throws Exception {
		UserSession userSession = (UserSession) request.getSession()
				.getAttribute("userSession");
		Department dp = userSession.getDepartment();
		if (jkdss == null || "".equals(jkdss)) {
			jkdss = "1";
		}
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		request.setAttribute("kdbhs", jkdss);
		if ("4300".equals(dp.getGlbm().substring(0, 4))) {
			return "monitor/st_report/st_transmonitordetail";
		}
		if (fs != null && fs.equals("1"))
			return "monitor/transmonitordetail";
		else if (fs != null &&fs.equals("2"))
			return "monitor/transdaymonitordetail";
		else {
			List<List<String>> datascl = this.sjcsjcProjectManager.getSjcsQueryScl(kssj, jssj, jkdss);
			List<List<String>> datasc = this.sjcsjcProjectManager.getSjcsQuerySc(kssj, jssj, jkdss);
			if(datascl==null||datasc==null) {
				request.setAttribute("message", "数据尚未关联卡口");
				return "monitor/transmonitorchart";
			}			
			String responseTextScl = FusionChartsXMLGenerator.getInstance().getMultiDSXML(datascl, "卡口数据上传统计【统计时间： "+kssj+" 至 "+jssj+"】", "上传率","1","%25",false,"卡口方向", "上传率", 1, 1, 2, 1);
			request.setAttribute("responseTextScl", responseTextScl);
			
			String responseTextSc = FusionChartsXMLGenerator.getInstance().getMultiDSXML(datasc, "卡口数据上传统计【统计时间： "+kssj+" 至 "+jssj+"】", "上传数据", "1", "", false, "卡口方向", "上传数", 1, 1, 2, 1);
			request.setAttribute("responseTextSc", responseTextSc);
			
			return "monitor/transmonitorchart";		
		}
			
	}
	
	//数据传输关联车道统计
	@RequestMapping()
	public String transDetailCd(HttpServletRequest request,
			HttpServletResponse response, String kssj, String jssj, String fs,
			String jkdss,String fxbh,String fxmc) throws Exception {
		UserSession userSession = (UserSession) request.getSession()
				.getAttribute("userSession");
		Department dp = userSession.getDepartment();
		if (jkdss == null || "".equals(jkdss)) {
			jkdss = "1";
		}
		request.setAttribute("kssj", kssj);
		request.setAttribute("jssj", jssj);
		request.setAttribute("dwdm", dp.getGlbm());
		request.setAttribute("jb", dp.getJb());
		request.setAttribute("kdbhs", jkdss);
		//不选方向
		if(fxbh==null||fxbh.equals("")) {
			List<CodeGateExtend> fxList = this.gateManager.getDirectList(fxbh);
			List<String> responseTextList = new ArrayList<String>();
			//List<String> responseTextSclList = new ArrayList<String>();
			for(int i = 0 ;i < fxList.size(); i++) {
				CodeGateExtend fx = fxList.get(i);
				List<List<String>> datascl = this.sjcsjcProjectManager.getSjcsQueryCdScl(kssj, jssj, jkdss,fx.getFxbh());
				List<List<String>> datasc = this.sjcsjcProjectManager.getSjcsQueryCdSc(kssj, jssj, jkdss,fx.getFxbh());
				if(datascl==null||datasc==null) {
					request.setAttribute("message", "数据未关联到该方向");
					return "monitor/transmonitorcdchart";
				}
				String responseTextScl = FusionChartsXMLGenerator.getInstance().getMultiDSXML(datascl, fx.getFxmc()+"【统计时间： "+kssj+" 至 "+jssj+"】", "上传率","1","%25",false,"方向车道", "上传率", 1, 1, 2, 0);
				String responseTextSc = FusionChartsXMLGenerator.getInstance().getMultiDSXML(datasc, fx.getFxmc()+"【统计时间： "+kssj+" 至 "+jssj+"】", "上传数据", "1", "", false,"方向车道", "上传数", 1, 1, 2, 0);
				responseTextList.add(responseTextScl);
				responseTextList.add(responseTextSc);					
			}
			request.setAttribute("responseTextList", responseTextList);
			return "monitor/transmonitorcdchart";	
		} else {
			//选择一个方向
			List<List<String>> datascl = this.sjcsjcProjectManager.getSjcsQueryCdScl(kssj, jssj, jkdss,fxbh);
			List<List<String>> datasc = this.sjcsjcProjectManager.getSjcsQueryCdSc(kssj, jssj, jkdss,fxbh);
			if(datascl==null||datasc==null) {
				request.setAttribute("message", "数据未关联到该方向");
				return "monitor/transmonitorchart";
			}
			String responseTextScl = FusionChartsXMLGenerator.getInstance().getMultiDSXML(datascl, fxmc+"【统计时间： "+kssj+" 至 "+jssj+"】", "上传率","1","%25", false,"方向车道", "上传率", 1, 1, 2, 0);
			String responseTextSc = FusionChartsXMLGenerator.getInstance().getMultiDSXML(datasc, fxmc+"【统计时间： "+kssj+" 至 "+jssj+"】", "上传数据", "1", "", false,"方向车道", "上传数", 1, 1, 2, 0);
			request.setAttribute("responseTextScl", responseTextScl);
			request.setAttribute("responseTextSc", responseTextSc);
			return "monitor/transmonitorchart";
		}
	}

}
