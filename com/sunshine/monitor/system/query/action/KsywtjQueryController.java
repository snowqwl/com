package com.sunshine.monitor.system.query.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.service.KsywtjQueryManager;

@Controller
@RequestMapping(value = "/ksywtj.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KsywtjQueryController {
	private String ksywtjMain = "query/ksywtjquery";
	
	private String yjPage = "query/ksywtjForYJList";
	
	@Autowired
	private KsywtjQueryManager ksywtjQueryManager;
	
	@Autowired
	private SystemManager systemManager;
	
	@RequestMapping
	public ModelAndView forwardQueryMain(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String glbm = userSession.getDepartment().getGlbm();

			mv.setViewName(ksywtjMain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	@RequestMapping
	@ResponseBody
	public List ksywtjList(HttpServletRequest request,
			HttpServletResponse response) {
		List list = null;
		try {
			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String glbm = userSession.getDepartment().getGlbm();
			String kssj = request.getParameter("kssj");
			String jssj = request.getParameter("jssj");
			String sssf = request.getParameter("sssf");
			if("1".equals(sssf)){
				list = ksywtjQueryManager.ksywtjList(kssj, jssj, sssf);
			}else if("2".equals(sssf)){
				PropertiesConfiguration config;
				config = new PropertiesConfiguration("ipport.properties");
				String ipGd = config.getString("ipgd");
				String portGd = config.getString("portgd");
				
				//POST的URL
				String url = "http://" + ipGd + ":" + portGd + "/api/gdservice/vehicle/ksywtj/ksywtjList";
				//建立HttpPost对象
				HttpPost httppost = new HttpPost(url);
				//建立一个NameValuePair数组，用于存储欲传送的参数
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
				//添加参数
				params.add(new BasicNameValuePair("kssj", kssj));
				params.add(new BasicNameValuePair("jssj", jssj));
				params.add(new BasicNameValuePair("sssf", sssf));
				
				HttpResponse responseSend;
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				responseSend = new DefaultHttpClient().execute(httppost);
				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
				Map<String, Object> map = (Map<String, Object>)JSON.parse(sendPostResult);
				list = (List) map.get("resultContent");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public Object gccxList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = null;
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String sssf = request.getParameter("sssf");
		String dwdm = request.getParameter("dwdm");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String glbm = userSession.getDepartment().getGlbm();
			if("1".equals(sssf)){
				map = ksywtjQueryManager.getGccxList(conditions);
			}else if("2".equals(sssf)){
				PropertiesConfiguration config;
				config = new PropertiesConfiguration("ipport.properties");
				String ipGd = config.getString("ipgd");
				String portGd = config.getString("portgd");
				
				//POST的URL
				String url = "http://" + ipGd + ":" + portGd + "/api/gdservice/vehicle/ksywtj/gccxList";
				//建立HttpPost对象
				HttpPost httppost = new HttpPost(url);
				//建立一个NameValuePair数组，用于存储欲传送的参数
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
				//添加参数
				params.add(new BasicNameValuePair("kssj", kssj));
				params.add(new BasicNameValuePair("jssj", jssj));
				params.add(new BasicNameValuePair("sssf", sssf));
				params.add(new BasicNameValuePair("dwdm", dwdm));
				params.add(new BasicNameValuePair("page", page));
				params.add(new BasicNameValuePair("rows", rows));
				
				HttpResponse responseSend;
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				responseSend = new DefaultHttpClient().execute(httppost);
				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
				map = (Map<String, Object>)JSON.parse(sendPostResult);
				map = (Map<String, Object>) map.get("resultContent");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object bkList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = null;
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String sssf = request.getParameter("sssf");
		String dwdm = request.getParameter("dwdm");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String glbm = userSession.getDepartment().getGlbm();
			if("1".equals(sssf)){
				map = ksywtjQueryManager.getBkList(conditions);
			}else if("2".equals(sssf)){
				PropertiesConfiguration config;
				config = new PropertiesConfiguration("ipport.properties");
				String ipGd = config.getString("ipgd");
				String portGd = config.getString("portgd");
				
				//POST的URL
				String url = "http://" + ipGd + ":" + portGd + "/api/gdservice/vehicle/ksywtj/bkList";
				//建立HttpPost对象
				HttpPost httppost = new HttpPost(url);
				//建立一个NameValuePair数组，用于存储欲传送的参数
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
				//添加参数
				params.add(new BasicNameValuePair("kssj", kssj));
				params.add(new BasicNameValuePair("jssj", jssj));
				params.add(new BasicNameValuePair("sssf", sssf));
				params.add(new BasicNameValuePair("dwdm", dwdm));
				params.add(new BasicNameValuePair("page", page));
				params.add(new BasicNameValuePair("rows", rows));
				
				HttpResponse responseSend;
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				responseSend = new DefaultHttpClient().execute(httppost);
				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
				map = (Map<String, Object>)JSON.parse(sendPostResult);
				map = (Map<String, Object>) map.get("resultContent");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object ckList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = null;
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String sssf = request.getParameter("sssf");
		String dwdm = request.getParameter("dwdm");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String glbm = userSession.getDepartment().getGlbm();
			if("1".equals(sssf)){
				map = ksywtjQueryManager.getCkList(conditions);
			}else if("2".equals(sssf)){
				PropertiesConfiguration config;
				config = new PropertiesConfiguration("ipport.properties");
				String ipGd = config.getString("ipgd");
				String portGd = config.getString("portgd");
				
				//POST的URL
				String url = "http://" + ipGd + ":" + portGd + "/api/gdservice/vehicle/ksywtj/ckList";
				//建立HttpPost对象
				HttpPost httppost = new HttpPost(url);
				//建立一个NameValuePair数组，用于存储欲传送的参数
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
				//添加参数
				params.add(new BasicNameValuePair("kssj", kssj));
				params.add(new BasicNameValuePair("jssj", jssj));
				params.add(new BasicNameValuePair("sssf", sssf));
				params.add(new BasicNameValuePair("dwdm", dwdm));
				params.add(new BasicNameValuePair("page", page));
				params.add(new BasicNameValuePair("rows", rows));
				
				HttpResponse responseSend;
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				responseSend = new DefaultHttpClient().execute(httppost);
				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
				map = (Map<String, Object>)JSON.parse(sendPostResult);
				map = (Map<String, Object>) map.get("resultContent");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object yjList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map = null;
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		String sssf = request.getParameter("sssf");
		String dwdm = request.getParameter("dwdm");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String glbm = userSession.getDepartment().getGlbm();
			if("1".equals(sssf)){
				map = ksywtjQueryManager.getYjList(conditions);
			}else if("2".equals(sssf)){
				PropertiesConfiguration config;
				config = new PropertiesConfiguration("ipport.properties");
				String ipGd = config.getString("ipgd");
				String portGd = config.getString("portgd");
				
				//POST的URL
				String url = "http://" + ipGd + ":" + portGd + "/api/gdservice/vehicle/ksywtj/yjList";
				//建立HttpPost对象
				HttpPost httppost = new HttpPost(url);
				//建立一个NameValuePair数组，用于存储欲传送的参数
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
				//添加参数
				params.add(new BasicNameValuePair("kssj", kssj));
				params.add(new BasicNameValuePair("jssj", jssj));
				params.add(new BasicNameValuePair("sssf", sssf));
				params.add(new BasicNameValuePair("dwdm", dwdm));
				params.add(new BasicNameValuePair("page", page));
				params.add(new BasicNameValuePair("rows", rows));
				
				HttpResponse responseSend;
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				responseSend = new DefaultHttpClient().execute(httppost);
				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
				map = (Map<String, Object>)JSON.parse(sendPostResult);
				map = (Map<String, Object>) map.get("resultContent");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
