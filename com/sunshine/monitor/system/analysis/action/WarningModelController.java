package com.sunshine.monitor.system.analysis.action;

import java.io.UnsupportedEncodingException;
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
import com.sunshine.monitor.system.analysis.service.WarningModelManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/warning.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WarningModelController {
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("warningManager")
	private WarningModelManager warningManager;
	
	@RequestMapping()
	public String findForward(HttpServletRequest request) {
		try{
			////案发区域
			List ajqyList = this.systemManager.getCityList();
			request.setAttribute("ajqyList", ajqyList);
			//号牌颜色集合
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			//号牌种类集合
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		return "analysis/warningmodelmain";
	}
	
	@RequestMapping()
	public String findForwardQuery(){
		return "analysis/queryfalselicense";
	}
	
	/**
	 * 预警模型（区域首次分析）查询列表
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryWarningList(HttpServletRequest request,VehPassrec veh,String xzqhdm){
		Map filter=new HashMap();
		filter.put("xzqhdm",xzqhdm!=null?xzqhdm.toString():null);
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		
		List<Map<String, Object>> result=null;
		try{
			result=this.warningManager.queryWarningListExt(filter, veh);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}	
	/**
	 * 预警模型（区域首次分析）查询总数
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryWarningListTotal(HttpServletRequest request,VehPassrec veh){
		Map filter=new HashMap();
		filter.put("xzqhdm",(String)request.getParameter("xzqhdm")!=null?request.getParameter("xzqhdm").toString():null);
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		
		int total = 0;
		try{
			total = this.warningManager.queryWarningListTotal(filter, veh);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 *查询区域首次出现的车辆详细信息
	 * @param request
	 * @return  queryOneIllegal
	 * @throws 
	 */
	@RequestMapping
	@ResponseBody
	public Object queryPass(HttpServletRequest request) throws UnsupportedEncodingException{
		Map filter = new HashMap();
		String gcxh = (String)request.getParameter("gcxh")!=null?request.getParameter("gcxh").toString():null;
		filter.put("gcxh", gcxh);
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		Map result = null;
		try{
			result = this.warningManager.queryPass(filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 *查询最新的一条违法信息
	 * @param request
	 * @return  queryOneIllegal
	 * @throws 
	 */
	@RequestMapping
	@ResponseBody
	public Object queryOneIllegal(HttpServletRequest request) throws UnsupportedEncodingException{
		Map filter = new HashMap();
		String gcxh = (String)request.getParameter("gcxh")!=null?request.getParameter("gcxh").toString():null;
		String hphm = (String)request.getParameter("hphm")!=null?request.getParameter("hphm").toString():null;
		String hpzl = (String)request.getParameter("hpzl")!=null?request.getParameter("hpzl").toString():null;
		filter.put("gcxh", gcxh);
		filter.put("hphm", hphm);
		filter.put("hpzl", hpzl);
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		Map result = null;
		try{
			result = this.warningManager.queryOneIllegal(filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * 导出区域首次出现车辆Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getWarningXls(HttpServletRequest request,HttpServletResponse response,VehPassrec veh,String xzqhdm) {
		String[] headName = new String[]{"号牌号码", "号牌颜色", "车牌种类", "卡口名称","过车时间","违法信息关联"};
		short[] columnWidth = new short[]{6000,5000,5000,9000,3000,3000,3000,9000,3000,6000}; 
		String[] method = new String[]{"hphm","hphmys","hpzlmc","kdmc","gcsj","wfxx"};
		String name="区域首次出现车辆分析";
		try {			
			List<Map<String,Object>> infos=(List<Map<String,Object>>) this.queryWarningList(request, veh, xzqhdm);
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


