package com.sunshine.monitor.comm.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
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
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.ibm.icu.text.SimpleDateFormat;
import com.sunshine.core.util.Assert;
import com.sunshine.monitor.comm.bean.MaintainHandle;
import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.filter.OnlineUserListener;
import com.sunshine.monitor.comm.maintain.service.MaintainManager;
import com.sunshine.monitor.comm.service.MainPageManager;
import com.sunshine.monitor.comm.service.MenuManager;
import com.sunshine.monitor.comm.service.PublicManager;
import com.sunshine.monitor.comm.service.RoleMenuManager;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.waterMark.CreateImage;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SysUserManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.monitor.service.KkjrjcProjectManager;
import com.sunshine.monitor.system.monitor.service.SjcsjcProjectManager;
import com.sunshine.monitor.system.sign.service.BKSignManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoEditManager;
import com.sunshine.monitor.system.ws.ConnectionService.CitySituationService;
import com.sunshine.monitor.system.ws.ConnectionService.bean.CitySituationEntity;
import com.sunshine.monitor.system.ws.util.AddHeaderInterceptor;


@Controller
@RequestMapping(value="/login.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginController {
	
	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	private SysUserManager sysuserManager;
	
	@Autowired
	private DepartmentManager departmentManager;
	
	@Autowired
	private SysparaManager sysparaManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("urlManager")
	private UrlManager urlManager;

	@Autowired
	@Qualifier("suspinfoAuditApproveManager")
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;
	
	@Autowired
	@Qualifier("vehAlarmHandleManager")
	private VehAlarmHandleManager vehAlarmHandleManager;
	
	@Autowired
	@Qualifier("suspinfoEditManager")
	private SuspinfoEditManager suspinfoEditManager;
	
	@Autowired
	private MaintainManager maintainManager;
	
	@Autowired
	@Qualifier("kkjrjcProjectManager")
	private KkjrjcProjectManager kkjrjcProjectManager;
	
	@Autowired
	@Qualifier("sjcsjcProjectManager")
	private SjcsjcProjectManager sjcsjcProjectManager;
	
	@Autowired
	private RoleMenuManager roleMenuManager;
	
	@Autowired
	private LogManager logManager;
	
	@Autowired
	private BKSignManager bkSignManager;
	
	@Autowired
	private VehAlarmManager vehAlarmManager;
	
	@Autowired
	private MainPageManager mainPageManager;	
	
	@Autowired
	private PublicManager publicManager;
	
	private  PropertiesConfiguration config;
		
	private static  Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	private static final String CERT_ATTRIBUTE = "javax.servlet.request.X509Certificate";
	
	private static String forwardLoginPath = "../../index";
	
	private void saveLog(SysUser user, String czlx, String cznr, String ip){
		Log log = new Log();
		if(user != null){
			log.setGlbm(user.getGlbm());
			log.setYhdh(user.getYhdh());
		}
		log.setIp(ip);
		log.setCzlx(czlx);
		log.setCznr(cznr);
		try{
			logManager.saveLog(log);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Login To Main JSP 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView mav = new ModelAndView();
		/** If happen to Exception then forward to login page path by default */
				
		try {
			HttpSession session = request.getSession();
			String strRemoteAddr = request.getRemoteAddr();
			String sunshine = request.getParameter("sunshine");
			
			/**
			 * @author yaowang
			 * 读取配置文件将衡阳图片的是否读取的状态读取出来
			 * Picswitch
			 */
			config = new PropertiesConfiguration("ipport.properties");
			String Picswitch=config.getString("picAddressSplicing");
			session.setAttribute("Picswitch", Picswitch);
			
			if (session.getAttribute("userSession") != null) {
				mav.setViewName("main");
				return mav;
			}
			/** HTTPS PKI */
			if(request.isSecure()){
				try {
					X509Certificate[] certificate = (X509Certificate[])request
							.getAttribute(CERT_ATTRIBUTE);
					/**Certificate Subject*/
					String s1 = certificate[0].getSubjectDN().getName();
					try{
						Assert.isNullOrEmpty(s1, "Certificate Subject is Empty!");
					}catch(IllegalArgumentException e){
						e.printStackTrace();
						/** Error page forward*/
						mav.setViewName(forwardLoginPath);
						return mav;
					}
					List<String> sfzs = null;
					/**Analysis Centificate Subject*/
					String[] s2 = s1.split(",");
					if ((s2 != null) && (s2.length > 2)) {
						String[] s3 = s2[0].split(" ");
						if ((s3 != null) && (s3.length == 2)) {
							String sfz = s3[1].trim();
							if ((sfz.length() == 18) || (sfz.length() == 15)) {
								sfzs = new ArrayList<String>();
								if (sfz.length() == 15){
									sfzs.add(sfz);
								    sfzs.add(Common.sfzUpto18(sfz));
								} else {
									sfzs.add(sfz);
								}
								/** Bind variable*/
								SysUser user = this.sysuserManager.getSysuserBySfzmhm(sfzs);
								try{
									Assert.isNull(user,"系统中未找到该PKI用户的注册信息["+sfzs+"]!");
								}catch(IllegalArgumentException e){
									e.printStackTrace();
									mav.setViewName(forwardLoginPath);
									mav.addObject("message", "系统中未找到该PKI用户的注册信息!");
									return mav;
								}
								//user.setMm("");
								//user.setIp(strRemoteAddr);
								//user.setSfzmhm(sfz);
								//电子证书验证
								String result=verifyDzzs(user, "1");
								if(result != null){
									mav.setViewName(forwardLoginPath);
									mav.addObject("message", result);
									return mav;
								}
								/*String ser = this.sysparaManager.getSyspara(
										"local", "1", null).getCsz();
								if (ser.length() > 1) {*/
									//session.setAttribute("pki", user);
									//mav.setView(new RedirectView(ser + "/login.do?method=login&sunshine=2"));
									//RequestDispatcher rd = request.getRequestDispatcher("/login.do?method=login&sunshine=2");
									//rd.forward(request, response);
								/**验证是否分配角色*/
								SysUser tuser = this.sysuserManager.getSysUser(user.getYhdh());
								try{
									Assert.isNull(tuser,"系统中未找到该PKI用户的注册信息["+user.getYhdh()+"]!");
								}catch(IllegalArgumentException e){
									e.printStackTrace();
									mav.setViewName(forwardLoginPath);
									mav.addObject("message", "系统中未找到该PKI用户的注册信息!");
									return mav;
								}
								ModelAndView tmav = checkUserRole(tuser);
								if(tmav != null){
									return tmav;
								}
								//initPKIParameters(session, request, tuser);
								//return new ModelAndView("main");
								
								String ser = this.sysparaManager.getSyspara("local", "1", null).getCsz();
								if (ser.length() > 1) {
									session.setAttribute("pki", user);
									RedirectView rv = new RedirectView(ser + "/login.do?method=login&sunshine=2");
									rv.setExposeModelAttributes(true);
									rv.setExposePathVariables(false);
									mav.setView(rv);
								}else{
									mav.setViewName(forwardLoginPath);
									mav.addObject("message", "系统PKI登录信息配置不正确!");
									saveLog(user,"1004","PKI登录成功登录后跳转URL未配置",strRemoteAddr);
								}
								return mav;
							}
							mav.setViewName(forwardLoginPath);
							mav.addObject("message", "个人证书中身份证号[15或18位]异常!");
							saveLog(null,"1004","个人证书中身份证号[15或18位]异常",strRemoteAddr);
							return mav;
						}
						mav.setViewName(forwardLoginPath);
						mav.addObject("message", "个人证书主题CN异常!");
						saveLog(null,"1004","个人证书主题CN异常",strRemoteAddr);
						return mav;
					}
					mav.setViewName(forwardLoginPath);
					mav.addObject("message", "个人证书主题异常!");
					saveLog(null,"1004","个人证书主题异常",strRemoteAddr);
					return mav;
				} catch (Exception ex) {
					ex.printStackTrace();
					mav.setViewName(forwardLoginPath);
					mav.addObject(
							"message",
							"网关校验失败："
									+ Common.ScriptToString(ex
											.getMessage()));
					saveLog(null,"1004","PKI登录失败，网关校验异常",strRemoteAddr);
					return mav;
				}
			}else if ((session != null) && (session.getAttribute("pki") != null)) { // PKI第二次登录处理
				SysUser tmp = (SysUser) session.getAttribute("pki");
				SysUser sysuser = this.sysuserManager.getSysUser(tmp.getYhdh());
				/**验证是否分配角色*/
				ModelAndView tmav = checkUserRole(sysuser);
				if(tmav != null)
					return tmav;
				initPKIParameters(session, request, sysuser);
			}else if ("3".equals(sunshine)) {
				/**
				 * @author quxiaol by 2019-11-06
				 * 跳过登录页面，直接放行
				 */
				String yhdh = request.getParameter("yhdh");
				SysUser sysuser = this.sysuserManager.getSysUser(yhdh);
				
				try{
					Assert.isNull(sysuser,"系统中未找到该用户的注册信息["+yhdh+"]!");
				}catch(IllegalArgumentException e){
					e.printStackTrace();
					mav.setViewName(forwardLoginPath);
					mav.addObject("message", "系统中未找到该用户的注册信息!");
					return mav;
				}
				
				/**验证是否分配角色*/
				String rolesStr = sysuser.getRoles();
				try{
					Assert.isNullOrEmpty(rolesStr, "暂未分配角色!");
				}catch(Exception e){
					//e.printStackTrace();
					logger.debug(sysuser.getYhdh()+"-暂未分配角色");
					mav.setViewName(forwardLoginPath);
					mav.addObject("message", "您暂无权限权登录，请联系管理员!");
					return mav;
				}
				sysuser.setIp(request.getRemoteAddr());
				
				boolean isProvinceDpt = "4300".equals(sysuser.getGlbm().substring(0,4));
				
				Department department = this.departmentManager.getDepartment(sysuser.getGlbm());
				List list = this.sysparaManager.getSysparas(department.getGlbm());
				UserSession userSession = new UserSession();
				userSession.setSysuser(sysuser);
				userSession.setDepartment(department);
				userSession.setSyspara(list);	
				userSession.setIsProvinceDpt(isProvinceDpt);
				request.getSession().setAttribute("userSession", userSession);
				if (sysuser.getMm().equals("password")){
					setOnline(request, userSession,
							strRemoteAddr, "系统登录", "警综用户");
				}else{
					setOnline(request, userSession, 
							strRemoteAddr, "系统登录", "系统用户");
				}
				this.systemManager.updateVisitTotal();
				
				if (list != null) {
					for (Iterator it = list.iterator(); it.hasNext();) {
						Syspara syspara = (Syspara) it.next();
						if ((syspara.getGjz().equalsIgnoreCase("users"))
								&& (syspara.getSfgy().equals("1"))) {
							request.getSession().setAttribute("totalP", syspara.getCsz());
						}
					}
				}
				
		        // 设置响应的类型格式为图片格式  
		        response.setContentType("image/jpeg");  
		        // 禁止图像缓存。  
		        response.setHeader("Pragma", "no-cache");  
		        response.setHeader("Cache-Control", "no-cache");  
		        response.setDateHeader("Expires", 0); 
		        
		        String  sfzmhm= sysuser.getSfzmhm();
		        String jhstr = sysuser.getJh();
		        //根据部门名称动态生成水印在宽高
		        int leng = sfzmhm.length();
		        int width = 240+leng*7;
		        int height = 150+leng*11;
		        String path = request.getRealPath("/")+"\\tempimg\\"+jhstr+".png";//D:\tools\apache-tomcat-6.0.41\webapps\jcbk\
		        String marklj = request.getRealPath("/")+"\\tempimg";
		        File markmu = new File(marklj);
		        if(markmu.mkdir()){
		        	System.out.println("创建水印图片路径成功："+marklj);
		        }else{
		        	System.out.println("水印目录已创建或水印目录创建失败!");
		        }
		        
		        // 生成水印,部门名称+用户代号
		        CreateImage image = new CreateImage(width, height, sfzmhm+" "+jhstr,path);
				saveLog(sysuser,"1001","用户登录成功",sysuser.getIp());
			}else{
				String user =  request.getParameter("user");
				String codes = (String) session.getAttribute("codes");
				if ((codes == null) || (codes.length() != 4)) {
					mav.setViewName(forwardLoginPath);
					mav.addObject("message", "生成验证码失败！");
					return mav;
				}
				if (!codes.equals(request.getParameter("yzm"))) {
					mav.setViewName(forwardLoginPath);
					mav.addObject("message", "验证码不正确！");
					return mav;
				}
				/**Check user and password*/
				SysUser command = new SysUser();
				command.setIp(strRemoteAddr);
				command.setYhdh(request.getParameter("user"));
				command.setMm(request.getParameter("mm"));
				command.setYzm(request.getParameter("yzm"));
				command.setBz(request.getParameter("bz"));
				this.bkSignManager.checkUser(command);
				
				SysUser sysuser = this.sysuserManager.getSysUser(user);
				/**验证是否分配角色*/
				String rolesStr = sysuser.getRoles();
				try{
					Assert.isNullOrEmpty(rolesStr, "暂未分配角色!");
				}catch(Exception e){
					//e.printStackTrace();
					logger.debug(sysuser.getYhdh()+"-暂未分配角色");
					mav.setViewName(forwardLoginPath);
					mav.addObject("message", "您暂无权限权登录，请联系管理员!");
					return mav;
				}
				//电子证书验证
				String result=verifyDzzs(sysuser, "0");
				if(result != null){
					mav.setViewName(forwardLoginPath);
					mav.addObject("message", result);
					return mav;
				}
				sysuser.setIp(request.getRemoteAddr());
				//TODO
				boolean isProvinceDpt = "4300".equals(sysuser.getGlbm().substring(0,4));
				//TODO
				Department department = this.departmentManager.getDepartment(sysuser.getGlbm());
				List list = this.sysparaManager.getSysparas(department.getGlbm());
				UserSession userSession = new UserSession();
				userSession.setSysuser(sysuser);
				//department.setAgent(department.getGlbm(), department.getBmlx());
				//========================================================================
				// 2015/11/12
				/**department.setPrefecture(this.departmentManager.getPrefecture(department));*/
				//=========================================================================
				//department.setKey(this.departmentManager.getDepartmentKey(department.getGlbm()));
				userSession.setDepartment(department);
				userSession.setSyspara(list);	
				userSession.setIsProvinceDpt(isProvinceDpt);
				request.getSession().setAttribute("userSession", userSession);
				if (sysuser.getMm().equals("password")){
					setOnline(request, userSession,
							strRemoteAddr, "系统登录", "警综用户");
				}else{
					setOnline(request, userSession, 
							strRemoteAddr, "系统登录", "系统用户");
				}
				this.systemManager.updateVisitTotal();
				// TODO
				if (list != null) {
					for (Iterator it = list.iterator(); it.hasNext();) {
						Syspara syspara = (Syspara) it.next();
						if ((syspara.getGjz().equalsIgnoreCase("users"))
								&& (syspara.getSfgy().equals("1"))) {
							request.getSession().setAttribute("totalP", syspara.getCsz());
						}
					}
				}
				
		        // 设置响应的类型格式为图片格式  
		        response.setContentType("image/jpeg");  
		        // 禁止图像缓存。  
		        response.setHeader("Pragma", "no-cache");  
		        response.setHeader("Cache-Control", "no-cache");  
		        response.setDateHeader("Expires", 0); 
		        /*
		         * yaowang	
		         * 2018-4-16
		         * 车辆平台的水印，需要改为身份证+警号
		         */
		        //String  bmmc= sysuser.getBmmc();
		        String  sfzmhm= sysuser.getSfzmhm();
		        String jhstr = sysuser.getJh();
		        //根据部门名称动态生成水印在宽高
		        int leng = sfzmhm.length();
		        int width = 240+leng*7;
		        int height = 150+leng*11;
		        String path = request.getRealPath("/")+"\\tempimg\\"+jhstr+".png";//D:\tools\apache-tomcat-6.0.41\webapps\jcbk\
		        String marklj = request.getRealPath("/")+"\\tempimg";
		        File markmu = new File(marklj);
		        if(markmu.mkdir()){
		        	System.out.println("创建水印图片路径成功："+marklj);
		        }else{
		        	System.out.println("水印目录已创建或水印目录创建失败!");
		        }
		        System.err.println("路径："+path);
		        System.err.println("******************************************************");
		       // System.err.println("("+bmmc+")部门名称长度："+leng);
		        System.err.println("("+sfzmhm+")身份证号码长度为："+leng);
		        System.err.println("生成水印图片长为："+width+"。宽为:"+height);
		        System.err.println("******************************************************");
		        // 生成水印,部门名称+用户代号
		        
		       // CreateImage image = new CreateImage(width, height, bmmc+" "+jhstr,path);
		        CreateImage image = new CreateImage(width, height, sfzmhm+" "+jhstr,path);
//		        Cookie cookie = new Cookie("watermark", image.getImgStr());  
//		        cookie.setMaxAge(1800);  
//		        response.addCookie(cookie);  
//		        image.write(response.getOutputStream());
		        
				saveLog(sysuser,"1001","用户登录成功",sysuser.getIp());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName(forwardLoginPath);
			mav.addObject("message", e.getMessage());
			return mav;
		}
		ModelAndView mv = new ModelAndView("main");
		return mv;
	}

	/**
	 * 验证用户是否分配角色
	 * @param sysuser
	 * @return
	 */
	private ModelAndView checkUserRole(SysUser sysuser){
		ModelAndView mav = new ModelAndView();
		/**验证是否分配角色*/
		String rolesStr = sysuser.getRoles();
		try{
			Assert.isNullOrEmpty(rolesStr, "暂未分配角色!");
		}catch(Exception e){
			//e.printStackTrace();
			logger.debug(sysuser.getYhdh()+"-暂未分配角色");
			mav.setViewName(forwardLoginPath);
			mav.addObject("message", "您暂无权限权登录，请联系管理员!");
			return mav;
		}
		return null;
	}
	
	/**
	 * PKI登录参数初始化
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private void initPKIParameters(HttpSession session, HttpServletRequest request, SysUser sysuser) throws Exception{
		String strRemoteAddr = request.getRemoteAddr();
		// TODO
		boolean isProvinceDpt = sysuser.getGlbm().substring(0,4).equals("4300");
		sysuser.setIp(strRemoteAddr);
		Department department = this.departmentManager
				.getDepartment(sysuser.getGlbm());
		List list = this.sysparaManager.getSysparas(department.getGlbm());
		UserSession userSession = new UserSession();
		userSession.setSysuser(sysuser);
		department.setAgent(department.getGlbm(), department.getBmlx());
		//=====================================================================
		//TODO 获取下级部门
		// 2015/11/12 
		/**department.setPrefecture(this.departmentManager
				.getPrefecture(department));*/
		//=====================================================================
	    //department.setKey(this.departmentManager.getDepartmentKey(department.getGlbm()));
		userSession.setDepartment(department);
		/** Province User Mark*/
		userSession.setIsProvinceDpt(isProvinceDpt);
		/** Sava System parameters*/
		userSession.setSyspara(list);
		request.getSession().setAttribute("userSession", userSession);
		// TODO
		if (list != null) {
			for (Iterator it = list.iterator(); it.hasNext();) {
				Syspara syspara = (Syspara) it.next();
				if ((syspara.getGjz().equalsIgnoreCase("users"))
						&& (syspara.getSfgy().equals("1"))) {
					request.getSession().setAttribute("totalP", syspara.getCsz());
				}
			}
		}
		if (sysuser.getMm().equals("password"))
			setOnline(request, userSession, 
					strRemoteAddr, "PKI登录", "警综用户");
		else
			setOnline(request, userSession, 
					strRemoteAddr, "PKI登录", "系统用户");
		this.systemManager.updateVisitTotal();//新增系统访问次数
		saveLog(sysuser,"1003","PKI登录成功",sysuser.getIp());
		request.setAttribute("title",
				this.systemManager.getParameter("name", request));
	}
	
	/**
	 * 验证电子证书信息，对应用户只能使用相应方式登录
	 * dzzs=0:普通用户
	 * dzzs=1：pki用户
	 * 
	 * @param flag
	 * @param user
	 * @return result
	 */
	@RequestMapping(params="method=verifyDzzs")
	public static String verifyDzzs(SysUser user, String flag){
		String dzzsstr=user.getDzzs();
		String result=null;
		if(!flag.equals(dzzsstr)){
			if("1".equals(dzzsstr)){
				result="您是PKI用户，请使用PKI进行登录";
			}else if("0".equals(dzzsstr)){
				result="您是普通用户，请输入账号密码登录";
			}else{
				result="您的登录方式未定义，请联系管理员";
			}
		}
		return result;
	}
	@RequestMapping(params="method=signOut")
	public ModelAndView sysSignOut(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("logout");
		request.getSession().removeAttribute("userSession");
		return mav;
	}
	
	@RequestMapping(params="method=queryCount")
	public ModelAndView queryCount(HttpServletRequest request, HttpServletResponse response) {
	
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		SysUser user = userSession.getSysuser();
		Department department = userSession.getDepartment();
		ModelAndView mv = null;
		Map map = null;
		try {
			//map = this.getDataCountInfo(request, response, userSession,user);
			mv = new ModelAndView("manager/datacount");
			//mv.addObject("mapinfo", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return mv;
	}
	
	@RequestMapping(params="method=getGateProjectCount")
	@ResponseBody
	public Map getGateProjectCount(HttpServletRequest request,HttpServletResponse response) {
		Map map = null;
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		if (userSession != null) {
			SysUser user = userSession.getSysuser();
			if(user.getGlbm().length()>0){
				map = this.mainPageManager.getGateProjectData(request, response, userSession, user);

			}
		}
		return map;
	}
	
	@RequestMapping(params="method=getFlow")
	@ResponseBody
	public Map getFlow(HttpServletRequest request,HttpServletResponse response) {
		Map map = null;
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		if (userSession != null) {
			SysUser user = userSession.getSysuser();
			map = this.mainPageManager.getFlow(userSession, user);
		}
		return map;
	}
	
	/**
	 * 公安环比月
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params="method=getPoliceRatio")
	@ResponseBody
	public Object getPoliceRatio(HttpServletRequest request,HttpServletResponse response) {
		Calendar cal = new GregorianCalendar();
		int current = cal.get(Calendar.DAY_OF_MONTH);
		Calendar calendar = Calendar.getInstance(); 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
		String lastMonth_start = "";
		String lastMonth_end = "";
		String thisMonth_start = "";
		String thisMonth_end = "";
		if(current>25) {
			calendar.set(Calendar.DAY_OF_MONTH, 25);
			lastMonth_end = sdf.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, 26);
			thisMonth_start = sdf.format(calendar.getTime());
			calendar.add(Calendar.MONTH, -1);
			lastMonth_start = sdf.format(calendar.getTime());
			calendar.add(Calendar.MONTH, +2);
			calendar.set(Calendar.DAY_OF_MONTH, 25);
			thisMonth_end = sdf.format(calendar.getTime());
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, 25);
			thisMonth_end = sdf.format(calendar.getTime());
			calendar.add(Calendar.MONTH, -1);
			lastMonth_end = sdf.format(calendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, 26);
			thisMonth_start = sdf.format(calendar.getTime());
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 26);
			lastMonth_start = sdf.format(calendar.getTime());
		}
		lastMonth_start += " 00:00:00";
		lastMonth_end += " 23:59:59";
		thisMonth_start += " 00:00:00";
		thisMonth_end += " 23:59:59";
		Map condition = new HashMap();
		condition.put("lastMonth_start", lastMonth_start);
		condition.put("lastMonth_end", lastMonth_end);
		condition.put("thisMonth_start", thisMonth_start);
		condition.put("thisMonth_end", thisMonth_end);
		
		return this.mainPageManager.getPoliceRatio(condition);
	}
	
	@RequestMapping(params="method=getGateConnectCount")
	@ResponseBody
	public Map getGateConnectCount(HttpServletRequest request,HttpServletResponse response) {
		Map map = null;
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		try {
		if (userSession != null) {
			SysUser user = userSession.getSysuser();
			if(user.getGlbm().length()>0){
				if(user.getGlbm().substring(0,4).equals("4400")){
					map=this.mainPageManager.getGateInCount();
				}
				else{
					map=this.mainPageManager.getGateInCountByCity(user.getGlbm().substring(0,4));
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	@RequestMapping(params="method=getqueryCount")
	@ResponseBody
	public Map getqueryCount(HttpServletRequest request,HttpServletResponse response)
	{
		Map map = null;
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		if (userSession != null) {
			SysUser user = userSession.getSysuser();
			Department department = userSession.getDepartment();
			long _1 = System.currentTimeMillis();
			map = this.mainPageManager.getDataCountInfo(request, response, userSession, user);
			long _2 = System.currentTimeMillis();
			System.out.println("===================首页业务统计:" + (_2-_1)+"MS==========================");
		}
		return map;
	}
	
	@RequestMapping(params="method=getqueryDefereCount")
	@ResponseBody
	public Map getqueryDefereCount(HttpServletRequest request,HttpServletResponse response)
	{
		Map map = null;
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		if (userSession != null) {
			SysUser user = userSession.getSysuser();
			Department department = userSession.getDepartment();
			long _1 = System.currentTimeMillis();
			map = this.mainPageManager.getDataCountDefereInfo(request, response, userSession, user);
			long _2 = System.currentTimeMillis();
			System.out.println("===================数据质量监测部分首页业务统计:" + (_2-_1)+"MS==========================");
		}
		return map;
	}
	
	public Map getDataCountInfo(HttpServletRequest request,HttpServletResponse response, UserSession userSession,SysUser user){
		ModelAndView mv = null;
		Map map = new HashMap();
		try {
			String end = this.systemManager.getSysDate(null, false);
			String begin = this.systemManager.getSysDate("-180", false);
			int editCount = this.suspinfoEditManager.getSuspinfoEditCount(begin, end, user.getYhdh(), "1");
			int editBkfwlxCount = this.suspinfoEditManager.getSuspinfoEditCount(begin, end, user.getYhdh(), "2");
			int alarmcmdCount = this.vehAlarmHandleManager.getAlaramCmdCount(this.systemManager.getSysDate("-1/24",true), this.systemManager.getSysDate("+1/24", true),userSession.getDepartment().getGlbm());
			int alarmNoHandleCount = this.vehAlarmHandleManager.getAlaramNoHandleCount(begin,end,userSession.getDepartment().getGlbm());
			int suspinfoAuditCount = this.suspinfoAuditApproveManager.getSuspinfoAuditCount(begin, end, userSession.getDepartment().getGlbm());
			int suspinfoApproveCount = this.suspinfoAuditApproveManager.getSuspinfoApproveCount(begin, end, userSession.getDepartment().getGlbm());
			int alarmNoHandleBackCount = this.vehAlarmHandleManager.getAlaramNoHandledBackCount(begin, end, userSession.getDepartment().getGlbm());
			int alarmLjSuspinfoCount = this.vehAlarmHandleManager.getAlarmNoLjSuspinfoCount(begin, end, userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh(),userSession.getDepartment().getJb(),userSession.getDepartment().getBmlx());
			int alarmCancelSuspinfoCount = this.vehAlarmHandleManager.getAlarmNoCancleSuspinfoCount(begin, end, userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh());
			int suspinfoCancelAuditCount = this.suspinfoAuditApproveManager.getSuspinfoCancelAuditCount(begin, end, userSession.getDepartment().getGlbm());
			int suspinfoCancelApproveCount = this.suspinfoAuditApproveManager.getSuspinfoCancelApproveCount(begin, end, userSession.getDepartment().getGlbm());
			int suspinfoExpireCount = this.suspinfoAuditApproveManager.getExpireCancelSuspinfoCount(begin, end, userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh());
			int alarmSuspinfoCount =  this.vehAlarmHandleManager.getAlarmHasBeenSuspinfoCount(begin, end, userSession.getSysuser().getYhdh());
			int suspinfoBkfwlxCount = this.suspinfoAuditApproveManager.getSuspinfoBkfwlxCount(begin, end, userSession.getSysuser().getGlbm());
			int suspinfoNoBkfwlxCount = this.suspinfoAuditApproveManager.getSuspinfoNoBkfwlxCount(begin, end, userSession.getSysuser().getGlbm());
			int kkjrsinfoCount = this.kkjrjcProjectManager.getKkjrjcQueryCount();
			int kkzxinfoCount = this.kkjrjcProjectManager.getKkzxQueryCount();
			int sjcsinfoCount = this.sjcsjcProjectManager.getSjcsQueryCount();
			int overTimeAlarmCount = this.vehAlarmManager.getOverTimeAlarmCountInThisMonth(userSession.getDepartment().getGlbm());			
			Map filter = new HashMap();
			filter.put("curPage", "1");	//页数
			filter.put("pageSize", "1");	//每页显示行数
			Map maintainMap = this.maintainManager.findMaintainHandleForMap(filter,new MaintainHandle(),userSession.getDepartment());
			
			int maintainCount = (Integer) maintainMap.get("total");
			
			List userSuspinfoList = this.suspinfoAuditApproveManager.getUserSuspinfo(begin, end, userSession.getSysuser().getYhdh());
			map.put("editCount", editCount);
			map.put("editBkfwlxCount", editBkfwlxCount);
			map.put("alarmcmdCount", alarmcmdCount);
			map.put("alarmNoHandleCount", alarmNoHandleCount);
			map.put("alarmLjSuspinfoCount", alarmLjSuspinfoCount);
			map.put("suspinfoAuditCount", suspinfoAuditCount);
			map.put("suspinfoApproveCount", suspinfoApproveCount);
			map.put("alarmNoHandleBackCount", alarmNoHandleBackCount);
			map.put("alarmCancelSuspinfoCount", alarmCancelSuspinfoCount);
			map.put("suspinfoCancelAuditCount", suspinfoCancelAuditCount);
			map.put("suspinfoCancelApproveCount", suspinfoCancelApproveCount);
			map.put("suspinfoExpireCount", suspinfoExpireCount);
			map.put("userSuspinfoList", userSuspinfoList);
			map.put("alarmSuspinfoCount", alarmSuspinfoCount);
			map.put("suspinfoBkfwlxCount", suspinfoBkfwlxCount);
			map.put("kkjrsinfoCount", kkjrsinfoCount);
			map.put("kkzxinfoCount",kkzxinfoCount);
			map.put("sjcsinfoCount", sjcsinfoCount);
			map.put("suspinfoNoBkfwlxCount", suspinfoNoBkfwlxCount);
			map.put("maintainCount", maintainCount);
			map.put("overTimeAlarmCount", overTimeAlarmCount);
	    }catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Get main menu tree
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getMenuTrees(HttpServletRequest request,
			HttpServletResponse response){
		List<Menu> list = null ;
		try {
			list = this.menuManager.findMenuList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping
	public Object getMenuTreesByRoles(HttpServletRequest request,
			HttpServletResponse response,String id){
		List<Menu> list = null ;
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");		
		try {		
			if(userSession!=null){
				SysUser user = userSession.getSysuser();	
				list = this.menuManager.findMenuListByRoles(user.getRoles(),id);			
				}
			} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}	
	
	
	
	/**
	 * 
	 * 函数功能说明： 监控登录用户待处理业务情况
	 * 修改日期 	2013-6-21
	 * @return    
	 * @return Object
	 * @throws Exception 
	 */
	@RequestMapping
	@ResponseBody
	public Object getUserBusiness(HttpServletRequest request) {
		Map map = new HashMap();	
		StringBuffer sb = new StringBuffer();
		UserSession userSession = (UserSession) WebUtils
									.getSessionAttribute(request, "userSession");
		
		if(userSession != null){
			SysUser user = userSession.getSysuser();
			
			Map filter = new HashMap();
			filter.put("curPage", "1");	//页数
			filter.put("pageSize", "1");	//每页显示行数		
			boolean auditTip = false;
			boolean approveTip = false;
			boolean LjTip = false;
			boolean cAuditTip = false;
			boolean cApproveTip = false;
			boolean handleTip = false;
			
			//String[]roles = user.getRoles().split(",");
			
			
			try {
				
				
				String end = this.systemManager.getSysDate(null, false);
				String begin = this.systemManager.getSysDate("-180", false);
				/**
				for(String role : roles){
					List<RoleMenu> list = roleMenuManager.getRoleMenuList(role);
					for(RoleMenu rm : list){
						if(rm.getCxdh().equals("100042")) auditTip = true;
						if(rm.getCxdh().equals("100054")) approveTip = true;
						if(rm.getCxdh().equals("100065")) LjTip = true;
						if(rm.getCxdh().equals("100045")) cAuditTip = true;
						if(rm.getCxdh().equals("100057")) cApproveTip = true;
					    if(rm.getCxdh().equals("100010")) handleTip = true;
					}
				}
				**/
				//获取用户资源列表
				List<String> list = roleMenuManager.queryMenusByRole(user.getYhdh());
				for(String s : list){
					if ("100042".equals(s)) {
						auditTip = true;
						continue;
					}
					if ("100054".equals(s)) {
						approveTip = true;
						continue;
					}
					if ("100065".equals(s)) {
						LjTip = true;
						continue;
					}
					if ("100045".equals(s)) {
						cAuditTip = true;
						continue;
					}
					if ("100057".equals(s)) {
						cApproveTip = true;
						continue;
					}
					if ("100010".equals(s)) {
						handleTip = true;
						continue;
					}
				}
				
				int i = 0;
				VehSuspinfo info = new VehSuspinfo();
				String glbm = user.getGlbm();
				
				if(auditTip){
					Map mapAudit = suspinfoAuditApproveManager.findSuspinfoAuditForMap(filter, info,glbm );
					i = Integer.parseInt(mapAudit.get("total").toString());  
					if(i > 0){
						sb.append("你有<a href=\"#\"  onclick=\"addTab11(1)\"><span style=\"color: red;\">【").append(i).append("】</span></a>条布控申请记录需审核。</br>");
					}
				}
				
				if (approveTip) {
					Map mapApprove = suspinfoAuditApproveManager
							.getSuspinfoApproves(filter, info, glbm);
					i = Integer.parseInt(mapApprove.get("total").toString());
					if (i > 0) {
						sb.append("你有<a href=\"#\"  onclick=\"addTab11(2)\"><span style=\"color: red;\">【")
						  .append(i)
						  .append("】</span></a>条布控记录需审批。</br>");
					}
				}
							
				if (cAuditTip) {
					VehSuspinfo cAuditInfo = new VehSuspinfo();
					cAuditInfo.setKssj(begin);
					cAuditInfo.setJssj(end);
					Map mapCAudit = suspinfoAuditApproveManager
							.findSuspinfoCancelForMap(filter, cAuditInfo, glbm, "");
					i = Integer.parseInt(mapCAudit.get("total").toString());
					if (i > 0) {
						sb.append("你有<a href=\"#\"  onclick=\"addTab11(3)\"><span style=\"color: red;\">【")
						  .append(i)
						  .append("】</span></a>条撤控记录需审核。</br>");
					}
				}
				
				
				if (cApproveTip) {
					VehSuspinfo cApproveInfo = new VehSuspinfo();
					cApproveInfo.setKssj(begin);
					cApproveInfo.setJssj(end);
					Map mapCApprove = suspinfoAuditApproveManager
							.getSuspinfoCancelApproves(filter, cApproveInfo, glbm);
					i = Integer.parseInt(mapCApprove.get("total").toString());
					if (i > 0) {
						sb.append("你有<a href=\"#\"  onclick=\"addTab11(4)\"><span style=\"color: red;\">【")
						  .append(i)
						  .append("】</span></a>条撤控记录需审批。</br>");
					}
				}
				if (LjTip) {
					int alarmLjSuspinfoCount = this.vehAlarmHandleManager.getAlarmNoLjSuspinfoCount(begin, end, userSession.getDepartment().getGlbm(),userSession.getSysuser().getYhdh(),userSession.getDepartment().getJb(),userSession.getDepartment().getBmlx());
					if (alarmLjSuspinfoCount>0) {
						sb.append("你有<a href=\"#\"  onclick=\"addTab11(5)\"><span style=\"color: red;\">【")
						  .append(alarmLjSuspinfoCount)
						  .append("】</span></a>条记录需拦截。</br>");
					}
				}
				
				if (handleTip){
					Map<String,Object> filters = new HashMap<String,Object>();
					filters.put("curPage", 1);	//页数
					filters.put("pageSize",1);	
					MaintainHandle handle =new  MaintainHandle();
					handle.setSfcl("0");
					int handleCount = (Integer)this.maintainManager.findMaintainHandleForMap(filters,handle,userSession.getDepartment()).get("total");
					if(handleCount>0){
						 sb.append("你有<a href=\"#\"  onclick=\"addTab11(6)\"><span style=\"color: red;\">【")
						   .append(handleCount)
						   .append("】</span></a>条故障处理反馈记录需处理。</br>");
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		map.put("msg", sb.toString());
		return map;
	}
	
	/**
	 * 
	 * 函数功能说明：监测地市连接情况
	 * @return    
	 * @return Object
	 * @throws Exception 
	 */
	@RequestMapping
	@ResponseBody
	public Object getConnection() {
		try {
			CodeUrl cu = this.urlManager.getUrl("430000000000");
			// Web service client
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.setServiceClass(CitySituationService.class);
			// Request Informations
			factory.getOutInterceptors().add(new LoggingOutInterceptor());
			// Response Informations
			factory.getInInterceptors().add(new LoggingInInterceptor());
			factory.getOutInterceptors().add(new AddHeaderInterceptor());
			//String wsurl = "http://"+cu.getUrl()+":"+cu.getPort()+"/"+cu.getContext()+"/service/CitySituationService";
			String wsurl = "http://"+cu.getUrl()+":"+cu.getPort()+"/jcbk/service/CitySituationService";
			factory.setAddress(wsurl);
			//factory.setAddress("http://10.46.28.196:9080/jcbk/service/CitySituationService");
			CitySituationService service = (CitySituationService) factory.create();
			
			Client proxy = ClientProxy.getClient(service);
			HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
			conduit.getClient().setReceiveTimeout(0);
			HTTPClientPolicy policy = new HTTPClientPolicy();
			// 设置连接超时 3000ms
			policy.setConnectionTimeout(3000);
			//取消块编码
			policy.setAllowChunking(false);
			// 设置请求超时
			policy.setReceiveTimeout(30000);
			conduit.setClient(policy);		
			List<CitySituationEntity> list = service.getSurvey();			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (Error e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 文档下载
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping
	public void wddownload(HttpServletRequest request,
			HttpServletResponse response, String filename) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		Log log = new Log();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			// 获取文件名格式后缀
			/*
			 * if(filename!=null){ String ars=
			 * filename.substring(filename.lastIndexOf(".")+1,
			 * filename.length());
			 * 
			 * System.out.println(ars); }
			 */
			/*
			 * FileInputStream fis=new FileInputStream(new
			 * File(request.getSession
			 * ().getServletContext().getRealPath(File.separator
			 * )+"document"+File.separator+filename+""));
			 * 
			 * byte[] b=new byte[fis.available()]; fis.read(b); fis.close();
			 */
			// response.reset();
			log = getLog(request, response);
			log.setCznr("成功! 导出了"
					+ filename
					+ "文件,"
					+ "请求下载路径为"
					+ request.getSession().getServletContext().getRealPath(
							File.separator) + "document" + File.separator
					+ filename + "");
			this.logManager.saveLog(log);
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setCharacterEncoding("UTF-8");
			response.reset();
			response.setContentType("application/xml;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;Filename="
					+ new String(filename.getBytes("GBK"), "ISO8859-1"));
			/*
			 * OutputStream os = response.getOutputStream(); DataOutputStream
			 * d=new DataOutputStream(os); //; System.out.println(b.length);
			 * d.write(b, 0, b.length); os.close(); d.close();
			 */
			// os.close();

			// bis=new BufferInputStream(new FileInputStream(new
			// File(request.getSession().getServletContext().getRealPath(File.separator)+"document"+File.separator+filename+"")));
			bis = new BufferedInputStream(new FileInputStream(new File(request
					.getSession().getServletContext().getRealPath(
							File.separator)
					+ "document" + File.separator + filename + "")));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			log = getLog(request, response);
			log.setCznr("失败! 没找到"
					+ filename
					+ "文件,"
					+ "请求下载路径为"
					+ request.getSession().getServletContext().getRealPath(
							File.separator) + File.separator + "document"
					+ File.separator + filename + " 不存在");
			this.logManager.saveLog(log);
			/*
			 * PrintWriter out =response.getWriter();out.print(
			 * "<script language='javascript'>alert('请求失败!');window.history.back(-1); </script>"
			 * ); out.close();
			 */
			e.printStackTrace();
		} catch (IOException e) {
			/*log = getLog(request, response);
			log.setCznr("失败! IO异常,"
					+ "请求下载路径为"
					+ request.getSession().getServletContext().getRealPath(
							File.separator) + File.separator + "document"
					+ File.separator + filename + " ");
			this.logManager.saveLog(log);*/
			/*
			 * PrintWriter out =response.getWriter();out.print(
			 * "<script language='javascript'>alert('请求失败!');window.history.back(-1); </script>"
			 * ); out.close();
			 */
			e.printStackTrace();
		} catch (Exception e) {
			log = getLog(request, response);
			log.setCznr("失败! ,"
					+ e.toString()
					+ "请求下载路径为"
					+ request.getSession().getServletContext().getRealPath(
							File.separator) + File.separator + "document"
					+ File.separator + filename + " ");
			/*
			 * PrintWriter out =response.getWriter();out.print(
			 * "<script language='javascript'>alert('请求失败!');window.history.back(-1);</script>"
			 * ); out.close();
			 */
			e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}

	}
	
	
	
	private Log getLog(HttpServletRequest request,HttpServletResponse response){
		Log log=new Log();
		 //文档下载日志记录
        UserSession userSession = (UserSession) WebUtils
		.getSessionAttribute(request, "userSession");
        String yhdh = userSession.getSysuser().getYhdh();
        String glbm = userSession.getDepartment().getGlbm();
        String ip = request.getHeader("x-forwarded-for");        
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {        
           ip = request.getHeader("Proxy-Client-IP");        
       }        
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {        
           ip = request.getHeader("WL-Proxy-Client-IP");        
        }        
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {        
            ip = request.getRemoteAddr();        
       }  
		log.setGlbm(glbm);
		log.setIp(ip);
		log.setYhdh(yhdh);
		log.setCzlx("10000");
		return log ;
	}
	
	private void setOnline(HttpServletRequest request, UserSession userSession,
			String ip, String type, String system) throws Exception {
		SysUser s = new SysUser();
		s.setYhdh(userSession.getSysuser().getYhdh());
		s.setYhmc(userSession.getSysuser().getYhmc());
		s.setJh(userSession.getSysuser().getJh());
		s.setIp(ip);
		s.setGlbm(userSession.getDepartment().getGlbm());
		s.setBmmc(userSession.getDepartment().getBmmc());
		s.setZt(Common.getNow());
		s.setYzm(type);
		s.setMm(system);
		/*HashMap map = (HashMap) application.getAttribute("onlines");
		if (map == null) {
			map = new HashMap();
			map.put(s.getYhdh(), s);
			application.setAttribute("onlines", map);
			application.setAttribute("online", String.valueOf(1));
		} else if (map.containsKey(s.getYhdh())) {
			map.put(s.getYhdh(), s);
			application.setAttribute("onlines", map);
		} else {
			map.put(s.getYhdh(), s);
			int c = Integer.parseInt((String) application
					.getAttribute("online"));
			c++;
			application.setAttribute("onlines", map);
			application.setAttribute("online", String.valueOf(c));
		}*/
		request.getSession().setAttribute(OnlineUserListener.LISTENER_NAME, s);
		request.getSession().getServletContext().setAttribute("online", OnlineUserListener.lineCount.get());
	}
	
	/**
	 * 
	 * 函数功能说明：模糊搜索字段
	 * @return    
	 * @return Object
	 * @throws Exception 
	 */
	@RequestMapping
	@ResponseBody
	public Object getSearchValue(HttpServletRequest request) throws Exception {
		String gate_text = request.getParameter("gate_text");
		String search_text = gate_text.replace(" ", "%");
		Map map = this.publicManager.FullTextSearch(search_text);
		return map;
	}
	
	/**
	 * 
	 * 函数功能说明：号牌号码跳转页面
	 * @return    
	 * @return ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping
	public ModelAndView forwardHphmMessege(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String hphm = URLDecoder.decode(request.getParameter("hphm"), "UTF-8");
		request.setAttribute("hphm", hphm);
		request.setAttribute("hpzllist", this.systemManager.getCode("030107"));
		request.setAttribute("hpyslist", this.systemManager.getCodes("031001"));
		request.setAttribute("citylist", this.systemManager.getCodes("105000"));
		request.setAttribute("qrztlist",this.systemManager.getCode("120014"));
		request.setAttribute("bklblist", this.systemManager.getCode("120005"));
		request.setAttribute("bkdllist", this.systemManager.getCode("120019"));
		request.setAttribute("ywztlist", this.systemManager.getCode("120008"));
		request.setAttribute("bjztlist", this.systemManager.getCode("130009"));
		request.setAttribute("xxlylist", this.systemManager.getCode("120012"));
		return new ModelAndView("query/hphmmessege");
	}
	
	/**
	 * 读取更新日志提示配置文件
	 * @return
	 */
	private Map<String,Object> readProperties(){
		InputStream in;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String log_id="";
            boolean flag=false;
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
			FileInputStream fis = new FileInputStream(cl.getResource("").getPath()+"updatelog.properties");
			in = new BufferedInputStream(fis);
			Properties p = new Properties();        
			p.load(in);
			String str_Date=p.get("log.update.date").toString();
			log_id=str_Date.replaceAll("\\-","\\_");
			int duration=Integer.parseInt(p.get("log.update.duration").toString());
			
			Date now = new Date();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(str_Date);
			long time = (date.getTime()/(24*60*60*1000)+1+duration)*(24*60*60*1000);
			date.setTime(time);
			flag = now.before(date);
		    
			map.put("log_id",log_id);
			map.put("flag",flag?1:0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (ParseException e){
			e.printStackTrace();
		}
      return map;
	}
	
	/**
	 * 注销移除sesson,从设置application的onlines属性
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView mv = new ModelAndView(forwardLoginPath);
		HttpSession session = request.getSession();
		session.removeAttribute(OnlineUserListener.LISTENER_NAME);
		session.invalidate();
		session.getServletContext().setAttribute("online", OnlineUserListener.lineCount.intValue());
		return mv;
	}
	
	public void setMenuManager(MenuManager menuManager) {
		this.menuManager = menuManager;
	}


	public VehAlarmHandleManager getVehAlarmHandleManager() {
		return vehAlarmHandleManager;
	}


	public void setVehAlarmHandleManager(VehAlarmHandleManager vehAlarmHandleManager) {
		this.vehAlarmHandleManager = vehAlarmHandleManager;
	}


	public SuspinfoAuditApproveManager getSuspinfoAuditApproveManager() {
		return suspinfoAuditApproveManager;
	}


	public void setSuspinfoAuditApproveManager(
			SuspinfoAuditApproveManager suspinfoAuditApproveManager) {
		this.suspinfoAuditApproveManager = suspinfoAuditApproveManager;
	}


	public SuspinfoEditManager getSuspinfoEditManager() {
		return suspinfoEditManager;
	}


	public void setSuspinfoEditManager(SuspinfoEditManager suspinfoEditManager) {
		this.suspinfoEditManager = suspinfoEditManager;
	}

	public MaintainManager getMaintainManager() {
		return maintainManager;
	}

	public void setMaintainManager(MaintainManager maintainManager) {
		this.maintainManager = maintainManager;
	}

	public KkjrjcProjectManager getKkjrjcProjectManager() {
		return kkjrjcProjectManager;
	}

	public void setKkjrjcProjectManager(KkjrjcProjectManager kkjrjcProjectManager) {
		this.kkjrjcProjectManager = kkjrjcProjectManager;
	}

	public SjcsjcProjectManager getSjcsjcProjectManager() {
		return sjcsjcProjectManager;
	}

	public void setSjcsjcProjectManager(SjcsjcProjectManager sjcsjcProjectManager) {
		this.sjcsjcProjectManager = sjcsjcProjectManager;
	}

	public VehAlarmManager getVehAlarmManager() {
		return vehAlarmManager;
	}

	public void setVehAlarmManager(VehAlarmManager vehAlarmManager) {
		this.vehAlarmManager = vehAlarmManager;
	}

	public MainPageManager getMainPageManager() {
		return mainPageManager;
	}

	public void setMainPageManager(MainPageManager mainPageManager) {
		this.mainPageManager = mainPageManager;
	}

	
	public UrlManager getUrlManager() {
		return urlManager;
	}

	public void setUrlManager(UrlManager urlManager) {
		this.urlManager = urlManager;
	}
	
	@ResponseBody
	@RequestMapping
	public Object getOnlineUserTotal(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			Syspara syspara = this.sysparaManager.getSyspara("users", "1", "");
			map.put("totalP", syspara.getCsz());
			map.put("online", OnlineUserListener.lineCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
