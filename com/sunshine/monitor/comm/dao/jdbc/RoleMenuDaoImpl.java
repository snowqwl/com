package com.sunshine.monitor.comm.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.RoleMenu;
import com.sunshine.monitor.comm.dao.RoleMenuDao;

@Repository
public class RoleMenuDaoImpl extends BaseDaoImpl implements RoleMenuDao {
	
	private Logger log = LoggerFactory.getLogger(RoleMenuDaoImpl.class);

	public RoleMenu getRoleMenu(RoleMenu roleMenu) throws Exception{
		String sql = "SELECT /*+FIRST_ROWS(1)*/ JSDH,CXDH,PARENTID FROM JM_ROLEMENU  WHERE JSDH=? AND CXDH= ?";
		List<RoleMenu> list = this.queryList(sql, RoleMenu.class,roleMenu.getJsdh(),roleMenu.getCxdh());
		if(list.size()==0){
			return null ;
		}
		return list.get(0);
	}
	
	public boolean batchInsertRoleMenu(List<RoleMenu> list) throws Exception{
		boolean flag = true;
		List<String> executeSql = new ArrayList<String>();

		String sql="INSERT INTO JM_ROLEMENU VALUES(?,?,?)" ;
		List param = new ArrayList<>();
		for (RoleMenu roleMenu : list) {
			RoleMenu rm = getRoleMenu(roleMenu);
			if(rm == null){
				param.add(roleMenu.getJsdh());
				param.add(roleMenu.getCxdh());
				param.add(roleMenu.getParentid());
			}
		}
		if(executeSql.size()<=0){
			return false ;
		}
		int[] result = this.jdbcTemplate.batchUpdate(sql,param);
		for (int j = 0; j < result.length; j++) {
			if(result[j]<0){
				// 第i+1条数据执行失败，只要有一条数据执行失败，则结果为失败
				flag = false ;
			}
		}
		return flag;
	}
	
	public List<RoleMenu> getRoleMenuList(String jsdh) throws Exception {
		/*
		String sql = "SELECT JSDH,CXDH,PARENTID FROM JM_ROLEMENU WHERE JSDH = " + jsdh ;
		return this.queryList(sql, RoleMenu.class);
		*/
		String sql = "SELECT JSDH,CXDH,PARENTID FROM JM_ROLEMENU WHERE JSDH = ?";
		return this.queryForList(sql,new Object[]{jsdh}, RoleMenu.class);
	}
	
	public List<RoleMenu> queryParenRoleMenu(RoleMenu roleMenu)
			throws Exception {
		String sql =
				"SELECT JSDH,CXDH,PARENTID FROM JM_ROLEMENU  WHERE JSDH=? AND PARENTID= ?";
		List<RoleMenu> list = this.queryList(sql, RoleMenu.class,roleMenu.getJsdh(),roleMenu.getCxdh());
		return list;
	}

	public int addRoleMenu(RoleMenu roleMenu) throws Exception {
		RoleMenu rm = getRoleMenu(roleMenu);
		if(rm == null){
			RoleMenu temp = (RoleMenu)roleMenu.clone();
			temp.setCxdh(roleMenu.getParentid());
			RoleMenu prm = getRoleMenu(temp);
			if(prm == null){
				String sql1 = "INSERT INTO JM_ROLEMENU VALUES(?, ?, ?)" ;
				this.jdbcTemplate.update(sql1,new Object[]{temp.getJsdh(),temp.getCxdh(),temp.getParentid()});
			}
			String sql = "INSERT INTO JM_ROLEMENU VALUES(?, ?, ?)" ;
			int result = this.jdbcTemplate.update(sql,new Object[]{roleMenu.getJsdh(),roleMenu.getCxdh(),roleMenu.getParentid()});
			return result ;
		}
		return 0;
	}

	public int deleteRoleMenu(RoleMenu roleMenu) throws Exception {
		int result = 0;
		RoleMenu rm = getRoleMenu(roleMenu);
		if(rm != null){
			String sql = "delete from JM_ROLEMENU where jsdh = ? and cxdh = ?" ;
			List<RoleMenu> list = queryParenRoleMenu(roleMenu);
			if(list.size() > 0){
				String sql2 = "delete from JM_ROLEMENU where jsdh = ? and parentid = ?" ;
				result = this.jdbcTemplate.update(sql2,new Object[]{roleMenu.getJsdh(),roleMenu.getCxdh()});
			}
			result = this.jdbcTemplate.update(sql,new Object[]{roleMenu.getJsdh(),roleMenu.getCxdh()});
		}
		return result;
	}

	public List<String> queryMenusByRole(String yhdh) throws Exception {
		String sql="select distinct rm.cxdh from jm_rolemenu rm  where exists(select r.jsdh,r.yhdh from jm_userrole r where r.yhdh=? and r.jsdh=rm.jsdh)";
		return this.jdbcTemplate.queryForList(sql, String.class, new Object[]{yhdh});
	}
	
}
