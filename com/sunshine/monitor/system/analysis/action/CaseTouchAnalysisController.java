package com.sunshine.monitor.system.analysis.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.analysis.bean.CaseTouch;
import com.sunshine.monitor.system.analysis.bean.CaseTouchAnalysis;
import com.sunshine.monitor.system.analysis.bean.CombineCondition;
import com.sunshine.monitor.system.analysis.service.CaseTouchManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/casetouchCtr.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CaseTouchAnalysisController {
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired(required=true)
	private CaseTouchManager caseTouchManager;
	
	protected Logger debugLogger = LoggerFactory.getLogger(LikeVehiclesController.class);	
	/**
	 * 案件对碰分析首页
	 * 
	 * @return
	 */
	@RequestMapping
	public ModelAndView findForward(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();		
		try {
			////对碰规则
			List dpgzList =this.caseTouchManager.getCaseTouchRule();
			////案件来源
			List ajlyList = getCaseDm("","案件来源","");
			////案件类型
			List ajlxList = getCaseDm("","案件类别","车");			
			////案发区域
			List ajqyList = this.systemManager.getCityList();
			///号牌种类
			List hpzlList = this.systemManager.getCodes("030107");
			request.setAttribute("dpgzList", dpgzList);
			request.setAttribute("ajlyList", ajlyList);
			request.setAttribute("ajlxList", ajlxList);
			request.setAttribute("ajqyList", ajqyList);
			request.setAttribute("hpzlList",hpzlList);
			mv.setViewName("analysis/caseTouchAnaiysis");		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	
	public List getCaseDm(String ywxtmc,String dmlb,String dmmc){
		List dmlist = new ArrayList();
		try {
			dmlist = this.caseTouchManager.getCaseDm(ywxtmc,dmlb,dmmc);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return dmlist;
	}
	
	/**
	 * SAVE案件对碰规则
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveCaseTouchGz(HttpServletRequest request,
			HttpServletResponse response,CaseTouchAnalysis command) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			CaseTouchAnalysis information = command;
			String dpgzmc = "";
			String gzmc ="";			
			int length = information.getDpgz().length();
			if(length==1){
				switch(Integer.parseInt(information.getDpgz().toString().trim())){
				case 0:
					gzmc += "同区域,所有案件";
					dpgzmc += "同区域";
				case 1:
					gzmc += "不同区域,同一案件";
					dpgzmc += "同一案件";
				}
			}else{
					gzmc += "同区域,同一案件";
					dpgzmc += "同区域,所有案件";
					
			}
			information.setDpgzmc(dpgzmc);
			information.setGzmc(gzmc);
			
			String xh = this.caseTouchManager.saveCaseTouchGz(information);
			if (xh != null) {
				_map.put("msg", "保存成功!");
			} else {
				_map.put("msg", "保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	/**
	 * 案件对碰规则查询
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryCaseTouchRuleList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {						
				Map<String, Object> conditions = Common.getParamentersMap(request);
				result = this.caseTouchManager.queryCaseTouchRuleList(conditions);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 案件规则修改页面
	 * @param request
	 * @param response
	 * @param modal
	 * @return
	 */
	@RequestMapping
	public ModelAndView editCaseTouch(HttpServletRequest request,
			HttpServletResponse response,String modal) {
		ModelAndView mv = null;
		try {
				mv = new ModelAndView();
				request.setAttribute("gzbh",request.getParameter("gzbh"));
				mv.setViewName("analysis/caseTouchRuleWin");				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 得到指定编号的规则
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping
	public Object getEditInformations(HttpServletRequest request) throws Exception {
		String gzbh = request.getParameter("gzbh");
		CaseTouchAnalysis result = null;
		try {
			result = this.caseTouchManager.getEditInformation(gzbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * UPDATE案件对碰规则
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object updateCaseTouchGz(HttpServletRequest request,
			HttpServletResponse response,CaseTouchAnalysis command) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			CaseTouchAnalysis information = command;
			String dpgzmc = "";
			String gzmc ="";			
			int length = information.getDpgz().length();
			if(length==1){
				switch(Integer.parseInt(information.getDpgz().toString().trim())){
				case 0:
					gzmc += "同区域,不同案件";
					dpgzmc += "同区域";
				case 1:
					gzmc += "不同区域,同一案件";
					dpgzmc += "同一案件";
				}
			}else{
					gzmc += "同区域,同一案件";
					dpgzmc += "同区域,同一案件";
					
			}
			information.setDpgzmc(dpgzmc);
			information.setGzmc(gzmc);
			information.setGzbh(request.getParameter("gzbh"));
			
			int xh = this.caseTouchManager.updateCaseTouchGz(information);
			if (xh != 0) {
				_map.put("msg", "保存成功!");
			} else {
				_map.put("msg", "保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	/**
	 * 案件查询
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Map<String , Object> getCaseForList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {						
				Map<String, Object> conditions = Common.getParamentersMap(request);
				result = this.caseTouchManager.getCaseForList(conditions);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 案件对碰分析
	 * @param request
	 * @param response
	 * @return
	 */
/*	@SuppressWarnings({ "static-access", "unchecked" })
	@ResponseBody
	@RequestMapping
	public Object getCaseTouchInformations(HttpServletRequest request,HttpServletResponse response){
		String dpgz=request.getParameter("dpgz");
		String conditions=request.getParameter("conditions");
		List<CaseTouch> list = null;
		try{
			JSONArray arry  = JSONArray.fromObject(conditions);
			JSONArray temparray = new JSONArray();
			for(int i = 0;i<arry.size();i++){
				if(arry.getJSONObject(i)!=null){
				 	JSONObject jsonObject = arry.getJSONObject(i);
				 	JSONObject jsonObject1 = new JSONObject();
				 	Iterator iter = jsonObject.keys();
		            while(iter.hasNext()){
		            String keystr = iter.next().toString();
		            String key	= keystr.toLowerCase();
		            String value = jsonObject.getString(keystr);
		            jsonObject1.put(key, value);
		            }
		            temparray.add(jsonObject1);
				}
			}
			list = temparray.toList(temparray, CaseTouch.class);
			
			this.caseTouchManager.getCaseTouchInformations(list);
		}		
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}*/
	
	/**
	 * 从临时表统计分析数据
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getCaseTouchAnaiysisDatagrid(HttpServletRequest request,HttpServletResponse response,String page,String rows){
		Map<String,Object>  filter=new HashMap<String,Object>();
		filter.put("rows", rows);
		filter.put("page",page);
		Map<String,Object> map=null;
		CaseTouchAnalysis result = null;
		String conditions = request.getParameter("conditions");
		String gzbh = request.getParameter("gzbh");
		JSONArray arry  = JSONArray.fromObject(conditions);
			try{	
				result = this.caseTouchManager.getEditInformation(gzbh);
				filter.put("pl",result.getPl());
				JSONArray temparray = new JSONArray();
				for(int i = 0;i<arry.size();i++){
					if(arry.getJSONObject(i)!=null){
					 	JSONObject jsonObject = arry.getJSONObject(i);
					 	JSONObject jsonObject1 = new JSONObject();
					 	Iterator iter = jsonObject.keys();
			            while(iter.hasNext()){
			            String keystr = iter.next().toString();
			            String key	= keystr.toLowerCase();
			            String value = jsonObject.getString(keystr);
			            jsonObject1.put(key, value);
			            }
			            temparray.add(jsonObject1);
					}
				}
				List<CaseTouch> list = temparray.toList(temparray, CaseTouch.class);				
				map=this.caseTouchManager.getCaseTouchAnaiysisDatagrid(filter,list);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map;		
	}
	

	/**
	 * //根据号牌号码和部分条件获取过车的轨迹列表
	 * 
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */

	@RequestMapping
	@ResponseBody
	public Map<String, Object> getCaseTouchVehList(HttpServletRequest request,
			HttpServletResponse response, VehPassrec veh, String page,
			String rows) {
		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		String conditions = request.getParameter("conditions");
		JSONArray arry  = JSONArray.fromObject(conditions);
		JSONArray temparray = new JSONArray();
		filter.put("page", page);
		filter.put("rows", rows);	
		try {
			for(int i = 0;i<arry.size();i++){
				if(arry.getJSONObject(i)!=null){
				 	JSONObject jsonObject = arry.getJSONObject(i);
				 	JSONObject jsonObject1 = new JSONObject();
				 	Iterator iter = jsonObject.keys();
		            while(iter.hasNext()){
		            String keystr = iter.next().toString();
		            String key	= keystr.toLowerCase();
		            String value = jsonObject.getString(keystr);
		            jsonObject1.put(key, value);
		            }
		            temparray.add(jsonObject1);
				}
			}
			List<CaseTouch> list = temparray.toList(temparray, CaseTouch.class);
			filter.put("list",list);
			veh.setHphm(java.net.URLDecoder.decode(veh.getHphm(), "UTF-8"));
			map = caseTouchManager.getCaseTouchVehList(veh, filter);
		} catch (Exception e) {
			debugLogger.warn("车辆列表分页查询", e);
		}
		return map;
	}
	
	/**
	 * 
	 * 导出案件对碰分析Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getTouchXls(HttpServletRequest request,HttpServletResponse response) {
		String[] headName = new String[]{"案件名称", "案件时间初值", "案件时间终值", "案发地址","案件来源","简要案情"};
		short[] columnWidth = new short[]{6000,5000,5000,9000,3000,3000,3000,9000,3000,6000}; 
		String[] method = new String[]{"ajmc","fasjcz","fasjzz","fadd_qxmc","fadd_qx","ajly","zyaq"};
		String name = "案件对碰分析";
		try {			
			List<Map<String,Object>> infos=(List<Map<String,Object>>)this.getCaseForList(request,response).get("rows");
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
}
