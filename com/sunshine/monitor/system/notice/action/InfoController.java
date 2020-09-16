package com.sunshine.monitor.system.notice.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.notice.bean.SysInformation;
import com.sunshine.monitor.system.notice.bean.SysInformationreceive;
import com.sunshine.monitor.system.notice.service.InformationManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;

@Controller
@RequestMapping(params = "method", value = "/notice.do")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InfoController {

	@Autowired
	private SystemManager systemManager;

	@Autowired
	private InformationManager informationManager;
	
	@Autowired
	@Qualifier("departmentManager")
	private DepartmentManager departmentManager;

	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = null;
		try {
			mv = new ModelAndView();
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			request.setAttribute("yhdh", userSession.getSysuser().getYhdh());
			request.setAttribute("xxlblist", this.systemManager
					.getCodes("100002"));
			request.setAttribute("sfgklist", this.systemManager
					.getCodes("100005"));
			request.setAttribute("sfyxlist", this.systemManager
					.getCodes("100010"));
			request.setAttribute("glbmlist", this.informationManager
					.getDepartmentByUser());
			mv.setViewName("notice/noticepublish");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@ResponseBody
	@RequestMapping
	public Object getInformations(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {			
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			if (userSession != null) {
				Map<String, Object> conditions = Common
						.getParamentersMap(request);
				conditions.put("CZR", userSession.getSysuser().getYhdh());
				result = this.informationManager.getInformations(conditions);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@ResponseBody
	@RequestMapping
	public Object getEditInformations(HttpServletRequest request) throws Exception {
		String xh = request.getParameter("xh");
		SysInformation result = null;
		List yh = null;
		try {
			result = this.informationManager.getEditInformation(xh);
			yh = this.informationManager.getYhdh(xh);
			String a = "";
			String b = "";
		
			if("0".equals(result.getSfgk())){
			for(int i=0;i<yh.size();i++){				
				SysInformationreceive temp = (SysInformationreceive)yh.get(i);				
				String yhmc = this.systemManager.getUserName(temp.getYhdh());
				a+=yhmc+",";
				b+=temp.getYhdh()+",";
				}
			}else{
				for(int i=0;i<yh.size();i++){
				SysInformationreceive temp = (SysInformationreceive)yh.get(i);	
				String yhmc =this.departmentManager.getDepartmentName(temp.getYhdh());
				a+=yhmc+",";
				b+=temp.getYhdh()+",";
				}
			}
		  result.setYhmc(a);
		  result.setYhdh(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	@RequestMapping
	public ModelAndView addInformation(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = null;
		try {
			mv = new ModelAndView();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping
	public ModelAndView addNotice(HttpServletRequest request,
			HttpServletResponse response,String modal) {
		ModelAndView mv = null;
		try {
			mv = new ModelAndView();
			if("new".equals(modal)){
			request.setAttribute("xxlblist", this.systemManager
					.getCodes("100002"));
			request.setAttribute("sfgklist", this.systemManager
					.getCodes("100005"));
			request.setAttribute("sfyxlist", this.systemManager
					.getCodes("100010"));
			mv.setViewName("notice/noticepublishWin");
			}else if("fbfw".equals(modal)){
				request.setAttribute("glbmlist", this.informationManager
					.getDepartmentByUser());
				String sfgk = request.getParameter("sfgk");
				request.setAttribute("sfgk", sfgk);
			mv.setViewName("notice/noticefbfwWin");
			}else{
				request.setAttribute("xxlblist", this.systemManager
						.getCodes("100002"));
				request.setAttribute("sfgklist", this.systemManager
						.getCodes("100005"));
				request.setAttribute("sfyxlist", this.systemManager
						.getCodes("100010"));
				request.setAttribute("xh",request.getParameter("xh"));
				mv.setViewName("notice/noticeeditWin");				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@ResponseBody
	@RequestMapping
	public Object getUser(HttpServletRequest request,
			HttpServletResponse response) {
		List list = null;
		try {
			String glbm = request.getParameter("glbm");
			list = this.systemManager.getUsers(glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@ResponseBody
	@RequestMapping
	public Object saveNotice(HttpServletRequest request,
			HttpServletResponse response, SysInformation command) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			SysInformation information = command;
			String fbfw = request.getParameter("fbfw");
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			information.setCzdw(userSession.getDepartment().getGlbm());
			information.setCzr(userSession.getSysuser().getYhdh());
			String xh = this.informationManager.saveInformation(information);
			boolean flag = false;
			if (xh != null) {
				flag = true;
				_map.put("msg", "保存成功!");
			} else {
				_map.put("msg", "保存失败!");
			}
			if (flag) {
				if ((fbfw != null) && (fbfw.length() > 0)) {
					this.informationManager.removeReceive(xh);
					String[] tmpUsers = fbfw.split(",");
					for (int i = 0; i < tmpUsers.length; i++) {
						SysInformationreceive sysInformationreceive = new SysInformationreceive();
						sysInformationreceive.setYhdh(tmpUsers[i]);
						sysInformationreceive.setXh(xh);
						sysInformationreceive.setSfjs("0");
						this.informationManager
								.saveReceive(sysInformationreceive);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	@ResponseBody
	@RequestMapping
	public Object updateNotice(HttpServletRequest request,
			HttpServletResponse response, SysInformation command) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			SysInformation information = command;
			String fbfw = request.getParameter("fbfw");
			String xh = request.getParameter("xh");
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			information.setCzdw(userSession.getDepartment().getGlbm());
			information.setCzr(userSession.getSysuser().getYhdh());
			int fh = this.informationManager.updateInformation(information);
			boolean flag = false;
			if (fh != 0) {
				flag = true;
				_map.put("msg", "更新成功!");
			} else {
				_map.put("msg", "更新失败!");
			}
			if (flag) {
				if ((fbfw != null) && (fbfw.length() > 0)) {
					this.informationManager.removeReceive(xh);
					String[] tmpUsers = fbfw.split(",");
					for (int i = 0; i < tmpUsers.length; i++) {
						SysInformationreceive sysInformationreceive = new SysInformationreceive();
						sysInformationreceive.setYhdh(tmpUsers[i]);
						sysInformationreceive.setXh(xh);
						sysInformationreceive.setSfjs("0");
						this.informationManager
								.saveReceive(sysInformationreceive);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	@ResponseBody
	@RequestMapping
	public Object removeNotice(HttpServletRequest request,
			HttpServletResponse response, SysInformation command) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			SysInformation information = command;
			String xh = request.getParameter("xh");
			String fbfw = request.getParameter("fbfw");
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			information.setCzdw(userSession.getDepartment().getGlbm());
			information.setCzr(userSession.getSysuser().getYhdh());
			int fh = this.informationManager.removeInformation(information);
			boolean flag = false;
			if (fh != 0) {
				flag = true;
				_map.put("msg", "删除成功!");
			} else {
				_map.put("msg", "删除失败!");
			}
			if (flag) {
				if ((fbfw != null) && (fbfw.length() > 0)) {
					this.informationManager.removeReceive(xh);
				
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	@RequestMapping
	public ModelAndView fquery(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("xxlblist", this.systemManager
					.getCodes("100002"));
			request.setAttribute("ztlist", this.systemManager
					.getCodes("100012"));
			request.setAttribute("bmlist", this.informationManager
					.getDepartmentByUser());
			request.setAttribute("sign", "query");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("notice/noticequery");
	}

	@RequestMapping
	@ResponseBody
	public Object list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			Map<String, Object> params = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			if (userSession != null) {
				params.put("czr", userSession.getSysuser().getYhdh());
				result = this.informationManager.getSysInfos(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping
	@ResponseBody
	public Object zxgglist(HttpServletRequest request, HttpServletResponse response) {
		List<SysInformation> result = null;
		try {
			Map<String, Object> params = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			if (userSession != null) {
				params.put("czr", userSession.getSysuser().getYhdh());
				result = this.informationManager.getZxggInfos(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping()
	public ModelAndView detail(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("notice/noticeinfodetail");
		Map<String,Object> result = null ;
		try {
			result = new HashMap<String,Object>();
			String xh = request.getParameter("xh");
			if ((xh == null) || (xh.length() < 1)) {
				result.put("code", "0");
				result.put("msg", "非法的请求！");
				mv.addObject("codeMap",result);
				return mv;
			}
			UserSession userSession = (UserSession) request.getSession()
					.getAttribute("userSession");
			SysInformation information = this.informationManager.getInformation(xh, userSession.getSysuser().getYhdh());
			if (information.getSfyx().equals("1")) {
				if(this.systemManager.getDepartment(
						information.getCzdw())!=null){
				request.setAttribute("lxdh", this.systemManager.getDepartment(
						information.getCzdw()).getLxdh1());
				}
				else{
					request.setAttribute("lxdh", "");
				}
				//information.setNr(Common.UBB(information.getNr()));
				//information.setFbsj(information.getFbsj().substring(0, 10));
				//information.setJzsj(information.getJzsj().substring(0, 10));
				if(this.systemManager
						.getDepartmentName(information.getCzdw())!=null){
					if(!this.systemManager
						.getDepartmentName(information.getCzdw()).equals("null")){
						information.setCzdw(this.systemManager
								.getDepartmentName(information.getCzdw()));
					}
					else{
						information.setCzdw("");
					}
				}
				else{
					information.setCzdw("");
				}
				request.setAttribute("information", information);
				if (information.getSfgk().equals("1")) {
					LinkedList sublist = new LinkedList();
					List tmpsublist = this.informationManager
							.getReceivesForSublist(information.getXh());
					for (Iterator iter = tmpsublist.iterator(); iter.hasNext();) {
						SysInformationreceive sir = (SysInformationreceive) iter
								.next();
						sir.setYhdh(this.systemManager.getUserName(sir
								.getYhdh()));
						sublist.add(sir);
					}
					if (sublist.size() == 0)
						request.setAttribute("sublist", null);
					else
						request.setAttribute("sublist", sublist);
				}
				SysInformationreceive sir = this.informationManager
						.getReceives(information.getXh(), userSession
								.getSysuser().getYhdh());
				if ((sir != null) && (sir.getSfjs() != null)) {
					if ((sir.getJsnr() == null) || (sir.getJsnr().length() < 1))
						request.setAttribute("qs", "true");
					else {
						request.setAttribute("qs", "false");
					}
					if (sir.getSfjs().equals("0")) {
						sir.setSfjs("1");
						int r = this.informationManager.saveReceive(sir);
						if (r == 1) {
							HashMap map = new HashMap();
							if (request.getSession().getServletContext()
									.getAttribute("news") != null) {
								map = (HashMap) request.getSession()
										.getServletContext().getAttribute(
												"news");
								if (map.containsKey(userSession.getSysuser()
										.getYhdh())) {
									String tmpValue = (String) map
											.get(userSession.getSysuser()
													.getYhdh());
									if (tmpValue.indexOf(xh) != -1) {
										tmpValue = tmpValue.substring(0,
												tmpValue.indexOf(xh))
												+ tmpValue.substring(tmpValue
														.indexOf(xh)
														+ xh.length(), tmpValue
														.length());
										if (tmpValue.startsWith(","))
											tmpValue = tmpValue.substring(1,
													tmpValue.length());
										else if (tmpValue.endsWith(","))
											tmpValue = tmpValue.substring(0,
													tmpValue.length() - 1);
										else {
											tmpValue = tmpValue.replaceAll(
													",,", ",");
										}
										map.put(userSession.getSysuser()
												.getYhdh(), tmpValue);
										request.getSession()
												.getServletContext()
												.setAttribute("news", map);
									}
								}
							}
						}
					}
				}
			} else {
				result.put("code", "0");
				result.put("msg", "非法的请求！");
				mv.addObject("codeMap",result);
				return mv;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv; 
	}
}
