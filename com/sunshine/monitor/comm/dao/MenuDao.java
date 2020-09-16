package com.sunshine.monitor.comm.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.util.CompositeComponent;

public interface MenuDao extends BaseDao{
	
	/**
	 * Building Menu Tree
	 * @return
	 */
	public void initMenuTree() throws Exception;
	
	/**
	 * Query all of Menus
	 * @return
	 */
	public List<Menu> findMenuList() throws Exception;
	
	/**
	 * @Description:query top menu
	 * @author: TDD
	 * @date: 2014-9-12 下午01:55:43
	 */
	public List<Menu> findTopMenuList() throws Exception;
	
	public List<Menu> findMenuListByRoles(String roles,String id) throws Exception ;
	
	//public String findMenuUrlsForString(String roleId) throws Exception;
	
	/**
	 * Query a parent menu by cxdh
	 * @param id
	 * @return
	 */
	public Menu findMenu(String id)throws Exception;
	
	/**
	 * Query submenu list
	 * @param id
	 * @return
	 */
	public List<Menu> findMenuListByPid(String id) throws Exception;
	

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Menu> queryMenuDirectory() throws Exception;
	
	
	/**
	 * Pager
	 * @param rows
	 * @param page
	 * @param condition
	 * @return
	 */
	public Map<String,Object> findMenuListByPage(Map<?,?> condition) throws Exception;
	
	/**
	 * Add new Menu object
	 * @param menu
	 * @return
	 */
	public boolean insertMenu(Menu menu) throws Exception;
	
	/**
	 * Delete a menu object
	 * @param id
	 * @return
	 */
	public boolean deleteMenu(String id) throws Exception;
	
	/**
	 * Speration is ","
	 * @param ids
	 */
	public boolean batchDeleteMenus(String[] ids) throws Exception;
	
	/**
	 * Update Menu
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public boolean updateMenu(Menu menu) throws Exception;
	
	
	public List<Map<String,Object>> getIndexMenuPower(String filter) throws Exception;
	
	public List<Menu> findMenuListByRoles(String roles) throws Exception ;
}
