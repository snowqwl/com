package com.sunshine.monitor.system.susp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoProvinceApproveManager;

@Controller
@RequestMapping(value = "/suspinfoProvinceApprove.do", params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspinfoProvinceApproveCtroller {
	
	@Autowired
	@Qualifier("suspinfoProvinceApproveManager")
	private SuspinfoProvinceApproveManager suspinfoProvinceApproveManager;
	
	@Autowired
	private SystemManager systemManager;
	
	
	private Logger logger = LoggerFactory.getLogger(SuspinfoProvinceApproveCtroller.class);
	
	
	/**
	 * 跳转相应的审批主页
	 * @param response
	 * @param request
	 * @param approveType
	 * @param begin
	 * @param end
	 * @param param
	 * @return
	 */
	@RequestMapping(params="method=index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,String approveType,String begin,String end,String param,String isFromIndex) {
		ModelAndView mv = null;
		List bkdlList = null;
		List bklbList = null;
		try {
			if("".equals(isFromIndex) || isFromIndex==null){
				isFromIndex="0";//默认为0，表示号牌号码默认为"湘"字，1表示从首页传过来的，页面不显示"湘"
			}
			
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			if (approveType != null && approveType.equals("classApprove")) {
			   bklbList = this.systemManager.getCodesByDmsm("120005", "1", 4);
			   
			   mv = new ModelAndView("engineer/approveclassmain");
			} else if (approveType != null && approveType.equals("trafficApprove")) {
				bklbList = this.systemManager.getCodesByDmsm("120005", "2", 4);
				mv = new ModelAndView("engineer/approvetrafficmain");
			}else { 
			   bkdlList = this.systemManager.getCode("120019");
			   bklbList = this.systemManager.getCode("120005");
			   mv = new ModelAndView("engineer/provinceapprovemain");
			}
		
			if (param != null && param.equals("overtimecount")) {
				end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				begin = this.systemManager.getSysDate("-180", false)
						+ " 00:00:00";
				request.setAttribute("approveovertime", "1");
				bkdlList = systemManager.getCode("120019");
				if(bkdlList.size()>=2)
				bkdlList.remove(1);
			}
			
			List hpzlList = this.systemManager.getCode("030107");
			List auditList = this.systemManager.getCode("190002");
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("bkdlList", bkdlList);
			map.put("hpzlList", hpzlList);
			map.put("bklbList", bklbList);
			map.put("auditList", auditList);
			map.put("begin", begin);
			map.put("end", end);
			map.put("ywzt", "12");
			mv.addObject("codeMap",map);
			//暂时使用云浮市公安机构代码,正常为登录用户所在部门
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
	 * 布控指挥中心审批查询列表
	 * @param page
	 * @param rows
	 * @param request
	 * @param response
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
			filter.put("pageSize", rows);
			
			//暂时使用云浮公安机构代码,正常为登录用户的部门代码
			UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			String glbm = userSession.getDepartment().getGlbm();
			
			if (request.getParameter("overtime") != null) {
				if (request.getParameter("overtime").equals("1")) {
					map = this.suspinfoProvinceApproveManager.getSuspinfoApprovesOverTime(
							filter, vehSuspinfo, glbm);
				}
			} else
				map = this.suspinfoProvinceApproveManager.getSuspinfoApproves(filter,
						vehSuspinfo, glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	/**
	 * 跳转布控审批详细页面
	 * @param bkxh
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping
	public String forwardWin(HttpServletRequest request,@RequestParam String bkxh){
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
		
		return "engineer/approvewin";
	}
	
	
}
