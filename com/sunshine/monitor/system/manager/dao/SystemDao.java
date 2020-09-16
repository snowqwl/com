package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Crossing;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.District;
import com.sunshine.monitor.system.manager.bean.Road;
import com.sunshine.monitor.system.manager.bean.SysUser;

public interface SystemDao extends BaseDao {

	public String getCodeValuenew(String dmlb, String dmz) throws Exception;
	
	/**
	 * 从数据库加载
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List getCode(String code)throws Exception;
	
	/**
	 * 内存加载
	 * @param dmlb
	 * @return
	 * @throws Exception
	 */
	public List<Code> getCodes(String dmlb) throws Exception;
	
	public List getCodesByDmsm(String dmlb, String dmsm, int position)throws Exception;
	
	public String getCodeValue(String dmlb, String dmz, int dmsm)throws Exception;
	
	public String getCodeValue(String paramString1, String paramString2) throws Exception;
	
	public Map getGateByKDBH(String kdbh);
	
	public List getAllGate();
	
	public Code getCode(String paramString1, String paramString2) throws Exception;
	
	public String getDepartmentName(String paramString) throws Exception;
	
	public List<String> getCityNames(String[] dwdm) throws Exception;

	public int loadUser() throws Exception;
	
	public void addUser(SysUser su) throws Exception;

	public int loadDepartment() throws Exception;
	
	public void addDepartment(Department d) throws Exception;

	public int loadCode() throws Exception;

	public void addCode(Code code) throws Exception;
	
	public int loadPrefecture() throws Exception;

	public String getParameter(String paramString,
			HttpServletRequest paramHttpServletRequest);

	public String getParameter(String paramString1,
			String paramString2, HttpServletRequest paramHttpServletRequest);

	public SysUser getUser(String paramString) throws Exception;

	public String getUserName(String paramString) throws Exception;

	public List getUsers(String paramString) throws Exception;

	public List getDepartments() throws Exception;

	public Department getDepartment(String paramString)
			throws Exception;

	public String getDepartmentKey(String paramString)
			throws Exception;

	public String getSysDate(String paramString, boolean paramBoolean)
			throws Exception;

	public String getCsys(String paramString) throws Exception;

	/**
	 * 查询所有行政区
	 * @param subConditions
	 * @return
	 * @throws Exception
	 * @author OY
	 */
	public List<AdministrativeDivision> queryAdministrativeDivisions(String subConditions) throws Exception;
	
	/**
	 * 查询某城市的行政区
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public List<AdministrativeDivision> queryCityAdministrativeAdvisions(
			String cityname) throws Exception;
	
	public List<District> getDistricts(String glbm) throws Exception;
	
	/**
	 * 根据行政区划代码获取行政区划名称
	 * @param xzqhdm
	 * @return
	 * @throws Exception
	 */
	public String getDistrictNameByXzqh(String xzqhdm) throws Exception;
	
	public List<District> getDistrictsByCity(String city) throws Exception;
	
	public List<Road> getRoadsByFilter(String xzqh) throws Exception;
	
	public List<Crossing> getCrossingByFilter(String xzqh,String dldm) throws Exception;
	
	public List getTrafficDepartment(String xzqh) throws Exception;
	
	public String getTrafficDepartmentName(String dwdm)throws Exception;
	
	public void updateStatSystem(StatSystem ss);
	
	/**
	 * 加载方向
	 * @return
	 * @throws Exception
	 */
	public int loadDirect()  throws Exception;

	public List<Map<String,Object>> getDirects() throws Exception;
	
	public List getGateTree(String jb) throws Exception;

	public void loadGateTree() throws Exception;
	
	public void loadGateTreeFilterDirect() throws Exception;
	
	/**
	 * 更新访问数
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
	
	public String getBaseConnectionState(String ds)throws Exception;
	
	public List getCodeByDmlb(String dmlb,String subsql)throws Exception;
	
}
