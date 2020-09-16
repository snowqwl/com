package com.sunshine.monitor.system.sign.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.notice.bean.SysInformation;
import com.sunshine.monitor.system.notice.bean.SysInformationreceive;
import com.sunshine.monitor.system.notice.service.InformationManager;
import com.sunshine.monitor.system.sign.bean.BKSign;
import com.sunshine.monitor.system.sign.bean.Duty;
import com.sunshine.monitor.system.sign.service.BKSignManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

@Controller
@RequestMapping(value="/bksign.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BKSignController {	
	
	@Autowired
	private BKSignManager bkSignManager;
	
	@Autowired
	private InformationManager informationManager;
	
	
	
	@RequestMapping
	public ModelAndView forwardInit(HttpServletRequest request,
			HttpServletResponse response){
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		BKSign bs = new BKSign();
		// 地市行政区划前4位如:广州(4401)
		//bs.setDwdm(userSession.getSysuser().getGlbm());
		bs.setQdr(userSession.getSysuser().getYhdh());
		try {
			BKSign bkSign = this.bkSignManager.queryBkSignListBysubsql(bs);
			if(bkSign.getQdjssj() != null&&bkSign.getQdjssj().length()>0){
				// 未签到
				bs.setQdr(userSession.getSysuser().getYhdh());
				bs.setDwdm(userSession.getSysuser().getGlbm());
				bs.setQdrmc(userSession.getSysuser().getYhmc());
				bs.setDwdmmc(userSession.getDepartment().getBmmc());
				request.setAttribute("bksign",bs);
			} else {
				// 已签到
				request.setAttribute("bksign",null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("worksign/initsgin");
	}
	
	@RequestMapping
	public ModelAndView forwardSignout(HttpServletRequest request,
			HttpServletResponse response){
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		BKSign bs = new BKSign();
		bs.setQdr(userSession.getSysuser().getYhdh());
		bs.setDwdm(userSession.getSysuser().getGlbm());
		bs.setQdrmc(userSession.getSysuser().getYhmc());
		bs.setDwdmmc(userSession.getDepartment().getBmmc());
		try {
			if(bs.getQdr()==null || "".equals(bs.getQdr())){
				// 非法访问
				request.setAttribute("bz", "-1");
			} else {
				// 是否存在未签出的信息(未做任务交接)
				List<BKSign> list = this.bkSignManager.queryNotSignout(userSession.getSysuser().getYhdh());
				List<Duty> dutylist = this.bkSignManager.queryNextZbr(userSession.getSysuser().getYhmc());
				// 交接任务
				if(list != null && list.size() > 0){
					int _size = list.size();
					if(_size == 1){
						request.setAttribute("bksign", list.get(0));
						request.setAttribute("dutylist", dutylist);
					} else {// 多条任务,只取一条签出并做交接班任务
						request.setAttribute("bksign", list.get(0));
						request.setAttribute("dutylist", dutylist);
					}
				// 无交接任务
				} else {
					request.setAttribute("bz", "2");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("bz", "0");
			return new ModelAndView("worksign/zsgin");
		}
		return new ModelAndView("worksign/zsgin");
	}
	
	/**
	 * 签到
	 * @param request
	 * @param response
	 * @param bs
	 * @return
	 */
	@RequestMapping
	public ModelAndView addsign(HttpServletRequest request,
			HttpServletResponse response, BKSign bs){
		String qdkssj = bs.getQdkssj();
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			bs.setQdr(userSession.getSysuser().getYhdh());
			bs.setDwdm(userSession.getSysuser().getGlbm());
			bs.setQdrmc(userSession.getSysuser().getYhmc());
			bs.setDwdmmc(userSession.getDepartment().getBmmc());
			if(qdkssj == null || "".equals(qdkssj)){
				request.setAttribute("bz", "0");
				request.setAttribute("bksign", bs);
				request.setAttribute("errorMsg", "签到时间不能为空！");
				return new ModelAndView("worksign/initsgin");
			}
			String result = bkSignManager.addSign(bs);
			if(result != null && !"".equals(result)){
				request.setAttribute("bz", "1");
				request.setAttribute("bksign", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 异常
			request.setAttribute("bz", "-1");
			request.setAttribute("bksign", bs);
			return new ModelAndView("worksign/initsgin");
		}
		return new ModelAndView("worksign/initsgin");
	}
	
	/**
	 * 签出
	 * 认证从本系统注册的用户及综警用户，暂不支持PKI认证!
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView signout(HttpServletRequest request,
			HttpServletResponse response, BKSign bs){
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		BKSign bks = null ;
		try {
			bks = this.bkSignManager.queryBKSignById(bs.getId());
			bks.setQdrmc(userSession.getSysuser().getYhmc());
			bks.setDwdmmc(userSession.getDepartment().getBmmc());
			bks.setJjr(request.getParameter("jjr"));
			bks.setJjrmc(request.getParameter("jjrmc"));
			// 封装用户信息
			SysUser sysUser = new SysUser();
			sysUser.setYhdh(request.getParameter("zbrbh"));
			sysUser.setMm(request.getParameter("mmmd"));
			sysUser.setYzm(request.getParameter("yzm"));
			sysUser.setBz(request.getParameter("mmsha"));
			// 验证用户密码
			/*try {
				this.bkSignManager.checkUser(sysUser);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("emsg", e.getMessage());
				request.setAttribute("bksign", bks);
				return new ModelAndView("worksign/zsgin");
			}*/
			// 交接任务
			//this.bkSignManager.signout(bs);
			this.bkSignManager.dosignout(bs);
			request.setAttribute("bksign", null);
			request.setAttribute("bz", "1");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("bz", "3");
			request.setAttribute("bksign", bks);
			return new ModelAndView("worksign/zsgin");
		}
		return new ModelAndView("worksign/zsgin");
	}
	
	@RequestMapping
	public ModelAndView forwardSigns(HttpServletRequest request,
			HttpServletResponse response){
		
		return new ModelAndView("worksign/wsginlist");
	}
	

	@RequestMapping
	public ModelAndView forwardDuty(HttpServletRequest request,
			HttpServletResponse response){
		
		return new ModelAndView("worksign/dutylist");
	}	
	@RequestMapping
	public ModelAndView addZblist(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			request.setAttribute("glbmlist", this.informationManager
					.getDepartmentByUser(userSession.getSysuser().getGlbm()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("worksign/dutylistWin");
	}
	
	@RequestMapping
	public ModelAndView editZblist(HttpServletRequest request,
			HttpServletResponse response) {
		String fpkssj = "";
		String fpjssj = "";
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			request.setAttribute("glbmlist", this.informationManager
					.getDepartmentByUser(userSession.getSysuser().getGlbm()));
			List editZblist = this.bkSignManager.editZblist(userSession.getSysuser().getGlbm());
			if(editZblist.size()>0){
			Duty editDuty =	(Duty)editZblist.get(0);
				fpkssj = editDuty.getFpkssj();
				fpjssj = editDuty.getFpjssj();
			}
			request.setAttribute("editZblist",editZblist);
			request.setAttribute("fpkssj",fpkssj);
			request.setAttribute("fpjssj",fpjssj);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("worksign/dutyeditWin");
	}
	@ResponseBody
	@RequestMapping
	public Object getSigns(HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			conditions.put("QDR", userSession.getSysuser().getYhdh());
			result = this.bkSignManager.getSignList(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 布撤控审批数详细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getBCKSPS(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Object> list = null ;
		try {
			BKSign bs = this.bkSignManager.queryBKSignById(id);
			if(bs != null){
				list = this.bkSignManager.queryBCKSPSList(bs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}	
	/**
	 * 预警签收数数详细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getYJQSS(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Object> list = null ;
		try {
			BKSign bs = this.bkSignManager.queryBKSignById(id);
			if(bs != null){
				list = this.bkSignManager.queryYJQSSList(bs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}	
	
	/**
	 * 下达指令数详细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getXDZLS(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Object> list = null ;
		try {
			BKSign bs = this.bkSignManager.queryBKSignById(id);
			if(bs != null){
				list = this.bkSignManager.queryXDZLSList(bs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 指令反馈数详细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getZLFKS(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Object> list = null ;
		try {
			BKSign bs = this.bkSignManager.queryBKSignById(id);
			if(bs != null){
				list = this.bkSignManager.queryZLFKSList(bs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getStaticsDatas(HttpServletRequest request) {
		Map<String,Object> map = null ;
		String id = request.getParameter("id");
		String qdjssj = request.getParameter("qdjssj");
		try {
			map = new HashMap<String,Object>();
			BKSign bs = this.bkSignManager.queryBKSignById(id);
			bs.setQdjssj(qdjssj);
			if(bs != null){
				List<Object> y_bcksp = this.bkSignManager.queryBCKSPSList(bs);
				List<Object> y_yjqs = this.bkSignManager.queryYJQSSList(bs);
				List<Object> y_xdzl = this.bkSignManager.queryXDZLSList(bs);
				List<Object> y_zlfk = this.bkSignManager.queryZLFKSList(bs);
				
				List<VehSuspinfo> n_bcksp = this.bkSignManager.queryNoBCKSPSList(bs);
				List<VehAlarmrec> n_yjqs = this.bkSignManager.queryNoYJQSSList(bs);
				List<VehAlarmrec> n_xdzl = this.bkSignManager.queryNoXDZLSList(bs);
				List<Object> n_zlfk = this.bkSignManager.queryNoZLFKSList(bs);
				
				
				//add by will  统计该用户的完成的任务
				this.bkSignManager.countWork(id,y_bcksp.size(),y_yjqs.size(),y_xdzl.size(),y_zlfk.size());
				
				
				map.put("ybcksp", y_bcksp);
				map.put("yyjqs", y_yjqs);
				map.put("yxdzl", y_xdzl);
				map.put("yzlfk", y_zlfk);
				
				map.put("nbcksp", n_bcksp);
				map.put("nyjqs", n_yjqs);
				map.put("nxdzl", n_xdzl);
				map.put("nzlfk", n_zlfk);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	/**
	 * 查询值班信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getZblist(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			
				Map<String, Object> conditions = Common
						.getParamentersMap(request);
				conditions.put("glbm", userSession.getSysuser().getGlbm());
				result = this.bkSignManager.getZblist(conditions);
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping
	public Object getZblistView(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			
				Map<String, Object> conditions = Common
						.getParamentersMap(request);
				conditions.put("glbm", userSession.getSysuser().getGlbm());
				result = this.bkSignManager.getZblistView(conditions);
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 增加值班
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object saveZbrlist(HttpServletRequest request,
			HttpServletResponse response) {
		int result = 0;	
		int result1 = 0;
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			Map<String, Object> conditions = Common.getParamentersMap(request);
			String a = conditions.get("zbrbhs").toString();
			String b[] = a.split(";");
			String c = conditions.get("zbrs").toString();
			String d[] = c.split(";");		
			String time = this.bkSignManager.getTime();		
			for(int i=0;i<b.length;i++){
				conditions.put("zbrbh",b[i]);
				conditions.put("zbrmc",d[i]);
				conditions.put("time",time);
				result = this.bkSignManager.saveZbrlist(conditions);				
			}
			result1 = this.bkSignManager.addZxzb(userSession);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	 
	
	@RequestMapping
	public ModelAndView getPeople(HttpServletRequest request) {
		//Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			String dwdm = userSession.getDepartment().getGlbm();
			//	conditions.put("QDR", userSession.getSysuser().getYhdh());
			request.setAttribute("dwdm",dwdm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mv = null;
		mv =  new ModelAndView("worksign/changeDuty");
		return mv;
	}
	
	
	
	@ResponseBody
	@RequestMapping
	public Object getPeopleList(HttpServletRequest request) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			result = this.bkSignManager.getPeople(conditions);
			request.setAttribute("result",result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	@ResponseBody
	@RequestMapping
	public Map changePeople(HttpServletRequest request) {
		Map result=null;
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			
			conditions.put("DWDM", userSession.getSysuser().getGlbm());
			conditions.put("QDR", userSession.getSysuser().getYhdh());
			 result = this.bkSignManager.changePeople(conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
