package com.sunshine.monitor.system.susp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoCancelApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

@Controller
@RequestMapping(value = "/suspinfoCancelCtrl.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspInfoCancelCtroller {

	@Autowired
	@Qualifier("suspinfoAuditApproveManager")
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@Autowired
	@Qualifier("suspinfoEditDao")
	private SuspinfoEditDao suspinfoEditDao;

	@Autowired
	@Qualifier("vehAlarmHandleManager")
	private VehAlarmHandleManager vehAlarmHandleManager;

	@Autowired
	private SuspinfoManager suspinfoManager;
	
	@Autowired
	@Qualifier("logManager")
	private LogManager logManager;
	
	@Autowired
	@Qualifier("suspinfoCancelApproveManager")
	private SuspinfoCancelApproveManager suspinfoCancelApproveManager;
	
	/**
	 * 跳转撤控申请页面
	 * @param request
	 * @param begin
	 * @param end
	 * @param bjzt
	 * @param param
	 * @param state
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request, String begin,
			String end, String bjzt, String param, String state) {

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();

		request.setAttribute("yhmc", user.getYhmc());
		request.setAttribute("bmmc", dp.getBmmc());

		try {
			if (param != null && param.equals("count")) {
				end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				begin = this.systemManager.getSysDate("-180", false)
						+ " 00:00:00";
			}
			request.setAttribute("ckyyList", systemManager.getCode("120007"));
			request.setAttribute("hpzlList", systemManager.getCode("030107"));
			request.setAttribute("bklbList", systemManager.getCode("120005"));
			request.setAttribute("bjztList", systemManager.getCode("130009"));
			request.setAttribute("bkdlList", systemManager.getCodes("120019"));
			request.setAttribute("begin", begin);
			request.setAttribute("end", end);
			request.setAttribute("bjzt", bjzt);
			request.setAttribute("state", state);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "engineer/cancelsuspinfomain";
	}
	/**
	 * 跳转撤控申请详细页面
	 * @param request
	 * @param bkxh
	 * @return
	 */
	@RequestMapping
	public String forwardWin(HttpServletRequest request, String bkxh) {

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();

		request.setAttribute("yhmc", user.getYhmc());
		request.setAttribute("bmmc", dp.getBmmc());
		request.setAttribute("modul",request.getParameter("modul"));
		try {
			request.setAttribute("ckyyList", systemManager.getCode("120007"));
			request.setAttribute("bkxh", bkxh);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "engineer/cancelsuspwin";
	}
	/**
	 * 跳转一键批量布控详细页面
	 * @param request
	 * @param bkxhs
	 * @return
	 */
	@RequestMapping
	public String forwardYJCKWin(HttpServletRequest request, String bkxhs) {

		try {
			request.setAttribute("ckyyList", systemManager.getCode("120007"));
			request.setAttribute("bkxhs", bkxhs);
			request.setAttribute("modul",request.getParameter("modul"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "engineer/yjcancelsuspwin";
	}
	/**
	 * 撤控申请列表
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @param state
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryList(String page, String rows, VehSuspinfo info,
			HttpServletRequest request, String state) {

		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		Map map = null;
		String modul = request.getParameter("modul");
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			SysUser user = userSession.getSysuser();
			String glbm = user.getGlbm();
			info.setBkr(user.getYhdh());
			if (state != null && state.equals("timeout")) {
				map = suspinfoAuditApproveManager
						.findSuspinfoCancelTimeoutForMap(filter, info, glbm);
			} else {
				map = suspinfoAuditApproveManager.findSuspinfoCancelForMap(
						filter, info, glbm, modul);
				if (request.getParameter("overtime") != null) {
					//审核
					if (request.getParameter("overtime").equals("1")) {
						map = this.suspinfoAuditApproveManager.findSuspinfoCancelOverTimeForMap(
								filter, info, glbm, modul);
					}
				}
				
				Log log = new Log();
				log.setGlbm(userSession.getSysuser().getGlbm());
				log.setIp(userSession.getSysuser().getIp());
				log.setYhdh(userSession.getSysuser().getYhdh());
				StringBuffer sb=new StringBuffer();
				if ("new".equals(modul)) {
					log.setCzlx("2510");
					sb.append("撤控申请查询成功，操作条件：");
					if (info.getHphm() != null && info.getHphm().length() > 0) {
						sb.append("，号牌号码："+ info.getHphm());
					}
					if (info.getKssj() != null && info.getKssj().length() > 0) {
						sb.append("，开始时间："
								+ info.getKssj().substring(0,10));
					}
					if (info.getJssj() != null && info.getJssj().length() > 0) {
						sb.append("，结束时间："
								+ info.getJssj().substring(0,10));
					}
					String hpzl = systemManager.getCodeValue("030107", info.getHpzl());
					if (hpzl != null && hpzl.length() > 0) {
						sb.append("，号牌种类："
								+ hpzl);
					}
					String bkdl = systemManager.getCodeValue("120019", info.getBkdl());
					if (bkdl != null && bkdl.length() > 0) {
						sb.append("，布控类型："
								+ bkdl);
					}
					String bkzl = systemManager.getCodeValue("120005", info.getBklb());
					if (bkzl != null && bkzl.length() > 0) {
						sb.append("，布控子类："
								+ bkzl);
					}
					String bjzt = systemManager.getCodeValue("130009", info.getBjzt());
					if (bjzt != null && bjzt.length() > 0) {
						sb.append("，报警状态："
								+ bjzt);
					}
					log.setCznr(sb.toString());
				} else {
					log.setCzlx("2610");
					sb.append("撤控领导审核查询成功，操作条件：");
					if (info.getHphm() != null && info.getHphm().length() > 0) {
						sb.append("，号牌号码："+ info.getHphm());
					}
					if (info.getKssj() != null && info.getKssj().length() > 0) {
						sb.append("，开始时间："
								+ info.getKssj().substring(0,10));
					}
					if (info.getJssj() != null && info.getJssj().length() > 0) {
						sb.append("，结束时间："
								+ info.getJssj().substring(0,10));
					}
					String hpzl = systemManager.getCodeValue("030107", info.getHpzl());
					if (hpzl != null && hpzl.length() > 0) {
						sb.append("，号牌种类："
								+ hpzl);
					}
					String bkdl = systemManager.getCodeValue("120019", info.getBkdl());
					if (bkdl != null && bkdl.length() > 0) {
						sb.append("，布控类型："
								+ bkdl);
					}
					String bkzl = systemManager.getCodeValue("120005", info.getBklb());
					if (bkzl != null && bkzl.length() > 0) {
						sb.append("，布控子类："
								+ bkzl);
					}
					String bjzt = systemManager.getCodeValue("130009", info.getBjzt());
					if (bjzt != null && bjzt.length() > 0) {
						sb.append("，报警状态："
								+ bjzt);
					}
					log.setCznr(sb.toString());
				}
				this.logManager.saveLog(log);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	}
	/**
	 * 验证布控信息
	 * @param bkxh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object validateSuspInfo(String bkxh) throws Exception {
		Map s = null;

		List handList = vehAlarmHandleManager.getVehAlarmHandleForBkxh(bkxh);
		boolean con = false;
		for (Object o : handList) {
			VehAlarmHandled hand = (VehAlarmHandled) o;
			if ("1".equals(hand.getSflj()))
				con = true;
		}
		if (con) {
			VehSuspinfo susp = suspinfoManager.getSuspInfo(bkxh);
			if (!"1".equals(susp.getBy3())) {
				s = Common.messageBox("该布控车辆已拦截成功，请先对布控信息进行确认后再撤控申请！", "0");
			}
		}

		return s;
	}
	/**
	 * 保存撤控信息
	 * @param request
	 * @param response
	 * @param info
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public void saveCancelSuspInfo(HttpServletRequest request,
			HttpServletResponse response, VehSuspinfo info) throws IOException {
		Map s = null;
		String users=request.getParameter("dxjsr");
		String lxdh=request.getParameter("lxdh");
		if(!StringUtils.isBlank(users)){
			info.setUser(users);
		}
		if(!StringUtils.isBlank(lxdh)){
			info.setLxdh(lxdh);
		}
		Department dp = null;
		boolean isHandle = true;

		if (StringUtils.isBlank(info.getBkxh())) {
			s = Common.messageBox("参数传输异常!", "0");
		} else {
			try {
				UserSession userSession = (UserSession) WebUtils
						.getSessionAttribute(request, "userSession");
				SysUser user = userSession.getSysuser();

				dp = userSession.getDepartment();
				if (dp == null)
					s = Common.messageBox("非法操作!", "0");
				else if (StringUtils.isBlank(dp.getSsjz())) {
					s = Common.messageBox("您操作的用户所在的部门缺少警种!", "0");
				} else {

					if (isHandle) {
						VehSuspinfo susp = suspinfoManager.getSuspInfo(info
								.getBkxh());

						info.setBy1(susp.getBy1());
						//获取布控申请人的单位
						info.setBkjg(susp.getBkjg());
						info.setCxsqr(user.getYhdh());
						info.setCxsqrmc(user.getYhmc());
						info.setCxsqrjh(user.getJh());
						info.setCxsqdw(user.getGlbm());
						info.setCxsqdwmc(dp.getBmmc());
						info.setBkrjh(susp.getBkrjh());
						boolean tip = suspinfoAuditApproveManager
								.saveCancelSuspInfocksq(info, dp.getSsjz());
						if (tip)
							s = Common.messageBox("保存成功！", "1");
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				s = Common.messageBox("程序异常！", "0");
			}
		}

		PrintWriter writer = null;

		try {
			writer = response.getWriter();
			writer.print(JSONObject.fromObject(s));
		} catch (IOException e) {
			throw e;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}

	}

	@RequestMapping
	@ResponseBody
	public Object saveYjck(HttpServletRequest request, String dm, String ms,
			String bkxhs) {

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();

		if (dp == null)
			return Common.messageBox("非法操作!", "0");
		else if (StringUtils.isBlank(dp.getSsjz())) {
			return Common.messageBox("您操作的用户所在的部门缺少警种!", "0");
		}

		if (StringUtils.isBlank(bkxhs) || StringUtils.isBlank(dm)
				|| StringUtils.isBlank(ms)) {
			return Common.messageBox("参数传输异常！", "0");
		}

		try {
			String[] bkxhArray = bkxhs.split(",");
			for (String bkxh : bkxhArray) {
				VehSuspinfo info = suspinfoEditDao
						.getSuspinfoDetailForBkxh(bkxh);
				info.setCkyydm(dm);
				info.setCkyyms(URLDecoder.decode(ms, "utf-8"));

				info.setCxsqr(user.getYhdh());
				info.setCxsqrjh(user.getJh());
				info.setCxsqdw(user.getGlbm());
				info.setCxsqdwmc(dp.getBmmc());

				suspinfoAuditApproveManager.saveCancelSuspInfo(info, dp
						.getSsjz());
			}

			return Common
					.messageBox("布控序号为【" + bkxhs + "】的布控记录一键撤控 申请成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}

	}

	@RequestMapping
	@ResponseBody
	public Object getSuspinfoAlarm(String bkxh) {
		try {
			List list = suspinfoAuditApproveManager.getAlarmList(bkxh);
			if (list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询撤控审核审批
	 * @param bkxh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Object getSuspinfoCAuditHistory(String bkxh) {
		AuditApprove aa = new AuditApprove();
		aa.setBkxh(bkxh);
		aa.setBzw("34"); // 查询撤控审核审批
		try {
			List list = this.suspinfoAuditApproveManager.getAuditApproves(aa);
			if (list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@RequestMapping
	@ResponseBody
	public Object getSuspinfoCAuditHistoryXml(HttpServletRequest request) {
		StringBuffer r = new StringBuffer("");
		String bkxh = request.getParameter("bkxh");
		String modul = request.getParameter("modul");
		AuditApprove aa = new AuditApprove();
		aa.setBkxh(bkxh);
		aa.setBzw("34");// 查询撤控审核审批
		try {
			List list = this.suspinfoAuditApproveManager.getAuditApproves(aa);
			if (list.size() > 0) {
				r.append("<fieldset><legend>审核审批");

				if (!"all".equals(modul)) {
					r.append("不同意");
				}

				r
						.append("意见</legend><table border=\"0\" cellspacing=\"1\" cellpadding=\"0\" width=\"98%\">");
				r
						.append(
								"<tr ><td width=\"11%\">类型</td><td width=\"15%\">审核审批人</td><td width=\"22%\">审核审批机关</td>")
						.append(
								"<td width=\"8%\">结果</td><td width=\"14%\">时间</td><td width=\"30%\">内容</td></tr>");

				for (Object o : list) {
					AuditApprove audit = (AuditApprove) o;

					if ("all".equals(modul)) {

						r
								.append("<tr onMouseOver=\"this.className='over'\" onMouseOut=\"this.className='out'\" style=\"cursor:auto\">");
						r.append("<td>").append(audit.getBzwmc()).append(
								"</td><td>").append(audit.getCzrmc()).append(
								"</td>").append("<td>").append(
								audit.getCzrdwmc()).append("</td><td>").append(
								audit.getCzjgmc()).append("</td><td>").append(
								audit.getCzsj()).append(
								"</td><td align=\"left\">").append(
								audit.getMs()).append("</td></tr>");

					} else {
						if ("0".equals(audit.getCzjg())) {
							r
									.append("<tr onMouseOver=\"this.className='over'\" onMouseOut=\"this.className='out'\" style=\"cursor:auto\">");
							r.append("<td>").append(audit.getBzwmc()).append(
									"</td><td>").append(audit.getCzrmc())
									.append("</td>").append("<td>").append(
											audit.getCzrdwmc()).append(
											"</td><td>").append(
											audit.getCzjgmc()).append(
											"</td><td>")
									.append(audit.getCzsj()).append(
											"</td><td align=\"left\">").append(
											audit.getMs()).append("</td></tr>");

						}

					}

				}

				r.append("</table></fieldset>");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return r.toString();
	}
	
	/**
	 * 跳转一键撤控申请页面
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public String findForwardOneKey(HttpServletRequest request) {	
		try {			
			request.setAttribute("ckyyList", systemManager.getCode("120007"));
			request.setAttribute("hpzlList", systemManager.getCode("030107"));
			request.setAttribute("bklbList", systemManager.getCode("120005"));
			request.setAttribute("bjztList", systemManager.getCode("130009"));
			request.setAttribute("bkdlList", systemManager.getCodes("120019"));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "engineer/csuspbyonekeymain";
	}
	
	/**
	 * 一键撤控列表
	 * @param page
	 * @param rows
	 * @param info
	 * @param request
	 * @param state
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryListByOnekey(String page, String rows, VehSuspinfo info,HttpServletRequest request) {
		Map filter = new HashMap();
		filter.put("curPage", page); // 页数
		filter.put("pageSize", rows); // 每页显示行数
		Map map = null;
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			SysUser user = userSession.getSysuser();
			String glbm = user.getGlbm();
			info.setBkr(user.getYhdh());			
			map = suspinfoAuditApproveManager.findSuspinfoCancelForMap(
					filter, info, glbm, "new");					
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
			StringBuffer sb=new StringBuffer();
			log.setCzlx("9096");
			sb.append("一键撤控查询成功，操作条件：");
			if (info.getHphm() != null && info.getHphm().length() > 0) {
				sb.append("，号牌号码："+ info.getHphm());
			}
			if (info.getKssj() != null && info.getKssj().length() > 0) {
				sb.append("，开始时间："
						+ info.getKssj().substring(0,10));
			}
			if (info.getJssj() != null && info.getJssj().length() > 0) {
				sb.append("，结束时间："
						+ info.getJssj().substring(0,10));
			}
			String hpzl = systemManager.getCodeValue("030107", info.getHpzl());
			if (hpzl != null && hpzl.length() > 0) {
				sb.append("，号牌种类："
						+ hpzl);
			}
			String bkdl = systemManager.getCodeValue("120019", info.getBkdl());
			if (bkdl != null && bkdl.length() > 0) {
				sb.append("，布控类型："
						+ bkdl);
			}
			String bkzl = systemManager.getCodeValue("120005", info.getBklb());
			if (bkzl != null && bkzl.length() > 0) {
				sb.append("，布控子类："
						+ bkzl);
			}
			String bjzt = systemManager.getCodeValue("130009", info.getBjzt());
			if (bjzt != null && bjzt.length() > 0) {
				sb.append("，报警状态："
						+ bjzt);
			}
			log.setCznr(sb.toString());			
			this.logManager.saveLog(log);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object saveYjckByOnekey(HttpServletRequest request, String dm, String ms,
			String bkxhs) {

		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();

		if (dp == null)
			return Common.messageBox("非法操作!", "0");
		else if (StringUtils.isBlank(dp.getSsjz())) {
			return Common.messageBox("您操作的用户所在的部门缺少警种!", "0");
		}

		if (StringUtils.isBlank(bkxhs) || StringUtils.isBlank(dm)
				|| StringUtils.isBlank(ms)) {
			return Common.messageBox("参数传输异常！", "0");
		}

		try {
			String[] bkxhArray = bkxhs.split(",");
			for (String bkxh : bkxhArray) {
				VehSuspinfo info = suspinfoEditDao.getSuspinfoDetailForBkxh(bkxh);
				AuditApprove audit_info = new AuditApprove();
				info.setCkyydm(dm);
				info.setCkyyms(URLDecoder.decode(ms, "utf-8"));
				info.setCxsqr(info.getBkr());
				info.setCxsqrjh(info.getBkrjh());
				info.setCxsqdw(info.getBkjg());
				info.setCxsqdwmc(info.getBkjgmc());
				info.setBy5(user.getYhdh());
				info.setCxsqr(user.getYhdh());
				
				audit_info.setBkxh(info.getBkxh());
				audit_info.setCzr(user.getYhdh());
				audit_info.setCzrdw(user.getGlbm());
				audit_info.setCzrdwmc(dp.getBmmc());
				audit_info.setCzrjh(user.getJh());
				audit_info.setCzrmc(user.getYhmc());
				audit_info.setCzjg("1");
				
				/**更新布控表ywzt='41',进入撤控审核阶段**/
				boolean flag = suspinfoAuditApproveManager.saveCancelSuspInfo(info, dp.getSsjz());
				if(flag){
				/**插入audit_approve表**/
					Map<String,String> auditMap = suspinfoAuditApproveManager.saveCAudit(audit_info);
				if("1".equals(auditMap.get("code"))&&URLDecoder.decode(auditMap.get("message"), "utf-8").indexOf("成功")!=-1&&!"3".equals(info.getBkdl()))
					this.suspinfoCancelApproveManager.saveApprove(audit_info);
				}	
				
			}
			return Common.messageBox("布控序号为【" + bkxhs + "】的布控记录一键撤控 成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}

	}	
}
