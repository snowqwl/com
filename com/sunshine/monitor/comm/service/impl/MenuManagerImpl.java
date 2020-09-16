package com.sunshine.monitor.comm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.dao.MenuDao;
import com.sunshine.monitor.comm.service.MenuManager;
import com.sunshine.monitor.comm.util.MenuTrees;

@Transactional
@Service
public class MenuManagerImpl implements MenuManager {
	
	@Autowired
	private MenuDao menuDao;

	public List<Menu> findMenuList() throws Exception {
		return this.menuDao.findMenuList();
	}
	
	public List<Menu> findTopMenuList(String role) throws Exception {
		List<Menu> listTopMenus =  this.menuDao.findTopMenuList();
		List<Menu> list = new ArrayList<Menu>();
		List<Menu> listSubMenus = new ArrayList<Menu>();
		for(Menu menu : listTopMenus) {
			listSubMenus = findMenuListByRoles(role, menu.getId());
			if (listSubMenus.size() > 0) {
				list.add(menu);
			}
		}
		return list;
	}

	public List<Menu> findMenuListByRoles(String roles,String id) throws Exception {
		return this.menuDao.findMenuListByRoles(roles,id);
	}
	/*public String findMenuUrlsForString(String roleId) throws Exception {
		return this.menuDao.findMenuUrlsForString(roleId);
	}*/
	
	public Menu findMenu(String cxdh) throws Exception {
		return this.menuDao.findMenu(cxdh);
	}

	public List<Menu> findMenuListByPid(String cxdh) throws Exception {
		return this.menuDao.findMenuListByPid(cxdh);
	}

	public Map<String, Object> findMenuListByPage(Map<?, ?> condition) throws Exception{
		return this.menuDao.findMenuListByPage(condition);
	}
	
	public boolean insertMenu(Menu menu) throws Exception{
		boolean flag = false ;
		if(this.menuDao.insertMenu(menu)){
			MenuTrees.getInstance().addMenu(menu);
			flag = true ;
		}
		return flag ;
	}
	
	public boolean deleteMenu(String id) throws Exception{
		boolean flag = false ;
		if(this.menuDao.deleteMenu(id)){
			MenuTrees.getInstance().removeMenu(id);
			flag = true ;
		}
		
		return flag;
	}

	public boolean updateMenu(Menu menu) throws Exception {
		boolean flag = false ;
		if(this.menuDao.updateMenu(menu)){
			MenuTrees.getInstance().updateMenu(menu);
			flag = true ;
		}
		return flag;
	}	
	
	public List<Menu> findMenuListByRoles(String roles) throws Exception{
		return this.menuDao.findMenuListByRoles(roles);
	}
	
	
	public boolean batchDeleteMenus(String[] ids) throws Exception{
		return this.menuDao.batchDeleteMenus(ids);
	}
	
	public List<Menu> queryMenuDirectory() throws Exception{
		return this.menuDao.queryMenuDirectory();
	}
	
	public void initMenuTree() throws Exception {
		this.menuDao.initMenuTree();		
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

}
