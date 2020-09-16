package com.sunshine.monitor.system.veh.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import sun.net.www.protocol.ftp.FtpURLConnection;

import com.sunshine.core.util.Assert;
import com.sunshine.core.util.ClassUtil;
import com.sunshine.core.util.DateUtil;
import com.sunshine.core.util.FileUtil;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.DateUtils;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.comm.util.compress.PicturePackZipTools;
import com.sunshine.monitor.comm.util.picws.PicWSInvokeTool;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.service.PassrecManager;
import com.sunshine.monitor.system.veh.util.EncryUtil;
import com.sunshine.monitor.system.ws.QueryVehPassrecConditions;
import com.sunshine.monitor.system.ws.VehPassrecEntity;
import com.sunshine.monitor.system.ws.server.QueryVehPassrecService;
import com.sunshine.monitor.system.ws.util.AddHeaderInterceptor;
import com.sunshine.monitor.system.ws.util.NotDefPaserException;

@Controller
@RequestMapping(value = "/passquery.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PassrecController {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;

	@Autowired
	@Qualifier("passrecManager")
	private PassrecManager passrecManager;

	@Autowired
	@Qualifier("urlManager")
	private UrlManager urlManager;

	private Logger log = LoggerFactory.getLogger(PassrecController.class);
	private  PropertiesConfiguration config;
	
	
	/**
	 * 跳转跨省查询微服务页面
	 * @param request
	 * @param response
	 * @return
	 * @throws ConfigurationException 
	 * @throws IOException 
	 */
	@RequestMapping
	public void forwardks(HttpServletRequest request,
			HttpServletResponse response) throws ConfigurationException, IOException {
			config = new PropertiesConfiguration("ipport.properties");
			String portxy = config.getString("ks.ipport");//跳转ip
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String sfzmhm = userSession.getSysuser().getSfzmhm();
//			String url="redirect:http://"+portxy+"/index.html?sfzmhm="+sfzmhm+"&bkpt=43";
//			response.setHeader("X-Frame-Options", "SAMEORIGIN");// 解决IFrame拒绝的问题 
			response.sendRedirect("http://www.baidu.com");  //重定向要百度
	}
	
	@RequestMapping
	public ModelAndView test(HttpServletRequest request,
			HttpServletResponse response) throws ConfigurationException, IOException {
		ModelAndView mv=new ModelAndView("veh/temp");
		return mv; 
	}
	
	@RequestMapping
	public void  forwardks1(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ConfigurationException {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		config = new PropertiesConfiguration("ipport.properties");
		String ipdiz = config.getString("ks.ipport");//跳转ip
		String sfzmhm = userSession.getSysuser().getSfzmhm();
		String url="redirect:http://"+ipdiz+"/index.html?sfzmhm="+sfzmhm+"&bkpt=43";
		ModelAndView mv=new ModelAndView(url);
		response.sendRedirect(url);  
//		return mv; 
	}
	
	@RequestMapping
	public ModelAndView  forwardks2(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ConfigurationException {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		config = new PropertiesConfiguration("ipport.properties");
		String ipdiz = config.getString("ks.ipport");//跳转ip
		String sfzmhm = userSession.getSysuser().getSfzmhm();
		String url="redirect:http://"+ipdiz+"/index.html?sfzmhm="+sfzmhm+"&bkpt=43";
		ModelAndView mv=new ModelAndView(url);
		response.addHeader("x-frame-options","SAMEORIGIN"); 
		return mv; 
	}
	
	@RequestMapping
	public ModelAndView  ydbPassRecYDB(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ConfigurationException {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		String user = userSession.getSysuser().getYhdh();
		String jh = userSession.getSysuser().getJh();
		String sfzh = userSession.getSysuser().getSfzmhm();
		Department dp = userSession.getDepartment();
		String bmmc=dp.getBmmc();
		config = new PropertiesConfiguration("ipport.properties");
		String ipdiz = config.getString("vehpassip");
		String strRemoteAddr = request.getRemoteAddr();
		String url="redirect:http://"+ipdiz+"/vehicle?user="+EncryUtil.encrypt(user)+"&bmmc="+EncryUtil.encrypt(bmmc)+"&jhstr="+jh+"&ip="+EncryUtil.encrypt(strRemoteAddr)+"&dep="+EncryUtil.encrypt(userSession.getDepartment().getGlbm())+"&sfz="+EncryUtil.encrypt(sfzh);
		
        
		//response.sendRedirect("http://union.baidu.com");
		ModelAndView mv=new ModelAndView(url);
//		mv.addObject("user", userSession.getSysuser());
//		mv.addObject("depart", userSession.getDepartment());
		return mv; 
	}
	
	@RequestMapping
	public ModelAndView  ydbOtherPassRecYDB(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ConfigurationException {
		/*UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		String user = userSession.getSysuser().getYhdh();
		String jh = userSession.getSysuser().getJh();
		String sfzh = userSession.getSysuser().getSfzmhm();
		Department dp = userSession.getDepartment();
		String bmmc=dp.getBmmc();*/
		config = new PropertiesConfiguration("ipport.properties");
		String ipdiz = config.getString("vehpassip");
		//String strRemoteAddr = request.getRemoteAddr();
		String url="redirect:http://"+ipdiz+"/vehicle/places.html";
		ModelAndView mv=new ModelAndView(url);
		response.addHeader("x-frame-options","SAMEORIGIN"); 
		return mv; 
	}
	
	
	/**
	 * 进入过车信息查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws ConfigurationException 
	 */
	@RequestMapping
	public ModelAndView  ydbPassRec(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ConfigurationException {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		config = new PropertiesConfiguration("ipport.properties");
		String ipdiz = config.getString("ks.ipport");//跳转ip
		String sfzmhm = userSession.getSysuser().getSfzmhm();
		String url="redirect:http://"+ipdiz+"/index.html?sfzmhm="+sfzmhm+"&bkpt=43";
		ModelAndView mv=new ModelAndView(url);
		response.addHeader("x-frame-options","SAMEORIGIN"); 
		return mv; 
	}
	
	/**
	 * 进入过车信息查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPassMain(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			request.setAttribute("hpyslist", this.systemManager
					.getCodes("031001"));
			request.setAttribute("fzjg", this.systemManager.getParameter(
					"fzjg", request));
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
			return new ModelAndView("query/querystpassmain");
	}
	
	/**
	 * 进入过车信息（含交警）查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPassAndJjMainDS(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			request.setAttribute("hpyslist", this.systemManager
					.getCodes("031001"));
			request.setAttribute("csyslist", this.systemManager
					.getCodes("030108"));
			request.setAttribute("cllxlist", this.systemManager
					.getCodes("030104"));
			
			config = new PropertiesConfiguration("ipport.properties");
			String ipdiz = config.getString("vehpassip");
			String strRemoteAddr = request.getRemoteAddr();
			String url="redirect:http://"+ipdiz+"/vehicle";
			
			ModelAndView mv=new ModelAndView(url);

			return mv; 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("query/querystpassandjjmainDS");
	}
	
	/**
	 * 进入过车信息（含交警）查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPassAndJjMain(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			request.setAttribute("hpyslist", this.systemManager
					.getCodes("031001"));
			request.setAttribute("csyslist", this.systemManager
					.getCodes("030108"));
			request.setAttribute("cllxlist", this.systemManager
					.getCodes("030104"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("query/querystpassandjjmain");
	}
	
	
	/**
	 * 进入过车信息查询主页(简易查询)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPassAndSimpleMain(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("query/querystpassandSimplemain");
	}
	
	/**
	 * 进入过车信息查询主页(高级查询)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardPassAndSeniorMain(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			request.setAttribute("hpyslist", this.systemManager
					.getCodes("031001"));
			request.setAttribute("csyslist", this.systemManager
					.getCodes("030108"));
			request.setAttribute("cllxlist", this.systemManager
					.getCodes("030104"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("query/querystpassandSeniormain");
	}
	
	/**
	 * 查询提示：提示用户查询数量过大(含交警)是否继续查询
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryTipsAndJj(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> conditions = Common.getParamentersMap(request);
		/* 必填参数验证  */
		Assert.isNullOrEmpty(conditions.get("kssj"), "开始时间不能为空值!");
		Assert.isNullOrEmpty(conditions.get("jssj"), "结束时间不能为空值!");
		putValue(request, conditions);
		return this.passrecManager.queryTipsAndJj(conditions);
	}
	
	/**
	 * <2016-7-20> 过车查询优化:只对精确号牌查询做总数统计
	 * 对于模糊号牌,返回0;对于精确号牌,大于1000时,返回1000;对于无号牌,返回1001
	 * 
	 * 查询提示：提示用户查询数量过大(含交警)是否继续查询
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryTipsAndJj2(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> conditions = Common.getParamentersMap(request);
		Map<String, Object> result = new HashMap<String, Object>();
		Object kssj = conditions.get("kssj").toString();
		Object jssj = conditions.get("jssj").toString();
		Object cxfs = conditions.get("cxfs");
		if(kssj==null || jssj==null){
			result.put("code","0");
			result.put("msg","开始时间和结束时间不能为空！");
		}else{
			int days = DateUtil.getDiffDays(kssj.toString(), jssj.toString());
			if(cxfs!=null&&"0".equals(cxfs.toString())&&days>15){
				result.put("code",0);
				result.put("msg","简易查询时间范围不能超过15天！");
			}else if(cxfs!=null&&"1".equals(cxfs.toString())&&days>92){
				result.put("code","0");
				result.put("msg","高级查询时间范围不能超过3个月！");
			}else{
				int count = 0;
				if(conditions.containsKey("hphm")){
					count = this.passrecManager.getPassCount(conditions);
				}else {
					if("1".equals(cxfs.toString())){
						count = 1000;
					}else{
						count = 200;
					}
				}
				result.put("code", 1);
				result.put("msg", count);
			}
		}
		return result;
		
	}
	
	private void putValue(HttpServletRequest request,Map<String, Object> conditions) throws Exception{
		String cityDwdm = request.getParameter("city");
		String _kdbh = (String)conditions.get("kdbh");
		if (StringUtils.isNotBlank(cityDwdm)) {
			Code code = this.systemManager.getCode("105000", cityDwdm);
			/**DBLINK名称*/
			String cityname = code.getDmsm1();
			/**DMSM3本地查询标志*/
			String flag = code.getDmsm3();
			/**转换标记,需要转换单位代码(高支队单位430000870000转成431500)*/
			String zhdwdm = code.getDmsm4();
			if(StringUtils.isNotBlank(cityname)){
				/**如果FLAG为1则按本地库查询*/
				if("1".equals(flag)){
					/**转换级别高于>卡口条件，则不在模糊查询*/
					if(!"".equals(zhdwdm) && _kdbh == null){
						conditions.put("kdbhlike", zhdwdm);
					} else if(StringUtils.isBlank(_kdbh)){
						conditions.put("kdbhlike", cityDwdm.substring(0,4));
					} 
					cityname = "";
				}
			}
			conditions.put("cityname", cityname);
		}
	}
	
	
	/**
	 * 查询提示：提示用户查询数量过大是否继续查询
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryTips(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> conditions = Common.getParamentersMap(request);
		if (StringUtils.isNotBlank(request.getParameter("city"))) {

			String cityname = this.systemManager.getCodeValue("105000",
					request.getParameter("city"));
			conditions.put("cityname", cityname);
		}
		return this.passrecManager.queryTips(conditions);
	}
	
	
	/**
	 * 查询过车列表(含交警卡口)
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryPassAndJjlist(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			conditions.remove("kdmc");
			putValue(request, conditions);
			// ======================
			// 查询限制时间(数据量大)
			// ======================
			result = this.passrecManager.getPassrecAndJjList(conditions);
			List<VehPassrec> list = (List<VehPassrec>) result.get("rows");
			// 是否有数据判断
			if (list == null || list.size() == 0) {
				result.put("counts", "0");
			} else {
				result.put("counts", list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * <2016-7-20> 过车查询优化,精确车牌
	 * 
	 * 查询过车列表(含交警卡口)
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryPassAndJjlist2(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			conditions.remove("kdmc");
			putValue(request, conditions);
			// ======================
			// 查询限制时间(数据量大)
			// ======================
			result = this.passrecManager.getPasslists(conditions); 
			List<VehPassrec> list = (List<VehPassrec>) result.get("rows");
			// 是否有数据判断
			if (list == null || list.size() == 0) {
				result.put("counts", "0");
			} else {
				result.put("counts", list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping
	public List<VehPassrec> queryPassAndJjlist3DS(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		List<VehPassrec> list = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			String hphm = conditions.get("hphm").toString();
			hphm = URLDecoder.decode(hphm, "UTF-8");
			conditions.put("hphm", hphm);
			conditions.remove("kdmc");
			putValue(request, conditions);
			// ======================
			// 查询限制时间(数据量大)
			// ======================
			conditions.put("rows", conditions.get("pageSize"));
			result = this.passrecManager.getPasslistsExtDS(conditions); 
			list = (List<VehPassrec>) result.get("rows");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * <2016-12-19> 过车查询优化,精确车牌
	 * 
	 * 查询过车列表(含交警卡口)
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public List<VehPassrec> queryPassAndJjlist3(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		List<VehPassrec> list = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			String hphm = conditions.get("hphm").toString();
			hphm = URLDecoder.decode(hphm, "UTF-8");
			conditions.put("hphm", hphm);
			conditions.remove("kdmc");
			putValue(request, conditions);
			// ======================
			// 查询限制时间(数据量大)
			// ======================
			conditions.put("rows", conditions.get("pageSize"));
			result = this.passrecManager.getPasslistsExt(conditions); 
			list = (List<VehPassrec>) result.get("rows");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public Integer queryPassAndJjlist3Total(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		int total = 0;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			String hphm = conditions.get("hphm").toString();
			hphm = URLDecoder.decode(hphm, "UTF-8");
			conditions.put("hphm", hphm);
			conditions.remove("kdmc");
			putValue(request, conditions);
			total = passrecManager.queryPassAndJjlist3Total(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	/**
	 * <2016-7-20> 过车查询优化,无车牌
	 * 
	 * 查询过车列表(含交警卡口)
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryPassAndJjlist2NoHphm(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			conditions.remove("kdmc");
			putValue(request, conditions);
			// ======================
			// 查询限制时间(数据量大)
			// ======================
			result = this.passrecManager.getPasslistsNoCount(conditions); 
			List<VehPassrec> list = (List<VehPassrec>) result.get("rows");
			// 是否有数据判断
			if (list == null || list.size() == 0) {
				result.put("counts", "0");
			} else {
				result.put("counts", list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping
	public Object queryPassAndJjlist2NoHphmDS(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			conditions.remove("kdmc");
			putValue(request, conditions);
			// ======================
			// 查询限制时间(数据量大)
			// ======================
			result = this.passrecManager.getPasslistsNoCountDS(conditions); 
			List<VehPassrec> list = (List<VehPassrec>) result.get("rows");
			// 是否有数据判断
			if (list == null || list.size() == 0) {
				result.put("counts", "0");
			} else {
				result.put("counts", list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询过车列表
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryPasslist(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			conditions.remove("kdmc");
			if (StringUtils.isNotBlank(request.getParameter("city"))) {

				String cityname = this.systemManager.getCodeValue("105000",
						request.getParameter("city"));
				conditions.put("cityname", cityname);
			}

			// ======================
			// 查询限制时间(数据量大)
			// ======================
			result = this.passrecManager.getPassrecList(conditions);
			List<VehPassrec> list = (List<VehPassrec>) result.get("rows");
			// 是否有数据判断
			if (list == null || list.size() == 0) {
				result.put("counts", "0");
			} else {
				result.put("counts", list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * （根据号牌号码）查询过车列表（模糊查询）
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryPasslistByHphm(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			result = this.passrecManager.getPassrecListByHphm(conditions);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据过车序号查询过车详细信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getVehPassrecDetail(HttpServletRequest request) {
		String gcxh = request.getParameter("gcxh");
		try {
			VehPassrec vr = this.passrecManager.getVehPassrecDetail(gcxh);
			// 处理长沙卡口图片
			JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(vr,vr.getKdbh());
			vr.setTp1(PicWSInvokeTool.getValue(tpJsonObject,"tp1",vr.getTp1()));
			vr.setTp2(PicWSInvokeTool.getValue(tpJsonObject,"tp2",vr.getTp2()));
			vr.setTp3(PicWSInvokeTool.getValue(tpJsonObject,"tp3",vr.getTp3()));
			return vr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@ResponseBody
	@RequestMapping
	public Object exportQueryPasslist(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			// ======================
			// 查询限制时间(数据量大)
			// ======================
			result = this.passrecManager.exportPassrecList(conditions);
			List<VehPassrec> list = (List<VehPassrec>) result.get("rows");
			// 是否有数据判断
			if (list == null || list.size() == 0) {
				result.put("counts", "0");
			} else {
				result.put("counts", list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping
	public ModelAndView lister(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		
		ModelAndView mv = null;
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		try {
			if ((request.getParameter("kssj") == null)
					|| (request.getParameter("jssj").length() < 1)
					|| (request.getParameter("jssj") == null)
					|| (request.getParameter("kssj").length() < 1)) {
				Common.setAlerts("请输入必填项！", request);

				//if (userSession.getDepartment().getGlbm().startsWith("4300")) {
					return new ModelAndView("query/querystpassmain");
				//} else {
				//	return new ModelAndView("query/querypassmain");
				//}
			}
			mv = new ModelAndView("query/passdetail2");
			//mv.addObject("glbmlist", this.gateManager.getDepartmentsByKdbh());
			mv.addObject("hpzllist", this.systemManager.getCodes("030107"));
			mv.addObject("hpyslist", this.systemManager.getCodes("031001"));
			mv.addObject("fzjg", this.systemManager.getParameter("fzjg",
					request));
			Map<String, Object> conditions = Common.getParamentersMap(request);
			if(request.getParameter("f_hphm")!=null && !("".equals(request.getParameter("f_hphm")))){
				String hphm = URLDecoder.decode(request.getParameter("f_hphm"),"UTF-8");
				conditions.remove("hphm");
				conditions.put("f_hphm", hphm);
			}
			mv.addObject("cds", conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 
	 * 查询过车详细信息（含交警卡口过车）
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@RequestMapping
	public ModelAndView listerAndJj(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		ModelAndView mv = null;
		/*UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");*/
		try {
			if ((request.getParameter("kssj") == null)
					|| (request.getParameter("jssj").length() < 1)
					|| (request.getParameter("jssj") == null)
					|| (request.getParameter("kssj").length() < 1)) {
				Common.setAlerts("请输入必填项！", request);
				return new ModelAndView("query/querystpassandjjmain");
			}
			//mv = new ModelAndView("query/vehpassrecandjjdetail");
			mv = new ModelAndView("query/passdetail");
			mv.addObject("glbmlist", this.gateManager.getDepartmentsByKdbh());
			mv.addObject("hpzllist", this.systemManager.getCodes("030107"));
			mv.addObject("hpyslist", this.systemManager.getCodes("031001"));
			mv.addObject("fzjg", this.systemManager.getParameter("fzjg",
					request));
			mv.addObject("count", request.getParameter("count"));
			Map<String, Object> conditions = Common.getParamentersMap(request);
			String hphm = URLDecoder.decode(request.getParameter("f_hphm"),"UTF-8");
			
			conditions.remove("hphm");
			conditions.put("f_hphm", hphm);
			mv.addObject("cds", conditions);
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("query/querystpassandjjmain");
		}
		return mv;
	}
	
	/**
	 * <2016-7-20> 优化过车查询:精确号牌统计总数,无号牌不统计总数
	 * 
	 * 查询过车详细信息（含交警卡口过车）
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@RequestMapping
	public ModelAndView listerAndJj2(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		ModelAndView mv = null;
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		try {
			if ((request.getParameter("kssj") == null)
					|| (request.getParameter("jssj").length() < 1)
					|| (request.getParameter("jssj") == null)
					|| (request.getParameter("kssj").length() < 1)) {
				Common.setAlerts("请输入必填项！", request);
				return new ModelAndView("query/querystpassandjjmain");
			}
			//mv = new ModelAndView("query/vehpassrecandjjdetail");
			mv = new ModelAndView();
			// mv.addObject("glbmlist", this.gateManager.getDepartmentsByKdbh());
			mv.addObject("hpzllist", this.systemManager.getCodes("030107"));
			mv.addObject("hpyslist", this.systemManager.getCodes("031001"));
			mv.addObject("fzjg", this.systemManager.getParameter("fzjg",
					request));
			Map<String, Object> conditions = Common.getParamentersMap(request);
			String hphm = URLDecoder.decode(request.getParameter("f_hphm"),"UTF-8");
			
			Object cxfs = request.getParameter("cxfs");
			String count = request.getParameter("count");
			if(cxfs!=null && cxfs.toString().equals("0")&&Integer.parseInt(count)>200){ // 简易查询限制前200条
				count = "200";
			}
			if(cxfs!=null && cxfs.toString().equals("1")&&Integer.parseInt(count)>1000){ // 精确查询限制前1000条
				count = "1000";
			}
			if(hphm==null||"".equals(hphm)){
				//conditions.remove("count"); // 无号牌 1001
				mv.setViewName("query/passdetail2");
			}else{ // 精确号牌
				mv.addObject("count", count);
				mv.setViewName("query/passdetail");
			}
			conditions.remove("hphm");
			conditions.put("f_hphm", hphm);
			mv.addObject("cds", conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * <2016-12-19> 优化过车查询:精确号牌首次加载不统计总数.页面加载成功后
	 * 
	 * 查询过车详细信息（含交警卡口过车）
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@RequestMapping
	public ModelAndView listerAndJj3DS(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		ModelAndView mv = null;
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		try {
			Object kssj = request.getParameter("kssj");
			Object jssj = request.getParameter("jssj");
			if ((kssj == null)
					|| (kssj.toString().length() < 1)
					|| (jssj == null)
					|| (jssj.toString().length() < 1)) {
				Common.setAlerts("请输入必填项！", request);
				return new ModelAndView("query/querystpassandjjmainDS");
			}
			//mv = new ModelAndView("query/vehpassrecandjjdetail");
			mv = new ModelAndView();
			// mv.addObject("glbmlist", this.gateManager.getDepartmentsByKdbh());
			mv.addObject("hpzllist", this.systemManager.getCodes("030107"));
			mv.addObject("hpyslist", this.systemManager.getCodes("031001"));
			mv.addObject("fzjg", this.systemManager.getParameter("fzjg",
					request));
			Map<String, Object> conditions = Common.getParamentersMap(request);
			String hphm = URLDecoder.decode(request.getParameter("f_hphm"),"UTF-8");
			
			Object cxfs = request.getParameter("cxfs");
			Integer count = 0;
			if(cxfs!=null && cxfs.toString().equals("0")){ // 简易查询限制前200条
				count = 200;
			}
			if(cxfs!=null && cxfs.toString().equals("1")){ // 精确查询限制前1000条
				count = 1000;
			}
			if(hphm==null||"".equals(hphm)){
				//conditions.remove("count"); // 无号牌 1001
				// 无号牌设置结束时间为开始时间的前一天
				if(DateUtil.getDiffDays(kssj.toString(), jssj.toString()) > 1){
					kssj = DateUtil.getBeforeHours(jssj.toString(),24);
					conditions.put("kssj", kssj);
				}
				mv.setViewName("query/passdetail2DS");
			}else{ // 精确号牌
				mv.addObject("count", count);
				mv.setViewName("query/passdetailDS");
			}
			conditions.remove("hphm");
			conditions.put("f_hphm", hphm);
			mv.addObject("cds", conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	
	/**
	 * <2016-12-19> 优化过车查询:精确号牌首次加载不统计总数.页面加载成功后
	 * 
	 * 查询过车详细信息（含交警卡口过车）
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@RequestMapping
	public ModelAndView listerAndJj3(HttpServletRequest request,
			HttpServletResponse response, VehPassrec command) {
		ModelAndView mv = null;
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		try {
			Object kssj = request.getParameter("kssj");
			Object jssj = request.getParameter("jssj");
			if ((kssj == null)
					|| (kssj.toString().length() < 1)
					|| (jssj == null)
					|| (jssj.toString().length() < 1)) {
				Common.setAlerts("请输入必填项！", request);
				return new ModelAndView("query/querystpassandjjmain");
			}
			//mv = new ModelAndView("query/vehpassrecandjjdetail");
			mv = new ModelAndView();
			// mv.addObject("glbmlist", this.gateManager.getDepartmentsByKdbh());
			mv.addObject("hpzllist", this.systemManager.getCodes("030107"));
			mv.addObject("hpyslist", this.systemManager.getCodes("031001"));
			mv.addObject("fzjg", this.systemManager.getParameter("fzjg",
					request));
			Map<String, Object> conditions = Common.getParamentersMap(request);
			String hphm = URLDecoder.decode(request.getParameter("f_hphm"),"UTF-8");
			
			Object cxfs = request.getParameter("cxfs");
			Integer count = 0;
			if(cxfs!=null && cxfs.toString().equals("0")){ // 简易查询限制前200条
				count = 200;
			}
			if(cxfs!=null && cxfs.toString().equals("1")){ // 精确查询限制前1000条
				count = 1000;
			}
			if(hphm==null||"".equals(hphm)){
				//conditions.remove("count"); // 无号牌 1001
				// 无号牌设置结束时间为开始时间的前一天
				if(DateUtil.getDiffDays(kssj.toString(), jssj.toString()) > 1){
					kssj = DateUtil.getBeforeHours(jssj.toString(),24);
					conditions.put("kssj", kssj);
				}
				mv.setViewName("query/passdetail2");
			}else{ // 精确号牌
				mv.addObject("count", count);
				mv.setViewName("query/passdetail");
			}
			conditions.remove("hphm");
			conditions.put("f_hphm", hphm);
			mv.addObject("cds", conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 漫游查询主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardRoamQueryMain(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("hpzllist", this.systemManager
					.getCodes("030107"));
			request.setAttribute("urllist", this.urlManager.getCodeUrls());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("query/roamquerymain");
	}

	/**
	 * 获取漫游查询条件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView getRoamQueryConditions(HttpServletRequest request,
			HttpServletResponse response, VehPassrec vehPassrec) {
		try {
			request.setAttribute("hpzllist", this.systemManager.getCodes("030107"));
			request.setAttribute("urllist", this.urlManager.getCodeUrls());
			request.setAttribute("conditions", vehPassrec);
			String[] selecAll = request.getParameterValues("citys");
			if (selecAll != null && selecAll.length > 0) {
				request.setAttribute("sflag", "1");
			} else {
				request.setAttribute("sflag", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("query/roamquerymain");
	}

	/**
	 * 漫游查询
	 * 
	 * @param request
	 * @param response
	 * @param vehPassrec
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryRoamVehpassreclist(HttpServletRequest request,
			HttpServletResponse response, VehPassrec vehPassrec) {
		// Save roam Data
		List<VehPassrecEntity> list = null;
		// Web service execute message
		StringBuffer errorMsg = new StringBuffer();
		StringBuffer succeMsg = new StringBuffer();
		String hphm = "";
		// Save return Datas
		Map<String, Object> result = new HashMap<String, Object>();
		// Query conditions
		QueryVehPassrecConditions queryConditions = new QueryVehPassrecConditions();
		try {
			if(vehPassrec.getHphm()!=null){
			hphm = URLDecoder.decode(vehPassrec.getHphm(), "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		queryConditions.setKdbh(vehPassrec.getKdbh());
		queryConditions.setFxbh(vehPassrec.getFxbh());
		queryConditions.setHphm(hphm);
		queryConditions.setHpzl(vehPassrec.getHpzl());
		queryConditions.setKssj(vehPassrec.getKssj());
		queryConditions.setJssj(vehPassrec.getJssj());

		String glbm = vehPassrec.getGlbm();
		list = new LinkedList<VehPassrecEntity>();
		if (!"".equals(glbm)) {
			CodeUrl codeUrl = null;
			List<VehPassrecEntity> temp = null;
			try {
				codeUrl = this.urlManager.getUrl(glbm);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (codeUrl == null) {
				return null;
			}
			log.debug(codeUrl.getJdmc() + ":context="
					+ codeUrl.getContext());
			if ("dc".equals(codeUrl.getContext())) {
				//log.debug("漫游查询 " + codeUrl.getJdmc()
						//+ " 使用 axis2 查询 中 ... ");
				try {
					temp = passrecManager.queryPassrecDataByAxis2(codeUrl,
							vehPassrec);
					succeMsg.append(codeUrl.getJdmc());
					succeMsg.append("、");
				} catch (AxisFault e) {
					log.error("漫游查询 " + codeUrl.getJdmc()
							+ " axis2 连接出现异常 ... ", e);
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(连接异常)、");
				} catch (DocumentException e) {
					log.error("漫游查询 " + codeUrl.getJdmc()
							+ " 返回数据解析异常 ... ");
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(内部异常)、");
				} catch (NotDefPaserException e) {
					log.error("漫游查询 " + codeUrl.getJdmc()
							+ " 无法找到字段对应的映射处理器 ... ");
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(内部异常)、");
				} catch (Exception e) {
					log.error("未知异常", e);
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(内部异常)、");
				}
			} else {
				log.debug("漫游查询 " + codeUrl.getJdmc()
						+ " 使用 cxf 查询 中 ... ");
				// Web service client
				JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
				factory.setServiceClass(QueryVehPassrecService.class);
				// Request Informations
				factory.getOutInterceptors().add(new LoggingOutInterceptor());
				// Response Informations
				factory.getInInterceptors().add(new LoggingInInterceptor());
				factory.getOutInterceptors().add(new AddHeaderInterceptor());
				temp = getDatas(request, glbm, queryConditions, factory,
						errorMsg, succeMsg);
			}
			if (temp != null && temp.size() > 0) {
				list.addAll(temp);
			} else {
				if (errorMsg.length() == 0) {
					succeMsg.append("(无记录)、");
				}
			}
			log.debug("漫游查询 " + codeUrl.getJdmc() + " 查询总数 ： " + list.size());
			result.put("datas", list);
			result.put("emsg", errorMsg.toString());
			result.put("smsg", succeMsg.toString());
			result.put("size", list.size());
		}
		return result;
	}

	private List<VehPassrecEntity> getDatas(HttpServletRequest request,
			String glbm, QueryVehPassrecConditions queryConditions,
			JaxWsProxyFactoryBean factory, StringBuffer errorMsg,
			StringBuffer succeMsg) {
		StringBuffer url = new StringBuffer(20);
		CodeUrl codeUrl = null;
		List<VehPassrecEntity> temp = null;
		try {
			codeUrl = this.urlManager.getUrl(glbm);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		url.append("http://");
		url.append(codeUrl.getUrl());
		url.append(":");
		url.append(codeUrl.getPort());
		url.append(request.getContextPath());
		url.append("/service/QueryVehPassrecServer");
		factory.setAddress(url.toString());
		QueryVehPassrecService service = (QueryVehPassrecService) factory
				.create();
		this.setTimeOut(service);

		try {
			InetAddress address = InetAddress.getByName(codeUrl.getUrl());
			boolean state = address.isReachable(5000);
			if (state) {
			temp = service.queryVehPassrec(queryConditions);
			succeMsg.append(codeUrl.getJdmc());
			succeMsg.append("、");
			return temp;
			} else {
				errorMsg.append(codeUrl.getJdmc());
				errorMsg.append("(查询失败,网络异常!)、");
				return null;
			}
		} catch (Exception ex) {
			if (ex instanceof WebServiceException) {
				WebServiceException e1 = (WebServiceException) ex;
				String msg2 = e1.getCause().getMessage();
				if (msg2.indexOf("Read timed out") != -1) {
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(超时)、");
					return null;
				} else if (msg2.indexOf("404: Not Found") != -1) {
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(无效请求)、");
					return null;
				} else if (msg2.indexOf("503: Service Unavailable") != -1) {
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(无法到达)、");
					return null;
				} else if (msg2.indexOf("认证错误") != -1) {
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(认证错误)、");
					return null;
				} else {
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(异常)、");
					ex.printStackTrace();
					return null;
				}
			} else {
				errorMsg.append(codeUrl.getJdmc());
				errorMsg.append("(异常)、");
				ex.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * 请求、连接超时时间设置
	 * 
	 */
	private void setTimeOut(QueryVehPassrecService service) {
		Client proxy = ClientProxy.getClient(service);
		HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		HTTPClientPolicy policy = new HTTPClientPolicy();
		// 设置连接超时 100ms
		policy.setConnectionTimeout(100);
		// 设置请求超时
		policy.setReceiveTimeout(30000);
		conduit.setClient(policy);
	}
	
	@RequestMapping(params = "method=showXlsList", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> showXlsList(HttpServletRequest request,HttpServletResponse response) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String data = request.getParameter("datas");
		 	JSONArray array = JSONArray.fromObject(data);
		List<VehPassrecEntity> vehList = JSONArray.toList(array);

		int count = vehList.size() / 100;
		int remain = vehList.size() % 100;
		if (count != 0) {
			for (int i = 0; i < count; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", (i+1));
				map.put("name", "全国漫游查询" + (i+1)+".xls");
				map.put("begin", i * 100);
				map.put("end", (i + 1) * 100);
				list.add(map);
			}

			if (remain != 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", (count+1) );
				map.put("name", "全国漫游查询" + (count+1)+".xls");
				map.put("begin", (count) * 100);
				map.put("end", vehList.size());
				list.add(map);
			}
		}
		return list;
	}

	@RequestMapping(params = "method=downloadRoamVehList", method = RequestMethod.POST)
	public void downloadRoamVehList(HttpServletRequest request,HttpServletResponse response) {
		VehPassrecEntity veh = null;
		String data = request.getParameter("jsonstr");
		int begin = Integer.parseInt(request.getParameter("begin"));
		int end = Integer.parseInt(request.getParameter("end"));
		int excelwidth = Integer.parseInt(request.getParameter("excelwidth"));
		int excelheight = Integer.parseInt(request.getParameter("excelheight"));
   
		String[] columnName = new String[] { "地市", "号牌", "时间", "卡口", "方向",
				"设备", "照片1", "照片2", "照片3" };
		String[] methodName = new String[] { "City", "Hphm", "Gcsj", "Kdmc",
				"Fxmc", "Sbmc", "Tp1", "Tp2", "Tp3" };

		JSONArray array = JSONArray.fromObject(data);
		List<VehPassrecEntity> list = JSONArray.toList(array,VehPassrecEntity.class);
		list=list.subList(begin,end);
		
		// 创建Excel工作薄
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet("sheet1");
		HSSFPatriarch pa = sheet.createDrawingPatriarch();
		HSSFRow columnRow = sheet.createRow(0);
		columnRow.setHeight((short) 500);
		sheet.setDefaultColumnWidth((short)32);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = columnRow.createCell((short) i);
			cell.setCellStyle(ExcelStyle.excelStyle3(book));
			cell.setCellValue(columnName[i]);
		}
		// 组装Excel单元格内容
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 1);
			row.setHeight((short)400);
			veh = list.get(i);
			for (int j = 0; j < methodName.length; j++) {
				Object s = ClassUtil.invokeMethod(veh, "get" + methodName[j]);
				if (!methodName[j].startsWith("Tp")) {
					HSSFCell c = row.createCell((short) j);
					c.setCellStyle(ExcelStyle.excelStyle3(book));
					c.setCellValue(s.toString());
				} else {
					if (s != null) {
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,excelwidth, excelheight, (short) i, j,(short) i, j);
						byte[] pic = this.getPictureBytes(s.toString());// 获取图片二进制数组
						anchor.setAnchorType(2);
						pa.createPicture(anchor, book.addPicture(pic,HSSFWorkbook.PICTURE_TYPE_JPEG));
					}
				}
			}
		}	
		try {
			// 输出Excel工作薄
			response.setCharacterEncoding("UTF-8");
		    response.reset();
			response.setContentType("application/msexcel");
			response.setHeader("Content-Disposition", "attachment;Filename="
					+ new Date().getTime()
					+ new String("全国漫游查询".getBytes(), "ISO_8859_1") + ".xls");
			ServletOutputStream stream = response.getOutputStream();
			book.write(stream);
			stream.flush();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * 获取图片二进制流
	 * @param destUrl
	 * @return
	 */
	private byte[] getPictureBytes(String destUrl) {
		byte[] content = null;
		HttpURLConnection httpUrl = null;
		try {
			URL url = new URL(destUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			content = IOUtils.toByteArray(httpUrl.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	@ResponseBody
	@RequestMapping
	public void downloadPic(String urls,HttpServletResponse response) {
		
		Long current = new Date().getTime();
		String realPath ="";
		try {
		realPath = FileUtil.getFileHomeRealPath();
		//临时存放图片路径
		FileUtil.createFolderNotExists(realPath+"/"+current);
		JSONArray ja = JSONArray.fromObject(urls);
		// 图片地址
		String imgUrl = "";
		// 图片格式
		String suffix = "";
		// 随机文件名
		//String uuid = "";
		String hphm = "";
		String gcsj = "";
		
		
			//下载图片到磁盘中临时存储
			for (int i = 0; i < ja.size(); i++) {
				imgUrl = (String) ja.getJSONObject(i).get("img");
				hphm = URLDecoder.decode((String) ja.getJSONObject(i).get("hphm"), "utf-8");
				gcsj = (String) ja.getJSONObject(i).get("gcsj");
				if(imgUrl == null || "".equals(imgUrl)) {
					continue;
				}
				suffix = FilenameUtils.getExtension(imgUrl);
				//uuid = UUID.randomUUID().toString();
				Long current1 = new Date().getTime();
				//String filePath = realPath+"/"+current+"/" + uuid + "." + suffix;
				String filePath = realPath+"/"+current+"/" + (hphm+"_"+gcsj+"_"+current1).replaceAll(":", "-").replaceAll(" ", "-") + "." + suffix;
				createImage(imgUrl, filePath);
			}
			//把下载的图片打包成压缩包
			List<File> files= Arrays.asList(new File(realPath+"/"+current).listFiles());
			if(files != null && files.size()>0) {
				FileUtil.zip(files, realPath+"/"+current+".zip");
			}
			response.reset();
			OutputStream os = response.getOutputStream();
			if(!FileUtil.checkFile(realPath+"/"+current+".zip")) {
				FileUtil.delFolder(realPath+"/"+current);
				
			} else {
				File file = new File(realPath+"/"+current+".zip");
				InputStream in = new FileInputStream(file);
				((HttpServletResponse) response).addHeader("Content-Disposition","attachment;filename=" + file.getName());
				((HttpServletResponse) response).addHeader("Content-length", ""+file.length());
				response.setContentType("application/stream");
				IOUtils.copy(in, os);
				IOUtils.closeQuietly(in);
				os.flush();
				os.close();
				       
		        //下载完成后删除本地文件
		        FileUtil.delFolder(realPath+"/"+current);
		        FileUtil.deleteFile(realPath+"/"+current+".zip");
				
			}
			
		} 
		catch (Exception e) {
			FileUtil.delFolder(realPath+"/"+current);
	        FileUtil.deleteFile(realPath+"/"+current+".zip");
			e.printStackTrace();
		} catch (Error e) {
			FileUtil.delFolder(realPath+"/"+current);
	        FileUtil.deleteFile(realPath+"/"+current+".zip");
		}
	}

	public static void createImage(String imgurl, String filePath)  {
		try {			
		URLConnection conn = null;
		if(imgurl.startsWith("h")||imgurl.startsWith("H")||imgurl.startsWith("f")||imgurl.startsWith("F")){
		URL url = new URL(imgurl);
		if(imgurl.startsWith("h")||imgurl.startsWith("H")) {				
			conn = (HttpURLConnection) url.openConnection();
		} else if (imgurl.startsWith("f")||imgurl.startsWith("F")) {	
			conn = (FtpURLConnection) url.openConnection();
		}
		conn.setConnectTimeout(10000);
		System.out.println("+++++++++++++++++++"+conn.getHeaderField(0));
		if(conn.getHeaderField(0)!=null){
		if(conn.getHeaderField(0).indexOf("OK")!=-1){
		InputStream inputStream = conn.getInputStream(); // 通过输入流获得图片数据		
		byte[] getData = readInputStream(inputStream); // 获得图片的二进制数据		
		File imageFile = new File(filePath);		
		FileOutputStream fos = new FileOutputStream(imageFile);
		fos.write(getData);	
		fos.close();
		}				
		}
		}
		}catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public static byte[] readInputStream(InputStream inputStream) {
		try {
			byte[] buffer = new byte[1024];
			int len = 0;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while ((len = inputStream.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.close();
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping
	@ResponseBody
	public int queryPassGetAllCount(HttpServletRequest request,HttpServletResponse response){

		int  result = 0;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			result = this.passrecManager.getAllCount(conditions);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	
		
	}
	
	/**
	 * 
	 * 查询过车信息的总数（含交警卡口）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public int queryPassAndJjGetAllCount(HttpServletRequest request,HttpServletResponse response){
		int  result = 0;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			result = this.passrecManager.getPassAndJjAllCount(conditions);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * <2016-5-19> licheng 解决由响应速度造成的过车图片顺序混乱问题,在方法中新增一个参数by1,作为唯一标识
	 * 通过ajax得到长沙的图片
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getPic(HttpServletRequest request,
			HttpServletResponse response,String tp1,String tp2,String tp3,String kdbh, String by1) {
		VehPassrec v  = null;
		try {
			v = new VehPassrec();
			v.setTp1(tp1);
			v.setTp2(tp2);
			v.setTp3(tp3);
			v.setKdbh(kdbh);
			v.setBy1(by1==null?"":by1);
			JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(v,v.getKdbh());
			if(tp1!=null&&tp1!=""){
				v.setTp1(PicWSInvokeTool.getValue(tpJsonObject,"tp1",v.getTp1()).replaceAll("\\\\", "/"));
			}
			if(tp2!=null&&tp2!=""){
				v.setTp2(PicWSInvokeTool.getValue(tpJsonObject,"tp2",v.getTp2()).replaceAll("\\\\", "/"));
			}
			if(tp3!=null&&tp3!=""){
				v.setTp3(PicWSInvokeTool.getValue(tpJsonObject,"tp3",v.getTp3()).replaceAll("\\\\", "/"));
			}
			if(by1!=null&&by1!=""){
				v.setBy1(PicWSInvokeTool.getValue(tpJsonObject,"by1",v.getBy1()).replaceAll("\\\\", "/"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}
	
	/**
	 * 将过车信息导入到excel
	 * @param request
	 * @param response
	 * @return url
	 */
	@ResponseBody
	@RequestMapping
	public String preload(HttpServletRequest request, HttpServletResponse response) {
		String filename = "";
		try {
			String path = request.getSession().getServletContext().getRealPath("/")+"/download/";
			Map<String, Object> conditions = Common.getParamentersMap(request);
			if(conditions.containsKey("hphm")){
				String hphm = conditions.get("hphm").toString();
				hphm = URLDecoder.decode(hphm, "UTF-8");
					//URLDecoder.decode(hphm, "UTF-8");
				//String hphm = java.net.URLDecoder.decode(conditions.get("hphm").toString(),"utf-8");
				conditions.remove("hphm");
				conditions.put("hphm", hphm);
			}
			conditions.put("path", path);
			filename = this.passrecManager.PassrecAndJjListFileUrl(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename;
	}
	

	/**
	 * 过车图片下载
	 * @param urls
	 * @param request
	 * @param response
	 * @return filename 
	 */
	@ResponseBody
	@RequestMapping
	public String downloadPicZip(String urls, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String filename = DateUtils.getCurrTimeStr(1)+".zip";
		JSONArray ja = JSONArray.fromObject(urls);
		// 图片地址
		String imgUrl = "";
		// 图片格式
		String suffix = "";
		// 文件名
		String hphm = "";
		String gcsj = "";
		FileOutputStream fout = null;
		Map<String, String> map = null;
		try {
			File file = new File(request.getSession().getServletContext().getRealPath("/")+"/download/"+filename+"");
			fout = new FileOutputStream(file);
			map = new HashMap<String, String>();
			//下载图片到磁盘中临时存储
			for (int i = 0; i < ja.size(); i++) {
				JSONObject passrec = ja.getJSONObject(i);
				if(passrec != null){
					gcsj = (String) passrec.get("gcsj");
					Object _hphm = passrec.get("hphm");
					Object _imgUrl = passrec.get("img");
					if(_hphm == null || "".equals(_hphm)){
						hphm = "-";
					}else {
						hphm = URLDecoder.decode(URLDecoder.decode((String)_hphm, "UTF-8"), "UTF-8"); 
					}
					//过滤loading图片/无图片情况
					if(_imgUrl == null || "".equals(_imgUrl) || _imgUrl == "loading3.gif") {
						continue;
					}else {
						imgUrl = URLDecoder.decode(URLDecoder.decode(URLDecoder.decode((String)_imgUrl, "UTF-8"), "UTF-8"));
					}
					//无后缀统一给jpg或者先获取二进制流,再取后缀
					suffix = FilenameUtils.getExtension(imgUrl);
					if(suffix == null || "".equals(suffix)){
						suffix="jpg";
					}
					/**原将图片名加时间戳后缀，存在重复。改为序号*/
					map.put((hphm+"_"+gcsj).replaceAll(":", "-").replaceAll(" ", "-") + "_"+ i+ "." + suffix, imgUrl);
				}
			}
			PicturePackZipTools.compressZip(map, fout, null, true);
			return filename;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fout)
					fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	
}
