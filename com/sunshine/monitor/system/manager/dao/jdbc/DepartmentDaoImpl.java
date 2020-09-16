package com.sunshine.monitor.system.manager.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.CallableStatementCallbackImpl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.dao.DepartmentDao;

@Repository("departmentDao")
public class DepartmentDaoImpl extends BaseDaoImpl implements DepartmentDao {

	public Map<String, Object> findDepartmentForMap(Map filter,Department department) throws Exception {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(department.getJb())){
			sql.append("  and jb='").append(department.getJb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(department.getGlbm())){
			sql.append("  and glbm like '%").append(department.getGlbm()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(department.getBmmc())){
			sql.append(" and bmmc like '%").append(department.getBmmc()).append("%' ");
		}
		
		StringBuffer sb = new StringBuffer("select glbm,bmmc,jb,jb_tb.dmsm1  as bmjb,de.lxdh1||'  '||de.lxdh2||' '||de.lxdh3||'  '||de.lxdh4||'  '||de.lxdh5  as lxdh ,zt_tb.dmsm1 as bmzt  from frm_department de,");
		sb.append(" (select dmlb,dmz,dmsm1,dmsm2,dmsm3,dmsm4,dmsx,zt from frm_code  where dmlb='000001')jb_tb, ");
		sb.append(" (select dmlb,dmz,dmsm1,dmsm2,dmsm3,dmsm4,dmsx,zt from frm_code  where dmlb='100010')zt_tb ");
		sb.append(" where de.jb = jb_tb.dmz and de.zt = zt_tb.dmz ");
		sb.append(" and glbm in (select xjjg  from frm_prefecture  where dwdm in(select xjjg from frm_prefecture where dwdm = '"+filter.get("dwdm").toString()+"')) ");
		sb.append(sql).append(" order by glbm ");
		
		return this.findPageForMap(sb.toString(), 
				Integer.parseInt(filter.get("curPage").toString()), 
				Integer.parseInt(filter.get("pageSize").toString()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.sunshine.monitor.system.manager.dao.DepartmentDao#getPrefecture()
	 * 获取当前登录用户所在部门的所有下级单位
	 */
	public List getPrefecture(Department department) throws Exception {
		StringBuffer sb = null;
		if(StringUtils.isBlank(department.getGlbm())){
			sb = new StringBuffer("select *  from frm_department  where glbm in(select xjjg  from frm_prefecture  where dwdm in(select csz  from frm_syspara where gjz = 'xzqh'))  order by glbm ");
			return this.queryList(sb.toString(), Department.class);
		}else{
			sb = new StringBuffer("select *  from frm_department  where glbm in(select xjjg  from frm_prefecture  where dwdm = ?) order by glbm ");
			return this.queryForList(sb.toString(), new Object[]{department.getGlbm()}, Department.class);
		}
	}

	public Department getDepartment(String glbm) throws Exception {
		
		Department dp = null;
		if(StringUtils.isBlank(glbm))
			return null;
	
		String sql = "select f.GLBM,f.BMMC,f.JB,f.SJBM,f.YZMC,f.LXDZ,f.LXDH1,f.LXDH2,f.LXDH3,f.LXDH4,f.LXDH5,f.LXCZ,f.YZBM,f.LXR,f.JYRS,f.XJRS,f.SYJGDM,f.PY,f.BMLX,f.SFLSJG,f.SSJZ,f.ZT,f.GXSJ,f.BZ,f.SFZS, d.bmmc as sjbmmc "
				+ " from (select GLBM,BMMC,JB,SJBM,YZMC,LXDZ,LXDH1,LXDH2,LXDH3,LXDH4,LXDH5,LXCZ,YZBM,LXR,JYRS,XJRS,SYJGDM,PY,BMLX,SFLSJG,SSJZ,ZT,GXSJ,BZ,SFZS from frm_department where glbm = ?) f left join frm_department d on f.sjbm = d.glbm";
		List list = this.queryForList(sql, new Object[]{glbm}, Department.class);
		
		if(list.size() > 0)
			dp = (Department)list.get(0);
		
		return dp;
	}

	
	public int saveDepartment(Department d) throws Exception {
		
		int result = 0;
		StringBuffer sb = new StringBuffer();
		
		String sql = "SELECT count(*)  from frm_department where glbm = '"+d.getGlbm()+"'";
		int r = this.jdbcTemplate.queryForObject(sql, Integer.class);
		
		if(r >0){
			sb.append(" UPDATE frm_department set ");
			sb.append(" bmmc = '").append(d.getBmmc()).append("' ");
			if(d.getBz() != null)sb.append(",bz='").append(d.getBz()).append("' ");
			if(d.getJb() != null)sb.append(",jb='").append(d.getJb()).append("' ");
			if(d.getSjbm() != null)sb.append(",sjbm='").append(d.getSjbm()).append("' ");
			if(d.getYzmc() != null)sb.append(",yzmc='").append(d.getYzmc()).append("' ");
			if(d.getLxdz() != null)sb.append(",lxdz='").append(d.getLxdz()).append("' ");
			if(d.getLxdh1() != null)sb.append(",lxdh1='").append(d.getLxdh1()).append("' ");
			if(d.getLxdh2() != null)sb.append(",lxdh2='").append(d.getLxdh2()).append("' ");
			if(d.getLxdh3() != null)sb.append(",lxdh3='").append(d.getLxdh3()).append("' ");
			if(d.getLxdh4() != null)sb.append(",lxdh4='").append(d.getLxdh4()).append("' ");
			if(d.getLxdh5() != null)sb.append(",lxdh5 ='").append(d.getLxdh5()).append("' ");
			if(d.getLxcz() != null)sb.append(",lxcz='").append(d.getLxcz()).append("' ");
			if(d.getYzbm() != null)sb.append(",yzbm='").append(d.getYzbm()).append("' ");
			if(d.getLxr() != null)sb.append(",lxr='").append(d.getLxr()).append("' ");
			if(d.getJyrs() != null)sb.append(",jyrs='").append(d.getJyrs()).append("' ");
			if(d.getXjrs() != null)sb.append(",xjrs='").append(d.getXjrs()).append("' ");
			if(d.getSyjgdm() != null)sb.append(",syjgdm='").append(d.getSyjgdm()).append("' ");
			if(d.getPy() != null)sb.append(",py='").append(d.getPy()).append("' ");
			if(d.getBmlx()!=null)sb.append(",bmlx ='").append(d.getBmlx()).append("' ");
			if(d.getSflsjg() != null)sb.append(",sflsjg='").append(d.getSflsjg()).append("' ");
			if(d.getSsjz() != null)sb.append(",ssjz='").append(d.getSsjz()).append("' ");
			if(d.getZt() != null)sb.append(",zt='").append(d.getZt()).append("' ");
			sb.append(",gxsj=sysdate  ");
			sb.append(" where glbm = '").append(d.getGlbm()).append("' ");
		}else{
			sb.append("INSERT Into frm_department (bz,glbm,bmmc,jb,sjbm,yzmc,lxdz,lxdh1,lxdh2,lxdh3,lxdh4,lxdh5,");
			sb.append("lxcz,yzbm,lxr,jyrs,xjrs,syjgdm,py,bmlx,sflsjg,ssjz,zt,gxsj)");
			sb.append(" Values('");
			sb.append(d.getBz()==null?"":d.getBz()).append("','").append(d.getGlbm()).append("','").append(d.getBmmc()).append("','")
			.append(d.getJb()).append("','").append(d.getSjbm()).append("','").append(d.getYzmc()).append("','")
			.append(d.getLxdz()).append("','").append(d.getLxdh1()).append("','").append(d.getLxdh2()).append("','")
			.append(d.getLxdh3()).append("','").append(d.getLxdh4()).append("','").append(d.getLxdh5()).append("','")
			.append(d.getLxcz()).append("','").append(d.getYzbm()).append("','")
			.append(d.getLxr()).append("','").append(d.getJyrs()).append("','").append(d.getXjrs()).append("','")
			.append(d.getSyjgdm()==null?"":d.getSyjgdm()).append("','")
			.append(d.getPy()==null?"":d.getPy()).append("','").append(d.getBmlx()).append("','")
			.append(d.getSflsjg()).append("','")
			.append(d.getSsjz()).append("','").append(d.getZt()).append("',sysdate");
			sb.append(") ");
		}
		try {
			result = this.jdbcTemplate.update(sb.toString());
		}catch(Exception e){
			throw e;
		}
		return result;
	}

	public int getUserCountForDepartment(String glbm) throws Exception {
		String sql = "select count(*) c from frm_sysuser  where glbm = '" + glbm + "'";
		
		return this.jdbcTemplate.queryForInt(sql);
	}

	public int removeDepartment(String glbm) throws Exception {
		String sql = " DELETE from frm_department Where glbm = ?";
		int result = 0;
		
		try {
			result = this.jdbcTemplate.update(sql, new Object[]{glbm});
		} catch (Exception e) {
			throw e;
		}
		
		
		return result;
	}

	public List getDepartmentListForGlbm(String glbm,String sqlCon) throws Exception {
		
		String sql = "select *  from frm_department  where glbm like '"+glbm+"%'  and zt='1'  " ;
		if(sqlCon != null) sql += sqlCon;
		
		return this.queryForList(sql, Department.class);
	}

	/***
	 * 获取上级部门集合
	 */
	public List<Department> getHigherDepartmentList(String glbm) throws Exception {
		String sql = "select *  from frm_department  where glbm in(select dwdm  from frm_prefecture where xjjg = '"
					+ glbm +"')";
		
		return this.queryForList(sql, Department.class);
	}
	
	/**
	 * 同步警综部门
	 * @return
	 * @throws Exception
	 */
	public int SyncDepartment() throws Exception {
		String callString = "{call DC_SYNCHRONIZATION_PKG.getDepartments(?)}";
		CallableStatementCallbackImpl callBack = new CallableStatementCallbackImpl() {
			public Object doInCallableStatement(CallableStatement cstmt)
					throws SQLException, DataAccessException {
				cstmt.registerOutParameter(1, 2);
				cstmt.execute();
				int iResult = cstmt.getInt(1);
				return new Integer(iResult);
			}
		};
		callBack.setParameterObject(null);
		Integer i = (Integer) this.jdbcTemplate.execute(callString, callBack);
		return i.intValue();
	}

	@Override
	public List<Department> SyncKcDepartment(String kssj) throws Exception {
		String sql = "select t.org_id as glbm, t.org_name as bmmc, t.org_level as jb, "
				+ "t.parent_id as sjbm,t.lastupdatetime as gxsj from v_org t  where "
				+ "t.lastupdatetime > to_date(?,'yyyy-mm-dd hh24:mi:ss') and "
				+ "t.lastupdatetime <= sysdate order by t.lastupdatetime desc";
		List<Department> list = this.queryForList(sql, new Object[]{kssj}, Department.class);
		return list;
	}
	
	public List<Department> getKcBmmc(String userId) throws Exception {
		List<Department> list = new ArrayList<Department>();
		 String sql = "select code as glbm, org_name as bmmc from v_org where org_id = "
		 		+ "(select t1.org_id from v_sm_user t,v_org_user t1 where t.user_id = "
		 		+ "t1.user_id and t.user_name ='" + userId + "')";
		list = this.queryForList(sql, Department.class);
		return list;
	}
}
