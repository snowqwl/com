package com.sunshine.monitor.system.alarm.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.picws.PicWSInvokeTool;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.RealalarmManager;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UrlManager;

@Controller
@RequestMapping(value = "/realalarm.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RealalarmController {

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private GateManager gateManager;

	@Autowired
	private UrlManager urlManager;
	
	@Autowired
	private RealalarmManager realalarmManager;
	
	@Autowired
	private DepartmentManager departmentManager;
	
	@Autowired
	private SysparaManager sysparaManager;

	/**
	 * 单卡口报警实时监控
	 * Transfer to Single KDBH Alarm main page
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardSingleGateMain(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = null;
		try {
			mv = new ModelAndView();
			List<Code> bklbList = this.systemManager.getCodes("120005");
			//List<Code> bklb1List = this.systemManager.getCodesByDmsm("120019",
			//		"1", 4);
			List<Code> bklb1List = this.systemManager.getCodes("120019");
			request.setAttribute("bklbList", bklbList);
			request.setAttribute("bklb1List", bklb1List);
			/**
			 * 用于扩展省、市州同一版本
			if (this.systemManager.getParameter("xzqh", request).equals(
					"430000000000")) {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				CodeUrl cu = new CodeUrl();
				cu.setJb("3");
				// 全省各地市缉查布控URL
				request.setAttribute("urls", this.urlManager.getUrls(cu));
				request.setAttribute("sign", "true");
			} else {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "false");
			}*/
			mv.setViewName("real/singlegatealarmmain");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/** 
	 * 查询单卡口实时报警
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView queryList(HttpServletRequest request, HttpServletResponse response) {
		try {
			String kd = request.getParameter("kdbh");
			String lb = request.getParameter("bklbs");
			String city = request.getParameter("city");
			/** 
		              当前卡口与管理部门无关联关系
		 	String glbm = "";
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			if(userSession != null){
		     	glbm = userSession.getSysuser().getGlbm();
			}else{
			 	glbm=request.getParameter("glbm");
			}
			request.setAttribute("glbm", glbm);
			**/
			if ((kd == null) || (kd.length() < 1) || (lb == null)
					|| (lb.length() < 1)) {
				throw new IllegalArgumentException("卡口编号或报警类别参数为空！");
			}
			request.setAttribute("kd", kd);
			request.setAttribute("mybjlxs", lb);
			/**
			 * 由City与行政区划判断是否是本地调用,如果是本地调用，city不再传回JSP,
			 * 反之远程调用
			 */
			if(city != null){
				String xzqh = this.sysparaManager.getSyspara(
					"xzqh", "1", null).getCsz();
				if(!city.equals(xzqh))
					request.setAttribute("city", city.substring(0,4)+"00000000");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("real/realalarmdetail");
	}
	
	/**
	 * 多卡口报警实时监控
	 * Transfer to Multiple KDBH Alarms main page
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardMultiGateMain(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = null;
		try {
			mv = new ModelAndView();
			List<Code> bklbList = this.systemManager.getCodes("120005");
			//List<Code> bklb1List = this.systemManager.getCodesByDmsm("120019",
			//		"1", 4);
			List<Code> bklb1List = this.systemManager.getCodes("120019");
			request.setAttribute("bklbList", bklbList);
			request.setAttribute("bklb1List", bklb1List);
			/**
			request.setAttribute("flag", "list");
			if (this.systemManager.getParameter("xzqh", request).equals(
					"430000000000")) {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				CodeUrl cu = new CodeUrl();
				cu.setJb("3");
				// 全省各地市缉查布控URL
				request.setAttribute("urls", this.urlManager.getUrls(cu));
				request.setAttribute("sign", "true");
			} else {
				request.setAttribute("departmentList", this.gateManager
						.getDepartmentsByKdbh());
				request.setAttribute("sign", "false");
			}
			**/
			mv.setViewName("real/multigatealarmmain");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 进入多卡口实时报警信息
	 * @param request
	 * @param response 
	 * @return
	 */
	@RequestMapping
	public ModelAndView queryRealMultiList(HttpServletRequest request, HttpServletResponse response) {
		try {
			String mykds = request.getParameter("jkdss");
			String mybjlxs = request.getParameter("bklbs");
			String city = request.getParameter("city");
			if ((mykds == null) || (mykds.length() < 1) || (mybjlxs == null)
					|| (mybjlxs.length() < 1)) {
				throw new IllegalArgumentException("卡口编号或报警类别参数为空！");
			} else {
				request.setAttribute("mykds", mykds);
				request.setAttribute("mybjlxs", mybjlxs);
				/**
				 * 由City与行政区划判断是否是本地调用,如果是本地调用，city不再传回JSP,
				 * 反之远程调用
				 */
				if(city != null){
					String xzqh = this.sysparaManager.getSyspara(
						"xzqh", "1", null).getCsz();
					if(!city.equals(xzqh))
						request.setAttribute("city", city.substring(0,4)+"00000000");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("real/multirealalarmlist");
	}
	/**
	 * 多卡口报警实时监控详细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardRealMultiDetail(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("real/multirealalarmdetail");
		try {
			String bjxh = request.getParameter("bjxh");
			mv.addObject("bjxh", bjxh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}	
	
	/**
	 * 多卡口实时报警详细
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object realView(HttpServletRequest request, HttpServletResponse response) {
		VehAlarmrec va = null ;
		try {
			String bjxh = request.getParameter("xh");
			// 报警表信息(veh_alarmrec)
			va = this.realalarmManager.getVehAlarmDetail(bjxh);
			if (va != null) {
				// 处理长沙卡口图片
				JSONObject tpJsonObject = PicWSInvokeTool.getPicJsonObject(va,va.getKdbh());
				va.setTp1(PicWSInvokeTool.getValue(tpJsonObject,"tp1",va.getTp1()));
				va.setTp2(PicWSInvokeTool.getValue(tpJsonObject,"tp2",va.getTp2()));
				va.setTp3(PicWSInvokeTool.getValue(tpJsonObject,"tp3",va.getTp3()));
				va.setHpzlmc(this.systemManager.getCodeValue("030107", va.getHpzl()));
				va.setBjlx(this.systemManager.getCodeValue("120005", va.getBjlx()));
				va.setHpys(this.systemManager.getCodeValue("031001", va.getHpys()));
				va.setCsys(this.systemManager.getCodeValue("030108", va.getCsys()));
				va.setCllx(this.systemManager.getCodeValue("030104", va.getCllx()));
				va.setKdmc(this.gateManager.getGateName(va.getKdbh()));
				va.setFxmc(this.gateManager.getDirectName(va.getFxbh()));
				va.setSbmc("");
				va.setBkjb(this.systemManager.getCodeValue("120020", va.getBkjb()));
				va.setBkjg(this.departmentManager.getDepartmentName(va.getBkjg()));
				long bjTotal = this.realalarmManager.getRealAlarmByKdbh(va.getKdbh());
				va.setBy5(Long.valueOf(bjTotal).toString());
				UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
				if ((userSession.getSysuser().getGlbm().equals(va.getBjdwdm()))
						&& (va.getQrzt().equals("0")))
					//request.setAttribute("alarmShow", "block");
					va.setBy1("block");
				else {
					va.setBy1("none");
					request.setAttribute("alarmShow", "none");
				}
				//System.out.println(" 00000000000000000 " + va.getCdbh());
			}
			//request.setAttribute("vehalarmrec", va);
			//request.setAttribute("control", request.getParameter("c"));
			va.setBy2(request.getParameter("c"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return va;
	}	
}
