package com.sunshine.monitor.system.monitor.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.system.monitor.bean.GateProject;
import com.sunshine.monitor.system.monitor.service.GateProjectManager;

@Controller
@RequestMapping(value = "/gateProject.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GateProjectCtroller {
	
	@Autowired
	@Qualifier("gateProjectManager")
	private GateProjectManager gateProjectManager;
	
	
	@RequestMapping(params = "method=index")
	public String index() {
		return "monitor/gateproject";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=queryGateProjectForPage", method = RequestMethod.POST)
	@ResponseBody
	public Map queryGateProjectForPage(String page, String rows, GateProject project) {	 
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		
		return this.gateProjectManager.findGateProjectForMap(filter, project);
	}
	
	@RequestMapping(params = "method=getGateProject")
	@ResponseBody
	public GateProject getGateProject(String rq, String dwdm) {
		return this.gateProjectManager.getGateProject(rq, dwdm);
	}
	
	@RequestMapping(params = "method=saveGateProject", method = RequestMethod.POST)
	public void saveGateProject(GateProject project, HttpServletResponse response) {
		String code = null ;
		boolean flag = false ;
		String result = null;
		PrintWriter out = null;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			out = response.getWriter();
		
			flag = this.gateProjectManager.saveGateProject(project);
			if(flag){
				result = "卡口规划登记成功!";
				code = "1";
			}else {
				result = "卡口规划登记失败!";
				code = "0";
			}
			
			
		} catch (IOException e) {
			result = "执行失败!";
			code = "0";
			e.printStackTrace();
		}
		
		out.print("{\"jg\":\""+code+"\",\"info\":\""+result+"\"}");
		out.flush();
		out.close();
	}
	
	/**
	 * @Description:删除
	 * @param: 
	 * @return: 
	 * @version:
	 * @date:
	 */
	@RequestMapping
	public void batchDeleteProject(@RequestParam String ids,
			HttpServletResponse response) {
		try {
			StringTokenizer token = new StringTokenizer(ids, ",");
			List<String> list = new ArrayList<String>();
			while (token.hasMoreElements()) {
				String id = (String) token.nextElement();
				list.add(id);
			}
			boolean flag = this.gateProjectManager.batchDeleteProject(list
					.toArray(new String[] {}));
			PrintWriter out = response.getWriter();
			out.print((flag) ? "1" : "0");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}