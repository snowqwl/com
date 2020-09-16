package com.sunshine.monitor.system.susp.action;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;

@Controller
@RequestMapping(value="/suspinfoCancelAuditCtrl.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuspInfoCancelAuditCtroller {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("suspinfoAuditApproveManager")
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;
	/**
	 * 跳转撤控审核页面
	 * @param request
	 * @param begin
	 * @param end
	 * @param param
	 * @return
	 */
	@RequestMapping()
	public String findForward(HttpServletRequest request,String begin,String end,String param,String isFromIndex){
		
		UserSession userSession = (UserSession) WebUtils
									.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();
		
		request.setAttribute("yhmc", user.getYhmc());
		request.setAttribute("bmmc", dp.getBmmc());

		
		try {
			if("".equals(isFromIndex) || isFromIndex==null){
				isFromIndex="0";//默认为0，表示号牌号码默认为"湘"字，1表示从首页传过来的，页面不显示"湘"
			}
			if (param != null && param.equals("count")) {
				 end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				 begin = this.systemManager.getSysDate("-180", false) + " 00:00:00";
			}
			if (param != null && param.equals("overtimecount")) {
				end = this.systemManager.getSysDate(null, false) + " 23:59:59";
				begin = this.systemManager.getSysDate("-180", false)
						+ " 00:00:00";
				request.setAttribute("canceladuitovertime", "1");
				List list = systemManager.getCode("120019");
				list.remove(0);
				list.remove(0);
				request.setAttribute("bkdlList", list);

			}
			else
			request.setAttribute("bkdlList", systemManager.getCode("120019"));
			request.setAttribute("auditList", systemManager.getCode("190002"));
			request.setAttribute("hpzlList", systemManager.getCode("030107"));
			request.setAttribute("bklbList", systemManager.getCode("120005"));
			request.setAttribute("bjztList", systemManager.getCode("130009"));
			request.setAttribute("begin", begin);
			request.setAttribute("end", end);
			request.setAttribute("isFromIndex", isFromIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "engineer/cancelauditmain";
	}
	
	/**
	 * 跳转撤控审核详细页面
	 * @param request
	 * @param bkxh
	 * @return
	 */
	@RequestMapping
	public String forwardWin(HttpServletRequest request,String bkxh){
		
		UserSession userSession = (UserSession) WebUtils
												.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		Department dp = userSession.getDepartment();
		
		request.setAttribute("yhmc", user.getYhmc());
		request.setAttribute("bmmc", dp.getBmmc());
		request.setAttribute("bkxh", bkxh);
		
		try {
			request.setAttribute("auditList", systemManager.getCode("190002"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return "engineer/cancelsuspauditwin";
	}
	
	
	@RequestMapping
	@ResponseBody
	public void saveCancelAuditSuspInfo(AuditApprove info,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map v = null;
		String users=request.getParameter("dxjsr");
		String lxdh=request.getParameter("lxdh");
		if(!StringUtils.isBlank(users)){
			info.setUser(users);
		}
		if(!StringUtils.isBlank(lxdh)){
			info.setLxdh(lxdh);
		}
		if(StringUtils.isBlank(info.getBkxh()) || StringUtils.isBlank(info.getCzjg())){
			v = Common.messageBox("参数传输异常！","0");
		}else{
		
			UserSession userSession = (UserSession) WebUtils
											.getSessionAttribute(request, "userSession");
			SysUser user = userSession.getSysuser();
			Department dp = userSession.getDepartment();
			
			info.setCzr(user.getYhdh());
			info.setCzrdw(user.getGlbm());
			info.setCzrdwmc(dp.getBmmc());
			info.setCzrjh(user.getJh());
			info.setCzrmc(user.getYhmc());
			
			try {
				v = suspinfoAuditApproveManager.saveCAudit(info);
			} catch (Exception e) {
				e.printStackTrace();
				v = Common.messageBox("程序异常！","0");
			}
		}
		
		PrintWriter writer = null;
		
		try {
			writer = response.getWriter();
			writer.print(JSONObject.fromObject(v));
		} catch (IOException e) {
			throw e;
		}finally{
			if(writer != null){
				writer.flush();
				writer.close();
			}
		}
		
	}
	
}
