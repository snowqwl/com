package com.sunshine.monitor.system.susp.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.text.SimpleDateFormat;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.service.ReSuspinfoQueryManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoApproveManager;

@Controller
@RequestMapping(value = "/suspinfoApprove.do", params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspinfoApproveCtroller {
	
	@Autowired
	@Qualifier("suspinfoApproveManager")
	private SuspinfoApproveManager suspinfoApproveManager;
	
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	private ReSuspinfoQueryManager ksspqkService;
	
	private Logger logger = LoggerFactory.getLogger(SuspinfoApproveCtroller.class);
	
	private HttpUtils hu=new HttpUtils();
	
	/**
	 * 当前时间
	 * @return
	 */
	public String getCurtime(){
		long l=System.currentTimeMillis();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(l));
	}
	
	
	@RequestMapping(params="method=callKs", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> callKs (HttpServletRequest request,
			HttpServletResponse response) throws Exception{ 
		Map<String, Object> result=new HashMap<String, Object>();
		String  time1=getCurtime();
		Map<String, Object> map=new HashMap<String, Object>();
		String bkxh=request.getParameter("bkxh");
		VehSuspinfo v=(VehSuspinfo) getApproveDetailInfo(request, response, bkxh);
		String bkfw=v.getBkfw();
		map=packMap(v, request);
		List<Map<String, Object>> list=getsfBybkfw(bkfw);
		List<Map<String, Object>> listrows=new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> temp=new HashMap<String, Object>();
			temp.put("bkxh", v.getBkxh());
			temp.put("fqsj", time1);
			temp.put("sf", list.get(i).get("sf"));
			temp.put("ds", list.get(i).get("ds"));
			temp.put("fksj", getCurtime());
			String res="失败";
			try {
				Map mm=call(map, list.get(i).get("sfdm").toString());
				if(mm.containsKey("resultCode")&&mm.get("resultCode")!=null && mm.get("resultCode").equals("OK")){
					res="成功";
				}
				System.out.println(mm.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			temp.put("fkjg", res);
			listrows.add(temp);
		}
		try {
			List l=ksspqkService.query(bkxh);
			if(l.size()<=0){
				ksspqkService.insert(listrows);
			}else{
				ksspqkService.updates(listrows);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result.put("rows", listrows);
		return  result;
	}
	
	public String changebkfw(String str){
		String bkfw="";
		String []sarr=str.split(",");
		for (int i = 0; i < sarr.length; i++) {
			if(i==sarr.length-1){
				bkfw=bkfw+sarr[i].substring(0, 4);
			}else{
				bkfw=bkfw+sarr[i].substring(0, 4)+",";
			}
		}
		return bkfw;
	}
	
	/**
	 * 封装参数
	 * @param v
	 * @param request
	 * @return
	 */
	public Map<String, Object> packMap(VehSuspinfo v,HttpServletRequest request){
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		SysUser user = userSession.getSysuser();
		String sfzmhm=user.getSfzmhm();
		Map<String, Object> m=new HashMap<String, Object>();
		m.put("sfzmhm", sfzmhm);
		m.put("xh", v.getBkxh());
		m.put("hpzl", v.getHpzl());
		m.put("hphm", v.getHphm());
		m.put("bkdl", v.getBkdl());
		m.put("bklb", v.getBklb());
		m.put("bkqssj", v.getBkqssj());
		m.put("bkjzsj", v.getBkjzsj());
		m.put("jyaq", v.getJyaq());
		m.put("bkfw", changebkfw(v.getBkfw()) );
		m.put("bkjb", v.getBkjb());
		m.put("sqsb", v.getSqsb());
		m.put("dxjshm", v.getDxjshm());
		m.put("bkr", v.getBkr());
		m.put("bkrjh", v.getBkrjh());
		m.put("bkrmc", v.getBkrmc());
		m.put("bkjg", v.getBkjg());
		m.put("bkjgmc", v.getBkjgmc());
		m.put("bkjglxdh", v.getBkjglxdh());
		m.put("bksj", v.getBksj());
		m.put("bkpt", "43");
		return m;
	}
	
	
	public Map call( Map<String, Object> map , String sfdm) throws ConfigurationException{
		return hu.callService(map, getUrl(sfdm));	
	}
	
	public String getUrl(String sfdm) throws ConfigurationException{
		PropertiesConfiguration config;
		config = new PropertiesConfiguration("ipport.properties");
		String url = config.getString("sp.ipport."+sfdm);
		return url;
	}
	
	public List<Map<String, Object>> getsfBybkfw(String bkfw) throws Exception{
		List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
		String[] sarr=bkfw.split(",");
		List<String> temp=new ArrayList<String>();
		for (int i = 0; i < sarr.length; i++) {
			temp.add(sarr[i].substring(0, 2));
		}
		temp=removeDuplicate(temp);
		temp.remove("43");
		for (int i = 0; i < temp.size(); i++) {
			Map<String, Object> m=new HashMap<String, Object>();
			String sf=systemManager.getCodeValue("000033", temp.get(i)+"0000" );
//			if(temp.get(i).equals("43")){
//				sf="福南省";
//			}else if(temp.get(i).equals("44")){
//				sf="广东省";
//			}else if(temp.get(i).equals("45")){
//				sf="广西壮族自治区";
//			}else if(temp.get(i).equals("46")){
//				sf="海南省";
//			}else if(temp.get(i).equals("35")){
//				sf="福建省";
//			}else if(temp.get(i).equals("36")){
//				sf="江西省";
//			}
			m.put("sf", sf);
			m.put("sfdm", temp.get(i));
			String ds="";
			for (int j = 0; j < sarr.length; j++) {
				String s=sarr[j].substring(0, 6);
				if(s.startsWith(temp.get(i)) ){
					String cityValue=systemManager.getCodeValue("000033",s );
					if(j==sarr.length-1){
						ds+=cityValue+" ";
					}else{
						ds+=cityValue+",";
					}
				}
			}
			m.put("ds", ds);
			list.add(m);
		}
		return list;
	}
	
	public static List removeDuplicate(List list) {   
	    HashSet h = new HashSet(list);   
	    list.clear();   
	    list.addAll(h);   
	    return list;   
	}   
	
	
	/**
	 * 跳转相应的审批主页
	 * @param response
	 * @param request
	 * @param approveType
	 * @param begin
	 * @param end
	 * @param param
	 * @return
	 */
	@RequestMapping(params="method=index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,String approveType,String begin,String end,String param,String isFromIndex) {
		ModelAndView mv = null;
		List bkdlList = null;
		List bklbList = null;
		try {
			if("".equals(isFromIndex) || isFromIndex==null){
				isFromIndex="0";//默认为0，表示号牌号码默认为"湘"字，1表示从首页传过来的，页面不显示"湘"
			}
			
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			if (approveType != null && approveType.equals("classApprove")) {
			   bklbList = this.systemManager.getCodesByDmsm("120005", "1", 4);
			   
			   mv = new ModelAndView("engineer/approveclassmain");
			} else if (approveType != null && approveType.equals("trafficApprove")) {
				bklbList = this.systemManager.getCodesByDmsm("120005", "2", 4);
				mv = new ModelAndView("engineer/approvetrafficmain");
			}else { 
			   bkdlList = this.systemManager.getCode("120019");
			   bklbList = this.systemManager.getCode("120005");
			   mv = new ModelAndView("engineer/approvemain");
			}
		
			if (param != null && param.equals("overtimecount")) {
				end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				begin = this.systemManager.getSysDate("-180", false)
						+ " 00:00:00";
				request.setAttribute("approveovertime", "1");
				bkdlList = systemManager.getCode("120019");
				if(bkdlList.size()>=2)
				bkdlList.remove(1);
			}
			
			List hpzlList = this.systemManager.getCode("030107");
			List auditList = this.systemManager.getCode("190002");
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklbList", bklbList);
			map.put("auditList", auditList);
			map.put("begin", begin);
			map.put("end", end);
			map.put("ywzt", "12");
			mv.addObject("codeMap",map);
			//暂时使用云浮市公安机构代码,正常为登录用户所在部门
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			SysUser user = userSession.getSysuser();
			Department dp = userSession.getDepartment();
			request.setAttribute("yhmc", user.getYhmc());
			request.setAttribute("bmmc", dp.getBmmc());
			request.setAttribute("isFromIndex", isFromIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
		
	}
	/**
	 * 跳转布控审批详细页面
	 * @param bkxh
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping
	public String forwardWin(HttpServletRequest request,@RequestParam String bkxh){
		try {
			List auditList = this.systemManager.getCode("190002");
			
			request.setAttribute("auditList", auditList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();
		request.setAttribute("yhmc", user.getYhmc());
		request.setAttribute("bmmc", dp.getBmmc());
		request.setAttribute("bkxh", bkxh);
		
		return "engineer/approvewin";
	}
	
	/**
	 * 布控指挥中心审批查询列表
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params="method=queryList", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList (HttpServletRequest request, HttpServletResponse response, 
			String rows, String page, VehSuspinfo command) {
		Map map = null;
		try {
			Map filter = new HashMap();
			VehSuspinfo vehSuspinfo = command;
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			
			//暂时使用云浮公安机构代码,正常为登录用户的部门代码
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String glbm = userSession.getDepartment().getGlbm();
			
			if (request.getParameter("overtime") != null) {
				if (request.getParameter("overtime").equals("1")) {
					map = this.suspinfoApproveManager.getSuspinfoApprovesOverTime(
							filter, vehSuspinfo, glbm);
				}
			} else
				map = this.suspinfoApproveManager.getSuspinfoApproves(filter,
						vehSuspinfo, glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 布控指挥中心涉案类审批查询列表
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params="method=queryListClassApprove", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryListClassApprove (HttpServletRequest request, HttpServletResponse response, 
			String rows, String page, VehSuspinfo command) {
		Map map = null;
		try {
			Map filter = new HashMap();
			VehSuspinfo vehSuspinfo = command;
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			
			//暂时使用云浮公安机构代码,正常为登录用户的部门代码
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String glbm = userSession.getDepartment().getGlbm();
			map = this.suspinfoApproveManager.getSuspinfoClassApproves(filter, vehSuspinfo, glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 布控指挥中心交通类审批查询列表
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params="method=queryListTrafficApprove", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryListTrafficApprove (HttpServletRequest request, HttpServletResponse response, 
			String rows, String page, VehSuspinfo command) {
		Map map = null;
		try {
			Map filter = new HashMap();
			VehSuspinfo vehSuspinfo = command;
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			
			//暂时使用云浮公安机构代码,正常为登录用户的部门代码
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String glbm = userSession.getDepartment().getGlbm();
			map = this.suspinfoApproveManager.getSuspinfoTrafficApproves(filter, vehSuspinfo, glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	
	@RequestMapping(params="method=getApproveDetailInfo")
	@ResponseBody
	public Object getApproveDetailInfo(HttpServletRequest request,HttpServletResponse response,String bkxh) {
		
		//暂时使用云浮公安机构代码,正常为登录用户的部门代码
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		String glbm = userSession.getDepartment().getGlbm();
		VehSuspinfo vehSuspinfo = null;
		try {
			vehSuspinfo = (VehSuspinfo) this.suspinfoApproveManager.getApproveDetailForBkxh(bkxh, glbm);
			//List list = this.suspinfoApproveManager.getSuspinfoAuditHistory(bkxh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehSuspinfo;
	}
	
	@RequestMapping(params="method=saveApprove", method=RequestMethod.POST)
	@ResponseBody
	public Object saveApprove(HttpServletRequest request, HttpServletResponse response,
			AuditApprove info) {
		Map map = null;
		//**************暂时使用云浮公安机构编码,登录用户的信息****************
		//User user = new User();
//		user.setYhdh("shengting");
//		user.setYhmc("省厅");
//		user.setGlbm("445300000000");
//		user.setBmmc("云浮市公安局");
//		//**************暂时使用云浮公安机构编码,登录用户的信息****************
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		SysUser user = userSession.getSysuser();
		info.setCzr(user.getYhdh());
		info.setCzrmc(user.getYhmc());
		info.setCzrdw(user.getGlbm());
		info.setCzrdwmc(userSession.getDepartment().getBmmc());
		info.setCzrjh(user.getJh());
       try {
			map = this.suspinfoApproveManager.saveApprove(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 批量审批
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params="method=batchSaveApprove", method=RequestMethod.POST)
	@ResponseBody
	public Object batchSaveApprove(HttpServletRequest request, HttpServletResponse response) {
		AuditApprove info = new AuditApprove();
		Map<String,String> map = new HashMap<String,String>();
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		SysUser user = userSession.getSysuser();
		int count = 0;
		int fale = 0;
		try {
			List<Map<String,Object>> list = suspinfoApproveManager.getBatchApproveList();
			if(list.size() > 0){
				for(Map<String,Object> resMap : list){
					info.setBkxh(String.valueOf(resMap.get("BKXH")));
					info.setCzjg("1");
					info.setMs("同意");
					info.setCzr(user.getYhdh());
					info.setCzrmc(user.getYhmc());
					info.setCzrdw(user.getGlbm());
					info.setCzrdwmc(userSession.getDepartment().getBmmc());
					info.setCzrjh(user.getJh());
					map = this.suspinfoApproveManager.saveApprove(info);
					if(map.get("code").equals("1"))
						count ++;
					else
						fale ++;
				}
			}
			logger.info("批量布控审批查询结果：" + list);
		} catch (Exception e) {
			fale ++;
			logger.error(e.getMessage());
			//e.printStackTrace();
		}
		/*String bkxhs = request.getParameter("bkxhs");
		String[] bkxh = bkxhs.split(",");
		for(String xh : bkxh){
			info.setBkxh(xh);
			info.setCzjg("1");
			info.setMs("同意");
			info.setCzr(user.getYhdh());
			info.setCzrmc(user.getYhmc());
			info.setCzrdw(user.getGlbm());
			info.setCzrdwmc(userSession.getDepartment().getBmmc());
			info.setCzrjh(user.getJh());
	       try {
				map = this.suspinfoApproveManager.saveApprove(info);
				if(map.get("code").equals("1"))
					count ++;
				else
					fale ++;
			} catch (Exception e) {
				fale ++;
				logger.error(e.getMessage());
				//e.printStackTrace();
			}
		}*/
		String msg = "成功审批" + count + "条，失败" + fale + "条。";
		map.put("msg", msg);
		return map;
	}
	
	
	@RequestMapping(params="method=getApproveHistoryInfo")
	@ResponseBody
	public Object getApproveHistoryInfo(HttpServletRequest request,HttpServletResponse response, String bkxh) {
		StringBuffer sb = null;
		Map result = null;
	   try {
		   result = new HashMap();
		List list = this.suspinfoApproveManager.getSuspinfoAuditHistory(bkxh);
		result.put("suspinfoAuditHistory", list);
	} catch (Exception e) {
		e.printStackTrace();
	}
		return result;
	}

	public SuspinfoApproveManager getSuspinfoApproveManager() {
		return suspinfoApproveManager;
	}

	public void setSuspinfoApproveManager(
			SuspinfoApproveManager suspinfoApproveManager) {
		this.suspinfoApproveManager = suspinfoApproveManager;
	}

	public SystemManager getSystemManager() {
		return systemManager;
	}

	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}
}
