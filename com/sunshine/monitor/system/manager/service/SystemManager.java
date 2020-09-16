package com.sunshine.monitor.system.manager.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import java.util.Map;

public interface SystemManager {

	public int loadDirect() throws Exception;

	public List getCode(String code) throws Exception;

	public abstract int loadUser() throws Exception;

	public abstract int loadDepartment() throws Exception;

	public abstract int loadCode() throws Exception;

	public abstract int loadPrefecture() throws Exception;

	public abstract String getParameter(String paramString,
			HttpServletRequest paramHttpServletRequest);

	public abstract String getParameter(String paramString1,
			String paramString2, HttpServletRequest paramHttpServletRequest);

	public abstract SysUser getUser(String paramString) throws Exception;

	public abstract String getUserName(String paramString) throws Exception;

	public abstract List getUsers(String paramString) throws Exception;

	public abstract List getDepartments() throws Exception;

	public abstract Department getDepartment(String paramString)
			throws Exception;

	public abstract String getDepartmentName(String paramString)
			throws Exception;

	public abstract List<Code> getCodes(String paramString) throws Exception;

	public abstract Code getCode(String paramString1, String paramString2)
			throws Exception;

	public abstract String getCodeValue(String paramString1, String paramString2)
			throws Exception;

	public abstract List<Code> getCodesByDmsm(String paramString1,
			String paramString2, int paramInt) throws Exception;

	public abstract String getSysDate(String paramString, boolean paramBoolean)
			throws Exception;

	public List<AdministrativeDivision> queryAdministrativeAdvisions()
			throws Exception;

	public List<AdministrativeDivision> queryCityAdministrativeAdvisions(
			String cityname) throws Exception;

	public List getDistricts(String glbm) throws Exception;

	public List getDistrictsByCity(String city) throws Exception;

	public String getCodeValue(String dmlb, String dmz, int position)throws Exception;
	/**
	 * 根据行政区划代码获取行政区划名称
	 * 
	 * @param xzqhdm
	 * @return
	 * @throws Exception
	 */
	public String getDistrictNameByXzqh(String xzqhdm) throws Exception;

	public List getRoadsByFilter(String xzqh) throws Exception;

	public List getCrossingByFilter(String xzqh, String dldm) throws Exception;

	public List getTrafficDepartment(String xzqh) throws Exception;

	public List<Map<String, Object>> getDirects() throws Exception;

	public void updateStatSystem(StatSystem ss);
	
	public void loadGateTree() throws Exception;
	
	public void loadGateTreeFilterDirect() throws Exception;
	
	/**
	 * 更新访问总数
	 * @return
	 * @throws Exception
	 */
    public int updateVisitTotal() throws Exception;
    
	/**
	 * 获取行政区划树
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getXzqhTree(String dwdm) throws Exception;
	
	public List getCityList() throws Exception;
	
	public List getKd(String dwdm) throws Exception;
	
	public String getBaseConnectionState(String ds)throws Exception;
	
	public List getCodeByDmlb(String dmlb,String subsql)throws Exception;

}
