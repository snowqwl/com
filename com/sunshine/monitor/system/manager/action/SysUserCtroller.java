package com.sunshine.monitor.system.manager.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom.IllegalDataException;
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
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.action.CommController;
import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.filter.OnlineUserListener;
import com.sunshine.monitor.comm.service.RoleManager;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.service.DepartmentManager;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.PrefectureManager;
import com.sunshine.monitor.system.manager.service.SysUserManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.notice.service.InformationManager;

@Controller
@RequestMapping(value = "/sysUserCtrl.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SysUserCtroller {

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@Autowired
	@Qualifier("sysUserManager")
	private SysUserManager sysUserManager;

	@Autowired
	@Qualifier("departmentManager")
	private DepartmentManager departmentManager;

	@Autowired
	@Qualifier("prefectureManager")
	private PrefectureManager prefectureManager;
	
	@Autowired
	@Qualifier("logManager")
	private LogManager logManager;

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private InformationManager informationManager;

	@RequestMapping()
	public ModelAndView findForward(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			/**
			Role r = new Role();
			request.setAttribute("jsList", this.sysUserManager.getRoleList(r));
			*/
			Role role = getRoleEntrity(request);
			List list = sysUserManager.getRolesList(role);
			if(list.size()==0){
				throw new IllegalDataException("角色属性未维护,请先维护角色属性[1:同级管理员,0:同级普通]");
			}
			request.setAttribute("jsList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("manager/sysusermain");
	}

	@RequestMapping()
	@ResponseBody
	public Map queryList(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			SysUser command) {
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String dwdm = userSession.getSysuser().getGlbm();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dwdm", dwdm);
		map.put("curPage", page); // 页数
		map.put("pageSize", rows); // 每页显示行数

		Map result = new HashMap();
		try {

			result = this.sysUserManager.findSysUserForMap(map, command);
			
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
			StringBuffer sb=new StringBuffer();
			
			log.setCzlx("9041");
			sb.append("用户查询，操作条件：");
				
			if (command.getGlbm() != null && command.getGlbm().length() > 0) {
				sb.append(" 管理部门："
						+ departmentManager.getDepartmentName(command.getGlbm()));
			}
			if (command.getYhdh() != null && command.getYhdh().length() > 0) {
				sb.append(" 用户代码：" + command.getYhdh());
			}
			if (command.getYhmc() != null && command.getYhmc().length() > 0) {
				sb.append(" 用户名称：" + command.getYhmc());
			}
			if (command.getJh() != null && command.getJh().length() > 0) {
				sb.append(" 警号：" + command.getJh());
			}
			if (command.getSfzmhm() != null && command.getSfzmhm().length() > 0) {
				sb.append(" 身份证号：" + command.getSfzmhm());
			}
			log.setCznr(sb.toString());
			this.logManager.saveLog(log);
			/**
			if(!StringUtils.isBlank(command.getRoles())){ //具体角色
				result = this.sysUserManager.findSysUserForMap(map, command);
			} else {// 全部
				Role role = getRoleEntrity(request);
				List list = sysUserManager.getRolesList(role);
				if(list.size()>0){ 
					String rolesStr = "";
					for(Object obj:list){
						Role tempRole =  (Role)obj;
						rolesStr += "'" + tempRole.getJsdh() +"',";
					}
					rolesStr = rolesStr.substring(0, rolesStr.length()-1);
					command.setRoles(rolesStr);
					result = this.sysUserManager.findSysUsersForMap(map, command);
				}
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping()
	@ResponseBody
	public Object saveUser(HttpServletRequest request,
			HttpServletResponse response, SysUser command) {
		try {
			String modal = request.getParameter("modal");
			String kcUser = request.getParameter("synch");
			SysUser tmpSysuser = new SysUser();
			if(StringUtils.isNotBlank(kcUser)) {
				tmpSysuser = sysUserManager.findUserByJHAndSfzh(command.getJh(), command.getSfzmhm());
			} else
				tmpSysuser = this.sysUserManager.getSysUser(command.getYhdh());

			if ("new".equals(modal)) {
				if (tmpSysuser != null) {
					return Common.messageBox("已存在用户代号为" + command.getYhdh()
							+ "的用户", "0");
				}
			}

			if (StringUtils.isBlank(command.getMm())) {
				// 默认密码为888888
				command.setMm("888888");
			}
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request , "userSession");
			String ip =request.getRemoteAddr();
			String bz=request.getParameter("bz"); 
			command.setBz(bz);
			this.sysUserManager.saveOrUpdateSysUser(command, modal, userSession.getSysuser(), ip);
			
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
		    log.setCzlx("9042");
		    //新增
		    log.setJh(userSession.getSysuser().getJh());
//		    log.setBsqr(command.getYhdh());
		    log.setBsqr(command.getYhmc());
		    log.setBsqrjh(command.getJh());
		    log.setSqjs(command.getRolemc());
		    log.setSqr(userSession.getSysuser().getYhmc());
		    if(command.getRolemc() !=null && !"".equals(command.getRolemc())) {
		    	log.setSfsq("1");
		    }
		    
		    StringBuffer sb=new StringBuffer();
		    if(modal.equals("new")){
		    	 sb.append("用户新增成功，操作条件为：");
				 sb.append("用户名称："+command.getYhmc());
		    }else{
		    	sb.append("用户修改成功，操作条件为：");
		    	if(StringUtils.isNotBlank(tmpSysuser.getBmmc()))
				    if (StringUtils.isNotBlank(command.getBmmc()) && !tmpSysuser.getBmmc().equals(command.getBmmc())) {
				    	sb.append(" 修改前部门名称："+departmentManager.getDepartmentName(tmpSysuser.getGlbm())+"，修改后部门名称："+departmentManager.getDepartmentName(command.getGlbm()));
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getZt()))
				    if (StringUtils.isNotBlank(command.getZt())  && !tmpSysuser.getZt().equals(command.getZt())) {
				    	if(StringUtils.isNotBlank(tmpSysuser.getZt())){
					    	if(tmpSysuser.getZt().equals("0")){
					    		sb.append(" 修改前状态：无效，修改后状态：有效");
					    	}else if(tmpSysuser.getZt().equals("1")){
					    		sb.append(" 修改前状态：有效，修改后状态：无效");
					    	}
				    	}
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getYhdh()))
				    if (StringUtils.isNotBlank(command.getYhdh()) && !tmpSysuser.getYhdh().equals(command.getYhdh())) {
				    	sb.append(" 修改前用户名："+tmpSysuser.getYhdh()+"，修改后用户名："+command.getYhdh());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getYhmc()))
				    if (StringUtils.isNotBlank(command.getYhmc()) && !tmpSysuser.getYhmc().equals(command.getYhmc())) {
				    	sb.append(" 修改前用户姓名："+tmpSysuser.getYhmc()+"，修改后用户姓名："+command.getYhmc());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getSfzmhm()))
				    if (StringUtils.isNotBlank(command.getSfzmhm()) && !tmpSysuser.getSfzmhm().equals(command.getSfzmhm())) {
				    	sb.append(" 修改前证件号码："+tmpSysuser.getSfzmhm()+"，修改后证件号码："+command.getSfzmhm());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getJh()))
				    if (StringUtils.isNotBlank(command.getJh()) && !tmpSysuser.getJh().equals(command.getJh())) {
				    	sb.append(" 修改前警号："+tmpSysuser.getJh()+"，修改后警号："+command.getJh());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getDzzs()))
				    if (StringUtils.isNotBlank(command.getDzzs()) && !tmpSysuser.getDzzs().equals(command.getDzzs())) {
				    	StringBuffer dzzsA = new StringBuffer();
				    	if(tmpSysuser.getDzzs().equals("0")){
				    		dzzsA.append("普通登录");
				    	}else if(tmpSysuser.getDzzs().equals("1")){
				    		dzzsA.append("PKI登录");
				    	}else if(tmpSysuser.getDzzs().equals("2")){
				    		dzzsA.append("外部系统单点登录");
				    	}
				    	
				    	StringBuffer dzzsB = new StringBuffer();
				    	if(command.getDzzs().equals("0")){
				    		dzzsB.append("普通登录");
				    	}else if(command.getDzzs().equals("1")){
				    		dzzsB.append("PKI登录");
				    	}else if(command.getDzzs().equals("2")){
				    		dzzsB.append("外部系统单点登录");
				    	}
				    	
				    	sb.append(" 修改前电子证书："+dzzsA.toString()+"，修改后电子证书："+dzzsB.toString());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getDzzsxx()))
				    if (StringUtils.isNotBlank(command.getDzzsxx()) && !tmpSysuser.getDzzsxx().equals(command.getDzzsxx())) {
				    	sb.append(" 修改前电子证书信息："+tmpSysuser.getDzzsxx()+"，修改后电子证书信息："+command.getDzzsxx());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getLxdh1()))
				    if (StringUtils.isNotBlank(command.getLxdh1()) && !tmpSysuser.getLxdh1().equals(command.getLxdh1())) {
				    	sb.append(" 修改前移动电话："+tmpSysuser.getLxdh1()+"，修改后移动电话："+command.getLxdh1());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getLxdh2()))
				    if (StringUtils.isNotBlank(command.getLxdh2()) && !tmpSysuser.getLxdh2().equals(command.getLxdh2())) {
				    	sb.append(" 修改前办公电话："+tmpSysuser.getLxdh2()+"，修改后办公电话："+command.getLxdh2());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getLxdh3()))
				    if (StringUtils.isNotBlank(command.getLxdh3()) && !tmpSysuser.getLxdh3().equals(command.getLxdh3())) {
				    	sb.append(" 修改前家庭电话："+tmpSysuser.getLxdh3()+"，修改后家庭电话："+command.getLxdh3());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getLxcz()))
				    if (StringUtils.isNotBlank(command.getLxcz()) && !tmpSysuser.getLxcz().equals(command.getLxcz())) {
				    	sb.append(" 修改前联系传真："+tmpSysuser.getLxcz()+"，修改后联系传真："+command.getLxcz());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getBgdz()))
				    if (StringUtils.isNotBlank(command.getBgdz()) && !tmpSysuser.getBgdz().equals(command.getBgdz())) {
				    	sb.append(" 修改前办公地址："+tmpSysuser.getBgdz()+"，修改后办公地址："+command.getBgdz());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getDzyx()))
				    if (StringUtils.isNotBlank(command.getDzyx()) && !tmpSysuser.getDzyx().equals(command.getDzyx())) {
				    	sb.append(" 修改前电子邮箱："+tmpSysuser.getDzyx()+"，修改后电子邮箱："+command.getDzyx());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getLxdz()))
				    if (StringUtils.isNotBlank(command.getLxdz()) && !tmpSysuser.getLxdz().equals(command.getLxdz())) {
				    	sb.append(" 修改前联系地址："+tmpSysuser.getLxdz()+"，修改后联系地址："+command.getLxdz());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getMmyxq()))
				    if (StringUtils.isNotBlank(command.getMmyxq()) && !tmpSysuser.getMmyxq().equals(command.getMmyxq())) {
				    	sb.append(" 修改前密码有效期："+tmpSysuser.getMmyxq()+"，修改后密码有效期："+command.getMmyxq());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getZhyxq()))
				    if (StringUtils.isNotBlank(command.getZhyxq()) && !tmpSysuser.getZhyxq().equals(command.getZhyxq())) {
				    	sb.append(" 修改前账号有效期："+tmpSysuser.getZhyxq()+"，修改后账号有效期："+command.getZhyxq());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getIpks()))
				    if (StringUtils.isNotBlank(command.getIpks()) && !tmpSysuser.getIpks().equals(command.getIpks())) {
				    	sb.append(" 修改前IP起始地址："+tmpSysuser.getIpks()+"，修改后IP起始地址："+command.getIpks());
					}
		    	if(StringUtils.isNotBlank(tmpSysuser.getIpjs()))
				    if (StringUtils.isNotBlank(command.getIpjs()) && !tmpSysuser.getIpjs().equals(command.getIpjs())) {
				    	sb.append(" 修改前IP结束地址："+tmpSysuser.getIpjs()+"，修改后IP结束地址："+command.getIpjs());
					}
		    }
		   
		    log.setCznr(sb.toString());
		    
			this.logManager.saveLog(log);
			
			return Common.messageBox("保存成功！", "1");

		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}

	}

	@RequestMapping()
	@ResponseBody
	public Object saveGrxx(HttpServletRequest request,
						   HttpServletResponse response, SysUser command) {
		try {
			this.sysUserManager.updateSysUser(command);

			return Common.messageBox("保存成功！", "1");

		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！", "0");
		}

	}

	@RequestMapping()
	@ResponseBody
	public Object updatePasswd(HttpServletRequest request,
			HttpServletResponse response, SysUser user) {
		String yhdh = request.getParameter("yhdh");
		int module = Integer.parseInt(request.getParameter("module"));
		Map map;
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request , "userSession");
			int result = 0;
			if (module == 0) {
				result = this.sysUserManager.updatePasswd(user, yhdh);
			} else {
				result = this.sysUserManager.reset(yhdh);
			}
			if (result == 1) {
				map = Common.messageBox("修改成功！", "1");
				
				Log log = new Log();
				log.setGlbm(userSession.getSysuser().getGlbm());
				log.setIp(userSession.getSysuser().getIp());
				log.setYhdh(userSession.getSysuser().getYhdh());
			    log.setCzlx("9049");
			    StringBuffer sb=new StringBuffer();
			    sb.append("用户重置密码成功，操作条件为：");
			    sb.append("，用户名为："+yhdh);
			    log.setCznr(sb.toString());
				this.logManager.saveLog(log);
			} else {
				map = Common.messageBox("原始密码输入不正确！", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("出现异常！", "0");
		}
		return map;
	}

	@RequestMapping()
	@ResponseBody
	public SysUser getSysUser(HttpServletRequest request,
			HttpServletResponse response) {
		SysUser sysuser = null;
		try {
			String yhdh = URLDecoder.decode(request.getParameter("yhdh"),
					"UTF-8");
			sysuser = this.sysUserManager.getSysUser(yhdh);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (sysuser == null)
			sysuser = new SysUser();
		return sysuser;
	}

	@RequestMapping()
	@ResponseBody
	public Object removeUser(HttpServletRequest request,
			HttpServletResponse response, SysUser user) {
		
		try {
			System.out.println("yd -- > " + user.getYhdh());
			
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request , "userSession");
			
			if (StringUtils.isBlank(user.getYhdh())) {
				return Common.messageBox("参数传输异常！", "0");
			}

			// SysUser user = new SysUser();
			// user.setYhdh(yhdh);
			this.sysUserManager.delSysUser(user);
			
			Log log = new Log();
			log.setGlbm(userSession.getSysuser().getGlbm());
			log.setIp(userSession.getSysuser().getIp());
			log.setYhdh(userSession.getSysuser().getYhdh());
		    log.setCzlx("9043");
		    StringBuffer sb=new StringBuffer();
		    sb.append("用户删除成功，操作条件为：");
		    sb.append(" 用户名称为："+user.getYhmc());
		    log.setCznr(sb.toString());
			this.logManager.saveLog(log);
			
			return Common.messageBox("删除成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("程序异常！!", "0");
		}

	}

	@RequestMapping()
	@ResponseBody
	public List<Map<String, Object>> getRoleTree() {
		List<Map<String, Object>> menuNodes = new ArrayList<Map<String, Object>>();
		Map root = new HashMap();
		root.put("id", "100");
		root.put("pid", "0");
		root.put("name", "系统用户角色");
		menuNodes.add(root);

		try {
			List list = sysUserManager.getRoleList(new Role());
			for (Object o : list) {
				Role r = (Role) o;
				Map m = new HashMap();
				m.put("id", r.getJsdh());
				m.put("name", r.getJsmc());
				m.put("pid", "100");
				menuNodes.add(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return menuNodes;
	}

	@RequestMapping()
	@ResponseBody
	public List<Map<String, Object>> getRolesTree(HttpServletRequest request) {
		List<Map<String, Object>> menuNodes = new ArrayList<Map<String, Object>>();
		try {
			/**if (StringUtils.isBlank(roles)) {
				throw new Exception("当前用户角色未分配，请联系管理员！");
			} else {
				String[] roleArray = roles.split(",");
				if (roleArray.length > 1) {
					List<Role> rlist = roleManager.queryRoles(roles);
					role =getSuperRole(rlist);
				} else {
					role = roleManager.getRoleById(roles);
				}
			}*/
			Role role = getRoleEntrity(request);
			List list = sysUserManager.getRolesList(role);
			if(list.size()==0){
				//menuNodes.clear();
				throw new IllegalDataException("角色属性未维护,请先维护角色属性[1:同级管理员,0:同级普通]");
			}
//			Map root = new HashMap();
//			root.put("id", "100");
//			root.put("pid", "0");
//			root.put("name", "系统用户角色");
//			menuNodes.add(root);
			for (Object o : list) {
				Role r = (Role) o;
				Map m = new HashMap();
				m.put("id", r.getJsdh());
				m.put("name", r.getJsmc());
				m.put("pid", r.getType());
				boolean b = this.roleManager.getRoleTreeIsParent(r.getJsdh());
			    m.put("isParent", b);
				menuNodes.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuNodes;
	}

	private Role getRoleEntrity(HttpServletRequest request) throws Exception{
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		String roles = userSession.getSysuser().getRoles();
		Role role = null;
		if (StringUtils.isBlank(roles)) {
			throw new Exception("当前用户角色未分配，请联系管理员！");
		} else {
			String[] roleArray = roles.split(",");
			if (roleArray.length > 1) {
				List<Role> rlist = roleManager.queryRoles(roles);
				role = getSuperRole(rlist);
			} else {
				role = roleManager.getRoleById(roles);
			}
		}
		return role;
	}
	
	/**
	 * 获最大级别角色
	 * 
	 * @param list
	 * @return
	 */
	private Role getSuperRole(List list) throws Exception {
		Role dRole = null;
		if (list.size() > 0){
			dRole = (Role) list.get(0);
			if("1".equals(dRole.getJsjb())) 
				return dRole;
			for (int i = 0; i < list.size(); i++) {
				Role role = (Role) list.get(i);
				if (dRole.getJsjb().equals(role.getJsjb())
						&& "1".equals(role.getJssx())) {
					dRole = role;
				}
			}
		}
		return dRole;
	}

	/**
	 * 查询当前在线用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public Object online(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ServletContext application = request.getSession()
					.getServletContext();
			//HashMap map = (HashMap) application.getAttribute("onlines");
			Map map = OnlineUserListener.getUsers();
			LinkedList list = new LinkedList();
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				SysUser s = (SysUser) e.getValue();
				list.add(s);
			}
			request.setAttribute("queryList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "query/onlines";
	}

	/**
	 * 
	 * 函数功能说明：查询同一部门的用户 修改日期 2013-6-28
	 * 
	 * @param glbm
	 * @return
	 * @return List
	 */
	@RequestMapping
	@ResponseBody
	public List getUserListForGlbm(@RequestParam("glbm") String glbm) {

		List listPolice = null;
		;
		try {
			listPolice = sysUserManager.getUserListForGlbm(glbm);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listPolice;
	}

	// 构造警员树
	@RequestMapping
	@ResponseBody
	public List<Map<String, Object>> getPoliceTree(
			@RequestParam("glbm") String glbm) {
		List<Map<String, Object>> menuNodes = new ArrayList<Map<String, Object>>();

		Map map = new HashMap();
		map.put("glbm", glbm);
		List list = this.prefectureManager.getDepartmentTree(map);
		if (list.size() > 0) {
			Iterator<Object> it = list.iterator();
			while (it.hasNext()) {
				Map m = (Map) it.next();
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", m.get("glbm").toString());
				item.put("name", m.get("bmmc").toString().replaceAll(" ", ""));
				item.put("pId", m.get("sjbm").toString());
				item.put("nocheck", "true");
				menuNodes.add(item);
			}
		}

		try {
			List listPolice = sysUserManager.getUserListForGlbm(glbm);
			for (Object o : listPolice) {
				SysUser user = (SysUser) o;
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", user.getYhdh());
				item.put("name", user.getYhmc());
				item.put("pId", user.getGlbm());
				item.put("check", "{enable: true}");
				menuNodes.add(item);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuNodes;
	}

	@RequestMapping(params = "method=syncSysUser", method = RequestMethod.POST)
	@ResponseBody
	public Map syncSysUser(HttpServletRequest request,
			HttpServletResponse response) {
		Map result = null;
		try {
			int i = this.sysUserManager.syncUser();
			if (i < 0) {
				result = Common.messageBox("同步失败！", "0");
			} else {
				i = this.systemManager.loadUser();
				if (i < 0)
					result = Common.messageBox("同步失败！", "0");
				else {
					result = Common.messageBox("同步成功！", "1");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping
	public ModelAndView queryPass(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			request.setAttribute("yhdh", userSession.getSysuser().getYhdh());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("manager/passManager");
	}

	@RequestMapping
	public ModelAndView toGrxx(HttpServletRequest request,
								  HttpServletResponse response) {
		try {
			UserSession userSession = (UserSession) WebUtils
					.getSessionAttribute(request, "userSession");
			request.setAttribute("yhdh", userSession.getSysuser().getYhdh());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("manager/editGrxx");
	}
	/**
	 * 将 已/未授权的用户信息 导入到excel
	 * @param request
	 * @param response
	 * @return url
	 */
	@ResponseBody
	@RequestMapping
	public String outPutSysuser(HttpServletRequest request, HttpServletResponse response,SysUser command) {
		String filename = "";
		try {
			String path = request.getSession().getServletContext().getRealPath("/")+"/download/";
			Map<String, Object> conditions  = Common.getParamentersMap(request);
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
			String dwdm = userSession.getSysuser().getGlbm();
			conditions.put("dwdm", dwdm);
			
			if(conditions.containsKey("hphm")){
				String hphm = java.net.URLDecoder.decode(conditions.get("hphm").toString(),"utf-8");
				conditions.remove("hphm");
				conditions.put("hphm", hphm);
			}
			filename = this.sysUserManager.outPutUserList(conditions,path,command);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename;
	}
	@ResponseBody
	@RequestMapping
	public Map  getPoliceInfo(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			SysUser command) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		CommController co=new CommController();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("curPage", page); // 页数
		map.put("pageSize", rows); // 每页显示行数
		String jh=command.getJh();
		String sfzhm=command.getSfzmhm();
		String yhmc=command.getYhmc();
		Map result = new HashMap();
		try {
			result = this.sysUserManager.getPoliceInfo(map, command);
		} catch (Exception e) {
			e.printStackTrace();
		}
			return result;
	}
	@RequestMapping()
	@ResponseBody
	public SysUser getpolice(HttpServletRequest request,
			HttpServletResponse response) {
		List<SysUser> list = new ArrayList<SysUser>();
		SysUser sysuser = new SysUser();
		try {
			String user_realname = request.getParameter("USER_REALNAME");
			user_realname = new String(user_realname.getBytes("iso-8859-1"), "UTF-8");
			String user_name = request.getParameter("USER_NAME");
			String user_idcard =request.getParameter("USER_IDCARD");
			list = this.sysUserManager.getKcPolice(user_name, user_idcard, user_realname);
			sysuser = list.get(0);
			sysuser.setMm("888888");
			sysuser.setIpks("1.1.1.1");
			sysuser.setIpjs("255.255.255.255");
			sysuser.setZt("1");
			sysuser.setBz("JYXX");
			sysuser.setDzzs("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*Policess pol=new Policess();
				pol=(Policess) list.get(0);
				sysuser.setSfzmhm(pol.getUSER_IDCARD());
				sysuser.setJh(pol.getUSER_NAME());
				sysuser.setYhdh(pol.getUSER_NAME());
				sysuser.setYhmc(pol.getUSER_REALNAME());
				sysuser.setLxdh2(pol.getUSER_WORKTEL());
				sysuser.setLxdh1(pol.getUSER_MOBILETEL1());
				sysuser.setBmmc(pol.getORGNAME());
				
				sysuser.setGlbm(pol.getORGCODE());*/
		return sysuser;
	}
	@RequestMapping()
	@ResponseBody
	public int getpoliceNum(HttpServletRequest request,
			HttpServletResponse response) {
		List<SysUser> list=new ArrayList<SysUser>();
		int res = 0;
		try {
			String user_realname = request.getParameter("USER_REALNAME");
			String user_name = request.getParameter("USER_NAME");
			String user_idcard =request.getParameter("USER_IDCARD");
			list = this.sysUserManager.getKcPolice(user_name,user_idcard,user_realname);
			res = list.size();
			if(res == 1) {
				SysUser user = new SysUser();
				user = list.get(0);
				List<Department> deps = departmentManager.getKcBmmc(user.getJh());
				if(deps.size() <= 0)
					res = 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	//删除警员信息
	
	@RequestMapping()
	@ResponseBody
	public Object updatePolice(HttpServletRequest request,
			HttpServletResponse response, SysUser user) {
		String Syyh=request.getParameter("polid"); 
		System.out.println("开始删除数据");
			int i;
			try {
				i = this.sysUserManager.updatePolice(Syyh);
				if(i>0){
					System.out.println("删除成功");
				return Common.messageBox("删除成功！", "1");}else{
				return Common.messageBox("程序异常！!", "0");}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Common.messageBox("程序异常！!", "0");
			}
	}
}
