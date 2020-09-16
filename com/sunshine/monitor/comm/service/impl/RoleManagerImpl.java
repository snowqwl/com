package com.sunshine.monitor.comm.service.impl;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.dao.RoleDao;
import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.service.RoleManager;

@Transactional
@Service
public class RoleManagerImpl implements RoleManager {
	
	@Autowired
	private RoleDao roleDao;
	
	public Map<String, Object> findRoleListByPage(Map<?, ?> condition)
			throws Exception {
		return this.roleDao.findRoleListByPage(condition);
	}
	@OperationAnnotation(type=OperationType.ROLE_ADD,description="角色新增")
	public boolean insertRole(Role role) throws Exception {
		return this.roleDao.insertRole(role);
	}
	
	@OperationAnnotation(type=OperationType.ROLE_DELETE,description="角色删除")
	public boolean batchDeleteRoles(String[] jsdhs) throws Exception{
		return this.roleDao.batchDeleteRoles(jsdhs);
	}

	public Role getRoleById(String jsdh)throws Exception{
		return this.roleDao.getRoleById(jsdh);
	}
	
	@OperationAnnotation(type=OperationType.ROLE_MODIFY,description="角色修改")
	public boolean updateRole(Role role) throws Exception {
		return this.roleDao.updateRole(role);
	}
	
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public List<Role> queryRoles(String param) throws Exception {
		StringTokenizer st = new StringTokenizer(param, ",");
		StringBuffer sb = new StringBuffer();
		sb.append(" and jsdh in(");
		while(st.hasMoreTokens()){
			String role = st.nextToken();
			sb.append("'")
			.append(role)
			.append("'");
			if(st.hasMoreTokens()){
				sb.append(",");
			}
		}
		sb.append(")")
		.append(" order by jsjb asc");
		return this.roleDao.queryRoles(sb.toString());
	}
	@Override
	public boolean getRoleTreeIsParent(String param) throws Exception {
		return this.roleDao.getRoleTreeIsParent(param); 
	}
	
	@Override
	public List<Role> getRoleListByType(String sb) throws Exception {
		return this.roleDao.queryRoles(sb.toString());
	}
}
