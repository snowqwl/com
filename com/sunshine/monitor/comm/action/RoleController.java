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
import com.sunshine.monitor.comm.bean.RoleMenu;
import com.sunshine.monitor.comm.log.LogHandlerInter;
import com.sunshine.monitor.comm.log.LogHandlerParam;
import com.sunshine.monitor.comm.log.LogInvocationHandler;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.service.MenuManager;
import com.sunshine.monitor.comm.service.RoleManager;
import com.sunshine.monitor.comm.service.RoleMenuManager;

/**
 * 
 * @author YANGYANG
 * 
 */
@Controller
@RequestMapping(value = "/role.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RoleController {

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
	public Object getRoleList(HttpServletRequest request
			) throws Exception {
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
	 * Adding Role
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping
	public void insertRole(@ModelAttribute("role") Role role, HttpServletResponse response) {
		String code = null;
		PrintWriter out = null ;
		try {
			// Role attribute
			role.setJssx("123456");
			boolean flag = this.roleManager.insertRole(role);
			code = (flag) ? "1" : "0";
			out = response.getWriter();
		} catch (Exception e) {
			code = "0";
			e.printStackTrace();
		}
		out.print("{\"code\":\""+code+"\"}");
		out.flush();
		out.close();
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
	public void batchDeleteroles(@RequestParam String ids,
			HttpServletResponse response) {
		try {
			StringTokenizer token = new StringTokenizer(ids, ",");
			List<String> list = new ArrayList<String>();
			while (token.hasMoreElements()) {
				String id = (String) token.nextElement();
				list.add(id);
			}
			boolean flag = this.roleManager.batchDeleteRoles(list
					.toArray(new String[] {}));
			PrintWriter out = response.getWriter();
			out.print((flag) ? "1" : "0");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save Authory
	 * 
	 * @param jsid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView saveRoleAuthory(@RequestParam String jsid,
			HttpServletRequest request, HttpServletResponse response) {
		String[] menuids = request.getParameterValues("rolemenu");
		List<RoleMenu> list = new ArrayList<RoleMenu>();
		for (String ids : menuids) {
			String[] temp = ids.split("_");
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setJsdh(jsid);
			roleMenu.setCxdh(temp[1]);
			roleMenu.setParentid(temp[0]);
			list.add(roleMenu);
		}
		try {
			this.roleMenuManager.batchInsertRoleMenu(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(this.roleMain);
	}

	/**
	 * Query a list of menus according to roleid(jsdh)
	 * 
	 * @param jsdh
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object getMenuListByJsdh(
			@RequestParam String jsdh,
			HttpServletRequest request, HttpServletResponse response) {
		List<RoleMenu> list = null;
		try {
			list = this.roleMenuManager.getRoleMenuList(jsdh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 异步更新角色资源
	 * @param jsdh
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object configRole(@RequestParam String jsdh,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String,String> result = new HashMap<String,String>();
		if("".equals(jsdh) || jsdh == null){
			result.put("msg", "请先给用户分配角色!");
			return result;
		}
		/*确认还是取消*/
		String czlx = request.getParameter("czbz");
		/*资源编号*/
		String id = request.getParameter("id");
		/*资源编号*/
		String pid = request.getParameter("pid");
		
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setJsdh(jsdh);
		roleMenu.setCxdh(id);
		roleMenu.setParentid(pid);
		
		try {
			/*添加资源*/
			if("true".equals(czlx)){
				//int r1 = this.roleMenuManager.addRoleMenu(roleMenu);
				
				LogInvocationHandler proxy = new LogInvocationHandler(logHandlerInter); 
				LogHandlerParam logParam = new LogHandlerParam();
				logParam.setType(OperationType.ROLE_MENU_BATCH_ADD.getType());//日志类型
				logParam.setDescription(OperationType.ROLE_MENU_BATCH_ADD.getDesc());//日志描述
				logParam.setOperationMethod("addRoleMenu");//执行方法
				RoleMenuManager manager = (RoleMenuManager) proxy.bind(this.roleMenuManager,logParam,request);
				int r1=manager.addRoleMenu(roleMenu);
				
				if(r1 == 1){
					result.put("msg", "添加角色授权成功！");
				}
			} else if("false".equals(czlx)){
				//int r2 = this.roleMenuManager.deleteRoleMenu(roleMenu);
				LogInvocationHandler proxy = new LogInvocationHandler(logHandlerInter); 
				LogHandlerParam logParam = new LogHandlerParam();
				logParam.setType(OperationType.ROLE_MENU_BATCH_CANCEL.getType());//日志类型
				logParam.setDescription(OperationType.ROLE_MENU_BATCH_CANCEL.getDesc());//日志描述
				logParam.setOperationMethod("deleteRoleMenu");//执行方法
				RoleMenuManager manager = (RoleMenuManager) proxy.bind(this.roleMenuManager,logParam,request);
				int r2=manager.deleteRoleMenu(roleMenu);
				if(r2 == 1){
					result.put("msg", "取消角色授权成功！");
				}
			} else {
				
				result.put("msg", "非法操作!");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "程序异常！");
			return result ;
		}
		return result ;
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
	public void updateRole(@ModelAttribute Role role, HttpServletResponse response) {
		boolean flag = false;
		String code = null;
		PrintWriter out = null ;
		try {
			flag = this.roleManager.updateRole(role);
			code = (flag) ? "1" : "0";
			out = response.getWriter();
		} catch (Exception e) {
			code = "0";
			e.printStackTrace();
		}
		out.print("{\"code\":\""+code+"\"}");
		out.flush();
		out.close();
	}
	
	/**
	 * 权限登陆显示
	 */
	@RequestMapping("/sysAuthoInfo.do")
	public String index55555() {
		return "manager/authoinfolist";
	}
	
	/**
	 * 根据角色展示权限菜单树,并自动勾选
	 */
	/*
	@RequestMapping("/getAllMenuByRole.do")
	@ResponseBody
	public List getAllMenuByRole(String roleid) throws Exception {
		String menuUrls = this.menuManager.findMenuUrlsForString(roleid);
		
		List<Map<String,Object>> menuNodes = new ArrayList<Map<String,Object>>();
		List list = this.menuManager.findMenuList();
		if(list.size()>0) {
			Iterator<Object> it = list.iterator();
			while(it.hasNext()){
				Map m = (Map)it.next();
				Map<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", m.get("id").toString());
				item.put("name", m.get("name").toString().replaceAll(" ",""));
				item.put("pId", m.get("pid").toString());
				
				if(!(menuUrls==null || menuUrls.equals(""))) {
					String[] arrayNodes = menuUrls.split(",");
					for(int i=0;i<arrayNodes.length;i++){
						if(arrayNodes[i].equals(m.get("id").toString())){
							item.put("checked", true);
						}
					}
				}
				
				menuNodes.add(item);
			}
		}
		return menuNodes;

		//JSONArray jsonArray = JSONArray.fromObject(menuNodes);
		//response.getWriter().print(jsonArray.toString());
		//response.getWriter().close();
	}
	*/
}
