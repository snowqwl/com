package com.sunshine.monitor.comm.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.core.util.Assert;
import com.sunshine.core.util.DateUtil;
import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.service.MenuManager;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.Ping;
import com.sunshine.monitor.comm.util.VehCardType;
import com.sunshine.monitor.comm.util.ftp.FtpUtil;
import com.sunshine.monitor.comm.util.picws.PicWSInvokeTool;
import com.sunshine.monitor.comm.vehicle.VehQuery;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.RealalarmManager;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.ZgfxBean;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.TztbManager;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.manager.service.VersionLogManager;
import com.sunshine.monitor.system.manager.service.ZgfxManager;
import com.sunshine.monitor.system.query.service.CollectVehManager;
import com.sunshine.monitor.system.sign.service.BKSignManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.bean.VehRealpass;
import com.sunshine.monitor.system.veh.service.RealManager;
import com.sunshine.monitor.system.ws.GateService.CodeGateConditionService;
import com.sunshine.monitor.system.ws.GateService.bean.CodeGateEntity;
import com.sunshine.monitor.system.ws.GateService.bean.CodeGateExtendEntity;
import com.sunshine.monitor.system.ws.util.AddHeaderInterceptor;

@Controller
@RequestMapping(value = "/ajax.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AJAXCtrl {

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private SuspinfoManager suspinfoManager;

	@Autowired
	private GateManager gateManager;

	@Autowired
	private RealManager realpassManager;

	@Autowired
	private RealalarmManager realalarmManager;

	@Autowired
	private DepartmentManager departmentManager;
	
	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	@Qualifier("urlManager")
	private UrlManager urlManager;
	
	@Autowired
	private CollectVehManager collectVehManager;
	
	@Autowired
	private BKSignManager bkSignManager;
	
	@Autowired
	private ZgfxManager zgfxManager;
	
	@Autowired
	private TztbManager tztbManager;
	
	@Autowired
	private VersionLogManager versionLogManager;
	
	private Logger log = LoggerFactory.getLogger(AJAXCtrl.class);
	
	@RequestMapping
	public void getDatas(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuilder buf = new StringBuilder();
		buf.append("<chart caption='zjshuai' xAxisName='Month' yAxisName='Units' showValues='0'");
		buf.append(" decimals='0' formatNumberScale='0' chartRightMargin='30'>");
		buf.append("<set label='Jan' value='462'/>");
		buf.append("<set label='Feb' value='857'/>");
		buf.append("<set label='Mar' value='671'/>");
		buf.append("<set label='Apr' value='494'/>");
		buf.append("<set label='May' value='761'/>");
		buf.append("<set label='Jun' value='960'/>");
		buf.append("<set label='Jul' value='629'/>");
		buf.append("<set label='Aug' value='622'/>");
		buf.append("<set label='Sep' value='376'/>");
		buf.append("<set label='Oct' value='494'/>");
		buf.append("<set label='Nov' value='761'/>");
		buf.append("<set label='Dec' value='960'/>");
		buf.append("</chart>");

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");  
		System.out.println(buf.toString());
		try {
			response.getWriter().print(buf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * @Description:漫游卡口条件查询
	 * @param: 
	 * @return: 
	 * @version:
	 * @author: TDD
	 * @date: 2014-9-25 上午08:54:26
	 */
	@ResponseBody
	@RequestMapping
	public Object getGateCondition(HttpServletRequest request,
			HttpServletResponse response,VehPassrec vehPassrec) {
		String glbm = vehPassrec.getGlbm();
		StringBuffer url = new StringBuffer(20);
		StringBuffer errorMsg = new StringBuffer("");
		StringBuffer succeMsg = new StringBuffer("");
		Map<String, Object> result = new HashMap<String, Object>();
		if (!"".equals(glbm)) {
			CodeUrl codeUrl = null;
			try {
				codeUrl = this.urlManager.getUrl(glbm);			
			if (codeUrl == null) {
				return null;
			}
			//判断ip是否联通
			InetAddress address = InetAddress.getByName(codeUrl.getUrl());
			boolean state = address.isReachable(5000);
			if (state) {
				// Web service client
				JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
				factory.setServiceClass(CodeGateConditionService.class);
				// Request Informations
				factory.getOutInterceptors().add(new LoggingOutInterceptor());
				// Response Informations
				factory.getInInterceptors().add(new LoggingInInterceptor());
				factory.getOutInterceptors().add(new AddHeaderInterceptor());
				url.append("http://");
				url.append(codeUrl.getUrl());
				url.append(":");
				url.append(codeUrl.getPort());
				url.append(request.getContextPath());
				url.append("/service/CodeGateConditionService");
				factory.setAddress(url.toString());
				CodeGateConditionService service = (CodeGateConditionService) factory.create();
				Client proxy = ClientProxy.getClient(service);
				HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
				HTTPClientPolicy policy = new HTTPClientPolicy();
				// 设置连接超时 3000ms
				policy.setConnectionTimeout(3000);
				// 设置请求超时
				policy.setReceiveTimeout(30000);
				conduit.setClient(policy);	
				//this.setTimeOut(service);
					if (vehPassrec.getKdbh() == null) {
						List<CodeGateEntity> list = service.getGates();
						if (list != null && list.size() > 0) {
							result.put("datas", list);
							succeMsg.append("查询成功！");
						} else {
							if (errorMsg.length() == 0) {
								succeMsg.append("查询成功！(无记录)、");
							}
						}
					} else {
						List<CodeGateExtendEntity> list = service.getDirectName(vehPassrec.getKdbh());
						if (list != null && list.size() > 0) {
							result.put("datas", list);
							succeMsg.append("查询成功！");
						} else {
							if (errorMsg.length() == 0) {
								succeMsg.append("查询成功！(无记录)、");
							}
						}
					}
				} else {
					errorMsg.append(codeUrl.getJdmc());
					errorMsg.append("(查询失败,网络异常!)、");
				}
					} catch (Exception ex) {
						if (ex instanceof WebServiceException) {
							//ex.printStackTrace();
							WebServiceException e1 = (WebServiceException) ex;
							String msg2 = e1.getCause().getMessage();
							if (msg2.indexOf("Read timed out") != -1) {
								errorMsg.append(codeUrl.getJdmc());
								errorMsg.append("(超时)、");
							} else if (msg2.indexOf("404: Not Found") != -1) {
								errorMsg.append(codeUrl.getJdmc());
								errorMsg.append("(无效请求)、");
							} else if (msg2.indexOf("503: Service Unavailable") != -1) {
								errorMsg.append(codeUrl.getJdmc());
								errorMsg.append("(无法到达)、");
							} else if (msg2.indexOf("认证错误") != -1) {
								errorMsg.append(codeUrl.getJdmc());
								errorMsg.append("(认证错误)、");
							} else {
								errorMsg.append(codeUrl.getJdmc());
								errorMsg.append("(异常)、");
								ex.printStackTrace();
							}
						} else {
							errorMsg.append(codeUrl.getJdmc());
							errorMsg.append("(异常)、");
							ex.printStackTrace();
						}
					}
				}
		
		result.put("emsg", errorMsg.toString());
		result.put("smsg", succeMsg.toString());
		return result;
	}
	
	/**
	 * 获取报警大类
	 * 
	 * @author licheng
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public List getBjdl(HttpServletRequest request, HttpServletResponse response) {
		List<Code> list = null;
		try {
			// 位置4说明代码类别（即报警大类或布控大类）
			list = this.systemManager.getCode("120019");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据布控大类查询布控类别
	 * 
	 * @author oy
	 * @param bjdl
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getBjlx(@RequestParam String bjdl,
			HttpServletRequest request, HttpServletResponse response) {
		List<Code> list = null;
		String _bjdl = request.getParameter("bjdl");
		try {
			// 位置4说明代码类别（即报警大类或布控大类）
			list = this.systemManager.getCodesByDmsm("120005", _bjdl, 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据布控大类查询布控级别 涉案类---一级 交通违法类---一级或二级 管控类---三级
	 * 
	 * @author oy
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getBkjb(HttpServletRequest request,
			HttpServletResponse response) {
		String bkdl = request.getParameter("bkdl");
		List<Code> list = null;
		try {
			list = this.systemManager.getCodesByDmsm("120020", bkdl, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据布控大类查询报警预案 涉案类----拦截 交通违法类---拦截或不拦截 管控类---不拦截
	 * 
	 * @author oy
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getBjya(HttpServletRequest request,
			HttpServletResponse response) {
		String bkdl = request.getParameter("bkdl");
		List<Code> list = null;
		try {
			if ("1".equals(bkdl))
				list = this.systemManager.getCodesByDmsm("130022", "1", 2);
			else {
				list = this.systemManager.getCodes("130022");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据布控大类查询布控性质 1、涉案类、交通违法类--公开 2、管控类---私密
	 * 
	 * @author oy
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getBkxz(HttpServletRequest request,
			HttpServletResponse response) {
		String bkdl = request.getParameter("bkdl");
		List<Code> list = null;
		try {
			if ("3".equals(bkdl))
				list = this.systemManager.getCodesByDmsm("120021", "1", 3);
			else {
				list = this.systemManager.getCodesByDmsm("120021", "1", 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据号牌号码各号牌种类查询机动车库
	 * 
	 * @author oy
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getVehicle(HttpServletRequest request,
			HttpServletResponse response) {
		VehQuery vq = new VehQuery();
		String hphm = null;
		String hpzl = request.getParameter("hpzl");
		try {
			hphm = URLDecoder.decode(request.getParameter("hphm"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String vehicle = vq.getVehilceInfo(request, hpzl, hphm);
		return vehicle;
	}

	/**
	 * 检查号牌号码是否已经存在布控信息 更新：增加过滤管控信息 2013/10/12 条件：号牌号码、号牌种类 未撤控的布控信息
	 * 
	 * @author oy
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object checkSusp(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuffer sb = new StringBuffer();
		try {
			String hphm = request.getParameter("hphm");
			String hpzl = request.getParameter("hpzl");
			hphm = URLDecoder.decode(hphm, "utf-8");
			List<VehSuspinfo> list = (List<VehSuspinfo>) this.suspinfoManager.getSuspinfoByHphm(
					hphm, hpzl, false).get("data");
			if ((list == null) || (list.size() < 1)) {
				sb.append("[]");
			} else {
				sb.append("[");
				for (Iterator<VehSuspinfo> it = list.iterator(); it.hasNext();) {
					VehSuspinfo vs = (VehSuspinfo) it.next();
					sb.append("{'bkxh':'")
							.append(vs.getBkxh())
							.append("','bklbmc':'")
							.append(URLEncoder.encode(this.systemManager
									.getCodeValue("120005", vs.getBklb()),
									"UTF-8"))
							.append("','lar':'")
							.append(URLEncoder.encode(vs.getLar(), "UTF-8"))
							.append("','bkjgmc':'")
							.append(URLEncoder.encode(this.systemManager
									.getDepartmentName(vs.getBkjg()), "UTF-8"))
							.append("','bkjzsj':'")
							.append(vs.getBkjzsj())
							.append("','ywzt':'")
							.append(vs.getYwzt())
							.append("','ywztmc':'")
							.append(URLEncoder.encode(this.systemManager
									.getCodeValue("120008", vs.getYwzt()),
									"UTF-8")).append("','ladwlxdh':'")
							.append(vs.getLadwlxdh()).append("','ladw':'")
							.append(URLEncoder.encode(vs.getLadw(), "UTF-8"))
							.append("'},");
				}
				if (sb.length() == 1)
					sb.append(']');
				else
					sb.setCharAt(sb.length() - 1, ']');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@ResponseBody
	@RequestMapping
	public Object getSuspinfo(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuffer sb = new StringBuffer();
		try {
			String hphm = request.getParameter("hphm");
			String hpzl = request.getParameter("hpzl");
			if ((hpzl == null) || (hpzl.length() < 1) || (hphm == null)
					|| (hphm.length() < 1)) {
				sb.append("[]");
				return sb.toString();
			}
			hphm = URLDecoder.decode(hphm, "utf-8");
			List<VehSuspinfo> list = (List<VehSuspinfo>) this.suspinfoManager.getSuspinfoByHphm(
					hphm, hpzl, true).get("data");
			if ((list == null) && (list.size() < 1)) {
				sb.append("[]");
			} else {
				sb.append("[");
				for (Iterator<VehSuspinfo> it = list.iterator(); it.hasNext();) {
					VehSuspinfo vs = (VehSuspinfo) it.next();
					sb.append("{'bkxh':'")
							.append(vs.getBkxh())
							.append("','bklb':'")
							.append(vs.getBklb())
							.append("','bklbmc':'")
							.append(URLEncoder.encode(this.systemManager
									.getCodeValue("120005", vs.getBklb()),
									"UTF-8"))
							.append("','bkjg':'")
							.append(vs.getBkjg())
							.append("','bkjgmc':'")
							.append(URLEncoder.encode(this.systemManager
									.getDepartmentName(vs.getBkjg()), "UTF-8"))
							.append("','bksj':'")
							.append(vs.getBksj())
							.append("','ywzt':'")
							.append(vs.getYwzt())
							.append("','ywztmc':'")
							.append(URLEncoder.encode(this.systemManager
									.getCodeValue("120008", vs.getYwzt()),
									"UTF-8"))
							.append("','bjzt':'")
							.append(vs.getBjzt())
							.append("','bjztmc':'")
							.append(URLEncoder.encode(this.systemManager
									.getCodeValue("130009", vs.getBjzt()),
									"UTF-8")).append("'},");
				}
				if (sb.length() == 1)
					sb.append(']');
				else
					sb.setCharAt(sb.length() - 1, ']');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 部门警力情况
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping
	public void getDepartmentStat(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		StringBuffer sb = new StringBuffer();
		try {
			String dwdms = request.getParameter("dwdms");
			dwdms = dwdms.substring(0, dwdms.length() - 1);
			PrintWriter printwriter = response.getWriter();
			String[] dwa = dwdms.split(",");
			for (int i = 0; i < dwa.length; i++) {
				Department dep = this.systemManager.getDepartment(dwa[i]);
				if (i != 0) {
					sb.append("<br>");
				}
				sb.append(dep.getBmmc()).append("（警员数：").append(dep.getJyrs())
						.append("；联系电话：").append(dep.getLxdh1()).append("）");
			}
			printwriter.write(sb.toString());
		} catch (Exception e) {
			sb.delete(0, sb.length());
			sb.append("{'date':'" + e.getLocalizedMessage() + "'}");
		}
	}

	/**
	 * <2016-11-3> 修改实时过车数据从redis中查询 licheng
	 * 函数功能说明:详细过车信息 修改日期 2013-7-5
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @return Object
	 */
	@RequestMapping
	@ResponseBody
	public Object getRealPassDetail(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuffer sb = new StringBuffer();
		try {
			String kdbh = request.getParameter("kdbh");
			String fxbh = request.getParameter("fxbh");
			String cdbh = request.getParameter("cdbh");
			String city = request.getParameter("city");
/*			if(city != null && !"".equals(city)){
				String url = getRequestUrl(request,city);
				System.out.println("单卡口实时过车监测："+url);
				*//**Requesting Remote Service and Filtering Double Quotes of Result*//*
				sb.append(requestRemote(url, null).replace("\"", ""));
			} else {*/
				// 增加卡口今日过车总量统计
				//long gcTotal =  this.realpassManager.getVehpassByKdbh(kdbh,fxbh,cdbh);
				VehRealpass vr = this.realpassManager.getVehRealPassRedis(kdbh,fxbh,cdbh);
				if (vr == null) {
					sb.append("{'errorMsg':'无实时过车信息上传'}");
				} else {
				     // 处理长沙卡口图片
					JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(vr,kdbh);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					long between = (date.getTime() - sdf.parse(vr.getGcsj())
							.getTime()) / 60000L;
					//long between = System.currentTimeMillis()- Long.parseLong(vr.getGcsj());
					sb.append("{'cllx':'")
							.append(systemManager.getCodeValue("030104",
									vr.getCllx()))
							.append("','fxmc':'")
							.append(URLEncoder.encode(
									this.gateManager.getDirectName(vr.getFxbh()),
									"UTF-8"));
					sb.append("','kdmc':'")
							.append(URLEncoder.encode(
									this.gateManager.getGateName(vr.getKdbh()),
									"UTF-8"))
							.append("','csys':'")
							.append(systemManager.getCodeValue("030108",
									vr.getCsys()));
					sb.append("','cwhphm':'")
							.append(URLEncoder.encode(vr.getCwhphm(),"UTF-8"))
							.append("','cwhpys':'")
							.append(systemManager.getCodeValue("031001",
									vr.getCwhpys()));
					sb.append("','gcxh':'")
							.append(vr.getGcxh())
							.append("','kdbh':'")
							.append(vr.getKdbh())
							.append("','hpzl':'")
							.append(vr.getHpzl());
					sb.append("','hphm':'")
							.append(URLEncoder.encode(vr.getHphm(), "utf-8"))
							.append("','hpys':'");
					sb.append(URLEncoder.encode(systemManager.getCodeValue("031001", vr.getHpys()),"utf-8"))
							.append("','hpzlmc':'")
							.append(URLEncoder.encode(systemManager.getCodeValue("030107",
									vr.getHpzl()),"utf-8"))
							.append("','xszt':'")
							.append(URLEncoder.encode(systemManager.getCodeValue("110005",
									vr.getXszt()),"utf-8"));
					sb.append("','clsd':'").append(vr.getClsd())
							.append("','cdbh':'").append(vr.getCdbh())
							.append("','gcsj':'").append(vr.getGcsj());
					sb.append("','tp1':'")
							.append(PicWSInvokeTool.getValue(tpJsonObject,"tp1",vr.getTp1()).replaceAll("\\\\", "/"))
							.append("','tp2':'")
							.append(PicWSInvokeTool.getValue(tpJsonObject,"tp2",vr.getTp1()).replaceAll("\\\\", "/"));
					sb.append("','tp3':'")
							.append(PicWSInvokeTool.getValue(tpJsonObject,"tp3",vr.getTp1()).replaceAll("\\\\", "/"))
							.append("'");
					sb.append(",'rgcl':'").append(vr.getDailyCount()).append("'");
					if (between > 30L)
						sb.append(",'csbj':'1'");
					else {
						sb.append(",'csbj':'0'");
					}
					sb.append("}");
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb.delete(0, sb.length());
			sb.append("{'errorMsg':'服务器发生异常：" + e.getMessage() + "'}");
		}
		return sb.toString();
	}
	
	/**
	 * 分析组装请求URL
	 * @param request
	 * @return
	 */
	public String getRequestUrl(HttpServletRequest request, String city) throws Exception{
		/** Getting Application System Request Information of State*/
		CodeUrl codeUrl = urlManager.getUrl(city);
		Assert.isNull(codeUrl, "市州CODEURL表未找到记录!");
		if(codeUrl.getUrl()==null || codeUrl.getContext()==null || codeUrl.getPort()==null){
			Assert.isNull(codeUrl, "市州CODEURL配置不完整(IP或CONEXT或PORT),请检查!");
		}
		/** http://ip:port/context*/
		StringBuffer url = new StringBuffer("http://"
				+ codeUrl.getUrl() + ":" + codeUrl.getPort() + "/"
				+ codeUrl.getContext());
		/**.do*/
		String doStr = request.getRequestURI().substring(
				request.getRequestURI().lastIndexOf("/"), 
				request.getRequestURI().length());
		url.append(doStr);
		url.append("?");
		/**Filter parameter city*/
		String queryStr = request.getQueryString();
		/**&city=430000000000&para1=v1&para2=v2*/
		int pos = -1 ;
		if((pos = queryStr.indexOf("city")) != -1){
			String t1 = queryStr.substring(0, pos-1);
			String t2 = queryStr.substring(pos,queryStr.length());
			url.append(t1);
			int pos2 = t2.indexOf("&");
			if(pos2 != -1){
				url.append(t2.substring(pos2,t2.length()));
			} 
		} else {
			url.append(queryStr);
		}
		return url.toString();
	}
	
	/**
	 * 远程接口调用
	 * @param reqUrl
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String requestRemote(String reqUrl, Object data) throws Exception{
		String content = null;
		HttpClient httpclient = null;
		try {
			httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(reqUrl);
			httpGet.setHeader("Content-type","text/html;charset=utf-8");
			httpGet.setHeader("Accept-Charset", "utf-8");
			HttpResponse httpResponse = httpclient.execute(httpGet);
			StatusLine sl = httpResponse.getStatusLine();
			if(sl.getStatusCode() >= 300){
				throw new Exception("应用服务器连接异常【"+sl.getStatusCode()+"】!");
			}
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) { 
			    content = EntityUtils.toString(httpEntity);  
			}
		} catch (HttpHostConnectException e) {
			throw new Exception("应用服务器请求连接超时!");
		} finally{
			/*** Close Connection */
			httpclient.getConnectionManager().shutdown();  
		}
		return content;
	}
	
	/**
	 * 函数功能说明:多卡口过车监控列表
	 * @param request
	 * @return
	 * @return Object
	 */
	@RequestMapping
	@ResponseBody
	public Object getRealPassList(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		try {
			String jkds = request.getParameter("kds");
			String city = request.getParameter("city");
/*			if(city != null && !"".equals(city)){
				String url = getRequestUrl(request,city);
				System.out.println("多卡口实时过车监测："+url);
				*//**Requesting Remote Service and Filtering Double Quotes of Result*//*
				sb.append(requestRemote(url, null).replace("\"", ""));
			} else {*/
				String[] kds = jkds.split(",");
				List realPassList = this.realpassManager.getRealPassListRedis(kds);
				if ((realPassList == null) && (realPassList.size() < 1)) {
					sb.append("[]");
				} else {
					sb.append("[");
					for (int i = 0; i < realPassList.size(); i++) {
						VehRealpass vr = (VehRealpass) realPassList.get(i);
						if(vr!=null){
							// 处理长沙卡口图片
							//JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(vr,vr.getKdbh());
							String fxmc;
							try {
								CodeGateExtend direct = this.gateManager.getDirect(vr
										.getFxbh());
								if (direct == null)
									fxmc = vr.getFxbh();
								else
									fxmc = URLEncoder.encode(direct.getFxmc(), "UTF-8");
							} catch (Exception e) {
								fxmc = vr.getFxbh();
							}
							String kdmc;
							try {
								CodeGate gate = this.gateManager.getGate(vr.getKdbh());
								if (gate == null)
									kdmc = vr.getKdbh();
								else
									kdmc = URLEncoder.encode(gate.getKdmc(), "UTF-8");
							} catch (Exception e) {
								kdmc = vr.getKdbh();
							}
							sb.append("{'hphm':'")
							.append(URLEncoder.encode(vr.getHphm(), "UTF-8"))
							.append("','kdbh':'").append(vr.getKdbh())
							.append("','gcsj':'").append(vr.getGcsj())
							.append("','kdmc':'");
							sb.append(kdmc).append("','fxmc':'").append(fxmc)
							.append("','gcxh':'").append(vr.getGcxh())
							.append("','cd':'").append(vr.getCdbh())
							.append("','fxbh':'").append(vr.getFxbh())
							.append("','clsd':'").append(vr.getClsd())
							.append("','cwhphm':'").append(vr.getCwhphm())
							.append("','cwhpys':'").append(vr.getCwhpys())
							.append("','xszt':'").append(vr.getXszt())
							.append("','kdbh':'").append(vr.getKdbh());
							sb.append("','tp1':'")
							.append(vr.getTp1())
							.append("','tp2':'")
							.append(vr.getTp2())
							.append("','tp3':'")
							.append(vr.getTp3())
							.append("'},");
							/*sb.append("','tp1':'")
							.append(PicWSInvokeTool.getValue(tpJsonObject,"tp1",vr.getTp1()).replaceAll("\\\\", "/"))
							.append("','tp2':'")
							.append(PicWSInvokeTool.getValue(tpJsonObject,"tp2",vr.getTp2()).replaceAll("\\\\", "/"))
							.append("','tp3':'")
							.append(PicWSInvokeTool.getValue(tpJsonObject,"tp3",vr.getTp3()).replaceAll("\\\\", "/"))
							.append("'},");*/
						}
					}
					if (sb.length() == 1)
						sb.append(']');
					else
						sb.setCharAt(sb.length() - 1, ']');
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb.delete(0, sb.length());
			sb.append("[]");
		}

		return sb.toString();
	}

	/**
	 * 单卡口实时报警信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getRealAlarmDetail(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuffer sb = new StringBuffer();
		try {
			String kdbh = request.getParameter("kdbh");
			String bklbs = request.getParameter("bklbs");
			String city = request.getParameter("city");
			if (city != null && !"".equals(city)) {
				String url = getRequestUrl(request,city);
				System.out.println("单卡口实时报警监测："+url);
				/**Requesting Remote Service and Filtering Double Quotes of Result*/
				sb.append(requestRemote(url, null).replace("\"", ""));
			} else {
				VehAlarmrec va = this.realalarmManager.getVehAlarmDetail(kdbh,
						bklbs);
				// 增加卡口今日报警数量
				long bjTotal = this.realalarmManager.getRealAlarmByKdbh(kdbh);
				if (va == null) {
					sb.append("{}");
				} else {
					// 处理长沙卡口图片
					JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(va,va.getKdbh());
					sb.append("{'cllx':'")
							.append(getCodeValue("030104", va.getCllx()))
							.append("','fxmc':'")
							.append(URLEncoder.encode(this.gateManager
									.getDirectName(va.getKdbh()+va.getFxbh()), "UTF-8"));
					sb.append("','kdmc':'")
							.append(URLEncoder.encode(
									this.gateManager.getGateName(va.getKdbh()),
									"UTF-8")).append("','csys':'")
							.append(getCodeValue("030108", va.getCsys()));
					sb.append("','bjsj':'").append(va.getBjsj())
							.append("','bjlx':'")
							.append(getCodeValue("120005", va.getBjlx()));
					sb.append("','bjxh':'").append(va.getBjxh())
							.append("','bkxh':'").append(va.getBkxh());
					sb.append("','hphm':'")
							.append(URLEncoder.encode(va.getHphm(), "utf-8"))
							.append("','hpys':'");
					sb.append(getCodeValue("031001", va.getHpys()))
							.append("','hpzlmc':'")
							.append(getCodeValue("030107", va.getHpzl()));
					sb.append("','hpzl':'").append(va.getHpzl());
					sb.append("','bjTotal':'").append(bjTotal);
					sb.append("','bkjb':'")
							.append(getCodeValue("120020", va.getBkjb()))
							.append("','sbmc':''");
					sb.append(",'bkjg':'").append(
							URLEncoder.encode(this.departmentManager
									.getDepartmentName(va.getBkjg()), "utf-8"));
					sb.append("','clsd':'").append(va.getClsd())
							.append("','cdbh':'").append(va.getCdbh());
					sb.append("','tp1':'")
							.append(URLEncoder.encode(PicWSInvokeTool.getValue(tpJsonObject,"tp1",va.getTp1()), "utf-8"))
							.append("','tp2':'")
							.append(URLEncoder.encode(PicWSInvokeTool.getValue(tpJsonObject,"tp2",va.getTp2()), "utf-8"));
					sb.append("','tp3':'")
							.append(URLEncoder.encode(PicWSInvokeTool.getValue(tpJsonObject,"tp3",va.getTp3()), "utf-8"))
							.append("','bjdwdm':'").append(va.getBjdwdm())
							.append("','qrzt':'").append(va.getQrzt())
							.append("'}");
				}
			}
		} catch (Exception e) {
			sb.delete(0, sb.length());
			sb.append("{}");
		}
		return sb;
	}

	/**
	 * 多卡口实时报警信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getMultiRealAlarmList(HttpServletRequest request,
			HttpServletResponse response) {
		StringBuffer sb = new StringBuffer();
		String jkds = request.getParameter("kds");
		String[] kds = jkds.split(",");
		String bklbs = request.getParameter("bklbs");
		String[] bklb = bklbs.split(",");
		String city = request.getParameter("city");
		try {
			if (city != null && !"".equals(city)) {
				String url = getRequestUrl(request,city);
				System.out.println("多卡口实时报警监测："+url);
				/**Requesting Remote Service and Filtering Double Quotes of Result*/
				sb.append(requestRemote(url, null).replace("\"", ""));
			} else {
				List<VehAlarmrec> realAlarmList = new LinkedList<VehAlarmrec>();
				realAlarmList = this.realalarmManager
						.getVehAlarmList(kds, bklb);
				if ((realAlarmList != null) && (realAlarmList.size() > 0)) {
					sb.append("[");
					for (int i = 0; i < realAlarmList.size(); i++) {
						VehAlarmrec va = realAlarmList.get(i);
						// 处理长沙卡口图片
						JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(va,va.getKdbh());
						String fxmc;
						try {
							CodeGateExtend direct = this.gateManager
									.getDirect(va.getKdbh()+va.getFxbh());
							if (direct == null)
								fxmc = "无备案信息方向：" + va.getFxbh();
							else
								fxmc = URLEncoder.encode(direct.getFxmc(),
										"UTF-8");
						} catch (Exception e) {
							fxmc = "错误的方向：" + va.getFxbh();
						}
						String kdmc;
						try {
							CodeGate gate = this.gateManager.getGate(va
									.getKdbh());
							if (gate == null)
								kdmc = va.getKdbh();
							else
								kdmc = URLEncoder.encode(gate.getKdmc(),
										"UTF-8");
						} catch (Exception e) {
							kdmc = va.getKdbh();
						}
						sb.append("{'hphm':'")
								.append(URLEncoder.encode(va.getHphm(), "UTF-8"))
								.append("','kdbh':'").append(va.getKdbh())
								.append("','bjsj':'").append(va.getBjsj())
								.append("','kdmc':'");
						sb.append(kdmc).append("','fxmc':'").append(fxmc)
								.append("','bjxh':'").append(va.getBjxh())
								.append("','bjlxmc':'")
								.append(getCodeValue("120005", va.getBjlx()));
						sb.append("','tp1':'")
								.append(PicWSInvokeTool.getValue(tpJsonObject,"tp1",va.getTp1()).replaceAll("\\\\", "/"))
								.append("','tp2':'")
								.append(PicWSInvokeTool.getValue(tpJsonObject,"tp2",va.getTp2()).replaceAll("\\\\", "/"))
								.append("','tp3':'")
								.append(PicWSInvokeTool.getValue(tpJsonObject,"tp3",va.getTp3()).replaceAll("\\\\", "/"))
								.append("'},");
					}
					if (sb.length() == 1)
						sb.append(']');
					else
						sb.setCharAt(sb.length() - 1, ']');
				} else {
					sb.append("[]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 根据号牌类型关联号牌颜色
	 * 
	 * @author oy
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getColorByCartype(HttpServletRequest request) {
		String _type = request.getParameter("platetype");
		StringBuffer sb = new StringBuffer(10);
		if (_type != null && !"".equals(_type)) {
			String result = VehCardType.getColorByCartype(_type);
			sb.append("{'code':'");
			sb.append(result);
			sb.append("'}");
			return sb.toString();
		}
		return null;
	}

	
	@ResponseBody
	@RequestMapping
	public Object queryTopMenus(HttpServletRequest request) {
		List<Menu> topMenu = null;
		try {
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			SysUser user = userSession.getSysuser();
			topMenu = menuManager.findTopMenuList(user.getRoles());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return topMenu;
	}	
	
	
	/**
	 * 是否拦载与未拦截到原因列表
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getSfljandWljdyyList(HttpServletRequest request) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			List<Code> sfljList = systemManager.getCodes("130032");
			List<Code> wljdyyList = systemManager.getCodes("130003");
			map.put("sfljList", sfljList);
			map.put("wljdyyList", wljdyyList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}

	/**
	 * 查询某卡口今日过车总数
	 * 
	 * @author oy
	 * @param request
	 * @return
	 */
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping public Object getVehpassByKdbh(HttpServletRequest
	 * request) { String _kdbh = request.getParameter("kdbh"); StringBuffer sb =
	 * new StringBuffer(10); if(_kdbh != null && !"".equals(_kdbh)){ long result
	 * = realpassManager.getVehpassByKdbh(_kdbh); sb.append("{'total':'");
	 * sb.append(result); sb.append("'}"); return sb.toString(); } return null;
	 * }
	 */

	private String getCodeValue(String dmlb, String dmz) {
		try {
			if ((dmlb == null) || (dmlb.length() < 1) || (dmz == null)
					|| (dmz.length() < 1)) {
				return dmz;
			}
			String dmmc = this.systemManager.getCodeValue(dmlb, dmz);
			return URLEncoder.encode(dmmc, "UTF-8");
		} catch (Exception ex) {
		}
		return dmz;
	}

	@ResponseBody
	@RequestMapping
	public void testPing(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		try {
			PrintWriter printwriter = response.getWriter();
			String ip = request.getParameter("ip");
			if ((ip == null) || (ip.length() < 1)) {
				printwriter.write("");
			} else {
				String r = Ping.test(ip,
						this.systemManager.getParameter("systemtype", request));
				printwriter.write(Common.getPingResult(r));
			}
		} catch (Exception e) {
			try {
				PrintWriter printwriter = response.getWriter();
				printwriter.write(e.getLocalizedMessage());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	@ResponseBody
	@RequestMapping
	public SysUser getJjrdh(HttpServletRequest request){
		SysUser jjrdh=null;
		try {
		String jjr = request.getParameter("jjr");
		 jjrdh = this.bkSignManager.getJjrdh(jjr);		
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return jjrdh;
	}
	
	@ResponseBody
	@RequestMapping
	public Map checkYzmCode(HttpServletRequest request){
		HttpSession session = request.getSession();
		String codes = (String) session.getAttribute("codes");
		String yzm = request.getParameter("yzm");
		Map map = new HashMap<String,String>();
		map.put("code", "0");
		if ((codes == null) || (codes.length() != 4)) {
			map.put("code", "1");
			return map;
		}
		if (!codes.equals(yzm)) {
			map.put("code", "2");
			return map;
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping
	public List<CodeUrl> getQsUrl(HttpServletRequest request){
		List<CodeUrl> list = new ArrayList<CodeUrl>();
		try {
			list = this.urlManager.getCodeUrls();
			for(int i =0;i<list.size();i++){
				String urlstr = "http://"+list.get(i).getUrl()+":"+list.get(i).getPort()+"/"+list.get(i).getContext()+"/index.jsp";
				list.get(i).setBz(urlstr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			return list;
	}
	
	/**
	 * 通过文件名下载项目download下的文件
	 * @param response
	 * @param filename
	 * @param request
	 */
	@ResponseBody
	@RequestMapping
	public void getDownModel(HttpServletResponse response,String filename,HttpServletRequest request){
		try {
			FileInputStream	 fis=new FileInputStream(new File(request.getSession().getServletContext().getRealPath("/")+"/download/"+filename+""));
			byte[] b=new byte[fis.available()];
			fis.read(b);
			fis.close();
			response.setContentType("application;charset=UTF-8");   			
			response.setHeader("Content-Disposition", "attachment;Filename="+new String(filename.getBytes("utf-8"), "ISO_8859_1"));
			OutputStream os = response.getOutputStream();
			os.write(b, 0, b.length);  
	        os.flush(); 
	        os.close(); 
		}
		catch(Exception e){
			e.toString();
		}
	}
	
	/**
	 * 根据dmlb获取code
	 * 
	 * @author licheng
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public List getCodeByDmlb(HttpServletRequest request, HttpServletResponse response) {
		List<Code> list = null;
		try {
			String dmlb = request.getParameter("dmlb");
			String dmsm3 = " order by dmsm3 ";
			list = this.systemManager.getCodeByDmlb(dmlb,dmsm3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * 跳转文档上传
	 */
	@RequestMapping
	public ModelAndView findwardUploadWd(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = null;
		String divId = request.getParameter("divId");
		String lx = request.getParameter("lx");
		String ftpTabType = request.getParameter("ftpTabType");
		try {
			request.setAttribute("divId", divId);
			request.setAttribute("lx", lx);
			request.setAttribute("ftpTabType", ftpTabType);
			mv = new ModelAndView();
			mv.setViewName("manager/uploadWd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  mv;
	}
	
	/*
	 * 跳转培训类文档上传
	 */
	@RequestMapping
	public ModelAndView findwardUploadTrain(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = null;
		String divId = request.getParameter("divId");
		String lx = request.getParameter("lx");
		String ftpTabType = request.getParameter("ftpTabType");
		try {
			request.setAttribute("divId", divId);
			request.setAttribute("lx", lx);
			request.setAttribute("ftpTabType", ftpTabType);
			mv = new ModelAndView();
			mv.setViewName("manager/uploadWdtrain");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  mv;
	}
	
	/*
	 * ftp上传，按照文件的类型上传到ftp指定的文件夹中
	 */
	@RequestMapping
	@ResponseBody
	public int ftpUpload(HttpServletRequest request, HttpServletResponse response){
		boolean ftpUploadFlag  = false;
		int message = 0;
		String path = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try{
		InputStream input = null;
		String filename = "";
		int lx = Integer.parseInt(request.getParameter("wdlx"));
		switch(lx){
			case 0 :
				path = "/软件下载类";
				break;
			case 1 :
				path = "/系统文档类";
				break;
			case 2 :
				path = "/考核通报类";
				break;
			case 3 :
				path = "/视频答疑类";//视频类
				break;
			case 4 :
				path = "/操作答疑类";//操作类
				break;
			case 5 :
				path = "/问题答疑类";//问题类
				break;
		}
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile excelFile = mrequest.getFile("exfilename");
		if (excelFile != null && !excelFile.isEmpty()) {
			input = excelFile.getInputStream();
			
			filename = excelFile.getOriginalFilename();
		}
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		String yhmc = userSession.getSysuser().getYhmc();
		String saveFileName = yhmc+"-"+sdf.format(new Date())+"-"+filename;
		ftpUploadFlag = FtpUtil.uploadFile(path, saveFileName, input);
			if(ftpUploadFlag){
				message = 1;
			}else{
				message = 0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return message;
	}
	
	/*
	 * ftp上传，按照文件的类型上传到ftp指定的文件夹中
	 */
	@RequestMapping
	@ResponseBody
	public void ftpDownload(HttpServletRequest request, HttpServletResponse response){
		String path = "";
		String localpath = "";
		try{	
		String filename = java.net.URLDecoder.decode(request.getParameter("wdname"),"utf-8");
		String yhmc = java.net.URLDecoder.decode(request.getParameter("yhmc"),"utf-8");
		String cjsj = request.getParameter("cjsj").replaceAll("-", "").replaceAll(":", "");
		String saveFileName = yhmc+"-"+cjsj.substring(0, 8)+cjsj.substring(9, cjsj.length())+"-"+filename;
		localpath = request.getSession().getServletContext().getRealPath("/")+"/download/";
		int lx = Integer.parseInt(request.getParameter("wdlx"));
		switch(lx){
			case 0 :
				path = "/软件下载类";
				break;
			case 1 :
				path = "/系统文档类";
				break;
			case 2 :
				path = "/考核通报类";
				break;
			case 3 :
				path = "/视频答疑类";//视频类
				break;
			case 4 :
				path = "/操作答疑类";//操作类
				break;
			case 5 :
				path = "/问题答疑类";//问题类
				break;
			}
			boolean flag = FtpUtil.downFile(path, saveFileName,localpath);
			File file = new File(request.getSession().getServletContext().getRealPath("/")+"/download/"+saveFileName+"");
			if(flag){
			FileInputStream	 fis=new FileInputStream(file);
			byte[] b=new byte[fis.available()];
			fis.read(b);
			fis.close();
			response.setContentType("application;charset=UTF-8");   			
			response.setHeader("Content-Disposition", "attachment;Filename="+new String(filename.getBytes(System.getProperty("file.encoding")), "iso-8859-1"));
			OutputStream os = response.getOutputStream();
			os.write(b, 0, b.length);  
	        os.flush(); 
	        os.close(); 
	        file.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 思路整理：
	 * 1.获取资料下载三个类型的所有数据
	 * 2.写三个方法，在action中合成一个list返回
	 * 3.页面展示，控制，显示数量，显示当前最新的5个
	 * 4.点击更多的时候，还是按原来的方式进行请求
	 * 5.拓展-可以试着将上传按钮放到显示的主tab里面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> ftpFileListForTab(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> result  = new HashMap<String, Object>();
		String path1 = "";
		String path2 = "";
		String path3 = "";
		String tabType = request.getParameter("tabType");//0.资料下载 1.培训答疑
		try{
			if(StringUtils.isBlank(tabType)){
				tabType = "0";//默认为0
			}
			if(tabType.equals("0")){
				path1 = "软件下载类";
				path2 = "系统文档类";
				path3 = "考核通报类";
			}else if(tabType.equals("1")){
				path1 = "视频答疑类";
				path2 = "操作答疑类";
				path3 = "问题答疑类";
			}
			
			List list1 = new ArrayList();
			List list2 = new ArrayList();
			List list3 = new ArrayList();			
		    list1 = FtpUtil.listFtpWd("/"+path1);
			list2 = FtpUtil.listFtpWd("/"+path2);
			list3 = FtpUtil.listFtpWd("/"+path3);
			
			if(list1 == null || list1.size() <= 0){
				result.put("list1", null);
			}else{
				list1=ftpListDesc(list1);
				result.put("list1", list1);
			} 
			if(list2 == null || list2.size() <= 0){
				result.put("list2", null);
			}else{
				list2=ftpListDesc(list2);
				result.put("list2", list2);
			}
			if(list3 == null || list3.size() <= 0){
				result.put("list3", null);
			}else{
				list3=ftpListDesc(list3);
				result.put("list3", list3);
			}			
			
		}catch(Exception e){
			log.debug("首页-资料下载-培训答疑-获取ftp数据出错.");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 *  FTP-主页-资料下载、培训答疑-专用
	 * @param list 
	 * @param titleName 页面title显示的名称
	 * @return
	 */
	public List<Map<String,Object>> ftpListDesc(List list){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {		
			if(list.size()>0){
				/***********
				 * 修改：资料卡的顺序没有达到逆序排的效果huyaj 2017/7/13
				 */
				//Collections.reverse(list);
				for(int i=0;i<list.size();i++){
					 Map<String,Object> map = new HashMap<String,Object>();
					 String wj[] = list.get(i).toString().split("-");
					 map.put("yhmc", wj[0]);
					 String cjsj = wj[1].substring(0, 4)+"-"+wj[1].substring(4,6)+"-"+wj[1].substring(6,8)
					 			   +" "+wj[1].substring(8,10)+":"+wj[1].substring(10,12) +":"+wj[1].substring(12,wj[1].length());
					 map.put("cjsj",cjsj);
					 map.put("filename", wj[2]);
					 resultList.add(map);
					 if(i>3){
						 break;//只显示最新的5条
					 }
				 }
				
				/**按时间逆序排序**/
				 for(int n =1;n<resultList.size();n++){
					 for(int m =0;m<resultList.size();m++){
						 Date a = sdf.parse(resultList.get(m).get("cjsj").toString());
						 Date b = sdf.parse(resultList.get(n).get("cjsj").toString());
						 if(a.before(b)){//b的时间大
							 Map<String,Object> copywjmap = resultList.get(n);
							 resultList.set(n, resultList.get(m));
							 resultList.set(m, copywjmap);
						 }
					 }
				 }
			 }
		} catch (Exception e) {
			log.debug("首页-资料下载-培训答疑-数据遍历出错.");
			e.printStackTrace();
		}
		return resultList;
	}
	
	/*
	 * 遍历ftp指定类型文件夹中文件
	 */
	@RequestMapping
	@ResponseBody
	public List listFtpWd(HttpServletRequest request, HttpServletResponse response){
		List wjlist  = new ArrayList();	
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String path = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{

		int lx = Integer.parseInt(request.getParameter("wdlx"));
		switch(lx){
			case 0 :
				path = "/软件下载类";
				break;
			case 1 :
				path = "/系统文档类";
				break;
			case 2 :
				path = "/考核通报类";
				break;
			case 3 :
				path = "/视频答疑类";//视频类
				break;
			case 4 :
				path = "/操作答疑类";//操作类
				break;
			case 5 :
				path = "/问题答疑类";//问题类
				break;
		}
		
		 wjlist = FtpUtil.listFtpWd(path);
		 if(wjlist == null) {
			 return null;
		 }
		 if(wjlist.size()>0){
			 for(int i=0;i<wjlist.size();i++){
				 Map<String,Object> map = new HashMap<String,Object>();
				 String wj[] = wjlist.get(i).toString().split("-");
				 map.put("yhmc", wj[0]);
				 String cjsj = wj[1].substring(0, 4)+"-"+wj[1].substring(4,6)+"-"+wj[1].substring(6,8)
				 			   +" "+wj[1].substring(8,10)+":"+wj[1].substring(10,12) +":"+wj[1].substring(12,wj[1].length());
				 map.put("cjsj",cjsj);
				 map.put("filename", wj[2]);
				
				 list.add(map);
			 }
			 
			 /**按时间逆序排序**/
			 for(int n =1;n<list.size();n++){
				 for(int m =0;m<list.size();m++){
					 Date a = sdf.parse(list.get(m).get("cjsj").toString());
					 Date b = sdf.parse(list.get(n).get("cjsj").toString());
					 if(a.before(b)){//b的时间大
						 Map<String,Object> copywjmap = list.get(n);
						 list.set(n, list.get(m));
						 list.set(m, copywjmap);
					 }
				 }
			 }
			 
		 }
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 当前用户待办工作-饼图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object getSuspinfoPieData(HttpServletRequest request,HttpServletResponse response) {
		String result = null;
		try {
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			SysUser user = userSession.getSysuser();
			List<Map<String, Object>> datas = this.suspinfoManager.querySuspinfoCountByBkr(user.getGlbm()); 

			if(datas != null){
				result = this.getSingleDSXML(datas, "", "", "0", "", "", "", 1, 1, 0, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	private String[] colors = {"4fcddc", "fca19e", "4789b9","a2abf6","b0c4de"};
	
	
	/**
	 * data数据格式为:
	 * name value
	 * name value
	 * @param caption       图表主标题
	 * @param subCaption    图表副标题
	 * @param slantLable    是否旋转45,默认为默认为0(False):横向显示
	 * @param suffix        后缀显示名称
	 * @param xAxisName     横向坐标轴(x轴)名称
	 * @param yAxisName     纵向坐标轴(y轴)名称
	 * @param showNames     是否显示横向坐标轴(x轴)标签名称
	 * @param showValues    是否在图表显示对应的数据值，默认为1(True)
	 * @param decimalPrecision   指定小数位的位数，[0-10]    例如：='0' 取整
	 * @param rotateNames        是否旋转显示标签，默认为0(False):横向显示
	 */
	public String getSingleDSXML(List<Map<String, Object>> data,String caption,String subCaption,String slantLable,String suffix,String xAxisName, String yAxisName, int showNames, int showValues, int decimalPrecision, int rotateNames) {
			
			StringBuffer strXml = new StringBuffer();
			strXml.append("<graph baseFont='SunSim' baseFontSize='12' caption='" + caption +
					"' subcaption='" + subCaption + "' yAxisMinValue='51650.1"+ 
					"' yAxisMaxValue='71118.3' xAxisName='" + xAxisName + "' yAxisName='" + yAxisName + 
					"' bgColor='FFFFFF'  hovercapbg='FFECAA' hovercapborder='F0FFF0' formatNumberScale='0' decimalPrecision='" + decimalPrecision + 
					"' showValues='" + showValues + "' numdivelines='5' numVdivlines='0' showNames='" + showNames + "' slantLabels='" + slantLable + "' numberSuffix='" + suffix + 
					"' rotateNames='" + rotateNames + 
					"' rotateYAxisName='0' showAlternateHGridColor='1' >");
			int j = 0;
			for (int i = 0; i < data.size(); i++) {
				Map<String, Object> map = data.get(i);
				String bzw = (String) map.get("bzw");
				String lable = getPieName(bzw); 
				if(lable.equals("")){
					continue;
				}				
				String value = map.get("total").toString();
				if(value.equals("0")){
					continue;
				}
				String color = colors[j];
				strXml.append("<set name='" + lable + "' value='" + value + "' color='" + color + "'  />");
				j++;
			}
			strXml.append("</graph>");
			String str = null;
			if(j>0){
				str = strXml.toString();
			}
		return str;
	}
	
	private String getPieName(String bzw){
		String lable = "";
		if(bzw.equals("0")){
			lable = "布控已申请";
		}else if(bzw.equals("1")){
			lable = "布控已审核";
		}else if(bzw.equals("2")){
			lable = "布控已审批";
		}else if(bzw.equals("3")){
			lable = "撤控已审核";
		}else if(bzw.equals("4")){
			lable = "撤控已审批";
		}
		return lable;
	}
	
	/**
	 * 当前用户所在部门本月布/撤控已处理数量统计-柱形图
	 * @param request
	 * @param response
	 * @param yhdh 用户代号 
	 * @param glbm 管理部门
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object getSuspinfoColumnData(HttpServletRequest request,HttpServletResponse response) {
		String result = null;
		try {
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			SysUser user = userSession.getSysuser();
			List<Object> datas = this.suspinfoManager.querySuspinfoCountByBkjg(user.getRoles(),user.getYhdh(),user.getGlbm()); 

			result = this.getMultiDSXML(datas,"", "", "", "", false, "待处理", "数量", 1, 1, 0, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * data形式为
	 * type0 category1，category2，category3...
	 * type1 value1，value2，value3...
	 * type2 value2，value2，value3...
	 * @param data  
	 * @param caption       图表主标题
	 * @param subCaption    图表副标题
	 * @param slantLable    是否旋转45,默认为默认为0(False):横向显示
	 * @param suffix        后缀显示名称
	 * @param xAxisName     横向坐标轴(x轴)名称
	 * @param yAxisName     纵向坐标轴(y轴)名称
	 * @param showNames     是否显示横向坐标轴(x轴)标签名称
	 * @param showValues    是否在图表显示对应的数据值，默认为1(True)
	 * @param decimalPrecision   指定小数位的位数，[0-10]    例如：='0' 取整
	 * @param rotateNames        是否旋转显示标签，默认为0(False):横向显示
	 * @return 
	 */
	public String getMultiDSXML (List<Object> data,String caption,String subCaption,String slantLable,String suffix,boolean maxValue,String xAxisName,String yAxisName,int showNames,int showValues,int decimalPrecision,int rotateNames) {
		StringBuffer strXml = new StringBuffer();
		strXml.append("<graph baseFont='SumSim' baseFontSize='12'  caption='" + caption + 
				"' subcaption = '" + subCaption+ "' yAxisMinValue='' xAxisName = '" + xAxisName + "' yAxisName = '" + yAxisName + 
				"' bgColor='FFFFFF' hovercapbg='FFECAA'" + " hovercapborder='F0FFF0' formatNumberScale='0' decimalPrecision='" + decimalPrecision + 
				"' showValues = '" + showValues + "' numdivlines='5' numVdivlines='0' showNames='" + showNames + 
				"' rotateNames='" + rotateNames + "' slantLabels='" + slantLable + "' numberSuffix='" + suffix + "' rotateYAxisName='0' showAlternateHGridColor='1'>");
		if (maxValue) {
			strXml = new StringBuffer(strXml.substring(0, strXml.length() - 1));
			strXml.append(" yAxisMaxValue='100'>");
		}
		
		strXml.append("<categories>");
		@SuppressWarnings("unchecked")
		List<String> categoryNameList = (List<String>) data.get(0);
		for(String value : categoryNameList){
			strXml.append("<category name='"+value+"'/>");
		}
		strXml.append("</categories>");
		
		String[] colorArry = {"a2abf6","a9eefe"};
		
		@SuppressWarnings("unchecked")
		List<Map<String, List<String>>> dataList = (List<Map<String, List<String>>>) data.get(1);
		for (int i = 0; i < dataList.size(); i++) {		
			Map<String,List<String>> map = (Map<String, List<String>>) dataList.get(i);
			String color = colorArry[i];

			Set<String> keySet = map.keySet();
			String name = keySet.iterator().next();
			strXml.append("<dataset seriesName='" + name + "' color='" + color + "' anchorBorderColor='" + color + "' anchorBgColor='" + color + "'>");
			
			List<String> list = (List<String>) map.get(name);	
			for (int j = 0; j < list.size(); j ++) {				
				String link = getColumnLink(i,j);
				strXml.append("<set value='" + list.get(j) + "' link='"+link+"' />"); 
			}
			strXml.append("</dataset>");	
					
		}
		
		strXml.append("</graph>");
		String str = strXml.toString();
		return str;
	}
	
	/**
	 * 
	 * @param type 第一个循环的下标，先是布控0，后撤控1
	 * @param status 第二个嵌套的循环的下标
	 * @return
	 */
	public String getColumnLink(int type,int status) {
		String link = "";
		if(type==0){
			if(status==0){
				link = "javaScript:showData(3)";
			}else if(status==1){
				link = "javaScript:showData(5)";
			}
		}else if(type==1){
			if(status==0){
				link = "javaScript:showData(10)";
			}else if(status==1){
				link = "javaScript:showData(12)";
			}
		}
		return link;
	}
	
	/**
	 * 查询关注车辆-主页
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryVehCollectList(HttpServletRequest request) {
		List<VehPassrec> result = null;
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			String conSql = " and jh='"+userSession.getSysuser().getJh()+"'";			
			result = this.collectVehManager.getCollectVehForMainPage(conSql);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询当前用户所关注的车辆列表失败");
		}
		return result;
	}
	
	/**
	 * 设置关注车辆状态为已读
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object editCollectVeh(HttpServletRequest request,HttpServletResponse response) {
		try {
			String hphm = URLDecoder.decode(request.getParameter("hphm"),"UTF-8");
			String hpzl = request.getParameter("hpzl");
			String isread = request.getParameter("isread");
			int result = collectVehManager.editCollectVehIsRead(hphm, hpzl, isread);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@RequestMapping()
	@ResponseBody
	public Object queryZgfxForMainPage(HttpServletRequest request,HttpServletResponse response) {
		List<ZgfxBean> list = null;
		try {
			list = this.zgfxManager.queryZgfxForMainPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping()
	@ResponseBody
	public Map<String, Object> queryListForMainPage(HttpServletRequest request,String page, String rows) {
		Map<String, Object> queryMap = null;
		try {
//			String title = URLDecoder.decode(request.getParameter("title"),"UTF-8");
			String title = "";
			if(request.getParameter("title")!=null&&!"".equals(request.getParameter("title").toString())){
				title = URLDecoder.decode(request.getParameter("title"),"UTF-8");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("curPage", page);
			map.put("pageSize", rows);
			queryMap = this.tztbManager.queryTztbForMainPage(map, title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}
	
	@RequestMapping()
	@ResponseBody
	public Map<String, Object> queryVersionForMainPage(HttpServletRequest request,String page, String rows) {
		Map<String, Object> queryMap = null;
		try {
			String name = "";
			if(request.getParameter("name")!=null&&!"".equals(request.getParameter("name").toString())){
				name = URLDecoder.decode(request.getParameter("name"),"UTF-8");
			}
			
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			queryMap = this.versionLogManager.queryVersionForMainPage(filter, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}
	
	/**
	 * 首页-通知通报-战果分析-附件上传（共用）
	 * ftp上传，按照文件的类型上传到ftp指定的文件夹中
	 */
	@RequestMapping
	@ResponseBody
	public Object ftpUploadMainPage(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		boolean ftpUploadFlag  = false;
		int message = 0;
		String path = "";
		int lx = Integer.parseInt(request.getParameter("wdlx"));
		switch(lx){
			case 0 :
				path = "/tztb";
				break;
			case 1 :
				path = "/zgfx";
				break;
			case 10 :
				path = "/系统文档类";
		}
		try{
		InputStream input = null;
		String filename = "";
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile excelFile = mrequest.getFile("exfilename");
		if (excelFile != null && !excelFile.isEmpty()) {
			input = excelFile.getInputStream();
			
			filename = excelFile.getOriginalFilename();
		}
		String saveFileName = filename;
		ftpUploadFlag = FtpUtil.uploadFile(path, saveFileName, input);
			if(ftpUploadFlag){
				message = 1;
				result.put("message", message);
				result.put("filename", filename);
			}else{
				message = 0;
				result.put("message", message);
				result.put("filename", "");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 通知通报-战果分析-嫌疑假牌二次识别 （共用)
	 * ftp下载，按照文件的类型上传到ftp指定的文件夹中
	 */
	@RequestMapping
	@ResponseBody
	public void ftpDownloadMainPage(HttpServletRequest request, HttpServletResponse response){
		//先从存储图片的地方下载，下载完成转换成字节流后，马上上传到指定的FTP文件地址上去
		
		int lx = Integer.parseInt(request.getParameter("wdlx"));
		String path = "";
		switch(lx){
			case 0 :
				path = "/tztb";
				break;
			case 1 :
				path = "/zgfx";
				break;
			case 10 :
				path = "/系统文档类";
		}
		String localpath = "";
		try{
			String filename = java.net.URLDecoder.decode(request.getParameter("wdname"),"utf-8");
			String saveFileName = filename;
			localpath = request.getSession().getServletContext().getRealPath("/")+"/download/";
	
			boolean flag = FtpUtil.downFile(path, saveFileName,localpath);
			File file = new File(request.getSession().getServletContext().getRealPath("/")+"/download/"+saveFileName+"");
			if(flag){
				FileInputStream	 fis=new FileInputStream(file);
				byte[] b=new byte[fis.available()];
				fis.read(b);
				fis.close();
				response.setContentType("application;charset=UTF-8");   			
				response.setHeader("Content-Disposition", "attachment;Filename="+new String(filename.getBytes(System.getProperty("file.encoding")), "iso-8859-1"));
				OutputStream os = response.getOutputStream();
				os.write(b, 0, b.length);  
		        os.flush(); 
		        os.close(); 
		        file.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

