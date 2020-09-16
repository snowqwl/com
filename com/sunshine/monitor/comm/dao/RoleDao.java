package com.sunshine.monitor.comm.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.Role;

public interface RoleDao extends BaseDao {
	/**
	 * Role list 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findRoleListByPage(Map<?, ?> condition) throws Exception ;	

	/**
	 * Add new role object
	 * @param role
	 * @return
	 */
	public boolean insertRole(Role role) throws Exception;
	
	/**
	 * Batch delete role
	 * @param jsdhs
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteRoles(String[] jsdhs) throws Exception;
	
	/**
	 * Get single role
	 * @param jsdh
	 * @return
	 */
	public Role getRoleById(String jsdh)throws Exception ;
	
	/**
	 * Update Role
	 * @param role
	 * @return
	 */
	public boolean updateRole(Role role) throws Exception ;
	
	
	/**
	 * 
	 * @param subSql
	 * @return
	 * @throws Exception
	 */
	public List<Role> queryRoles(String subSql) throws Exception;
	
	/**
	 * 查询角色树是否有下级
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean getRoleTreeIsParent(String param) throws Exception;
	
}
