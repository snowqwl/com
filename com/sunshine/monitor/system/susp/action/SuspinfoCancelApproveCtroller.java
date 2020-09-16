package com.sunshine.monitor.system.susp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoCancelApproveManager;

@Controller
@RequestMapping(value = "/suspinfoCancelApprove.do", params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspinfoCancelApproveCtroller {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("suspinfoCancelApproveManager")
	private SuspinfoCancelApproveManager suspinfoCancelApproveManager;
	/**
	 * 跳转相应的撤控审批/审核主页
	 * @param response
	 * @param request
	 * @param approveType
	 * @param begin
	 * @param end
	 * @param param
	 * @return
	 */
	@RequestMapping(params="method=index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,String approveType,String begin,String end,String param,String isFromIndex){
		ModelAndView mv = null;
		Map map = new HashMap();
		try {
			List bklbList = null;
			List bkdlList = null;
			if("".equals(isFromIndex) || isFromIndex==null){
				isFromIndex="0";//默认为0，表示号牌号码默认为"湘"字，1表示从首页传过来的，页面不显示"湘"
			}
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			if (approveType != null && approveType.equals("classApprove")) {
			   bklbList = this.systemManager.getCodesByDmsm("120005", "1", 4);
			   
			   mv = new ModelAndView("engineer/cancelapproveclassmain");
			} else if (approveType != null && approveType.equals("trafficApprove")) {
				bklbList = this.systemManager.getCodesByDmsm("120005", "2", 4);
				mv = new ModelAndView("engineer/cancelapprovetrafficmain");
			}else { 
			   bkdlList = this.systemManager.getCode("120019");
			   bklbList = this.systemManager.getCode("120005");
			   mv = new ModelAndView("engineer/cancelapprovemain");
			}
			
			if (param != null && param.equals("overtimecount")) {
				 request.setAttribute("cancelapproveovertime", "1");
				 bkdlList = this.systemManager.getCode("120019");
				 bkdlList.remove(2);
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			
			List hpzlList = this.systemManager.getCode("030107");
			List auditList = this.systemManager.getCode("190002");
			List bklbsList = this.systemManager.getCode("120005");
			map.put("bkdlList", bkdlList);
			map.put("bklbList", bklbList);
			map.put("hpzlList", hpzlList);
			map.put("auditList", auditList);
			map.put("ywzt", "42");
			map.put("begin", begin);
			map.put("end",end);
			map.put("bklbsList", bklbsList);
			mv.addObject("codeMap",map);
			
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			SysUser user = userSession.getSysuser();
			Department dp = userSession.getDepartment(); 
			request.setAttribute("yhmc", user.getYhmc());
			request.setAttribute("bmmc", dp.getBmmc());
			request.setAttribute("isFromIndex", isFromIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	/**
	 * 跳转撤控审批详细页面
	 * @param request
	 * @param bkxh
	 * @return
	 */
	@RequestMapping
	public String forwardWin(HttpServletRequest request, String bkxh){
		try {
			List auditList = this.systemManager.getCode("190002");
			request.setAttribute("auditList", auditList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment(); 
		request.setAttribute("yhmc", user.getYhmc());
		request.setAttribute("bmmc", dp.getBmmc());
		request.setAttribute("bkxh", bkxh);
		
		return "engineer/cancelapprovewin";
	}
	
	/**
	 * 撤控指挥中心审批查询
	 * @param request
	 * @param response
	 * @param rows
	 * @param page
	 * @param command
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params="method=queryList", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList (HttpServletRequest request, HttpServletResponse response,
			String rows, String page, VehSuspinfo command) {
		Map map = null;
		try {
			Map filter = new HashMap();
			VehSuspinfo vehSuspinfo = command;
			filter.put("curPage", page);
			filter.put("pageSize",rows);
			//暂时使用云浮公安机构代码,正常为登录用户的部门代码
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String glbm = userSession.getDepartment().getGlbm();
			
			if (request.getParameter("overtime") != null) {
				if (request.getParameter("overtime").equals("1")) {
					map = this.suspinfoCancelApproveManager.getSuspinfoCancelApprovesOverTime(filter, vehSuspinfo, glbm);
				}
			} else
			map = this.suspinfoCancelApproveManager.getSuspinfoCancelApproves(filter, vehSuspinfo, glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 撤控指挥中心涉案类审批查询
	 * @param request
	 * @param response
	 * @param rows
	 * @param page
	 * @param command
	 * @param bkxh
	 * @return
	 */
	@RequestMapping(params="method=queryListCalssApprove", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryListCalssApprove(HttpServletRequest request, HttpServletResponse response, 
			String rows, String page, VehSuspinfo command){
		Map map = null;
		try {
			Map filter = new HashMap();
			VehSuspinfo vehSuspinfo = command;
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			//暂时使用云浮公安机构代码,正常为登录用户的部门代码
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String glbm = userSession.getDepartment().getGlbm();
			map = this.suspinfoCancelApproveManager.getSuspinfoCancelClassApproves(filter, vehSuspinfo, glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 撤控指挥中心交通类审批查询
	 * @param request
	 * @param response
	 * @param rows
	 * @param page
	 * @param command
	 * @return
	 */
	@RequestMapping(params="method=queryListTrafficApprove", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryListTrafficApprove(HttpServletRequest request, HttpServletResponse response,
			String rows, String page, VehSuspinfo command) {
		Map map = null;
		try {
			Map filter = new HashMap();
			VehSuspinfo vehSuspinfo = command;
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			//暂时使用云浮公安机构代码,正常为登录用户的部门代码
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String glbm = userSession.getDepartment().getGlbm();
			map = this.suspinfoCancelApproveManager.getSuspinfoCancleTrafficApproves(filter, vehSuspinfo, glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param bkxh
	 * @return
	 */
	@RequestMapping(params="method=getCancelApproveDetailInfo")
	@ResponseBody
	public Object getCancelApproveDetailInfo(HttpServletRequest request, HttpServletResponse response,String bkxh) {
		//暂时使用云浮公安机构代码,正常为登录用户的部门代码
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		String glbm = userSession.getDepartment().getGlbm();
		VehSuspinfo vehInfo = null;
		try {
			vehInfo = (VehSuspinfo) this.suspinfoCancelApproveManager.getApproveDetailForBkxh(bkxh, glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehInfo;
	}
	
	@RequestMapping(params="method=saveCancelApprove", method=RequestMethod.POST)
	@ResponseBody
	public Object saveCancelApprove(HttpServletRequest request, HttpServletResponse response,
			AuditApprove info) {
		Map map = null;
		//**************暂时使用云浮公安机构编码,登录用户的信息****************
		//User user = new User();
//		user.setYhdh("shengting");
//		user.setYhmc("省厅");
//		user.setGlbm("445300000000");
//		user.setBmmc("云浮市公安局");
		//*****************暂时使用云浮公安机构编码,等用户的信息*****************
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		SysUser user = userSession.getSysuser();
		info.setCzr(user.getYhdh());
		info.setCzrmc(user.getYhmc());
		info.setCzrdw(user.getGlbm());
		info.setCzrdwmc(userSession.getDepartment().getBmmc());
		info.setCzrjh(user.getJh());
		try {
			map = this.suspinfoCancelApproveManager.saveApprove(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 
	 * 函数功能说明:审核审批历史记录
	 * @param bkxh
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	@RequestMapping(params="method=getAuditApproveHistoryInfo")
	@ResponseBody
	public Object getAuditApproveHistoryInfo(HttpServletRequest request, HttpServletResponse response, String bkxh) {
		StringBuffer sb = null;
		Map result = null;
		try {
			List list = this.suspinfoCancelApproveManager.getSuspinfoCAuditHistory(bkxh);
			result = new HashMap();
			result.put("suspinfoAuditHistory", list);
		    sb = new StringBuffer("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * 函数功能说明:撤销布控警报历史记录
	 * @param bkxh
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	@RequestMapping(params="method=getCAlarmListHistory")
	@ResponseBody
	public Object getCAlarmListHistory(HttpServletRequest request, HttpServletResponse response, String bkxh) {
		StringBuffer sb = null;
		Map result = null;
		try {
			List list = this.suspinfoCancelApproveManager.getSuspinfoAlarm(bkxh);
		    result = new HashMap();
		    result.put("suspinfoAlarm", list);
		    sb = new StringBuffer("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public SystemManager getSystemManager() {
		return systemManager;
	}

	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	public SuspinfoCancelApproveManager getSuspinfoCancelApproveManager() {
		return suspinfoCancelApproveManager;
	}

	public void setSuspinfoCancelApproveManager(
			SuspinfoCancelApproveManager suspinfoCancelApproveManager) {
		this.suspinfoCancelApproveManager = suspinfoCancelApproveManager;
	}
	
	
	
}
