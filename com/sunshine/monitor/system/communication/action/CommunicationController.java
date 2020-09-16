package com.sunshine.monitor.system.communication.action;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.communication.bean.Communication;
import com.sunshine.monitor.system.communication.service.CommunicationManager;

/**
 * 
 * @author yudam
 * @since 2014-10-15
 */
@Controller
@RequestMapping(value = "/communication.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CommunicationController {

	@Autowired
	@Qualifier("CommunicationManager")
	private CommunicationManager CommunicationManager;

	/**
	 * Forward main page
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("communication/communicationQuery");
	}
	
	/**
	 * 获取短信的信息
	 * @param request
	 * @return
	 * 
	 */
	@RequestMapping
	@ResponseBody
	public Object getSMSDates(HttpServletRequest request,
			HttpServletResponse response ) throws Exception {
		Map<String, Object> result = null ;
		Map<String, Object> conditions = Common.getParamentersMap(request);	
	    result = this.CommunicationManager.querySMSlist(conditions);
		List<Communication> list = (List<Communication>) result.get("rows");
		if (list == null || list.size() == 0) {
			result.put("counts", "0");
		} else {
			result.put("counts", list.size());
		}
		return result;
	}

	/**
	 * 获取卡口的信息
	 * @param request
	 * @return
	 * 
	 */
	@RequestMapping
	@ResponseBody
	public Object getGateDates(HttpServletRequest request,
			HttpServletResponse response ) throws Exception {
		Map<String, Object> result = null ;
		Map<String, Object> conditions = Common.getParamentersMap(request);	
	    result = this.CommunicationManager.queryGatelist(conditions);
		List<Communication> list = (List<Communication>) result.get("rows");
		if (list == null || list.size() == 0) {
			result.put("counts", "0");
		} else {
			result.put("counts", list.size());
		}
		
		return result;
	}
	
	/**
	 * 修改该卡口方向的管理电话
	 * @param request
	 * @return
	 * 
	 */
	@RequestMapping
	@ResponseBody
	public int gateDirectQuery(HttpServletRequest request,
			HttpServletResponse response ) throws Exception {
		Map gate = new HashMap();
		gate.put("kdbh", request.getParameter("kdbh"));
		gate.put("fxbh", request.getParameter("fxbh"));
		gate.put("ywsjhm", request.getParameter("ywsjhm"));
		int result = this.CommunicationManager.updateGate(gate);
		return result;
	}
	
	/**
	 * 修改用户电话
	 * @param request
	 * @return
	 * 
	 */
	@RequestMapping
	@ResponseBody
	public int updateUserPhone(HttpServletRequest request,
			HttpServletResponse response ) throws Exception {
		Map<String,String> user = new HashMap<String,String>();
		user.put("yhdh", request.getParameter("yhdh"));
		user.put("lxdh1", request.getParameter("lxdh1"));
		user.put("lxdh2", request.getParameter("lxdh2"));
		user.put("lxdh3", request.getParameter("lxdh3"));
		int result = this.CommunicationManager.updateUserPhone(user);
		return result;
	}
	
	
	/**
	 * 获取用户的信息
	 * @param request
	 * @return
	 * @author yudam
	 */
	@RequestMapping
	@ResponseBody
	public Object getUserDates(HttpServletRequest request,
			HttpServletResponse response ) throws Exception {
		Map<String, Object> result = null ;
		Map<String, Object> conditions = Common.getParamentersMap(request);	
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String dwdm=userSession.getSysuser().getGlbm();
		conditions.put("glbm", dwdm);
	    result = this.CommunicationManager.queryUserlist(conditions);
		List<Communication> list = (List<Communication>) result.get("rows");
		if (list == null || list.size() == 0) {
			result.put("counts", "0");
		} else {
			result.put("counts", list.size());
		}
		
		return result;
	}
	
	/**
	 * 立刻发送短信
	 * @return  0 :发送失败  1:发送成功
	 * 
	 * */
	@RequestMapping
	@ResponseBody
	public int sendSMS(HttpServletRequest request,HttpServletResponse response ) throws Exception {
		
		//String dxjshm = request.getParameter("dxjshm");
		//String dxnr = request.getParameter("dxnr");
		//String id = request.getParameter("id");
		Map<String,String> sms = new HashMap<String,String>();
		sms.put("dxjshm", request.getParameter("dxjshm"));
		sms.put("dxnr", request.getParameter("dxnr"));
		sms.put("id", request.getParameter("id"));
		try{
		//发送成功  修改FRM_COMMUNICATION表的相关字段
		int result = this.CommunicationManager.updateCommById(sms);
		return result;
		}
		catch (Exception e){
			return 0;
		}
	}
	
	/**
	 * 通过xh获取短信的信息
	 * @param request
	 * @return
	 * 
	 */
	@RequestMapping
	@ResponseBody
	public Object getSMSDatesByXh(HttpServletRequest request) throws Exception {
		String xh = request.getParameter("xh");	  
		List list =  this.CommunicationManager.getSMSDatesByXh(xh);		
		return list;
	}
	
	
}
