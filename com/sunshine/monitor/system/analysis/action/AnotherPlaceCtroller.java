package com.sunshine.monitor.system.analysis.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.service.AnotherPlaceManager;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/anotherPlace.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AnotherPlaceCtroller {
	@Autowired
	private GateManager gateManager;
	@Autowired
	private SystemManager systemManager;
	@Autowired
	private AnotherPlaceManager anotherPlaceManager;
	private static String glbm = "";

	/**
	 * 初始化页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String findForward(HttpServletRequest request,
			HttpServletResponse response) {
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		glbm = userSession.getDepartment().getGlbm();
		try {
			// 号牌颜色集合
			request.setAttribute("hpysList", this.systemManager
					.getCodes("031001"));
			// 车辆颜色集合
			request.setAttribute("csysList", this.systemManager
					.getCode("030108"));
			// 号牌种类集合
			request.setAttribute("hpzlList", this.systemManager
					.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "analysis/anotherPlacemain";
	}

	/**
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
	public Map<String, Object> queryForAnotherPlaceList(
			HttpServletRequest request, HttpServletResponse response,
			VehPassrec veh, String page, String rows) {
		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		String local = glbm.substring(0, 4);
		try {
			map = this.anotherPlaceManager.queryForAnotherPlaceList(veh,
					filter, local);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @param hm
	 * @param taskName
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> getAnotherPlaceList(
			HttpServletRequest request, HttpServletResponse response,
			VehPassrec veh, String page, String rows,String hm,String taskName) {
		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("taskName", taskName);
		filter.put("hm", hm);
		filter.put("kssj", veh.getKssj());
		filter.put("jssj", veh.getJssj());
		String local = glbm.substring(0, 4);
		try {
			map = this.anotherPlaceManager.queryForAnotherPlaceList(veh,
					filter, local);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param rows
	 * @param page
	 * @param sign
	 * @param zcd
	 * @param kssj
	 * @param jssj
	 * @param gw
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getList(HttpServletRequest request,HttpServletResponse response ,String rows ,String page,String sign,String zcd,String kssj,String jssj,String gw,String gjx,String bdx) throws Exception{
		Map<String,Object> filter=new HashMap<String,Object>();
		String zcdlist="";
		if(!zcd.equals("")){
			zcdlist=zcd.substring(0,zcd.length()-1);
		}
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("zcd", URLDecoder.decode(zcdlist, "utf-8"));
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		filter.put("gjx", gjx);
		filter.put("bdx", bdx);
		filter.put("local", glbm);
		filter.put("arrayid", request.getParameter("arrayid"));
		filter.put("dangerousCs", request.getParameter("dangerousCs"));
		filter.put("sessionid", request.getSession().getId().replace("-", "_"));
		Map<String,Object> map=null;
		try{
			//条件成立，重新查询大表
			/*if(Integer.parseInt(page)==1&&Integer.parseInt(sign)==0){
				map=this.anotherPlaceManager.getList(filter);
				map.put("pageSign", "0");
			}
			else{
				map=this.anotherPlaceManager.getListForpage(filter);
				map.put("pageSign", "1");
			}*/
			map=this.anotherPlaceManager.getListExt(filter);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Integer queryListTotal(HttpServletRequest request,HttpServletResponse response ,String rows ,String page,String sign,String zcd,String kssj,String jssj,String gw,String gjx,String bdx) throws Exception{
		int total = 0;
		Map<String,Object> filter=new HashMap<String,Object>();
		String zcdlist="";
		if(!zcd.equals("")){
			zcdlist=zcd.substring(0,zcd.length()-1);
		}
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("zcd", URLDecoder.decode(zcdlist, "utf-8"));
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		filter.put("gjx", gjx);
		filter.put("bdx", bdx);
		filter.put("local", glbm);
		filter.put("arrayid", request.getParameter("arrayid"));
		filter.put("dangerousCs", request.getParameter("dangerousCs"));
		filter.put("sessionid", request.getSession().getId().replace("-", "_"));
		try {
			total=this.anotherPlaceManager.getListTotal(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	@RequestMapping
	@ResponseBody
	public String saveZcd(HttpServletRequest request,HttpServletResponse response,String rows ,String page,String zcd,String kssj,String jssj,String taskname,String gw,String gjx,String bdx) throws Exception{
		Map filter=new HashMap();
		//一次最多只能保存1000条记录
		String zcdlist="";
		if(!zcd.equals("")){
			zcdlist=zcd.substring(0,zcd.length()-1);
		}
		filter.put("rows", "1000");
		filter.put("page", "1");
		//filter.put("zcd", zcdlist);
		String zcdsz = URLDecoder.decode(zcd, "utf-8");
		String czds = zcdsz.substring(0, zcdsz.length()-1);
		filter.put("zcd", czds);
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		filter.put("local", glbm);
		filter.put("gw", gw);
		filter.put("gjx", gjx);
		filter.put("bdx", bdx);
		String flag="";
		Map map=null;
		try{
		 //map=this.anotherPlaceManager.getList(filter);
			flag=this.anotherPlaceManager.saveZcd(taskname,filter);
		 }
		catch(Exception e){
			flag=e.toString();
			e.printStackTrace();
		}
		return flag;
	}
   
	@RequestMapping 
	@ResponseBody
	public Map<String , Object> findtask(HttpServletRequest request,HttpServletResponse response ,String rows,String page,String taskname,String xh,String kssj,String jssj){
		Map filter=new HashMap();
		filter.put("rows", rows);
		filter.put("page", page);
		//System.out.println("xxx"+kssj);
		//System.out.println("xxx"+jssj);
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		Map map=null;
		try{
		map =this.anotherPlaceManager.findtask(xh==null?"":xh, filter, taskname==null?"":taskname);
		List list=(List)map.get("rows");
		for(int i=0;i<list.size();i++){
			Map mapTj=(Map)list.get(i);
			String _kssj=mapTj.get("CONDISION").toString().substring(mapTj.get("CONDISION").toString().indexOf("开"),mapTj.get("CONDISION").toString().indexOf("结"));
			String _jssj=mapTj.get("CONDISION").toString().substring(mapTj.get("CONDISION").toString().indexOf("结"),mapTj.get("CONDISION").toString().indexOf("注"));
			String _zcd=mapTj.get("CONDISION").toString().substring(mapTj.get("CONDISION").toString().indexOf("注"),mapTj.get("CONDISION").toString().indexOf("过"));
			String _canshu=mapTj.get("CONDISION").toString().substring(mapTj.get("CONDISION").toString().indexOf("过"),mapTj.get("CONDISION").toString().length());
			mapTj.put("CONDISION",_kssj+"<br>"+_jssj+"<br>"+_zcd+"<br>"+_canshu);
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping 
	@ResponseBody
	public Map getZcdList(HttpServletRequest request,HttpServletResponse response ,String rows,String page,String taskname,String xh){
		Map filter=new HashMap();
		filter.put("rows", rows);
		filter.put("page", page);
		Map map=null;
		try{
		map =this.anotherPlaceManager.findZcdList(xh==null?"":xh, filter);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping 
	@ResponseBody
	public List<Map<String,Object>> queryDetilList(HttpServletRequest request,HttpServletResponse response,String rows ,String page,String xh,String sign,String pageSign){
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("sessionid", request.getSession().getId().replace("-","_"));
		filter.put("dangerousId", request.getParameter("dangerousId"));
		try {
			filter.put("dangerousMatchCity", URLDecoder.decode(request.getParameter("dangerousMatchCity"),"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		List<Map<String,Object>> list = null;
		try {
			/*if(!pageSign.equals("1")){
			xh=URLDecoder.decode(xh, "utf-8");
			result=this.anotherPlaceManager.queryDetilList(xh, filter, sign);
			}
			else{
				filter.put("stat", "1");
				result=this.anotherPlaceManager.queryDetilList(xh,filter);
			}*/
			xh=URLDecoder.decode(xh, "utf-8");
			list=this.anotherPlaceManager.queryDetilListExt(xh, filter, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public Integer queryForDetalTotal(HttpServletRequest request,HttpServletResponse response,String rows ,String page,String xh,String sign,String pageSign){
		int total = 0;
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("sessionid", request.getSession().getId().replace("-","_"));
		filter.put("dangerousId", request.getParameter("dangerousId"));
		try {
			filter.put("dangerousMatchCity", URLDecoder.decode(request.getParameter("dangerousMatchCity"),"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			xh=URLDecoder.decode(xh, "utf-8");
			total=this.anotherPlaceManager.getForDetilTotal(xh, filter, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	@RequestMapping 
	@ResponseBody
	public List<Map<String,Object>> queryDangerousDetailList(HttpServletRequest request,HttpServletResponse response,String rows ,String page,String xh,String sign,String pageSign){
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("sessionid", request.getSession().getId().replace("-","_"));
		filter.put("dangerousId", request.getParameter("dangerousId"));
		try {
			filter.put("dangerousMatchCity", URLDecoder.decode(request.getParameter("dangerousMatchCity"),"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		List<Map<String,Object>> list=null;
		try {			
			xh=URLDecoder.decode(xh, "utf-8");
			list=this.anotherPlaceManager.queryDangerousDetailListExt(xh, filter, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public Integer queryForDetalDangerousTotal(HttpServletRequest request,HttpServletResponse response,String rows ,String page,String xh,String sign,String pageSign){
		int total = 0;
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("sessionid", request.getSession().getId().replace("-","_"));
		filter.put("dangerousId", request.getParameter("dangerousId"));
		try {
			filter.put("dangerousMatchCity", URLDecoder.decode(request.getParameter("dangerousMatchCity"),"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			filter.put("stat", "1");
			total=this.anotherPlaceManager.getForDetilDangerousTotal(xh, filter, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	@RequestMapping
	@ResponseBody
	public Map queryForDetilList(HttpServletRequest request,HttpServletResponse response,String rows ,String page,String xh ,String hpys,String hphm,String hpzl){
		Map filter=new HashMap();
		Map result=null;
		try{
			String _hpys = request.getParameter("hpys");
			String sign = request.getParameter("sign");
			filter.put("xh", xh);
			filter.put("sign", sign);
			filter.put("hphm",java.net.URLDecoder.decode(hphm,"UTF-8"));
			if (StringUtils.isBlank(hpzl)) {
				filter.put("hpzl", "");
			} else {
				filter.put("hpzl", java.net.URLDecoder.decode(hpzl, "UTF-8"));
			}
			if (StringUtils.isBlank(hpys)) {
				filter.put("hpys", "");
			} else {
				filter.put("hpys",java.net.URLDecoder.decode(hpys,"UTF-8"));
			}
			filter.put("page",page);
			filter.put("rows", rows);
		    result = this.anotherPlaceManager.queryForDetilList(filter);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public String sureDeleteTask(HttpServletRequest request,
			HttpServletResponse response, String xh) {
		Map filter = new HashMap();
		String flag = "";
		try {
			if (xh != null && !xh.equals("")) {
				filter.put("xh",xh);
				if (!this.anotherPlaceManager.sureDeleteTask(filter).equals(""))
					return "success";
				;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping
	public Object findwordDangerousMain(HttpServletRequest request ,HttpServletResponse response){
		return new ModelAndView("analysis/highDangerousSet");
	}
	
	//构建地市树
	@SuppressWarnings("unchecked")
	@RequestMapping
	@ResponseBody
	public List<Map<String,Object>> cityTree(HttpServletRequest request ,HttpServletResponse response){
		List<Map<String,Object>> menuNodes=new ArrayList<Map<String,Object>>();
		try{
			List list=this.anotherPlaceManager.cityTree();
			for(int i=0;i<list.size();i++)
			{
				Map<String,Object> item = new HashMap<String,Object>(); 
				Map map=(Map)list.get(i);
				item.put("id", map.get("ID"));
				item.put("pId", map.get("PID"));
				//item.put("province", map.get("PROVINCE"));
				if(map.get("CITY")==null)
					item.put("city", "");
				else
					item.put("city", map.get("CITY"));
				if(map.get("CITY")==null||map.get("NAME")==null){
					item.put("name",map.get("PROVINCE"));
					//item.put("nocheck", "true");
				}
				else
				item.put("name",map.get("CITY")+"-"+map.get("NAME"));
				if(map.get("NAME")==null)
					item.put("hp", "");
				else
					item.put("hp",map.get("NAME"));
				//item.put("check", "true");
				menuNodes.add(item);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return menuNodes;
	}
	
	
	/**
	 * 添加高危地区组
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object insertDangerousArray(HttpServletRequest request ,HttpServletResponse response){
		
		Map filter=new HashMap();
		filter.put("arrayname", request.getParameter("title"));
		filter.put("city", request.getParameter("hp"));
		String result="";
		try {
			if(this.anotherPlaceManager.insertDangerousArray(filter)>0)
				result="success";
					} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 根据条件查询高危地区组
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object queryForDangerous(HttpServletRequest request,HttpServletResponse response){
		Map filter=new HashMap();
		filter.put("arrayname", request.getParameter("title"));
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		Map resultMap=null;
		try {
			resultMap=this.anotherPlaceManager.queryDangerous(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	
	/**
	 * 根据任务id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object deleteById(HttpServletRequest request ,HttpServletResponse response){
		String result="";
		Map filter=new HashMap();
		filter.put("id", request.getParameter("xh"));
		try {
			if(this.anotherPlaceManager.deleteById(filter)>0)
				result="success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 
	 * 导出异地牌专题分析Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getanotherXls(HttpServletRequest request,HttpServletResponse response, String rows,String page,String taskname,String xh,String kssj,String jssj) {
		String[] headName = new String[]{"任务名称", "时间任务", "条件", "过境型","候鸟型","本地型","合计"};
		short[] columnWidth = new short[]{6000,5000,30000,3000,3000,3000,3000}; 
		String[] method = new String[]{"taskname","xh","condision","gjxs","hnxs","bdxs","total"};
		String name ="异地牌专题分析";
		try {			
			List<Map<String,Object>> infos =(List<Map<String,Object>>) this.findtask(request, response, rows, page, taskname, xh, kssj, jssj).get("rows");
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
