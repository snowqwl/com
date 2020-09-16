package com.sunshine.monitor.comm.maintain.action;

import java.net.URLDecoder;
import java.util.HashMap;
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
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.MaintainHandle;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.maintain.MaintainBean;
import com.sunshine.monitor.comm.maintain.StkkItsmTransmitter;
import com.sunshine.monitor.comm.maintain.service.MaintainManager;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value = "/maintain.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MaintainController {
	
	@Autowired
	private MaintainManager maintainManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	private GateManager gateManager;
	
	/**
	 * 人工故障接入
	 * @return
	 */
	@RequestMapping
	public ModelAndView forward() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("maintain/querymaintain");
		return mv;
	}
	
	/**
	 * 自动故障接入
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardZd() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("maintain/queryzdmt");
		return mv;
	}
	
	/**
	 * 故障反馈录入
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardMaintainHandle(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		try {
			request.setAttribute("gzlxList", systemManager.getCodes("200000"));
			request.setAttribute("gzclztList", systemManager.getCodes("200002"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("maintain/maintainhandle");
		return mv;
	}
	
	
	@RequestMapping(params="method=kksjscForward",method=RequestMethod.GET)	
	public ModelAndView kksjscForward(HttpServletRequest request,String xh) {
		ModelAndView mv = new ModelAndView();
		System.out.println("test");
		try {
			System.out.println("test");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("test");
		mv.setViewName("maintain/kksjscmain");
		return mv;
	}
	
	/**
	 * 故障反馈录入详细页面
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardMaintainHandleWin(HttpServletRequest request,String xh) {
		ModelAndView mv = new ModelAndView();
		
		try {
			request.setAttribute("xh", xh);
			request.setAttribute("gzlxList", systemManager.getCodes("200000"));
			request.setAttribute("gzclztList", systemManager.getCodes("200002"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("maintain/editmaintain");
		return mv;
	}
	
	/**
	 * 批量处理故障
	 * @param request
	 * @param xhs
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardBatchHandle(HttpServletRequest request,String xhs,String clzt) {
		ModelAndView mv = new ModelAndView();
		
		try {
			request.setAttribute("clzt", clzt);
			request.setAttribute("xhs", xhs);
			request.setAttribute("gzclztList", systemManager.getCodes("200002"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("maintain/batchmaintain");
		return mv;
	}
	
	/**
	 * 查询卡口不在线列表
	 * @param page
	 * @param rows
	 * @param bzlx:类型为1时(卡口延时上传)查询为monitor用户的抽取数据、不为1时查询JM_MAINTAIN_HANDLE表数据
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Map queryList(String page, String rows,String bzlx){
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		
		Map map = null;
		try{
			map = maintainManager.findMaintainForMap(filter,bzlx);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 发送运维信息到运维平台(自动接入故障-卡口不在线)
	 * @param kdbh
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Object sendMaintain(String kdbh,HttpServletRequest request){
		String content = "3";
		
		UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
		
		try {
			CodeGate gate = gateManager.getGate(kdbh);
			
			MaintainBean bean = new MaintainBean();
			bean.setBusinessModule("2048");
			bean.setEventType("02");
			
			bean.setLinkMan(userSession.getSysuser().getJh());
			bean.setMobilePhone(userSession.getSysuser().getLxdh1());
			
			bean.setContent("卡口【" +gate.getKdmc() + "】不在线，请修复！");
			
		content = new StkkItsmTransmitter().send(null, bean);
		
		
		if(content.equals("0")){
			maintainManager.setby1ForGate(kdbh);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return content;
	}
	
	
	/**
	 * 发送运维信息到运维平台(人工故障)
	 * @param 
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Object sendMaintainForRg(String gzqk,HttpServletRequest request){
		String content = "3";
		UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
		
		try {
			
			MaintainBean bean = new MaintainBean();
			bean.setBusinessModule("2048");
			bean.setEventType("02");
			
			bean.setLinkMan(userSession.getSysuser().getJh());
			bean.setMobilePhone("13450271734");
			bean.setContent(URLDecoder.decode(gzqk,"utf-8"));
			
		content = new StkkItsmTransmitter().send(null, bean);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return content;
	}
	
	/**
	 * 批量发送运维信息到运维平台(人工故障)
	 * @param xhs
	 * @param request
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Object batchSendMaintainForRg(String xhs,HttpServletRequest request){
		String content = "3";
		int success_count = 0;
		int fail_cout=0;
		UserSession userSession = (UserSession)WebUtils.getSessionAttribute(request, "userSession");
		
		try {
			
			MaintainBean bean = new MaintainBean();
			bean.setBusinessModule("2048");
			bean.setEventType("02");
			
			bean.setLinkMan(userSession.getSysuser().getJh());
			bean.setMobilePhone("13450271734");
			MaintainHandle  handle = null;
			String[] xhlist = xhs.split(",");
			for(int i = 0; i < xhlist.length; i++) {
				handle = maintainManager.getMaintainHandleForId(xhlist[i]);
				bean.setContent(handle.getGzqk());
				content = new StkkItsmTransmitter().send(null, bean);
				if("0".equals(content)) {
					success_count++;
				} else {
					fail_cout++;
				}
			}
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return "成功发送运维平台"+success_count+"条，发送失败："+fail_cout+"条";
	}
	
	/**
	 * 查询故障反馈列表
	 * @param page
	 * @param rows
	 * @param handle
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping()
	@ResponseBody
	public Object queryMaintainHandleList(HttpServletRequest request,String page, String rows,MaintainHandle handle){
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		Department department =userSession.getDepartment();
		Map map = null;
		try{
			map = maintainManager.findMaintainHandleForMap(filter,handle,department);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 *根据id查询MaintainHandle对象
	 * @param id
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public MaintainHandle getMaintainHandleForId(String id){
		MaintainHandle  handle = null;
		
		try {
			handle = maintainManager.getMaintainHandleForId(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return handle;
	}
	
	/**
	 * 保存表单
	 * @param handle
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Object saveMaintainHandle(HttpServletRequest request, MaintainHandle handle){
		Map map = null;
		
		try {
			UserSession userSession = (UserSession) WebUtils
										.getSessionAttribute(request, "userSession");
			
			handle.setSbr(userSession.getSysuser().getYhdh());
			handle.setSbrmc(userSession.getSysuser().getYhmc());
			
			handle.setSbrdwdm(userSession.getDepartment().getGlbm());
			handle.setFkrdwdm(userSession.getDepartment().getGlbm());
			String i = maintainManager.saveMaintainHandle(handle);
			if(null!=i){
			if(i.length() > 0){
				map = Common.messageBox("操作成功！", "1");
			}
			else{
				map = Common.messageBox("操作失败！", "0");
			}
			}
			else{
				map = Common.messageBox("操作失败！", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 批量处理故障
	 * @param request
	 * @param xhs
	 * @param handle
	 * @return
	 */
	@RequestMapping()
	@ResponseBody
	public Object batchHandle(HttpServletRequest request,String xhs, MaintainHandle handle){
		Map map = null;	
		try {
			String[] xhlist = xhs.split(",");
			int success_count = 0;
			int fail_count = 0;
			UserSession userSession = (UserSession) WebUtils
										.getSessionAttribute(request, "userSession");
			
			handle.setSbr(userSession.getSysuser().getYhdh());
			handle.setSbrmc(userSession.getSysuser().getYhmc());
			
			handle.setSbrdwdm(userSession.getDepartment().getGlbm());
			handle.setFkrdwdm(userSession.getDepartment().getGlbm());
			String result = null;
			for(int i =0; i<xhlist.length; i++) {
				handle.setXh(xhlist[i]);
			
			result = maintainManager.saveMaintainHandle(handle);
			if(null!=result){
			if(result.length() > 0){
				success_count ++;
			}
			else{
				fail_count++;
			}
			}
			else{
				fail_count++;
			}
			}
			map = Common.messageBox("成功处理"+success_count+"条故障，失败数："+fail_count, "1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 跳转到IT资源综合监管系统
	 * 
	 */
	@RequestMapping
	public ModelAndView findForward(HttpServletRequest request, HttpServletResponse response) {		
		return new ModelAndView("maintain/ittemp");
	}
	
	
}
