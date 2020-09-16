package com.sunshine.monitor.system.analysis.action;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.vehicle.VehQuery;
import com.sunshine.monitor.system.analysis.bean.JmYearInspection;
import com.sunshine.monitor.system.analysis.service.ViolationManager;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;

@Controller
@RequestMapping(value = "/violation.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ViolationController {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("violationManager")
	private ViolationManager violationManager;
	
	@Autowired
	private UrlManager urlManager;

	/**
	 * 跳转到车辆登记库、交通违法库关联分析主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView findForward(HttpServletRequest request) {
		try {
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			
			request.setAttribute("urllist", this.urlManager.getCodeUrls());

			request.setAttribute("cllxlist", this.systemManager
					.getCodes("030104"));

			request.setAttribute("csyslist", this.systemManager
					.getCodes("030108"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("analysis/violationMain");
	}
	
	/**
	 * 查询车辆登记库
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object getCarsList(HttpServletRequest request,HttpServletResponse response,JmYearInspection bean) {
		
		JSONObject jo = new JSONObject();
		List<Object> list = new ArrayList<Object>();
		//获取用户信息
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		//把请求参数转换为map类型作为巨龙接口方法的参数
        Class type = bean.getClass(); 
        Map returnMap = new HashMap(); 
        try{
        	  BeanInfo beanInfo = Introspector.getBeanInfo(type); 
              PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
              for (int i = 0; i< propertyDescriptors.length; i++) { 
                  PropertyDescriptor descriptor = propertyDescriptors[i]; 
                  String propertyName = descriptor.getName(); 
                  if (!propertyName.equals("class")) { 
                      Method readMethod = descriptor.getReadMethod(); 
                      Object result = readMethod.invoke(bean, new Object[0]); 
                      if (result != null) { 
                          returnMap.put(propertyName, result); 
                      } else { 
                          returnMap.put(propertyName, ""); 
                      } 
                  } 
              } 
        } catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
      
      
        if(bean.getHpzl()==null||bean.getHpzl().length()<1){
        	bean.setHpzl("02");
        }
        
        VehQuery vq = new VehQuery();
        
		//ProvincialRBSPHandle RBSP=new ProvincialRBSPHandle();
		try {
			jo = JSONObject.fromObject(vq.getVehilceInfo(request, bean.getHpzl(), bean.getHphm()));
			//jo=RBSP.findJson(user,returnMap,bean.getHpzl());
			if(jo!=null) {
				list.add(jo);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Map map=new HashMap();
//		map.put("HPHM", "粤1233445");
//		map.put("HPZL", "02");
//		map.put("CSYS", "A");
//		Map map2=new HashMap();
//		map2.put("HPHM", "粤12312345");
//		map2.put("HPZL", "03");
//		map2.put("CSYS", "J");
//		list.add(map);
//		list.add(map2);
//		return list;
		return null;
	}
	
	/**
	 * 与交通违法库关联分析：违法处理库
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping
	@ResponseBody
	public Object getViolationList(HttpServletRequest request) throws UnsupportedEncodingException {
		String hphm = URLDecoder.decode(request.getParameter("hphm"), "UTF-8");
		String hpzl = request.getParameter("hpzl");
		List list = null;
		try {
			list=this.violationManager.getViolationList("T_AP_VIO_VIOLATION", hphm, hpzl);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 与交通违法库关联分析：非现场违法处理库
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping
	@ResponseBody
	public Object getSurveilList(HttpServletRequest request) throws UnsupportedEncodingException {
		String hphm = URLDecoder.decode(request.getParameter("hphm"), "UTF-8");
		String hpzl = request.getParameter("hpzl");
		List list = null;
		try {
			list=this.violationManager.getViolationList("T_AP_VIO_SURVEIL", hphm, hpzl);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 与交通违法库关联分析：强制违法处理库
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping
	@ResponseBody
	public Object getForceList(HttpServletRequest request) throws UnsupportedEncodingException {
		String hphm = URLDecoder.decode(request.getParameter("hphm"), "UTF-8");
		String hpzl = request.getParameter("hpzl");
		List list = null;
		try {
			list=this.violationManager.getViolationList("T_AP_VIO_FORCE", hphm, hpzl);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 跳转到违法库关联分析页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping
	public ModelAndView forwardViolationAnalysis(HttpServletRequest request) throws Exception {
		String hphm = URLDecoder.decode(request.getParameter("hphm"), "UTF-8");
		String hpzl = request.getParameter("hpzl");
		request.setAttribute("hphm", hphm);
		request.setAttribute("hpzl", hpzl);
		request.setAttribute("hpzllist", this.systemManager
				.getCodes("030107"));		
		request.setAttribute("urllist", this.urlManager.getCodeUrls());
		return new ModelAndView("analysis/violationAnalysis");
	}
}
