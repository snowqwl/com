package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.manager.bean.Department;

public interface DepartmentManager {

	public Map<String, Object> findDepartmentForMap(Map filter,Department department) throws Exception;
	
	public List getPrefecture(Department department)throws Exception;
	
	public Department getDepartment(String glbm)throws Exception;
	
	/**
	 * 
	 * 函数功能说明：保存新增和修改部门
	 * 修改日期 	2013-6-25
	 * @param department
	 * @param modal 
	 * @return
	 * @throws Exception    
	 * @return int
	 */
	public int saveDepartment(Department department)throws Exception;
	
	/**
	 * 
	 * 函数功能说明:实现保存功能
	 * 修改日期 	2013-6-26
	 * @param department
	 * @param modul
	 * @throws Exception    
	 * @return void
	 */
	public void saveUnit(Department department,String modul)throws Exception;
	
	public int getUserCountForDepartment(String glbm)throws Exception;
	
	public int removeDepartment(String glbm)throws Exception;
	
	/**
	 * 部门编号-->部门名称
	 * @param glbm
	 * @return
	 * @throws Exception
	 * @author OUYANG
	 */
	public String getDepartmentName(String glbm)throws Exception;
	
	/**
	 *同步警综部门数据
	 * @return
	 * @throws Exception
	 */
	public abstract int SyncDepartment() throws Exception;
	
	/**
	 * 获取科创部门名称
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Department> getKcBmmc(String userId) throws Exception;
}
