package com.sunshine.monitor.system.analysis.action;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.service.LinkedAnalysisManager;
import com.sunshine.monitor.system.analysis.service.SubjectManager;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value = "/linkedAnalysis.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LinkedAnalysisController {
	
	@Autowired
	@Qualifier("linkedManager")
	private LinkedAnalysisManager linkedManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("subjectManager")
	private SubjectManager subjectManager;
	
	private static final String main = "analysis/linkedAnalysisMain";
	
	/**
	 * 跳转到关联分析主页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
    public String forwardMain(HttpServletRequest request,HttpServletResponse response){
		try{
		  request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
		  request.setAttribute("csysList", this.systemManager.getCode("030108"));
		  request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		  request.setAttribute("cllxList", this.systemManager.getCode("030104"));
		  
		  //request.setAttribute("fxBtn",request.getParameter("fxBtn"));//用于判断是否显示保存分析按钮
		}catch(Exception ex){
			ex.printStackTrace();
		}
    	return main;
    }
	
	/**
	 * 关联分析
	 * @param request
	 * @param response
	 * @param link
	 * @param page
	 * @param rows
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String,Object>> queryList(HttpServletRequest request,HttpServletResponse response,String link,String page,String rows,ScsVehPassrec veh)throws Exception{

		    List<Map<String,Object>> list = null;
			Map<String, Object> filter = new HashMap<String, Object>();
			String[] links = link.split(",");
			filter.put("page", page);
			filter.put("rows", rows);
			filter.put("link",links);
			filter.put("sessionId", request.getSession().getId().replace("-","_"));
			String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
			hphm = URLDecoder.decode(hphm, "UTF-8");
			veh.setHphm(hphm);
			try {
				list = this.linkedManager.findLinkForPageExt(veh, filter);
				//map.put("pagesign", veh.getPageSign());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}
	
	@RequestMapping
	@ResponseBody
	public int queryForLinkTotal(HttpServletRequest request,HttpServletResponse response,String link,String page,String rows,ScsVehPassrec veh) throws Exception{
		    int total = 0;
			Map<String, Object> filter = new HashMap<String, Object>();
			String[] links = link.split(",");
			filter.put("page", page);
			filter.put("rows", rows);
			filter.put("link",links);
			filter.put("sessionId", request.getSession().getId().replace("-","_"));
			String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
			hphm = URLDecoder.decode(hphm, "UTF-8");
			veh.setHphm(hphm);
			try {
				total = this.linkedManager.getForLinkTotal(veh, filter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return total;
	}
	
	/**
	 * 查看过车明细列表
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> findLinkDetailForPage(HttpServletRequest request,HttpServletResponse response,ScsVehPassrec veh,String page,String rows){
		Map<String,Object> map = null;
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("sessionId", request.getSession().getId().replace("-","_"));
			try {
				map = this.linkedManager.findLinkDetailForPage(veh, filter);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    return map;
	}
	
	/**
	 * 查询已保存分析的关联分析结果
	 * @param ztbh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getLinkListBySave(String ztbh,String page,String rows){
		Map<String,Object> map = null;
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		    try {
				map = this.subjectManager.getLinkList(ztbh, filter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return map;
	}
	
	/**
	 * 
	 * 导出车辆分析的Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getCaseInfoXls(HttpServletRequest request,HttpServletResponse response,String link,String page,String rows,ScsVehPassrec veh) {
		String[] headName = new String[]{"号牌号码", "号牌颜色", "号牌种类", "过车次数", "车辆品牌", "车身颜色", "违法信息"};
		short[] columnWidth = new short[]{3000,5000,5000,9000,3000,3000,3000,9000,12000,3000,3000,6000}; 
		String[] method = new String[]{"hphm","hpysmc","hpzlmc","cs","clpp1","csysmc","wfxx"};
		String hphm = request.getParameter("fxhphm");
		veh.setHphm(hphm);
		String name="车辆关联分析";
		try {
			List<Map<String,Object>> infos= this.queryList(request, response, link, page, rows, veh);
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}

