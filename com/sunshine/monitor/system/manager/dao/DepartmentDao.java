package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.Department;

public interface DepartmentDao extends BaseDao{

	public Map<String,Object> findDepartmentForMap(Map filter,Department department)throws Exception; 
	
	/**
	 * 
	 * 函数功能说明：获取部门下级单位集合
	 * 修改日期 	2013-8-20
	 * @param department
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	public List getPrefecture(Department department)throws Exception;
	
	public Department getDepartment(String glbm)throws Exception;
	
	public int saveDepartment(Department department)throws Exception;
	
	public int getUserCountForDepartment(String glbm)throws Exception;
	
	public int removeDepartment(String glbm)throws Exception;
	
	/**
	 * 
	 * 函数功能说明:查询部门代码 LIKE GLBM% 的部门集合
	 * 修改日期 	2013-6-25
	 * @param sqlCon : 查询语句的条件，可为空。
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	public List getDepartmentListForGlbm(String glbm,String sqlCon)throws Exception;
	
	/**
	 * 
	 * 函数功能说明：获取部门上级单位集合
	 * 修改日期 	2013-8-20
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	public List<Department> getHigherDepartmentList(String glbm)throws Exception;
	
	/**
	 * 同步警综部门
	 * @return
	 * @throws Exception
	 */
	public int SyncDepartment() throws Exception;
	
	/**
	 * 同步科创机构表到系统部门表
	 * @return
	 * @throws Exception
	 */
	public List<Department> SyncKcDepartment(String kssj) throws Exception;
	
	/**
	 * 获取科创部门名称
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public List<Department> getKcBmmc(String userId) throws Exception;
}
