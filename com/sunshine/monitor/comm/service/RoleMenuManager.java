package com.sunshine.monitor.comm.service;

import java.util.List;

import com.sunshine.monitor.comm.bean.RoleMenu;

public interface RoleMenuManager {
	
	public RoleMenu getRoleMenu(RoleMenu roleMenu) throws Exception;
	
	public boolean batchInsertRoleMenu(List<RoleMenu> list) throws Exception;
	
	public List<RoleMenu> getRoleMenuList(String jsdh) throws Exception ;

	public int addRoleMenu(RoleMenu roleMenu) throws Exception;
	
	public int deleteRoleMenu(RoleMenu roleMenu) throws Exception;
	
	public List<String> queryMenusByRole(String yhdh) throws Exception ;
}
