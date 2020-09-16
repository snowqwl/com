package com.sunshine.monitor.system.analysis.action;

import static com.sunshine.monitor.comm.util.Common.putReParamToHttpP;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSONArray;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.analysis.bean.SuitLicense;
import com.sunshine.monitor.system.analysis.bean.WhilteName;
import com.sunshine.monitor.system.analysis.dao.XySuitLicenseDao;
import com.sunshine.monitor.system.analysis.service.SuitLicenseManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/suitLicense.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuitLicenseController {
	
	@Autowired
	private SuitLicenseManager suitLicenseManager;
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private XySuitLicenseDao xySuitLicenseDao;
	
	@ResponseBody
	@RequestMapping
	public Map deleteWhite(HttpServletRequest request) throws Exception {
		Map mapResult=new HashMap<>();
		String temp=request.getParameter("hphmsss");
		JSONArray jar=JSONArray.parseArray(temp);
		mapResult=suitLicenseManager.deleteWhite(jar);
		return mapResult;
	}

	@ResponseBody
	@RequestMapping
	public Map addWhite(HttpServletRequest request, WhilteName wn) throws Exception {
		Map mapResult=new HashMap<>();
		int i=suitLicenseManager.insertWhite(wn);
		if(i>0){
			mapResult.put("msg", "新增成功");
		}else if(i==-1){
			mapResult.put("msg", "车牌和种类已存在");
		}else {
			mapResult.put("msg", "新增失败");
		}
		return mapResult;
	}
	
	
	@ResponseBody
	@RequestMapping
	public Map gethpzl(HttpServletRequest request) throws Exception {
		Map mapResult=new HashMap<>();
		List<Map>  list=new ArrayList<Map>();
		list=suitLicenseManager.queryhpzl();
		mapResult.put("data", list);
		return mapResult;
	}
	
	@ResponseBody
	@RequestMapping
	public Object querywhitelist(HttpServletRequest request, WhilteName condition) {
		Map<String, Object> result = null ;
		try {
			result = new HashMap<String, Object>();
			UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
			Map<String,Object> paras = Common.getParamentersMap(request);
			if(userSession != null) {
				result = this.suitLicenseManager.whiteList(paras);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	
	@RequestMapping
	@ResponseBody
	public Object getMorePic(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		Map<String, Object> conditions = Common.getParamentersMap(request);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		PropertiesConfiguration config;
		config = new PropertiesConfiguration("ipport.properties");
		String deckip = config.getString("ydbpssip");
		String deckport = config.getString("ydbpssport");
		try {
				Map map = Common.sendHttpClientNew("/vehicle/api/data/rest/getinfos",  putReParamToHttpP(conditions), deckip, deckport);
				System.out.println("结果"+map);
				list=(List<Map<String, Object>>) map.get("list");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 跳转到套牌分析查询主页
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request) {
		try{
		//号牌颜色集合
		request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
		//号牌种类集合
		request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		//市州集合
		request.setAttribute("szList", this.xySuitLicenseDao.getSz());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("analysis/suitLicenseMain");
	}
	
	/**
	 * 套牌库统计
	 * @param request
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map querySuitLicense(HttpServletRequest request, String rows, String page) {
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		
		Map filter = new HashMap();
		filter.put("curPage", page);
		filter.put("pageSize", rows);
		return this.suitLicenseManager.querySuitLicense(filter, kssj, jssj);
	}
	
	/**
	 * 嫌疑套牌统计
	 * @param request
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String,Object>> queryXySuitLicense(HttpServletRequest request, String rows, String page) throws Exception {
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String zt = request.getParameter("zt");
		/**
		 * add by huanghaip
		 * @date 2017-6-8
		 * 新增跨市查询条件
		 */
		String sfks = request.getParameter("sfks");
		String hphm = null;
		try {
			if(request.getParameter("hphm")!=null&&!"".equals(request.getParameter("hphm").toString())){
				hphm = URLDecoder.decode(request.getParameter("hphm"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String hpzl = request.getParameter("hpzl");
		String hpys = request.getParameter("hpys");
		/*
		 * 添加市州筛选查询条件 2019-05-27
		 */
		String szsx = request.getParameter("szsx");
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("sfks", sfks);
		filter.put("szsx", szsx);
		List<Map<String,Object>> list = this.suitLicenseManager.queryXySuitLicenseExt(filter, kssj, jssj, zt, hphm, hpzl, hpys);
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public Integer queryXySuitTotal(HttpServletRequest request, String rows, String page) {
		int total = 0;
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String zt = request.getParameter("zt");
		String hphm = request.getParameter("hphm");
		String sfks = request.getParameter("sfks");
		try {
			if(request.getParameter("hphm")!=null&&!"".equals(request.getParameter("hphm").toString())){
				hphm = URLDecoder.decode(request.getParameter("hphm"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String hpzl = request.getParameter("hpzl");
		String hpys = request.getParameter("hpys");
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("sfks", sfks);
		try {
			total = this.suitLicenseManager.getXySuitTotal(filter, kssj, jssj, zt, hphm, hpzl, hpys);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * 嫌疑套牌
	 * @param request
	 * @param rows
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping
	@ResponseBody
	public Map getXySuit(HttpServletRequest request,String rows, String page) throws Exception {
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String zt = request.getParameter("zt");
		String hpzl = request.getParameter("hpzl");
		
		request.setAttribute("hpyslist",this.systemManager.getCode("031001"));
		Map filter = new HashMap();
		filter.put("curPage", page);
		filter.put("pageSize", rows);
		return this.suitLicenseManager.getXySuit(filter, kssj, jssj, zt,hpzl);
	}
	
	/**
	 * 套牌车统计详情
	 * @param request
	 * @param rows
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Map getXySuitCount(HttpServletRequest request,String rows, String page) throws Exception {
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String hpzl = request.getParameter("hpzl");
		request.setAttribute("hpyslist",this.systemManager.getCode("031001"));
		Map filter = new HashMap();
		filter.put("curPage", page);
		filter.put("pageSize", rows);
		return this.suitLicenseManager.getSuitCount(filter, kssj, jssj, hpzl);
	}
	
	/**
	 * 获取相关轨迹列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping
	@ResponseBody
	public Object getXySuitList(HttpServletRequest request,String rows, String page) throws Exception {
		String hphm = request.getParameter("hphm");
		String hpzl = request.getParameter("hpzl");
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String zt = request.getParameter("zt");
		String hpys = request.getParameter("hpys");
		String gcxh1 = request.getParameter("gcxh1");
		String gcxh2 = request.getParameter("gcxh2");
		Map filter = new HashMap();
		filter.put("curPage", page);
		filter.put("pageSize", rows);
		filter.put("hpys", hpys);
		filter.put("gcxh1", gcxh1);
		filter.put("gcxh2", gcxh2);
		return this.suitLicenseManager.getSuitList(filter,hphm,hpzl, kssj, jssj, zt);
	}
	
	/**
	 * 查询套牌车轨迹列表
	 * @param request
	 * @param rows
	 * @param page
	 * @param suitLicense
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping
	@ResponseBody
	public Object getSuitVehList(HttpServletRequest request,String rows, String page, SuitLicense suitLicense) throws Exception {
		Map filter = new HashMap();
		filter.put("curPage", page);
		filter.put("pageSize", rows);
		return this.suitLicenseManager.getSuitVehList(filter, suitLicense);
	}
	
	/**
	 * 修改状态
	 * @param request
	 * @param suitLicense
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object updateZT(HttpServletRequest request) {
		Map<String,Object> suitLicense = new HashMap<String,Object>();
		Object hphmObj = request.getParameter("hphm");
		String hphm = null;
		if(hphmObj!=null&&!"".equals(hphmObj.toString())){
			hphm = hphmObj.toString();
			suitLicense.put("hphm",hphm);
		}
		Object hpzlObj = request.getParameter("hpzl");
		String hpzl = null;
		if(hpzlObj!=null&&!"".equals(hpzlObj.toString())){
			hpzl = hpzlObj.toString();
			suitLicense.put("hpzl",hpzl);
		}
		Object hpysObj = request.getParameter("hpys");
		String hpys = null;
		if(hpysObj!=null&&!"".equals(hpysObj.toString())){
			hpys = hpysObj.toString();
			suitLicense.put("hpys",hpys);
		}
		Object gcxh1Obj = request.getParameter("gcxh1");
		String gcxh1 = null;
		if(gcxh1Obj!=null&&!"".equals(gcxh1Obj.toString())){
			gcxh1 = gcxh1Obj.toString();
			suitLicense.put("gcxh1",gcxh1);
		}
		Object gcxh2Obj = request.getParameter("gcxh2");
		String gcxh2 = null;
		if(gcxh2Obj!=null&&!"".equals(gcxh2Obj.toString())){
			gcxh2 = gcxh2Obj.toString();
			suitLicense.put("gcxh2",gcxh2);
		}
		Object kdbhObj = request.getParameter("kdbh");
		String kdbh = null;
		if(kdbhObj!=null&&!"".equals(kdbhObj.toString())){
			kdbh = kdbhObj.toString();
			suitLicense.put("kdbh",kdbh);
		}
		Object tp1Obj = request.getParameter("tp1");
		String tp1 = null;
		if(tp1Obj!=null&&!"".equals(tp1Obj.toString())){
			tp1 = tp1Obj.toString();
			suitLicense.put("tp1",tp1);
		}
		Object ypyjObj = request.getParameter("ypyj");
		String ypyj = null;
		if(ypyjObj!=null&&!"".equals(ypyjObj.toString())){
			ypyj = ypyjObj.toString();
			suitLicense.put("ypyj",ypyj);
		}
		String newzt = request.getParameter("newzt");
		Map map = new HashMap();
		String msg = null;
		try {
			this.suitLicenseManager.updateSuitLicenseOracle(suitLicense, newzt);
			msg = "更新成功";
		} catch(Exception e) {
			msg = "更新失败";
		}
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 根据组合过车序号查询详细
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object querySuitInfoByMixGcxh(HttpServletRequest request){
		
		
		
		return null;
	}
	
	/**
	 * 根据条件查询套牌组合列表
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String, Object>> querySuitList(HttpServletRequest request,VehPassrec veh){
		Map<String, Object> filter=new HashMap<String, Object>();
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		List<Map<String, Object>> result=null;
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		try{
			hphm = URLDecoder.decode(hphm, "UTF-8");
			veh.setHphm(hphm);
			result=this.suitLicenseManager.querySuitListExt(filter, veh);
		}		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public Integer querySuitTotal(HttpServletRequest request,VehPassrec veh){
		int total = 0;
		Map<String, Object> filter=new HashMap<String, Object>();
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		try{
			hphm = URLDecoder.decode(hphm, "UTF-8");
			veh.setHphm(hphm);
			total=this.suitLicenseManager.getSuitTotal(filter, veh);
		}		catch(Exception e){
			e.printStackTrace();
		}
		return total;
	}
	

	/**
	 * 
	 * 导出套牌专题分析Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getSuitXls(HttpServletRequest request,HttpServletResponse response,String page,String rows) {
		String[] headName = new String[]{"号牌号码", "号牌颜色", "号牌种类", "违法信息关联","状态"};
		short[] columnWidth = new short[]{6000,5000,5000,4000,3000,6000,6000}; 
		String[] method = new String[]{"hphm","hpysmc","hpzlmc","wfxx","zt"};
		String name ="套牌专题分析";
		try {			
			List<Map<String,Object>> infos= this.queryXySuitLicense(request, rows, page);
			for(int i=0;i<infos.size();i++){
				Map<String,Object> map = infos.get(i);
				if(map.get("zt").equals("0")){
					map.put("zt", "未确认");
				}else if(map.get("zt").equals("1")){
					map.put("zt", "套牌");
				}else{
					map.put("zt", "非套牌");
				}
				
				if(map.get("wfxx")==null){
					map.put("wfxx", "0");
				}else{
					map.put("wfxx", " ");
				}
			}
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * 导出套牌专题分析Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getSecondxmls(HttpServletRequest request,HttpServletResponse response,String page,String rows) {
		Map<String, Object> filter=new HashMap<String, Object>();
		filter.put("page", request.getParameter("page"));
		filter.put("rows", request.getParameter("rows"));
		VehPassrec veh=new VehPassrec();
		veh.setHphm(request.getParameter("hphm2"));
		veh.setHpzl(request.getParameter("search_hpzl"));//种类
		veh.setHpys(request.getParameter("search_cpys"));//颜色
		veh.setKssj(request.getParameter("kssj_secondTab"));
		veh.setJssj(request.getParameter("jssj_secondTab"));
		
		String[] headName = new String[]{"号牌号码", "号牌种类", "号牌颜色", "车辆品牌","行政区划","入库时间","过车图片1" ,"过车图片2","过车图片3" };
		short[] columnWidth = new short[]{6000,5000,5000,4000,3000,6000,7000 , 7000 ,7000 }; 
		String[] method = new String[]{"hphm","hpzlmc","hpysmc","clpp","XZQHMC","rksj","gctp1" ,"gctp2" ,"gctp3" };
		String name ="套牌专题分析";
		try {			
			List<Map<String,Object>> dataList=this.querySuitList(request, veh);
			System.out.println(dataList.size());
			for(int i=0;i<dataList.size();i++){
				Map<String,Object> map = dataList.get(i);
				map.put("CLPP",map.get("CLPP")==null?"":map.get("CLPP").toString() );
				map.put("XZQHMC",map.get("XZQHMC")==null?"":map.get("XZQHMC").toString() );
			}
			exportCommon.exportExecl2(dataList, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
