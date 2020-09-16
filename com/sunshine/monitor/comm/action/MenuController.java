package com.sunshine.monitor.comm.action;

import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
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

import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.service.MenuManager;
import com.sunshine.monitor.comm.util.MenuSort;
import com.sunshine.monitor.comm.util.MenuTrees;

@Controller
@RequestMapping(value = "/menu.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MenuController {

	@Autowired
	private MenuManager menuManager;

	private String menuList = "manager/menulist";

	/**
	 * Forward
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView(this.menuList);
	}

	/**
	 * Query menu list
	 * @param request
	 * @param rows
	 * @param page
	 * @param sort
	 * @param order
	 * @param name
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping
	public Object getMenuList(HttpServletRequest request,
			@RequestParam int rows, @RequestParam int page,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String order,
			@RequestParam(required = false) String name, @RequestParam(required = false) String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("rows", rows);
		params.put("page", page);
		params.put("sort", sort);
		params.put("order", order);
		params.put("id", id) ;
		Map<String, Object> result = this.menuManager
				.findMenuListByPage(params);
		return result;
	}
	
	/**
	 * Query menu Directory
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getMenuDirectory(HttpServletRequest request, HttpServletResponse response){
		try {
			List<Menu> list = this.menuManager.queryMenuDirectory();
			String pid = request.getParameter("pid");
			String p_id = request.getParameter("id");
			StringBuffer optionStr = new StringBuffer(50);
			if("null".equals(pid)) {
				String name = null;
				for(Menu menu : list){
					if(menu.getId().equals(p_id)) {
						name = menu.getName();
					}
				}
				optionStr.append("<option value=''>"+name+"</option>");
			} else {
				for(Menu menu : list){
					String id = menu.getId();
					String isSelected = "";
					if(id.equals(pid)){
						isSelected = "selected";
					}
					optionStr.append("<option value=\""+menu.getId()+"\" "+isSelected+">"+menu.getName()+"</option>");
				}
			}
			PrintWriter out = response.getWriter();
			out.print(optionStr.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a new menu
	 * @param menu
	 * @param response
	 */
	@RequestMapping
	public void insertMenu(@ModelAttribute("menu") Menu menu, HttpServletResponse response){
		String flag = "0" ;
		PrintWriter out = null ;
		try {
			out = response.getWriter();
			String lb = menu.getLb();
			// 设置菜单级别(目前支持两级)
			if(MenuSort.MENU_DIRECTORY.getCode().equals(lb))
				menu.setCxsx("1") ;
			else
				menu.setCxsx("2") ;
			if(this.menuManager.insertMenu(menu)) flag = "1" ;
			out.print(flag);
		} catch (Exception e) {
			out.print(flag);
			e.printStackTrace();
		}finally{
			//out.flush();
			out.close();			
		}
	}
	
	/**
	 * Batch delete menus
	 * @param ids
	 * @param response
	 */
	@Deprecated
	@RequestMapping
	public void batchDeleteMenus(@RequestParam String ids, HttpServletResponse response){
		try {
			StringTokenizer token = new StringTokenizer(ids,",");
			List<String> list = new ArrayList<String>();
			while(token.hasMoreElements()){
				String id = (String)token.nextElement();
				list.add(id) ;
			}
			boolean flag = this.menuManager.batchDeleteMenus(list.toArray(new String[]{}));
			PrintWriter out = response.getWriter();
			out.print((flag)?"1":"0");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Query a menu
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping
	public Object queryMenuById(HttpServletRequest request,
			@RequestParam String id) throws Exception {
		Menu menu = this.menuManager.findMenu(id);
		return menu;
	}
	
	/**
	 * Delete menu
	 * {"code":"1","msg":"成功"}
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping
	public Object deleteMenu(@RequestParam String id,@RequestParam String lb, HttpServletResponse response){
		String msg = null ;
		String code = null ;
		Map<String, String> params = new HashMap<String, String>();
		try {
			boolean flag = false ;
			if(MenuSort.MENU_ITEM.getCode().equals(lb)){
				flag = this.menuManager.deleteMenu(id);
				msg = "菜单删除成功" ;
			}else if(MenuSort.MENU_DIRECTORY.getCode().equals(lb)){
				List<Menu> list = this.menuManager.findMenuListByPid(id);
				if(list.size()>1){
					msg = "该目录存在子菜单，先删除其子菜单！";
				}else{
					flag = this.menuManager.deleteMenu(id);
					msg = "目录删除成功" ;
				}
			}
			code = (flag)?"1" : "0" ;
		} catch (Exception e) {
			code = "0";
			msg = "执行失败!" ;
			e.printStackTrace();
		}
		params.put("code", code);
		params.put("msg", msg);
		return params;
	}
	
	@RequestMapping
	public void updateMenu(@ModelAttribute Menu menu, HttpServletResponse response){
		String msg = null ;
		String code = null ;
		boolean flag = false ;
		String result = null;
		PrintWriter out = null ;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			out = response.getWriter();
			// Old information
			Menu oldMenu = this.menuManager.findMenu(menu.getId());
			String oldLb= oldMenu.getLb();
			String newLb = menu.getLb();
			// 原来是菜单项
			if(MenuSort.MENU_ITEM.getCode().equals(oldLb)){
				if(MenuSort.MENU_DIRECTORY.getCode().equals(newLb)){
					// 菜单升级到目录（可行）
					menu.setPid(menu.getId());
					menu.setLb(MenuSort.MENU_DIRECTORY.getCode());
					msg = "菜单升级到目录更新{0}！";
				}else{
					msg = "菜单更新{0}！" ;
				}
				flag = this.menuManager.updateMenu(menu);
			// 原来是目录
			}else{
				if(MenuSort.MENU_DIRECTORY.getCode().equals(newLb)){
					//menu.setPid(menu.getId());
					flag = this.menuManager.updateMenu(menu);
					msg = "目录更新{0}！";
				}else{
					// 目录降级到菜单
					List<Menu> list = this.menuManager.findMenuListByPid(menu.getId());
					if(list.size()>1){
						msg = "该目录存在子菜单,不能直接更新,更新{0}！";
					}else{
						flag = this.menuManager.updateMenu(menu);
						msg = "目录降级到菜单更新{0}" ;
					}
				}
			}
			if(flag){
				result = MessageFormat.format(msg,new Object[]{"成功"});
				code = "1" ;
			}else{
				result = MessageFormat.format(msg,new Object[]{"失败"});
				code = "0" ;
			}
		} catch (Exception e) {
			code = "0";
			msg = "执行{0}!" ;
			e.printStackTrace();
		}
		out.print("{\"code\":\""+code+"\",\"msg\":\""+result+"\"}");
		out.flush();
		out.close();
	}
	
	@ResponseBody
	@RequestMapping
	public List<Menu> queryAllMenus() {
		List<Menu> c = null;
		try {
			//c = MenuTrees.getInstance().getMenuMap().values();
			c = this.menuManager.findMenuList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
}