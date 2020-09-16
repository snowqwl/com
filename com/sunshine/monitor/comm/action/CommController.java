package com.sunshine.monitor.comm.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
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
import org.springframework.web.util.WebUtils;

import com.sunshine.core.util.FileUtil;
import com.sunshine.monitor.comm.bean.ITresource;
import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.bean.RoleMenu;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.log.LogHandlerInter;
import com.sunshine.monitor.comm.log.LogHandlerParam;
import com.sunshine.monitor.comm.log.LogInvocationHandler;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.service.RoleManager;
import com.sunshine.monitor.comm.service.RoleMenuManager;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.PrefectureDao;
import com.sunshine.monitor.system.manager.service.PrefectureManager;
import com.sunshine.monitor.system.manager.service.SysUserManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoEditManager;

@Controller
@RequestMapping(value="/comm.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CommController {	
	
	@Autowired
	@Qualifier("sysUserManager")
	private SysUserManager sysUserManager;
	
	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysParaManager;
	
	@Autowired
	@Qualifier("prefectureManager")
	private PrefectureManager prefectureManager;
	
	@Autowired
	private RoleManager roleManager;
	
	@Autowired
	@Qualifier("suspinfoEditManager")
	private SuspinfoEditManager suspinfoEditManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("urlManager")
	private UrlManager urlManager;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Autowired
	@Qualifier("prefectureDao")
	private PrefectureDao prefectureDao;
	
	@Autowired
	private RoleMenuManager roleMenuManager;
	
	@Autowired
	private LogHandlerInter logHandlerInter;
	
	@RequestMapping
	public ModelAndView toZoomJsp(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("zoomimage");
		return mv;
	}
	
	//构造警员树
	@RequestMapping
	@ResponseBody
	public List<Map<String,Object>> getPoliceTree(@RequestParam("glbm") String glbm,HttpServletRequest request){
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		
		Map map = new HashMap();
		map.put("glbm", glbm);
		List list =null;
		String leader = request.getParameter("leader");
		//判断是否只获取上级领导
		if(!StringUtils.isBlank(leader)){
			if("1".equals(leader)){
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			Department dp = userSession.getDepartment();
			map.put("glbm", dp.getGlbm());
			list = this.prefectureManager.getLeaderDepartmentTree(map);
			}
			if("2".equals(leader)){
				UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
				Department dp = userSession.getDepartment();
				map.put("glbm", dp.getGlbm());
				map.put("audit", "1");
				list = this.prefectureManager.getLeaderDepartmentTree(map);
				}
		}
		else
		list = this.prefectureManager.getDepartmentTree(map);
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				Map<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", m.get("glbm").toString());
				item.put("name", m.get("bmmc").toString().replaceAll(" ",""));
				item.put("pId", m.get("sjbm").toString());
				item.put("nocheck", "true");
				menuNodes.add(item);
			}
		}
		
		try {
			List listPolice=null;
			if(!StringUtils.isBlank(leader)){
				if("1".equals(leader)){
				UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
				Department dp = userSession.getDepartment();
				listPolice= sysUserManager.getLeaderUserListForGlbm(dp.getGlbm());
				}
				if("2".equals(leader)){
					UserSession userSession = (UserSession) WebUtils
							.getSessionAttribute(request, "userSession");
					Department dp = userSession.getDepartment();
					listPolice= sysUserManager.getLocalUserListForGlbm(dp.getGlbm());
					}
			}
			else
			listPolice= sysUserManager.getUserListForGlbm(glbm);
			for(Object o : listPolice){
				SysUser user = (SysUser)o;
				Map<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", user.getYhdh());
				
				item.put("pId", user.getGlbm());
				item.put("jh", user.getJh());
				item.put("yhmc", user.getYhmc());
				if(!StringUtils.isBlank(user.getLxdh3())){
					item.put("lxdh", user.getLxdh3());
					item.put("name", user.getYhmc()+":"+user.getLxdh3());
				}
				else{
					item.put("lxdh", user.getLxdh1());
					item.put("name", user.getYhmc()+":"+user.getLxdh1());
				}
				item.put("check", "{enable: true}");
				menuNodes.add(item);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuNodes;
	}
	
	
	
	
	@RequestMapping()
	@ResponseBody
	public Object getSuspinfoDetail(HttpServletRequest request){
		String bkxh = request.getParameter("bkxh");
		VehSuspinfo sb = null;
		
		try {
			sb = this.suspinfoEditManager.getSuspinfoDetailForBkxh(bkxh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	/**
	 * 统一跳转
	 * @author oy
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPage(HttpServletRequest request){
		String _page = request.getParameter("pg");
		try {
			_page = URLDecoder.decode(_page, "UTF-8");
			ModelAndView mv = null;
			if(_page != null && !"".equals(_page)){
				mv = new ModelAndView(_page);
				Enumeration<String> enumes = request.getParameterNames();
				while (enumes.hasMoreElements()) {
					String param = enumes.nextElement();
					if (param != null && param.length() > 0
							&& !"method".equalsIgnoreCase(param) && !"pg".equalsIgnoreCase(param)) {
						//new String(request.getParameter(param).getBytes("iso-8859-1"), "UTF-8");
						Object value = URLDecoder.decode(URLDecoder.decode(request.getParameter(param), "UTF-8"), "UTF-8");
						mv.addObject(param, value);
					}
				}
				return mv;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("main");
	}
	
	/**
	 * 根据条件展示机构菜单树
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=getDepartmentTreeByFilterMul")
	@ResponseBody
	public List<Map<String,Object>> getDepartmentTreeByFilterMul(@RequestParam("glbm") String glbm,@RequestParam("jb") String jb) {
		//String dep_id = request.getParameter("depId");
		Map map = new HashMap();
		map.put("glbm", glbm);
		map.put("jb",jb);
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		long begin = System.currentTimeMillis();
		List list = this.prefectureManager.getDepartmentTree(map);
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				
				String bmmc = m.get("bmmc").toString();
				if(bmmc.indexOf("撤销") < 0){
					Map<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", m.get("glbm").toString());
					item.put("name", m.get("bmmc").toString().replaceAll(" ",""));
					item.put("pId", m.get("sjbm").toString());
					
					item.put("py", m.get("py") == null?"":m.get("py").toString());
					menuNodes.add(item);
				}
				
			}
		}
		System.out.println("加载机构树所用时间:"+(System.currentTimeMillis()-begin));
		return menuNodes;
	}
	
	/**
	 * 根据条件展示机构菜单树_异步
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=getDepartmentTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getDepartmentTreeByFilter(HttpServletRequest request) {
		//String dep_id = request.getParameter("depId");
		String sjbm = request.getParameter("id");
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		List list = this.prefectureManager.getDepartmentTreeAsync(sjbm);
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				
				String bmmc = m.get("bmmc").toString();
				if(bmmc.indexOf("撤销") < 0){
					Map<String,Object> item = new HashMap<String,Object>(); 
					boolean isParent = this.prefectureManager.getCountForLowerDepartment(m.get("glbm").toString());
					item.put("id", m.get("glbm").toString());
					item.put("name", m.get("bmmc").toString().replaceAll(" ",""));
					item.put("pId", m.get("sjbm").toString());
					item.put("isParent", isParent);
					item.put("py", m.get("py") == null?"":m.get("py").toString());
					menuNodes.add(item);
				}
				
			}
		}
		return menuNodes;
	}
	/**
	 * 根据条件展示辖区机构菜单树_异步
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=getPrefectureTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getPrefectureTreeByFilter(HttpServletRequest request) {
		//String dep_id = request.getParameter("depId");
		UserSession userSession = (UserSession) WebUtils
		.getSessionAttribute(request, "userSession");
		Department dp = userSession.getDepartment();
		String sjbm = request.getParameter("id");
		String glbmn = dp.getGlbm();
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		List list = this.prefectureManager.getPrefectureTreeAsync(sjbm,glbmn);
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				
				String bmmc = m.get("bmmc").toString();
				if(bmmc.indexOf("撤销") < 0){
					Map<String,Object> item = new HashMap<String,Object>(); 
					boolean isParent = this.prefectureManager.getCountForLowerDepartment(m.get("glbm").toString());
					item.put("id", m.get("glbm").toString());
					item.put("name", m.get("bmmc").toString().replaceAll(" ",""));
					item.put("pId", m.get("sjbm").toString());
					item.put("isParent", isParent);
					item.put("py", m.get("py") == null?"":m.get("py").toString());
					menuNodes.add(item);
				}
				
			}
		}
		return menuNodes;
	}
	@RequestMapping(params = "method=getGateTreeByGlbm")
	@ResponseBody
	public List<Map<String,Object>> getGateTreeByGlbm(HttpServletRequest request) throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		String kd = "";
		 kd = URLDecoder.decode(request.getParameter("kdmc"), "UTF-8");
		////过滤输入的特殊字符//////////
		String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";   
        Pattern   p   =   Pattern.compile(regEx);      
        Matcher   mat   =   p.matcher(kd);  
        kd = mat.replaceAll("").trim();		
		//String dwdm = request.getParameter("dwdm");
        String dwdm="";
		try {
			///判断是否是省厅版/////
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			if(userSession.getDepartment().getJb().equals("1")){
				
				list = this.gateManager.getGateTreeByGlbmAndSt(kd,dwdm);	
			}
			else{				
				list = this.gateManager.getGateTreeByGlbm(kd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list.size()>0) {
			
			Iterator<Map<String,Object>> it = list.iterator();
			while(it.hasNext()){
				Map<String,Object> m = it.next();
				
				Map<String,Object> item = new HashMap<String,Object>(); 
				if(m.get("id")!=null) {
					
				  item.put("id", m.get("id").toString());
				  item.put("name", m.get("idname").toString());
				  item.put("pId", m.get("pid").toString());
			      menuNodes.add(item);
			      
				}
			}
		}
		return menuNodes;
	}
	
	
	
	/**
	 * 根据条件展示卡口菜单树(显示方向)
	 */
	@RequestMapping(params = "method=getGateTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getGateTreeByFilter(@RequestParam("dwdm") String dwdm) {
		return this.prefectureManager.getGateTree(dwdm);
	}
	
	/**
	 * 根据条件展示卡口菜单树(不显示方向)
	 */
	@RequestMapping(params = "method=getMulGateTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getMulGateTreeByFilter(@RequestParam("dwdm") String dwdm) {
		/*
		Map map = new HashMap();
		map.put("dwdm", dwdm);
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		List list = null;
		if(dwdm.startsWith("4400")){
			list = this.prefectureManager.getSTGateTreeOnlyGate();
		} else {
			list = this.prefectureManager.getMulGateTree(map);
		}*/
		List list = this.prefectureManager.getOldGateTreeOnlyGate(dwdm);
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				
				Map<String,Object> item = new HashMap<String,Object>(); 
				if(m.get("id")!=null && m.get("idname")!=null) {
				item.put("id", m.get("id").toString());
				item.put("name", m.get("idname").toString());
				item.put("pId", m.get("pid").toString());
				if("430000000000".equals(m.get("id").toString())) {
					item.put("nocheck", true);
				}
				menuNodes.add(item);
				}
			}
		}
		return menuNodes;
	}
	
	/**
	 * 根据条件展示地市菜单树
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=getCityTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getCityTreeByFilter() throws Exception {
		
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		List list = this.prefectureDao.getGdAnHnCityTree();
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				Map<String,Object> item = new HashMap<String,Object>();
				if(m.get("DWDM").toString().substring(0,2).equals("44")){
					item.put("id", m.get("DWDM").toString());
					item.put("name", m.get("JDMC").toString().replaceAll(" ",""));
					item.put("pId", "440000000000");
					menuNodes.add(item);
				}else if(m.get("DWDM").toString().substring(0,2).equals("43")){
					item.put("id", m.get("DWDM").toString());
					item.put("name", m.get("JDMC").toString().replaceAll(" ",""));
					item.put("pId", "430000000000");
					menuNodes.add(item);
				}else if(m.get("DWDM").toString().substring(0,2).equals("45")){
					item.put("id", m.get("DWDM").toString());
					item.put("name", m.get("JDMC").toString().replaceAll(" ",""));
					item.put("pId", "450000000000");
					menuNodes.add(item);
				}
				else if(m.get("DWDM").toString().substring(0,2).equals("35")){
					item.put("id", m.get("DWDM").toString());
					item.put("name", m.get("JDMC").toString().replaceAll(" ",""));
					item.put("pId", "350000000000");
					menuNodes.add(item);
				}else if(m.get("DWDM").toString().substring(0,2).equals("36")){
					item.put("id", m.get("DWDM").toString());
					item.put("name", m.get("JDMC").toString().replaceAll(" ",""));
					item.put("pId", "360000000000");
					menuNodes.add(item);
				}
				else if(m.get("DWDM").toString().substring(0,2).equals("46")){
					item.put("id", m.get("DWDM").toString());
					item.put("name", m.get("JDMC").toString().replaceAll(" ",""));
					item.put("pId", "460000000000");
					menuNodes.add(item);
				}
//				if(!m.get("dwdm").equals("430000000000") && !m.get("dwdm").equals("440000000000")){
//					item.put("id", m.get("DWDM").toString());
//					item.put("name", m.get("JDMC").toString().replaceAll(" ",""));
//					item.put("pId", "0");
//					menuNodes.add(item);
//				}
				
			}
		}
		return menuNodes;
	}
	
	
	/**
	 * 根据条件展示跨省菜单树
	 */
	@RequestMapping(params = "method=getKsCityTreeByFilter")
	@ResponseBody
	public List<Map<String,Object>> getKsCityTreeByFilter(HttpServletRequest request, HttpServletRequest response) {
		
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		/*
		 * 修改人：huyaj 2017/7/4
		 * 修改原因：不需要通过接口去查询广东省的行政区划树，因为端口不通
		 * 修改时间：20170628
		PropertyUtil.load("ipport");
		String ipgd=PropertyUtil.get("ipgd").trim();
		String portgd=PropertyUtil.get("portgd").trim();
		POST的URL
		String url = "http://" + ipgd + ":" +portgd +"/api/gdservice/department/query";
		String url = "http://127.0.0.1:8899/api/service/department/query";
		//建立HttpPost对象
		HttpPost httppost = new HttpPost(url);*/
		try {
			//HttpResponse responseSend = new  DefaultHttpClient().execute(httppost);
			//String sendPostResult = EntityUtils.toString(responseSend.getEntity());
			//List<Map<String, Object>> list = (List<Map<String, Object>>)JSON.parse(sendPostResult);
			List<Map<String, Object>> list = prefectureManager.getGdCityTree();
			if(list.size()>0) {
				Iterator<Map<String, Object>> it = list.iterator();
				while(it.hasNext()){
					Map<String,Object> m = it.next();
					Map<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", m.get("DWDM").toString());
					item.put("name", m.get("JDMC").toString().replaceAll(" ",""));
					item.put("pId", m.get("SJJD").toString());
					menuNodes.add(item);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return menuNodes;
	}
	
	
	
	/**
	 * 获取行政区划列表
	 * <2016-12-5> 修改行政区划不与当前部门关联，要所有人都可以查
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getDistricts",method=RequestMethod.POST)
	@ResponseBody
	public List getDistricts(HttpServletRequest request){
		String glbm =null;
		//UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		/*Department department = userSession.getDepartment();
		if(!"1".equals(department.getJb())){
			glbm=department.getGlbm().substring(0,4);
		}*/
		List list = null;
		try {
		    list=this.systemManager.getDistricts(glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取城市列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getCitys",method=RequestMethod.POST)
	@ResponseBody
	public List getCitys(HttpServletRequest request) {
		List list = null;
		try {
			list = this.urlManager.getCodeUrls();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取各城市的行政区划
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=queryCityAdministrativeAdvisions")
	@ResponseBody
	public List<AdministrativeDivision> queryCityAdministrativeAdvisions(HttpServletRequest request) {
		String cityname;		
		try {
			String city = request.getParameter("city");
			if(!"440000".equals(city)) {
				cityname = this.systemManager.getCodeValue("105000", city);
				return this.systemManager.queryCityAdministrativeAdvisions(cityname);
			} else {
				return this.systemManager.queryCityAdministrativeAdvisions(null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据城市获取行政区划列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getDistrictsByCity",method=RequestMethod.POST)
	@ResponseBody
	public List getDistrictsByCity(HttpServletRequest request) {
		List list = null;
	    String city = request.getParameter("city");
		try {
			list = this.systemManager.getDistrictsByCity(city);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据行政区划获取道路代码列表
	 * @param request
	 * @param xzqh
	 * @return
	 */
	@RequestMapping(params="method=getRoadsByFilter",method=RequestMethod.POST)
	@ResponseBody
	public List getRoadsByFilter(@RequestParam("xzqh") String xzqh){
		List list = null;
		try {
		    list=this.systemManager.getRoadsByFilter(xzqh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据道路代码获取路口路段列表
	 * @param request
	 * @param dldm
	 * @return
	 */
	@RequestMapping(params="method=getCrossingByFilter",method=RequestMethod.POST)
	@ResponseBody
	public List getCrossingByFilter(@RequestParam("xzqh") String xzqh,@RequestParam("dldm") String dldm){
		List list = null;
		try {
		    list=this.systemManager.getCrossingByFilter(xzqh, dldm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据当前登录用户获取交管部门
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getTrafficDepartment",method=RequestMethod.POST)
	@ResponseBody
	public List getTrafficDepartment(HttpServletRequest request){
		List list = null;
		//Map<String,Object> map=new HashMap();
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		String xzqh=userSession.getDepartment().getGlbm().substring(0,4);
		try {
			list=this.systemManager.getTrafficDepartment(xzqh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 文件下载
	 * @param request
	 * @param resp
	 * @param wdlj
	 */
	@RequestMapping(params="method=downloadFile",method=RequestMethod.POST)
	@ResponseBody
	public void downloadFile(HttpServletRequest request,HttpServletResponse resp) {
		String realPath;
		String wdlj=request.getParameter("wdlj");
		try {
			realPath = FileUtil.getFileHomeRealPath() + wdlj;
			File file = new File(realPath);
			InputStream in = new FileInputStream(file);
			((HttpServletResponse) resp).addHeader("Content-Disposition","attachment;filename=" + file.getName());
			((HttpServletResponse) resp).addHeader("Content-length", ""+file.length());
			resp.setContentType("application/stream");
			OutputStream out = resp.getOutputStream();
			IOUtils.copy(in, out);
			IOUtils.closeQuietly(in);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * 模板下载，模板须保存在download文件夹下
	 * @param filename:模板的文件名
	 *
	 */
	@RequestMapping(params="method=downloadModal",method=RequestMethod.POST)
	@ResponseBody
	public void downloadModal(HttpServletResponse response,HttpServletRequest request,String filename) {
		try {
			FileInputStream	 fis=new FileInputStream(new File(request.getSession().getServletContext().getRealPath("/")+"/download/"+filename+""));
			byte[] b=new byte[fis.available()];
			fis.read(b);
			fis.close();
			response.setContentType("application;charset=UTF-8");   			
			response.setHeader("Content-Disposition", "attachment;Filename="+new String(filename.getBytes(), "utf-8"));
			OutputStream os = response.getOutputStream();
			os.write(b, 0, b.length);  
	        os.flush(); 
	        os.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}				
	}
	
	/**
	 * 获取下拉菜单列表
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(params = "method=getDmsmForCode", method = RequestMethod.POST)
	@ResponseBody
	public List getDmsmForCode(String code) {
		List list = null;
		try {
			list = this.systemManager.getCode(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 跳转到pgis平台
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPGIS(HttpServletRequest request, HttpServletResponse response) {
		try {
			//ClassLoader cl = Thread.currentThread().getContextClassLoader();
			//FileInputStream fis = new FileInputStream(cl.getResource("").getPath()+"forwardUrl.properties");
			//FileInputStream	 fis=new FileInputStream(new File(CommController.class.getResource("/").getPath()+"/forwardUrl.properties"));
			//InputStream in = new BufferedInputStream(fis);
			//Properties p = new Properties();        
			//p.load(in);
			//String pgisUrl=p.get("pgisUrl").toString();
			Syspara sysPara = this.sysParaManager.getSyspara("pgisUrl", "1", "");
			request.setAttribute("pgis",sysPara.getCsz());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("pgis/temp");
	}
	
	/**
	 * 跳转到新的过车查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardNewVehicle(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setAttribute("vehicle","http://10.142.54.33:8883/vehicle/loginAction.do?method=login");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("newvehicle/vehicle");
	}
	
	
	
	/**
	 * 跳转到视综平台
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardView(HttpServletRequest request, HttpServletResponse response) {
		try {
			//ClassLoader cl = Thread.currentThread().getContextClassLoader();
			//FileInputStream fis = new FileInputStream(cl.getResource("").getPath()+"forwardUrl.properties");
			//FileInputStream	 fis=new FileInputStream(new File(CommController.class.getResource("/").getPath()+"/forwardUrl.properties"));
			//InputStream in = new BufferedInputStream(fis);
			//Properties p = new Properties();        
			//p.load(in);
			//String viewUrl=p.get("viewUrl").toString();
			Syspara sysPara = this.sysParaManager.getSyspara("viewUrl","1","");
			request.setAttribute("view",sysPara.getCsz());
		} catch (Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("view/temp");
	}
	
	/**
	 * 跳转到IT运维管理平台
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardRegulatory(HttpServletRequest request, HttpServletResponse response) {
		try {
			//ClassLoader cl = Thread.currentThread().getContextClassLoader();
			//FileInputStream fis = new FileInputStream(cl.getResource("").getPath()+"forwardUrl.properties");
			//FileInputStream	 fis=new FileInputStream(new File(CommController.class.getResource("/").getPath()+"/forwardUrl.properties"));
			//InputStream in = new BufferedInputStream(fis);
			//Properties p = new Properties();        
			//p.load(in);
			//String itUrl=p.get("itUrl").toString();
			Syspara sysPara = this.sysParaManager.getSyspara("itsmUrl","1","");
			request.setAttribute("regulatory", sysPara.getCsz());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("regulatory/temp");
	}
	
	@RequestMapping(params = "method=getCityList", method = RequestMethod.GET)
	@ResponseBody
    public List getCityList(){
    	List list = null;
    	try {
			//list=urlManager.getCodeUrls("3");
    		list=urlManager.getCodeUrls();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return list;
    }
	
	@RequestMapping(params = "method=getCity", method = RequestMethod.GET)
	@ResponseBody
	public CodeUrl getCity(String dwdm){
		CodeUrl code = null;
		try {
			code=this.urlManager.getUrl(dwdm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	
	@RequestMapping
	@ResponseBody
	public List<Map<String,Object>> getXzqhTree(HttpServletRequest request,String dwdm){
		List<Map<String,Object>> list = null;
		try {
			list = this.systemManager.getXzqhTree(dwdm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public List<Map<String,Object>> getXzqhTreeForPgis(HttpServletRequest request,String dwdm){
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		String initDwdm = request.getParameter("initDwdm");
		try {
			//如果是批量修改卡口经纬度，则需要根据当前登录用户部门进行控制
			if(dwdm==null&&initDwdm!=null&&initDwdm.equals("0")){
				UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
				Department dp = userSession.getDepartment();
				dwdm = dp.getGlbm().substring(0,6);
				if(dwdm!=null && "430000".equals(dwdm)){
					dwdm = null;
				}
			}
//			dwdm="430000870000";
			List list = this.systemManager.getXzqhTree(dwdm);
			if(list.size()>0) {
				Iterator<Object> it = list.iterator();
				while(it.hasNext()){
					Map m = (Map)it.next();
					Map<String,Object> item = new HashMap<String,Object>(); 
					item.put("id", m.get("id").toString());
					item.put("name", m.get("name").toString().replaceAll(" ",""));
					if(m.get("pid")!=null && !"".equals(m.get("pid"))) {
						item.put("pId", m.get("pid").toString());
						if(dwdm != null && dwdm.equals(m.get("id")))
							item.put("pId", "0");
					}else {
						item.put("pId", "0");
					}
					if(m.get("jb")!=null && !"".equals(m.get("jb"))) {
						item.put("jb", m.get("jb").toString());
					}else {
						item.put("jb", "0");
					}
					menuNodes.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuNodes;
	}
	

	@RequestMapping(params = "method=getGateTreeByCity")
	@ResponseBody
	public List<Map<String,Object>> getGateTreeByCity(HttpServletRequest request) throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		String kd = "";			
		String dwdm = request.getParameter("dwdm");
		try {
		
				list = this.gateManager.getGateTreeByGlbmAndSt(kd,dwdm);				
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list.size()>0) {
			
			Iterator<Map<String,Object>> it = list.iterator();
			while(it.hasNext()){
				Map<String,Object> m = it.next();
				
				Map<String,Object> item = new HashMap<String,Object>(); 
				if(m.get("id")!=null) {
					
				  item.put("id", m.get("id").toString());
				  item.put("name", m.get("idname").toString());
				  item.put("pId", m.get("pid").toString());
			      menuNodes.add(item);
			      
				}
			}
		}
		return menuNodes;
	}
	
	/**
	 * it资源信息整合 liumeng
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request, HttpServletResponse response) {
		String doStr = "";
		try {
			doStr = request.getParameter("doStr");
			ITresource it = SpringApplicationContext.getBean("itresource", ITresource.class);
			if(it != null){
				request.setAttribute("it",it);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(doStr);
	}
	
	@RequestMapping
	public ModelAndView forwardHelpWin(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("manager/helpWin");
	}
	
	@RequestMapping
	public ModelAndView forwardPagePolice(HttpServletRequest request){
		String _page = request.getParameter("pg");
		try {
			_page = URLDecoder.decode(_page, "UTF-8");
			ModelAndView mv = null;
			if(_page != null && !"".equals(_page)){
				mv = new ModelAndView(_page);
				Enumeration<String> enumes = request.getParameterNames();
				while (enumes.hasMoreElements()) {
					String param = enumes.nextElement();
					if (param != null && param.length() > 0
							&& !"method".equalsIgnoreCase(param) && !"pg".equalsIgnoreCase(param)) {
						// Object value = request.getParameter(param);
						Object value ;
						if(param=="yhmc"||param.equals("yhmc")){
							 value=new String(request.getParameter(param).getBytes("iso-8859-1"), "UTF-8");
						}else{
							value= URLDecoder.decode(URLDecoder.decode(request.getParameter(param), "UTF-8"), "UTF-8");
						}
						mv.addObject(param, value);
					}
				}
				return mv;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("main");
	}
	
	
	/**
	 * Query a list of menus according to roleid(jsdh)
	 * 
	 * @param jsdh
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getMenuListByJsdh(@RequestParam String jsdh,
			HttpServletRequest request, HttpServletResponse response) {
		List<RoleMenu> list = null;
		try {
			list = this.roleMenuManager.getRoleMenuList(jsdh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 异步更新角色资源
	 * @param jsdh
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object configRole(@RequestParam String jsdh,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> result = new HashMap<String,String>();
		if("".equals(jsdh) || jsdh == null){
			result.put("msg", "请先给用户分配角色!");
			return result;
		}
		/*确认还是取消*/
		String czlx = request.getParameter("czbz");
		/*资源编号*/
		String id = request.getParameter("id");
		/*资源编号*/
		String pid = request.getParameter("pid");
		
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setJsdh(jsdh);
		roleMenu.setCxdh(id);
		roleMenu.setParentid(pid);
		
		try {
			/*添加资源*/
			if("true".equals(czlx)){
				LogInvocationHandler proxy = new LogInvocationHandler(logHandlerInter); 
				LogHandlerParam logParam = new LogHandlerParam();
				logParam.setType(OperationType.ROLE_MENU_BATCH_ADD.getType());//日志类型
				logParam.setDescription(OperationType.ROLE_MENU_BATCH_ADD.getDesc());//日志描述
				logParam.setOperationMethod("addRoleMenu");//执行方法
				RoleMenuManager manager = (RoleMenuManager) proxy.bind(this.roleMenuManager,logParam,request);
				int r1=manager.addRoleMenu(roleMenu);
				
				if(r1 == 1){
					result.put("msg", "添加角色授权成功！");
				}
			} else if("false".equals(czlx)){
				LogInvocationHandler proxy = new LogInvocationHandler(logHandlerInter); 
				LogHandlerParam logParam = new LogHandlerParam();
				logParam.setType(OperationType.ROLE_MENU_BATCH_CANCEL.getType());//日志类型
				logParam.setDescription(OperationType.ROLE_MENU_BATCH_CANCEL.getDesc());//日志描述
				logParam.setOperationMethod("deleteRoleMenu");//执行方法
				RoleMenuManager manager = (RoleMenuManager) proxy.bind(this.roleMenuManager,logParam,request);
				int r2=manager.deleteRoleMenu(roleMenu);
				if(r2 == 1){
					result.put("msg", "取消角色授权成功！");
				}
			} else {
				
				result.put("msg", "非法操作!");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "程序异常！");
			return result ;
		}
		return result ;
	}
	
	@RequestMapping
	public ModelAndView forwardMain(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Role> list = this.roleManager.getRoleListByType(" and type=100 order by jsjb ");
		request.setAttribute("typeList", list);	
		return new ModelAndView("manager/rolelist");
	}
}
