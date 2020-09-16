package com.sunshine.monitor.system.query.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.bean.SuspMonitor;
import com.sunshine.monitor.system.query.service.ReSuspinfoQueryManager;
import com.sunshine.monitor.system.query.service.SuspinfoQueryManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoEditManager;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

@Controller
@RequestMapping(value = "/suspinfoQueryCtrl.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspinfoQueryCtroller {

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private SuspinfoQueryManager suspinfoQueryManager;

	@Autowired
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;

	@Autowired
	private SuspinfoManager suspinfoManager;

	@Autowired
	@Qualifier("suspinfoEditManager")
	private SuspinfoEditManager suspinfoEditManager;

	@Autowired
	@Qualifier("logManager")
	private LogManager logManager;

	@Autowired
	private ReSuspinfoQueryManager rs;
	
	/**
	 * @modify huanghaip
	 * @date 2017-3-23
	 * 查询JM_SUSPINFO_MONITOR表信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object getksinfo(HttpServletRequest request) {
		String bkxh = request.getParameter("bkxh");
		List<Map<String, Object>> list = null;
		if (bkxh != null) {
			try {
				list = rs.query(bkxh);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 跳转布控信息查询主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request) {

		try {
			String end = this.systemManager.getSysDate(null, false) + " 23:59:59";
			List bkdlList = this.systemManager.getCodes("120019");

			List bklbList = this.systemManager.getCodes("120005");
			List list = new LinkedList();
			if (bklbList != null) {
				for (int i = 0; i < bklbList.size(); i++) {
					Code code = (Code) bklbList.get(i);
					if (!code.getDmsm4().equals("3")) {
						list.add(code);
					}
				}
			}
			List hpzlList = this.systemManager.getCodes("030107");

			List bkxzList = this.systemManager.getCodes("120021");

			List yadmList = this.systemManager.getCodes("130022");

			List bkjbList = this.systemManager.getCodes("120020");

			List xxlyList = this.systemManager.getCodes("120012");

			List ywztList = this.systemManager.getCodes("120008");

			List jlztList = this.systemManager.getCodes("120002");

			List bjztList = this.systemManager.getCodes("130009");

			request.setAttribute("end", end);
			request.setAttribute("tip", "query");
			request.setAttribute("bkdlList", bkdlList);
			request.setAttribute("yadmList", yadmList);
			request.setAttribute("bkxzList", bkxzList);
			request.setAttribute("bkjbList", bkjbList);
			request.setAttribute("hpzlList", hpzlList);
			request.setAttribute("bklbList", bklbList);
			request.setAttribute("xxlyList", xxlyList);
			request.setAttribute("ywztList", ywztList);
			request.setAttribute("jlztList", jlztList);
			request.setAttribute("bjztList", bjztList);
			request.setAttribute("ckyyList", systemManager.getCodes("120007"));
			request.setAttribute("citylist", this.systemManager
					.getCodes("105000"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String glbm = userSession.getDepartment().getGlbm();

		if (StringUtils.isNotBlank(glbm) && glbm.indexOf("430000") == 0) {			
			return "query/suspinfostquery";
		} else {
			return "query/suspinfoquery";
			//return "query/suspinfostquery";
		}
	}
	
	/**
	 * 跳转管控信息查询主页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForwardGK(HttpServletRequest request) {

		try {
			List bkdl = this.systemManager.getCodes("120019");
			List bkdlList = new LinkedList();
			for (Object o : bkdl) {
				Code code = (Code) o;
				if (code.getDmz().equals("3")) {
					bkdlList.add(code);
				}
			}

			List bklbList = this.systemManager.getCodes("120005");
			List list = new LinkedList();
			for (int i = 0; i < bklbList.size(); i++) {
				Code code = (Code) bklbList.get(i);
				if (code.getDmsm4().equals("3")) {
					list.add(code);
				}
			}

			List hpzlList = this.systemManager.getCodes("030107");

			List bkxzList = this.systemManager.getCodes("120021");

			List yadmList = this.systemManager.getCodes("130022");

			List bkjbList = this.systemManager.getCodes("120020");

			List xxlyList = this.systemManager.getCodes("120012");

			List ywztList = this.systemManager.getCodes("120008");

			List jlztList = this.systemManager.getCodes("120002");

			List bjztList = this.systemManager.getCodes("130009");

			request.setAttribute("tip", "queryGK");
			request.setAttribute("bkdlList", bkdlList);
			request.setAttribute("yadmList", yadmList);
			request.setAttribute("bkxzList", bkxzList);
			request.setAttribute("bkjbList", bkjbList);
			request.setAttribute("hpzlList", hpzlList);
			request.setAttribute("bklbList", list);
			request.setAttribute("xxlyList", xxlyList);
			request.setAttribute("ywztList", ywztList);
			request.setAttribute("jlztList", jlztList);
			request.setAttribute("bjztList", bjztList);
			request.setAttribute("ckyyList", systemManager.getCodes("120007"));
			request.setAttribute("citylist", this.systemManager.getCodes("105000"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String glbm = userSession.getDepartment().getGlbm();

		if (StringUtils.isNotBlank(glbm) && glbm.indexOf("430000") == 0) {			
			return "query/suspinfostquery";
		} else {
			return "query/suspinfoquery";
		}
	}
	
	/**
	 * 模糊布控信息查询主页
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForwardMH(HttpServletRequest request) {

		try {
			List bkdlList = this.systemManager.getCodesByDmsm("120019", "", 3);

			List bklbList = this.systemManager.getCodes("120005");
			List list = new LinkedList();
			if (bklbList != null) {
				for (int i = 0; i < bklbList.size(); i++) {
					Code code = (Code) bklbList.get(i);
					if (!code.getDmsm4().equals("3")) {
						list.add(code);
					}
				}
			}
			List hpzlList = this.systemManager.getCodes("030107");

			List bkxzList = this.systemManager.getCodes("120021");

			List yadmList = this.systemManager.getCodes("130022");

			List bkjbList = this.systemManager.getCodes("120020");

			List xxlyList = this.systemManager.getCodes("120012");

			List ywztList = this.systemManager.getCodes("120008");

			List jlztList = this.systemManager.getCodes("120002");

			List bjztList = this.systemManager.getCodes("130009");

			request.setAttribute("tip", "queryMH");
			request.setAttribute("bkdlList", bkdlList);
			request.setAttribute("yadmList", yadmList);
			request.setAttribute("bkxzList", bkxzList);
			request.setAttribute("bkjbList", bkjbList);
			request.setAttribute("hpzlList", hpzlList);
			request.setAttribute("bklbList", bklbList);
			request.setAttribute("xxlyList", xxlyList);
			request.setAttribute("ywztList", ywztList);
			request.setAttribute("jlztList", jlztList);
			request.setAttribute("bjztList", bjztList);
			request.setAttribute("ckyyList", systemManager.getCodes("120007"));
			request.setAttribute("citylist", this.systemManager
					.getCodes("105000"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String glbm = userSession.getDepartment().getGlbm();

		if (StringUtils.isNotBlank(glbm) && glbm.indexOf("430000") == 0) {			
			return "query/suspinfostquery";
		} else {
			return "query/suspinfoquery";
			//return "query/suspinfostquery";
		}
	}
	/**
	 * 布/管控信息查询列表
	 * @param page
	 * @param rows
	 * @param request
	 * @param info
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryList(String page, String rows, VehSuspinfo info,
			HttpServletRequest request) {
		Map filter = new HashMap();
		String city = request.getParameter("city");
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		filter.put("city", city);
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String tip = request.getParameter("tip");
		String glbm = userSession.getDepartment().getGlbm();
		filter.put("glbm", glbm);
		Map map = null;
		try {

			if ("query".equals(tip)) {
				// 只查询涉案类和交通违法类

				// String conSql = " and bkdl in('1','2') ";
				//String conSql = "  and bkdl <> '3'  ";
				String conSql = "";
				map = suspinfoQueryManager.getSuspinfoMapForFilter(filter,
						info, conSql);
				List list=(List) map.get("rows");
				for(int i = 0;i<list.size();i++){
					 Map vehmap=(Map) list.get(i);
					 String bksprmc =  this.suspinfoQueryManager.getBksprByBkxh(vehmap.get("bkxh").toString());
					 vehmap.put("BKSPRMC",bksprmc);
				}
			} else if ("queryMH".equals(tip)) {
				if (info.getHphm() != null && !info.getHphm().equals("")) {
					info.setHphm(info.getHphm().replace('?', '_'));
				}
				map = suspinfoQueryManager.getSuspinfoMapForFilter(filter, info, " and mhbkbj = '1'");
				
			} else {
				// UserSession userSession = (UserSession) WebUtils
				// .getSessionAttribute(request, "userSession");
				// 管控类布控只能布控单位能查看到
				map = suspinfoQueryManager.getSuspinfoMapForFilter(filter,
						info, " and bkdl = '3'  and  bkjg = '" + glbm + "'  ");
			}
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
			StringBuffer sb=new StringBuffer();
			if ("query".equals(tip)) {
				log.setCzlx("5520");
				sb.append("布控信息查询成功，操作条件：");
				
				if (info.getKssj() != null && info.getKssj().length() > 0) {
					sb.append("，开始时间："
							+ info.getKssj().substring(0,10));
				}
				if (info.getJssj() != null && info.getJssj().length() > 0) {
					sb.append("，结束时间："
							+ info.getJssj().substring(0,10));
				}
				if (city != null && city.length() > 0){
					String kkszd = systemManager.getCodeValue("000033", city);
					if(null != kkszd && !"".equals(kkszd)){
						sb.append("，卡口所在地：" + kkszd);
					}else{
						sb.append("，卡口所在地：" + "高速卡口");
					}
					
				}
				if (info.getHphm() != null && info.getHphm().length() > 0) {
					sb.append("，号牌号码："
							+ info.getHphm());
				}
				if (info.getBkxh() != null && info.getBkxh().length() > 0) {
					sb.append("，布控序号："
							+ info.getBkxh());
				}
				if (info.getBkdl() != null && info.getBkdl().length() > 0) {
					sb.append("，布控类型："
							+ systemManager.getCodeValue("120019", info.getBkdl()));
				}
				if (info.getBklb() != null && info.getBklb().length() > 0) {
					sb.append("，布控子类："
							+ systemManager.getCodeValue("120005", info.getBklb()));
				}
				if (info.getHpzl() != null && info.getHpzl().length() > 0) {
					sb.append("，号牌种类："
							+ systemManager.getCodeValue("030107", info.getHpzl()));
				}
				if (info.getBkjb() != null && info.getBkjb().length() > 0) {
					sb.append("，布控级别："
							+ systemManager.getCodeValue("120020", info.getBkjb()));
				}
				if (info.getYwzt() != null && info.getYwzt().length() > 0) {
					sb.append("，业务状态："
							+ systemManager.getCodeValue("120008", info.getYwzt()));
				}
				if (info.getBjya() != null && info.getBjya().length() > 0) {
					sb.append("，报警预案："
							+ systemManager.getCodeValue("130022", info.getBjya()));
				}
				if (info.getXxly() != null && info.getXxly().length() > 0) {
					sb.append("，信息来源："
							+ systemManager.getCodeValue("120012", info.getXxly()));
				}
				if (info.getBjzt() != null && info.getBjzt().length() > 0) {
					sb.append("，报警状态："
							+ systemManager.getCodeValue("130009", info.getBjzt()));
				}
				log.setCznr(sb.toString());
			} else {
				log.setCzlx("5530");  
				sb.append("模糊布控信息查询成功，操作条件：");
				
				if (info.getKssj() != null && info.getKssj().length() > 0) {
					sb.append("，开始时间："
							+ info.getKssj().substring(0,10));
				}
				if (info.getJssj() != null && info.getJssj().length() > 0) {
					sb.append("，结束时间："
							+ info.getJssj().substring(0,10));
				}
				if (city != null && city.length() > 0){
					sb.append("，卡口所在地："
							+ systemManager.getCodeValue("000033", city));
				}
				if (info.getHphm() != null && info.getHphm().length() > 0) {
					sb.append("，号牌号码："
							+ info.getHphm());
				}
				if (info.getBkxh() != null && info.getBkxh().length() > 0) {
					sb.append("，布控序号："
							+ info.getBkxh());
				}
				if (info.getBkdl() != null && info.getBkdl().length() > 0) {
					sb.append("，布控类型："
							+ systemManager.getCodeValue("120019", info.getBkdl()));
				}
				if (info.getBklb() != null && info.getBklb().length() > 0) {
					sb.append("，布控子类："
							+ systemManager.getCodeValue("120005", info.getBklb()));
				}
				if (info.getHpzl() != null && info.getHpzl().length() > 0) {
					sb.append("，号牌种类："
							+ systemManager.getCodeValue("030107", info.getHpzl()));
				}
				if (info.getBkjb() != null && info.getBkjb().length() > 0) {
					sb.append("，布控级别："
							+ systemManager.getCodeValue("120020", info.getBkjb()));
				}
				if (info.getYwzt() != null && info.getYwzt().length() > 0) {
					sb.append("，业务状态："
							+ systemManager.getCodeValue("120008", info.getYwzt()));
				}
				if (info.getBjya() != null && info.getBjya().length() > 0) {
					sb.append("，报警预案："
							+ systemManager.getCodeValue("130022", info.getBjya()));
				}
				if (info.getXxly() != null && info.getXxly().length() > 0) {
					sb.append("，信息来源："
							+ systemManager.getCodeValue("120012", info.getXxly()));
				}
				if (info.getBjzt() != null && info.getBjzt().length() > 0) {
					sb.append("，报警状态："
							+ systemManager.getCodeValue("130009", info.getBjzt()));
				}
				log.setCznr(sb.toString());
			}
			this.logManager.saveLog(log);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	
	
	@RequestMapping()
	@ResponseBody
	public Map getAllDbLink(HttpServletRequest request) {
		Map map = new HashMap();
		int n = 0;
		try {
			String cityname = this.systemManager.getCodeValue("105000", request.getParameter("city"));
			n = this.suspinfoQueryManager.getAllDbLink(cityname);
			map.put("key",n); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	
	/**
	 * 各地市布控信息查询
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryCitySuspList(String page, String rows, VehSuspinfo info,
			HttpServletRequest request) {
		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		Map map = null;
		
		try {
			String cityname = this.systemManager.getCodeValue("105000", request.getParameter("city"));			
			map = this.suspinfoQueryManager.getCitySuspinfoMapForFilter(filter, info, cityname);
			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
			StringBuffer sb=new StringBuffer();
			log.setCzlx("5520");
			sb.append("布控信息查询成功 ,操作条件:");
			if (info.getHphm() != null && info.getHphm().length() > 0) {
				sb.append(" 号牌号码:"
						+ info.getHphm());
			}
			if (info.getKssj() != null && info.getKssj().length() > 0) {
				sb.append(" 开始时间:"
						+ info.getKssj().substring(0,10));
			}
			if (info.getJssj() != null && info.getJssj().length() > 0) {
				sb.append(" 结束时间:"
						+ info.getJssj().substring(0,10));
			}
			log.setCznr(sb.toString());
			this.logManager.saveLog(log);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**根据号牌号码查询该号牌号码相关的所有布控信息和本单位布控的管控信息
	 * 查询布控信息(审核、审批)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryListByHphm(String page, String rows, VehSuspinfo info,
			HttpServletRequest request) {
		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		
		Map map = null;

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		
		String glbm = userSession.getDepartment().getGlbm();
		try {
			map=this.suspinfoQueryManager.getSuspinfoMapForFilerByHphm(filter, info.getHphm(), glbm);
			Log bklog = new Log();
			bklog.setGlbm(userSession.getSysuser().getGlbm());
			bklog.setIp(userSession.getSysuser().getIp());
			bklog.setYhdh(userSession.getSysuser().getYhdh());
			bklog.setCzlx("5520");
			bklog.setCznr("布控信息查询成功，操作条件号牌号码："+info.getHphm());
				
			this.logManager.saveLog(bklog);
			
			Log gklog = new Log();
			gklog.setGlbm(userSession.getSysuser().getGlbm());
			gklog.setIp(userSession.getSysuser().getIp());
			gklog.setYhdh(userSession.getSysuser().getYhdh());
			gklog.setCzlx("5520");
			gklog.setCznr("布控信息查询成功，操作条件:号牌号码："+info.getHphm());
				
			this.logManager.saveLog(gklog);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return map;
	}
	/**
	 * 
	 * 函数功能说明:获取审核审批表记录集合
	 * 修改日期 	2013-6-19
	 * @param aa
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	@RequestMapping
	@ResponseBody
	public List getAuditApproveList(String bkxh) {
		List list = null;

		AuditApprove aa = new AuditApprove();
		aa.setBkxh(bkxh);
		try {
			list = suspinfoAuditApproveManager.getAuditApproves(aa);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 查询布控信息(审核、审批)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @author OUYANG
	 */
	@RequestMapping
	public ModelAndView queryUnlimitDetail(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String bkxh = request.getParameter("bkxh");
			AuditApprove auditApprove = new AuditApprove();
			auditApprove.setBkxh(bkxh);
			VehSuspinfo vs = this.suspinfoManager.getSuspDetail(bkxh);
			if ((vs != null) && (!vs.getBkdl().equals("3"))) {
				request.setAttribute("vs", vs);
				auditApprove.setBzw("1");
				request.setAttribute("auditList", suspinfoAuditApproveManager
						.getAuditApproves(auditApprove));
				auditApprove.setBzw("2");
				request.setAttribute("approveList", suspinfoAuditApproveManager
						.getAuditApproves(auditApprove));
				auditApprove.setBzw("3");
				request.setAttribute("cAuditList", suspinfoAuditApproveManager
						.getAuditApproves(auditApprove));
				auditApprove.setBzw("4");
				request.setAttribute("cApproveList",
						suspinfoAuditApproveManager
								.getAuditApproves(auditApprove));
			} else {
				request.setAttribute("vs", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("query/quesuspdetail");
	}
	/**
	 * 查询veh_suspinfo表信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping()
	@ResponseBody
	public Object getSuspinfoDetail(HttpServletRequest request) {
		String bkxh = request.getParameter("bkxh");
		String city = request.getParameter("city");
		VehSuspinfo sb = null;

		try {
		//if(city == null || city.length()<1 || "430000".equals(city)) {
			sb = this.suspinfoEditManager.getSuspinfoDetailForBkxh(bkxh);
	/*	} else {
			//String cityname = this.systemManager.getCodeValue("105000", city);
			sb = this.suspinfoEditManager.getCitySuspinfoDetailForBkxh(bkxh, city);
		}*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}
	
	/**
	 * @modify huanghaip
	 * @date 2017-3-23
	 * 查询JM_SUSPINFO_MONITOR表信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Object getSuspMonitorList(HttpServletRequest request) {
		String bkxh = request.getParameter("bkxh");
		//获取是否查看联动布控或联动撤控情况列表
		String selected = request.getParameter("selected");
		List<SuspMonitor> list = null;
		//获取该条布控对应的布控范围
		if (bkxh != null) {
			try {
				list = suspinfoQueryManager.getSuspMonitorList(bkxh, selected);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
