package com.sunshine.monitor.comm.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Role;

public interface RoleManager {
	
	public Map<String, Object> findRoleListByPage(Map<?, ?> condition) throws Exception ;	

	public boolean insertRole(Role role) throws Exception;

	public boolean batchDeleteRoles(String[] jsdhs) throws Exception;
	
	public Role getRoleById(String jsdh)throws Exception ;
	
	public boolean updateRole(Role role) throws Exception ;
	
	public List<Role> queryRoles(String param) throws Exception;
	
	/**
	 * 查询角色树是否有下级
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean getRoleTreeIsParent(String param) throws Exception;
	
	public List<Role> getRoleListByType(String param) throws Exception;
	
}
