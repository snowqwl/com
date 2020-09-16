package com.sunshine.monitor.comm.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.log.LogHandlerInter;
import com.sunshine.monitor.comm.service.MenuManager;
import com.sunshine.monitor.comm.service.RoleManager;
import com.sunshine.monitor.comm.service.RoleMenuManager;

/**
 * @author huanghaip
 * 解决角色授权报404错误
 */
@Controller
@RequestMapping(value="/userRole.do",params="method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserRoleController {
	
	@Autowired
	private RoleManager roleManager;

	@Autowired
	private RoleMenuManager roleMenuManager;
	
	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	private LogHandlerInter logHandlerInter;
	
	private String roleMain = "manager/rolelist";
	
	private String roleWin = "manager/rolelistWin";
	
	/**
	 * 跳转到角色主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Role> list = this.roleManager.getRoleListByType(" and type=100 order by jsjb ");
		request.setAttribute("typeList", list);	
		return new ModelAndView(this.roleMain);
	}
	
	@RequestMapping
	public ModelAndView forwardWin(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");
		String jsdh = request.getParameter("jsdh");
		List<Role> list = this.roleManager.getRoleListByType(" and type=100 order by jsjb ");
		request.setAttribute("type", type);
		request.setAttribute("jsdh", jsdh);
		request.setAttribute("typeList", list);		
		return new ModelAndView(this.roleWin);
	}
	
	/**
	 * Get a list of Role
	 * 
	 * @param request
	 * @param rows
	 * @param page
	 * @param sort
	 * @param order
	 * @param jsdh
	 * @param jsmc
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping
	public Object getRoleList(HttpServletRequest request) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String rows=request.getParameter("rows");
		String page=request.getParameter("page");
		String sort=request.getParameter("sort");
		String order=request.getParameter("order");
		String jsdh=request.getParameter("jsdh");
		String jsmc=request.getParameter("jsmc");
		String cxdh=request.getParameter("cxdh");
		
		params.put("jsdh", jsdh);
		params.put("jsmc", jsmc);
		params.put("cxdh", cxdh);
		params.put("rows", rows);
		params.put("page", page);
		params.put("sort", sort);
		params.put("order", order);
		Map<String, Object> result = this.roleManager
				.findRoleListByPage(params);
		return result;
	}
	
	/**
	 * Update a role object
	 * 
	 * @param role
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> updateRole(@ModelAttribute Role role, HttpServletResponse response) {
		boolean flag = false;
		String code = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			flag = this.roleManager.updateRole(role);
			code = (flag) ? "1" : "0";
		} catch (Exception e) {
			code = "0";
			e.printStackTrace();
		}
		map.put("code", code);
		return map;
	}
	
	/**
	 * Adding Role
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> insertRole(@ModelAttribute("role") Role role, HttpServletResponse response) {
		String code = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			role.setJssx("123456");
			boolean flag = this.roleManager.insertRole(role);
			code = (flag) ? "1" : "0";
		} catch (Exception e) {
			code = "0";
			e.printStackTrace();
		}
		map.put("code", code);
		return map;
	}
	
	/**
	 * Query a role object according to roleid(jsdh)
	 * 
	 * @param jsdh
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getRoleById(@RequestParam String jsdh,
			HttpServletRequest request, HttpServletResponse response) {
		Role role = null;
		try {
			role = this.roleManager.getRoleById(jsdh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role;
	}
	
	/**
	 * Batch delete roles
	 * 
	 * @param ids
	 * @param response
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> batchDeleteroles(@RequestParam String ids,
			HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String code = "0";
		try {
			StringTokenizer token = new StringTokenizer(ids, ",");
			List<String> list = new ArrayList<String>();
			while (token.hasMoreElements()) {
				String id = (String) token.nextElement();
				list.add(id);
			}
			boolean flag = this.roleManager.batchDeleteRoles(list
					.toArray(new String[] {}));
			code = (flag) ? "1" : "0";
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("code", code);
		return map;
	}
}
