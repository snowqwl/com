package com.sunshine.monitor.comm.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Menu;

public interface MenuManager {
	
	public void initMenuTree() throws Exception ;
	
	public List<Menu> findMenuList() throws Exception ;
	
	public List<Menu> findTopMenuList(String role) throws Exception;

	public List<Menu> findMenuListByRoles(String roles, String id) throws Exception ;
	//public String findMenuUrlsForString(String roleId) throws Exception;
	
	public Menu findMenu(String id) throws Exception ;

	public List<Menu> findMenuListByPid(String id) throws Exception ;
	
	public List<Menu> queryMenuDirectory() throws Exception;
	
	public Map<String,Object> findMenuListByPage(Map<?,?> condition) throws Exception;
	
	public boolean insertMenu(Menu menu) throws Exception;
	
	public boolean deleteMenu(String id) throws Exception;
	
	public boolean batchDeleteMenus(String[] ids) throws Exception;
	
	public boolean updateMenu(Menu menu) throws Exception ;
	
	public List<Menu> findMenuListByRoles(String roles) throws Exception;
	
}
