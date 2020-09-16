package com.sunshine.monitor.comm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.RoleMenu;
import com.sunshine.monitor.comm.dao.RoleMenuDao;
import com.sunshine.monitor.comm.service.RoleMenuManager;

@Transactional
@Service
public class RoleMenuManagerImpl implements RoleMenuManager {

	@Autowired
	private RoleMenuDao roleMenuDao;
	
	public boolean batchInsertRoleMenu(List<RoleMenu> list) throws Exception {
		return this.roleMenuDao.batchInsertRoleMenu(list);
	}

	public RoleMenu getRoleMenu(RoleMenu roleMenu) throws Exception {
		return this.roleMenuDao.getRoleMenu(roleMenu);
	}
	
	public List<RoleMenu> getRoleMenuList(String jsdh) throws Exception {
		return this.roleMenuDao.getRoleMenuList(jsdh);
	}
	
	public int addRoleMenu(RoleMenu roleMenu) throws Exception{
		return this.roleMenuDao.addRoleMenu(roleMenu);
	}

	public int deleteRoleMenu(RoleMenu roleMenu) throws Exception {
		return this.roleMenuDao.deleteRoleMenu(roleMenu);
	}

	public List<String> queryMenusByRole(String yhdh) throws Exception {
		return this.roleMenuDao.queryMenusByRole(yhdh);
	}
}
