package com.sunshine.monitor.comm.dao;

import java.util.List;

import com.sunshine.monitor.comm.bean.RoleMenu;

public interface RoleMenuDao extends BaseDao{

	/**
	 * query
	 * @param roleMenu
	 * @return
	 */
	public RoleMenu getRoleMenu(RoleMenu roleMenu) throws Exception;
	
	
	/**
	 * batch insert RoleMenu object
	 * @param list
	 * @return
	 */
	public boolean batchInsertRoleMenu(List<RoleMenu> list) throws Exception;
	
	
	/**
	 * jsdh=roleid
	 * @param jsdh
	 * @return
	 */
	public List<RoleMenu> getRoleMenuList(String jsdh) throws Exception;
	
	/**
	 * 
	 * @param roleMenu
	 * @return
	 * @throws Exception
	 */
	public int addRoleMenu(RoleMenu roleMenu) throws Exception;
	
	/**
	 * 
	 * @param roleMenu
	 * @return
	 * @throws Exception
	 */
	public int deleteRoleMenu(RoleMenu roleMenu) throws Exception;
	
	/**
	 * 
	 * @param roleMenu
	 * @return
	 * @throws Exception
	 */
	public List<RoleMenu> queryParenRoleMenu(RoleMenu roleMenu) throws Exception;
	
	
	/**
	 * 根据用户账号查询所有菜单编号列表
	 * @param yhdh 用户账号
	 * @return
	 * @throws Exception
	 */
	public List<String> queryMenusByRole(String yhdh) throws Exception;
}
