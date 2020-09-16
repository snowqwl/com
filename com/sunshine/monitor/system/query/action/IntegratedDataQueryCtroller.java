package com.sunshine.monitor.system.query.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.query.bean.Surveil;
import com.sunshine.monitor.system.query.bean.VehAlarmrecIntegrated;
import com.sunshine.monitor.system.query.bean.VehPassrecIntegrated;
import com.sunshine.monitor.system.query.service.IntegrateQueryManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value="/integratedDataQueryCtrl.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IntegratedDataQueryCtroller {

	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private UrlManager urlManager;
	
	@Autowired
	private IntegrateQueryManager integrateQueryManager;
	/**
	 * 跳转全省过车集中库查询主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request){
		
		try {
			request.setAttribute("urllist", this.urlManager.getCodeUrls());
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			
			request.setAttribute("cllxlist", this.systemManager
					.getCodes("030104"));
			
			request.setAttribute("hpyslist", this.systemManager
					.getCodes("031001"));
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "query/queryallpassmain";
	}
	/**
	 * 跳转全省交通违法查询主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForwardTraffic(HttpServletRequest request){
		
		try {
			request.setAttribute("urllist", this.urlManager.getCodeUrls());
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "query/queryTraffic";
	}
	
	/**
	 * 跳转全省报警信息集中库查询主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForwardAlarm(HttpServletRequest request){
		try{
		request.setAttribute("urllist",this.urlManager.getCodeUrls());
		request.setAttribute("hpzllist",this.systemManager.getCodes("030107"));
		request.setAttribute("bjdllist",this.systemManager.getCodes("130033"));
		request.setAttribute("bjlxlist",this.systemManager.getCodes("130034"));
		} catch (Exception e){
			e.printStackTrace();
		}
		return "query/queryallalarmmain";
	}
	
	/**
	 * 
	 * 函数功能说明：省集中库查询过车数据
	 * 修改日期 	2013-7-20
	 * @param page
	 * @param rows
	 * @param info
	 * @param citys
	 * @param request
	 * @return    
	 * @return Map
	 */
	@RequestMapping
	@ResponseBody
	public Map queryList(String page,String cxlx, String rows,VehPassrec info,String citys,HttpServletRequest request){
		Map map  = null;
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		filter.put("cxlx",cxlx);  //查询类型
		try {
			map = integrateQueryManager.getMapForIntegratePass(filter, info, citys);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map ;
		
	}
	
	/**
	 * 
	 * 函数功能说明：省集中库查询交通违法信息
	 * 修改日期 	2013-7-20
	 * 修改日期 	2016-3-23 liumeng
	 * @param page
	 * @param rows
	 * @param info
	 * @param citys
	 * @param request
	 * @return    
	 * @return Map
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> queryListTraffic(String page, String rows,VehPassrec info,String citys,String wflxTab,HttpServletRequest request){
		Map<String,Object> map  = null;		
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数		
		try {
			String hphm = info.getHphm().replace("?", "_").replace("？", "_");
			info.setHphm(hphm);
			map = integrateQueryManager.getMapForIntegrateTraffic(filter, info, citys,wflxTab);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map ;		
	}
	
	/**
	 * 省集中库报警信息查询
	 * @param page
	 * @param rows
	 * @param info
	 * @param citys
	 * @return
	 */
	@RequestMapping
	@ResponseBody
    public Map queryListAlarm(String page, String rows,VehAlarmrecIntegrated info,String citys){
    	Map map = null;
    	Map filter = new HashMap();
    	filter.put("curPage",page);
    	filter.put("pageSize", rows);
    	try{
    		map = integrateQueryManager.getMapForIntegrateAlarm(filter, info, citys);
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	return map;
    }
	
	/**
	 * 省集中库过车记录查询明细
	 * @param request
	 * @param hphm
	 * @param kdbh
	 * @param gcsj
	 * @param cxlx
	 * @return
	 */
	@RequestMapping
	public Object forwardIntegratedPassDetail(HttpServletRequest request, String hphm,String kdbh,String gcsj,String cxlx){
		VehPassrecIntegrated pass = null;
			
		try {
			pass = integrateQueryManager.getPassIntegrateDetail(hphm, kdbh, gcsj, cxlx);
			
			request.setAttribute("info",pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "query/allpasswin";
	}
	
	/**
	 * 省集中库报警信息查询明细
	 * @param request
	 * @param hphm
	 * @param kdbh
	 * @param gcsj
	 * @param cxlx
	 * @return
	 */
	@RequestMapping
	public Object forwardIntegratedAlarmDetail(HttpServletRequest request,
			String bjxh) {
		VehAlarmrecIntegrated alarm = null;

		try {
			alarm = integrateQueryManager.getAlarmIntegrateDetail(bjxh);

			request.setAttribute("info", alarm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "query/allalarmwin";
	}
	
	/**
	 * 查询非现场文本记录信息
	 * @param xh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object getSuvreit(String xh){
		Surveil s = null;
		
		s = integrateQueryManager.getSuvreitForXh(xh);
		
		return s;
	}
	
	/**
	 * 查询违法记录信息
	 * @param wfbh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object getViolation(String dzwzId){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = integrateQueryManager.getViolationForWfbh(dzwzId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(list.size() > 0){
			
			Map<String,Object> map = list.get(0);
			JSONArray json = JSONArray.fromObject(map); 
			
			return json.toString();
		}
		
			
		return null;
	}
	
	/**
	 * 查询强制措施违法记录信息
	 * @param xh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object getForce(String xh){
		
		List<Map<String,Object>> list = integrateQueryManager.getForceForXh(xh);
		
		if(list.size() > 0){
			
			Map<String,Object> map = list.get(0);
			JSONArray json = JSONArray.fromObject(map); 
			
			return json.toString();
		}
		
			
		return null;
	}
	
	/**
	 * 查询违法明细
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	@Deprecated
	public Map<String,Object> getTrafficDetail(HttpServletRequest request,HttpServletResponse response,VehPassrec veh,String page,String rows){
	    Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("curPage", page);
		filter.put("pageSize", rows);
		Map<String,Object> result = null ;
		try {
			result =  this.integrateQueryManager.getTrafficDetail(veh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据号牌号码，号牌种类查询车辆违法明细
	 * @param request
	 * @param response
	 * @param car
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getViolationDetail(HttpServletRequest request,HttpServletResponse response,VehPassrec veh,String page,String rows){
	    Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("curPage", page);
		filter.put("pageSize", rows);
		CarKey car = new CarKey(veh.getHphm(),veh.getHpzl());
		Map<String,Object> result = null ;
		try {
			result =  this.integrateQueryManager.getViolationDetail(car, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
