package com.sunshine.monitor.system.alarm.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.util.AlarmSignStau;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.ReCodeManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;
@Controller
@RequestMapping(value = "/alarm.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VehAlarmController {

	@Autowired
	private VehAlarmManager alarmManager;

	@Autowired
	private SuspinfoManager suspinfoManager;

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private GateManager gateManager;

	@Autowired
	private ReCodeManager reCodeManager;
	
	private String alarm_sign_list = "alarm/alarmsignlist";
	
	private String alaram_sign = "alarm/alarmsignlist_datagrid";

	private String command_list = "alarm/commandlist";
	
	private String alarm_sign_temp_list = "alarm/alarmsigntemplist";

	private String alarm_handle_confirm = "alarm/alarmhandleconfirmlist";
	
	/**
	  * led大屏展示预警信息独立为一个应用
	  * @return
	  */
	 @RequestMapping
	 @ResponseBody
	 public Map<String,Object> getAlarmHandleList(int curPage,int rows,String gates){
		 Page p = new Page(curPage, rows);
		 Map<String,Object> map = null;
		 try {
			map = this.alarmManager.getAlarmList(gates,p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return map;
	 }
	
	
	/**
	 * 进入预警签收主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardAlarmsignlist(HttpServletRequest request,
			HttpServletResponse response, String begin, String end,String param) {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> map = null;
		try {
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			map = new HashMap<String, Object>();
			// 报警大类
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			// 号牌种类
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			// 报警小类
			List<Code> bklxList = this.systemManager.getCodes("120005");
			// 卡口名称
			List<CodeGate> gateList = this.gateManager.getAllGates();
			
			Calendar cal = Calendar.getInstance();
			long time = System.currentTimeMillis();
			cal.setTimeInMillis(time - 3600000L);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			map.put("kssj",sdf.format(cal.getTime())+":00");
			cal.setTimeInMillis(time + 3600000L);
			map.put("jssj",sdf.format(cal.getTime())+":00");
			map.put("begin", begin);
			map.put("end", end);
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklxList", bklxList);
			mv.addObject("codemap", map);
			mv.addObject("gatelist", gateList);
			mv.setViewName(alarm_sign_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 进入预警签收主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardAlarmsign(HttpServletRequest request,
			HttpServletResponse response, String begin, String end,String param) {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> map = null;
		try {
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			map = new HashMap<String, Object>();
			// 报警大类
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			// 号牌种类
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			// 报警小类
			List<Code> bklxList = this.systemManager.getCodes("120005");
			// 卡口名称
			List<CodeGate> gateList = this.gateManager.getAllGates();
			
			Calendar cal = Calendar.getInstance();
			long time = System.currentTimeMillis();
			cal.setTimeInMillis(time - 3600000L);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			map.put("kssj",sdf.format(cal.getTime())+":00");
			cal.setTimeInMillis(time + 3600000L);
			map.put("jssj",sdf.format(cal.getTime())+":00");
			map.put("begin", begin);
			map.put("end", end);
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklxList", bklxList);
			mv.addObject("codemap", map);
			mv.addObject("gatelist", gateList);
			mv.setViewName(alaram_sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 查询预警签收列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAlarmsignList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			// 未确认
			conditions.put("QRZT", AlarmSignStau.NO_SIGN.getCode());
			// 报警单位
			conditions.put("BJDWDM", userSession.getSysuser().getGlbm());
			result = this.alarmManager.getAlarmsignList(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Datagrid查询预警签收列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAlarmsignListInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			// 未确认
			conditions.put("QRZT", AlarmSignStau.NO_SIGN.getCode());
			// 报警单位
			conditions.put("BJDWDM", userSession.getSysuser().getGlbm());
			result = this.alarmManager.getAlarmsignListInfo(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 预警签收查询(布控、报警信息)
	 * 
	 * @param bkxh
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object showAlarmsignDetail(
			@RequestParam(required = false) String bjxh,
			HttpServletRequest request, HttpServletResponse response) {
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		Map<String, Object> result = new HashMap<String, Object>();
		VehAlarmrec alarm = null;
		VehSuspinfo susp = null;
		//CodeDevice device = null;
		CodeGate gate = null;
		// 报警信息
		try {
			alarm = this.alarmManager.getAlarmsignDetail(bjxh);
			// System.out.println("CSYS:"+alarm.getCsys());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// if ((alarm.getBjdwdm().equals(userSession.getSysuser().getGlbm()))
		// && (AlarmSignStau.NO_SIGN.getCode().equals(alarm.getQrzt()))) {
		if (alarm.getBjdwdm().equals(userSession.getSysuser().getGlbm())) {
			// 布控信息
			try {
				susp = this.suspinfoManager.getSuspDetail(alarm.getBkxh());
			} catch (Exception e) {
				e.printStackTrace();
			}
			alarm.setQrr(userSession.getSysuser().getYhdh());
			alarm.setQrrjh(userSession.getSysuser().getJh());
			alarm.setQrdwdm(userSession.getSysuser().getGlbm());
			// alarm.setQrdwlxdh(userSession.getDepartment().getLxdh1());

			List<Code> qrztList = null;
			// 是否具备拦截
			List<Code> ljtjList = null;
			List<Code> cjwtList = null;
			try {
				qrztList = this.systemManager.getCodesByDmsm("120014", "1", 2);
				ljtjList = this.systemManager.getCodes("130004");

				cjwtList = this.systemManager.getCodes("130007");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				gate = this.gateManager.getGate(alarm.getKdbh());
			} catch (Exception e) {
				e.printStackTrace();
			}

			result.put("susp", susp);
			result.put("qrztList", qrztList);
			result.put("ljtjList", ljtjList);
			result.put("alarm", alarm);
			result.put("cjwtList", cjwtList);
			if (userSession != null) {
				result.put("qrrmc", userSession.getSysuser().getYhmc());
				result.put("qrdwmc", userSession.getDepartment().getBmmc());
			}
			//result.put("device", device);
			result.put("gate", gate);
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				result.put("nowDate", format.format(this.alarmManager
						.getDbNowTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 预警签收
	 * @param vehAlarmrec
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveAlarmsign(@ModelAttribute VehAlarmrec vehAlarmrec,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = null;
		String msg = null;
		try {
			map = new HashMap<String, Object>();
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			Department dep = userSession.getDepartment();
			SysUser user = userSession.getSysuser();
			if ((dep.getSsjz() == null) || ("".equals(dep.getSsjz()))) {
				msg = "当前用户单位所属警种为空，请进入警综系统进行设置！";
				map.put("code", "0");
				map.put("msg", msg);
				return map;
			}
			if ((user.getJh() == null) || ("".equals(user.getJh()))) {
				msg = "当前用户警号为空，请进入警综系统进行设置！";
				map.put("code", "0");
				map.put("msg", msg);
				return map;
			}
			vehAlarmrec.setQrr(userSession.getSysuser().getYhdh());
			vehAlarmrec.setQrrjh(userSession.getSysuser().getJh());
			vehAlarmrec.setQrrmc(userSession.getSysuser().getYhmc());
			vehAlarmrec.setQrdwdm(userSession.getSysuser().getGlbm());
			vehAlarmrec.setSfxdzl("0");
			// ------部门信息修改后，存在用的信息未及时修改, 改进直接从部门里查询-----
			vehAlarmrec.setQrrdwjz(dep.getSsjz());

			map = this.alarmManager.doTransactionalSaveAlarmSign(vehAlarmrec);
		} catch (Exception e) {
			msg = "预警签收失败";
			map.put("code", "0");
			map.put("msg", msg);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 进入下达指令主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardCmdlist(HttpServletRequest request,
			HttpServletResponse response, String sfxdzl,String begin,String end,String param) {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> map = null;
		try {
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate("+1/24", true);
				 begin = this.systemManager.getSysDate("-1/24", true);
			}
			map = new HashMap<String, Object>();
			// 报警大类
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			// 号牌种类
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			// 报警小类
			List<Code> bklxList = this.systemManager.getCodes("120005");
			// 卡口名称
			List<CodeGate> gateList = this.gateManager.getAllGates();
			
			Calendar cal = Calendar.getInstance();
			long time = System.currentTimeMillis();
			cal.setTimeInMillis(time - 3600000L);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			map.put("kssj",sdf.format(cal.getTime())+":00");
			cal.setTimeInMillis(time + 3600000L);
			map.put("jssj",sdf.format(cal.getTime())+":00");
			map.put("begin", begin);
			map.put("end", end);
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklxList", bklxList);
			map.put("dxzl", sfxdzl);
			mv.addObject("codemap", map);
			mv.addObject("gatelist", gateList);
			mv.setViewName(this.command_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 查询下达指令列表
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryCmdList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			// 下达指令
			conditions.put("QRZT", AlarmSignStau.SIGN_VALID.getCode());
			// 报警单位
			conditions.put("BJDWDM", userSession.getSysuser().getGlbm());
			// 具备拦截条件
			conditions.put("JYLJTJ", "1");
			result = this.alarmManager.getAlarmListForCmd(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 下达指令详细页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object showCmdDetail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			String bjxh = request.getParameter("bjxh");
			VehAlarmrec alarm = this.alarmManager.getAlarmsignDetail(bjxh);
			
			CodeGateExtend gate = this.gateManager.getDirect(alarm.getFxbh());
			//CodeDevice device = this.deviceManager.getDevice(alarm.getSbbh());
			// 设备拦截单位(即指令接收单位)
			String ljdw = "";
			String ljdwmc = "";
			
			result = new HashMap<String, Object>();
			result.put("gate", gate);
			if (gate!=null && gate.getLjdw() != null && gate.getLjdw().length() > 0) {

			//if ((device != null) && (device.getLjdw().length() > 2)) {
			//	ljdw = device.getLjdw().substring(1,device.getLjdw().length() - 1);
				//json格式，把两边的大括号去掉
				ljdw = gate.getLjdw().substring(1, gate.getLjdw().length() - 1);
				String[] ljdws = ljdw.split(",");
				String curLjdw = "";
				String bjdls = "'" + alarm.getBjdl() + "':";
				for (int j = 0; j < ljdws.length; j++) {
					// 根据类别取出相应的单位代码
					if (ljdws[j].startsWith(bjdls)) {
						curLjdw = ljdws[j].substring(bjdls.length() + 1,
								ljdws[j].length() - 1);
						break;
					}
				}
				ljdw = curLjdw;
				if ((ljdw != null) && (!"".equals(ljdw))) {
					String[] dws = ljdw.split(";");
					for (int i = 0; i < dws.length; i++) {
						if(i==0) {
							ljdwmc = ljdwmc 
							+ this.systemManager.getDepartmentName(dws[i]);
						} else {
							ljdwmc = ljdwmc + ","
							+ this.systemManager.getDepartmentName(dws[i]);
						}
													
					}
				}
				if (ljdw == null) {
					ljdw = "";
				}
				if (ljdwmc == null) {
					ljdwmc = "";
				}
			}
			/*
			if (gate.getLjdw() != null && gate.getLjdw().length() > 0) {
					String[] ljdws = gate.getLjdw().replaceAll("\\{", "")
							.replaceAll("\\}", "").split("','");
					for (int i = 0; i < ljdws.length; i++) {
						String dw = ljdws[i].replace("'", "").substring(2);
						String[] ljdwdm = dw.split(",");
						String tempDwmc = "";
						for (int j = 0; j < ljdwdm.length; j++) {
							// tempDwmc = tempDwmc + "," +
							// this.systemDao.getDepartmentName(ljdwdm[j]);
							if (j == 0) {
								tempDwmc = this.systemManager.getDepartmentName(ljdwdm[j]);
							} else {
								tempDwmc = tempDwmc
										+ ","
										+ this.systemManager.getDepartmentName(ljdwdm[j]);
							}
						}
						if(dw!=null&&dw.length()>0){
						if (i == 0) {
							ljdw=dw;
							ljdwmc=tempDwmc;
						} else {
							ljdw=ljdw+","+dw;
							ljdwmc=ljdwmc+","+tempDwmc;
						}
						}
					
					}	
				
				}
				*/
			
			VehSuspinfo susp = this.suspinfoManager.getSuspDetail(alarm
					.getBkxh());
			List<Code> zlfsList = this.systemManager.getCodes("130021");
			List<Code> zlmbList = this.systemManager.getCodes("130005");
			List<VehAlarmCmd> historyCmdList = this.alarmManager.getCmdlist(alarm.getBjxh(), true);
			
			List yaList = reCodeManager.getReCodeListForKdbh(alarm.getKdbh());
			
			//result = new HashMap<String, Object>();
			result.put("susp", susp);
			result.put("zlfsList", zlfsList);
			result.put("zlmbList", zlmbList);
			result.put("alarm", alarm);
			result.put("user", userSession.getSysuser());
			result.put("ljdw", ljdw);
			result.put("ljdwmc", ljdwmc);
			result.put("dep", userSession.getDepartment());
			result.put("historyCmdList",historyCmdList);
			result.put("recodelist", yaList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询当前用所以部门的下级机构列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView getCmdScope(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			Department dep = userSession.getDepartment();
			// 下级部门
			List<Department> list = dep.getPrefecture();
			request.setAttribute("deplist", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("alarm/CmdScopeModalDialog");
	}	
	
	/**
	 * 下达指令
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveCmd(HttpServletRequest request,
			HttpServletResponse response, VehAlarmCmd vehAlarmCmd) {
		Map<String, Object> map = null;
		String msg = null;
		try {
			map = new HashMap<String, Object>();
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			Department dep = userSession.getDepartment();
			SysUser user = userSession.getSysuser();
			if ((dep.getSsjz() == null) || ("".equals(dep.getSsjz()))) {
				msg = "当前用户单位所属警种为空，请进入警综系统进行设置！";
				map.put("code", "0");
				map.put("msg", msg);
				return map;
			}
			if ((user.getJh() == null) || ("".equals(user.getJh()))) {
				msg = "当前用户警号为空，请进入警综系统进行设置！";
				map.put("code", "0");
				map.put("msg", msg);
				return map;
			}
			vehAlarmCmd.setZlr(userSession.getSysuser().getYhdh());
			vehAlarmCmd.setZlrmc(userSession.getSysuser().getYhmc());
			vehAlarmCmd.setZlrjh(userSession.getSysuser().getJh());
			vehAlarmCmd.setZldw(userSession.getSysuser().getGlbm());
			vehAlarmCmd.setZldwmc(userSession.getDepartment().getBmmc());
			boolean success = this.alarmManager.doTransactionalSaveCmd(vehAlarmCmd);
			if(success){
				msg = "下达指令成功";
				map.put("code", "1");
				map.put("msg", msg);
			}
		} catch (Exception e) {
			msg = "下达指令失败";
			map.put("code", "0");
			map.put("msg", msg);
			e.printStackTrace();
			return map;
		}
		return map;
	}
	
	/**
	 * 转入签收待修改列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardAlarmsigntemplist(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			// 报警大类
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			// 号牌种类
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			// 报警小类
			List<Code> bklxList = this.systemManager.getCodes("120005");
			// 卡口名称
			List<CodeGate> gateList = this.gateManager.getAllGates();
			
			Calendar cal = Calendar.getInstance();
			long time = System.currentTimeMillis();
			cal.setTimeInMillis(time - 3600000L);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			map.put("kssj",sdf.format(cal.getTime())+":00");
			cal.setTimeInMillis(time + 3600000L);
			map.put("jssj",sdf.format(cal.getTime())+":00");
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklxList", bklxList);
			mv.addObject("codemap", map);
			mv.addObject("gatelist", gateList);
			mv.setViewName(alarm_sign_temp_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 查询签收待修改列表
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAlarmsigntempList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			// 未确认
			//conditions.put("QRZT", AlarmSignStau.NO_SIGN.getCode());
			// 报警单位
			conditions.put("BJDWDM", userSession.getSysuser().getGlbm());
			result = this.alarmManager.getAlarmsigntempList(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping
	public Object updateAlarmsign(@ModelAttribute VehAlarmrec vehAlarmrec,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = null;
		String msg = null;
		try {
			map = new HashMap<String, Object>();
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			Department dep = userSession.getDepartment();
			SysUser user = userSession.getSysuser();
			if ((dep.getSsjz() == null) || ("".equals(dep.getSsjz()))) {
				msg = "当前用户单位所属警种为空，请进入警综系统进行设置！";
				map.put("code", "0");
				map.put("msg", msg);
				return map;
			}
			if ((user.getJh() == null) || ("".equals(user.getJh()))) {
				msg = "当前用户警号为空，请进入警综系统进行设置！";
				map.put("code", "0");
				map.put("msg", msg);
				return map;
			}
			vehAlarmrec.setQrr(userSession.getSysuser().getYhdh());
			vehAlarmrec.setQrrjh(userSession.getSysuser().getJh());
			vehAlarmrec.setQrrmc(userSession.getSysuser().getYhmc());
			vehAlarmrec.setQrdwdm(userSession.getSysuser().getGlbm());
			// ------部门信息修改后，存在用的信息未及时修改, 改进直接从部门里查询-----
			vehAlarmrec.setQrrdwjz(dep.getSsjz());

			map = this.alarmManager.doTransactionalUpdateAlarmSign(vehAlarmrec);
		} catch (Exception e) {
			msg = "预警签收修改失败";
			map.put("code", "0");
			map.put("msg", msg);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 跳转指挥中心反馈签收
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardAlarmhandlesure(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Map<String, List<Code>> map = null;
		try {
			map = new HashMap<String, List<Code>>();
			// 报警大类
			List<Code> bkdlList = this.systemManager.getCodes("120019");
			// 号牌种类
			List<Code> hpzlList = this.systemManager.getCodes("030107");
			// 报警小类
			List<Code> bklxList = this.systemManager.getCodes("120005");
			// 卡口名称
			List<CodeGate> gateList = this.gateManager.getAllGates();
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklxList", bklxList);
			mv.addObject("codemap", map);
			mv.addObject("gatelist", gateList);
			mv.setViewName(alarm_handle_confirm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}	
	
	/**
	 * 指挥中心出警反馈确认列表
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryAlarmhandleConfirm(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			//conditions.put("SFFK", "1");
			//conditions.put("SFLJ", "1");
			// 报警单位
			conditions.put("BJDWDM", userSession.getSysuser().getGlbm());
			result = this.alarmManager.queryAlarmhandleConfirmlist(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 指挥中心确认详细信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object showHandleConfirmDetail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try { 
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			String bjxh = request.getParameter("bjxh");
			String fkbh = request.getParameter("fkbh");
			VehAlarmrec alarm = this.alarmManager.getAlarmsignDetail(bjxh);
			VehSuspinfo susp = this.suspinfoManager.getSuspDetail(alarm.getBkxh());
			List<VehAlarmCmd> historyCmdList = this.alarmManager.getCmdlist(alarm.getBjxh(), true);
			List<VehAlarmLivetrace> livetraceList = this.alarmManager.getLivetraceList(bjxh);
			List<VehAlarmHandled> handled = this.alarmManager.queryAlarmHandleList(bjxh);
			result = new HashMap<String, Object>();
			result.put("susp", susp);
			result.put("alarm", alarm);
			result.put("historyCmdList",historyCmdList);
			result.put("livetraceList", livetraceList);
			result.put("handled", handled);
			result.put("user", userSession.getSysuser());
			result.put("dep", userSession.getDepartment());
			result.put("fkbh", fkbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 保存反馈签收
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveHandledConfirm(HttpServletRequest request,VehAlarmHandled vehAlarmHandled) {
		Map<String, String> result = null;
		Map<String, String> dxcondition = null;
		try {
			result = new HashMap<String,String>();
			dxcondition = new HashMap<String,String>();
			int success = this.alarmManager.saveHandledConfirm(vehAlarmHandled);
			if(success == 1){
				result.put("code", "1");
				
					if("1".equals(vehAlarmHandled.getBy1())){	
					//BKSign dqzbrsj = (BKSign)this.bkSignManager.getDqzbrsj();
					//if(dqzbrsj!=null){
					SmsFacade smsFacade = new SmsFacade();
					VehAlarmrec alarm = this.alarmManager.getAlarmsignDetail(vehAlarmHandled.getBjxh());
					VehSuspinfo susp = this.suspinfoManager.getSuspDetail(vehAlarmHandled.getBkxh());
					String msg = "在【"+alarm.getBjsj()+"】时间发生的预警类型为【"+alarm.getBjlxmc()+"】，号牌号码为【"+alarm.getHphm()+"】和车辆类型为【"+susp.getHpzlmc()+"】，经过【"+alarm.getKdmc()+"】卡口的车辆已反馈签收，请8小时内务必进行拦截";
					//if(StringUtils.isNotBlank(dqzbrsj.getZbrdh()))
					//	smsTransmitter.send(dqzbrsj.getZbrdh(), msg);
					if(susp!=null){
					if(StringUtils.isNotBlank(susp.getDxjshm()))
						smsFacade.sendAndPostSms(susp.getDxjshm(), msg, null);
					///////////////保存短信数据/////////		
					//String dxjsr =dqzbrsj.getZbrmc();
					//	String dxjshm = dqzbrsj.getZbrdh();
					String xh = vehAlarmHandled.getBjxh();
					String ywlx = "15";			
					//String dxjsr =susp.getBkrmc();
					//String dxjshm = susp.getDxjshm();
					String dxnr = msg;
					dxcondition.put("xh", xh);
					dxcondition.put("ywlx", ywlx);
					dxcondition.put("dxjsr",susp.getBkrmc()==null?"":susp.getBkrmc());
					dxcondition.put("dxjshm",susp.getDxjshm()==null?"":susp.getDxjshm());
					dxcondition.put("dxnr", dxnr);
					int crdxcount = this.alarmManager.saveDxsj(dxcondition);
					if(crdxcount==1){result.put("msg", "反馈签收成功且短信数据已保存");}else{
						result.put("msg", "反馈签收成功但短信数据未保存");
					}
					}
				}
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("msg", "反馈签收失败");
			e.printStackTrace();
		}
		return result;
	}
	
}
