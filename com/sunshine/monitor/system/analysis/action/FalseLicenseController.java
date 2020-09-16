package com.sunshine.monitor.system.analysis.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.comm.util.ExcelUtil;
import com.sunshine.monitor.comm.util.ftp.FtpUtil;
import com.sunshine.monitor.comm.util.http.HttpXmlClient;
import com.sunshine.monitor.system.analysis.bean.FalseLicense;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.FalseLicenseManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/false.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FalseLicenseController {

	@Autowired
	@Qualifier("falseManager")
	private FalseLicenseManager falseManager;
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	private DirectDao directDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	@Qualifier("scsVehPassrecDao")
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;

	@RequestMapping()
	public String findForward(HttpServletRequest request) {
		try{
			//号牌颜色集合
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			//号牌种类集合
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		return "analysis/falselicensemain";
	}
	
	@RequestMapping()
	public String findForwardQuery(){
		return "analysis/queryfalselicense";
	}

	@Autowired
	private SystemManager systemManager;

	@RequestMapping
	@ResponseBody
	public Object uploadList(HttpServletRequest request,HttpServletResponse response) {
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile excelFile = mrequest.getFile("xlsfile");
		List<FalseLicense> list = ExcelUtil.xlsToBean(excelFile, FalseLicense.class);
		HashSet<FalseLicense> set = new HashSet<FalseLicense>(list);
		list.clear();
		list.addAll(set);
		try {
			for(FalseLicense fa:list){
			   fa.setHpzlmc(this.systemManager.getCodeValue("030107",fa.getHpzl()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object> findPageForFL(HttpServletRequest request,
			HttpServletResponse response,String page,String rows,FalseLicense fl){
		Map<String,Object> queryMap = null;
		try {
			Map<String,Object> filter = new HashMap<String,Object>();
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			queryMap = this.falseManager.findPageForFL(filter, fl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		   return queryMap;
	}
	
	@RequestMapping
	@ResponseBody
	public Map<String, String> delFalseLicense(HttpServletRequest request,
			HttpServletResponse response) {
		List<FalseLicense> list = null;
		String data = request.getParameter("jsonstr");
		JSONArray array = JSONArray.fromObject(data);
		list = array.toList(array, FalseLicense.class);
		try {
			this.falseManager.delFalseLicense(list);
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("出现异常", "1");
		}
		return Common.messageBox("删除成功", "0");
	}
	
	/**
	 * 计算不同状态的假牌数量 zt 0为未确认 1为假牌 2为非假牌
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getFalseCount(HttpServletRequest request,HttpServletResponse response,String page,String rows,String kssj,String jssj){
		Map filter=new HashMap();
		filter.put("rows", rows);
		filter.put("page",page);
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		Map map=null;
		try {
			 map=this.falseManager.getFalseCount(filter);
			 List list=(List)map.get("rows");
			 for(int i=0;i<list.size();i++){
				 Map maptime=(Map)list.get(i);
				 maptime.put("startTime", maptime.get("TJ").toString()+" 00:00:00");
				 maptime.put("endTime", maptime.get("TJ").toString()+" 23:59:59");
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	//页面跳转
	@RequestMapping
	public String forwardSuspectedList(HttpServletRequest request,HttpServletResponse response,String page,String rows,String sign,String tjsj){
		request.setAttribute("rows", rows);
		request.setAttribute("page", page);
		request.setAttribute("sign", sign);
		request.setAttribute("tjsj", tjsj);

		return "analysis/falselicensesuspected";
	}
	
	//根据假牌状态获取假牌数据列表
	@RequestMapping
	@ResponseBody
	public Map getSuspectedList(HttpServletRequest request,HttpServletResponse response,String page,String rows,String sign,String tjsj){
		Map filter =new HashMap();
		filter.put("rows",rows);
		filter.put("page", page);
		filter.put("sign", sign);
		filter.put("tjsj", tjsj);
	
		Map map=null;
		try{
		map=	this.falseManager.getSuspectedListBysign(filter);	
		List list=(List) map.get("rows");
		List<String> hphms = new ArrayList<String>();
		for(int i = 0;i<list.size();i++){
			 Map vehmap=(Map) list.get(i);
		     hphms.add(vehmap.get("hphm").toString());
		     if(vehmap.get("hpzl").equals("02")){
			 vehmap.put("HPYSMC","蓝牌");
		     }else{
		    	 vehmap.put("HPYSMC","其他牌");
		     }
				}
		
		
		//获取违法关联数      
        //Map<String,Object> wfxx=this.scsVehPassrecDao.getTrafficCount(hphms);
        Map<String,Object> wfxx=this.illegalZYKDao.getTrafficCount(hphms);
        for(int i=0;i<list.size();i++){
            Map<String,Object> map2 = (Map<String, Object>) list.get(i);
            map2.put("wfxx",wfxx.get(map2.get("hphm").toString()));
        }
        map.put("rows", list);
		}

        
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 假牌过车查询列表
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param sign
	 * @param tjsj
	 * @param hphm
	 * @param hpzl
	 * @param zt
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map getVehList(HttpServletRequest request,HttpServletResponse response,String page,String rows,String sign,String tjsj,String hphm,String hpzl,String zt){
		String dhphm = "";
		try {
			dhphm = java.net.URLDecoder.decode(hphm, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		Map filter =new HashMap();
		filter.put("rows",rows);
		filter.put("page", page);
		filter.put("hphm", dhphm);
		filter.put("tjsj", tjsj);
		filter.put("hpzl", hpzl);
		filter.put("zt", zt);
		Map map=null;
		try{
		map=	this.falseManager.getFalseListByHphm(filter);	
		List list=(List) map.get("rows");
		for(int i = 0;i<list.size();i++){
			 Map vehmap=(Map) list.get(i);
			 if(vehmap.get("hpzl").equals("02")){
				 vehmap.put("HPYSMC","蓝牌");
				     }else{
				    	 vehmap.put("HPYSMC","其他牌");
				     }
			// vehmap.put("HPYSMC", this.systemDao.getCodeValue("031001", vehmap.get("hpys").toString()));
			 vehmap.put("KDMC", this.gateDao.getOldGateName(vehmap.get("kdbh").toString()));
			 vehmap.put("FXMC", this.directDao.getOldDirectName(vehmap.get("fxbh").toString()));
			 vehmap.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(vehmap.get("kdbh").toString().substring(0, 6)));
		}
		}	
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 图片浏览列表
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param sign
	 * @param tjsj
	 * @param hphm
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map getVehPic(HttpServletRequest request,HttpServletResponse response,String page,String rows,String sign,String tjsj,String hphm){
		String dhphm = "";
		try {
			dhphm = java.net.URLDecoder.decode(java.net.URLDecoder.decode(hphm, "UTF-8"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		Map filter =new HashMap();
		filter.put("rows","6");
		filter.put("page", "1");
		filter.put("hphm", dhphm);
		filter.put("tjsj", tjsj);
		Map map=null;
		try{
		map=	this.falseManager.getFalseListByHphm(filter);		
		}	
		catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 确认假牌
	 * @param request
	 * @param response
	 * @param flag
	 * @param hphm
	 * @param hpzl
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public String sureFalse(HttpServletRequest request,HttpServletResponse response ,String flag,String hphm,String hpzl){
		String data=null;
		try {
			data =this.falseManager.sureFalse(URLDecoder.decode(hphm, "utf-8"),flag,hpzl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	/**
	 * 假牌专题库查询主列表
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping
	@ResponseBody
	public Object queryFalseList(HttpServletRequest request,VehPassrec veh) throws UnsupportedEncodingException{
		String hphm=null;
		if((String)request.getParameter("hphm")!=null&&!"".equals((String)request.getParameter("hphm"))){
			hphm = (String)request.getParameter("hphm").toString();
			hphm=URLDecoder.decode(hphm, "UTF-8");
		}
		Map filter=new HashMap();
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		filter.put("kssj",(String)request.getParameter("kssj")!=null?request.getParameter("kssj").toString():null);
		filter.put("jssj",(String)request.getParameter("jssj")!=null?request.getParameter("jssj").toString():null);
		filter.put("hphm",hphm);
		filter.put("hpzl",(String)request.getParameter("hpzl")!=null?request.getParameter("hpzl").toString():null);
		filter.put("hpys",(String)request.getParameter("hpys")!=null?request.getParameter("hpys").toString():null);
		Map result=null;
		try{
		 result=this.falseManager.queryFalseList(filter, veh);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 假牌嫌疑库分析（greemplum）
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping
	@ResponseBody
	public Object queryXyFalseList(HttpServletRequest request,HttpServletResponse response, String page, String rows,
			VehPassrec veh,String sign,String zt) throws UnsupportedEncodingException{
		Map filter = new HashMap();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("zt",(String)request.getParameter("zt")!=null?request.getParameter("zt").toString():null);
		List<Map<String, Object>> list = null;
		try{
			list = this.falseManager.queryXyFalseListExt(filter,veh);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 假牌嫌疑库分析（greemplum）-- 总数
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping
	@ResponseBody
	public Object queryXyFalseListTotal(HttpServletRequest request,HttpServletResponse response, String page, String rows,
			VehPassrec veh,String sign,String zt) throws UnsupportedEncodingException{
		Map filter = new HashMap();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("zt",(String)request.getParameter("zt")!=null?request.getParameter("zt").toString():null);
		String hphm = veh.getHphm();
		try {
			hphm = URLDecoder.decode(hphm, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		veh.setHphm(hphm);
		Integer total = 0;
		try{
			total = this.falseManager.queryXyFalseListTotal(filter,veh);
		}catch(Exception e){
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * 嫌疑假牌页面，根据号牌号码查询所有识别出来的假牌信息-列表
	 * liumeng 2016-11-23
	 */
	@RequestMapping
	@ResponseBody
	public Object getXyFalseData(HttpServletRequest request,HttpServletResponse response, String page, String rows) throws UnsupportedEncodingException{
		Map<String,Object> filter = new HashMap<String, Object>();
		filter.put("page", 1);
		filter.put("rows", 10);//查询最新的前10条
		String hphm=null;
		if((String)request.getParameter("hphm")!=null&&!"".equals((String)request.getParameter("hphm"))){
			hphm = (String)request.getParameter("hphm").toString();
			hphm=URLDecoder.decode(hphm, "UTF-8");
		}
		filter.put("kssj",(String)request.getParameter("kssj")!=null?request.getParameter("kssj").toString():null);
		filter.put("jssj",(String)request.getParameter("jssj")!=null?request.getParameter("jssj").toString():null);
		filter.put("hphm",hphm);
		filter.put("hpzl",(String)request.getParameter("hpzl")!=null?request.getParameter("hpzl").toString():null);
		List<Map<String, Object>> list = null;
		try{
			list = this.falseManager.queryXyFalseListExt(filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 假牌嫌疑轨迹库oracle
	 * @param request
	 * @return
	 * @throws 
	 */
	@RequestMapping
	@ResponseBody
	public Object queryXyAllFalseList(HttpServletRequest request) throws UnsupportedEncodingException{
		Map filter = new HashMap();
		
		String hphm = (String)request.getParameter("hphm")!=null?request.getParameter("hphm").toString():null;
		hphm=URLDecoder.decode(hphm, "UTF-8");
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		//filter.put("rows", (String)request.getParameter("total")!=null?request.getParameter("total").toString():request.getParameter("rows"));
		filter.put("kssj",(String)request.getParameter("kssj")!=null?request.getParameter("kssj").toString():null);
		filter.put("jssj",(String)request.getParameter("jssj")!=null?request.getParameter("jssj").toString():null);
		filter.put("zt",(String)request.getParameter("zt")!=null?request.getParameter("zt").toString():null);
		filter.put("hphm",hphm);
		filter.put("hpzl",(String)request.getParameter("hpzl")!=null?request.getParameter("hpzl").toString():null);
		filter.put("hpys",(String)request.getParameter("hpys")!=null?request.getParameter("hpys").toString():null);
		Map result = null;
		try{
			result = this.falseManager.queryXyAllFalseList(filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 修改状态
	 * @param request
	 * @param suitLicense
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping
	@ResponseBody
	public Object updateZT(HttpServletRequest request) throws UnsupportedEncodingException {
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		SysUser user = userSession.getSysuser();
		String newzt = request.getParameter("newzt");
		String hpzl=request.getParameter("hpzl");
		String hpys=request.getParameter("hpys");
		String hphm = (String)request.getParameter("hphm")!=null?request.getParameter("hphm").toString():null;
		hphm=URLDecoder.decode(hphm, "UTF-8");
		Map map = new HashMap();
		String msg = null;
		try {
			this.falseManager.updateXyFalse(newzt,hphm,hpzl,hpys,user);
			//this.falseManager.updateXyFalse(hphm,hpzl);
			msg = "更新成功";
		} catch(Exception e) {
			msg = "更新失败";
		}
		map.put("msg", msg);
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object queryPass(HttpServletRequest request) throws UnsupportedEncodingException{
		Map filter = new HashMap();
		String gcxh = (String)request.getParameter("gcxh")!=null?request.getParameter("gcxh").toString():null;
		filter.put("gcxh", gcxh);
//		filter.put("page", request.getParameter("page"));
//		filter.put("rows", request.getParameter("rows"));
		filter.put("sjc", request.getParameter("sjc"));
		List result = null;
		try{
			result = this.falseManager.queryPass(filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public Object ftpDownload(HttpServletRequest request, HttpServletResponse response){
//		String path = "/SecondRecognitionLPR/downloadpic";//图片下载的地址
		String gcxh=request.getParameter("gcxhVeh");
		String picUrl = request.getParameter("gcxhPic");//图片地址
		Map map = new HashMap();
		try{
			if(picUrl == null || "".equals(picUrl)) {
				map.put("flag", "0");
				return map;
			}else {
				String pic[] = picUrl.split("/");
				String lastPic = pic[pic.length-1];
				if("loading3.gif".equals(lastPic)  || "no_img.jpg".equals(lastPic)){//过滤加载图片和无图片
					map.put("flag", "0");
					return map;
				}
				picUrl = URLDecoder.decode((String)picUrl, "UTF-8");
			}
			String saveFileName = gcxh + ".jpg";
			boolean flag = FtpUtil.downFileSecondPic(picUrl, saveFileName);
			if(!flag){
				map.put("flag", "0");
				return map;
			}
			Thread.currentThread().sleep(15000);//设置等待8秒
			String serviceUrl = "http://10.142.54.32:9081/secondrecognition/pic/V1/4300/"+gcxh;
			String respText = HttpXmlClient.get(serviceUrl);
			System.out.println("输出结果:" + respText);			
			if(respText!=null && respText!="{}"){
				respText = "["+respText+"]";
				JSONArray json =  JSONArray.fromObject(respText);
				String hpys = (String) json.getJSONObject(0).get("hpys");
				String hphm = (String) json.getJSONObject(0).get("hphm");
				String flagStr = (String) json.getJSONObject(0).get("flag");
			    map.put("hpys", hpys);
			    map.put("hphm", hphm);
			    map.put("flag", flagStr);
				return map;
			}else{
				map.put("flag", "0");
				return map;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 
	 * 导出嫌疑假牌Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getfalseXls(HttpServletRequest request,HttpServletResponse response, String page, String rows,
			VehPassrec veh,String sign,String zt) {
		String[] headName = new String[]{"号牌号码", "号牌种类", "号牌颜色", "过车频率"};
		short[] columnWidth = new short[]{3000,5000,5000,9000,3000,3000,3000,9000,12000,3000,3000,6000}; 
		String[] method = new String[]{"hphm","hpzlmc","hpysmc","total"};
		String name ="嫌疑假牌分析";
		try {			
			List<Map<String,Object>> infos=(List<Map<String,Object>>) this.queryXyFalseList(request, response, page, rows, veh, sign, zt);
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
}


