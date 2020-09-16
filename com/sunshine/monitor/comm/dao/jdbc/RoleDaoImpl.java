package com.sunshine.monitor.comm.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.dao.RoleDao;

@Repository
public class RoleDaoImpl extends BaseDaoImpl implements RoleDao {

	public Role getRoleById(String jsdh)throws Exception {
		String sql = "SELECT JSDH, JSMC, JSSX, JSJB, BZ,TYPE,PX FROM JM_ROLE WHERE JSDH = ?";
		//System.out.println(sql);
		return queryList(sql, Role.class,jsdh).get(0);
	}

	public Map<String, Object> findRoleListByPage(Map<?, ?> condition)
			throws Exception {
		List param = new ArrayList<>();
		StringBuffer sql = new StringBuffer(
				"SELECT JSDH, JSMC, JSSX, JSJB, BZ, TYPE,PX FROM JM_ROLE WHERE 1=1 ");
		if (condition.get("jsdh") != null && !"".equals(condition.get("jsdh"))) {
			sql.append(" and JSDH = ?");
			param.add(condition.get("jsdh"));
		}
		if (condition.get("jsmc") != null && !"".equals(condition.get("jsmc"))) {
			sql.append(" and JSMC like ?");
			param.add("%"+condition.get("jsmc")+"%");

		}
		if (condition.get("cxdh") != null && !"".equals(condition.get("cxdh"))) {
			String[] cd = condition.get("cxdh").toString().split(",");
			sql.append(" and jsdh in (select jsdh from jm_rolemenu where cxdh in(?) group by jsdh having count(1) = ?)");
			param.add(condition.get("cxdh"));
			param.add(cd.length);
		}
		sql.append(" and jsdh<>100 and type<>100 ");
		sql.append(" order by ? ?");
		param.add(condition.get("sort"));
		param.add(condition.get("order"));
//		System.out.println(sql);
		Object[] array = param.toArray(new Object[param.size()]);
		return findPageForMap(sql.toString(), array, Integer.parseInt(condition.get(
				"page").toString()), Integer.parseInt(condition.get("rows")
				.toString()));
	}

	public boolean insertRole(final Role role) throws Exception {
		String sql = "INSERT INTO JM_ROLE(JSDH,JSMC,JSSX,JSJB,BZ,TYPE,PX) VALUES(ROLE_JSDH_SEQ" +
				".NEXTVAL,?,?,?,?,?)";
		int result = this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, role.getJsmc());
				ps.setString(2, role.getJssx());
				ps.setString(3, role.getJsjb());
				ps.setString(4, role.getBz());
				ps.setString(5, role.getType());
				ps.setString(6, role.getPx());
			}
		});
		boolean flag=(result>0)?true:false;
		return flag;
	}
	
	/**
	 * 需扩展(关联表删除)
	 */
	/*
	 * 2018-8-13，角色与用户关联时，建议不允许删除
	 */
	public boolean batchDeleteRoles(final String[] jsdhs) throws Exception {
		boolean flag = true ;

		for (String jsdh : jsdhs) {
			String sql="select count(1) from jm_userrole t where t.jsdh='"+jsdh+"'";
			int x = this.jdbcTemplate.queryForInt(sql);
			if(x!=0) {
				return false;
			}
		}
		int i = 0 ;
		List param = new ArrayList<>();
		String sql="DELETE FROM JM_ROLE WHERE JSDH = ?";
		for (String jsdh : jsdhs) {
			param.add(jsdh);
			i = i + 1 ;
		}
		int[] result = this.jdbcTemplate.batchUpdate(sql,param);
		for (int j = 0; i < result.length; j++) {
			if(result[j]<0){
				// 第i+1条数据执行失败，只要有一条数据执行失败，则结果为失败
				flag = false ;
			}
		}
		return flag ;
	}

	public boolean updateRole(final Role role) throws Exception {
		boolean flag = false ;
		String sql = "UPDATE JM_ROLE SET JSMC = ?,JSSX = ?,JSJB = ?,BZ = ?,TYPE = ?,PX = ? WHERE " +
				"JSDH = ?";
		int result = this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1,role.getJsmc());
				ps.setString(2,role.getJssx());
				ps.setString(3,role.getJsjb());
				ps.setString(4,role.getBz());
				ps.setString(5,role.getType());
				ps.setString(6,role.getPx());
				ps.setString(7,role.getJsdh());
			}
		}) ;
		if(result >= 1) {
			flag = true ;
		}
		return flag ;
	}

	public List<Role> queryRoles(String subSql) throws Exception {
		String sql = "select * from JM_ROLE where 1=1 ";
		if(!"".equals(subSql)){
			sql = sql + subSql;
		}
		return this.queryForList(sql, Role.class);
	}

	@Override
	public boolean getRoleTreeIsParent(String param) throws Exception {
		// TODO Auto-generated method stub
		boolean b = false;
		String sql=" select JSDH from JM_ROLE  where type=?";
		int count = this.getRecordCounts(sql, 1000,param);

		if(count>0){
			b=true;
		}
		return b;
	}
}
