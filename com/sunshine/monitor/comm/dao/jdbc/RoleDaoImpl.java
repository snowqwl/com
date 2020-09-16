package com.sunshine.monitor.comm.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.dao.RoleDao;

@Repository
public class RoleDaoImpl extends BaseDaoImpl implements RoleDao {

	public Role getRoleById(String jsdh)throws Exception {
		String sql = "SELECT JSDH, JSMC, JSSX, JSJB, BZ,TYPE,PX FROM JM_ROLE WHERE JSDH = '" + jsdh +"'";
		//System.out.println(sql);
		return queryList(sql, Role.class).get(0);
	}

	public Map<String, Object> findRoleListByPage(Map<?, ?> condition)
			throws Exception {
		StringBuffer sql = new StringBuffer(
				"SELECT JSDH, JSMC, JSSX, JSJB, BZ, TYPE,PX FROM JM_ROLE WHERE 1=1 ");
		if (condition.get("jsdh") != null && !"".equals(condition.get("jsdh"))) {
			sql.append(" and JSDH = '").append(condition.get("jsdh")).append("'");
		}
		if (condition.get("jsmc") != null && !"".equals(condition.get("jsmc"))) {
			sql.append(" and JSMC like '%");
			sql.append(condition.get("jsmc"));
			sql.append("%'");
		}
		if (condition.get("cxdh") != null && !"".equals(condition.get("cxdh"))) {
			String[] cd = condition.get("cxdh").toString().split(",");
			sql.append(" and jsdh in (select jsdh from jm_rolemenu where cxdh in(");
			sql.append(condition.get("cxdh"));
			sql.append(") group by jsdh having count(1) = ");
			sql.append(cd.length).append(")");
		}
		sql.append(" and jsdh<>100 and type<>100 ");
		sql.append(" order by ");
		sql.append(condition.get("sort"));
		sql.append(" ");
		sql.append(condition.get("order"));
//		System.out.println(sql);
		return findPageForMap(sql.toString(), Integer.parseInt(condition.get(
				"page").toString()), Integer.parseInt(condition.get("rows")
				.toString()));
	}

	public boolean insertRole(Role role) throws Exception {
		StringBuffer sql = new StringBuffer(50);
		sql.append("INSERT INTO JM_ROLE(JSDH,JSMC,JSSX,JSJB,BZ,TYPE,PX) VALUES(ROLE_JSDH_SEQ.NEXTVAL,'");
		sql.append(role.getJsmc()).append("','");
		sql.append((role.getJssx() == null) ? "" : role.getJssx()).append("','");
		sql.append((role.getJsjb() == null) ? "" : role.getJsjb()).append("','");
		sql.append((role.getBz() == null) ? "" : role.getBz()).append("','");
		sql.append((role.getType() == null) ? "" : role.getType()).append("','");
		sql.append((role.getPx() == null) ? "" : role.getPx()).append("')");
		boolean flag = (this.jdbcTemplate.update(sql.toString())>0)?true:false;
		return flag ;
	}
	
	/**
	 * 需扩展(关联表删除)
	 */
	/*
	 * 2018-8-13，角色与用户关联时，建议不允许删除
	 */
	public boolean batchDeleteRoles(String[] jsdhs) throws Exception {
		boolean flag = true ;
		for (String jsdh : jsdhs) {
			String sql="select count(1) from jm_userrole t where t.jsdh='"+jsdh+"'";
			int x = this.jdbcTemplate.queryForInt(sql);
			if(x!=0) {
				return false;
			}
		}
		String[] sqlArray = new String[jsdhs.length];
		int i = 0 ;
		for (String jsdh : jsdhs) {
			sqlArray[i]="DELETE FROM JM_ROLE WHERE JSDH = " + jsdh;
			i = i + 1 ;
		}
		int[] result = this.jdbcTemplate.batchUpdate(sqlArray);
		for (int j = 0; i < result.length; j++) {
			if(result[j]<0){
				// 第i+1条数据执行失败，只要有一条数据执行失败，则结果为失败
				flag = false ;
			}
		}
		return flag ;
	}

	public boolean updateRole(Role role) throws Exception {
		boolean flag = false ;
		StringBuffer sql = new StringBuffer(20);
		sql.append("UPDATE JM_ROLE SET ");
		sql.append("JSMC = '").append(role.getJsmc()).append("',");
		sql.append("JSSX = '").append(role.getJssx()).append("',");
		sql.append("JSJB = '").append(role.getJsjb()).append("',");
		sql.append("BZ = '").append(role.getBz()).append("', ");
		sql.append("TYPE = '").append(role.getType()).append("', ");
		sql.append("PX = '").append(role.getPx()).append("' ");
		sql.append(" WHERE JSDH = '").append(role.getJsdh()).append("'");
		int result = this.jdbcTemplate.update(sql.toString()) ;
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
		String sql=" select JSDH from JM_ROLE  where type='"+param+"'";
		int count = this.getRecordCounts(sql, 1000);
		if(count>0){
			b=true;
		}
		return b;
	}
}
